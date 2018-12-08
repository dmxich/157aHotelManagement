DROP DATABASE IF EXISTS hotel;
CREATE DATABASE hotel;
USE hotel;

DROP TABLE IF EXISTS `room_availability`;
DROP TABLE IF EXISTS `reservation`;
DROP TABLE IF EXISTS `user`;
CREATE TABLE user (
    user_id            	INTEGER       		not null		AUTO_INCREMENT,
    first_name    	VARCHAR(50)   	not null,
    last_name     	VARCHAR(50)   	not null,
    email    	 	VARCHAR(50)   	not null,
    isAdmin		BOOLEAN		not null		DEFAULT false,
    password		VARCHAR(50)            not null,
    PRIMARY KEY (user_id)
);
ALTER TABLE user MODIFY COLUMN user_id INT NOT NULL AUTO_INCREMENT;

DROP TABLE IF EXISTS `room`;
CREATE TABLE room (
    room_id        INTEGER          not null,
    room_type      VARCHAR(50)      not null,
    tv         	   BOOLEAN	 	    DEFAULT true,
    hot_tub		   BOOLEAN		    DEFAULT false,
    rate           DOUBLE           not null,
    PRIMARY KEY ( room_id )
);

DROP TABLE IF EXISTS `reservation`;
CREATE TABLE reservation (
    reservation_id     INTEGER          not null    AUTO_INCREMENT,
    room_id            INTEGER          not null,
    user_id          INTEGER          not null, 
    phone              VARCHAR(20)      not null,
    arrive             DATE             not null,
    depart             DATE             not null,
    cost               DOUBLE           not null,
    r_status           VARCHAR(50)      not null DEFAULT 'pending',
    payment_due        DATE             not null,
    payment_id         INTEGER          not null,
    FOREIGN KEY ( user_id ) REFERENCES user (user_id),
    FOREIGN KEY ( room_id ) REFERENCES room(room_id),
    PRIMARY KEY ( reservation_id )
);
ALTER TABLE reservation MODIFY COLUMN reservation_id INT NOT NULL AUTO_INCREMENT;

DROP TABLE IF EXISTS `payment`;
CREATE TABLE payment(
   payment_id         INTEGER         not null AUTO_INCREMENT,
   credit_card        VARCHAR(18)     not null,
   expiration_date    DATE            not null,
   amount_due         DOUBLE          not null,
   paid			      BOOLEAN	      DEFAULT false,
   PRIMARY KEY ( payment_id )
);
ALTER TABLE payment MODIFY COLUMN payment_id INT NOT NULL AUTO_INCREMENT;


DROP TABLE IF EXISTS `room_reserved`;
CREATE TABLE room_reserved (
    room_id           	INTEGER       		not null,
    start_date	    	DATE		   	not null,
    end_date	    	DATE		   	not null,
    PRIMARY KEY ( room_id, start_date ),
    FOREIGN KEY ( room_id ) REFERENCES room (room_id)
);


DROP TABLE IF EXISTS `reservationArchive`;
CREATE TABLE reservationArchive (
    reservation_id     INTEGER          not null,
    room_id            INTEGER          not null,
    user_id          INTEGER            not null, 
    phone              VARCHAR(20)      not null,
    arrive             DATE             not null,
    depart             DATE             not null,
    cost               DOUBLE           not null,
    r_status           VARCHAR(50)      not null DEFAULT 'reserved',
    payment_due        DATE             not null,
    payment_id         INTEGER          not null,
    FOREIGN KEY ( user_id ) REFERENCES user (user_id),
    FOREIGN KEY ( room_id ) REFERENCES room(room_id),
    PRIMARY KEY ( reservation_id )
);


DROP TRIGGER IF EXISTS `TResArchive`;
DELIMITER $$
USE `hotel`$$
CREATE TRIGGER TResArchive
BEFORE DELETE ON reservation
FOR EACH ROW
BEGIN
    INSERT INTO reservationArchive
    SELECT r.reservation_id, r.room_id, r.user_id, r.phone, r.arrive, r.depart, r.cost, r.r_status, r.payment_due, r.payment_id
    FROM reservation r 
    WHERE reservation_id = old.reservation_id;

    DELETE FROM room_reserved
    WHERE old.room_id = room_id and old.arrive = start_date;
