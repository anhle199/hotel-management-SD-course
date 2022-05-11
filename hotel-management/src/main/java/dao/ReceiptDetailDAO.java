package dao;

import db.DBConnectionException;
import db.SingletonDBConnection;
import models.ReceiptDetail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class ReceiptDetailDAO implements DAO<ReceiptDetail, Integer> {

	@Override
	public ArrayList<ReceiptDetail> getAll() throws DBConnectionException {
		return null;
	}

	public ArrayList<ReceiptDetail> getAllByReceiptId(Integer receiptId) throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		ArrayList<ReceiptDetail> receiptDetailList = new ArrayList<>();
		PreparedStatement preparedStatement = null;

		try {
			String sqlStatement = "select * from `hotel_management`.`receipt_detail` where `receipt_id` = ?";
			preparedStatement = connection.prepareStatement(sqlStatement);

			preparedStatement.setInt(1, receiptId);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				receiptDetailList.add(
						new ReceiptDetail(
								resultSet.getInt("id"),
								resultSet.getInt("receipt_id"),
								resultSet.getInt("quantity"),
								resultSet.getInt("product_id"),
								resultSet.getNString("product_name"),
								resultSet.getInt("product_type"),
								resultSet.getInt("price")
						)
				);
			}
		} catch (SQLException e) {
			System.out.println("ReceiptDetailDAO.java - getAllByReceiptId - catch - " + e.getMessage());
			System.out.println("ReceiptDetailDAO.java - getAllByReceiptId - catch - " + Arrays.toString(e.getStackTrace()));
			throw DBConnectionException.INSTANCE;
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (SQLException e) {
				System.out.println("ReceiptDetailDAO.java - getAllByReceiptId - finally/catch - " + e.getMessage());
				System.out.println("ReceiptDetailDAO.java - getAllByReceiptId - finally/catch - " + Arrays.toString(e.getStackTrace()));
			}
		}

		return receiptDetailList;
	}

	@Override
	public Optional<ReceiptDetail> get(Integer id) throws DBConnectionException {
		return Optional.empty();
	}

	@Override
	public void insert(ReceiptDetail entity) throws DBConnectionException {

	}

	@Override
	public void update(ReceiptDetail entity) throws DBConnectionException {

	}

	@Override
	public void delete(Integer id) throws DBConnectionException {

	}

}
