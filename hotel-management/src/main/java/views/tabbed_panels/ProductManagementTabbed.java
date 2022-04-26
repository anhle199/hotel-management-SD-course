package views.tabbed_panels;

import utils.RoleManager;
import views.panels.products.ImportInvoiceListPanel;
import views.panels.products.ProductListPanel;
import views.panels.products.ProductReceiptListPanel;

import javax.swing.*;

public class ProductManagementTabbed extends JTabbedPane {

	// Constants
	public static final String PRODUCT_LIST_PANEL_TITLE = "Product";
	public static final String RECEIPT_LIST_PANEL_TITLE = "Receipts";
	public static final String IMPORT_INVOICE_LIST_PANEL_TITLE = "Import Invoices";

	public static final int PRODUCT_LIST_PANEL_INDEX = 0;
	public static final int RECEIPT_LIST_PANEL_INDEX = 1;
	public static final int IMPORT_INVOICE_LIST_PANEL_INDEX = 2;

	// Components
	private final ProductListPanel productListPanel;
	private final ProductReceiptListPanel productReceiptListPanel;
	private ImportInvoiceListPanel importInvoiceListPanel;

	public ProductManagementTabbed() {
		super();

		// Product List Panel.
		productListPanel = new ProductListPanel();
		addTab(PRODUCT_LIST_PANEL_TITLE, productListPanel);

		// Receipt List Panel.
		productReceiptListPanel = new ProductReceiptListPanel();
		addTab(RECEIPT_LIST_PANEL_TITLE, productReceiptListPanel);

		if (RoleManager.getInstance().isManager()) {
			// Import Invoice List Panel.
			importInvoiceListPanel = new ImportInvoiceListPanel();
			addTab(IMPORT_INVOICE_LIST_PANEL_TITLE, importInvoiceListPanel);
		}
	}

	public ProductListPanel getProductListPanel() {
		return productListPanel;
	}

	public ProductReceiptListPanel getProductReceiptListPanel() {
		return productReceiptListPanel;
	}

	public ImportInvoiceListPanel getImportInvoiceListPanel() {
		return importInvoiceListPanel;
	}

}
