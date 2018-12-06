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
		    	User user = c.login();
		    	
		    	System.out.println("\nHi, " + user.getFirst_name() + " " + user.getLast_name());
		    	
		    	if (user.isAdmin()) {
		    		System.out.println("Your user type: admin user.");
		    		while (isOn) {
		    			Menu m = new Menu();
		    			m.printAdminMenu();
		    			System.out.println("Please enter the number of function from above:");
		    			Scanner scanner = new Scanner(System. in); 
		    			String input = scanner.nextLine();
		    			
		    	        switch (input) { 
		    	        case "1": 
		    	            c.makeReservationAsAdmin();
		    	            break; 
		    	        case "2": 
		    	             c.changeRoom();
		    	            break;
		    	        case "3": 
		    	            c.checkout(); 
		    	            break;
		    	        case "4": 
		    	            c.cancelReservation(); 
		    	            break;
		    	        case "5": 
		    	            c.updateReservation(); 
		    	            break;
		    	        case "6": // nick
		    	        	c.ListAvailableRoomsByDay();
		    	            break;
		    	        case "7": //nick
		    	        	c.listReservationsByGuest();
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
		    	        case "12": //nick
		    	        	c.listReservationsByRoom();
		    	            break;
		    	        case "13": 
		    	             c.listGuestByRoomNoAndDate();
		    	            break;
		    	        case "14": 
		    	            c.listGuestsOnArriveDate();
		    	            break;
		    	        case "15": 
		    	            c.listGuestsOnCheckoutDate();
		    	            break;
		    	        case "16": 
		    	            c.registerRegualUser();
		    	            break;
		    	        case "17": 
		    	        	c.registerAdminUser();
		    	            break;
		    	        case "18": 
		    	            c.searchAdminUser();
		    	            break;
		    	        case "19":
		    	        	c.archiveReservations();
		    	        	break;
		    	        case "20":
		    	        	c.listAllCoustomersWhoBookedRooms();
		    	        	break;
		    	        case "exit": 
		    	            System.out.println("Thank you. See you.");
		    	            isOn = false;
		    	            break;
		    	        default: 
		    	            System.out.println("Please Enter the number from menu");
		    	            break; 
		    	        }
		    	        
//				    	c.ListTakenRooms();
//				    	c.ListReservations();
		    		}
		    	}else {
		    		
		    		//regular user
		    		System.out.println("Your user type: regular user.");
		    		while (isOn) {
		    			Menu m = new Menu();
		    			m.printGuestMenu();
		    			System.out.println("Please enter the number of function from above:");
		    			Scanner scanner = new Scanner(System. in); 
		    			String input = scanner.nextLine();
		    			
		    	        switch (input) { 
		    	        case "1": 
		    	            c.makeReservation();
		    	            break; 
		    	        case "2": 
		    	            c.listMyReservation();
		    	            break;
		    	        case "3": 
		    	        	//Update payment using reservation number
		    	        	c.makePayment();
		    	            break;
		    	        case "4":
		    	        	c.updatePayment(); 
		    	            break;
		    	        case "exit": 
		    	            System.out.println("Thank you. See you.");
		    	            isOn = false;
		    	            break;
		    	        default: 
		    	            System.out.println("Please Enter the number from menu");
		    	            break; 
		    	        }
		    	        
//				    	c.ListTakenRooms();
//				    	c.ListReservations();
		    		}
		    	}
		    	



	    	} else {
	    		System.out.println("Failed to make connection!");
	    	}

	    	
	 }   
}