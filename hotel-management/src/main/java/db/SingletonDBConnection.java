package db;

import utils.Constants;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;

public class SingletonDBConnection {

	private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";

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
			connection = DriverManager.getConnection(
					Constants.DOTENV.get("DB_URL"),
					Constants.DOTENV.get("DB_USER"),
					Constants.DOTENV.get("DB_PASSWORD")
			);
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
