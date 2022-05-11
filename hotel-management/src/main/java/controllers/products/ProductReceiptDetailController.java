package controllers.products;

import dao.ProductDAO;
import dao.ReceiptDAO;
import dao.ReceiptDetailDAO;
import db.DBConnectionException;
import db.SingletonDBConnection;
import models.Product;
import models.Receipt;
import models.ReceiptDetail;
import utils.Constants;
import utils.DetailDialogModeEnum;
import utils.UtilFunctions;
import views.components.dialogs.ConnectionErrorDialog;
import views.components.panels.ScrollableTablePanel;
import views.components.table_model.NonEditableTableModel;
import views.dialogs.AddProductToImportInvoiceDialog;
import views.dialogs.ProductReceiptDetailDialog;

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

public class ProductReceiptDetailController implements ActionListener, ItemListener, DocumentListener {

	private final ProductReceiptDetailDialog productReceiptDetailDialog;
	private AddProductToImportInvoiceDialog addProductToReceiptDialog;
	private final ConnectionErrorDialog connectionErrorDialog;

	private final Receipt receipt;
	private ArrayList<Product> productList;
	private ArrayList<Product> selectedProductList;
	private final DetailDialogModeEnum viewMode;

	private final ProductReceiptListController productReceiptListController;

	public ProductReceiptDetailController(
			ProductReceiptDetailDialog dialog,
			JFrame mainFrame,
			DetailDialogModeEnum viewMode,
			ProductReceiptListController serviceListController
	) {
		this(dialog, mainFrame, null, viewMode, serviceListController);
	}

	public ProductReceiptDetailController(
			ProductReceiptDetailDialog dialog,
			JFrame mainFrame,
			Receipt receipt,
			DetailDialogModeEnum viewMode,
			ProductReceiptListController productReceiptListController
	) {
		this.productReceiptDetailDialog = dialog;
		this.connectionErrorDialog = new ConnectionErrorDialog(mainFrame);
		this.receipt = receipt;
		this.viewMode = viewMode;
		this.productReceiptListController = productReceiptListController;

		if (viewMode == DetailDialogModeEnum.CREATE) {
			this.addProductToReceiptDialog = new AddProductToImportInvoiceDialog(mainFrame);
			this.addProductToReceiptDialog.getPriceTextField().setEnabled(false);

			// Add action listeners
			this.productReceiptDetailDialog.getAddButton().addActionListener(this);
			this.productReceiptDetailDialog.getRemoveButton().addActionListener(this);
			this.productReceiptDetailDialog.getCreateButton().addActionListener(this);
			this.productReceiptDetailDialog.getCancelButton().addActionListener(this);
			this.addProductToReceiptDialog.getAddButton().addActionListener(this);
			this.addProductToReceiptDialog.getCancelButton().addActionListener(this);

			// Add item listener
			this.addProductToReceiptDialog.getProductNameComboBox().addItemListener(this);

			// Add document listener
			this.addProductToReceiptDialog.getQuantityTextField().getDocument().addDocumentListener(this);
		}
		this.connectionErrorDialog.getReconnectButton().addActionListener(this);
	}

	public void displayUI() {
		if (receipt != null) {
			productReceiptDetailDialog.getCustomerNameTextField()
									  .setText(receipt.getCustomerName());
			productReceiptDetailDialog.getPurchasedDateTextField()
									  .setText(UtilFunctions.formatTimestamp(
											  Constants.TIMESTAMP_PATTERN,
											  receipt.getPurchasedDate()
									  ));
			productReceiptDetailDialog.getTotalPriceTextField()
									  .setText(String.valueOf(receipt.getTotalPrice()));
			productReceiptDetailDialog.getNoteTextField()
									  .setText(receipt.getNote());

			loadReceiptDetailListAndAddToTable();
		} else {
			productReceiptDetailDialog.getPurchasedDatePanel()
									  .setSelectedDate(UtilFunctions.getTimestamp(LocalDateTime.now()));

			loadProductList();
		}

		productReceiptDetailDialog.setVisible(true);
	}

