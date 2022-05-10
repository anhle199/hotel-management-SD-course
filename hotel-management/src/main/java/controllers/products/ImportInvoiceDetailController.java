package controllers.products;

import dao.ImportInvoiceDAO;
import dao.ImportInvoiceDetailDAO;
import dao.ProductDAO;
import db.DBConnectionException;
import db.SingletonDBConnection;
import models.ImportInvoice;
import models.ImportInvoiceDetail;
import models.Product;
import utils.Constants;
import utils.DetailDialogModeEnum;
import utils.UtilFunctions;
import views.components.dialogs.ConnectionErrorDialog;
import views.components.panels.ScrollableTablePanel;
import views.components.table_model.NonEditableTableModel;
import views.dialogs.AddProductToImportInvoiceDialog;
import views.dialogs.ImportInvoiceDetailDialog;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Vector;

public class ImportInvoiceDetailController implements ActionListener, DocumentListener {

	private final ImportInvoiceDetailDialog importInvoiceDetailDialog;
	private AddProductToImportInvoiceDialog addProductToImportInvoiceDialog;
	private final ConnectionErrorDialog connectionErrorDialog;

	private final ImportInvoice importInvoice;
	private ArrayList<Product> productList;
	private ArrayList<Product> selectedProductList;
	private final DetailDialogModeEnum viewMode;

	private final ImportInvoiceListController importInvoiceListController;

	public ImportInvoiceDetailController(
			ImportInvoiceDetailDialog dialog,
			JFrame mainFrame,
			DetailDialogModeEnum viewMode,
			ImportInvoiceListController importInvoiceListController
	) {
		this(dialog, mainFrame, null, viewMode, importInvoiceListController);
	}

	public ImportInvoiceDetailController(
			ImportInvoiceDetailDialog dialog,
			JFrame mainFrame,
			ImportInvoice importInvoice,
			DetailDialogModeEnum viewMode,
			ImportInvoiceListController importInvoiceListController
	) {
		this.importInvoiceDetailDialog = dialog;
		this.connectionErrorDialog = new ConnectionErrorDialog(mainFrame);
		this.importInvoice = importInvoice;
		this.viewMode = viewMode;
		this.importInvoiceListController = importInvoiceListController;

		if (viewMode == DetailDialogModeEnum.CREATE) {
			this.addProductToImportInvoiceDialog = new AddProductToImportInvoiceDialog(mainFrame);

			// Add action listeners
			this.importInvoiceDetailDialog.getAddButton().addActionListener(this);
			this.importInvoiceDetailDialog.getRemoveButton().addActionListener(this);
			this.importInvoiceDetailDialog.getCreateButton().addActionListener(this);
			this.importInvoiceDetailDialog.getCancelButton().addActionListener(this);
			this.addProductToImportInvoiceDialog.getAddButton().addActionListener(this);
			this.addProductToImportInvoiceDialog.getCancelButton().addActionListener(this);

			// Add document listener
			this.addProductToImportInvoiceDialog.getQuantityTextField().getDocument().addDocumentListener(this);
			this.addProductToImportInvoiceDialog.getPriceTextField().getDocument().addDocumentListener(this);
		}
		this.connectionErrorDialog.getReconnectButton().addActionListener(this);
	}

	public void displayUI() {
		if (importInvoice != null) {
			importInvoiceDetailDialog.getImportedDateTextField()
									 .setText(
											 UtilFunctions.formatTimestamp(
													 Constants.TIMESTAMP_PATTERN,
													 importInvoice.getImportedDate()
											 )
									 );
			importInvoiceDetailDialog.getTotalPriceTextField()
									 .setText(String.valueOf(importInvoice.getTotalPrice()));
			importInvoiceDetailDialog.getNoteTextField()
									 .setText(importInvoice.getNote());

			loadImportInvoiceDetailListAndAddToTable();
		} else {
			Timestamp nowAfterOneHour = UtilFunctions.getTimestamp(LocalDateTime.now(), 1);
			importInvoiceDetailDialog.getImportedDatePanel().setSelectedDate(nowAfterOneHour);

			loadProductList();
		}

		importInvoiceDetailDialog.setVisible(true);
	}

	private void loadImportInvoiceDetailListAndAddToTable() {
		try {
			ImportInvoiceDetailDAO daoModel = new ImportInvoiceDetailDAO();
			ArrayList<ImportInvoiceDetail> importInvoiceDetailList = daoModel.getAllByInvoiceId(importInvoice.getId());
			addImportInvoiceDetailLstToTable(importInvoiceDetailList);
		} catch (DBConnectionException e) {
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
			System.out.println("ImportInvoiceDetailController.java - loadImportInvoiceDetailListAndAddToTable - catch - Unavailable connection.");
		}
	}

	private void loadProductList() {
		try {
			ProductDAO daoModel = new ProductDAO();
			productList = daoModel.getAll();
			selectedProductList = new ArrayList<>();
		} catch (DBConnectionException e) {
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
			System.out.println("ImportInvoiceDetailController.java - loadProductList - catch - Unavailable connection.");
		}
	}

