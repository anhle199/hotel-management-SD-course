package controllers.products;

import dao.ReceiptDAO;
import db.DBConnectionException;
import db.SingletonDBConnection;
import models.Receipt;
import utils.Constants;
import utils.DetailDialogModeEnum;
import utils.UtilFunctions;
import views.components.dialogs.ConnectionErrorDialog;
import views.components.table_model.NonEditableTableModel;
import views.dialogs.ProductReceiptDetailDialog;
import views.panels.products.ProductListPanel;
import views.panels.products.ProductReceiptListPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Vector;

public class ProductReceiptListController implements ActionListener {

	private final ProductReceiptListPanel productReceiptListPanel;
	private final JFrame mainFrame;
	private final ConnectionErrorDialog connectionErrorDialog;
	private final ReceiptDAO daoModel;

	public ProductReceiptListController(ProductReceiptListPanel productReceiptListPanel, JFrame mainFrame) {
		this.productReceiptListPanel = productReceiptListPanel;
		this.mainFrame = mainFrame;
		this.connectionErrorDialog = new ConnectionErrorDialog(mainFrame);
		this.daoModel = new ReceiptDAO();

		// Add action listeners
		this.productReceiptListPanel.getAddButton().addActionListener(this);
		this.connectionErrorDialog.getReconnectButton().addActionListener(this);

		// Add double-click listener for each row on the table
		productReceiptListPanel.getScrollableTable().getTable().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent mouseEvent) {
				if (mouseEvent.getClickCount() == 2) {
					doubleClicksInRowAction();
				}
			}
		});
	}

	public void loadProductReceiptListAndReloadTableData() {
		try {
			ArrayList<Receipt> productReceiptList = daoModel.getAll();
			addProductListToTable(productReceiptList);
		} catch (DBConnectionException e) {
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
			System.out.println("ProductReceiptListController.java - loadProductReceiptListAndReloadTableData - catch - Unavailable connection.");
		}
	}

	public void addProductListToTable(ArrayList<Receipt> productReceiptList) {
		NonEditableTableModel tableModel = (NonEditableTableModel) productReceiptListPanel.getScrollableTable().getTableModel();
		tableModel.removeAllRows();

		for (int i = 0; i < productReceiptList.size(); i++) {
			Object[] rowValue = mapReceiptInstanceToRowValue(i + 1, productReceiptList.get(i));
			tableModel.addRow(rowValue);
		}
	}

	private Object[] mapReceiptInstanceToRowValue(int no, Receipt receipt) {
		return new Object[]{
				no,
				receipt.getId(),
				receipt.getCustomerName(),
				UtilFunctions.formatTimestamp(Constants.TIMESTAMP_WITHOUT_NANOSECOND, receipt.getPurchasedDate()),
				receipt.getTotalPrice(),
				receipt.getNote(),
		};
	}

	private Receipt mapRowValueToReceiptInstance(Vector<Object> rowValue) {
		return new Receipt(
				(int) rowValue.get(ProductListPanel.HIDDE_COLUMN_PRODUCT_ID),
				String.valueOf(rowValue.get(2)),
				Timestamp.valueOf(String.valueOf(rowValue.get(3))),
				String.valueOf(rowValue.get(5)),
				(int) rowValue.get(4)
		);
	}

	public void displayUI() {
		loadProductReceiptListAndReloadTableData();
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == productReceiptListPanel.getAddButton()) {
			addButtonAction();
		} else if (event.getSource() == connectionErrorDialog.getReconnectButton()) {
			reconnectButtonAction();
		}
	}

	private void addButtonAction() {
		DetailDialogModeEnum viewMode = DetailDialogModeEnum.CREATE;
		ProductReceiptDetailDialog productReceiptDetailDialog = new ProductReceiptDetailDialog(mainFrame, viewMode);
		ProductReceiptDetailController productReceiptDetailController = new ProductReceiptDetailController(
				productReceiptDetailDialog, mainFrame, viewMode, this
		);

		productReceiptDetailController.displayUI();
	}

	private void reconnectButtonAction() {
		connectionErrorDialog.setExitOnCloseButton(false);
		connectionErrorDialog.setVisible(false);

		SingletonDBConnection.getInstance().connect();
		loadProductReceiptListAndReloadTableData();
	}

	private void doubleClicksInRowAction() {
		JTable table = productReceiptListPanel.getScrollableTable().getTable();
		NonEditableTableModel tableModel = (NonEditableTableModel) table.getModel();
		Vector<Object> selectedRowValue = tableModel.getRowValue(table.getSelectedRow());
		Receipt selectedReceipt = mapRowValueToReceiptInstance(selectedRowValue);

		DetailDialogModeEnum viewMode = DetailDialogModeEnum.VIEW_ONLY;
		ProductReceiptDetailDialog productReceiptDetailDialog = new ProductReceiptDetailDialog(mainFrame, viewMode);
		ProductReceiptDetailController productReceiptDetailController = new ProductReceiptDetailController(
				productReceiptDetailDialog, mainFrame, selectedReceipt, viewMode, this
		);

		productReceiptDetailController.displayUI();
	}

}
