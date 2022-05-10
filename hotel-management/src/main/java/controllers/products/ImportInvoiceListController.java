package controllers.products;

import dao.ImportInvoiceDAO;
import db.DBConnectionException;
import db.SingletonDBConnection;
import models.ImportInvoice;
import utils.Constants;
import utils.DetailDialogModeEnum;
import utils.UtilFunctions;
import views.components.dialogs.ConnectionErrorDialog;
import views.components.table_model.NonEditableTableModel;
import views.dialogs.ImportInvoiceDetailDialog;
import views.panels.products.ImportInvoiceListPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Vector;

public class ImportInvoiceListController implements ActionListener {

	private final ImportInvoiceListPanel importInvoiceListPanel;
	private final JFrame mainFrame;
	private final ConnectionErrorDialog connectionErrorDialog;
	private final ImportInvoiceDAO daoModel;

	public ImportInvoiceListController(ImportInvoiceListPanel importInvoiceListPanel, JFrame mainFrame) {
		this.importInvoiceListPanel = importInvoiceListPanel;
		this.mainFrame = mainFrame;
		this.connectionErrorDialog = new ConnectionErrorDialog(mainFrame);
		this.daoModel = new ImportInvoiceDAO();

		// Add action listeners
		this.importInvoiceListPanel.getAddButton().addActionListener(this);
		this.connectionErrorDialog.getReconnectButton().addActionListener(this);

		// Add double-click listener for each row on the table
		importInvoiceListPanel.getScrollableTable().getTable().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent mouseEvent) {
				if (mouseEvent.getClickCount() == 2) {
					doubleClicksInRowAction();
				}
			}
		});
	}

	public void loadImportInvoiceListAndReloadTableData() {
		try {
			ArrayList<ImportInvoice> productReceiptList = daoModel.getAll();
			addImportInvoiceListToTable(productReceiptList);
		} catch (DBConnectionException e) {
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
			System.out.println("ImportInvoiceListController.java - loadImportInvoiceListAndReloadTableData - catch - Unavailable connection.");
		}
	}

	public void addImportInvoiceListToTable(ArrayList<ImportInvoice> importInvoiceList) {
		NonEditableTableModel tableModel = (NonEditableTableModel) importInvoiceListPanel.getScrollableTable().getTableModel();
		tableModel.removeAllRows();

		for (int i = 0; i < importInvoiceList.size(); i++) {
			Object[] rowValue = mapImportInvoiceInstanceToRowValue(i + 1, importInvoiceList.get(i));
			tableModel.addRow(rowValue);
		}
	}

	private Object[] mapImportInvoiceInstanceToRowValue(int no, ImportInvoice importInvoice) {
		return new Object[]{
				no,
				importInvoice.getId(),
				UtilFunctions.formatTimestamp(Constants.TIMESTAMP_PATTERN, importInvoice.getImportedDate()),
				importInvoice.getTotalPrice(),
				importInvoice.getNote(),
		};
	}

	private ImportInvoice mapRowValueToImportInvoiceInstance(Vector<Object> rowValue) {
		return new ImportInvoice(
				(int) rowValue.get(ImportInvoiceListPanel.HIDDEN_COLUMN_INVOICE_ID),
				Timestamp.valueOf(String.valueOf(rowValue.get(2))),
				String.valueOf(rowValue.get(4)),
				(int) rowValue.get(3)
		);
	}

	public void displayUI() {
		loadImportInvoiceListAndReloadTableData();
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == importInvoiceListPanel.getAddButton()) {
			addButtonAction();
		} else if (event.getSource() == connectionErrorDialog.getReconnectButton()) {
			reconnectButtonAction();
		}
	}

	private void addButtonAction() {
		DetailDialogModeEnum viewMode = DetailDialogModeEnum.CREATE;
		ImportInvoiceDetailDialog importInvoiceDetailDialog = new ImportInvoiceDetailDialog(mainFrame, viewMode);
		ImportInvoiceDetailController productReceiptDetailController = new ImportInvoiceDetailController(
				importInvoiceDetailDialog, mainFrame, viewMode, this
		);

		productReceiptDetailController.displayUI();
	}

	private void reconnectButtonAction() {
		connectionErrorDialog.setExitOnCloseButton(false);
		connectionErrorDialog.setVisible(false);

		SingletonDBConnection.getInstance().connect();
		loadImportInvoiceListAndReloadTableData();
	}

	private void doubleClicksInRowAction() {
		JTable table = importInvoiceListPanel.getScrollableTable().getTable();
		int selectedRow = table.getSelectedRow();

		if (selectedRow >= 0 && selectedRow < table.getRowCount()) {
			NonEditableTableModel tableModel = (NonEditableTableModel) table.getModel();
			Vector<Object> selectedRowValue = tableModel.getRowValue(table.getSelectedRow());
			ImportInvoice selectedInvoice = mapRowValueToImportInvoiceInstance(selectedRowValue);

			DetailDialogModeEnum viewMode = DetailDialogModeEnum.VIEW_ONLY;
			ImportInvoiceDetailDialog importInvoiceDetailDialog = new ImportInvoiceDetailDialog(mainFrame, viewMode);
			ImportInvoiceDetailController productReceiptDetailController = new ImportInvoiceDetailController(
					importInvoiceDetailDialog, mainFrame, selectedInvoice, viewMode, this
			);

			productReceiptDetailController.displayUI();
		}
	}

}
