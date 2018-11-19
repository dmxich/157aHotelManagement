import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.sql.DataSource;
import javax.sql.DataSource;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
 
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
			System.out.println("Name: " + rs.getString("first_name") + " " + rs.getString("last_name") + " Amount Owed: " + rs.getInt("amnt")); 
		}
	}
	public void ListTakenRooms(){
		ResultSet rs = null;
		System.out.println("Taken Rooms");
		try {
			rs = statement.executeQuery("select R.room_id from room R where R.room_id IN (select room_id from room_reserved)");
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
}

