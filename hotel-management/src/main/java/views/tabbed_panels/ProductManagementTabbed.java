package views.tabbed_panels;

import utils.RoleManager;
import views.panels.products.ImportInvoiceListPanel;
import views.panels.products.ReceiptListPanel;
import views.panels.products.ProductListPanel;

import javax.swing.*;

public class ProductManagementTabbed extends JTabbedPane {

	// Constants
	public static final String PRODUCT_LIST_PANEL = "Product";
	public static final String RECEIPT_LIST_PANEL = "Receipts";
	public static final String IMPORT_INVOICE_LIST_PANEL = "Import Invoices";

	// Components
	private ProductListPanel productListPanel;
	private ReceiptListPanel receiptListPanel;
	private ImportInvoiceListPanel importInvoiceListPanel;

	public ProductManagementTabbed() {
		super();

		// Product List Panel.
		productListPanel = new ProductListPanel();
		addTab(PRODUCT_LIST_PANEL, productListPanel);

		// Receipt List Panel.
		receiptListPanel = new ReceiptListPanel();
		addTab(RECEIPT_LIST_PANEL, receiptListPanel);

		if (RoleManager.getInstance().isManager()) {
			// Import Invoice List Panel.
			importInvoiceListPanel = new ImportInvoiceListPanel();
			addTab(IMPORT_INVOICE_LIST_PANEL, importInvoiceListPanel);
		}
	}

}
