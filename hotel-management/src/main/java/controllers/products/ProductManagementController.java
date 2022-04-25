package controllers.products;

import views.tabbed_panels.ProductManagementTabbed;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ProductManagementController implements ChangeListener {

	private final ProductManagementTabbed productManagementTabbed;
	private final ProductListController productListController;

	private int currentTabIndex;

	public ProductManagementController(ProductManagementTabbed productManagementTabbed, JFrame mainFrame) {
		this.productManagementTabbed = productManagementTabbed;
		this.productListController = new ProductListController(
				productManagementTabbed.getProductListPanel(),
				mainFrame
		);
		this.currentTabIndex = ProductManagementTabbed.PRODUCT_LIST_PANEL_INDEX;

		// Add change listener
		this.productManagementTabbed.addChangeListener(this);
	}

	public void displayUI() {
		didSelectTab(currentTabIndex);
		productManagementTabbed.setVisible(true);
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		currentTabIndex = productManagementTabbed.getSelectedIndex();
		didSelectTab(currentTabIndex);
	}

	private void didSelectTab(int index) {
		switch (index) {
			case ProductManagementTabbed.PRODUCT_LIST_PANEL_INDEX:
				productListController.displayUI();
				break;
//			case ProductManagementTabbed.RECEIPT_LIST_PANEL_INDEX:
//				break;
//			case ProductManagementTabbed.IMPORT_INVOICE_LIST_PANEL_INDEX:
//				break;
		}
	}

}
