package dao;

import db.DBConnectionException;
import db.SingletonDBConnection;
import models.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class ProductDAO implements DAO<Product, Integer> {

	@Override
	public ArrayList<Product> getAll() throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		ArrayList<Product> productList = new ArrayList<>();
		Statement statement = null;

		try {
			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("select * from `hotel_management`.`product`");

			while (resultSet.next()) {
				productList.add(
						new Product(
								resultSet.getInt("id"),
								resultSet.getNString("name"),
								resultSet.getInt("price"),
								resultSet.getInt("stock"),
								resultSet.getNString("description"),
								resultSet.getInt("product_type")
						)
				);
			}
		} catch (SQLException e) {
			System.out.println("ProductDAO.java - getAll - catch - " + e.getMessage());
			System.out.println("ProductDAO.java - getAll - catch - " + Arrays.toString(e.getStackTrace()));
			throw DBConnectionException.INSTANCE;
		} finally {
			try {
				if (statement != null)
					statement.close();
			} catch (SQLException e) {
				System.out.println("ProductDAO.java - getAll - finally/catch - " + e.getMessage());
				System.out.println("ProductDAO.java - getAll - finally/catch - " + Arrays.toString(e.getStackTrace()));
			}
		}

		return productList;
	}

	public ArrayList<Product> getAllAvailable() throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		ArrayList<Product> productList = new ArrayList<>();
		Statement statement = null;

		try {
			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("select * from `hotel_management`.`product` where `stock` > 0");

			while (resultSet.next()) {
				productList.add(
						new Product(
								resultSet.getInt("id"),
								resultSet.getNString("name"),
								resultSet.getInt("price"),
								resultSet.getInt("stock"),
								resultSet.getNString("description"),
								resultSet.getInt("product_type")
						)
				);
			}
		} catch (SQLException e) {
			System.out.println("ProductDAO.java - getAllAvailable - catch - " + e.getMessage());
			System.out.println("ProductDAO.java - getAllAvailable - catch - " + Arrays.toString(e.getStackTrace()));
			throw DBConnectionException.INSTANCE;
		} finally {
			try {
				if (statement != null)
					statement.close();
			} catch (SQLException e) {
				System.out.println("ProductDAO.java - getAllAvailable - finally/catch - " + e.getMessage());
				System.out.println("ProductDAO.java - getAllAvailable - finally/catch - " + Arrays.toString(e.getStackTrace()));
			}
		}

		return productList;
	}

	public ArrayList<Product> searchByProductName(String productNameToken) throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		ArrayList<Product> productList = new ArrayList<>();
		Statement statement = null;

		try {
			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
					"select * from `hotel_management`.`product` where `name` like '%" + productNameToken + "%'"
			);

			while (resultSet.next()) {
				productList.add(
						new Product(
								resultSet.getInt("id"),
								resultSet.getNString("name"),
								resultSet.getInt("price"),
								resultSet.getInt("stock"),
								resultSet.getNString("description"),
								resultSet.getInt("product_type")
						)
				);
			}
		} catch (SQLException e) {
			System.out.println("ProductDAO.java - searchByProductName - catch - " + e.getMessage());
			System.out.println("ProductDAO.java - searchByProductName - catch - " + Arrays.toString(e.getStackTrace()));
			throw DBConnectionException.INSTANCE;
		} finally {
			try {
				if (statement != null)
					statement.close();
			} catch (SQLException e) {
				System.out.println("ProductDAO.java - searchByProductName - finally/catch - " + e.getMessage());
				System.out.println("ProductDAO.java - searchByProductName - finally/catch - " + Arrays.toString(e.getStackTrace()));
			}
		}

		return productList;
	}

	@Override
	public Optional<Product> get(Integer id) throws DBConnectionException {
		return Optional.empty();
	}

	@Override
	public void insert(Product entity) throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		PreparedStatement preparedStatement = null;

		try {
			// Declare sql statement and create PreparedStatement for it.
			String sqlStatement = "insert into `hotel_management`.`product` " +
					"(`name`, `price`, `stock`, `description`, `product_type`) values (?, ?, ?, ?, ?)";
			preparedStatement = connection.prepareStatement(sqlStatement);

			// Set values for PreparedStatement.
			preparedStatement.setNString(1, entity.getName());
			preparedStatement.setInt(2, entity.getPrice());
			preparedStatement.setInt(3, entity.getStock());
			preparedStatement.setNString(4, entity.getDescription());
			preparedStatement.setInt(5, entity.getProductType());

			// Execute queries.
			connection.setAutoCommit(false);
			preparedStatement.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			System.out.println("ProductDAO.java - insert - catch - " + e.getMessage());
			System.out.println("ProductDAO.java - insert - catch - " + Arrays.toString(e.getStackTrace()));

			try {
				connection.rollback();
			} catch (SQLException ex) {
				System.out.println("ProductDAO.java - insert - catch/catch - " + ex.getMessage());
				System.out.println("ProductDAO.java - insert - catch/catch - " + Arrays.toString(ex.getStackTrace()));
			}

			throw DBConnectionException.INSTANCE;
		} finally {
			try {
				connection.setAutoCommit(true);

				if (preparedStatement != null)
					preparedStatement.close();
			} catch (SQLException e) {
				System.out.println("ProductDAO.java - insert - finally/catch - " + e.getMessage());
				System.out.println("ProductDAO.java - insert - finally/catch - " + Arrays.toString(e.getStackTrace()));
			}
		}
	}

	@Override
	public void update(Product entity) throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		PreparedStatement preparedStatementUpdateProduct = null;
		PreparedStatement preparedStatementUpdateReceiptDetail = null;
		PreparedStatement preparedStatementUpdateImportInvoiceDetail = null;

		try {
			// Declare sql statements and create PreparedStatement for it.
			String updateProductStatement = "update `hotel_management`.`product` " +
					"set `name` = ?, `price` = ?, `stock` = ?, `description` = ?, `product_type` = ? where `id` = ?";
			String updateReceiptDetailStatement = "update `hotel_management`.`receipt_detail` " +
					"set `product_name` = ?, `product_type` = ? where `product_id` = ?";
			String updateImportInvoiceDetailStatement = "update `hotel_management`.`import_invoice_detail` " +
					"set `product_name` = ?, `product_type` = ? where `product_id` = ?";

			preparedStatementUpdateProduct = connection.prepareStatement(updateProductStatement);
			preparedStatementUpdateReceiptDetail = connection.prepareStatement(updateReceiptDetailStatement);
			preparedStatementUpdateImportInvoiceDetail = connection.prepareStatement(updateImportInvoiceDetailStatement);

			// Set values for PreparedStatement.
			preparedStatementUpdateProduct.setNString(1, entity.getName());
			preparedStatementUpdateProduct.setInt(2, entity.getPrice());
			preparedStatementUpdateProduct.setInt(3, entity.getStock());
			preparedStatementUpdateProduct.setNString(4, entity.getDescription());
			preparedStatementUpdateProduct.setInt(5, entity.getProductType());
			preparedStatementUpdateProduct.setInt(6, entity.getId());
			preparedStatementUpdateReceiptDetail.setNString(1, entity.getName());
			preparedStatementUpdateReceiptDetail.setInt(2, entity.getProductType());
			preparedStatementUpdateReceiptDetail.setInt(3, entity.getId());
			preparedStatementUpdateImportInvoiceDetail.setNString(1, entity.getName());
			preparedStatementUpdateImportInvoiceDetail.setInt(2, entity.getProductType());
			preparedStatementUpdateImportInvoiceDetail.setInt(3, entity.getId());

			// Execute queries.
			connection.setAutoCommit(false);
			preparedStatementUpdateProduct.executeUpdate();
			preparedStatementUpdateReceiptDetail.executeUpdate();
			preparedStatementUpdateImportInvoiceDetail.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			System.out.println("ProductDAO.java - update - catch - " + e.getMessage());
			System.out.println("ProductDAO.java - update - catch - " + Arrays.toString(e.getStackTrace()));

			try {
				connection.rollback();
			} catch (SQLException ex) {
				System.out.println("ProductDAO.java - update - catch/catch - " + ex.getMessage());
				System.out.println("ProductDAO.java - update - catch/catch - " + Arrays.toString(ex.getStackTrace()));
			}

			throw DBConnectionException.INSTANCE;
		} finally {
			try {
				connection.setAutoCommit(true);

				if (preparedStatementUpdateProduct != null)
					preparedStatementUpdateProduct.close();
				if (preparedStatementUpdateReceiptDetail != null)
					preparedStatementUpdateReceiptDetail.close();
				if (preparedStatementUpdateImportInvoiceDetail != null)
					preparedStatementUpdateImportInvoiceDetail.close();
			} catch (SQLException e) {
				System.out.println("ProductDAO.java - update - finally/catch - " + e.getMessage());
				System.out.println("ProductDAO.java - update - finally/catch - " + Arrays.toString(e.getStackTrace()));
			}
		}
	}

	@Override
	public void delete(Integer id) throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		Statement statementDeleteProduct = null;
		PreparedStatement preparedStatementUpdateReceiptDetail = null;
		PreparedStatement preparedStatementUpdateImportInvoiceDetail = null;

		try {
			String deleteProductStatement = "delete from `hotel_management`.`product` where `id` = " + id;
			String updateReceiptDetailStatement = "update `hotel_management`.`receipt_detail` " +
					"set `product_id` = ? where `product_id` = ?";
			String updateImportInvoiceDetailStatement = "update `hotel_management`.`import_invoice_detail` " +
					"set `product_id` = ? where `product_id` = ?";

			statementDeleteProduct = connection.createStatement();
			preparedStatementUpdateReceiptDetail = connection.prepareStatement(updateReceiptDetailStatement);
			preparedStatementUpdateImportInvoiceDetail = connection.prepareStatement(updateImportInvoiceDetailStatement);

			preparedStatementUpdateReceiptDetail.setNull(1, Types.INTEGER);
			preparedStatementUpdateReceiptDetail.setInt(2, id);
			preparedStatementUpdateImportInvoiceDetail.setNull(1, Types.INTEGER);
			preparedStatementUpdateImportInvoiceDetail.setInt(2, id);

			connection.setAutoCommit(false);
			statementDeleteProduct.executeUpdate(deleteProductStatement);
			preparedStatementUpdateReceiptDetail.executeUpdate();
			preparedStatementUpdateImportInvoiceDetail.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			System.out.println("ProductDAO.java - delete - catch - " + e.getMessage());
			System.out.println("ProductDAO.java - delete - catch - " + Arrays.toString(e.getStackTrace()));

			try {
				connection.rollback();
			} catch (SQLException ex) {
				System.out.println("ProductDAO.java - delete - catch/catch - " + ex.getMessage());
				System.out.println("ProductDAO.java - delete - catch/catch - " + Arrays.toString(ex.getStackTrace()));
			}

			throw DBConnectionException.INSTANCE;
		} finally {
			try {
				connection.setAutoCommit(true);

				if (statementDeleteProduct != null)
					statementDeleteProduct.close();
				if (preparedStatementUpdateReceiptDetail != null)
					preparedStatementUpdateReceiptDetail.close();
				if (preparedStatementUpdateImportInvoiceDetail != null)
					preparedStatementUpdateImportInvoiceDetail.close();
			} catch (SQLException e) {
				System.out.println("ProductDAO.java - delete - finally/catch - " + e.getMessage());
				System.out.println("ProductDAO.java - delete - finally/catch - " + Arrays.toString(e.getStackTrace()));
			}
		}
	}

	public boolean isExistingProductName(String productName) throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();
		if (connection == null)
			throw DBConnectionException.INSTANCE;

		boolean isExisting = false;
		PreparedStatement preparedStatement = null;

		try {
			ResultSet resultSet;
			String sqlStatement = "select * from `hotel_management`.`product` where `name` = ?";

			preparedStatement = connection.prepareStatement(sqlStatement);
			preparedStatement.setNString(1, productName);

			resultSet = preparedStatement.executeQuery();
			isExisting = resultSet.next();  // next() return true if this value is existing, otherwise return false.
		} catch (SQLException e) {
			System.out.println("ProductDAO.java - isExistingProductName - catch - " + e.getMessage());
			System.out.println("ProductDAO.java - isExistingProductName - catch - " + Arrays.toString(e.getStackTrace()));
			throw DBConnectionException.INSTANCE;
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (SQLException e) {
				System.out.println("ProductDAO.java - isExistingProductName - finally/catch - " + e.getMessage());
				System.out.println("ProductDAO.java - isExistingProductName - finally/catch - " + Arrays.toString(e.getStackTrace()));
			}
		}

		return isExisting;
	}

}
