DROP TABLE IF EXISTS `room_availability`;
DROP TABLE IF EXISTS `reservation`;
DROP TABLE IF EXISTS `guest`;
CREATE TABLE guest (
    guest_id         INTEGER           not null,
    first_name        VARCHAR(50)       not null,
    last_name         VARCHAR(50)       not null,
    email             VARCHAR(50)       not null,
   PRIMARY KEY (guest_id)
);

DROP TABLE IF EXISTS `room`;
CREATE TABLE room (
    room_id        INTEGER          not null,
    room_type      VARCHAR(50)      not null,
    tv         	   BOOLEAN	 	    DEFAULT true,
    hot_tub		   BOOLEAN		    DEFAULT false,
    PRIMARY KEY ( room_id )
);


CREATE TABLE reservation (
    reservation_id     INTEGER          not null,
    room_id            INTEGER          not null,
    guest_id          INTEGER          not null, 
    phone              VARCHAR(20)      not null,
    arrive             DATE             not null,
    depart             DATE             not null,
    cost               DOUBLE           not null,
    reservation_status VARCHAR(50)      not null,
    payment_due        DATE             not null,
    payment_id         INTEGER          not null,
    FOREIGN KEY ( guest_id ) REFERENCES guest (guest_id),
    FOREIGN KEY ( room_id ) REFERENCES room(room_id),
    PRIMARY KEY ( reservation_id )
);

DROP TABLE IF EXISTS `payment`;
CREATE TABLE payment(
   payment_id         INTEGER         not null,
   credit_card        VARCHAR(18)     not null,
   experation_date    DATE            not null,
   amount_due         DOUBLE          not null,
   paid			      BOOLEAN	      DEFAULT false,
   PRIMARY KEY ( payment_id )
);



DROP TABLE IF EXISTS `room_availability`;
CREATE TABLE room_availability (
    room_id             INTEGER            not null,
    start_date          DATE               not null,
    end_date            DATE               not null,
    FOREIGN KEY ( room_id ) REFERENCES room (room_id)
);
