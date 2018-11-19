import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

public class Main{
	 public static void main (String [] args)
	 {
	        DataSource ds = DataSourceFactory.getMySQLDataSource();     
	       
	        Connection connection =  null; 
	        try {
	    		connection = ds.getConnection(); 
	    	} catch (SQLException e) {
	    		System.out.println("Connection Failed! Check output console");
	    		e.printStackTrace();
	    		return;
	    	}
	     
	    	if (connection != null) {
	    		System.out.println("You made it, take control your database now!");
		    	Connector c = new Connector(connection);
		    	c.ListGuestsThatOweMoney();
		    	c.ListTakenRooms();
		    	c.ListReservations();
	    	} else {
	    		System.out.println("Failed to make connection!");
	    	}

	    	
	 }   
}