	public void addImportInvoiceDetailLstToTable(ArrayList<ImportInvoiceDetail> importInvoiceDetailList) {
		NonEditableTableModel tableModel = (NonEditableTableModel) importInvoiceDetailDialog.getScrollableTable()
																							.getTableModel();
		tableModel.removeAllRows();

		for (int i = 0; i < importInvoiceDetailList.size(); i++) {
			Object[] rowValue = mapImportInvoiceDetailInstanceToRowValue(
					i + 1,
					importInvoiceDetailList.get(i),
					-1
			);

			tableModel.addRow(rowValue);
		}
	}

	private Object[] mapImportInvoiceDetailInstanceToRowValue(
			int no,
			ImportInvoiceDetail importInvoiceDetail,
			int productId
	) {
		String capitalizedProductType = UtilFunctions.capitalizeFirstLetterInString(
				Product.ProductTypeEnum.valueOf(importInvoiceDetail.getProductType()).name()
		);

		return new Object[]{
				no,
				productId,
				importInvoiceDetail.getProductName(),
				capitalizedProductType,
				importInvoiceDetail.getPrice(),
				importInvoiceDetail.getQuantity(),
		};
	}

	private ImportInvoiceDetail mapRowValueToImportInvoiceDetailInstance(Vector<Object> rowValue) {
		Product.ProductTypeEnum productType = Product.ProductTypeEnum.valueOfIgnoreCase(
				String.valueOf(rowValue.get(3))
		);

		return new ImportInvoiceDetail(
				(int) rowValue.get(ImportInvoiceDetailDialog.HIDDEN_COLUMN_PRODUCT_ID),
				-1,
				(byte) rowValue.get(5),
				String.valueOf(rowValue.get(2)),
				productType.ordinal(),
				(int) rowValue.get(4)
		);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == connectionErrorDialog.getReconnectButton()) {
			reconnectButtonAction();
		}

