package dao;

import db.DBConnectionException;
import db.SingletonDBConnection;
import models.User;
import utils.RoleManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class UserDAO implements DAO<User, Integer> {

	@Override
	public ArrayList<User> getAll() throws DBConnectionException {
		return null;
	}

	public ArrayList<User> getAllByRole(RoleManager.RoleEnum role) throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		ArrayList<User> userList = new ArrayList<>();
		PreparedStatement preparedStatement = null;

		try {
			String sqlStatement = "select * from `hotel_management`.`user` where `role` = ?";
			preparedStatement = connection.prepareStatement(sqlStatement);

			preparedStatement.setByte(1, role.byteValue());
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				userList.add(
						new User(
								resultSet.getInt("id"),
								resultSet.getString("username"),
								resultSet.getString("password"),
								resultSet.getByte("role"),
								resultSet.getNString("full_name"),
								resultSet.getByte("gender"),
								resultSet.getShort("year_of_birth")
						)
				);
			}
		} catch (SQLException e) {
			System.out.println("UserDAO.java - getAllByRole - catch - " + e.getMessage());
			System.out.println("UserDAO.java - getAllByRole - catch - " + Arrays.toString(e.getStackTrace()));
			throw DBConnectionException.INSTANCE;
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (SQLException e) {
				System.out.println("UserDAO.java - getAllByRole - finally/catch - " + e.getMessage());
				System.out.println("UserDAO.java - getAllByRole - finally/catch - " + Arrays.toString(e.getStackTrace()));
			}
		}

		return userList;
	}

	@Override
	public Optional<User> get(Integer id) throws DBConnectionException {
		return Optional.empty();
	}

	public Optional<User> getByUsernameAndEncodedPassword(
			String username,
			String encodedPassword
	) throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		Optional<User> userOptional = Optional.empty();
		PreparedStatement preparedStatement = null;

		try {
			String sqlStatement = "select * from `hotel_management`.`user` where `username` = ? and `password` = ?;";
			preparedStatement = connection.prepareStatement(sqlStatement);

			preparedStatement.setString(1, username);
			preparedStatement.setString(2, encodedPassword);
			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				userOptional = Optional.of(new User(
						resultSet.getInt("id"),
						resultSet.getString("username"),
						resultSet.getString("password"),
						resultSet.getByte("role"),
						resultSet.getString("full_name"),
						resultSet.getByte("gender"),
						resultSet.getShort("year_of_birth")
				));
			}
		} catch (SQLException e) {
			System.out.println("UserDAO.java - getByUsernameAndEncodedPassword - catch - " + e.getMessage());
			System.out.println("UserDAO.java - getByUsernameAndEncodedPassword - catch - " + Arrays.toString(e.getStackTrace()));
			throw DBConnectionException.INSTANCE;
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (SQLException e) {
				System.out.println("UserDAO.java - getByUsernameAndEncodedPassword - finally/catch - " + e.getMessage());
				System.out.println("UserDAO.java - getByUsernameAndEncodedPassword - finally/catch - " + Arrays.toString(e.getStackTrace()));
			}
		}

		return userOptional;
	}

	public Optional<User> getByUsername(String username) throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		Optional<User> userOptional = Optional.empty();
		PreparedStatement preparedStatement = null;

		try {
			String sqlStatement = "select * from `hotel_management`.`user` where `username` = ?";
			preparedStatement = connection.prepareStatement(sqlStatement);

			preparedStatement.setString(1, username);
			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				userOptional = Optional.of(new User(
						resultSet.getInt("id"),
						resultSet.getString("username"),
						resultSet.getString("password"),
						resultSet.getByte("role"),
						resultSet.getString("full_name"),
						resultSet.getByte("gender"),
						resultSet.getShort("year_of_birth")
				));
			}
		} catch (SQLException e) {
			System.out.println("UserDAO.java - getByUsername - catch - " + e.getMessage());
			System.out.println("UserDAO.java - getByUsername - catch - " + Arrays.toString(e.getStackTrace()));
			throw DBConnectionException.INSTANCE;
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (SQLException e) {
				System.out.println("UserDAO.java - getByUsername - finally/catch - " + e.getMessage());
				System.out.println("UserDAO.java - getByUsername - finally/catch - " + Arrays.toString(e.getStackTrace()));
			}
		}

		return userOptional;
	}

	@Override
	public void insert(User entity) throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		PreparedStatement preparedStatement = null;

		try {
			// Declare sql statement and create PreparedStatement for it.
			String sqlStatement = "insert into `hotel_management`.`user` " +
					"(`username`, `password`, `role`, `full_name`, `gender`, `year_of_birth`) values (?, ?, ?, ?, ?, ?)";
			preparedStatement = connection.prepareStatement(sqlStatement);

			// Set values for PreparedStatement.
			preparedStatement.setString(1, entity.getUsername());
			preparedStatement.setString(2, entity.getPassword());
			preparedStatement.setByte(3, entity.getRole());
			preparedStatement.setNString(4, entity.getFullName());
			preparedStatement.setByte(5, entity.getGender());
			preparedStatement.setShort(6, entity.getYearOfBirth());

			// Execute queries.
			connection.setAutoCommit(false);
			preparedStatement.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			System.out.println("UserDAO.java - insert - catch - " + e.getMessage());
			System.out.println("UserDAO.java - insert - catch - " + Arrays.toString(e.getStackTrace()));

			try {
				connection.rollback();
			} catch (SQLException ex) {
				System.out.println("UserDAO.java - insert - catch/catch - " + ex.getMessage());
				System.out.println("UserDAO.java - insert - catch/catch - " + Arrays.toString(ex.getStackTrace()));
			}

			throw DBConnectionException.INSTANCE;
		} finally {
			try {
				connection.setAutoCommit(true);

				if (preparedStatement != null)
					preparedStatement.close();
			} catch (SQLException e) {
				System.out.println("UserDAO.java - insert - finally/catch - " + e.getMessage());
				System.out.println("UserDAO.java - insert - finally/catch - " + Arrays.toString(e.getStackTrace()));
			}
		}
	}

	@Override
	public void update(User entity) throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		PreparedStatement preparedStatementUpdateUser = null;

		try {
			// Declare sql statement and create PreparedStatement for it.
			String updateServiceStatement = "update `hotel_management`.`user`" +
					" set `username` = ?, `password` = ?, `role` = ?," +
					" `full_name` = ?, `gender` = ?, `year_of_birth` = ? where `id` = ?";

			preparedStatementUpdateUser = connection.prepareStatement(updateServiceStatement);

			// Set values for PreparedStatement.
			preparedStatementUpdateUser.setString(1, entity.getUsername());
			preparedStatementUpdateUser.setString(2, entity.getPassword());
			preparedStatementUpdateUser.setByte(3, entity.getRole());
			preparedStatementUpdateUser.setNString(4, entity.getFullName());
			preparedStatementUpdateUser.setByte(5, entity.getGender());
			preparedStatementUpdateUser.setShort(6, entity.getYearOfBirth());
			preparedStatementUpdateUser.setInt(7, entity.getId());

			// Execute querie.
			connection.setAutoCommit(false);
			preparedStatementUpdateUser.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			System.out.println("UserDAO.java - update - catch - " + e.getMessage());
			System.out.println("UserDAO.java - update - catch - " + Arrays.toString(e.getStackTrace()));

			try {
				connection.rollback();
			} catch (SQLException ex) {
				System.out.println("UserDAO.java - update - catch/catch - " + ex.getMessage());
				System.out.println("UserDAO.java - update - catch/catch - " + Arrays.toString(ex.getStackTrace()));
			}

			throw DBConnectionException.INSTANCE;
		} finally {
			try {
				connection.setAutoCommit(true);

				if (preparedStatementUpdateUser != null)
					preparedStatementUpdateUser.close();
			} catch (SQLException e) {
				System.out.println("UserDAO.java - update - finally/catch - " + e.getMessage());
				System.out.println("UserDAO.java - update - finally/catch - " + Arrays.toString(e.getStackTrace()));
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
			String sqlStatement = "delete from `hotel_management`.`user` where `id` = " + id;
			statement = connection.createStatement();

			connection.setAutoCommit(false);
			statement.executeUpdate(sqlStatement);
			connection.commit();
		} catch (SQLException e) {
			System.out.println("UserDAO.java - delete - catch - " + e.getMessage());
			System.out.println("UserDAO.java - delete - catch - " + Arrays.toString(e.getStackTrace()));

			try {
				connection.rollback();
			} catch (SQLException ex) {
				System.out.println("UserDAO.java - delete - catch/catch - " + ex.getMessage());
				System.out.println("UserDAO.java - delete - catch/catch - " + Arrays.toString(ex.getStackTrace()));
			}

			throw DBConnectionException.INSTANCE;
		} finally {
			try {
				connection.setAutoCommit(true);

				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				System.out.println("UserDAO.java - delete - finally/catch - " + e.getMessage());
				System.out.println("UserDAO.java - delete - finally/catch - " + Arrays.toString(e.getStackTrace()));
			}
		}
	}

	public boolean isExistingUsername(String username) throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();
		if (connection == null)
			throw DBConnectionException.INSTANCE;

		boolean isExisting = false;
		PreparedStatement preparedStatement = null;

		try {
			ResultSet resultSet;
			String sqlStatement = "select * from `hotel_management`.`user` where `username` = ?";

			preparedStatement = connection.prepareStatement(sqlStatement);
			preparedStatement.setString(1, username);

			resultSet = preparedStatement.executeQuery();
			isExisting = resultSet.next();  // next() return true if this value is existing, otherwise return false.
		} catch (SQLException e) {
			System.out.println("UserDAO.java - isExistingUsername - catch - " + e.getMessage());
			System.out.println("UserDAO.java - isExistingUsername - catch - " + Arrays.toString(e.getStackTrace()));
			throw DBConnectionException.INSTANCE;
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (SQLException e) {
				System.out.println("UserDAO.java - isExistingUsername - finally/catch - " + e.getMessage());
				System.out.println("UserDAO.java - isExistingUsername - finally/catch - " + Arrays.toString(e.getStackTrace()));
			}
		}

		return isExisting;
	}

}
