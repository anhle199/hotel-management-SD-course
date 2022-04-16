package dao;

import db.DBConnectionException;
import db.SingletonDBConnection;
import models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class UserDAO implements DAO<User, Integer> {

	@Override
	public ArrayList<User> getAll() throws DBConnectionException {
		return null;
	}

	@Override
	public Optional<User> get(Integer id) throws DBConnectionException {
		return Optional.empty();
	}

	public Optional<User> getByUsername(String username) throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		Optional<User> userOptional = Optional.empty();
		PreparedStatement preparedStatement = null;

		try {
			String sqlStatement = "select * from `hotel_management`.`user` where `username` = ?;";
			preparedStatement = connection.prepareStatement(sqlStatement);

			preparedStatement.setString(1, username);
			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				userOptional = Optional.of(new User(
						resultSet.getInt("id"),
						resultSet.getString("username"),
						resultSet.getString("password"),
						resultSet.getByte("role"),
						resultSet.getString("fullName"),
						resultSet.getByte("gender"),
						resultSet.getShort("yearOfBirth")
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
				System.out.println("UserDAO.java - getByUsername - catch finally - " + e.getMessage());
				System.out.println("UserDAO.java - getByUsername - catch finally - " + Arrays.toString(e.getStackTrace()));
			}
		}

		return userOptional;
	}

	@Override
	public void create(User entity) throws DBConnectionException {

	}

	@Override
	public void update(User entity) throws DBConnectionException {

	}

	@Override
	public void delete(User entity) throws DBConnectionException {

	}

}
