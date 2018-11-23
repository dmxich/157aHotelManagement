import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Calendar;
import java.util.Properties;

import javax.sql.DataSource;

import com.mysql.jdbc.CallableStatement;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.util.Scanner;;
 
class DataSourceFactory {
    public static DataSource getMySQLDataSource() {
        
    	Properties props = new Properties();
        FileInputStream fis = null;
        MysqlDataSource mysqlDS = null;
        try {
            fis = new FileInputStream("db.properties");
            props.load(fis);
            mysqlDS = new MysqlDataSource();
            mysqlDS.setURL(props.getProperty("MYSQL_DB_URL"));
            mysqlDS.setUser(props.getProperty("MYSQL_DB_USERNAME"));
            mysqlDS.setPassword(props.getProperty("MYSQL_DB_PASSWORD"));
        } catch (IOException e) {
        	System.out.println("db.properties is not found");
            e.printStackTrace();
        }
        return mysqlDS;
      
    }
}

public class Connector 
{
	private Connection conn;
	private Statement statement;
	public Connector(Connection connection){
		this.conn = connection;
		try {
			this.statement = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	public void ListGuestsThatOweMoney(){
		ResultSet rs = null;
		System.out.println("getting guests that owe money");
		try {
			rs = statement.executeQuery("SELECT g.first_name, g.last_name, SUM(p.amount_due) as amnt "
					+ "FROM payment p, reservation r , guest g "
					+ "WHERE g.guest_id = r.guest_id and p.payment_id  = r.payment_id and p.amount_due > 0 "
					+ "GROUP BY g.guest_id;");
			printGuestsThatOwe(rs);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	public void printGuestsThatOwe(ResultSet rs) throws SQLException{
		while(rs.next()){
			System.out.println("Name: " + rs.getString("first_name") + " " + rs.getString("last_name") + ", Amount Owed: $ " + rs.getDouble("amnt")); 
		}
	}
	public void ListTakenRooms(){
		ResultSet rs = null;
		System.out.println("Available Rooms");
		try {
			rs = statement.executeQuery("select R.room_id from room R where R.room_id NOT IN (select room_id from room_reserved)");
			printGuestsRoom(rs);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	public void printGuestsRoom(ResultSet rs) throws SQLException{
		while(rs.next()){
			System.out.println("Room Number: " + rs.getString("room_id")); 
		}
	}
	public void ListReservations(){
		ResultSet rs = null;
		System.out.println("Reserved Rooms");
		try {
			rs = statement.executeQuery("select * from reservation NATURAL JOIN guest");
			printReservation(rs);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	public void printReservation(ResultSet rs) throws SQLException{
		while(rs.next()){
			System.out.println("Name: " + rs.getString("first_name") + " " + rs.getString("last_name")+ " Room Number: "+ rs.getString("room_id") + " Start Date: " + rs.getDate("arrive") + " End Date: "+ rs.getDate("depart") ); 
		}
	}
	
	//find the difference of days between 2 days.
	public static int daysBetween(Date d1, Date d2) {
	    return (int) ((d2.getTime() - d1.getTime()) / (24 * 60 * 60 * 1000)); 
	}
	//add day to a target date
    public Date addDays(Date date, int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, days);
        return new Date(c.getTimeInMillis());
    }
	
	//Jun, function  # 1
	public void makeReservation() {
		
		System.out.println("\n<===== Make a reservation =====>");
		Scanner scanner = new Scanner(System. in); 

		try {
			java.sql.Date arrive, depart, payment_due, expiration;
			int rm_id, g_id = 0;
			int p_id = 0;
			String phone = "";
			double cost = 0;
			double amount = 0;
			String status = "";
			String card_number = "";
			
			//validate dates and room conflict
//			boolean isConflict = true;
//			while (isConflict) {
				//enter room number
				System.out.println("Please enter the room number you want:");
				rm_id = Integer.parseInt(scanner.nextLine());
				
				//enter arrive date
				System.out.println("Please enter the Arrival Data in format \"YYYY-MM-DD\":");
				arrive = java.sql.Date.valueOf(scanner.nextLine());
				
				//enter depart date
				System.out.println("Please enter the Depart Data in format \"YYYY-MM-DD\":");
				depart = java.sql.Date.valueOf(scanner.nextLine());
//				
//
//				//validate the date conflict
//				ResultSet rs = null;
//				
//				
//				String s = "SELECT *\n" + 
//						"FROM room_reserved\n" + 
//						"WHERE ? NOT IN\n" + 
//						"(SELECT room_id \n" + 
//						" FROM room_reserved\n" + 
//						" WHERE\n" + 
//						"   (start_date <= ? AND end_date >= ?) OR\n" + 
//						"   (start_date <= ? AND end_date >= ?) OR\n" + 
//						"   (start_date >= ? AND end_date <= ?))";
//				
//				PreparedStatement pstmt = conn.prepareStatement( s );
//				pstmt.setInt(1, rm_id);
//				pstmt.setDate(2, arrive);
//				pstmt.setDate(3, arrive);
//				pstmt.setDate(4, depart);
//				pstmt.setDate(5, depart);
//				pstmt.setDate(6, arrive);
//				pstmt.setDate(7, depart);
//				
//				rs = pstmt.executeQuery( s );
//				
//				System.out.print("Testing ---> " + rs);
//				
//				if(rs == null) {
//					isConflict = false;
//				}else {
//					System.out.println("We found you enter room and date conflict with other booking.\n"
//							+ "Please re-enter.\n");
//				}
//			}
				
		//calculate the days
			int days = daysBetween(arrive, depart);
			System.out.println("Number of nights stay: " + days);
			
		//calculate the cost
			Statement stmt = conn.createStatement();
			String sql = "SELECT rate from room where room_id = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, rm_id);
			
			ResultSet resultSet = pstmt.executeQuery();
			double rate = 0;
			while(resultSet.next())
			{
				rate = resultSet.getDouble("rate");
			}
			cost = rate * days;
			amount = cost;
			System.out.println("Rate: $" + rate+" per night.");

		//get guest id and validate user
			boolean isValidGuest = false;
			while(!isValidGuest)
			{
				System.out.println("\nPlease enter Guest ID #:");
				g_id = Integer.parseInt(scanner.nextLine());
				
			//validating the guest id
				sql = "SELECT * FROM guest WHERE guest_id = ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, g_id);
				
				ResultSet rs = pstmt.executeQuery();
				if(!rs.next()) {
					System.out.print("Guest number " + g_id + " is invalid.");
				}else {
					isValidGuest = true;
				}
			}
			
		// enter phone number
			System.out.println("\nPlease enter phone #:");
			phone = scanner.nextLine();
			
			status = "pending";
			java.sql.Date todaysDate = new java.sql.Date(new java.util.Date().getTime());
			payment_due = this.addDays(todaysDate, 1);
			
			System.out.println("\nPlease enter credit card #:");
			card_number = scanner.nextLine();
			
			System.out.println("\nPlease enter credit card expiration date (YYYY-MM-DD):");
			expiration = java.sql.Date.valueOf(scanner.nextLine());
			
		//get a new payment id for a new reservation
			sql = "SELECT payment_id FROM payment ORDER BY payment_id DESC LIMIT 1;\n";			
			resultSet = stmt.executeQuery(sql);
			while(resultSet.next())
			{
				p_id = resultSet.getInt("payment_id");
			}
		//got the latest one and find the next available p_id
			p_id++;
			
			sql = "{call spMakeReservation(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )}";
			java.sql.CallableStatement cstmt = conn.prepareCall(sql);
			
			cstmt.setInt(1,rm_id);
			cstmt.setInt(2,g_id);
			cstmt.setString(3,phone);
			cstmt.setDate(4,arrive);
			cstmt.setDate(5,depart);
			cstmt.setDouble(6,cost);
			cstmt.setString(7,status);
			cstmt.setDate(8,payment_due);
			cstmt.setString(9,card_number);
			cstmt.setDate(10,expiration);
			cstmt.setDouble(11,amount);
			cstmt.setInt(12,p_id);

			boolean hasResult = cstmt.execute();

			System.out.println("Failed to make a reservation.\n");
			
		} catch (SQLException e1) {
			e1.printStackTrace();
		}	
	}
	
	//Jun, function #9
	public void makePayment() {
		System.out.println("\n<===== Make Payment =====>");
		int reservation_id;
		Scanner scanner = new Scanner(System. in); 
		System.out.println("Please enter the Reservation ID:");
		
		reservation_id = scanner.nextInt();
		
		try {
			String sql = "SELECT * FROM reservation WHERE reservation_id = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, reservation_id);
			
			ResultSet rs = pstmt.executeQuery();
			if(!rs.next()) {
				System.out.print("Reservation number " + reservation_id + " is invalid.");
			}else {
				System.out.println("reservation_id----> " + reservation_id);
				sql = "{ call spMakePayment(?)}";
				java.sql.CallableStatement cstmt = conn.prepareCall(sql);
				cstmt.setInt(1, reservation_id);
				boolean hasResult = cstmt.execute();

				System.out.println("Reservation is paid.\n");

			}


		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//Jun, function #14
	public void listGuestsOnArriveDate() {
		System.out.println("<===== List guests arriving on a specific date =====>");
		System.out.println("Please enter the Arrive Date (YYYY-MM-DD)");
		
		Scanner scanner = new Scanner(System.in);
		java.sql.Date arrivalDate = java.sql.Date.valueOf(scanner.nextLine());
		
		String sql = "SELECT first_name, last_name\n"
					+ "FROM guest G, reservation R\n"
					+ "WHERE G.guest_id = R.guest_id and R.arrive = ?";
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setDate(1, arrivalDate);
			pstmt.executeQuery();
			ResultSet rs = pstmt.getResultSet();
			
			if(rs.next() == false) {
				System.out.println("There is no guest arrive on " + arrivalDate);
			}else {
				System.out.println("The guests who arrive on " + arrivalDate + " are listed below:");
				int i = 1;
				do {
					System.out.println(" " + i + ".  " + rs.getString("last_name") + ", " + rs.getString("first_name") ); 
					i++;
				} while (rs.next());
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	//Jun, function #15
	public void listGuestsOnCheckoutDate() {
		System.out.println("<===== List guests checkout on a specific date =====>");
		System.out.println("Please enter the Checkout Date (YYYY-MM-DD)");
		
		Scanner scanner = new Scanner(System.in);
		java.sql.Date checkoutDate = java.sql.Date.valueOf(scanner.nextLine());
		
		String sql = "SELECT first_name, last_name\n"
					+ "FROM guest G, reservation R\n"
					+ "WHERE G.guest_id = R.guest_id and R.depart = ?";
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setDate(1, checkoutDate);
			pstmt.executeQuery();
			ResultSet rs = pstmt.getResultSet();
			
			if(rs.next() == false) {
				System.out.println("There is no guest arrive on " + checkoutDate);
			}else {
				System.out.println("The guests who checkout on " + checkoutDate + " are listed below:");
				int i = 1;
				do {
					System.out.println(" " + i + ".  " + rs.getString("last_name") + ", " + rs.getString("first_name") ); 
					i++;
				} while (rs.next());
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	//Jun, function #16
	public void registerGuest() {
		System.out.println("\n<===== Register a new guest =====>");
		String last_name , first_name, email;
		int guest_id = 0;
		boolean isValid = true;
		Scanner scanner = new Scanner(System. in); 
		
		System.out.println("Please enter a new guest Last Name:");		
		last_name = scanner.nextLine();
		System.out.println("Please enter a new guest First Name:");		
		first_name = scanner.nextLine();
		System.out.println("Please enter a new guest Email Address:");		
		email = scanner.nextLine();
		
		try {
			String sql = "INSERT INTO guest (first_name, last_name, email)" + 
					"VALUES (?, ?, ?);";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,  first_name);
			pstmt.setString(2,  last_name);
			pstmt.setString(3,  email);
			pstmt.executeUpdate();
		}catch (SQLException e) {
			e.printStackTrace();
		}
		
		//show the last guest id
		System.out.println(first_name + " " + last_name + " has been added to system.");
		try {
			Statement stmt = conn.createStatement();
			String sql = "SELECT guest_id FROM guest ORDER BY guest_id DESC LIMIT 1;";			
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next())
			{
				guest_id = rs.getInt("guest_id");
			}
			System.out.println("Guest ID: " + guest_id);
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

