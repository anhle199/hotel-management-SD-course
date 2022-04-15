package views.tabbed_panels;

import views.panels.services.ServiceInvoiceListPanel;
import views.panels.services.ServiceListPanel;

import javax.swing.*;

public class ServiceManagementTabbed extends JTabbedPane {

    // Constants
    public static final String SERVICE_LIST_PANEL = "Services";
    public static final String SERVICE_INVOICE_LIST_PANEL = "Service Invoices";

    // Components
    final private ServiceListPanel serviceListPanel;
    final private ServiceInvoiceListPanel serviceInvoiceListPanel;

    public ServiceManagementTabbed() {
        super();

        // Service List Panel.
        serviceListPanel = new ServiceListPanel();
        addTab(SERVICE_LIST_PANEL, serviceListPanel);

        // Service Invoice List Panel.
        serviceInvoiceListPanel = new ServiceInvoiceListPanel();
        addTab(SERVICE_INVOICE_LIST_PANEL, serviceInvoiceListPanel);
    }

}
