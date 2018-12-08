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
	private User user;
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
	public void listAvailableRooms(){
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
	
	//Dmitriy, function #2
	public void changeRoom() throws SQLException
	{
		ResultSet rs = null;
		
		conn.setAutoCommit(false);
		statement = conn.createStatement();
		int roomId;
		boolean tv;
		boolean hotTub;
		String type = "";
		Double rate = 0.0;
		Scanner scan = new Scanner(System.in);
		
		System.out.println("\n<===== Changing a room =====>");
		
		System.out.println("Listing all possible rooms to change");
		listAvailableRooms();
		
		
		System.out.println("Enter the ID of the room you want to change :");
		roomId = scan.nextInt();
		
		//enter new value for TV
		System.out.println("Enter new value for tv :");
		tv  = scan.nextBoolean();
		int tvInt = tv ? 1 : 0; //MySQL only allows to insert 1 or 0, so we need to convert to int
		
		//Enter new value for hot tub  
		System.out.println("Enter new value for hot tub :");
		hotTub  = scan.nextBoolean();
		int hotTubInt = hotTub ? 1 : 0; //MySQL only allows to insert 1 or 0, so we need to convert to int 
		
		//enter new value for type
		System.out.println("Enter new value for type :");
		type = scan.next();
		
		//enter new value for rate
		System.out.println("Enter new value for rate :");
		rate = scan.nextDouble();
		
		
		String updRoom1 = "UPDATE room SET tv = '" + tvInt + "' WHERE room_id = '" + roomId +"'";
		String updRoom2 = "UPDATE room SET hot_tub = '" + hotTubInt + "' WHERE room_id = '" + roomId + "'";
		String updRoom3 = "UPDATE room SET room_type = '" + type + "' WHERE room_id = '" + roomId + "'";
		String updRoom4 = "UPDATE room SET rate = '" + rate + "' WHERE room_id = '" + roomId + "'";
		
		statement.addBatch(updRoom1);
		statement.addBatch(updRoom2);
		statement.addBatch(updRoom3);
		statement.addBatch(updRoom4);
		  
		statement.executeBatch(); 
		

		conn.commit();
		
	}
	
	//Dmitriy, function #3
	
	
	//Dmitriy, function #8
	public void deleteRoom()
	{
		ResultSet rs = null;
		String roomId;
		Scanner scan = new Scanner(System.in);
		
		
		System.out.println("\n<===== Deleting a room =====>");
		listAvailableRooms();
		System.out.println("Enter the id of the room you want to delete:");
		roomId = scan.nextLine();
		try {
			String sqlQuery =  "DELETE FROM room WHERE room_id = '" + roomId + "' AND room_id NOT IN "
					+ "(SELECT room_id from room_reserved where room_id = room.room_id);";
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(sqlQuery);
			
			
			System.out.println("Room is deleted successfully.");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//user login
	public User login() {
    	//Authentication
		boolean isValidUser = false;
		while(!isValidUser){
			Scanner scanner = new Scanner(System.in);
			System.out.println("\nPlease enter User ID #:");
			int u_id = Integer.parseInt(scanner.nextLine());
			System.out.println("\nPlease enter password:");
			String pw = scanner.nextLine();
			
		//validating the guest id
			try {
				String sql = "SELECT * FROM user WHERE user_id = ? and password = ?;";
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, u_id);
				pstmt.setString(2, pw);
				
				ResultSet rs = pstmt.executeQuery();
				if(!rs.next()) {
					System.out.print("Fail to login with user id " + u_id + "\n");
				}else {
					isValidUser = true;
					// need to set date to a user object
					user = new User();
					Statement stmt = conn.createStatement();
					sql = "SELECT * from user where user_id = ?";
					PreparedStatement ps = conn.prepareStatement(sql);
					ps.setInt(1, u_id);
					
					ResultSet rs1 = ps.executeQuery();
					while(rs1.next())
					{
						user.setAdmin(rs1.getBoolean("isAdmin"));
						user.setEmail(rs1.getString("email"));
						user.setFirst_name(rs1.getString("first_name"));
						user.setLast_name(rs1.getString("last_name"));
						user.setUser_id(rs1.getInt("user_id"));
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		return user;
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
			java.sql.Date arrive = null, depart = null, payment_due, expiration;
			int rm_id = 0, g_id = user.getUser_id();
			int p_id = 0;
			String phone = "";
			double cost = 0;
			double amount = 0;
			String status = "";
			String card_number = "";
			
			//validate dates and room conflict
			boolean isConflict = true;
			while (isConflict) {
				//enter room number
				System.out.println("Please enter the room number you want:");
				rm_id = Integer.parseInt(scanner.nextLine());
				
				//enter arrive date
				System.out.println("Please enter the Arrival Data in format \"YYYY-MM-DD\":");
				arrive = java.sql.Date.valueOf(scanner.nextLine());
				
				//enter depart date
				System.out.println("Please enter the Depart Data in format \"YYYY-MM-DD\":");
				depart = java.sql.Date.valueOf(scanner.nextLine());
				
				//validate the date conflict
				ResultSet rs = null;
				
				String s = "SELECT room_id \n" + 
						" FROM room_reserved\n" + 
						" WHERE\n" + 
						" 	room_id = ? AND \n" +
						"   ((start_date <= ? AND end_date >= ?) OR\n" + 
						"   (start_date <= ? AND end_date >= ?) OR\n" + 
						"   (start_date >= ? AND end_date <= ?))";
				
				PreparedStatement pstmt = conn.prepareStatement( s );
				pstmt.setInt(1, rm_id);
				pstmt.setDate(2, arrive);
				pstmt.setDate(3, arrive);
				pstmt.setDate(4, depart);
				pstmt.setDate(5, depart);
				pstmt.setDate(6, arrive);
				pstmt.setDate(7, depart);
				
				rs = pstmt.executeQuery();
				
				if(rs.next() == false) {	// if result is empty
					isConflict = false;
				}else {
					System.out.println("We found you enter room and date conflict with other booking.\n"
							+ "Please re-enter.\n");
				}
			}
				
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

		//get user id and validate user
			boolean isValidGuest = false;
			while(!isValidGuest)
			{
				
			//validating the guest id
				sql = "SELECT * FROM user WHERE user_id = ? and isAdmin = false;";
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
			
			status = "reserved";
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

			System.out.println("A new reservation made.\n");
			
		} catch (SQLException e1) {
			e1.printStackTrace();
		}	
	}
	// nick function #6
	public void ListAvailableRoomsByDay(){
		System.out.println("\n<===== List Available Rooms by date =====>");
		System.out.println("Please enter the A Date (YYYY-MM-DD)");
		
		Scanner scanner = new Scanner(System.in);
		java.sql.Date inDay = java.sql.Date.valueOf(scanner.nextLine());
		
		String sql = "call spListAvailableRoomsByDay(?);";
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setDate(1, inDay);
			pstmt.executeQuery();
			ResultSet rs = pstmt.getResultSet();
			
			if(rs.next() == false) {
				System.out.println("There no available rooms on " + inDay);
			}else {
				System.out.println("The available rooms on " + inDay + " are listed below:");
				int i = 1;
				do {
					System.out.println(" " + i + ".  Room Number:" + rs.getInt("room_id") + "   Room Type: " + rs.getString("room_type") + "   Rate per night: " + rs.getInt("rate") ); 
					i++;
				} while (rs.next());
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	//Nick function #7
	public void listReservationsByGuest(){
		System.out.println("\n<===== List all reservations for a guest =====>");
		System.out.println("Please enter the ID of the Guest");
		
		Scanner scanner = new Scanner(System.in);
		int inGuestID = scanner.nextInt();
		
		String sql = "call spListReservationsByGuest(?);";
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, inGuestID);
			pstmt.executeQuery();
			ResultSet rs = pstmt.getResultSet();
			
			if(rs.next() == false) {
				System.out.println("Guest " + inGuestID + " has made no reservations");
			}else {
				System.out.println("Guest "+ inGuestID + " has the following reservations:");
				int i = 1;
				do {
					System.out.println(" " + i + ".  Room Number:" + rs.getInt("room_id") + "   Arrive: " + rs.getDate("arrive") + "   Depart: " + rs.getDate("depart") ); 
					i++;
				} while (rs.next());
			}
		}catch(SQLException e) {
			e.printStackTrace();
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
	
	//Jun, #10
	public void updatePayment() {
		System.out.println("<===== Update payment of reservation number =====>");
		Scanner scanner = new Scanner(System.in);
		int r_id = 0;
		String card_number = "";
		java.sql.Date e_date;
		
	//validate the reservation id
		boolean isValid = false;
		while(!isValid)
		{
			System.out.println("\nPlease enter Reservation ID #:");
			r_id = Integer.parseInt(scanner.nextLine());
			
		//validating the guest id
			String s = "SELECT * FROM reservation WHERE reservation_id = ?";
			try {
				PreparedStatement pstmt = conn.prepareStatement(s);
				pstmt.setInt(1, r_id);
				
				ResultSet rs = pstmt.executeQuery();
				if(!rs.next()) {
					System.out.print("Reservation number " + r_id + " is invalid.");
				}else {
					isValid = true;
				}
			} catch ( SQLException e2) {
				e2.printStackTrace();
			}
		}
		
		System.out.println("Please enter a new card number:");
		card_number = scanner.nextLine();
		
		System.out.println("Please enter the expiration date (YYYY-MM-DD) of the new card:");
		e_date = java.sql.Date.valueOf(scanner.nextLine());
		
		try {
			String sql = "{call spUpdatePayment(?, ?, ?)}";
			java.sql.CallableStatement cstmt = conn.prepareCall(sql);
			cstmt.setInt(1, r_id);
			cstmt.setString(2, card_number);
			cstmt.setDate(3, e_date);
			cstmt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		System.out.println("Payment method information of reservation " + r_id + " has been updated.");
		
	}
	
	//Jun, function #14
	public void listGuestsOnArriveDate() {
		System.out.println("<===== List guests arriving on a specific date =====>");
		System.out.println("Please enter the Arrive Date (YYYY-MM-DD)");
		
		Scanner scanner = new Scanner(System.in);
		java.sql.Date arrivalDate = java.sql.Date.valueOf(scanner.nextLine());
		
		String sql = "SELECT first_name, last_name\n"
					+ "FROM user G, reservation R\n"
					+ "WHERE G.user_id = R.user_id and R.arrive = ?";
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
					+ "FROM user U, reservation R\n"
					+ "WHERE U.user_id = R.user_id and R.depart = ?";
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
	public void registerRegualUser() {
		System.out.println("\n<===== Register a new user =====>");
		String last_name , first_name, email;
		int user_id = 0;
		boolean isValid = true;
		boolean isAdmin = false;
		Scanner scanner = new Scanner(System. in); 
		
		System.out.println("Please enter a new guest Last Name:");		
		last_name = scanner.nextLine();
		System.out.println("Please enter a new guest First Name:");		
		first_name = scanner.nextLine();
		System.out.println("Please enter a new guest Email Address:");		
		email = scanner.nextLine();
		System.out.println("Please enter a password:");		
		 String pw = scanner.nextLine();
		
		try {
			String sql = "INSERT INTO user (first_name, last_name, email, isAdmin, password)" + 
					"VALUES (?, ?, ?, ?, ?);";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,  first_name);
			pstmt.setString(2,  last_name);
			pstmt.setString(3,  email);
			pstmt.setBoolean(4,  isAdmin);
			pstmt.setString(5,  pw);
			pstmt.executeUpdate();
		}catch (SQLException e) {
			e.printStackTrace();
		}
		
		//show the last guest id
		System.out.println(first_name + " " + last_name + " has been added to system.");
		try {
			Statement stmt = conn.createStatement();
			String sql = "SELECT user_id FROM user ORDER BY user_id DESC LIMIT 1;";			
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next())
			{
				user_id = rs.getInt("user_id");
			}
			System.out.println("User ID: " + user_id);
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//Jun, function #17
	public void registerAdminUser() {
		System.out.println("\n<===== Register a new Admin User =====>");
		String last_name , first_name, email;
		int user_id = 0;
		boolean isValid = true;
		boolean isAdmin = true;
		Scanner scanner = new Scanner(System. in); 
		
		System.out.println("Please enter the Last Name:");		
		last_name = scanner.nextLine();
		System.out.println("Please enter the First Name:");		
		first_name = scanner.nextLine();
		System.out.println("Please enter the Email Address:");		
		email = scanner.nextLine();
		System.out.println("Please enter a password:");		
		 String pw = scanner.nextLine();
		
		try {
			String sql = "INSERT INTO user (first_name, last_name, email, isAdmin, password)" + 
					"VALUES (?, ?, ?, ?, ?);";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,  first_name);
			pstmt.setString(2,  last_name);
			pstmt.setString(3,  email);
			pstmt.setBoolean(4,  isAdmin);
			pstmt.setString(5,  pw);
			pstmt.executeUpdate();
		}catch (SQLException e) {
			e.printStackTrace();
		}
		
		//show the last guest id
		System.out.println(first_name + " " + last_name + " has been added to system.");
		try {
			Statement stmt = conn.createStatement();
			String sql = "SELECT user_id FROM user ORDER BY user_id DESC LIMIT 1;";			
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next())
			{
				user_id = rs.getInt("user_id");
			}
			System.out.println("User ID: " + user_id);
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// Jun, admin #18 Show the room numbers which rate is higher than average rates
	public void searchAdminUser() {
		System.out.println("\n<===== Show the room numbers which rate is higher than average rate =====>");
		
		String sql = "SELECT room_id, rate\n" + 
				"FROM room\n" + 
				"GROUP BY room_id\n" + 
				"HAVING AVG(rate) > (SELECT AVG(rate) FROM room);";
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			System.out.println("Room # || Rate");
			while (rs.next()) {
				System.out.println(" " + rs.getString("room_id") + "     $" + rs.getDouble("rate"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	// Nick #19
	public void archiveReservations(){
		System.out.println("<===== List Archive Reservations after a specific date =====>");
		System.out.println("Please enter the Cut off Date (YYYY-MM-DD)");
		
		Scanner scanner = new Scanner(System.in);
		java.sql.Date cutOffDate = java.sql.Date.valueOf(scanner.nextLine());
		
		String sql = "call SPArchiveReservations(?)";
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setDate(1, cutOffDate);
			pstmt.executeQuery();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM reservationArchive");
			System.out.println("The archived reservation table now has the following values:");
			while (rs.next()) {
				System.out.println(" Reservation ID: " + rs.getInt("reservation_id") + " Arrive Date: " + rs.getDate("arrive")+ " Depart Date: " +rs.getDate("Depart"));
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	//Nick Function #20
	public void listAllCoustomersWhoBookedRooms(){
		System.out.println("<===== List all coustomers who have booked rooms =====>");
		String sql = "SELECT u.first_name, u.last_name "
				+ "FROM user u "
				+ "WHERE u.user_id in ("
				+ "	SELECT r.user_id FROM reservation r "
				+ "	Union "
				+ " SELECT ra.user_id FROM reservationArchive ra); ";
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			System.out.println("All coustomers who have booked rooms are: ");
			while (rs.next()) {
				System.out.println(" " + rs.getString("last_name") + ", " + rs.getString("first_name") );
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	//Dmitriy function #4
	public void cancelReservation() throws SQLException
	{
		conn.setAutoCommit(false);
		Statement delstmt = conn.createStatement();
		Scanner scan = new Scanner(System.in);
		int roomId = 0;
		int reservId = 0;
		java.sql.Date depDate = null;
		
		System.out.println("\n<===== Deleting reservation for a customer =====>");
		System.out.println("Select the room you want to delete reservation for :");
		roomId = Integer.parseInt(scan.nextLine());
		System.out.println("Printing all reservations for this room :");
		listReservationByRoomId(roomId);
		System.out.println("Select the reservation_id you want to delete :");
		reservId = Integer.parseInt(scan.nextLine());
		
		
		System.out.println("Please enter your Departure Date in format \"YYYY-MM-DD\":");
		depDate = Date.valueOf(scan.nextLine());
		
		
		String sqlDelete1  = "DELETE FROM reservation where room_id = ' "+ roomId +"'and reservation_id = '" + reservId + "';";
		String sqlDelete2 =  "DELETE FROM room_reserved where room_id = ' "+ roomId +"' and end_date = '" + depDate + "';";
		
		statement.addBatch(sqlDelete1);
		statement.addBatch(sqlDelete2);
		
		  
		statement.executeBatch(); 
		
		conn.commit();
		
		
	}
	
	//Dmitriy function#3
	public void checkout() throws SQLException
	{
		conn.setAutoCommit(false);
		Statement delstmt = conn.createStatement();
		Scanner scan = new Scanner(System.in);
		int roomId = 0;
		int reservId = 0;
		java.sql.Date depDate = null;
		
		System.out.println("\n<===== User checkout =====>");
		System.out.println("Select the room you were staying in :");
		roomId = Integer.parseInt(scan.nextLine());
		
		
		System.out.println("Please enter your Departure Date in format \"YYYY-MM-DD\":");
		depDate = Date.valueOf(scan.nextLine());
		
		
		String sqlDelete1  = "DELETE FROM reservation where room_id = ' "+ roomId +"'and depart = '" + depDate + "';";
		String sqlDelete2 =  "DELETE FROM room_reserved where room_id = ' "+ roomId +"' and end_date = '" + depDate + "';";
		
		statement.addBatch(sqlDelete1);
		statement.addBatch(sqlDelete2);
		
		  
		statement.executeBatch(); 
		
		conn.commit();
		
		
	}
	
	
	//Dmitriy function #5 
	public void updateReservation() throws SQLException
	{
		
		conn.setAutoCommit(false);
		Scanner scan = new Scanner(System.in);
		int roomId = 0;
		int reservId = 0;
		java.sql.Date arrive = null, depart = null;
		double cost = 0.0;
		double amount = 0.0;
		java.sql.Date depDate = null;
				
		System.out.println("\n<===== Changing a reservation for a customer =====>");
		System.out.println("Select room you are currently staying in");
		roomId = Integer.parseInt(scan.nextLine());
		System.out.println("Printing all reservations for this room :");
		listReservationByRoomId(roomId);
		System.out.println("Select the reservation_id you want to update :");
		reservId = Integer.parseInt(scan.nextLine());
		
		//enter new arrive date
		System.out.println("Please enter the new Arrival Date in format \"YYYY-MM-DD\":");
		arrive = Date.valueOf(scan.nextLine());
		
		//enter depart date
		System.out.println("Please enter the new Departure Date in format \"YYYY-MM-DD\":");
		depart = Date.valueOf(scan.nextLine());
		
		//calculate new days
		int days = daysBetween(arrive, depart);
		System.out.println("Number of nights stay: " + days);
		
		//calculate the cost
		Statement stmt = conn.createStatement();
		String sql = "SELECT rate from room where room_id  = '" + roomId + "'";
		
		PreparedStatement pstmt = conn.prepareStatement(sql);
		
		
		ResultSet resultSet = pstmt.executeQuery();
		double rate = 0;
		while(resultSet.next())
		{
			rate = resultSet.getDouble("rate");
		}
		cost = rate * days;
		amount = cost;
		System.out.println("Rate: $" + rate+" per night.");
		
		String updRes2 = "UPDATE reservation SET arrive = '" + arrive + "' WHERE room_id = '" + roomId +"' and reservation_id = '"+ reservId + "';";
		String updRes3 = "UPDATE reservation SET depart = '" + depart + "' WHERE room_id = '" + roomId + "'and reservation_id = '" + reservId + "';";
		String updRes4 = "UPDATE reservation SET cost = '" + amount + "' WHERE room_id = '" + roomId + "' and reservation_id = ' " + reservId + "';";
		String updRes5 = "UPDATE reservation SET r_status ='reserved' WHERE room_id = '" + roomId + "'and reservation_id = '" + reservId + "';";
		String updRes1 = "UPDATE room_reserved SET start_date = '" + arrive + "', end_date = '" + depart + "' WHERE room_id = '" + roomId + "' AND end_date in "
				+ "(SELECT depart FROM reservation WHERE reservation_id = '" + reservId + "' );"; 
		
		statement.addBatch(updRes1);
		statement.addBatch(updRes2);
		statement.addBatch(updRes3);
		statement.addBatch(updRes4);
		statement.addBatch(updRes5);
		  
		statement.executeBatch(); 
		
		conn.commit();
		
		
	}
	
	//Jun, #1, for admin account
	public void makeReservationAsAdmin() {
		
		System.out.println("\n<===== Make a reservation for a customer =====>");
		Scanner scanner = new Scanner(System.in); 

		try {
			java.sql.Date arrive = null, depart = null, payment_due, expiration;
			int rm_id = 0, g_id = 0;
			int p_id = 0;
			String phone = "";
			double cost = 0;
			double amount = 0;
			String status = "";
			String card_number = "";
			
			//validate dates and room conflict
			boolean isConflict = true;
			while (isConflict) {
				//enter room number
				System.out.println("Please enter the room number you want:");
				rm_id = Integer.parseInt(scanner.nextLine());
				
				//enter arrive date
				System.out.println("Please enter the Arrival Data in format \"YYYY-MM-DD\":");
				arrive = java.sql.Date.valueOf(scanner.nextLine());
				
				//enter depart date
				System.out.println("Please enter the Depart Data in format \"YYYY-MM-DD\":");
				depart = java.sql.Date.valueOf(scanner.nextLine());
				
				//validate the date conflict
				ResultSet rs = null;
				
				String s = "SELECT room_id \n" + 
						" FROM room_reserved\n" + 
						" WHERE\n" + 
						" 	room_id = ? AND \n" +
						"   ((start_date <= ? AND end_date >= ?) OR\n" + 
						"   (start_date <= ? AND end_date >= ?) OR\n" + 
						"   (start_date >= ? AND end_date <= ?))";
				
				PreparedStatement pstmt = conn.prepareStatement( s );
				pstmt.setInt(1, rm_id);
				pstmt.setDate(2, arrive);
				pstmt.setDate(3, arrive);
				pstmt.setDate(4, depart);
				pstmt.setDate(5, depart);
				pstmt.setDate(6, arrive);
				pstmt.setDate(7, depart);
				
				rs = pstmt.executeQuery();
				
				if(rs.next() == false) {	// if result is empty
					isConflict = false;
				}else {
					System.out.println("We found you enter room and date conflict with other booking.\n"
							+ "Please re-enter.\n");
				}
			}
				
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

		//get user id and validate user
			boolean isValidGuest = false;
			while(!isValidGuest)
			{
				
			//validating the user id
				System.out.println("Please enter user ID:");
				g_id = Integer.parseInt(scanner.nextLine());
				sql = "SELECT * FROM user WHERE user_id = ? and isAdmin = false;";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, g_id);
				
				ResultSet rs = pstmt.executeQuery();
				if(!rs.next()) {
					System.out.print("User number " + g_id + " is invalid.");
				}else {
					isValidGuest = true;
				}
			}
			
		// enter phone number
			System.out.println("\nPlease enter phone #:");
			phone = scanner.nextLine();
			
			status = "reserved";
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

			System.out.println("A new reservation made.\n");
			
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	
	//Nick #13
	public void listGuestByRoomNoAndDate(){
		System.out.println("<===== Finds the guest that reserved a room on a specific date =====>");
		Scanner scanner = new Scanner(System.in);
		System.out.println("please enter the room number");
		int rm_id = Integer.parseInt(scanner.nextLine());
		System.out.println("Please enter the Date (YYYY-MM-DD)");		
		java.sql.Date queryDate = java.sql.Date.valueOf(scanner.nextLine());
		
		String sql = "call spGuestByRoomNoAndDay(?, ?)";
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setDate(1, queryDate);
			pstmt.setInt(2, rm_id);

			pstmt.executeQuery();
			ResultSet rs = pstmt.getResultSet();
			
			if(rs.next() == false) {
				System.out.println("There is no guest in room " + rm_id +" on "+ queryDate);
			}else {
				System.out.println("The guest in room " + rm_id + " is:");
				do {
					System.out.println(" Guest: " + rs.getString("last_name") + ", " + rs.getString("first_name") + " UserID: " + rs.getInt("user_id")); 
				} while (rs.next());
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	//Nick 12
	public void listReservationsByRoom(){
		System.out.println("<===== Finds all guest that reserved a specific room =====>");
		Scanner scanner = new Scanner(System.in);
		System.out.println("please enter the room number");
		int rm_id = Integer.parseInt(scanner.nextLine());
		
		String sql = "call spListReservationsByRoom(?)";
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, rm_id);
			pstmt.executeQuery();
			ResultSet rs = pstmt.getResultSet();
			
			if(rs.next() == false) {
				System.out.println("There are no guest in room " + rm_id);
			}else {
				System.out.println("The following guests have reserved " + rm_id + " :");
				do {
					System.out.println(" Guest: " + rs.getString("last_name") + ", " + rs.getString("first_name") + " UserID: " + rs.getInt("user_id") + " Arrive: "+ rs.getDate("arrive") + " Depart: " +rs.getDate("depart")); 
				} while (rs.next());
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	//Jun, Guest menu, # 2, list all bookings for a login guest
	public void listMyReservation() {
		
		System.out.println("<===== List of your reservations =====>");
		try {
			String sql = "SELECT * from "
					+ "reservation R, room_reserved RR "
					+ "where R.user_id = ? and RR.room_id = R.room_id and RR.start_date = R.arrive";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, user.getUser_id());
			
			ResultSet rs = pstmt.executeQuery();
			System.out.println("\nRoom id -- Arrive Date -- Depart Date -- Reservation ID" );
			while(rs.next())
			{
				System.out.println("   " + rs.getInt("room_id") + "     "
						+ rs.getDate("arrive")+ "     "
						+ rs.getDate("depart")+ "         "
						+ rs.getInt("reservation_id"));
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	
	
	public void listReservationByRoomId(int roomId)
	{
		Statement stmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT * FROM reservation WHERE room_id = '" + roomId + "'";
			stmt = conn.createStatement();
			
			
			rs = stmt.executeQuery(sql);
			System.out.println("\nRoom id -- Arrive Date -- Depart Date -- Reservation ID" );
			while(rs.next())
			{
				System.out.println("   " + rs.getInt("room_id") + "     "
						+ rs.getDate("arrive")+ "     "
						+ rs.getDate("depart")+ "         "
						+ rs.getInt("reservation_id"));
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	

	
	
}