END $$
DELIMITER ;


DROP TRIGGER IF EXISTS overlapRreservationDates;
DELIMITER //
Create trigger overlapRreservationDates
Before UPDATE on reservation
For each row
BEGIN
IF EXISTS (Select arrive, depart
From reservation
Where (new.arrive <= depart AND new.arrive >= arrive and new.room_id = room_id) OR
(new.depart  >= arrive AND new.depart <= depart AND new.room_ID = room_ID))
THEN
SIGNAL SQLSTATE '45000'
SET MESSAGE_TEXT = 'Date insert conflicts with another date';
END IF;
END; //
DELIMITER ;



DROP procedure IF EXISTS `SPArchiveReservations`;

DELIMITER $$
USE `hotel`$$
CREATE PROCEDURE SPArchiveReservations
(IN in_day DATE)

Begin 
    DELETE FROM reservation 
    WHERE depart < in_day;
END $$
DELIMITER ;



USE `hotel`;
DROP procedure IF EXISTS `spMakeReservation`;

DELIMITER $$
USE `hotel`$$
CREATE DEFINER=`hotel`@`%` PROCEDURE `spMakeReservation`(
IN rm_id INTEGER, 
IN u_id INTEGER, 
IN phone VARCHAR(20), 
IN arrive DATE, 
IN depart DATE, 
IN cost DOUBLE, 
IN r_status VARCHAR(50), 
IN payment_due DATE, 
IN card_number VARCHAR(16),
IN expiration DATE,
IN amount DOUBLE,
IN p_id INTEGER)
BEGIN
	INSERT INTO payment 
	(credit_card, expiration_date, amount_due)
	VALUES
	( card_number, expiration, amount);

    INSERT INTO reservation 
    ( room_id, user_id, phone, arrive, depart, cost, r_status, payment_due, payment_id) 
	VALUES
	(rm_id, u_id, phone, arrive, depart, cost, r_status, payment_due, p_id);
    
    INSERT INTO room_reserved
    ( room_id, start_date, end_date)
    VALUES 
    ( rm_id, arrive, depart);
END$$

DELIMITER ;

USE `hotel`;
DROP procedure IF EXISTS `spMakePayment`;

DELIMITER $$
USE `hotel`$$
CREATE DEFINER=`hotel`@`%` PROCEDURE `spMakePayment`(IN reservationID INTEGER)
BEGIN
UPDATE payment
SET paid = true
WHERE payment.payment_id IN
		    (SELECT payment_id
		    FROM reservation
		    WHERE reservation_id = reservationID);
END$$

DELIMITER ;

USE `hotel`;
DROP procedure IF EXISTS `spUpdatePayment`;

DELIMITER $$
USE `hotel`$$
CREATE DEFINER=`hotel`@`%` PROCEDURE `spUpdatePayment`(IN reservationID INTEGER,
 IN card_number VARCHAR(18),
IN date DATE
) 
BEGIN
UPDATE payment
SET credit_card = card_number, expiration_date = date
WHERE payment.payment_id IN
		    (SELECT P.payment_id
		    FROM reservation P
		    WHERE P.reservation_id = reservationID);
END$$

DELIMITER ;

USE `hotel`;
DROP procedure IF EXISTS `spGuestByRoomNoAndDay`;

DELIMITER $$
USE `hotel`$$
CREATE PROCEDURE spGuestByRoomNoAndDay
(IN in_day DATE, IN room_no INTEGER)
BEGIN
    SELECT user.user_id, user.first_name, user.last_name
    FROM user NATURAL JOIN reservation 
    WHERE reservation.room_id = room_no and in_day BETWEEN reservation.arrive and DATE_SUB(reservation.depart, INTERVAL 1 DAY);
