package dao;

import db.DBConnectionException;
import db.SingletonDBConnection;
import models.RentalInvoice;

import java.sql.*;
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
								resultSet.getTimestamp("end_date"),
								resultSet.getInt("room_id"),
								resultSet.getNString("room_name"),
								resultSet.getInt("room_type_id"),
								resultSet.getNString("room_type_name"),
								resultSet.getInt("room_type_price"),
								resultSet.getNString("customer_name"),
								resultSet.getString("identity_number"),
								resultSet.getNString("address"),
								resultSet.getInt("customer_type"),
								resultSet.getByte("is_paid")
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
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		PreparedStatement preparedStatement = null;

		try {
			// Declare sql statement and create PreparedStatement for it.
			String sqlStatement = "insert into `hotel_management`.`rental_invoice` " +
					"(`start_date`, `end_date`, `room_id`, `room_name`," +
					" `room_type_id`, `room_type_name`, `room_type_price`, `customer_name`," +
					" `identity_number`, `address`, `customer_type`, `is_paid`) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 0)";
			preparedStatement = connection.prepareStatement(sqlStatement);

			// Set values for PreparedStatement.
			preparedStatement.setTimestamp(1, entity.getStartDate());
			preparedStatement.setTimestamp(2, entity.getEndDate());
			preparedStatement.setNString(4, entity.getRoomName());
			preparedStatement.setInt(5, entity.getRoomTypeId());
			preparedStatement.setNString(6, entity.getRoomTypeName());
			preparedStatement.setInt(7, entity.getRoomTypePrice());
			preparedStatement.setNString(8, entity.getCustomerName());
			preparedStatement.setString(9, entity.getIdentityNumber());
			preparedStatement.setNString(10, entity.getAddress());
			preparedStatement.setInt(11, entity.getCustomerType());

			if (entity.getRoomId() == 0 || entity.getRoomId() == -1) {
				preparedStatement.setNull(3, Types.INTEGER);
			} else {
				preparedStatement.setInt(3, entity.getRoomId());
			}

			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("RentalInvoiceDAO.java - insert - catch - " + e.getMessage());
			System.out.println("RentalInvoiceDAO.java - insert - catch - " + Arrays.toString(e.getStackTrace()));

			throw DBConnectionException.INSTANCE;
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (SQLException e) {
				System.out.println("RentalInvoiceDAO.java - insert - finally/catch - " + e.getMessage());
				System.out.println("RentalInvoiceDAO.java - insert - finally/catch - " + Arrays.toString(e.getStackTrace()));
			}
		}
	}

	@Override
	public void update(RentalInvoice entity) throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		PreparedStatement preparedStatement = null;

		try {
			// Declare sql statement and create PreparedStatement for it.
			String sqlStatement = "update `hotel_management`.`rental_invoice` " +
					"set `start_date` = ?, `end_date` = ?, `room_id` = ?, `room_name` = ?, " +
					"`room_type_id` = ?, `room_type_name` = ?, `room_type_price` = ?, " +
					"`customer_name` = ?, `identity_number` = ?, `address` = ?, `customer_type` = ? " +
					"where `id` = ?";

			preparedStatement = connection.prepareStatement(sqlStatement);

			// Set values for PreparedStatement.
			preparedStatement.setTimestamp(1, entity.getStartDate());
			preparedStatement.setTimestamp(2, entity.getEndDate());
			preparedStatement.setNString(4, entity.getRoomName());
			preparedStatement.setInt(5, entity.getRoomTypeId());
			preparedStatement.setNString(6, entity.getRoomTypeName());
			preparedStatement.setInt(7, entity.getRoomTypePrice());
			preparedStatement.setNString(8, entity.getCustomerName());
			preparedStatement.setString(9, entity.getIdentityNumber());
			preparedStatement.setNString(10, entity.getAddress());
			preparedStatement.setInt(11, entity.getCustomerType());
			preparedStatement.setInt(12, entity.getId());

			if (entity.getRoomId() == 0 || entity.getRoomId() == -1) {
				preparedStatement.setNull(3, Types.INTEGER);
			} else {
				preparedStatement.setInt(3, entity.getRoomId());
			}

			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("RentalInvoiceDAO.java - update - catch - " + e.getMessage());
			System.out.println("RentalInvoiceDAO.java - update - catch - " + Arrays.toString(e.getStackTrace()));

			throw DBConnectionException.INSTANCE;
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (SQLException e) {
				System.out.println("RentalInvoiceDAO.java - update - finally/catch - " + e.getMessage());
				System.out.println("RentalInvoiceDAO.java - update - finally/catch - " + Arrays.toString(e.getStackTrace()));
			}
		}
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
