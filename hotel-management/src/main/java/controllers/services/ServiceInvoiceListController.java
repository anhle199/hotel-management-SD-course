package controllers.services;

import dao.ServiceInvoiceDAO;
import db.DBConnectionException;
import db.SingletonDBConnection;
import models.ServiceInvoice;
import utils.DetailDialogModeEnum;
import utils.UtilFunctions;
import views.components.dialogs.ConnectionErrorDialog;
import views.components.panels.ScrollableTablePanel;
import views.components.table_model.NonEditableTableModel;
import views.dialogs.ServiceInvoiceDetailDialog;
import views.panels.services.ServiceInvoiceListPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Vector;

public class ServiceInvoiceListController implements ActionListener {

	private final ServiceInvoiceListPanel serviceInvoiceListPanel;
	private final JFrame mainFrame;
	private final ConnectionErrorDialog connectionErrorDialog;
	private final ServiceInvoiceDAO daoModel;

	public ServiceInvoiceListController(ServiceInvoiceListPanel serviceInvoiceListPanel, JFrame mainFrame) {
		this.serviceInvoiceListPanel = serviceInvoiceListPanel;
		this.mainFrame = mainFrame;
		this.connectionErrorDialog = new ConnectionErrorDialog(mainFrame);
		this.daoModel = new ServiceInvoiceDAO();

		// Add action listeners
		this.serviceInvoiceListPanel.getAddButton().addActionListener(this);
		this.serviceInvoiceListPanel.getRemoveButton().addActionListener(this);
		this.connectionErrorDialog.getReconnectButton().addActionListener(this);

		// Add double-click listener for each row on the table
		serviceInvoiceListPanel.getScrollableTable().getTable().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent mouseEvent) {
				if (mouseEvent.getClickCount() == 2) {
					doubleClicksInRowAction();
				}
			}
		});
	}

	public void loadServiceInvoiceListAndReloadTableData() {
		try {
			ArrayList<ServiceInvoice> serviceInvoiceList = daoModel.getAll();
			addServiceInvoiceListToTable(serviceInvoiceList);
		} catch (DBConnectionException e) {
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
			System.out.println("ServiceInvoiceListController.java - loadServiceInvoiceListAndReloadTableData - catch - Unavailable connection.");
		}
	}

	public void addServiceInvoiceListToTable(ArrayList<ServiceInvoice> serviceInvoiceList) {
		NonEditableTableModel tableModel = (NonEditableTableModel) serviceInvoiceListPanel.getScrollableTable().getTableModel();
		tableModel.removeAllRows();

		for (int i = 0; i < serviceInvoiceList.size(); i++) {
			Object[] rowValue = mapServiceInvoiceInstanceToRowValue(i + 1, serviceInvoiceList.get(i));
			tableModel.addRow(rowValue);
		}
	}

	private Object[] mapServiceInvoiceInstanceToRowValue(int no, ServiceInvoice serviceInvoice) {
		return new Object[]{
				no,
				serviceInvoice.getId(),
				serviceInvoice.getRoomName(),
				serviceInvoice.getServiceName(),
				serviceInvoice.getPrice(),
				serviceInvoice.getNumberOfCustomers(),
				serviceInvoice.getPrice() * serviceInvoice.getNumberOfCustomers(),
				serviceInvoice.getNote(),
				serviceInvoice.getRoomId(),
				serviceInvoice.getServiceId(),
		};
	}

	private ServiceInvoice mapRowValueToServiceInvoiceInstance(Vector<Object> rowValue) {
		return new ServiceInvoice(
				(int) rowValue.get(ServiceInvoiceListPanel.HIDDEN_COLUMN_SERVICE_INVOICE_ID),
				String.valueOf(rowValue.get(3)),
				(int) rowValue.get(5),
				(int) rowValue.get(4),
				String.valueOf(rowValue.get(7)),
				(int) rowValue.get(ServiceInvoiceListPanel.HIDDEN_COLUMN_ROOM_ID),
				String.valueOf(rowValue.get(2)),
				(int) rowValue.get(ServiceInvoiceListPanel.HIDDEN_COLUMN_SERVICE_ID)
		);
	}

	public void displayUI() {
		loadServiceInvoiceListAndReloadTableData();
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == serviceInvoiceListPanel.getAddButton()) {
			addButtonAction();
		} else if (event.getSource() == serviceInvoiceListPanel.getRemoveButton()) {
			removeButtonAction();
		} else if (event.getSource() == connectionErrorDialog.getReconnectButton()) {
			reconnectButtonAction();
		}
	}

	private void addButtonAction() {
		DetailDialogModeEnum viewMode = DetailDialogModeEnum.CREATE;
		ServiceInvoiceDetailDialog serviceInvoiceDetailDialog = new ServiceInvoiceDetailDialog(mainFrame, viewMode);
		ServiceInvoiceDetailController serviceInvoiceDetailController = new ServiceInvoiceDetailController(
				serviceInvoiceDetailDialog, mainFrame, viewMode, this
		);

		serviceInvoiceDetailController.displayUI();
	}

	private void removeButtonAction() {
		ScrollableTablePanel tablePanel = serviceInvoiceListPanel.getScrollableTable();
		int selectedRowIndex = tablePanel.getTable().getSelectedRow();

		if (selectedRowIndex == -1) {
			UtilFunctions.showWarningMessage(serviceInvoiceListPanel, "Remove Service Invoice", "You must select a row.");
		} else {
			int option = JOptionPane.showConfirmDialog(
					serviceInvoiceListPanel,
					"Are you sure to remove this invoice?",
					"Remove Service Invoice",
					JOptionPane.YES_NO_OPTION
			);

			if (option == JOptionPane.YES_OPTION) {
				int roomId = (int) tablePanel.getTableModel()
						.getValueAt(selectedRowIndex, ServiceInvoiceListPanel.HIDDEN_COLUMN_SERVICE_INVOICE_ID);

				try {
					daoModel.delete(roomId);

					UtilFunctions.showInfoMessage(
							serviceInvoiceListPanel,
							"Remove Service Invoice",
							"This invoice is removed successfully."
					);
					loadServiceInvoiceListAndReloadTableData();
				} catch (DBConnectionException e) {
					SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
					System.out.println("ServiceInvoiceListController.java - removeButtonAction - catch - Unavailable connection.");
				}
			}
		}
	}

	private void reconnectButtonAction() {
		connectionErrorDialog.setExitOnCloseButton(false);
		connectionErrorDialog.setVisible(false);

		SingletonDBConnection.getInstance().connect();
		loadServiceInvoiceListAndReloadTableData();
	}

	private void doubleClicksInRowAction() {
		JTable table = serviceInvoiceListPanel.getScrollableTable().getTable();
		int selectedRow = table.getSelectedRow();

		if (selectedRow >= 0 && selectedRow < table.getRowCount()) {
			NonEditableTableModel tableModel = (NonEditableTableModel) table.getModel();
			Vector<Object> selectedRowValue = tableModel.getRowValue(table.getSelectedRow());
			ServiceInvoice selectedServiceInvoice = mapRowValueToServiceInvoiceInstance(selectedRowValue);

			DetailDialogModeEnum viewMode = DetailDialogModeEnum.VIEW_ONLY;
			ServiceInvoiceDetailDialog serviceInvoiceDetailDialog = new ServiceInvoiceDetailDialog(mainFrame, viewMode);
			ServiceInvoiceDetailController serviceInvoiceDetailController = new ServiceInvoiceDetailController(
					serviceInvoiceDetailDialog, mainFrame, selectedServiceInvoice, viewMode, this
			);

			serviceInvoiceDetailController.displayUI();
		}
	}

}
