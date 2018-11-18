package DB;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import Java.Reservation;
public class ReserveRoom 
{
	Connection conn;
    PreparedStatement statement = null;
    ResultSet result = null;

    public ReserveRoom() {
    	DataSource ds = DataSourceFactory.getMySQLDataSource();     
        
        Connection connection =  null; 
    }
    
    public void makeReservation(Reservation reserve) 
    {
        
            try {
                String insertQuery = "insert into reservation"
                        + "('reservation_id','room_id','guest_id','arrive','depart','cost','status', 'phone', 'paument_due', paiment_id)"
                        + " values("
                        + 10001
                        + ",'" + reserve.getRoomID() + "'"
                        + "," + reserve.getGuestID() + ""
                        + "," + reserve.getArriveDate() + ""
                        + "," + reserve.getDepartDate() + ""
                        + ",'" + reserve.getCost() + ""
                        + ",'" + reserve.getAvailable() + ""
                        + ",'" + reserve.getPhoneNum() + ""
                        + ",'" + reserve.getPaymentDue() + ""
                        + ",'" + reserve.getPaymentID() 
                        + " )";

                // ^^^ 0 for has_checked_out
                statement = conn.prepareStatement(insertQuery);
                //System.out.println(">>>>>>>>>> " + insertQuery);
                statement.execute();

                System.out.println("successfully inserted new Reservation");

            } catch (SQLException ex) {
                System.out.println("InsertQuery   Failed");
            } finally {
                System.out.println("Inserted");
            }
        

    }
    
    //retrieve all reservations
    public ResultSet getReservationInfo() 
    {
        try {
            String query = "select * from reservation";
            statement = conn.prepareStatement(query);
            result = statement.executeQuery();
        } catch (SQLException ex) {
            System.out.print( "\n error coming from returning all reservations");
        }

        return result;
    }
    
    //retrieve a single reservation
    public ResultSet getAeservation(int reservationId) {
        try {
            String query = "select * from reservation where reservation_id = " + reservationId;
            statement = conn.prepareStatement(query);
            result = statement.executeQuery();
        } catch (SQLException ex) {
            System.out.println( "\n error coming from returning A reservation");
        }

        return result;
    }
    
    public void flushAll() 
    {
        {
            try {
                statement.close();
                result.close();
            } catch (SQLException ex) {
                System.err.print(ex.toString() + " >> CLOSING DB");
            }
        }
    }

    public void flushStatementOnly() 
    {
        {
            try {
                statement.close();
                //conn.close();
            } catch (SQLException ex) {
                System.err.print(ex.toString() + " >> CLOSING DB");
            }
        }
    }
    
    
}
