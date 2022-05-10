package controllers.services;

import dao.RoomDAO;
import dao.ServiceDAO;
import dao.ServiceInvoiceDAO;
import db.DBConnectionException;
import db.SingletonDBConnection;
import models.Room;
import models.Service;
import models.ServiceInvoice;
import utils.Constants;
import utils.DetailDialogModeEnum;
import utils.UtilFunctions;
import views.components.dialogs.ConnectionErrorDialog;
import views.dialogs.ServiceInvoiceDetailDialog;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Vector;

public class ServiceInvoiceDetailController implements ActionListener, ItemListener, DocumentListener {

	private final ServiceInvoiceDetailDialog serviceInvoiceDetailDialog;
	private final ConnectionErrorDialog connectionErrorDialog;

	private final ServiceInvoice serviceInvoice;
	private DetailDialogModeEnum viewMode;
	private ArrayList<Room> roomList;
	private ArrayList<Service> serviceList;

	private final ServiceInvoiceListController serviceInvoiceListController;

	public ServiceInvoiceDetailController(
			ServiceInvoiceDetailDialog dialog,
			JFrame mainFrame,
			DetailDialogModeEnum viewMode,
			ServiceInvoiceListController serviceInvoiceListController
	) {
		this(dialog, mainFrame, null, viewMode, serviceInvoiceListController);
	}

	public ServiceInvoiceDetailController(
			ServiceInvoiceDetailDialog dialog,
			JFrame mainFrame,
			ServiceInvoice serviceInvoice,
			DetailDialogModeEnum viewMode,
			ServiceInvoiceListController serviceInvoiceListController
	) {
		this.serviceInvoiceDetailDialog = dialog;
		this.connectionErrorDialog = new ConnectionErrorDialog(mainFrame);
		this.serviceInvoice = serviceInvoice;
		this.viewMode = viewMode;
		this.serviceInvoiceListController = serviceInvoiceListController;

		// Add action listeners
		this.serviceInvoiceDetailDialog.getPositiveButton().addActionListener(this);
		this.serviceInvoiceDetailDialog.getNegativeButton().addActionListener(this);
		this.connectionErrorDialog.getReconnectButton().addActionListener(this);

		// Add item change listener
		this.serviceInvoiceDetailDialog.getServiceNameComboBox().addItemListener(this);

		// Add document listener
		this.serviceInvoiceDetailDialog.getNumberOfCustomersTextField().getDocument().addDocumentListener(this);
	}

	public void displayUI() {
		if (viewMode == DetailDialogModeEnum.CREATE) {
			loadRoomList();
		}

		if (serviceInvoice != null) {
			int totalPrice = serviceInvoice.getPrice() * serviceInvoice.getNumberOfCustomers();

			serviceInvoiceDetailDialog.getRoomNameComboBox()
									  .addItem(serviceInvoice.getRoomName());
			serviceInvoiceDetailDialog.getPriceTextField()
									  .setValue(serviceInvoice.getPrice());
			serviceInvoiceDetailDialog.getNumberOfCustomersTextField()
									  .setValue(serviceInvoice.getNumberOfCustomers());
			serviceInvoiceDetailDialog.getTotalPriceTextField()
									  .setValue(totalPrice);
			serviceInvoiceDetailDialog.getNoteTextArea()
									  .setText(serviceInvoice.getNote());
		}

		loadServiceList();

		if (serviceInvoice == null) {
			Service service = serviceList.get(0);
			int numberOfCustomers = Constants.MIN_CUSTOMERS;

			serviceInvoiceDetailDialog.getPriceTextField()
									  .setValue(service.getPrice());
			serviceInvoiceDetailDialog.getNumberOfCustomersTextField()
									  .setValue(numberOfCustomers);
			serviceInvoiceDetailDialog.getTotalPriceTextField()
									  .setValue(numberOfCustomers * service.getPrice());
		}

		serviceInvoiceDetailDialog.setVisible(true);
	}

