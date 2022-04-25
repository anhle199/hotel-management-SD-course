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

	@Override
	public void insert(User entity) throws DBConnectionException {

	}

	@Override
	public void update(User entity) throws DBConnectionException {

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
