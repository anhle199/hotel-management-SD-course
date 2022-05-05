package dao;

import db.DBConnectionException;
import db.SingletonDBConnection;
import models.ImportInvoice;
import models.ImportInvoiceDetail;
import utils.Pair;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class ImportInvoiceDAO implements DAO<ImportInvoice, Integer> {

	@Override
	public ArrayList<ImportInvoice> getAll() throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		ArrayList<ImportInvoice> importInvoiceList = new ArrayList<>();
		Statement statement = null;

		try {
			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("select * from `hotel_management`.`import_invoice`");

			while (resultSet.next()) {
				importInvoiceList.add(
						new ImportInvoice(
								resultSet.getInt("id"),
								resultSet.getTimestamp("imported_date"),
								resultSet.getNString("notes"),
								resultSet.getInt("total_price")
						)
				);
			}
		} catch (SQLException e) {
			System.out.println("ImportInvoiceDAO.java - getAll - catch - " + e.getMessage());
			System.out.println("ImportInvoiceDAO.java - getAll - catch - " + Arrays.toString(e.getStackTrace()));
			throw DBConnectionException.INSTANCE;
		} finally {
			try {
				if (statement != null)
					statement.close();
			} catch (SQLException e) {
				System.out.println("ImportInvoiceDAO.java - getAll - finally/catch - " + e.getMessage());
				System.out.println("ImportInvoiceDAO.java - getAll - finally/catch - " + Arrays.toString(e.getStackTrace()));
			}
		}

		return importInvoiceList;
	}

	@Override
	public Optional<ImportInvoice> get(Integer id) throws DBConnectionException {
		return Optional.empty();
	}

	@Override
	public void insert(ImportInvoice entity) throws DBConnectionException {

	}

	public void insert(
			ImportInvoice entity,
			ArrayList<ImportInvoiceDetail> importInvoiceDetailList
	) throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		PreparedStatement preparedStatementInsertImportInvoice = null;
		PreparedStatement preparedStatementInsertImportInvoiceDetail = null;

		try {
			// Declare sql statement and create PreparedStatement for it.
			String insertImportInvoiceStatement = "insert into `hotel_management`.`import_invoice` " +
					"(`imported_date`, `notes`, `total_price`) values (?, ?, ?)";
			preparedStatementInsertImportInvoice = connection.prepareStatement(insertImportInvoiceStatement, Statement.RETURN_GENERATED_KEYS);

			// Set values for PreparedStatement.
			preparedStatementInsertImportInvoice.setTimestamp(1, entity.getImportedDate());
			preparedStatementInsertImportInvoice.setNString(2, entity.getNote());
			preparedStatementInsertImportInvoice.setInt(3, entity.getTotalPrice());

			// Execute queries.
			connection.setAutoCommit(false);
			preparedStatementInsertImportInvoice.executeUpdate();
			ResultSet generatedKeys = preparedStatementInsertImportInvoice.getGeneratedKeys();

			if (generatedKeys.next()) {
				int importInvoiceId = generatedKeys.getInt(1);

				String insertReceiptDetailStatement = "insert into `hotel_management`.`import_invoice_detail` " +
						"(`import_invoice_id`, `quantity`, `product_name`, `product_type`, `price`) values (?, ?, ?, ?, ?)";

				preparedStatementInsertImportInvoiceDetail = connection.prepareStatement(insertReceiptDetailStatement);

				for (ImportInvoiceDetail item: importInvoiceDetailList) {
					preparedStatementInsertImportInvoiceDetail.setInt(1, importInvoiceId);
					preparedStatementInsertImportInvoiceDetail.setByte(2, item.getQuantity());
					preparedStatementInsertImportInvoiceDetail.setNString(3, item.getProductName());
					preparedStatementInsertImportInvoiceDetail.setInt(4, item.getProductType());
					preparedStatementInsertImportInvoiceDetail.setInt(5, item.getPrice());
					preparedStatementInsertImportInvoiceDetail.addBatch();
				}

				preparedStatementInsertImportInvoiceDetail.executeBatch();
				connection.commit();
			} else {
				connection.rollback();
			}
		} catch (SQLException e) {
			System.out.println("ImportInvoiceDAO.java - insert 2 params - catch - " + e.getMessage());
			System.out.println("ImportInvoiceDAO.java - insert 2 params - catch - " + Arrays.toString(e.getStackTrace()));

			try {
				connection.rollback();
			} catch (SQLException ex) {
				System.out.println("ImportInvoiceDAO.java - insert 2 params - catch/catch - " + ex.getMessage());
				System.out.println("ImportInvoiceDAO.java - insert 2 params - catch/catch - " + Arrays.toString(ex.getStackTrace()));
			}

			throw DBConnectionException.INSTANCE;
		} finally {
			try {
				connection.setAutoCommit(true);

				if (preparedStatementInsertImportInvoice != null)
					preparedStatementInsertImportInvoice.close();
				if (preparedStatementInsertImportInvoiceDetail != null)
					preparedStatementInsertImportInvoiceDetail.close();
			} catch (SQLException e) {
				System.out.println("ImportInvoiceDAO.java - insert 2 params - finally/catch - " + e.getMessage());
				System.out.println("ImportInvoiceDAO.java - insert 2 params - finally/catch - " + Arrays.toString(e.getStackTrace()));
			}
		}
	}

	@Override
	public void update(ImportInvoice entity) throws DBConnectionException {

	}

	@Override
	public void delete(Integer id) throws DBConnectionException {

	}

	public ArrayList<Pair<String, Number>> getTopFiveMostImportedProductsByMonthAndYear(
			int month, int year
	) throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		ArrayList<Pair<String, Number>> rowKeysAndStatsValues = new ArrayList<>();
		PreparedStatement preparedStatement = null;

		try {
			String sqlStatement = "select iid.product_name as 'product_name', sum(iid.quantity) as 'quantity'" +
					" from `hotel_management`.`import_invoice` ii join `hotel_management`.`import_invoice_detail` iid" +
					" where (month(ii.imported_date) = ? and year(ii.imported_date) = ?)" +
					" group by iid.product_name";

			preparedStatement = connection.prepareStatement(sqlStatement);
			preparedStatement.setInt(1, month);
			preparedStatement.setInt(2, year);

			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next() && rowKeysAndStatsValues.size() < 5) {
				rowKeysAndStatsValues.add(new Pair<>(
						resultSet.getString("product_name"),
						resultSet.getInt("quantity")
				));
			}
		} catch (SQLException e) {
			System.out.println("ImportInvoiceDAO.java - getTopFiveMostImportedProductsByMonthAndYear - catch - " + e.getMessage());
			System.out.println("ImportInvoiceDAO.java - getTopFiveMostImportedProductsByMonthAndYear - catch - " + Arrays.toString(e.getStackTrace()));
			throw DBConnectionException.INSTANCE;
		} finally {

			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (SQLException e) {
				System.out.println("ImportInvoiceDAO.java - getTopFiveMostImportedProductsByMonthAndYear - finally/catch - " + e.getMessage());
				System.out.println("ImportInvoiceDAO.java - getTopFiveMostImportedProductsByMonthAndYear - finally/catch - " + Arrays.toString(e.getStackTrace()));
			}
		}

		return rowKeysAndStatsValues;
	}

}