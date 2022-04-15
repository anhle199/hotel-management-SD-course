package views.tabbed_panels;

import views.panels.ReceiptsListPanel;
import views.panels.ProductListPanel;

import javax.swing.*;

public class ProductManagementTabbed extends JTabbedPane {

   // Constants
   public static final String PRODUCT_LIST_PANEL = "Product";
   public static final String PRODUCT_INVOICE_LIST_PANEL = "Receipts";
   public static final int PRODUCT_LIST_INDEX = 0;
   public static final int PRODUCT_INVOICE_LIST_INDEX = 1;

   // Components
   final private ProductListPanel productListPanel;
   final private ReceiptsListPanel receiptListPanel;

   public ProductManagementTabbed() {
       super();

       // Necessaries list panel
       productListPanel = new ProductListPanel();
       addTab(PRODUCT_LIST_PANEL, productListPanel);

       // Cart panel
        receiptListPanel = new ReceiptsListPanel();
        addTab(PRODUCT_INVOICE_LIST_PANEL, receiptListPanel);
   }

}