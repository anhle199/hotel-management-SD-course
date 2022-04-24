package controllers.services;

import dao.ServiceDAO;
import db.DBConnectionException;
import db.SingletonDBConnection;
import models.Service;
import utils.DetailDialogModeEnum;
import utils.UtilFunctions;
import views.components.dialogs.ConnectionErrorDialog;
import views.components.panels.ScrollableTablePanel;
import views.components.table_model.NonEditableTableModel;
import views.dialogs.ServiceDetailDialog;
import views.panels.rooms.RoomListPanel;
import views.panels.services.ServiceListPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Vector;

public class ServiceListController implements ActionListener {

	private final ServiceListPanel serviceListPanel;
	private final JFrame mainFrame;
	private final ConnectionErrorDialog connectionErrorDialog;
	private final ServiceDAO daoModel;

	public ServiceListController(ServiceListPanel serviceListPanel, JFrame mainFrame) {
		this.serviceListPanel = serviceListPanel;
		this.mainFrame = mainFrame;
		this.connectionErrorDialog = new ConnectionErrorDialog(mainFrame);
		this.daoModel = new ServiceDAO();

		// Add action listeners
		this.serviceListPanel.getSearchButton().addActionListener(this);
		this.serviceListPanel.getAddButton().addActionListener(this);
		this.serviceListPanel.getRemoveButton().addActionListener(this);
		this.connectionErrorDialog.getReconnectButton().addActionListener(this);

		// Add double-click listener for each row on the table
		serviceListPanel.getScrollableTable().getTable().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent mouseEvent) {
				if (mouseEvent.getClickCount() == 2) {
					doubleClicksInRowAction();
				}
			}
		});
	}

	public void loadServiceListAndReloadTableData(String serviceNameToken) {
		try {
			ArrayList<Service> serviceList;
			if (serviceNameToken.isEmpty()) {
				serviceList = daoModel.getAll();
			} else {
				serviceList = daoModel.searchByServiceName(serviceNameToken);
			}

			addServiceListToTable(serviceList);
		} catch (DBConnectionException e) {
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
			System.out.println("ServiceListController.java - loadServiceListAndReloadTableData - catch - Unavailable connection.");
		}
	}

	public void addServiceListToTable(ArrayList<Service> serviceList) {
		NonEditableTableModel tableModel = (NonEditableTableModel) serviceListPanel.getScrollableTable().getTableModel();
		tableModel.removeAllRows();

		for (int i = 0; i < serviceList.size(); i++) {
			Object[] rowValue = mapServiceInstanceToRowValue(i + 1, serviceList.get(i));
			tableModel.addRow(rowValue);
		}
	}

	private Object[] mapServiceInstanceToRowValue(int no, Service service) {
		return new Object[]{
				no,
				service.getId(),
				service.getName(),
				service.getDescription(),
				service.getPrice(),
				service.getNote(),
		};
	}

	private Service mapRowValueToServiceInstance(Vector<Object> rowValue) {
		return new Service(
				(int) rowValue.get(ServiceListPanel.HIDDE_COLUMN_SERVICE_ID),
				String.valueOf(rowValue.get(2)),
				String.valueOf(rowValue.get(3)),
				(int) rowValue.get(4),
				String.valueOf(rowValue.get(5))
		);
	}

	public void displayUI() {
		loadServiceListAndReloadTableData("");
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == serviceListPanel.getSearchButton()) {
			searchButtonAction();
		} else if (event.getSource() == serviceListPanel.getAddButton()) {
			addButtonAction();
		} else if (event.getSource() == serviceListPanel.getRemoveButton()) {
			removeButtonAction();
		} else if (event.getSource() == connectionErrorDialog.getReconnectButton()) {
			reconnectButtonAction();
		}
	}

	private void searchButtonAction() {
		String searchText = UtilFunctions.removeRedundantWhiteSpace(
				serviceListPanel.getSearchBar().getText()
		);
		loadServiceListAndReloadTableData(searchText);
	}

	private void addButtonAction() {
		DetailDialogModeEnum viewMode = DetailDialogModeEnum.CREATE;
		ServiceDetailDialog serviceDetailDialog = new ServiceDetailDialog(mainFrame, viewMode);
		ServiceDetailController serviceDetailController = new ServiceDetailController(
				serviceDetailDialog, mainFrame, viewMode, this
		);

		serviceDetailController.displayUI();
	}

	private void removeButtonAction() {
		ScrollableTablePanel tablePanel = serviceListPanel.getScrollableTable();
		int selectedRowIndex = tablePanel.getTable().getSelectedRow();

		if (selectedRowIndex == -1) {
			UtilFunctions.showWarningMessage(serviceListPanel, "Remove service", "You must select a row.");
		} else {
			int option = JOptionPane.showConfirmDialog(
					serviceListPanel,
					"Are you sure to remove this service?",
					"Remove service",
					JOptionPane.YES_NO_OPTION
			);

			if (option == JOptionPane.YES_OPTION) {
				int roomId = (int) tablePanel.getTableModel()
						.getValueAt(selectedRowIndex, RoomListPanel.HIDDEN_COLUMN_ROOM_ID);

				try {
					daoModel.delete(roomId);

					UtilFunctions.showInfoMessage(
							serviceListPanel,
							"Remove service",
							"This service is removed successfully."
					);
				} catch (DBConnectionException e) {
					SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
					System.out.println("ServiceListController.java - removeButtonAction - catch - Unavailable connection.");
				}
			}
		}
	}

	private void reconnectButtonAction() {
		connectionErrorDialog.setExitOnCloseButton(false);
		connectionErrorDialog.setVisible(false);

		SingletonDBConnection.getInstance().connect();
		serviceListPanel.getSearchBar().setText("");
		loadServiceListAndReloadTableData("");
	}

	private void doubleClicksInRowAction() {
		JTable table = serviceListPanel.getScrollableTable().getTable();
		NonEditableTableModel tableModel = (NonEditableTableModel) table.getModel();
		Vector<Object> selectedRowValue = tableModel.getRowValue(table.getSelectedRow());
		Service selectedService = mapRowValueToServiceInstance(selectedRowValue);

		DetailDialogModeEnum viewMode = DetailDialogModeEnum.VIEW_ONLY;
		ServiceDetailDialog serviceDetailDialog = new ServiceDetailDialog(mainFrame, viewMode);
		ServiceDetailController serviceDetailController = new ServiceDetailController(
				serviceDetailDialog, mainFrame, selectedService, viewMode, this
		);

		serviceDetailController.displayUI();
	}

}
