package controllers.services;

import dao.ServiceDAO;
import db.DBConnectionException;
import db.SingletonDBConnection;
import models.Service;
import utils.DetailDialogModeEnum;
import utils.UtilFunctions;
import views.components.dialogs.ConnectionErrorDialog;
import views.dialogs.ServiceDetailDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ServiceDetailController implements ActionListener {

	private final ServiceDetailDialog serviceDetailDialog;
	private final ConnectionErrorDialog connectionErrorDialog;

	private final Service service;
	private final DetailDialogModeEnum viewMode;

	private final ServiceListController serviceListController;

	public ServiceDetailController(
			ServiceDetailDialog dialog,
			JFrame mainFrame,
			DetailDialogModeEnum viewMode,
			ServiceListController serviceListController
	) {
		this(dialog, mainFrame, null, viewMode, serviceListController);
	}

	public ServiceDetailController(
			ServiceDetailDialog dialog,
			JFrame mainFrame,
			Service service,
			DetailDialogModeEnum viewMode,
			ServiceListController serviceListController
	) {
		this.serviceDetailDialog = dialog;
		this.connectionErrorDialog = new ConnectionErrorDialog(mainFrame);
		this.service = service;
		this.viewMode = viewMode;
		this.serviceListController = serviceListController;

		// Add action listeners
		this.serviceDetailDialog.getPositiveButton().addActionListener(this);
		this.serviceDetailDialog.getNegativeButton().addActionListener(this);
		this.connectionErrorDialog.getReconnectButton().addActionListener(this);
	}

	public void displayUI() {
		if (service != null) {
			serviceDetailDialog.getServiceNameTextField().setText(service.getName());
			serviceDetailDialog.getDescriptionTextField().setText(service.getDescription());
			serviceDetailDialog.getPriceTextField().setText(String.valueOf(service.getPrice()));
			serviceDetailDialog.getNoteTextArea().setText(service.getNote());
		}

		serviceDetailDialog.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == serviceDetailDialog.getPositiveButton()) {
			positiveButtonAction();
		} else if (event.getSource() == serviceDetailDialog.getNegativeButton()) {
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
			serviceDetailDialog.setVisible(false);
		} else if (viewMode == DetailDialogModeEnum.EDITING) {
			rollbackAllChanges();
		} else if (viewMode == DetailDialogModeEnum.CREATE) {
			serviceDetailDialog.setVisible(false);
		}
	}

	private void reconnectButtonAction() {
		connectionErrorDialog.setExitOnCloseButton(false);
		connectionErrorDialog.setVisible(false);

		SingletonDBConnection.getInstance().connect();
		displayUI();
	}

	private void editButtonAction() {
		serviceDetailDialog.setViewMode(DetailDialogModeEnum.EDITING);
	}

	private void saveButtonAction() {
		try {
			ServiceDAO daoModel = new ServiceDAO();
			Service newService = getServiceInstanceFromInputFields();

			if (service.equals(newService)) {
				UtilFunctions.showInfoMessage(serviceDetailDialog, "Edit Service", "Information does not change.");
			} else if (newService.getName().isEmpty()) {
				UtilFunctions.showErrorMessage(serviceDetailDialog, "Edit Service", "Service name must not be empty.");
			} else if (daoModel.isExistingServiceName(newService.getName())) {
				UtilFunctions.showErrorMessage(serviceDetailDialog, "Edit Service", "This service name is existing.");
			} else {
				int option = UtilFunctions.showConfirmDialog(
						serviceDetailDialog,
						"Edit Service",
						"Are you sure to save new information for this service?"
				);

				if (option == JOptionPane.YES_OPTION) {
					daoModel.update(service);
					UtilFunctions.showInfoMessage(serviceDetailDialog, "Edit Service", "Save successfully.");
				}
			}
		} catch (DBConnectionException e) {
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
			System.out.println("ServiceDetailController.java - saveButtonAction - catch - Unavailable connection.");
		}
	}

	private void createButtonAction() {
		try {
			ServiceDAO daoModel = new ServiceDAO();
			Service newService = getServiceInstanceFromInputFields();

			if (newService.getName().isEmpty()) {
				UtilFunctions.showErrorMessage(serviceDetailDialog, "Create Service", "Service name must not be empty.");
			} else if (daoModel.isExistingServiceName(newService.getName())) {
				UtilFunctions.showErrorMessage(serviceDetailDialog, "Create Service", "This service name is existing.");
			} else {
				int option = UtilFunctions.showConfirmDialog(
						serviceDetailDialog,
						"Create Service",
						"Are you sure to create this service?"
				);

				if (option == JOptionPane.YES_OPTION) {
					daoModel.insert(service);
					UtilFunctions.showInfoMessage(serviceDetailDialog, "Create Room", "Create successfully.");

					serviceDetailDialog.setVisible(false);
					serviceListController.loadServiceListAndReloadTableData("");
				}
			}
		} catch (DBConnectionException e) {
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
			System.out.println("ServiceDetailController.java - createButtonAction - catch - Unavailable connection.");
		}
	}

	private void rollbackAllChanges() {
		serviceDetailDialog.getServiceNameTextField().setText(service.getName());
		serviceDetailDialog.getDescriptionTextField().setText(service.getDescription());
		serviceDetailDialog.getPriceTextField().setText(String.valueOf(service.getPrice()));
		serviceDetailDialog.getNoteTextArea().setText(service.getDescription());

		serviceDetailDialog.setViewMode(DetailDialogModeEnum.VIEW_ONLY);
	}

	private Service getServiceInstanceFromInputFields() {
		int serviceId = service != null ? service.getId() : -1;
		String serviceName = UtilFunctions.removeRedundantWhiteSpace(
				serviceDetailDialog.getServiceNameTextField().getText()
		);
		String description = UtilFunctions.removeRedundantWhiteSpace(
				serviceDetailDialog.getDescriptionTextField().getText()
		);
		int price = Integer.parseInt(serviceDetailDialog.getPriceTextField().getText());
		String note = UtilFunctions.removeRedundantWhiteSpace(
				serviceDetailDialog.getNoteTextArea().getText()
		);

		return new Service(serviceId, serviceName, description, price, note);
	}

}
