package dao;

import db.DBConnectionException;
import db.SingletonDBConnection;
import models.Room;
import models.RoomType;
import utils.Pair;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class RoomDAO implements DAO<Room, Integer> {

	@Override
	public ArrayList<Room> getAll() throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		ArrayList<Room> roomList = new ArrayList<>();
		Statement statement = null;

		try {
			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("select * from `hotel_management`.`room`");

			while (resultSet.next()) {
				roomList.add(
						new Room(
								resultSet.getInt("id"),
								resultSet.getNString("name"),
								resultSet.getString("description"),
								resultSet.getByte("status"),
								resultSet.getInt("room_type_id")
						)
				);
			}
		} catch (SQLException e) {
			System.out.println("RoomDAO.java - getAll - catch - " + e.getMessage());
			System.out.println("RoomDAO.java - getAll - catch - " + Arrays.toString(e.getStackTrace()));
			throw DBConnectionException.INSTANCE;
		} finally {
			try {
				if (statement != null)
					statement.close();
			} catch (SQLException e) {
				System.out.println("RoomDAO.java - getAll - finally/catch - " + e.getMessage());
				System.out.println("RoomDAO.java - getAll - finally/catch - " + Arrays.toString(e.getStackTrace()));
			}
		}

		return roomList;
	}

	public ArrayList<Pair<Room, RoomType>> getAllWithRoomType() throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		ArrayList<Pair<Room, RoomType>> roomList = new ArrayList<>();
		Statement statement = null;

		try {
			String sqlStatement = "select r.*, rt.name as `room_type_name`, rt.price" +
					" from `hotel_management`.`room` r join `hotel_management`.`room_type` rt" +
					" on r.room_type_id == rt.id";

			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sqlStatement);

			while (resultSet.next()) {
				Room room = new Room(
						resultSet.getInt("id"),
						resultSet.getNString("name"),
						resultSet.getString("description"),
						resultSet.getByte("status"),
						resultSet.getInt("room_type_id")
				);
				RoomType roomType = new RoomType(
						resultSet.getInt("room_type_id"),
						resultSet.getNString("room_type_name"),
						resultSet.getInt("price")
				);

				roomList.add(new Pair<>(room, roomType));
			}
		} catch (SQLException e) {
			System.out.println("RoomDAO.java - getAllWithRoomType - catch - " + e.getMessage());
			System.out.println("RoomDAO.java - getAllWithRoomType - catch - " + Arrays.toString(e.getStackTrace()));
			throw DBConnectionException.INSTANCE;
		} finally {
			try {
				if (statement != null)
					statement.close();
			} catch (SQLException e) {
				System.out.println("RoomDAO.java - getAllWithRoomType - finally/catch - " + e.getMessage());
				System.out.println("RoomDAO.java - getAllWithRoomType - finally/catch - " + Arrays.toString(e.getStackTrace()));
			}
		}

		return roomList;
	}

	@Override
	public Optional<Room> get(Integer id) throws DBConnectionException {
		return Optional.empty();
	}

	@Override
	public void insert(Room entity) throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		PreparedStatement preparedStatement = null;

		try {
			// Declare sql statement and create PreparedStatement for it.
			String sqlStatement = "insert into `hotel_management`.`room` " +
					"(`name`, `description`, `status`, `room_type_id`) values (?, ?, ?, ?)";
			preparedStatement = connection.prepareStatement(sqlStatement);

			// Set values for PreparedStatement.
			preparedStatement.setNString(1, entity.getName());
			preparedStatement.setString(2, entity.getDescription());
			preparedStatement.setByte(3, entity.getStatus());
			preparedStatement.setInt(4, entity.getRoomTypeId());

			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("RoomDAO.java - insert - catch - " + e.getMessage());
			System.out.println("RoomDAO.java - insert - catch - " + Arrays.toString(e.getStackTrace()));

			throw DBConnectionException.INSTANCE;
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (SQLException e) {
				System.out.println("RoomDAO.java - insert - finally/catch - " + e.getMessage());
				System.out.println("RoomDAO.java - insert - finally/catch - " + Arrays.toString(e.getStackTrace()));
			}
		}
	}

	@Override
	public void update(Room entity) throws DBConnectionException {

	}

	@Override
	public void delete(Integer id) throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		PreparedStatement preparedStatementRemoveFKInRentalInvoice = null;
		PreparedStatement preparedStatementRemoveFKInRentalReceipt = null;
		PreparedStatement preparedStatementRemoveFKInServiceInvoice = null;
		PreparedStatement preparedStatementDeleteRoom = null;

		try {
			// Declare sql statements.
			String removeFKInRentalInvoiceStatement = "update `hotel_management`.`rental_invoice` set `room_id` = ? where `room_id` = ?";
			String removeFKInRentalReceiptStatement = "update `hotel_management`.`rental_receipt` set `room_id` = ? where `room_id` = ?";
			String removeFKInServiceInvoiceStatement = "update `hotel_management`.`sevice_invoice` set `room_id` = ? where `room_id` = ?";
			String deleteRoomStatement = "delete from `hotel_management`.`room` where `id` = ?";

			// Create PreparedStatement for sql statements.
			preparedStatementRemoveFKInRentalInvoice = connection.prepareStatement(removeFKInRentalInvoiceStatement);
			preparedStatementRemoveFKInRentalReceipt = connection.prepareStatement(removeFKInRentalReceiptStatement);
			preparedStatementRemoveFKInServiceInvoice = connection.prepareStatement(removeFKInServiceInvoiceStatement);
			preparedStatementDeleteRoom = connection.prepareStatement(deleteRoomStatement);

			// Set values for PrepareStatement
			preparedStatementRemoveFKInRentalInvoice.setNull(1, Types.INTEGER);
			preparedStatementRemoveFKInRentalInvoice.setInt(2, id);
			preparedStatementRemoveFKInRentalReceipt.setNull(1, Types.INTEGER);
			preparedStatementRemoveFKInRentalReceipt.setInt(2, id);
			preparedStatementRemoveFKInServiceInvoice.setNull(1, Types.INTEGER);
			preparedStatementRemoveFKInServiceInvoice.setInt(2, id);
			preparedStatementDeleteRoom.setInt(1,id);

			// Execute queries.
			connection.setAutoCommit(false);
			preparedStatementRemoveFKInRentalInvoice.executeUpdate();
			preparedStatementRemoveFKInRentalReceipt.executeUpdate();
			preparedStatementRemoveFKInServiceInvoice.executeUpdate();
			preparedStatementDeleteRoom.executeUpdate();
			connection.commit();

		} catch (SQLException e) {
			System.out.println("RoomDAO.java - delete - catch - " + e.getMessage());
			System.out.println("RoomDAO.java - delete - catch - " + Arrays.toString(e.getStackTrace()));

			try {
				connection.rollback();
			} catch (SQLException ex) {
				System.out.println("RoomDAO.java - delete - catch/catch - " + ex.getMessage());
				System.out.println("RoomDAO.java - delete - catch/catch - " + Arrays.toString(ex.getStackTrace()));
			}

			throw DBConnectionException.INSTANCE;
		} finally {
			try {
				connection.setAutoCommit(true);

				// Close PreparedStatement.
				if (preparedStatementRemoveFKInRentalInvoice != null) {
					preparedStatementRemoveFKInRentalInvoice.close();
				}
				if (preparedStatementRemoveFKInRentalReceipt != null) {
					preparedStatementRemoveFKInRentalReceipt.close();
				}
				if (preparedStatementRemoveFKInServiceInvoice != null) {
					preparedStatementRemoveFKInServiceInvoice.close();
				}
				if (preparedStatementDeleteRoom != null) {
					preparedStatementDeleteRoom.close();
				}
			} catch (SQLException e) {
				System.out.println("RoomDAO.java - delete - finally/catch - " + e.getMessage());
				System.out.println("RoomDAO.java - delete - finally/catch - " + Arrays.toString(e.getStackTrace()));
			}
		}

	}

	public ArrayList<Pair<Room, RoomType>> searchByRoomNameReturnWithRoomType(
			String roomNameToken
	) throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		ArrayList<Pair<Room, RoomType>> roomList = new ArrayList<>();
		Statement statement = null;

		try {
			String sqlStatement = "select r.*, rt.name as `room_type_name`, rt.price" +
					" from `hotel_management`.`room` r join `hotel_management`.`room_type` rt" +
					" on r.room_type_id == rt.id" +
					" where r.name like '%" + roomNameToken + "%'";

			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sqlStatement);

			while (resultSet.next()) {
				Room room = new Room(
						resultSet.getInt("id"),
						resultSet.getNString("name"),
						resultSet.getString("description"),
						resultSet.getByte("status"),
						resultSet.getInt("room_type_id")
				);
				RoomType roomType = new RoomType(
						resultSet.getInt("room_type_id"),
						resultSet.getNString("room_type_name"),
						resultSet.getInt("price")
				);

				roomList.add(new Pair<>(room, roomType));
			}
		} catch (SQLException e) {
			System.out.println("RoomDAO.java - searchByRoomNameReturnWithRoomType - catch - " + e.getMessage());
			System.out.println("RoomDAO.java - searchByRoomNameReturnWithRoomType - catch - " + Arrays.toString(e.getStackTrace()));
			throw DBConnectionException.INSTANCE;
		} finally {
			try {
				if (statement != null)
					statement.close();
			} catch (SQLException e) {
				System.out.println("RoomDAO.java - searchByRoomNameReturnWithRoomType - finally/catch - " + e.getMessage());
				System.out.println("RoomDAO.java - searchByRoomNameReturnWithRoomType - finally/catch - " + Arrays.toString(e.getStackTrace()));
			}
		}

		return roomList;
	}

}
