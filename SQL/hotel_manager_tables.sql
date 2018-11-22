DROP TABLE IF EXISTS `room_availability`;
DROP TABLE IF EXISTS `reservation`;
DROP TABLE IF EXISTS `guest`;
CREATE TABLE guest (
    guest_id         INTEGER           not null     AUTO_INCREMENT,
    first_name        VARCHAR(50)       not null,
    last_name         VARCHAR(50)       not null,
    email             VARCHAR(50)       not null,
   PRIMARY KEY (guest_id)
);
ALTER TABLE guest MODIFY COLUMN guest_id INT NOT NULL AUTO_INCREMENT;

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
    guest_id          INTEGER          not null, 
    phone              VARCHAR(20)      not null,
    arrive             DATE             not null,
    depart             DATE             not null,
    cost               DOUBLE           not null,
    r_status           VARCHAR(50)      not null DEFAULT 'pending',
    payment_due        DATE             not null,
    payment_id         INTEGER          not null,
    FOREIGN KEY ( guest_id ) REFERENCES guest (guest_id),
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
    FOREIGN KEY ( room_id ) REFERENCES room (room_id),
);


INSERT INTO guest (guest_id, first_name, last_name, email) 
VALUES (10001, 'Jun', 'Ma', 'jun@gmail.com');

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

INSERT INTO reservation (reservation_id, room_id, guest_id, phone, arrive, depart, cost, r_status, payment_due, payment_id) 
VALUES (20001, 101, 10001, '4151111134', '2018-06-10', '2018-06-12', 460.2, 'pending', '2018-06-11', 30001);

INSERT INTO room_reserved (room_id, start_date, end_date) 
VALUES (101, '2018-06-10', '2018-06-12');


