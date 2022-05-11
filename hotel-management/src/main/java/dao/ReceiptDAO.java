package dao;

import db.DBConnectionException;
import db.SingletonDBConnection;
import models.Product;
import models.Receipt;
import models.ReceiptDetail;
import utils.Pair;
import utils.UtilFunctions;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class ReceiptDAO implements DAO<Receipt, Integer> {

	@Override
	public ArrayList<Receipt> getAll() throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		ArrayList<Receipt> receiptList = new ArrayList<>();
		Statement statement = null;

		try {
			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("select * from `hotel_management`.`receipt`");

			while (resultSet.next()) {
				receiptList.add(
						new Receipt(
								resultSet.getInt("id"),
								resultSet.getNString("customer_name"),
								resultSet.getTimestamp("purchased_date"),
								resultSet.getNString("notes"),
								resultSet.getInt("total_price")
						)
				);
			}
		} catch (SQLException e) {
			System.out.println("ReceiptDAO.java - getAll - catch - " + e.getMessage());
			System.out.println("ReceiptDAO.java - getAll - catch - " + Arrays.toString(e.getStackTrace()));
			throw DBConnectionException.INSTANCE;
		} finally {
			try {
				if (statement != null)
					statement.close();
			} catch (SQLException e) {
				System.out.println("ReceiptDAO.java - getAll - finally/catch - " + e.getMessage());
				System.out.println("ReceiptDAO.java - getAll - finally/catch - " + Arrays.toString(e.getStackTrace()));
			}
		}

		return receiptList;
	}

	@Override
	public Optional<Receipt> get(Integer id) throws DBConnectionException {
		return Optional.empty();
	}

	@Override
	public void insert(Receipt entity) throws DBConnectionException {

	}

	public void insert(
			Receipt entity,
			ArrayList<ReceiptDetail> receiptDetailList,
			ArrayList<Integer> remainingQuantityList
	) throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		PreparedStatement preparedStatementInsertReceipt = null;
		PreparedStatement preparedStatementInsertReceiptDetail = null;
		PreparedStatement preparedStatementUpdateQuantity = null;

		try {
			// Declare sql statement and create PreparedStatement for it.
			String insertReceiptStatement = "insert into `hotel_management`.`receipt` " +
					"(`customer_name`, `purchased_date`, `notes`, `total_price`) values (?, ?, ?, ?)";
			preparedStatementInsertReceipt = connection.prepareStatement(insertReceiptStatement, Statement.RETURN_GENERATED_KEYS);

			// Set values for PreparedStatement.
			preparedStatementInsertReceipt.setNString(1, entity.getCustomerName());
			preparedStatementInsertReceipt.setTimestamp(2, entity.getPurchasedDate());
			preparedStatementInsertReceipt.setNString(3, entity.getNote());
			preparedStatementInsertReceipt.setInt(4, entity.getTotalPrice());

			// Execute queries.
			connection.setAutoCommit(false);
			preparedStatementInsertReceipt.executeUpdate();
			ResultSet generatedKeys = preparedStatementInsertReceipt.getGeneratedKeys();

			if (generatedKeys.next()) {
				int receiptId = generatedKeys.getInt(1);

				String insertReceiptDetailStatement = "insert into `hotel_management`.`receipt_detail`" +
						" (`receipt_id`, `quantity`, `product_name`, `product_type`, `price`) values (?, ?, ?, ?, ?)";
				String updateQuantityStatement = "update `hotel_management`.`product` set `stock` = ? where `id` = ?";

				preparedStatementInsertReceiptDetail = connection.prepareStatement(insertReceiptDetailStatement);
				preparedStatementUpdateQuantity = connection.prepareStatement(updateQuantityStatement);

				for (int i = 0; i < receiptDetailList.size(); i++) {
					preparedStatementInsertReceiptDetail.setInt(1, receiptId);
					preparedStatementInsertReceiptDetail.setInt(2, receiptDetailList.get(i).getQuantity());
					preparedStatementInsertReceiptDetail.setNString(3, receiptDetailList.get(i).getProductName());
					preparedStatementInsertReceiptDetail.setInt(4, receiptDetailList.get(i).getProductType());
					preparedStatementInsertReceiptDetail.setInt(5, receiptDetailList.get(i).getPrice());
					preparedStatementInsertReceiptDetail.addBatch();

					preparedStatementUpdateQuantity.setInt(1, remainingQuantityList.get(i));
					preparedStatementUpdateQuantity.setInt(2, receiptDetailList.get(i).getProductId());
					preparedStatementUpdateQuantity.addBatch();
				}

				preparedStatementInsertReceiptDetail.executeBatch();
				preparedStatementUpdateQuantity.executeBatch();
				connection.commit();
			} else {
				connection.rollback();
			}
		} catch (SQLException e) {
			System.out.println("ReceiptDAO.java - insert 3 params - catch - " + e.getMessage());
			System.out.println("ReceiptDAO.java - insert 3 params - catch - " + Arrays.toString(e.getStackTrace()));

			try {
				connection.rollback();
			} catch (SQLException ex) {
				System.out.println("ReceiptDAO.java - insert 3 params - catch/catch - " + ex.getMessage());
				System.out.println("ReceiptDAO.java - insert 3 params - catch/catch - " + Arrays.toString(ex.getStackTrace()));
			}

			throw DBConnectionException.INSTANCE;
		} finally {
			try {
				connection.setAutoCommit(true);

				if (preparedStatementInsertReceipt != null)
					preparedStatementInsertReceipt.close();
				if (preparedStatementInsertReceiptDetail != null)
					preparedStatementInsertReceiptDetail.close();
				if (preparedStatementUpdateQuantity != null)
					preparedStatementUpdateQuantity.close();
			} catch (SQLException e) {
				System.out.println("ReceiptDAO.java - insert 3 params - finally/catch - " + e.getMessage());
				System.out.println("ReceiptDAO.java - insert 3 params - finally/catch - " + Arrays.toString(e.getStackTrace()));
			}
		}
	}

	@Override
	public void update(Receipt entity) throws DBConnectionException {

	}

	@Override
	public void delete(Integer id) throws DBConnectionException {

	}

	public ArrayList<Pair<String, Number>> getTopFiveMostPurchasedProductsByMonthAndYear(
			int month, int year
	) throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		ArrayList<Pair<String, Number>> rowKeysAndStatsValues = new ArrayList<>();
		PreparedStatement preparedStatement = null;

		try {
			String sqlStatement = "select rd.product_name as 'product_name', rd.product_type as 'product_type', sum(rd.quantity * rd.price) as 'total_price_per_product'" +
					" from `hotel_management`.`receipt` r join `hotel_management`.`receipt_detail` rd" +
					" on r.id = rd.receipt_id where (month(r.purchased_date) = ? and year(r.purchased_date) = ?)" +
					" group by rd.product_name, rd.product_type order by `total_price_per_product` desc";

			preparedStatement = connection.prepareStatement(sqlStatement);
			preparedStatement.setInt(1, month);
			preparedStatement.setInt(2, year);

			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next() && rowKeysAndStatsValues.size() < 5) {
				Product.ProductTypeEnum productTypeEnum = Product.ProductTypeEnum.valueOf(resultSet.getInt("product_type"));
				String capitalizedProductType = UtilFunctions.capitalizeFirstLetterInString(productTypeEnum.name());

				rowKeysAndStatsValues.add(new Pair<>(
						resultSet.getString("product_name") + " - " + capitalizedProductType,
						resultSet.getInt("total_price_per_product")
				));
			}
		} catch (SQLException e) {
			System.out.println("ReceiptDAO.java - getTopFiveMostPurchasedProductsByMonthAndYear - catch - " + e.getMessage());
			System.out.println("ReceiptDAO.java - getTopFiveMostPurchasedProductsByMonthAndYear - catch - " + Arrays.toString(e.getStackTrace()));
			throw DBConnectionException.INSTANCE;
		} finally {

			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (SQLException e) {
				System.out.println("ReceiptDAO.java - getTopFiveMostPurchasedProductsByMonthAndYear - finally/catch - " + e.getMessage());
				System.out.println("ReceiptDAO.java - getTopFiveMostPurchasedProductsByMonthAndYear - finally/catch - " + Arrays.toString(e.getStackTrace()));
			}
		}

		return rowKeysAndStatsValues;
	}

}
