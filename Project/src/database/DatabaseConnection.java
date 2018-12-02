package database;
import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
	String driver;
	String connectionURL;
	String dbName;
	String username;
	String password;
	public DatabaseConnection() {
		driver = "com.mysql.jdbc.Driver";
		connectionURL = "jdbc:mysql://localhost/";
		dbName = "labreservationsystem";
		username = "root";
		password = "root";
	}
	public Connection getConnection() throws Exception{
		Class.forName(driver);
		Connection connection = DriverManager.getConnection(connectionURL+dbName+"?useSSL=false", username, password);
		return connection;
		
	}
}