	private void loadReceiptDetailListAndAddToTable() {
		try {
			ReceiptDetailDAO daoModel = new ReceiptDetailDAO();
			ArrayList<ReceiptDetail> receiptDetailList = daoModel.getAllByReceiptId(receipt.getId());
			addReceiptDetailListToTable(receiptDetailList);
		} catch (DBConnectionException e) {
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
			System.out.println("ProductReceiptDetailController.java - loadReceiptDetailListAndAddToTable - catch - Unavailable connection.");
		}
	}

	private void loadProductList() {
		try {
			ProductDAO daoModel = new ProductDAO();
			productList = daoModel.getAllAvailable();
			selectedProductList = new ArrayList<>();
		} catch (DBConnectionException e) {
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
			System.out.println("ProductReceiptDetailController.java - loadProductList - catch - Unavailable connection.");
		}
	}

	public void addReceiptDetailListToTable(ArrayList<ReceiptDetail> receiptDetailList) {
		NonEditableTableModel tableModel = (NonEditableTableModel) productReceiptDetailDialog.getScrollableTable().getTableModel();
		tableModel.removeAllRows();

		for (int i = 0; i < receiptDetailList.size(); i++) {
			Object[] rowValue = mapReceiptDetailInstanceToRowValue(i + 1, receiptDetailList.get(i), -1);
			tableModel.addRow(rowValue);
		}
	}

	private Object[] mapReceiptDetailInstanceToRowValue(int no, ReceiptDetail receiptDetail, int productId) {
		String capitalizedProductType = UtilFunctions.capitalizeFirstLetterInString(
				Product.ProductTypeEnum.valueOf(receiptDetail.getProductType()).name()
		);

		return new Object[]{
				no,
				productId,
				receiptDetail.getProductName(),
				capitalizedProductType,
				receiptDetail.getPrice(),
				receiptDetail.getQuantity(),
		};
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == connectionErrorDialog.getReconnectButton()) {
			reconnectButtonAction();
		}

