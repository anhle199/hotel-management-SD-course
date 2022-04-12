package views.tabbed_panels;

import views.panels.ServiceInvoiceListPanel;
import views.panels.ServiceListPanel;

import javax.swing.*;

public class ServiceManagementTabbed extends JTabbedPane {

    // Constants
    public static final String SERVICE_LIST_PANEL = "Service";
    public static final String SERVICE_INVOICE_LIST_PANEL = "Service Invoices";
    public static final int SERVICE_LIST_INDEX = 0;
    public static final int SERVICE_INVOICE_LIST_INDEX = 1;

    // Components
    final private ServiceListPanel serviceListPanel;
    final private ServiceInvoiceListPanel serviceInvoiceListPanel;

    public ServiceManagementTabbed() {
        super();

        // Necessaries list panel
        serviceListPanel = new ServiceListPanel();
        addTab(SERVICE_LIST_PANEL, serviceListPanel);

        // Cart panel
        serviceInvoiceListPanel = new ServiceInvoiceListPanel();
        addTab(SERVICE_INVOICE_LIST_PANEL, serviceInvoiceListPanel);
    }

}