package dao;

import db.DBConnectionException;
import db.SingletonDBConnection;
import models.RentalInvoice;
import models.RentalReceipt;
import utils.Pair;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class RentalReceiptDAO implements DAO<RentalReceipt, Integer> {

	@Override
	public ArrayList<RentalReceipt> getAll() throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		ArrayList<RentalReceipt> rentalReceiptList = new ArrayList<>();
		Statement statement = null;

		try {
			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("select * from `hotel_management`.`rental_receipt`");

			while (resultSet.next()) {
				rentalReceiptList.add(
						new RentalReceipt(
								resultSet.getInt("id"),
								resultSet.getTimestamp("start_date"),
								resultSet.getTimestamp("end_date"),
								resultSet.getInt("price"),
								resultSet.getInt("total_price"),
								resultSet.getNString("room_name"),
								resultSet.getString("room_type_name"),
								resultSet.getInt("room_id")
						)
				);
			}
		} catch (SQLException e) {
			System.out.println("RentalReceiptDAO.java - getAll - catch - " + e.getMessage());
			System.out.println("RentalReceiptDAO.java - getAll - catch - " + Arrays.toString(e.getStackTrace()));
			throw DBConnectionException.INSTANCE;
		} finally {
			try {
				if (statement != null)
					statement.close();
			} catch (SQLException e) {
				System.out.println("RentalReceiptDAO.java - getAll - finally/catch - " + e.getMessage());
				System.out.println("RentalReceiptDAO.java - getAll - finally/catch - " + Arrays.toString(e.getStackTrace()));
			}
		}

		return rentalReceiptList;
	}

	@Override
	public Optional<RentalReceipt> get(Integer id) throws DBConnectionException {
		return Optional.empty();
	}

	@Override
	public void insert(RentalReceipt entity) throws DBConnectionException {

	}

	public void insert(RentalReceipt entity, int rentalInvoiceId) throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		PreparedStatement preparedStatementInsertRentalReceipt = null;
		PreparedStatement preparedStatementUpdatePaymentStatus = null;

		try {
			// Declare sql statement and create PreparedStatement for it.
			String insertRentalReceiptStatement = "insert into `hotel_management`.`rental_receipt` " +
					"(`start_date`, `end_date`, `price`, `total_price`, `room_name`," +
					" `room_type_name`, `room_id`) values (?, ?, ?, ?, ?, ?, ?)";
			String updatePaymentStatusStatement = "update `hotel_management`.`rental_invoice` set `is_paid` = ? where `id` = ?";

			preparedStatementInsertRentalReceipt = connection.prepareStatement(insertRentalReceiptStatement);
			preparedStatementUpdatePaymentStatus = connection.prepareStatement(updatePaymentStatusStatement);

			// Set values for PreparedStatement.
			preparedStatementInsertRentalReceipt.setTimestamp(1, entity.getStartDate());
			preparedStatementInsertRentalReceipt.setTimestamp(2, entity.getEndDate());
			preparedStatementInsertRentalReceipt.setInt(3, entity.getPrice());
			preparedStatementInsertRentalReceipt.setInt(4, entity.getTotalPrice());
			preparedStatementInsertRentalReceipt.setNString(5, entity.getRoomName());
			preparedStatementInsertRentalReceipt.setNString(6, entity.getRoomTypeName());

			preparedStatementUpdatePaymentStatus.setByte(1, RentalInvoice.PaymentStatusEnum.PAID.byteValue());
			preparedStatementUpdatePaymentStatus.setInt(2, rentalInvoiceId);

			if (entity.getRoomId() == 0 || entity.getRoomId() == -1) {
				preparedStatementInsertRentalReceipt.setNull(7, Types.INTEGER);
			} else {
				preparedStatementInsertRentalReceipt.setInt(7, entity.getRoomId());
			}

			// Execute queries.
			connection.setAutoCommit(false);
			preparedStatementInsertRentalReceipt.executeUpdate();
			preparedStatementUpdatePaymentStatus.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			System.out.println("RentalReceiptDAO.java - insert 2 params - catch - " + e.getMessage());
			System.out.println("RentalReceiptDAO.java - insert 2 params - catch - " + Arrays.toString(e.getStackTrace()));

			try {
				connection.rollback();
			} catch (SQLException ex) {
				System.out.println("RentalReceiptDAO.java - insert 2 params - catch/catch - " + ex.getMessage());
				System.out.println("RentalReceiptDAO.java - insert 2 params - catch/catch - " + Arrays.toString(ex.getStackTrace()));
			}

			throw DBConnectionException.INSTANCE;
		} finally {
			try {
				connection.setAutoCommit(true);

				if (preparedStatementInsertRentalReceipt != null)
					preparedStatementInsertRentalReceipt.close();
				if (preparedStatementUpdatePaymentStatus != null)
					preparedStatementUpdatePaymentStatus.close();
			} catch (SQLException e) {
				System.out.println("RentalReceiptDAO.java - insert 2 params - finally/catch - " + e.getMessage());
				System.out.println("RentalReceiptDAO.java - insert 2 params - finally/catch - " + Arrays.toString(e.getStackTrace()));
			}
		}
	}

	@Override
	public void update(RentalReceipt entity) throws DBConnectionException {

	}

	@Override
	public void delete(Integer id) throws DBConnectionException {

	}

	public ArrayList<Pair<String, Number>> getRevenueForEachRoomTypeByMonthAndYear(
			int month, int year
	) throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		ArrayList<Pair<String, Number>> rowKeysAndStatsValues = new ArrayList<>();
		PreparedStatement preparedStatement = null;

		try {
			String sqlStatement = "select `room_type_name`, sum(`total_price`) as 'revenue'" +
					" from `hotel_management`.`rental_receipt`" +
					" where (month(`start_date`) = ? and year(`start_date`) = ?)" +
					" and (month(`end_date`) = ? and year(`end_date`) = ?)" +
					" group by `room_type_name`";

			preparedStatement = connection.prepareStatement(sqlStatement);
			preparedStatement.setInt(1, month);
			preparedStatement.setInt(2, year);
			preparedStatement.setInt(3, month);
			preparedStatement.setInt(4, year);

			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				rowKeysAndStatsValues.add(new Pair<>(
						resultSet.getString("room_type_name"),
						resultSet.getInt("revenue")
				));
			}
		} catch (SQLException e) {
			System.out.println("RentalReceiptDAO.java - getRevenueForEachRoomTypeByMonthAndYear - catch - " + e.getMessage());
			System.out.println("RentalReceiptDAO.java - getRevenueForEachRoomTypeByMonthAndYear - catch - " + Arrays.toString(e.getStackTrace()));
			throw DBConnectionException.INSTANCE;
		} finally {

			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (SQLException e) {
				System.out.println("RentalReceiptDAO.java - getRevenueForEachRoomTypeByMonthAndYear - finally/catch - " + e.getMessage());
				System.out.println("RentalReceiptDAO.java - getRevenueForEachRoomTypeByMonthAndYear - finally/catch - " + Arrays.toString(e.getStackTrace()));
			}
		}

		return rowKeysAndStatsValues;
	}

	public ArrayList<Pair<String, Number>> getTopFiveRoomOccupancyRateByMonthAndYear(
			int month, int year
	) throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		ArrayList<Pair<String, Number>> rowKeysAndStatsValues = new ArrayList<>();
		PreparedStatement preparedStatement = null;

		try {
			String sqlStatement = "select `room_name`, count(*) as 'rented_time_count'" +
					" from `hotel_management`.`rental_receipt`" +
					" where (month(`start_date`) = ? and year(`start_date`) = ?)" +
					" and (month(`end_date`) = ? and year(`end_date`) = ?)" +
					" group by `room_name`";

			preparedStatement = connection.prepareStatement(sqlStatement);
			preparedStatement.setInt(1, month);
			preparedStatement.setInt(2, year);
			preparedStatement.setInt(3, month);
			preparedStatement.setInt(4, year);

			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next() && rowKeysAndStatsValues.size() < 5) {
				rowKeysAndStatsValues.add(new Pair<>(
						resultSet.getString("room_name"),
						resultSet.getInt("rented_time_count")
				));
			}
		} catch (SQLException e) {
			System.out.println("RentalReceiptDAO.java - getTopFiveRoomOccupancyRateByMonthAndYear - catch - " + e.getMessage());
			System.out.println("RentalReceiptDAO.java - getTopFiveRoomOccupancyRateByMonthAndYear - catch - " + Arrays.toString(e.getStackTrace()));
			throw DBConnectionException.INSTANCE;
		} finally {

			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (SQLException e) {
				System.out.println("RentalReceiptDAO.java - getTopFiveRoomOccupancyRateByMonthAndYear - finally/catch - " + e.getMessage());
				System.out.println("RentalReceiptDAO.java - getTopFiveRoomOccupancyRateByMonthAndYear - finally/catch - " + Arrays.toString(e.getStackTrace()));
			}
		}

		return rowKeysAndStatsValues;
	}

}