		if (viewMode == DetailDialogModeEnum.CREATE) {
			if (event.getSource() == productReceiptDetailDialog.getAddButton()) {
				addButtonAction();
			} else if (event.getSource() == productReceiptDetailDialog.getRemoveButton()) {
				removeButtonAction();
			} else if (event.getSource() == productReceiptDetailDialog.getCreateButton()) {
				createButtonAction();
			} else if (event.getSource() == productReceiptDetailDialog.getCancelButton()) {
				productReceiptDetailDialog.setVisible(false);
			} else if (event.getSource() == addProductToReceiptDialog.getAddButton()) {
				addButtonActionOfAddProductToReceiptDialog();
			} else if (event.getSource() == addProductToReceiptDialog.getCancelButton()) {
				addProductToReceiptDialog.setVisible(false);
			}
		}
	}

	@Override
	public void itemStateChanged(ItemEvent event) {
		if (event.getSource() == addProductToReceiptDialog.getProductNameComboBox()) {
			roomNameComboBoxActionOfAddProductToReceiptDialog();
		}
	}

	private void addButtonAction() {
		if (productList.isEmpty()) {
			UtilFunctions.showInfoMessage(
					productReceiptDetailDialog,
					"Add Product To Receipt",
					"There are no products to add into receipt."
			);
		} else {
			Vector<String> productNameList = new Vector<>();
			for (Product item: productList)
				productNameList.add(item.getName());

			int price = productList.get(0).getPrice();
			int quantity = productList.get(0).getStock();

			addProductToReceiptDialog.getProductNameComboBox()
									 .setModel(new DefaultComboBoxModel<>(productNameList));
			addProductToReceiptDialog.getQuantityTextField()
									 .setValue(Constants.MIN_QUANTITY);
			addProductToReceiptDialog.getPriceTextField()
									 .setValue(price);
			addProductToReceiptDialog.getTotalPriceTextField()
									 .setText(String.valueOf(price * Constants.MIN_QUANTITY));

			addProductToReceiptDialog.setRangeQuantity(Constants.MIN_QUANTITY, quantity);
			addProductToReceiptDialog.setVisible(true);
		}
	}

	private void removeButtonAction() {
		ScrollableTablePanel tablePanel = productReceiptDetailDialog.getScrollableTable();
		int selectedRowIndex = tablePanel.getTable().getSelectedRow();

		if (selectedRowIndex == -1) {
			UtilFunctions.showWarningMessage(
					productReceiptDetailDialog,
					"Remove Product",
					"You must select a row."
			);
		} else {
			int option = JOptionPane.showConfirmDialog(
					productReceiptDetailDialog,
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
				int currentTotalPrice = Integer.parseInt(productReceiptDetailDialog.getTotalPriceTextField().getText());
				productReceiptDetailDialog.getTotalPriceTextField()
										  .setText(String.valueOf(currentTotalPrice - (price * quantity)));

				// Remove product from saved list of products.
				int removedProductId = (int) rowValue.get(ProductReceiptDetailDialog.HIDDEN_COLUMN_PRODUCT_ID);
				int removedProductIndexInSelectedProductList = findIndexByProductId(selectedProductList, removedProductId);
				int removedProductIndexInProductList = findIndexByProductId(productList, removedProductId);
				Product removedProduct = selectedProductList.get(removedProductIndexInSelectedProductList);

				selectedProductList.remove(removedProductIndexInSelectedProductList);
				if (removedProductIndexInProductList == -1) {
					productList.add(removedProduct.deepCopy());
				} else {
					productList.get(removedProductIndexInProductList).addStock(quantity);
				}

				// Remove product from table.
				tableModel.removeRow(selectedRowIndex);
			}
		}
	}

	private void createButtonAction() {
		try {
			ReceiptDAO daoModel = new ReceiptDAO();
			Receipt newReceipt = getReceiptInstanceFromInputFields();
			ArrayList<ReceiptDetail> newReceiptDetailList = getReceiptDetailListFromInputFields();

			if (checkEmptyFields(newReceipt.getCustomerName())) {
				UtilFunctions.showErrorMessage(
						productReceiptDetailDialog,
						"Create Receipt",
						"All fields (except note field) must not be empty."
				);
			} else if (newReceiptDetailList.isEmpty()) {
				UtilFunctions.showErrorMessage(
						productReceiptDetailDialog,
						"Create Receipt",
						"The receipt must has at least one product."
				);
			} else {
				int option = UtilFunctions.showConfirmDialog(
						productReceiptDetailDialog,
						"Create Receipt",
						"Are you sure to create this receipt?"
				);

				if (option == JOptionPane.YES_OPTION) {
					ArrayList<Integer> remainingQuantityList = getRemainingProductQuantityListFromReceiptDetailList(newReceiptDetailList);
					daoModel.insert(newReceipt, newReceiptDetailList, remainingQuantityList);
					UtilFunctions.showInfoMessage(productReceiptDetailDialog, "Create Receipt", "Create successfully.");

					productReceiptDetailDialog.setVisible(false);
					productReceiptListController.loadProductReceiptListAndReloadTableData();
				}
			}
		} catch (DBConnectionException e) {
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
			System.out.println("ProductReceiptDetailController.java - createButtonAction - catch - Unavailable connection.");
		}
	}

	private void addButtonActionOfAddProductToReceiptDialog() {
		int quantity = Integer.parseInt(String.valueOf(addProductToReceiptDialog.getQuantityTextField().getValue()));
		int selectedProductIndex = addProductToReceiptDialog.getProductNameComboBox().getSelectedIndex();
		Product selectedProduct = productList.get(selectedProductIndex).deepCopy();
		selectedProduct.setStock(quantity);

		ReceiptDetail addedReceiptDetail = new ReceiptDetail(
				-1,
				-1,
				quantity,
				selectedProduct.getId(),
				selectedProduct.getName(),
				selectedProduct.getProductType(),
				selectedProduct.getPrice()
		);

		// That product will be changed quantity or be removed.
		productList.get(selectedProductIndex).addStock(-quantity);
		if (productList.get(selectedProductIndex).getStock() == 0) {
			productList.remove(selectedProductIndex);
		}

		// Add product into table and selectedProductList.
		NonEditableTableModel tableModel = (NonEditableTableModel) productReceiptDetailDialog.getScrollableTable().getTableModel();
		int productIndexInSelectedProductList = findIndexByProductId(selectedProductList, selectedProduct.getId());
		if (productIndexInSelectedProductList == -1) {
			selectedProductList.add(selectedProduct);

			int no = tableModel.getRowCount() + 1;
			tableModel.addRow(mapReceiptDetailInstanceToRowValue(no, addedReceiptDetail, selectedProduct.getId()));
		} else {
			selectedProductList.get(productIndexInSelectedProductList).addStock(quantity);
			tableModel.setValueAt(
					selectedProductList.get(productIndexInSelectedProductList).getStock(),
					productIndexInSelectedProductList,
					5
			);
		}

		// Update total price after adding product successfully.
		int price = selectedProduct.getPrice();
		int currentTotalPrice = Integer.parseInt(productReceiptDetailDialog.getTotalPriceTextField().getText());
		productReceiptDetailDialog.getTotalPriceTextField()
								  .setText(String.valueOf(currentTotalPrice + (price * quantity)));

		addProductToReceiptDialog.setVisible(false);
	}

	private void reconnectButtonAction() {
		connectionErrorDialog.setExitOnCloseButton(false);
		connectionErrorDialog.setVisible(false);

		SingletonDBConnection.getInstance().connect();

		// clear data
		productList.clear();
		selectedProductList.clear();
		NonEditableTableModel tableModel = (NonEditableTableModel) productReceiptDetailDialog.getScrollableTable().getTableModel();
		tableModel.removeAllRows();

		// reload data
		displayUI();
	}

	private void roomNameComboBoxActionOfAddProductToReceiptDialog() {
		int index = addProductToReceiptDialog.getProductNameComboBox().getSelectedIndex();
		Product selectedProduct = productList.get(index);

		int price = selectedProduct.getPrice();
		int quantity = Integer.parseInt(addProductToReceiptDialog.getQuantityTextField().getText());

		addProductToReceiptDialog.getPriceTextField()
								 .setValue(price);
		addProductToReceiptDialog.getTotalPriceTextField()
								 .setText(String.valueOf(price * quantity));
	}

	private boolean checkEmptyFields(String customerName) {
		return customerName.isEmpty();
	}

	private Receipt getReceiptInstanceFromInputFields() {
		String customerName = UtilFunctions.removeRedundantWhiteSpace(
				productReceiptDetailDialog.getCustomerNameTextField().getText()
		);
		Timestamp purchasedDate = UtilFunctions.getTimestamp(
				productReceiptDetailDialog.getPurchasedDatePanel().getSelectedYear(),
				productReceiptDetailDialog.getPurchasedDatePanel().getSelectedMonth(),
				productReceiptDetailDialog.getPurchasedDatePanel().getSelectedDay()
		);
		String note = UtilFunctions.removeRedundantWhiteSpace(
				productReceiptDetailDialog.getNoteTextField().getText()
		);
		int totalPrice = Integer.parseInt(productReceiptDetailDialog.getTotalPriceTextField().getText());

		return new Receipt(-1, customerName, purchasedDate, note, totalPrice);
	}

	private ArrayList<ReceiptDetail> getReceiptDetailListFromInputFields() {
		ArrayList<ReceiptDetail> receiptDetailList = new ArrayList<>();
		for (Product item: selectedProductList) {
			receiptDetailList.add(new ReceiptDetail(
					-1,
					-1,
					item.getStock(),
					item.getId(),
					item.getName(),
					item.getProductType(),
					item.getPrice()
			));
		}

		return receiptDetailList;
	}

	private ArrayList<Integer> getRemainingProductQuantityListFromReceiptDetailList(
			ArrayList<ReceiptDetail> receiptDetailList
	) {
		ArrayList<Integer> quantityList = new ArrayList<>();
		for (ReceiptDetail item: receiptDetailList) {
			int index = findIndexByProductId(productList, item.getProductId());
			quantityList.add(index == -1 ? 0 : productList.get(index).getStock());
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
		roomNameComboBoxActionOfAddProductToReceiptDialog();
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
