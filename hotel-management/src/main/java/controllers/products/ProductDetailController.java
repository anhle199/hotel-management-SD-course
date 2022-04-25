package controllers.products;

import dao.ProductDAO;
import db.DBConnectionException;
import db.SingletonDBConnection;
import models.Product;
import utils.DetailDialogModeEnum;
import utils.UtilFunctions;
import views.components.dialogs.ConnectionErrorDialog;
import views.dialogs.ProductDetailDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProductDetailController implements ActionListener {

	private final ProductDetailDialog productDetailDialog;
	private final ConnectionErrorDialog connectionErrorDialog;

	private final Product product;
	private final DetailDialogModeEnum viewMode;

	private final ProductListController productListController;

	public ProductDetailController(
			ProductDetailDialog dialog,
			JFrame mainFrame,
			DetailDialogModeEnum viewMode,
			ProductListController serviceListController
	) {
		this(dialog, mainFrame, null, viewMode, serviceListController);
	}

	public ProductDetailController(
			ProductDetailDialog dialog,
			JFrame mainFrame,
			Product product,
			DetailDialogModeEnum viewMode,
			ProductListController serviceListController
	) {
		this.productDetailDialog = dialog;
		this.connectionErrorDialog = new ConnectionErrorDialog(mainFrame);
		this.product = product;
		this.viewMode = viewMode;
		this.productListController = serviceListController;

		// Add action listeners
		this.productDetailDialog.getPositiveButton().addActionListener(this);
		this.productDetailDialog.getNegativeButton().addActionListener(this);
		this.connectionErrorDialog.getReconnectButton().addActionListener(this);
	}

	public void displayUI() {
		productDetailDialog.getProductTypeComboBox()
						   .setModel(new DefaultComboBoxModel<>(Product.ProductTypeEnum.allCases()));

		if (product != null) {
			productDetailDialog.getProductNameTextField()
							   .setText(product.getName());
			productDetailDialog.getPriceTextField()
							   .setText(String.valueOf(product.getPrice()));
			productDetailDialog.getQuantityTextField()
							   .setText(String.valueOf(product.getStock()));
			productDetailDialog.getNoteTextArea()
							   .setText(product.getNote());
		}

		productDetailDialog.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == productDetailDialog.getPositiveButton()) {
			positiveButtonAction();
		} else if (event.getSource() == productDetailDialog.getNegativeButton()) {
			negativeButtonAction();
		} else if (event.getSource() == connectionErrorDialog.getReconnectButton()) {
			reconnectButtonAction();
		}
	}

	private void positiveButtonAction() {
		if (viewMode == DetailDialogModeEnum.VIEW_ONLY) {
			editButtonAction();
		} else if (viewMode == DetailDialogModeEnum.EDITING) {
			saveButtonAction();
		} else if (viewMode == DetailDialogModeEnum.CREATE) {
			createButtonAction();
		}
	}

	private void negativeButtonAction() {
		if (viewMode == DetailDialogModeEnum.VIEW_ONLY) {
			productDetailDialog.setVisible(false);
		} else if (viewMode == DetailDialogModeEnum.EDITING) {
			rollbackAllChanges();
		} else if (viewMode == DetailDialogModeEnum.CREATE) {
			productDetailDialog.setVisible(false);
		}
	}

	private void reconnectButtonAction() {
		connectionErrorDialog.setExitOnCloseButton(false);
		connectionErrorDialog.setVisible(false);

		SingletonDBConnection.getInstance().connect();
		displayUI();
	}

	private void editButtonAction() {
		productDetailDialog.setViewMode(DetailDialogModeEnum.EDITING);
	}

	private void saveButtonAction() {
		try {
			ProductDAO daoModel = new ProductDAO();
			Product newProduct = getProductInstanceFromInputFields();

			if (product.equals(newProduct)) {
				UtilFunctions.showInfoMessage(
						productDetailDialog,
						"Edit Product",
						"Information does not change."
				);
			} else if (checkEmptyFields(newProduct.getName())) {
				UtilFunctions.showErrorMessage(
						productDetailDialog,
						"Edit Product",
						"All fields (except note field) must not be empty."
				);
			} else if (daoModel.isExistingProductName(newProduct.getName())) {
				UtilFunctions.showErrorMessage(
						productDetailDialog,
						"Edit Product",
						"This product name is existing."
				);
			} else {
				int option = UtilFunctions.showConfirmDialog(
						productDetailDialog,
						"Edit Product",
						"Are you sure to save new information for this product?"
				);

				if (option == JOptionPane.YES_OPTION) {
					daoModel.update(newProduct);
					UtilFunctions.showInfoMessage(productDetailDialog, "Edit Product", "Save successfully.");
				}
			}
		} catch (DBConnectionException e) {
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
			System.out.println("ProductDetailController.java - saveButtonAction - catch - Unavailable connection.");
		}
	}

	private void createButtonAction() {
		try {
			ProductDAO daoModel = new ProductDAO();
			Product newProduct = getProductInstanceFromInputFields();

			if (checkEmptyFields(newProduct.getName())) {
				UtilFunctions.showErrorMessage(
						productDetailDialog,
						"Create Product",
						"All fields (except note field) must not be empty."
				);
			} else if (daoModel.isExistingProductName(newProduct.getName())) {
				UtilFunctions.showErrorMessage(
						productDetailDialog,
						"Create Product",
						"This product name is existing."
				);
			} else {
				int option = UtilFunctions.showConfirmDialog(
						productDetailDialog,
						"Create Product",
						"Are you sure to create this product?"
				);

				if (option == JOptionPane.YES_OPTION) {
					daoModel.insert(newProduct);
					UtilFunctions.showInfoMessage(productDetailDialog, "Create Product", "Create successfully.");

					productDetailDialog.setVisible(false);
					productListController.loadProductListAndReloadTableData("");
				}
			}
		} catch (DBConnectionException e) {
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
			System.out.println("ProductDetailController.java - createButtonAction - catch - Unavailable connection.");
		}
	}

	private void rollbackAllChanges() {
		Product.ProductTypeEnum productType = Product.ProductTypeEnum.valueOf(product.getProductType());

		productDetailDialog.getProductNameTextField()
						   .setText(product.getName());
		productDetailDialog.getProductTypeComboBox()
						   .setSelectedItem(productType.name());
		productDetailDialog.getPriceTextField()
						   .setText(String.valueOf(product.getPrice()));
		productDetailDialog.getQuantityTextField()
						   .setText(String.valueOf(product.getStock()));
		productDetailDialog.getNoteTextArea()
						   .setText(product.getNote());

		productDetailDialog.setViewMode(DetailDialogModeEnum.VIEW_ONLY);
	}

	private boolean checkEmptyFields(String productName) {
		return !productName.isEmpty();
	}

	private Product getProductInstanceFromInputFields() {
		int productId = product != null ? product.getId() : -1;
		String productName = UtilFunctions.removeRedundantWhiteSpace(
				productDetailDialog.getProductNameTextField().getText()
		);
		int price = Integer.parseInt(productDetailDialog.getPriceTextField().getText());
		int quantity = Integer.parseInt(productDetailDialog.getQuantityTextField().getText());
		String note = UtilFunctions.removeRedundantWhiteSpace(
				productDetailDialog.getNoteTextArea().getText()
		);
		Product.ProductTypeEnum productType = Product.ProductTypeEnum.valueOfIgnoreCase(
				String.valueOf(productDetailDialog.getProductTypeComboBox().getSelectedItem())
		);

		return new Product(productId, productName, price, quantity, note, productType.ordinal());
	}

}
