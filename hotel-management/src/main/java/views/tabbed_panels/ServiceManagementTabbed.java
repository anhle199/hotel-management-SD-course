package views.tabbed_panels;

import views.panels.services.ServiceInvoiceListPanel;
import views.panels.services.ServiceListPanel;

import javax.swing.*;

public class ServiceManagementTabbed extends JTabbedPane {

    // Constants
    public static final String SERVICE_LIST_PANEL_TITLE = "Services";
    public static final String SERVICE_INVOICE_LIST_PANEL_TITLE = "Service Invoices";

    public static final int SERVICE_LIST_PANEL_INDEX = 0;
    public static final int SERVICE_INVOICE_LIST_PANEL_INDEX = 1;

    // Components
    private final ServiceListPanel serviceListPanel;
    private final ServiceInvoiceListPanel serviceInvoiceListPanel;

    public ServiceManagementTabbed() {
        super();

        // Service List Panel.
        serviceListPanel = new ServiceListPanel();
        addTab(SERVICE_LIST_PANEL_TITLE, serviceListPanel);

        // Service Invoice List Panel.
        serviceInvoiceListPanel = new ServiceInvoiceListPanel();
        addTab(SERVICE_INVOICE_LIST_PANEL_TITLE, serviceInvoiceListPanel);
    }

    public ServiceListPanel getServiceListPanel() {
        return serviceListPanel;
    }

}
