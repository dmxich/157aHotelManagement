import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import java.util.Scanner;

public class Main{
	 public static void main (String [] args) throws SQLException
	 {
	        DataSource ds = DataSourceFactory.getMySQLDataSource();     
	       
	        Connection connection =  null; 
	        try {
	    		connection = ds.getConnection(); 
	    	} catch (SQLException e) {
	    		System.out.println("Connection Failed! Check output console\n");
	    		e.printStackTrace();
	    		return;
	    	}
	     
	    	if (connection != null) {
	    		System.out.println("*** Welcome to Hotel reservation system ***\n");
	    		boolean isOn = true;
	    		
		    	Connector c = new Connector(connection);
		    	
	    		while (isOn) {
	    			Menu m = new Menu();
	    			System.out.println("Please enter the number of function from above:");
	    			Scanner scanner = new Scanner(System. in); 
	    			String input = scanner. nextLine();
	    			
	    	        switch (input) { 
	    	        case "1": 
	    	            c.makeReservation();
	    	            break; 
	    	        case "2": 
	    	             c.changeRoom();
	    	            break;
	    	        case "3": 
	    	             
	    	            break;
	    	        case "4": 
	    	             
	    	            break;
	    	        case "5": 
	    	             
	    	            break;
	    	        case "6": 
	    	             
	    	            break;
	    	        case "7": 
	    	             
	    	            break;
	    	        case "8": 
	    	            c.deleteRoom(); 
	    	            break;
	    	        case "9":
	    	            c.makePayment();
	    	            break;
	    	        case "10": 
	    	        	//Update payment using reservation number
	    	        	c.updatePayment();
	    	            break;
	    	        case "11": 
	    	        	c.ListGuestsThatOweMoney();
	    	             
	    	            break;
	    	        case "12": 
	    	             
	    	            break;
	    	        case "13": 
	    	             
	    	            break;
	    	        case "14": 
	    	            c.listGuestsOnArriveDate();
	    	            break;
	    	        case "15": 
	    	            c.listGuestsOnCheckoutDate();
	    	            break;
	    	        case "16": 
	    	            c.registerGuest();
	    	            break;
	    	        case "17": 
	    	             
	    	            break;
	    	        case "18": 
	    	             
	    	            break;
	    	        case "exit": 
	    	            System.out.println("Thank you. See you.");
	    	            isOn = false;
	    	            break;
	    	        default: 
	    	            System.out.println("Please Enter the number from menu");
	    	            break; 
	    	        }
	    	        
//			    	c.ListTakenRooms();
//			    	c.ListReservations();
	    		}


	    	} else {
	    		System.out.println("Failed to make connection!");
	    	}

	    	
	 }   
}