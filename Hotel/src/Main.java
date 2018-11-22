import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import java.util.Scanner;

public class Main{
	 public static void main (String [] args)
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
	    			System.out.println("\n\n                                 ----- Menu -----\n\n"
	    					+ "1.  Make a reservation                       10. Update payment using reservation number\n"
	    					+ "2.  Change rooms                             11. List guests who owe money\n"
	    					+ "3.  Checkout                                 12. List reservations for room \n"
	    					+ "4.  Cancel reservation                       13. Get guest by room number & date\n"
	    					+ "5.  Update reservation                       14. List guests arriving on a specific date\n"
	    					+ "6.  List Empty rooms and their costs         15. List guests leaving on a specific date\n"
	    					+ "7.  List all the reservations for a guest    16. Register a new guest (** basic function **)\n"
	    					+ "8.  Delete room                              17. \n"
	    					+ "9.  Pay for room using reservation ID        18. \n\n"
	    					+ "* Enter \"exit\" to exit the system\n"
	    					);
	    			System.out.println("Please enter the number of function from above:\n");
	    			Scanner scanner = new Scanner(System. in); 
	    			String input = scanner. nextLine();
	    			
	    	        switch (input) { 
	    	        case "1": 
	    	            System.out.print("<====== Making a reservation ======>");
	    	            c.makeReservation();
	    	            break; 
	    	        case "2": 
	    	             
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
	    	             
	    	            break;
	    	        case "9": 
	    	             
	    	            break;
	    	        case "10": 
	    	             
	    	            break;
	    	        case "11": 
	    	        	c.ListGuestsThatOweMoney();
	    	             
	    	            break;
	    	        case "12": 
	    	             
	    	            break;
	    	        case "13": 
	    	             
	    	            break;
	    	        case "14": 
	    	             
	    	            break;
	    	        case "15": 
	    	             
	    	            break;
	    	        case "16": 
	    	             
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