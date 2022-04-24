package controllers.services;

import views.tabbed_panels.ServiceManagementTabbed;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ServiceManagementController implements ChangeListener {

	private final ServiceManagementTabbed serviceManagementTabbed;
	private final ServiceListController serviceListController;
	private int currentTabIndex;

	public ServiceManagementController(ServiceManagementTabbed serviceManagementTabbed, JFrame mainFrame) {
		this.serviceManagementTabbed = serviceManagementTabbed;
		this.serviceListController = new ServiceListController(
				serviceManagementTabbed.getServiceListPanel(),
				mainFrame
		);
		this.currentTabIndex = ServiceManagementTabbed.SERVICE_LIST_PANEL_INDEX;

		// Add change listener
		this.serviceManagementTabbed.addChangeListener(this);
	}

	public void displayUI() {
		didSelectTab(currentTabIndex);
		serviceManagementTabbed.setVisible(true);
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		currentTabIndex = serviceManagementTabbed.getSelectedIndex();
		didSelectTab(currentTabIndex);
	}

	private void didSelectTab(int index) {
		switch (index) {
			case ServiceManagementTabbed.SERVICE_LIST_PANEL_INDEX:
				serviceListController.displayUI();
				break;
//			case ServiceManagementTabbed.SERVICE_INVOICE_LIST_PANEL_INDEX:
//				break;
		}
	}

}