	private void loadServiceList() {
		try {
			ServiceDAO daoModel = new ServiceDAO();
			serviceList = daoModel.getAll();

			boolean isExistCurrentServiceName = serviceInvoice == null;
			Vector<String> serviceNameList = new Vector<>();
			for (Service item: serviceList) {
				serviceNameList.add(item.getName());

				if (isExistCurrentServiceName || item.getName().equals(serviceInvoice.getServiceName()))
					isExistCurrentServiceName = true;
			}

			if (isExistCurrentServiceName) {
				serviceInvoiceDetailDialog.getServiceNameComboBox()
										  .setModel(new DefaultComboBoxModel<>(serviceNameList));
			}

			if (serviceInvoice != null) {
				String serviceName = serviceInvoice.getServiceName();

				if (isExistCurrentServiceName)
					serviceInvoiceDetailDialog.getServiceNameComboBox().setSelectedItem(serviceName);
				else {
					serviceInvoiceDetailDialog.getServiceNameComboBox().addItem(serviceName);
					serviceInvoiceDetailDialog.getPositiveButton().setEnabled(false);
				}
			}
		} catch (DBConnectionException e) {
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
			System.out.println("ServiceInvoiceDetailController.java - loadServiceList - catch - Unavailable connection.");
		}
	}

	private void loadRoomList() {
		try {
			RoomDAO daoModel = new RoomDAO();
			roomList = daoModel.getAllByStatus(Room.RoomStatusEnum.RESERVED);

			Vector<String> roomNameList = new Vector<>();
			for (Room item: roomList) {
				roomNameList.add(item.getName());
			}

			serviceInvoiceDetailDialog.getRoomNameComboBox().setModel(new DefaultComboBoxModel<>(roomNameList));
		} catch (DBConnectionException e) {
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
			System.out.println("ServiceInvoiceDetailController.java - loadRoomList - catch - Unavailable connection.");
		}
	}