END $$
DELIMITER ;

USE `hotel`;
DROP procedure IF EXISTS `spListAvailableRoomsByDay`;

DELIMITER $$
CREATE PROCEDURE spListAvailableRoomsByDay
(IN in_day DATE)
BEGIN
    SELECT * 
    FROM room 
    WHERE room.room_id not in (SELECT reservation.room_id FROM reservation WHERE  Date(in_day) Between reservation.arrive and DATE_SUB(reservation.depart, INTERVAL 1 DAY));
END $$
DELIMITER ;

USE `hotel`;
DROP procedure IF EXISTS `spListReservationsByRoom`;

DELIMITER $$
USE `hotel`$$
CREATE PROCEDURE spListReservationsByRoom
(IN in_room INTEGER)

Begin 
    Select user.user_id, user.first_name, user.last_name, reservation.arrive, reservation.depart
    FROM  user NATURAL JOIN reservation 
    WHERE reservation.room_id = in_room;
END $$
DELIMITER ;


USE `hotel`;
DROP procedure IF EXISTS `spListReservationsByGuest`;

DELIMITER $$

USE `hotel`$$
CREATE PROCEDURE spListReservationsByGuest
(IN inGuestID INTEGER)
Begin 
    Select reservation.room_id, reservation.arrive, reservation.depart
    FROM reservation 
    WHERE user_id = inGuestID;
END $$

DELIMITER ;

INSERT INTO user (user_id, first_name, last_name, email, isAdmin, password) 
VALUES (10001, 'Jun', 'Ma', 'jun@gmail.com', false, 123456);
INSERT INTO user (user_id, first_name, last_name, email, isAdmin, password) 
VALUES (10002, 'A', 'Admin', 'admin@gmail.com', true, 123456);

INSERT INTO room ( room_id, room_type, tv, hot_tub, rate) 
VALUES 
(101, "single", true, false, 70),
(102, "single", true, false, 70),
(103, "single", true, false, 70),
(104, "double", true, false, 110),
(105, "double", true, false, 110),
(201, "single", true, false, 70),
(202, "single", true, false, 70),
(203, "single", true, false, 70),
(204, "double", true, false, 120),
(205, "double", true, false, 120),
(301, "single", true, false, 70),
(302, "single", true, false, 70),
(303, "single", true, false, 70),
(304, "platinum", true, false, 130),
(305, "platinum", true, false, 130);

INSERT INTO payment (payment_id, credit_card, expiration_date, amount_due, paid)
VALUES (30001, "1234567890123456", "2018-09-01", 460.2, false);

INSERT INTO reservation (reservation_id, room_id, user_id, phone, arrive, depart, cost, r_status, payment_due, payment_id) 
VALUES (20001, 101, 10001, '4151111134', '2018-06-10', '2018-06-12', 460.2, 'reserved', '2018-06-11', 30001);

INSERT INTO room_reserved (room_id, start_date, end_date) 
VALUES (101, '2018-06-10', '2018-06-12');


-- for windows user
LOAD DATA LOCAL INFILE 'c:\\SQL\\user.txt' INTO TABLE user;
LOAD DATA LOCAL INFILE 'c:\\SQL\\payment.txt' INTO TABLE payment;
LOAD DATA LOCAL INFILE 'c:\\SQL\\room_reserved.txt' INTO TABLE room_reserved;
LOAD DATA LOCAL INFILE 'c:\\SQL\\reservation.txt' INTO TABLE reservation;

-- for mac user
-- LOAD DATA LOCAL INFILE '/Users/jun/Desktop/157A/user.txt' INTO TABLE user;
-- LOAD DATA LOCAL INFILE '/Users/jun/Desktop/157A/payment.txt' INTO TABLE payment;
-- LOAD DATA LOCAL INFILE '/Users/jun/Desktop/157A/room_reserved.txt' INTO TABLE room_reserved;
-- LOAD DATA LOCAL INFILE '/Users/jun/Desktop/157A/reservation.txt' INTO TABLE reservation;
