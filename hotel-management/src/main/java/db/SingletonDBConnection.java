package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;

public class SingletonDBConnection {

	private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	private static final String DB_URL ="jdbc:mysql://127.0.0.1:3306/";
	private static final String DB_USER ="root";
	private static final String DB_PASSWORD ="password";

	private Connection connection = null;

	private SingletonDBConnection() {
		connect();
	}

	private static class BillPughSingleton {
		private static final SingletonDBConnection INSTANCE = new SingletonDBConnection();
	}

	public static SingletonDBConnection getInstance() {
		return BillPughSingleton.INSTANCE;
	}

	public void connect() {
		try {
			if (connection != null && !connection.isClosed())
				closeConnection();

			Class.forName(JDBC_DRIVER);

			System.out.println("Connecting to the database");
			connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
			System.out.println("Connected to the database");
		} catch (SQLException sqlException) {
			System.out.println("Can not connect to MySQL");
			System.out.println(sqlException.getMessage());
			System.out.println(Arrays.toString(sqlException.getStackTrace()));
		} catch (ClassNotFoundException e) {
			System.out.println(JDBC_DRIVER + " class not found.");
			System.out.println(e.getMessage());
			System.out.println(Arrays.toString(e.getStackTrace()));
		}
	}

	public Connection getConnection() {
		return connection;
	}

	public void closeConnection() throws SQLException {
		if (connection != null && !connection.isClosed()) {
			connection.close();
			connection = null;
			System.out.println("Database connection is closed");
		}
	}

}