		if (viewMode == DetailDialogModeEnum.CREATE) {
			if (event.getSource() == importInvoiceDetailDialog.getAddButton()) {
				addButtonAction();
			} else if (event.getSource() == importInvoiceDetailDialog.getRemoveButton()) {
				removeButtonAction();
			} else if (event.getSource() == importInvoiceDetailDialog.getCreateButton()) {
				createButtonAction();
			} else if (event.getSource() == importInvoiceDetailDialog.getCancelButton()) {
				importInvoiceDetailDialog.setVisible(false);
			} else if (event.getSource() == addProductToImportInvoiceDialog.getAddButton()) {
				addButtonActionOfAddProductToImportInvoiceDialog();
			} else if (event.getSource() == addProductToImportInvoiceDialog.getCancelButton()) {
				addProductToImportInvoiceDialog.setVisible(false);
			}
		}
	}

	private void addButtonAction() {
		Vector<String> productNameList = new Vector<>();
		for (Product item : productList)
			productNameList.add(item.getName());

		addProductToImportInvoiceDialog.getProductNameComboBox()
									   .setModel(new DefaultComboBoxModel<>(productNameList));
		addProductToImportInvoiceDialog.getQuantityTextField()
									   .setValue(Constants.MIN_QUANTITY);
		addProductToImportInvoiceDialog.getPriceTextField()
									   .setValue(Constants.MIN_PRICE);
		addProductToImportInvoiceDialog.getTotalPriceTextField()
									   .setText(String.valueOf(Constants.MIN_PRICE * Constants.MIN_QUANTITY));

		addProductToImportInvoiceDialog.setVisible(true);
	}

	private void removeButtonAction() {
		ScrollableTablePanel tablePanel = importInvoiceDetailDialog.getScrollableTable();
		int selectedRowIndex = tablePanel.getTable().getSelectedRow();

		if (selectedRowIndex == -1) {
			UtilFunctions.showWarningMessage(
					importInvoiceDetailDialog,
					"Remove Product",
					"You must select a row."
			);
		} else {
			int option = JOptionPane.showConfirmDialog(
					importInvoiceDetailDialog,
					"Are you sure to remove this product?",
					"Remove Product",
					JOptionPane.YES_NO_OPTION
			);

			if (option == JOptionPane.YES_OPTION) {
				NonEditableTableModel tableModel = (NonEditableTableModel) tablePanel.getTableModel();

				int removedProductId = (int) tableModel.getValueAt(
						selectedRowIndex,
						ImportInvoiceDetailDialog.HIDDEN_COLUMN_PRODUCT_ID
				);
				int removedProductIndex = findIndexByProductId(selectedProductList, removedProductId);
				productList.add(selectedProductList.get(removedProductIndex));
				selectedProductList.remove(removedProductIndex);

				tableModel.removeRow(selectedRowIndex);
			}
		}
	}

	private void createButtonAction() {
		try {
			ImportInvoiceDAO daoModel = new ImportInvoiceDAO();
			ImportInvoice importInvoice = getImportInvoiceInstanceFromInputFields();
			ArrayList<ImportInvoiceDetail> importInvoiceDetailList = getImportInvoiceDetailListFromInputFields();

			importInvoice.setTotalPrice(calculateTotalPrice(importInvoiceDetailList));

			if (importInvoiceDetailList.isEmpty()) {
				UtilFunctions.showErrorMessage(
						importInvoiceDetailDialog,
						"Create Import Invoice",
						"The invoice must has at least one product."
				);
			} else {
				int option = UtilFunctions.showConfirmDialog(
						importInvoiceDetailDialog,
						"Create Import Invoice",
						"Are you sure to create this invoice?"
				);

				if (option == JOptionPane.YES_OPTION) {
					daoModel.insert(importInvoice, importInvoiceDetailList);
					UtilFunctions.showInfoMessage(
							importInvoiceDetailDialog,
							"Create Import Invoice",
							"Create successfully."
					);

					importInvoiceDetailDialog.setVisible(false);
					importInvoiceListController.loadImportInvoiceListAndReloadTableData();
				}
			}
		} catch (DBConnectionException e) {
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
			System.out.println("ImportInvoiceDetailController.java - createButtonAction - catch - Unavailable connection.");
		}
	}

	private void addButtonActionOfAddProductToImportInvoiceDialog() {
		int selectedProductIndex = addProductToImportInvoiceDialog.getProductNameComboBox().getSelectedIndex();
		Product selectedProduct = productList.get(selectedProductIndex);
		byte quantity = Byte.parseByte(
				String.valueOf(addProductToImportInvoiceDialog.getQuantityTextField().getValue())
		);

		ImportInvoiceDetail addedImportInvoiceDetail = new ImportInvoiceDetail(
				-1,
				importInvoice.getId(),
				quantity,
				selectedProduct.getName(),
				selectedProduct.getProductType(),
				selectedProduct.getPrice()
		);

		selectedProductList.add(selectedProduct);
		productList.remove(selectedProductIndex);

		NonEditableTableModel tableModel = (NonEditableTableModel) importInvoiceDetailDialog.getScrollableTable()
																							.getTableModel();
		int no = tableModel.getRowCount() + 1;
		tableModel.addRow(mapImportInvoiceDetailInstanceToRowValue(no, addedImportInvoiceDetail, selectedProduct.getId()));

		addProductToImportInvoiceDialog.setVisible(false);
	}

	private void reconnectButtonAction() {
		connectionErrorDialog.setExitOnCloseButton(false);
		connectionErrorDialog.setVisible(false);

		SingletonDBConnection.getInstance().connect();
		displayUI();
	}

	private ImportInvoice getImportInvoiceInstanceFromInputFields() {
		Timestamp importedDate = UtilFunctions.getTimestamp(
				importInvoiceDetailDialog.getImportedDatePanel().getSelectedYear(),
				importInvoiceDetailDialog.getImportedDatePanel().getSelectedMonth(),
				importInvoiceDetailDialog.getImportedDatePanel().getSelectedDay()
		);
		String note = UtilFunctions.removeRedundantWhiteSpace(
				importInvoiceDetailDialog.getNoteTextField().getText()
		);

		return new ImportInvoice(-1, importedDate, note, 0);
	}

	private ArrayList<ImportInvoiceDetail> getImportInvoiceDetailListFromInputFields() {
		NonEditableTableModel tableModel = (NonEditableTableModel) importInvoiceDetailDialog.getScrollableTable()
																							.getTableModel();
		int rowCount = tableModel.getRowCount();

		ArrayList<ImportInvoiceDetail> importInvoiceDetailList = new ArrayList<>();
		for (int i = 0; i < rowCount; i++) {
			importInvoiceDetailList.add(mapRowValueToImportInvoiceDetailInstance(tableModel.getRowValue(i)));
		}

		return importInvoiceDetailList;
	}

	private int calculateTotalPrice(ArrayList<ImportInvoiceDetail> importInvoiceDetailList) {
		int sum = 0;

		for (ImportInvoiceDetail item : importInvoiceDetailList) {
			sum += item.getPrice() * item.getQuantity();
		}

		return sum;
	}

	private int findIndexByProductId(ArrayList<Product> productList, int productId) {
		for (int i = 0; i < productList.size(); i++)
			if (productList.get(i).getId() == productId)
				return i;

		return 0;
	}


	// MARK: Document Listener methods

	@Override
	public void insertUpdate(DocumentEvent e) {
		// Do nothing
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		// Do nothing
	}

	@Override
	public void changedUpdate(DocumentEvent event) {
		int quantity = Integer.parseInt(
				String.valueOf(addProductToImportInvoiceDialog.getQuantityTextField().getValue())
		);
		int price = Integer.parseInt(
				String.valueOf(addProductToImportInvoiceDialog.getPriceTextField().getValue())
		);

		addProductToImportInvoiceDialog.getTotalPriceTextField()
									   .setText(String.valueOf(price * quantity));
	}

}
