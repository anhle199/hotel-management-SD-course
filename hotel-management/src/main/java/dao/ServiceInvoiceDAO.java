package dao;

import db.DBConnectionException;
import db.SingletonDBConnection;
import models.ServiceInvoice;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class ServiceInvoiceDAO implements DAO<ServiceInvoice, Integer> {

	@Override
	public ArrayList<ServiceInvoice> getAll() throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		ArrayList<ServiceInvoice> serviceInvoiceList = new ArrayList<>();
		Statement statement = null;

		try {
			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("select * from `hotel_management`.`service_invoice`");

			while (resultSet.next()) {
				serviceInvoiceList.add(
						new ServiceInvoice(
								resultSet.getInt("id"),
								resultSet.getNString("service_name"),
								resultSet.getInt("number_of_customers"),
								resultSet.getInt("total_price"),
								resultSet.getInt("time_used"),
								resultSet.getNString("notes"),
								resultSet.getInt("room_id"),
								resultSet.getNString("room_name"),
								resultSet.getInt("service_id")
						)
				);
			}
		} catch (SQLException e) {
			System.out.println("ServiceInvoiceDAO.java - getAll - catch - " + e.getMessage());
			System.out.println("ServiceInvoiceDAO.java - getAll - catch - " + Arrays.toString(e.getStackTrace()));
			throw DBConnectionException.INSTANCE;
		} finally {
			try {
				if (statement != null)
					statement.close();
			} catch (SQLException e) {
				System.out.println("ServiceInvoiceDAO.java - getAll - finally/catch - " + e.getMessage());
				System.out.println("ServiceInvoiceDAO.java - getAll - finally/catch - " + Arrays.toString(e.getStackTrace()));
			}
		}

		return serviceInvoiceList;
	}

	@Override
	public Optional<ServiceInvoice> get(Integer id) throws DBConnectionException {
		return Optional.empty();
	}

	@Override
	public void insert(ServiceInvoice entity) throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		PreparedStatement preparedStatement = null;

		try {
			// Declare sql statement and create PreparedStatement for it.
			String sqlStatement = "insert into `hotel_management`.`service_invoice` " +
					"(`service_name`, `number_of_customers`, `total_price`, `time_used`, " +
					"`notes`, `room_id`, `room_name`, `service_id`) values (?, ?, ?, ?, ?, ?, ?, ?)";
			preparedStatement = connection.prepareStatement(sqlStatement);

			// Set values for PreparedStatement.
			preparedStatement.setNString(1, entity.getServiceName());
			preparedStatement.setInt(2, entity.getNumberOfCustomers());
			preparedStatement.setInt(3, entity.getTotalPrice());
			preparedStatement.setInt(4, entity.getTimeUsed());
			preparedStatement.setNString(5, entity.getNote());
			preparedStatement.setNString(7, entity.getRoomName());

			if (entity.getRoomId() == 0 || entity.getRoomId() == -1) {
				preparedStatement.setNull(6, Types.INTEGER);
			} else {
				preparedStatement.setInt(6, entity.getRoomId());
			}

			if (entity.getServiceId() == 0 || entity.getServiceId() == -1) {
				preparedStatement.setNull(8, Types.INTEGER);
			} else {
				preparedStatement.setInt(8, entity.getServiceId());
			}

			// Execute queries.
			connection.setAutoCommit(false);
			preparedStatement.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			System.out.println("ServiceInvoiceDAO.java - insert - catch - " + e.getMessage());
			System.out.println("ServiceInvoiceDAO.java - insert - catch - " + Arrays.toString(e.getStackTrace()));

			try {
				connection.rollback();
			} catch (SQLException ex) {
				System.out.println("ServiceInvoiceDAO.java - insert - catch/catch - " + ex.getMessage());
				System.out.println("ServiceInvoiceDAO.java - insert - catch/catch - " + Arrays.toString(ex.getStackTrace()));
			}

			throw DBConnectionException.INSTANCE;
		} finally {
			try {
				connection.setAutoCommit(true);

				if (preparedStatement != null)
					preparedStatement.close();
			} catch (SQLException e) {
				System.out.println("ServiceInvoiceDAO.java - insert - finally/catch - " + e.getMessage());
				System.out.println("ServiceInvoiceDAO.java - insert - finally/catch - " + Arrays.toString(e.getStackTrace()));
			}
		}
	}

	@Override
	public void update(ServiceInvoice entity) throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		PreparedStatement preparedStatement = null;

		try {
			// Declare sql statement and create PreparedStatement for it.
			String sqlStatement = "update `hotel_management`.`service_invoice` " +
					"set `service_name` = ?, `number_of_customers` = ?, `total_price` = ?, `time_used` = ?, " +
					"`notes` = ?, `room_id` = ?, `room_name` = ?, `service_id` = ? where `id` = ?";
			preparedStatement = connection.prepareStatement(sqlStatement);

			// Set values for PreparedStatement.
			preparedStatement.setNString(1, entity.getServiceName());
			preparedStatement.setInt(2, entity.getNumberOfCustomers());
			preparedStatement.setInt(3, entity.getTotalPrice());
			preparedStatement.setInt(4, entity.getTimeUsed());
			preparedStatement.setNString(5, entity.getNote());
			preparedStatement.setNString(7, entity.getRoomName());
			preparedStatement.setInt(9, entity.getId());

			if (entity.getRoomId() == 0 || entity.getRoomId() == -1) {
				preparedStatement.setNull(6, Types.INTEGER);
			} else {
				preparedStatement.setInt(6, entity.getRoomId());
			}

			if (entity.getServiceId() == 0 || entity.getServiceId() == -1) {
				preparedStatement.setNull(8, Types.INTEGER);
			} else {
				preparedStatement.setInt(8, entity.getServiceId());
			}

			// Execute queries.
			connection.setAutoCommit(false);
			preparedStatement.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			System.out.println("ServiceInvoiceDAO.java - update - catch - " + e.getMessage());
			System.out.println("ServiceInvoiceDAO.java - update - catch - " + Arrays.toString(e.getStackTrace()));

			try {
				connection.rollback();
			} catch (SQLException ex) {
				System.out.println("ServiceInvoiceDAO.java - update - catch/catch - " + ex.getMessage());
				System.out.println("ServiceInvoiceDAO.java - update - catch/catch - " + Arrays.toString(ex.getStackTrace()));
			}

			throw DBConnectionException.INSTANCE;
		} finally {
			try {
				connection.setAutoCommit(true);

				if (preparedStatement != null)
					preparedStatement.close();
			} catch (SQLException e) {
				System.out.println("ServiceInvoiceDAO.java - update - finally/catch - " + e.getMessage());
				System.out.println("ServiceInvoiceDAO.java - update - finally/catch - " + Arrays.toString(e.getStackTrace()));
			}
		}
	}

	@Override
	public void delete(Integer id) throws DBConnectionException {

	}

}
