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
import views.dialogs.AddProductToReceiptDialog;
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
	private AddProductToReceiptDialog addProductToReceiptDialog;
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
			this.addProductToReceiptDialog = new AddProductToReceiptDialog(mainFrame);

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
									  .setText(
											  UtilFunctions.formatTimestamp(
													  Constants.TIMESTAMP_WITHOUT_NANOSECOND,
													  receipt.getPurchasedDate()
											  )
									  );
			productReceiptDetailDialog.getTotalPriceTextField()
									  .setText(String.valueOf(receipt.getTotalPrice()));
			productReceiptDetailDialog.getNoteTextField()
									  .setText(receipt.getNote());

			loadReceiptDetailListAndAddToTable();
		} else {
			Timestamp nowAfterTwoHour = UtilFunctions.getTimestamp(LocalDateTime.now(), 1);
			productReceiptDetailDialog.getPurchasedDatePanel().setSelectedDate(nowAfterTwoHour);

			loadProductList();
		}

		productReceiptDetailDialog.setVisible(true);
	}

	private void loadReceiptDetailListAndAddToTable() {
		try {
			ReceiptDetailDAO daoModel = new ReceiptDetailDAO();
			ArrayList<ReceiptDetail> receiptDetailList = daoModel.getAllByReceiptId(receipt.getId());
			addReceiptDetailLstToTable(receiptDetailList);
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

	public void addReceiptDetailLstToTable(ArrayList<ReceiptDetail> receiptDetailList) {
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

	private ReceiptDetail mapRowValueToReceiptDetailInstance(Vector<Object> rowValue) {
		Product.ProductTypeEnum productType = Product.ProductTypeEnum.valueOfIgnoreCase(
				String.valueOf(rowValue.get(3))
		);

		return new ReceiptDetail(
				(int) rowValue.get(ProductReceiptDetailDialog.HIDDEN_COLUMN_PRODUCT_ID),
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
		Vector<String> productNameList = new Vector<>();
		for (Product item: productList)
			productNameList.add(item.getName());

		addProductToReceiptDialog.getProductNameComboBox()
								 .setModel(new DefaultComboBoxModel<>(productNameList));
		addProductToReceiptDialog.getQuantityTextField()
								 .setValue(Constants.MIN_QUANTITY);
		addProductToReceiptDialog.getTotalPriceTextField()
								 .setText(String.valueOf(productList.get(0).getPrice()));

		addProductToReceiptDialog.setVisible(true);
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

				int removedProductId = (int) tableModel.getValueAt(
						selectedRowIndex,
						ProductReceiptDetailDialog.HIDDEN_COLUMN_PRODUCT_ID
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
			ReceiptDAO daoModel = new ReceiptDAO();
			Receipt newReceipt = getReceiptInstanceFromInputFields();
			ArrayList<ReceiptDetail> newReceiptDetailList = getReceiptDetailListFromInputFields();

			newReceipt.setTotalPrice(calculateTotalPrice(newReceiptDetailList));

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
					daoModel.insert(newReceipt, newReceiptDetailList);
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
		int selectedProductIndex = addProductToReceiptDialog.getProductNameComboBox().getSelectedIndex();
		Product selectedProduct = productList.get(selectedProductIndex);
		byte quantity = Byte.parseByte(
				String.valueOf(addProductToReceiptDialog.getQuantityTextField().getValue())
		);

		ReceiptDetail addedReceiptDetail = new ReceiptDetail(
				-1,
				receipt.getId(),
				quantity,
				selectedProduct.getName(),
				selectedProduct.getProductType(),
				selectedProduct.getPrice()
		);

		selectedProductList.add(selectedProduct);
		productList.remove(selectedProductIndex);

		NonEditableTableModel tableModel = (NonEditableTableModel) productReceiptDetailDialog.getScrollableTable().getTableModel();
		int no = tableModel.getRowCount() + 1;
		tableModel.addRow(mapReceiptDetailInstanceToRowValue(no, addedReceiptDetail, selectedProduct.getId()));

		addProductToReceiptDialog.setVisible(false);
	}

	private void reconnectButtonAction() {
		connectionErrorDialog.setExitOnCloseButton(false);
		connectionErrorDialog.setVisible(false);

		SingletonDBConnection.getInstance().connect();
		displayUI();
	}

	private void roomNameComboBoxActionOfAddProductToReceiptDialog() {
		int index = addProductToReceiptDialog.getProductNameComboBox().getSelectedIndex();
		Product selectedProduct = productList.get(index);

		int quantity = Integer.parseInt(
				String.valueOf(addProductToReceiptDialog.getQuantityTextField().getValue())
		);
		int totalPrice = selectedProduct.getPrice() * quantity;

		addProductToReceiptDialog.getTotalPriceTextField()
								 .setText(String.valueOf(totalPrice));
	}

	private boolean checkEmptyFields(String customerName) {
		return !customerName.isEmpty();
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

		return new Receipt(-1, customerName, purchasedDate, note, 0);
	}

	private ArrayList<ReceiptDetail> getReceiptDetailListFromInputFields() {
		NonEditableTableModel tableModel = (NonEditableTableModel) productReceiptDetailDialog.getScrollableTable().getTableModel();
		int rowCount = tableModel.getRowCount();

		ArrayList<ReceiptDetail> receiptDetailList = new ArrayList<>();
		for (int i = 0; i < rowCount; i++) {
			receiptDetailList.add(mapRowValueToReceiptDetailInstance(tableModel.getRowValue(i)));
		}

		return receiptDetailList;
	}

	private int calculateTotalPrice(ArrayList<ReceiptDetail> receiptDetailList) {
		int sum = 0;

		for (ReceiptDetail item: receiptDetailList) {
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
		if (event.getDocument() == addProductToReceiptDialog.getQuantityTextField().getDocument()) {
			roomNameComboBoxActionOfAddProductToReceiptDialog();
		}
	}

}
