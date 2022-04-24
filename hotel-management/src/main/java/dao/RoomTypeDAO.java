package dao;

import db.DBConnectionException;
import db.SingletonDBConnection;
import models.RoomType;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class RoomTypeDAO implements DAO<RoomType, Integer> {

	@Override
	public ArrayList<RoomType> getAll() throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		ArrayList<RoomType> roomTypeList = new ArrayList<>();
		Statement statement = null;

		try {
			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("select * from `hotel_management`.`room_type`");

			while (resultSet.next()) {
				roomTypeList.add(
						new RoomType(
								resultSet.getInt("id"),
								resultSet.getNString("name"),
								resultSet.getInt("price")
						)
				);
			}
		} catch (SQLException e) {
			System.out.println("RoomTypeDAO.java - getAll - catch - " + e.getMessage());
			System.out.println("RoomTypeDAO.java - getAll - catch - " + Arrays.toString(e.getStackTrace()));
			throw DBConnectionException.INSTANCE;
		} finally {
			try {
				if (statement != null)
					statement.close();
			} catch (SQLException e) {
				System.out.println("RoomTypeDAO.java - getAll - finally/catch - " + e.getMessage());
				System.out.println("RoomTypeDAO.java - getAll - finally/catch - " + Arrays.toString(e.getStackTrace()));
			}
		}

		return roomTypeList;
	}

	@Override
	public Optional<RoomType> get(Integer id) throws DBConnectionException {
		return Optional.empty();
	}

	@Override
	public void insert(RoomType entity) throws DBConnectionException {

	}

	@Override
	public void update(RoomType entity) throws DBConnectionException {

	}

	@Override
	public void delete(Integer id) throws DBConnectionException {

	}

}
