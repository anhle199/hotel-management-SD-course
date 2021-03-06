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

	public ArrayList<Room> getAllByStatus(Room.RoomStatusEnum status) throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		ArrayList<Room> roomList = new ArrayList<>();
		PreparedStatement preparedStatement = null;

		try {
			String sqlStatement = "select * from `hotel_management`.`room` where `status` = ?";
			preparedStatement = connection.prepareStatement(sqlStatement);

			preparedStatement.setByte(1, status.byteValue());
			ResultSet resultSet = preparedStatement.executeQuery();

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
			System.out.println("RoomDAO.java - getAllByStatus - catch - " + e.getMessage());
			System.out.println("RoomDAO.java - getAllByStatus - catch - " + Arrays.toString(e.getStackTrace()));
			throw DBConnectionException.INSTANCE;
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (SQLException e) {
				System.out.println("RoomDAO.java - getAllByStatus - finally/catch - " + e.getMessage());
				System.out.println("RoomDAO.java - getAllByStatus - finally/catch - " + Arrays.toString(e.getStackTrace()));
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
					" on r.room_type_id = rt.id";

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

	public ArrayList<Pair<Room, RoomType>> getAllWithRoomTypeByStatus(
			Room.RoomStatusEnum status
	) throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		ArrayList<Pair<Room, RoomType>> roomList = new ArrayList<>();
		PreparedStatement preparedStatement = null;

		try {
			String sqlStatement = "select r.*, rt.name as `room_type_name`, rt.price" +
					" from `hotel_management`.`room` r join `hotel_management`.`room_type` rt" +
					" on r.room_type_id = rt.id where r.status = ?";

			preparedStatement = connection.prepareStatement(sqlStatement);
			preparedStatement.setByte(1, status.byteValue());
			ResultSet resultSet = preparedStatement.executeQuery();

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
			System.out.println("RoomDAO.java - getAllWithRoomTypeByStatus - catch - " + e.getMessage());
			System.out.println("RoomDAO.java - getAllWithRoomTypeByStatus - catch - " + Arrays.toString(e.getStackTrace()));
			throw DBConnectionException.INSTANCE;
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (SQLException e) {
				System.out.println("RoomDAO.java - getAllWithRoomTypeByStatus - finally/catch - " + e.getMessage());
				System.out.println("RoomDAO.java - getAllWithRoomTypeByStatus - finally/catch - " + Arrays.toString(e.getStackTrace()));
			}
		}

		return roomList;
	}

	@Override
	public Optional<Room> get(Integer id) throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		Optional<Room> roomOptional = Optional.empty();
		PreparedStatement preparedStatement = null;

		try {
			String sqlStatement = "select * from `hotel_management`.`room` where id = ?";
			preparedStatement = connection.prepareStatement(sqlStatement);

			preparedStatement.setInt(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				roomOptional = Optional.of(new Room(
						resultSet.getInt("id"),
						resultSet.getNString("name"),
						resultSet.getString("description"),
						resultSet.getByte("status"),
						resultSet.getInt("room_type_id")
				));
			}
		} catch (SQLException e) {
			System.out.println("RoomDAO.java - get - catch - " + e.getMessage());
			System.out.println("RoomDAO.java - get - catch - " + Arrays.toString(e.getStackTrace()));
			throw DBConnectionException.INSTANCE;
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (SQLException e) {
				System.out.println("RoomDAO.java - get - finally/catch - " + e.getMessage());
				System.out.println("RoomDAO.java - get - finally/catch - " + Arrays.toString(e.getStackTrace()));
			}
		}

		return roomOptional;
	}

	public Optional<Pair<Room, RoomType>> getWithRoomType(Integer id) throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		Optional<Pair<Room, RoomType>> optionalResult = Optional.empty();
		PreparedStatement preparedStatement = null;

		try {
			String sqlStatement = "select r.*, rt.name as `room_type_name`, rt.price" +
					" from `hotel_management`.`room` r join `hotel_management`.`room_type` rt" +
					" on r.room_type_id = rt.id where r.id = ?";

			preparedStatement = connection.prepareStatement(sqlStatement);

			preparedStatement.setInt(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				optionalResult = Optional.of(new Pair<>(
						new Room(
								resultSet.getInt("id"),
								resultSet.getNString("name"),
								resultSet.getString("description"),
								resultSet.getByte("status"),
								resultSet.getInt("room_type_id")
						),
						new RoomType(
								resultSet.getInt("room_type_id"),
								resultSet.getNString("room_type_name"),
								resultSet.getInt("price")
						)
				));
			}
		} catch (SQLException e) {
			System.out.println("RoomDAO.java - getWithRoomType - catch - " + e.getMessage());
			System.out.println("RoomDAO.java - getWithRoomType - catch - " + Arrays.toString(e.getStackTrace()));
			throw DBConnectionException.INSTANCE;
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (SQLException e) {
				System.out.println("RoomDAO.java - getWithRoomType - finally/catch - " + e.getMessage());
				System.out.println("RoomDAO.java - getWithRoomType - finally/catch - " + Arrays.toString(e.getStackTrace()));
			}
		}

		return optionalResult;
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

			// Execute queries.
			connection.setAutoCommit(false);
			preparedStatement.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			System.out.println("RoomDAO.java - insert - catch - " + e.getMessage());
			System.out.println("RoomDAO.java - insert - catch - " + Arrays.toString(e.getStackTrace()));

			try {
				connection.rollback();
			} catch (SQLException ex) {
				System.out.println("RoomDAO.java - insert - catch/catch - " + ex.getMessage());
				System.out.println("RoomDAO.java - insert - catch/catch - " + Arrays.toString(ex.getStackTrace()));
			}

			throw DBConnectionException.INSTANCE;
		} finally {
			try {
				connection.setAutoCommit(true);

				if (preparedStatement != null)
					preparedStatement.close();
			} catch (SQLException e) {
				System.out.println("RoomDAO.java - insert - finally/catch - " + e.getMessage());
				System.out.println("RoomDAO.java - insert - finally/catch - " + Arrays.toString(e.getStackTrace()));
			}
		}
	}

	public void update(Room entity, RoomType roomType) throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		PreparedStatement preparedStatementUpdateRoom = null;
		PreparedStatement preparedStatementUpdateRentalInvoice = null;
		PreparedStatement preparedStatementUpdateRentalReceipt = null;
		PreparedStatement preparedStatementUpdateServiceInvoice = null;

		try {
			// Declare sql statements and create PreparedStatement for these.
			String updateRoomStatement = "update `hotel_management`.`room` " +
					"set `name` = ?, `description` = ?, `status` = ?, `room_type_id` = ? where `id` = ?";
			String updateRentalInvoice = "update `hotel_management`.`rental_invoice` set `room_name` = ?";
			String updateRentalReceipt = "update `hotel_management`.`rental_receipt` set `room_name` = ?";
			String updateServiceInvoice = "update `hotel_management`.`service_invoice` set `room_name` = ? where `room_id` = ?";

			if (entity.getRoomTypeId() == -1) {
				updateRentalInvoice += " where `room_id` = " + entity.getId();
				updateRentalReceipt += " where `room_id` = " + entity.getId();
			} else {
				updateRentalInvoice += ", `room_type_id` = ?, `room_type_name` = ?, `room_type_price` = ? where `room_id` = " + entity.getId();
				updateRentalReceipt += ", `room_type_name` = ? where `room_id` = " + entity.getId();
			}

			preparedStatementUpdateRoom = connection.prepareStatement(updateRoomStatement);
			preparedStatementUpdateRentalInvoice = connection.prepareStatement(updateRentalInvoice);
			preparedStatementUpdateRentalReceipt = connection.prepareStatement(updateRentalReceipt);
			preparedStatementUpdateServiceInvoice = connection.prepareStatement(updateServiceInvoice);

			// Set values for PreparedStatement.
			preparedStatementUpdateRoom.setNString(1, entity.getName());
			preparedStatementUpdateRoom.setString(2, entity.getDescription());
			preparedStatementUpdateRoom.setByte(3, entity.getStatus());
			preparedStatementUpdateRoom.setInt(5, entity.getId());

			preparedStatementUpdateRentalInvoice.setNString(1, entity.getName());
			preparedStatementUpdateRentalReceipt.setNString(1, entity.getName());
			preparedStatementUpdateServiceInvoice.setNString(1, entity.getName());
			preparedStatementUpdateServiceInvoice.setInt(2, entity.getId());

			if (entity.getRoomTypeId() == -1) {
				preparedStatementUpdateRoom.setNull(4, Types.INTEGER);
			} else {
				preparedStatementUpdateRoom.setInt(4, entity.getRoomTypeId());
				preparedStatementUpdateRentalInvoice.setInt(2, roomType.getId());
				preparedStatementUpdateRentalInvoice.setNString(3, roomType.getName());
				preparedStatementUpdateRentalInvoice.setInt(4, roomType.getPrice());
				preparedStatementUpdateRentalReceipt.setNString(2, roomType.getName());
			}

			// Execute queries.
			connection.setAutoCommit(false);
			preparedStatementUpdateRoom.executeUpdate();
			preparedStatementUpdateRentalInvoice.executeUpdate();
			preparedStatementUpdateRentalReceipt.executeUpdate();
			preparedStatementUpdateServiceInvoice.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			System.out.println("RoomDAO.java - update - catch - " + e.getMessage());
			System.out.println("RoomDAO.java - update - catch - " + Arrays.toString(e.getStackTrace()));

			try {
				connection.rollback();
			} catch (SQLException ex) {
				System.out.println("RoomDAO.java - update - catch/catch - " + ex.getMessage());
				System.out.println("RoomDAO.java - update - catch/catch - " + Arrays.toString(ex.getStackTrace()));
			}

			throw DBConnectionException.INSTANCE;
		} finally {
			try {
				connection.setAutoCommit(true);

				if (preparedStatementUpdateRoom != null)
					preparedStatementUpdateRoom.close();
				if (preparedStatementUpdateRentalInvoice != null)
					preparedStatementUpdateRentalInvoice.close();
				if (preparedStatementUpdateRentalReceipt != null)
					preparedStatementUpdateRentalReceipt.close();
				if (preparedStatementUpdateServiceInvoice != null)
					preparedStatementUpdateServiceInvoice.close();
			} catch (SQLException e) {
				System.out.println("RoomDAO.java - update - finally/catch - " + e.getMessage());
				System.out.println("RoomDAO.java - update - finally/catch - " + Arrays.toString(e.getStackTrace()));
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
			String removeFKInServiceInvoiceStatement = "update `hotel_management`.`service_invoice` set `room_id` = ? where `room_id` = ?";
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
					" on r.room_type_id = rt.id" +
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

	public ArrayList<Pair<Room, RoomType>> filterByAndReturnWithRoomType(
			String roomTypeName,
			String statusName,
//			String startDate,
//			String endDate,
//			int startPrice,
//			int endPrice,
			boolean ascendingPrice
	) throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		ArrayList<Pair<Room, RoomType>> roomList = new ArrayList<>();
		PreparedStatement preparedStatement = null;
		// start date: yyyy-MM-dd 00:00:00
		// end date: yyyy-MM-dd 23:59:59
		try {
			String sqlStatement = "select r.*, rt.name as `room_type_name`, rt.price" +
					" from `hotel_management`.`room` r join `hotel_management`.`room_type` rt" +
					" on r.room_type_id = rt.id";

			if (!roomTypeName.equalsIgnoreCase("all"))
				sqlStatement += " and rt.name = '" + roomTypeName + "'";
			if (!statusName.equalsIgnoreCase("all"))
				sqlStatement += " and r.status = " + Room.RoomStatusEnum.valueOfIgnoreCase(statusName).byteValue();

			sqlStatement += " order by rt.price " + (ascendingPrice ? "asc" : "desc");
			System.out.println(sqlStatement);
			preparedStatement = connection.prepareStatement(sqlStatement);
//			preparedStatement.setInt(1, startPrice);
//			preparedStatement.setInt(2, endPrice);

			ResultSet resultSet = preparedStatement.executeQuery();
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
			System.out.println("RoomDAO.java - filterByAndReturnWithRoomType - catch - " + e.getMessage());
			System.out.println("RoomDAO.java - filterByAndReturnWithRoomType - catch - " + Arrays.toString(e.getStackTrace()));
			throw DBConnectionException.INSTANCE;
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (SQLException e) {
				System.out.println("RoomDAO.java - filterByAndReturnWithRoomType - finally/catch - " + e.getMessage());
				System.out.println("RoomDAO.java - filterByAndReturnWithRoomType - finally/catch - " + Arrays.toString(e.getStackTrace()));
			}
		}

		return roomList;
	}

	public boolean isExistingRoomName(String roomName) throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();
		if (connection == null)
			throw DBConnectionException.INSTANCE;

		boolean isExisting = false;
		PreparedStatement preparedStatement = null;

		try {
			ResultSet resultSet;
			String sqlStatement = "select * from `hotel_management`.`room` where `name` = ?";

			preparedStatement = connection.prepareStatement(sqlStatement);
			preparedStatement.setNString(1, roomName);

			resultSet = preparedStatement.executeQuery();
			isExisting = resultSet.next();  // next() return true if this value is existing, otherwise return false.
		} catch (SQLException e) {
			System.out.println("RoomDAO.java - isExistingRoomName - catch - " + e.getMessage());
			System.out.println("RoomDAO.java - isExistingRoomName - catch - " + Arrays.toString(e.getStackTrace()));
			throw DBConnectionException.INSTANCE;
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (SQLException e) {
				System.out.println("RoomDAO.java - isExistingRoomName - finally/catch - " + e.getMessage());
				System.out.println("RoomDAO.java - isExistingRoomName - finally/catch - " + Arrays.toString(e.getStackTrace()));
			}
		}

		return isExisting;
	}

}
