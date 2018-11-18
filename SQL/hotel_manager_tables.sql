DROP TABLE IF EXISTS `room_availability`;
DROP TABLE IF EXISTS `reservation`;
DROP TABLE IF EXISTS `guest`;
CREATE TABLE guest (
    guest_id          INT(15)           not null     AUTO_INCREMENT,
    first_name        VARCHAR(50)       not null,
    last_name         VARCHAR(50)       not null,
    email             VARCHAR(50)       not null,
   PRIMARY KEY (guest_id)
);

DROP TABLE IF EXISTS `room`;
CREATE TABLE room (
    room_id        INT(15)          not null,
    room_type      VARCHAR(50)      not null,
    tv         	   BOOLEAN	 	    DEFAULT true,
    hot_tub		   BOOLEAN		    DEFAULT false,
    PRIMARY KEY ( room_id )
);


CREATE TABLE reservation (
    reservation_id     INT(15)          not null    AUTO_INCREMENT,
    room_id            INT(15)          not null,
    guest_id           INT(15)          not null,
    phone              VARCHAR(20)      not null,
    arrive             DATE             not null,
    depart             DATE             not null,
    cost               DOUBLE(10, 2)    not null,
    reservation_status VARCHAR(50)      not null DEFAULT 'available',
    payment_due        DATE             not null,
    payment_id         INT(15)          not null,
    FOREIGN KEY ( guest_id ) REFERENCES guest(guest_id),
    FOREIGN KEY ( room_id ) REFERENCES room(room_id),
    PRIMARY KEY ( reservation_id )
);
ALTER table reservation auto_increment = 10001;

DROP TABLE IF EXISTS `payment`;
CREATE TABLE payment(
   payment_id         INT(15)         not null,
   credit_card        VARCHAR(18)     not null,
   experation_date    DATE            not null,
   amount_due         DOUBLE(10, 2)   not null,
   paid			      BOOLEAN	      DEFAULT false,
   PRIMARY KEY ( payment_id )
);
ALTER table payment auto_increment = 20001;


DROP TABLE IF EXISTS `room_availability`;
CREATE TABLE room_availability (
    room_id             INT(15)            not null,
    start_date          DATE               not null,
    end_date            DATE               not null,
    FOREIGN KEY ( room_id ) REFERENCES room (room_id)
);


DROP TABLE IF EXISTS `payment`;
CREATE TABLE guest (
guest_ id         INT(15)           not null        AUTO_INCREMENT,
first_name        VARCHAR(50)       not null,
last_name         VARCHAR(50)       not null,
email             VARCHAR(50)       not null,
PRIMARY KEY (guest_id)
);



DROP TRIGGER IF EXISTS CancelReservation;
delimiter //
CREATE  TRIGGER CancelReservationTrigger
AFTER  INSERT ON payment
FOR EACH ROW
WHEN paid = False AND date > payment_due
BEGIN
    UPDATE reservation
    Set status = ‘available’
    WHERE new.reservation_id = reservation_id;
END;

//
delimiter ;


DROP TRIGGER IF EXISTS DeleteReservationTrigger;
delimeter //
CREATE  TRIGGER DeleteReservationTrigger
AFTER  INSERT ON reservation
FOR EACH ROW
WHEN paid = True AND date = depart
BEGIN
    DELETE FROM reservation
    WHERE new.reservation_id = reservation_id;
END;

delimeter ;
