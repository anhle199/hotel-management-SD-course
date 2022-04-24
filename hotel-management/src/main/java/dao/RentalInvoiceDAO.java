package dao;

import db.DBConnectionException;
import db.SingletonDBConnection;
import models.RentalInvoice;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class RentalInvoiceDAO implements DAO<RentalInvoice, Integer> {

	@Override
	public ArrayList<RentalInvoice> getAll() throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		ArrayList<RentalInvoice> rentalInvoiceList = new ArrayList<>();
		Statement statement = null;

		try {
			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("select * from `hotel_management`.`rental_invoice`");

			while (resultSet.next()) {
				rentalInvoiceList.add(
						new RentalInvoice(
								resultSet.getInt("id"),
								resultSet.getTimestamp("start_date"),
								resultSet.getInt("room_id"),
								resultSet.getNString("room_name"),
								resultSet.getNString("customer_name"),
								resultSet.getString("identity_number"),
								resultSet.getNString("address"),
								resultSet.getInt("customer_type")
						)
				);
			}
		} catch (SQLException e) {
			System.out.println("RentalInvoiceDAO.java - getAll - catch - " + e.getMessage());
			System.out.println("RentalInvoiceDAO.java - getAll - catch - " + Arrays.toString(e.getStackTrace()));
			throw DBConnectionException.INSTANCE;
		} finally {
			try {
				if (statement != null)
					statement.close();
			} catch (SQLException e) {
				System.out.println("RentalInvoiceDAO.java - getAll - finally/catch - " + e.getMessage());
				System.out.println("RentalInvoiceDAO.java - getAll - finally/catch - " + Arrays.toString(e.getStackTrace()));
			}
		}

		return rentalInvoiceList;
	}

	@Override
	public Optional<RentalInvoice> get(Integer id) throws DBConnectionException {
		return Optional.empty();
	}

	@Override
	public void insert(RentalInvoice entity) throws DBConnectionException {

	}

	@Override
	public void update(RentalInvoice entity) throws DBConnectionException {

	}

	@Override
	public void delete(Integer id) throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		Statement statement = null;

		try {
			String sqlStatement = "delete from `hotel_management`.`rental_invoice` where `id` = " + id;
			statement = connection.createStatement();

			connection.setAutoCommit(false);
			statement.executeUpdate(sqlStatement);
			connection.commit();
		} catch (SQLException e) {
			System.out.println("RentalInvoiceDAO.java - delete - catch - " + e.getMessage());
			System.out.println("RentalInvoiceDAO.java - delete - catch - " + Arrays.toString(e.getStackTrace()));

			try {
				connection.rollback();
			} catch (SQLException ex) {
				System.out.println("RentalInvoiceDAO.java - delete - catch/catch - " + ex.getMessage());
				System.out.println("RentalInvoiceDAO.java - delete - catch/catch - " + Arrays.toString(ex.getStackTrace()));
			}

			throw DBConnectionException.INSTANCE;
		} finally {
			try {
				connection.setAutoCommit(true);

				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				System.out.println("RentalInvoiceDAO.java - delete - finally/catch - " + e.getMessage());
				System.out.println("RentalInvoiceDAO.java - delete - finally/catch - " + Arrays.toString(e.getStackTrace()));
			}
		}
	}

}
