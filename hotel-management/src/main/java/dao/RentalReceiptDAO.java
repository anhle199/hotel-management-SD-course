package dao;

import db.DBConnectionException;
import db.SingletonDBConnection;
import models.RentalReceipt;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
								resultSet.getByte("rented_days"),
								resultSet.getInt("price"),
								resultSet.getInt("total_price"),
								resultSet.getNString("room_name"),
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

	@Override
	public void update(RentalReceipt entity) throws DBConnectionException {

	}

	@Override
	public void delete(Integer id) throws DBConnectionException {

	}

}
