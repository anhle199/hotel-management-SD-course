package dao;

import db.DBConnectionException;
import db.SingletonDBConnection;
import models.ImportInvoiceDetail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class ImportInvoiceDetailDAO implements DAO<ImportInvoiceDetail, Integer> {

	@Override
	public ArrayList<ImportInvoiceDetail> getAll() throws DBConnectionException {
		return null;
	}

	public ArrayList<ImportInvoiceDetail> getAllByInvoiceId(Integer invoiceId) throws DBConnectionException {
		Connection connection = SingletonDBConnection.getInstance().getConnection();

		if (connection == null)
			throw DBConnectionException.INSTANCE;

		ArrayList<ImportInvoiceDetail> receiptDetailList = new ArrayList<>();
		PreparedStatement preparedStatement = null;

		try {
			String sqlStatement = "select * from `hotel_management`.`import_invoice_detail` where `import_invoice_id` = ?";
			preparedStatement = connection.prepareStatement(sqlStatement);

			preparedStatement.setInt(1, invoiceId);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				receiptDetailList.add(
						new ImportInvoiceDetail(
								resultSet.getInt("id"),
								resultSet.getInt("import_invoice_id"),
								resultSet.getByte("quantity"),
								resultSet.getNString("product_name"),
								resultSet.getInt("product_type"),
								resultSet.getInt("price")
						)
				);
			}
		} catch (SQLException e) {
			System.out.println("ImportInvoiceDetailDAO.java - getAllByInvoiceId - catch - " + e.getMessage());
			System.out.println("ImportInvoiceDetailDAO.java - getAllByInvoiceId - catch - " + Arrays.toString(e.getStackTrace()));
			throw DBConnectionException.INSTANCE;
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (SQLException e) {
				System.out.println("ImportInvoiceDetailDAO.java - getAllByInvoiceId - finally/catch - " + e.getMessage());
				System.out.println("ImportInvoiceDetailDAO.java - getAllByInvoiceId - finally/catch - " + Arrays.toString(e.getStackTrace()));
			}
		}

		return receiptDetailList;
	}

	@Override
	public Optional<ImportInvoiceDetail> get(Integer id) throws DBConnectionException {
		return Optional.empty();
	}

	@Override
	public void insert(ImportInvoiceDetail entity) throws DBConnectionException {

	}

	@Override
	public void update(ImportInvoiceDetail entity) throws DBConnectionException {

	}

	@Override
	public void delete(Integer id) throws DBConnectionException {

	}

}
