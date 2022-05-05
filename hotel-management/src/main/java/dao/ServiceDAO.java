package dao;

import db.DBConnectionException;
import db.SingletonDBConnection;
import models.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class ServiceDAO implements DAO<Service, Integer> {

	@Override
	public ArrayList<Service> getAll() throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		ArrayList<Service> serviceList = new ArrayList<>();
		Statement statement = null;

		try {
			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("select * from `hotel_management`.`service`");

			while (resultSet.next()) {
				serviceList.add(
						new Service(
								resultSet.getInt("id"),
								resultSet.getNString("name"),
								resultSet.getNString("description"),
								resultSet.getInt("price"),
								resultSet.getNString("notes")
						)
				);
			}
		} catch (SQLException e) {
			System.out.println("ServiceDAO.java - getAll - catch - " + e.getMessage());
			System.out.println("ServiceDAO.java - getAll - catch - " + Arrays.toString(e.getStackTrace()));
			throw DBConnectionException.INSTANCE;
		} finally {
			try {
				if (statement != null)
					statement.close();
			} catch (SQLException e) {
				System.out.println("ServiceDAO.java - getAll - finally/catch - " + e.getMessage());
				System.out.println("ServiceDAO.java - getAll - finally/catch - " + Arrays.toString(e.getStackTrace()));
			}
		}

		return serviceList;
	}

	public ArrayList<Service> searchByServiceName(String serviceNameToken) throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		ArrayList<Service> serviceList = new ArrayList<>();
		Statement statement = null;

		try {
			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
					"select * from `hotel_management`.`service` where `name` like '" + serviceNameToken + "'"
			);

			while (resultSet.next()) {
				serviceList.add(
						new Service(
								resultSet.getInt("id"),
								resultSet.getNString("name"),
								resultSet.getNString("description"),
								resultSet.getInt("price"),
								resultSet.getNString("notes")
						)
				);
			}
		} catch (SQLException e) {
			System.out.println("ServiceDAO.java - searchByServiceName - catch - " + e.getMessage());
			System.out.println("ServiceDAO.java - searchByServiceName - catch - " + Arrays.toString(e.getStackTrace()));
			throw DBConnectionException.INSTANCE;
		} finally {
			try {
				if (statement != null)
					statement.close();
			} catch (SQLException e) {
				System.out.println("ServiceDAO.java - searchByServiceName - finally/catch - " + e.getMessage());
				System.out.println("ServiceDAO.java - searchByServiceName - finally/catch - " + Arrays.toString(e.getStackTrace()));
			}
		}

		return serviceList;
	}

	@Override
	public Optional<Service> get(Integer id) throws DBConnectionException {
		return Optional.empty();
	}

	@Override
	public void insert(Service entity) throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		PreparedStatement preparedStatement = null;

		try {
			// Declare sql statement and create PreparedStatement for it.
			String sqlStatement = "insert into `hotel_management`.`service` " +
					"(`name`, `description`, `price`, `notes`) values (?, ?, ?, ?)";
			preparedStatement = connection.prepareStatement(sqlStatement);

			// Set values for PreparedStatement.
			preparedStatement.setNString(1, entity.getName());
			preparedStatement.setString(2, entity.getDescription());
			preparedStatement.setInt(3, entity.getPrice());
			preparedStatement.setString(4, entity.getNote());

			// Execute queries.
			connection.setAutoCommit(false);
			preparedStatement.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			System.out.println("ServiceDAO.java - insert - catch - " + e.getMessage());
			System.out.println("ServiceDAO.java - insert - catch - " + Arrays.toString(e.getStackTrace()));

			try {
				connection.rollback();
			} catch (SQLException ex) {
				System.out.println("ServiceDAO.java - insert - catch/catch - " + ex.getMessage());
				System.out.println("ServiceDAO.java - insert - catch/catch - " + Arrays.toString(ex.getStackTrace()));
			}

			throw DBConnectionException.INSTANCE;
		} finally {
			try {
				connection.setAutoCommit(true);

				if (preparedStatement != null)
					preparedStatement.close();
			} catch (SQLException e) {
				System.out.println("ServiceDAO.java - insert - finally/catch - " + e.getMessage());
				System.out.println("ServiceDAO.java - insert - finally/catch - " + Arrays.toString(e.getStackTrace()));
			}
		}
	}

	@Override
	public void update(Service entity) throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		PreparedStatement preparedStatementUpdateService = null;
		PreparedStatement preparedStatementUpdateServiceInvoice = null;

		try {
			// Declare sql statements and create PreparedStatement for these.
			String updateServiceStatement = "update `hotel_management`.`service` " +
					"set `name` = ?, `description` = ?, `price` = ?, `notes` = ? where `id` = ?";
			String updateServiceInvoiceStatement = "update `hotel_management`.`service_invoice` " +
					"set `service_name` = ? where `service_id` = ?";

			preparedStatementUpdateService = connection.prepareStatement(updateServiceStatement);
			preparedStatementUpdateServiceInvoice = connection.prepareStatement(updateServiceInvoiceStatement);

			// Set values for PreparedStatement.
			preparedStatementUpdateService.setNString(1, entity.getName());
			preparedStatementUpdateService.setNString(2, entity.getDescription());
			preparedStatementUpdateService.setInt(3, entity.getPrice());
			preparedStatementUpdateService.setNString(4, entity.getNote());
			preparedStatementUpdateService.setInt(5, entity.getId());

			preparedStatementUpdateServiceInvoice.setNString(1, entity.getName());
			preparedStatementUpdateServiceInvoice.setInt(2, entity.getId());

			// Execute queries.
			connection.setAutoCommit(false);
			preparedStatementUpdateService.executeUpdate();
			preparedStatementUpdateServiceInvoice.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			System.out.println("ServiceDAO.java - update - catch - " + e.getMessage());
			System.out.println("ServiceDAO.java - update - catch - " + Arrays.toString(e.getStackTrace()));

			try {
				connection.rollback();
			} catch (SQLException ex) {
				System.out.println("ServiceDAO.java - update - catch/catch - " + ex.getMessage());
				System.out.println("ServiceDAO.java - update - catch/catch - " + Arrays.toString(ex.getStackTrace()));
			}

			throw DBConnectionException.INSTANCE;
		} finally {
			try {
				connection.setAutoCommit(true);

				if (preparedStatementUpdateService != null)
					preparedStatementUpdateService.close();
				if (preparedStatementUpdateServiceInvoice != null)
					preparedStatementUpdateServiceInvoice.close();
			} catch (SQLException e) {
				System.out.println("ServiceDAO.java - update - finally/catch - " + e.getMessage());
				System.out.println("ServiceDAO.java - update - finally/catch - " + Arrays.toString(e.getStackTrace()));
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
			String sqlStatement = "delete from `hotel_management`.`service` where `id` = " + id;
			statement = connection.createStatement();

			connection.setAutoCommit(false);
			statement.executeUpdate(sqlStatement);
			connection.commit();
		} catch (SQLException e) {
			System.out.println("ServiceDAO.java - delete - catch - " + e.getMessage());
			System.out.println("ServiceDAO.java - delete - catch - " + Arrays.toString(e.getStackTrace()));

			try {
				connection.rollback();
			} catch (SQLException ex) {
				System.out.println("ServiceDAO.java - delete - catch/catch - " + ex.getMessage());
				System.out.println("ServiceDAO.java - delete - catch/catch - " + Arrays.toString(ex.getStackTrace()));
			}

			throw DBConnectionException.INSTANCE;
		} finally {
			try {
				connection.setAutoCommit(true);

				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				System.out.println("ServiceDAO.java - delete - finally/catch - " + e.getMessage());
				System.out.println("ServiceDAO.java - delete - finally/catch - " + Arrays.toString(e.getStackTrace()));
			}
		}
	}

	public boolean isExistingServiceName(String serviceName) throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();
		if (connection == null)
			throw DBConnectionException.INSTANCE;

		boolean isExisting = false;
		PreparedStatement preparedStatement = null;

		try {
			ResultSet resultSet;
			String sqlStatement = "select * from `hotel_management`.`service` where `name` = ?";

			preparedStatement = connection.prepareStatement(sqlStatement);
			preparedStatement.setNString(1, serviceName);

			resultSet = preparedStatement.executeQuery();
			isExisting = resultSet.next();  // next() return true if this value is existing, otherwise return false.
		} catch (SQLException e) {
			System.out.println("ServiceDAO.java - isExistingServiceName - catch - " + e.getMessage());
			System.out.println("ServiceDAO.java - isExistingServiceName - catch - " + Arrays.toString(e.getStackTrace()));
			throw DBConnectionException.INSTANCE;
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (SQLException e) {
				System.out.println("ServiceDAO.java - isExistingServiceName - finally/catch - " + e.getMessage());
				System.out.println("ServiceDAO.java - isExistingServiceName - finally/catch - " + Arrays.toString(e.getStackTrace()));
			}
		}

		return isExisting;
	}

}
