package dao;

import db.DBConnectionException;
import db.SingletonDBConnection;
import models.RentalInvoice;
import models.Room;

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

		PreparedStatement preparedStatementInsertRentalInvoice = null;
		PreparedStatement preparedStatementUpdateRoomStatus = null;

		try {
			// Declare sql statements and create PreparedStatement for it.
			String insertRentalInvoiceStatement = "insert into `hotel_management`.`rental_invoice` " +
					"(`start_date`, `end_date`, `room_id`, `room_name`," +
					" `room_type_id`, `room_type_name`, `room_type_price`, `customer_name`," +
					" `identity_number`, `address`, `customer_type`, `is_paid`) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 0)";
			String updateRoomStatusStatement = "update `hotel_management`.`room` set `status` = ? where `id` = ?";

			preparedStatementInsertRentalInvoice = connection.prepareStatement(insertRentalInvoiceStatement);
			preparedStatementUpdateRoomStatus = connection.prepareStatement(updateRoomStatusStatement);

			// Set values for PreparedStatement.
			preparedStatementInsertRentalInvoice.setTimestamp(1, entity.getStartDate());
			preparedStatementInsertRentalInvoice.setTimestamp(2, entity.getEndDate());
			preparedStatementInsertRentalInvoice.setInt(3, entity.getRoomId());
			preparedStatementInsertRentalInvoice.setNString(4, entity.getRoomName());
			preparedStatementInsertRentalInvoice.setInt(5, entity.getRoomTypeId());
			preparedStatementInsertRentalInvoice.setNString(6, entity.getRoomTypeName());
			preparedStatementInsertRentalInvoice.setInt(7, entity.getRoomTypePrice());
			preparedStatementInsertRentalInvoice.setNString(8, entity.getCustomerName());
			preparedStatementInsertRentalInvoice.setString(9, entity.getIdentityNumber());
			preparedStatementInsertRentalInvoice.setNString(10, entity.getAddress());
			preparedStatementInsertRentalInvoice.setInt(11, entity.getCustomerType());
			preparedStatementUpdateRoomStatus.setByte(1, Room.RoomStatusEnum.RESERVED.byteValue());
			preparedStatementUpdateRoomStatus.setInt(2, entity.getRoomId());

			connection.setAutoCommit(false);
			preparedStatementInsertRentalInvoice.executeUpdate();
			preparedStatementUpdateRoomStatus.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			System.out.println("RentalInvoiceDAO.java - insert - catch - " + e.getMessage());
			System.out.println("RentalInvoiceDAO.java - insert - catch - " + Arrays.toString(e.getStackTrace()));

			try {
				connection.rollback();
			} catch (SQLException ex) {
				System.out.println("RentalInvoiceDAO.java - insert - catch/catch - " + ex.getMessage());
				System.out.println("RentalInvoiceDAO.java - insert - catch/catch - " + Arrays.toString(ex.getStackTrace()));
			}

			throw DBConnectionException.INSTANCE;
		} finally {
			try {
				connection.setAutoCommit(true);

				if (preparedStatementInsertRentalInvoice != null)
					preparedStatementInsertRentalInvoice.close();
				if (preparedStatementUpdateRoomStatus != null)
					preparedStatementUpdateRoomStatus.close();
			} catch (SQLException e) {
				System.out.println("RentalInvoiceDAO.java - insert - finally/catch - " + e.getMessage());
				System.out.println("RentalInvoiceDAO.java - insert - finally/catch - " + Arrays.toString(e.getStackTrace()));
			}
		}
	}

	@Override
	public void update(RentalInvoice entity) throws DBConnectionException {

	}

	public void update(RentalInvoice entity, int oldRoomId) throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		PreparedStatement preparedStatementUpdateRentalInvoice = null;
		PreparedStatement preparedStatementUpdateOldRoomStatus = null;
		PreparedStatement preparedStatementUpdateNewRoomStatus = null;

		try {
			// Declare sql statements and create PreparedStatement for it.
			String updateRentalInvoiceStatement = "update `hotel_management`.`rental_invoice` " +
					"set `start_date` = ?, `end_date` = ?, `room_id` = ?, `room_name` = ?, " +
					"`room_type_id` = ?, `room_type_name` = ?, `room_type_price` = ?, " +
					"`customer_name` = ?, `identity_number` = ?, `address` = ?, `customer_type` = ? " +
					"where `id` = ?";

			if (entity.getRoomId() != oldRoomId) {
				String updateOldRoomStatusStatement = "update `hotel_management`.`room` set `status` = ? where `id` = ?";
				String updateNewRoomStatusStatement = "update `hotel_management`.`room` set `status` = ? where `id` = ?";

				preparedStatementUpdateOldRoomStatus = connection.prepareStatement(updateOldRoomStatusStatement);
				preparedStatementUpdateNewRoomStatus = connection.prepareStatement(updateNewRoomStatusStatement);

				preparedStatementUpdateOldRoomStatus.setByte(1, Room.RoomStatusEnum.AVAILABLE.byteValue());
				preparedStatementUpdateOldRoomStatus.setInt(2, oldRoomId);
				preparedStatementUpdateNewRoomStatus.setByte(1, Room.RoomStatusEnum.RESERVED.byteValue());
				preparedStatementUpdateNewRoomStatus.setInt(2, entity.getRoomId());
			}

			preparedStatementUpdateRentalInvoice = connection.prepareStatement(updateRentalInvoiceStatement);

			// Set values for PreparedStatement.
			preparedStatementUpdateRentalInvoice.setTimestamp(1, entity.getStartDate());
			preparedStatementUpdateRentalInvoice.setTimestamp(2, entity.getEndDate());
			preparedStatementUpdateRentalInvoice.setNString(4, entity.getRoomName());
			preparedStatementUpdateRentalInvoice.setInt(5, entity.getRoomTypeId());
			preparedStatementUpdateRentalInvoice.setNString(6, entity.getRoomTypeName());
			preparedStatementUpdateRentalInvoice.setInt(7, entity.getRoomTypePrice());
			preparedStatementUpdateRentalInvoice.setNString(8, entity.getCustomerName());
			preparedStatementUpdateRentalInvoice.setString(9, entity.getIdentityNumber());
			preparedStatementUpdateRentalInvoice.setNString(10, entity.getAddress());
			preparedStatementUpdateRentalInvoice.setInt(11, entity.getCustomerType());
			preparedStatementUpdateRentalInvoice.setInt(12, entity.getId());

			if (entity.getRoomId() == 0 || entity.getRoomId() == -1) {
				preparedStatementUpdateRentalInvoice.setNull(3, Types.INTEGER);
			} else {
				preparedStatementUpdateRentalInvoice.setInt(3, entity.getRoomId());
			}

			// Execute queries.
			connection.setAutoCommit(false);
			if (entity.getRoomId() != oldRoomId) {
				preparedStatementUpdateOldRoomStatus.executeUpdate();
				preparedStatementUpdateNewRoomStatus.executeUpdate();
			}
			preparedStatementUpdateRentalInvoice.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			System.out.println("RentalInvoiceDAO.java - update 2 params - catch - " + e.getMessage());
			System.out.println("RentalInvoiceDAO.java - update 2 params - catch - " + Arrays.toString(e.getStackTrace()));

			try {
				connection.rollback();
			} catch (SQLException ex) {
				System.out.println("RentalInvoiceDAO.java - update 2 params - catch/catch - " + ex.getMessage());
				System.out.println("RentalInvoiceDAO.java - update 2 params - catch/catch - " + Arrays.toString(ex.getStackTrace()));
			}

			throw DBConnectionException.INSTANCE;
		} finally {
			try {
				connection.setAutoCommit(true);

				if (preparedStatementUpdateRentalInvoice != null)
					preparedStatementUpdateRentalInvoice.close();
				if (preparedStatementUpdateOldRoomStatus != null)
					preparedStatementUpdateOldRoomStatus.close();
				if (preparedStatementUpdateNewRoomStatus != null)
					preparedStatementUpdateNewRoomStatus.close();
			} catch (SQLException e) {
				System.out.println("RentalInvoiceDAO.java - update 2 params - finally/catch - " + e.getMessage());
				System.out.println("RentalInvoiceDAO.java - update 2 params - finally/catch - " + Arrays.toString(e.getStackTrace()));
			}
		}
	}

	@Override
	public void delete(Integer id) throws DBConnectionException {

	}

	public void delete(Integer id, int roomId, byte newRoomStatus) throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		Statement statementDeleteRentalInvoice = null;
		PreparedStatement preparedStatementUpdateRoomStatus = null;

		try {
			String deleteRentalInvoiceStatement = "delete from `hotel_management`.`rental_invoice` where `id` = " + id;
			statementDeleteRentalInvoice = connection.createStatement();

			connection.setAutoCommit(false);
			statementDeleteRentalInvoice.executeUpdate(deleteRentalInvoiceStatement);

			if (roomId > 0 && newRoomStatus != -1) {
				String updateRoomStatusStatement = "update `hotel_management`.`room` set `status` = ? where `id` = ?";
				preparedStatementUpdateRoomStatus = connection.prepareStatement(updateRoomStatusStatement);

				preparedStatementUpdateRoomStatus.setByte(1, newRoomStatus);
				preparedStatementUpdateRoomStatus.setInt(2, roomId);

				preparedStatementUpdateRoomStatus.executeUpdate();
			}
			connection.commit();
		} catch (SQLException e) {
			System.out.println("RentalInvoiceDAO.java - delete 3 params - catch - " + e.getMessage());
			System.out.println("RentalInvoiceDAO.java - delete 3 params - catch - " + Arrays.toString(e.getStackTrace()));

			try {
				connection.rollback();
			} catch (SQLException ex) {
				System.out.println("RentalInvoiceDAO.java - delete 3 params - catch/catch - " + ex.getMessage());
				System.out.println("RentalInvoiceDAO.java - delete 3 params - catch/catch - " + Arrays.toString(ex.getStackTrace()));
			}

			throw DBConnectionException.INSTANCE;
		} finally {
			try {
				connection.setAutoCommit(true);

				if (statementDeleteRentalInvoice != null)
					statementDeleteRentalInvoice.close();
				if (preparedStatementUpdateRoomStatus != null)
					preparedStatementUpdateRoomStatus.close();
			} catch (SQLException e) {
				System.out.println("RentalInvoiceDAO.java - delete 3 params - finally/catch - " + e.getMessage());
				System.out.println("RentalInvoiceDAO.java - delete 3 params - finally/catch - " + Arrays.toString(e.getStackTrace()));
			}
		}
	}

}
