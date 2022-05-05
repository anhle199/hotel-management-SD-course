package controllers.products;

import dao.ProductDAO;
import db.DBConnectionException;
import db.SingletonDBConnection;
import models.Product;
import utils.DetailDialogModeEnum;
import utils.UtilFunctions;
import views.components.dialogs.ConnectionErrorDialog;
import views.components.panels.ScrollableTablePanel;
import views.components.table_model.NonEditableTableModel;
import views.dialogs.ProductDetailDialog;
import views.panels.products.ProductListPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Vector;

public class ProductListController implements ActionListener {

	private final ProductListPanel productListPanel;
	private final JFrame mainFrame;
	private final ConnectionErrorDialog connectionErrorDialog;
	private final ProductDAO daoModel;

	public ProductListController(ProductListPanel productListPanel, JFrame mainFrame) {
		this.productListPanel = productListPanel;
		this.mainFrame = mainFrame;
		this.connectionErrorDialog = new ConnectionErrorDialog(mainFrame);
		this.daoModel = new ProductDAO();

		// Add action listeners
		this.productListPanel.getSearchButton().addActionListener(this);
		this.productListPanel.getAddButton().addActionListener(this);
		this.productListPanel.getRemoveButton().addActionListener(this);
		this.connectionErrorDialog.getReconnectButton().addActionListener(this);

		// Add double-click listener for each row on the table
		productListPanel.getScrollableTable().getTable().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent mouseEvent) {
				if (mouseEvent.getClickCount() == 2) {
					doubleClicksInRowAction();
				}
			}
		});
	}

	public void loadProductListAndReloadTableData(String productNameToken) {
		try {
			ArrayList<Product> productList;
			if (productNameToken.isEmpty()) {
				productList = daoModel.getAll();
			} else {
				productList = daoModel.searchByProductName(productNameToken);
			}

			addProductListToTable(productList);
		} catch (DBConnectionException e) {
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
			System.out.println("ProductListController.java - loadProductListAndReloadTableData - catch - Unavailable connection.");
		}
	}

	public void addProductListToTable(ArrayList<Product> productList) {
		NonEditableTableModel tableModel = (NonEditableTableModel) productListPanel.getScrollableTable().getTableModel();
		tableModel.removeAllRows();

		for (int i = 0; i < productList.size(); i++) {
			Object[] rowValue = mapProductInstanceToRowValue(i + 1, productList.get(i));
			tableModel.addRow(rowValue);
		}
	}

	private Object[] mapProductInstanceToRowValue(int no, Product product) {
		String capitalizedProductType = UtilFunctions.capitalizeFirstLetterInString(
				Product.ProductTypeEnum.valueOf(product.getProductType()).name()
		);

		return new Object[]{
				no,
				product.getId(),
				product.getName(),
				capitalizedProductType,
				product.getPrice(),
				product.getStock(),
				product.getDescription(),
		};
	}

	private Product mapRowValueToProductInstance(Vector<Object> rowValue) {
		Product.ProductTypeEnum productType = Product.ProductTypeEnum.valueOfIgnoreCase(
				String.valueOf(rowValue.get(3))
		);

		return new Product(
				(int) rowValue.get(ProductListPanel.HIDDE_COLUMN_PRODUCT_ID),
				String.valueOf(rowValue.get(2)),
				(int) rowValue.get(4),
				(int) rowValue.get(5),
				String.valueOf(rowValue.get(6)),
				productType.ordinal()
		);
	}

	public void displayUI() {
		loadProductListAndReloadTableData("");
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == productListPanel.getSearchButton()) {
			searchButtonAction();
		} else if (event.getSource() == productListPanel.getAddButton()) {
			addButtonAction();
		} else if (event.getSource() == productListPanel.getRemoveButton()) {
			removeButtonAction();
		} else if (event.getSource() == connectionErrorDialog.getReconnectButton()) {
			reconnectButtonAction();
		}
	}

	private void searchButtonAction() {
		String searchText = UtilFunctions.removeRedundantWhiteSpace(
				productListPanel.getSearchBar().getText()
		);
		loadProductListAndReloadTableData(searchText);
	}

	private void addButtonAction() {
		DetailDialogModeEnum viewMode = DetailDialogModeEnum.CREATE;
		ProductDetailDialog productDetailDialog = new ProductDetailDialog(mainFrame, viewMode);
		ProductDetailController productDetailController = new ProductDetailController(
				productDetailDialog, mainFrame, viewMode, this
		);

		productDetailController.displayUI();
	}

	private void removeButtonAction() {
		ScrollableTablePanel tablePanel = productListPanel.getScrollableTable();
		int selectedRowIndex = tablePanel.getTable().getSelectedRow();

		if (selectedRowIndex == -1) {
			UtilFunctions.showWarningMessage(productListPanel, "Remove Product", "You must select a row.");
		} else {
			int option = JOptionPane.showConfirmDialog(
					productListPanel,
					"Are you sure to remove this product?",
					"Remove Product",
					JOptionPane.YES_NO_OPTION
			);

			if (option == JOptionPane.YES_OPTION) {
				int productId = (int) tablePanel.getTableModel()
						.getValueAt(selectedRowIndex, ProductListPanel.HIDDE_COLUMN_PRODUCT_ID);

				try {
					daoModel.delete(productId);

					UtilFunctions.showInfoMessage(
							productListPanel,
							"Remove Product",
							"This product is removed successfully."
					);
				} catch (DBConnectionException e) {
					SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
					System.out.println("ProductListController.java - removeButtonAction - catch - Unavailable connection.");
				}
			}
		}
	}

	private void reconnectButtonAction() {
		connectionErrorDialog.setExitOnCloseButton(false);
		connectionErrorDialog.setVisible(false);

		SingletonDBConnection.getInstance().connect();
		productListPanel.getSearchBar().setText("");
		loadProductListAndReloadTableData("");
	}

	private void doubleClicksInRowAction() {
		JTable table = productListPanel.getScrollableTable().getTable();
		NonEditableTableModel tableModel = (NonEditableTableModel) table.getModel();
		Vector<Object> selectedRowValue = tableModel.getRowValue(table.getSelectedRow());
		Product selectedProduct = mapRowValueToProductInstance(selectedRowValue);

		DetailDialogModeEnum viewMode = DetailDialogModeEnum.VIEW_ONLY;
		ProductDetailDialog productDetailDialog = new ProductDetailDialog(mainFrame, viewMode);
		ProductDetailController productDetailController = new ProductDetailController(
				productDetailDialog, mainFrame, selectedProduct, viewMode, this
		);

		productDetailController.displayUI();
	}

}
