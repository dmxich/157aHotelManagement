UPDATE reservation
SET status =  “available”;
WHERE room_is NOT IN (SELECT room_id FROM room_availability 
                      WHERE room_id =reservation.room_id);

