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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Vector;

public class ImportInvoiceDetailController implements ActionListener, ItemListener, DocumentListener {

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

			// Add item listener
			this.addProductToImportInvoiceDialog.getProductNameComboBox().addItemListener(this);

			// Add document listener
			this.addProductToImportInvoiceDialog.getQuantityTextField().getDocument().addDocumentListener(this);
			this.addProductToImportInvoiceDialog.getPriceTextField().getDocument().addDocumentListener(this);
		}
		this.connectionErrorDialog.getReconnectButton().addActionListener(this);
	}

	public void displayUI() {
		if (importInvoice != null) {
			importInvoiceDetailDialog.getImportDateTextField()
									 .setText(UtilFunctions.formatTimestamp(
											 Constants.TIMESTAMP_PATTERN,
											 importInvoice.getImportedDate()
									 ));
			importInvoiceDetailDialog.getTotalPriceTextField()
									 .setText(String.valueOf(importInvoice.getTotalPrice()));
			importInvoiceDetailDialog.getNoteTextField()
									 .setText(importInvoice.getNote());

			loadImportInvoiceDetailListAndAddToTable();
		} else {
			importInvoiceDetailDialog.getImportedDatePanel()
									 .setSelectedDate(UtilFunctions.getTimestamp(LocalDateTime.now()));

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
				-1,
				-1,
				(int) rowValue.get(5),
				(int) rowValue.get(ImportInvoiceDetailDialog.HIDDEN_COLUMN_PRODUCT_ID),
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

	@Override
	public void itemStateChanged(ItemEvent event) {
		roomNameComboBoxActionOfAddProductToReceiptDialog();
	}

	private void addButtonAction() {
		if (productList.isEmpty()) {
			UtilFunctions.showInfoMessage(
					importInvoiceDetailDialog,
					"Add Product To Import Invoice",
					"There are no products to add into receipt."
			);
		} else {
			Vector<String> productNameList = new Vector<>();
			for (Product item : productList)
				productNameList.add(item.getName());

			addProductToImportInvoiceDialog.getProductNameComboBox()
										   .setModel(new DefaultComboBoxModel<>(productNameList));
			addProductToImportInvoiceDialog.getQuantityTextField()
										   .setValue(Constants.MIN_QUANTITY);

			// Obtain index of selected product in selectedProductList.
			int indexInProductNameComboBox = addProductToImportInvoiceDialog.getProductNameComboBox().getSelectedIndex();
			Product selectedProduct = productList.get(indexInProductNameComboBox);
			int indexInSelectedProductList = findIndexByProductId(selectedProductList, selectedProduct.getId());
			int totalPrice;

			// Check and disable Price ($) field.
			if (indexInSelectedProductList != -1) {
				int price = selectedProductList.get(indexInSelectedProductList).getPrice();
				totalPrice = price * Constants.MIN_QUANTITY;
				addProductToImportInvoiceDialog.getPriceTextField().setValue(price);
				addProductToImportInvoiceDialog.getPriceTextField().setEnabled(false);
			} else {
				totalPrice = Constants.MIN_PRICE * Constants.MIN_QUANTITY;
				addProductToImportInvoiceDialog.getPriceTextField().setValue(Constants.MIN_PRICE);
			}
			addProductToImportInvoiceDialog.getTotalPriceTextField().setText(String.valueOf(totalPrice));

			addProductToImportInvoiceDialog.setVisible(true);
		}
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
				Vector<Object> rowValue = tableModel.getRowValue(selectedRowIndex);

				// Update total price after removing product successfully.
				int price = (int) rowValue.get(4);
				int quantity = (int) rowValue.get(5);
				int currentTotalPrice = Integer.parseInt(importInvoiceDetailDialog.getTotalPriceTextField().getText());
				importInvoiceDetailDialog.getTotalPriceTextField()
										 .setText(String.valueOf(currentTotalPrice - (price * quantity)));

				int removedProductId = (int) rowValue.get(ImportInvoiceDetailDialog.HIDDEN_COLUMN_PRODUCT_ID);
				int removedProductIndex = findIndexByProductId(selectedProductList, removedProductId);
				selectedProductList.remove(removedProductIndex);

				// Remove product from table.
				tableModel.removeRow(selectedRowIndex);
			}
		}
	}

	private void createButtonAction() {
		try {
			ImportInvoiceDAO daoModel = new ImportInvoiceDAO();
			ImportInvoice newImportInvoice = getImportInvoiceInstanceFromInputFields();
			ArrayList<ImportInvoiceDetail> newImportInvoiceDetailList = getImportInvoiceDetailListFromInputFields();

			if (newImportInvoiceDetailList.isEmpty()) {
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
					ArrayList<Integer> currentQuantityList = getCurrentProductQuantityListFromImportInvoiceDetailList(newImportInvoiceDetailList);
					daoModel.insert(newImportInvoice, newImportInvoiceDetailList, currentQuantityList);
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
		int price = Integer.parseInt(String.valueOf(addProductToImportInvoiceDialog.getPriceTextField().getValue()));
		int quantity = Integer.parseInt(String.valueOf(addProductToImportInvoiceDialog.getQuantityTextField().getValue()));
		int selectedProductIndex = addProductToImportInvoiceDialog.getProductNameComboBox().getSelectedIndex();
		Product selectedProduct = productList.get(selectedProductIndex).deepCopy();
		selectedProduct.setStock(quantity);
		selectedProduct.setPrice(price);

		ImportInvoiceDetail addedImportInvoiceDetail = new ImportInvoiceDetail(
				-1,
				-1,
				quantity,
				selectedProduct.getId(),
				selectedProduct.getName(),
				selectedProduct.getProductType(),
				price
		);

		// Add product into table and selectedProductList.
		NonEditableTableModel tableModel = (NonEditableTableModel) importInvoiceDetailDialog.getScrollableTable().getTableModel();
		int productIndexInSelectedProductList = findIndexByProductId(selectedProductList, selectedProduct.getId());
		if (productIndexInSelectedProductList == -1) {
			selectedProductList.add(selectedProduct);

			int no = tableModel.getRowCount() + 1;
			tableModel.addRow(mapImportInvoiceDetailInstanceToRowValue(no, addedImportInvoiceDetail, selectedProduct.getId()));
		} else {
			selectedProductList.get(productIndexInSelectedProductList).addStock(quantity);
			tableModel.setValueAt(
					selectedProductList.get(productIndexInSelectedProductList).getStock(),
					productIndexInSelectedProductList,
					5
			);
		}

		// Update total price after adding product successfully.
		int currentTotalPrice = Integer.parseInt(importInvoiceDetailDialog.getTotalPriceTextField().getText());
		importInvoiceDetailDialog.getTotalPriceTextField()
								  .setText(String.valueOf(currentTotalPrice + (price * quantity)));

		addProductToImportInvoiceDialog.setVisible(false);
	}

	private void reconnectButtonAction() {
		connectionErrorDialog.setExitOnCloseButton(false);
		connectionErrorDialog.setVisible(false);

		SingletonDBConnection.getInstance().connect();

		// clear data
		productList.clear();
		selectedProductList.clear();
		NonEditableTableModel tableModel = (NonEditableTableModel) importInvoiceDetailDialog.getScrollableTable().getTableModel();
		tableModel.removeAllRows();

		// reload data
		displayUI();
	}

	private void roomNameComboBoxActionOfAddProductToReceiptDialog() {
		int indexInProductNameComboBox = addProductToImportInvoiceDialog.getProductNameComboBox().getSelectedIndex();
		Product selectedProduct = productList.get(indexInProductNameComboBox);
		int indexInSelectedProductList = findIndexByProductId(selectedProductList, selectedProduct.getId());

		int price;
		if (indexInSelectedProductList == -1) {
			addProductToImportInvoiceDialog.getPriceTextField().setValue(Constants.MIN_PRICE);
			addProductToImportInvoiceDialog.getPriceTextField().setEnabled(true);
			price = Integer.parseInt(addProductToImportInvoiceDialog.getPriceTextField().getText());
		} else {
			price = selectedProductList.get(indexInSelectedProductList).getPrice();
			addProductToImportInvoiceDialog.getPriceTextField().setValue(price);
			addProductToImportInvoiceDialog.getPriceTextField().setEnabled(false);
		}

		int quantity = Integer.parseInt(addProductToImportInvoiceDialog.getQuantityTextField().getText());
		addProductToImportInvoiceDialog.getTotalPriceTextField().setText(String.valueOf(price * quantity));
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
		int totalPrice = Integer.parseInt(importInvoiceDetailDialog.getTotalPriceTextField().getText());

		return new ImportInvoice(-1, importedDate, note, totalPrice);
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

	private ArrayList<Integer> getCurrentProductQuantityListFromImportInvoiceDetailList(
			ArrayList<ImportInvoiceDetail> importInvoiceDetailList
	) {
		ArrayList<Integer> quantityList = new ArrayList<>();
		for (ImportInvoiceDetail item: importInvoiceDetailList) {
			int index = findIndexByProductId(productList, item.getProductId());
			quantityList.add(productList.get(index).getStock());
		}

		return quantityList;
	}

	private int findIndexByProductId(ArrayList<Product> productList, int productId) {
		for (int i = 0; i < productList.size(); i++)
			if (productList.get(i).getId() == productId)
				return i;

		return -1;
	}


	// MARK: Document Listener methods

	@Override
	public void insertUpdate(DocumentEvent event) {
		int quantity = Integer.parseInt(addProductToImportInvoiceDialog.getQuantityTextField().getText());
		int price = Integer.parseInt(addProductToImportInvoiceDialog.getPriceTextField().getText());

		addProductToImportInvoiceDialog.getTotalPriceTextField().setText(String.valueOf(price * quantity));
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		// Do nothing
	}

	@Override
	public void changedUpdate(DocumentEvent event) {
		// Do nothing
	}

}