	private void setViewMode(DetailDialogModeEnum mode) {
		serviceInvoiceDetailDialog.setViewMode(mode);
		viewMode = mode;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == serviceInvoiceDetailDialog.getPositiveButton()) {
			positiveButtonAction();
		} else if (event.getSource() == serviceInvoiceDetailDialog.getNegativeButton()) {
			negativeButtonAction();
		} else if (event.getSource() == connectionErrorDialog.getReconnectButton()) {
			reconnectButtonAction();
		}
	}

	@Override
	public void itemStateChanged(ItemEvent event) {
		if (event.getSource() == serviceInvoiceDetailDialog.getServiceNameComboBox()) {
			serviceNameComboBoxAction();
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
			serviceInvoiceDetailDialog.setVisible(false);
		} else if (viewMode == DetailDialogModeEnum.EDITING) {
			rollbackAllChanges();
		} else if (viewMode == DetailDialogModeEnum.CREATE) {
			serviceInvoiceDetailDialog.setVisible(false);
		}
	}

	private void reconnectButtonAction() {
		connectionErrorDialog.setExitOnCloseButton(false);
		connectionErrorDialog.setVisible(false);

		SingletonDBConnection.getInstance().connect();
		displayUI();
	}

	private void editButtonAction() {
		setViewMode(DetailDialogModeEnum.EDITING);
		serviceInvoiceDetailDialog.getPriceTextField().setEnabled(false);
	}

	private void saveButtonAction() {
		try {
			ServiceInvoiceDAO daoModel = new ServiceInvoiceDAO();
			ServiceInvoice newServiceInvoice = getServiceInvoiceInstanceFromInputFields();

			if (serviceInvoice.equals(newServiceInvoice)) {
				UtilFunctions.showInfoMessage(
						serviceInvoiceDetailDialog,
						"Edit Service Invoice",
						"Information does not change."
				);
			} else {
				int option = UtilFunctions.showConfirmDialog(
						serviceInvoiceDetailDialog,
						"Edit Service Invoice",
						"Are you sure to save new information for this invoice?"
				);

				if (option == JOptionPane.YES_OPTION) {
					daoModel.update(newServiceInvoice);
					UtilFunctions.showInfoMessage(serviceInvoiceDetailDialog, "Edit Service Invoice", "Save successfully.");

					this.serviceInvoice.copyFrom(newServiceInvoice);
					setViewMode(DetailDialogModeEnum.VIEW_ONLY);
					serviceInvoiceListController.loadServiceInvoiceListAndReloadTableData();
				}
			}
		} catch (DBConnectionException e) {
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
			System.out.println("ServiceInvoiceDetailController.java - saveButtonAction - catch - Unavailable connection.");
		}
	}

	private void createButtonAction() {
		try {
			int option = UtilFunctions.showConfirmDialog(
					serviceInvoiceDetailDialog,
					"Create Service",
					"Are you sure to create this service?"
			);

			if (option == JOptionPane.YES_OPTION) {
				ServiceInvoiceDAO daoModel = new ServiceInvoiceDAO();
				daoModel.insert(getServiceInvoiceInstanceFromInputFields());

				UtilFunctions.showInfoMessage(
						serviceInvoiceDetailDialog,
						"Create Service Invoice",
						"Create successfully."
				);

				serviceInvoiceDetailDialog.setVisible(false);
				serviceInvoiceListController.loadServiceInvoiceListAndReloadTableData();
			}
		} catch (DBConnectionException e) {
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
			System.out.println("ServiceInvoiceDetailController.java - createButtonAction - catch - Unavailable connection.");
		}
	}

	private void rollbackAllChanges() {
		serviceInvoiceDetailDialog.getRoomNameComboBox().setSelectedItem(serviceInvoice.getRoomName());
		serviceInvoiceDetailDialog.getServiceNameComboBox().setSelectedItem(serviceInvoice.getRoomName());
		serviceInvoiceDetailDialog.getPriceTextField().setText(
				String.valueOf(serviceInvoice.getPrice() / serviceInvoice.getNumberOfCustomers())
		);
		serviceInvoiceDetailDialog.getNumberOfCustomersTextField().setText(String.valueOf(serviceInvoice.getNumberOfCustomers()));
		serviceInvoiceDetailDialog.getTotalPriceTextField().setText(String.valueOf(serviceInvoice.getPrice()));
		serviceInvoiceDetailDialog.getNoteTextArea().setText(serviceInvoice.getNote());

		setViewMode(DetailDialogModeEnum.VIEW_ONLY);
	}

	private void serviceNameComboBoxAction() {
		String serviceName = String.valueOf(serviceInvoiceDetailDialog.getServiceNameComboBox().getSelectedItem());
		Service service = getSelectedService(serviceName);
		int numberOfCustomers = Integer.parseInt(serviceInvoiceDetailDialog.getNumberOfCustomersTextField().getText());

		serviceInvoiceDetailDialog.getPriceTextField()
								  .setText(String.valueOf(service.getPrice()));
		serviceInvoiceDetailDialog.getTotalPriceTextField()
								  .setText(String.valueOf(numberOfCustomers * service.getPrice()));
	}

	private Service getSelectedService(String serviceName) {
		for (Service item: serviceList)
			if (item.getName().equals(serviceName))
				return item;

		return serviceList.get(0);
	}

	private ServiceInvoice getServiceInvoiceInstanceFromInputFields() {
		int serviceInvoiceId = serviceInvoice != null ? serviceInvoice.getId() : -1;
		String serviceName = UtilFunctions.removeRedundantWhiteSpace(
				String.valueOf(serviceInvoiceDetailDialog.getServiceNameComboBox().getSelectedItem())
		);
		int numberOfCustomers = Integer.parseInt(serviceInvoiceDetailDialog.getNumberOfCustomersTextField().getText());
		int totalPrice = Integer.parseInt(serviceInvoiceDetailDialog.getTotalPriceTextField().getText());
		String note = UtilFunctions.removeRedundantWhiteSpace(
				String.valueOf(serviceInvoiceDetailDialog.getNoteTextArea().getText())
		);
		int roomId = serviceInvoice != null ? serviceInvoice.getRoomId() : -1;
		String roomName = UtilFunctions.removeRedundantWhiteSpace(
				String.valueOf(serviceInvoiceDetailDialog.getRoomNameComboBox().getSelectedItem())
		);
		int serviceId = serviceInvoice != null ? serviceInvoice.getServiceId() : -1;

		return new ServiceInvoice(
				serviceInvoiceId, serviceName,
				numberOfCustomers, totalPrice, note,
				roomId, roomName, serviceId
		);
	}


	// MARK: Document Listener methods

	@Override
	public void insertUpdate(DocumentEvent e) {
		int price = Integer.parseInt(String.valueOf(serviceInvoiceDetailDialog.getPriceTextField().getText()));
		int numberOfCustomers = Integer.parseInt(String.valueOf(serviceInvoiceDetailDialog.getNumberOfCustomersTextField().getText()));
		serviceInvoiceDetailDialog.getTotalPriceTextField().setValue(price * numberOfCustomers);
		System.out.println("(price: " + price + ", numberOfCustomers: " + numberOfCustomers + ")");
	}

	@Override
	public void removeUpdate(DocumentEvent e) {

	}

	@Override
	public void changedUpdate(DocumentEvent event) {

	}

}
