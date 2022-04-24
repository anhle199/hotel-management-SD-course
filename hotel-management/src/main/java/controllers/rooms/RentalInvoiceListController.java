package controllers.rooms;

import dao.RentalInvoiceDAO;
import db.DBConnectionException;
import db.SingletonDBConnection;
import models.RentalInvoice;
import utils.Constants;
import utils.DetailDialogModeEnum;
import utils.UtilFunctions;
import views.components.dialogs.ConnectionErrorDialog;
import views.components.panels.ScrollableTablePanel;
import views.components.table_model.NonEditableTableModel;
import views.dialogs.RentalInvoiceDetailDialog;
import views.panels.rooms.RentalInvoiceListPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Vector;

public class RentalInvoiceListController implements ActionListener {

	private final RentalInvoiceListPanel rentalInvoiceListPanel;
	private final JFrame mainFrame;
	private final ConnectionErrorDialog connectionErrorDialog;
	private final RentalInvoiceDAO daoModel;

	public RentalInvoiceListController(RentalInvoiceListPanel rentalInvoiceListPanel, JFrame mainFrame) {
		this.rentalInvoiceListPanel = rentalInvoiceListPanel;
		this.mainFrame = mainFrame;
		this.connectionErrorDialog = new ConnectionErrorDialog(mainFrame);
		this.daoModel = new RentalInvoiceDAO();

		// Add action listeners
		this.rentalInvoiceListPanel.getAddButton().addActionListener(this);
		this.rentalInvoiceListPanel.getRemoveButton().addActionListener(this);
		this.connectionErrorDialog.getReconnectButton().addActionListener(this);

		// Add double-click listener for each row on the table
		rentalInvoiceListPanel.getScrollableTable().getTable().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent mouseEvent) {
				if (mouseEvent.getClickCount() == 2) {
					doubleClicksInRowAction();
				}
			}
		});
	}

	public void loadRentalInvoiceListAndReloadTableData() {
		try {
			ArrayList<RentalInvoice> rentalInvoiceList = daoModel.getAll();
			addRoomListToTable(rentalInvoiceList);
		} catch (DBConnectionException e) {
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
			System.out.println("RentalInvoiceListController.java - loadRentalInvoiceListAndReloadTableData - catch - Unavailable connection.");
		}
	}

	public void addRoomListToTable(ArrayList<RentalInvoice> rentalInvoiceList) {
		NonEditableTableModel tableModel = (NonEditableTableModel) rentalInvoiceListPanel.getScrollableTable()
																						 .getTableModel();

		tableModel.removeAllRows();
		for (int i = 0; i < rentalInvoiceList.size(); i++) {
			Object[] rowValue = mapRentalInvoiceInstanceToRowValue(i + 1, rentalInvoiceList.get(i));
			tableModel.addRow(rowValue);
		}
	}

	private Object[] mapRentalInvoiceInstanceToRowValue(int no, RentalInvoice rentalInvoice) {
		RentalInvoice.CustomerTypeEnum customerType = RentalInvoice.CustomerTypeEnum.valueOf(rentalInvoice.getCustomerType());
		String capitalizedCustomerType = UtilFunctions.capitalizeFirstLetterInString(customerType.name());

		return new Object[]{
				no,
				rentalInvoice.getId(),
				rentalInvoice.getRoomId(),
				rentalInvoice.getRoomName(),
				UtilFunctions.formatTimestamp(Constants.TIMESTAMP_WITHOUT_NANOSECOND, rentalInvoice.getStartDate()),
				rentalInvoice.getCustomerName(),
				capitalizedCustomerType,
				rentalInvoice.getIdentityNumber(),
				rentalInvoice.getAddress(),
		};
	}

	private RentalInvoice mapRowValueToRentalInvoiceInstance(Vector<Object> rowValue) {
		RentalInvoice.CustomerTypeEnum customerType = RentalInvoice.CustomerTypeEnum.valueOfIgnoreCase(
				String.valueOf(rowValue.get(6))
		);

		return new RentalInvoice(
				(int) rowValue.get(RentalInvoiceListPanel.HIDDEN_COLUMN_RENTAL_INVOICE_ID),
				Timestamp.valueOf(String.valueOf(rowValue.get(4))),
				(int) rowValue.get(RentalInvoiceListPanel.HIDDEN_COLUMN_ROOM_ID),
				String.valueOf(rowValue.get(3)),
				String.valueOf(rowValue.get(5)),
				String.valueOf(rowValue.get(7)),
				String.valueOf(rowValue.get(8)),
				customerType.ordinal()
		);
	}

	public void displayUI() {
		loadRentalInvoiceListAndReloadTableData();
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == rentalInvoiceListPanel.getAddButton()) {
			addButtonAction();
		} else if (event.getSource() == rentalInvoiceListPanel.getRemoveButton()) {
			removeButtonAction();
		} else if (event.getSource() == connectionErrorDialog.getReconnectButton()) {
			reconnectButtonAction();
		}
	}

	private void addButtonAction() {
		DetailDialogModeEnum viewMode = DetailDialogModeEnum.CREATE;
		RentalInvoiceDetailDialog rentalInvoiceDetailDialog = new RentalInvoiceDetailDialog(mainFrame, viewMode);
		RentalInvoiceDetailController rentalInvoiceDetailController = new RentalInvoiceDetailController(
				rentalInvoiceDetailDialog, mainFrame, viewMode, this
		);

		rentalInvoiceDetailController.displayUI();
	}

	private void removeButtonAction() {
		ScrollableTablePanel tablePanel = rentalInvoiceListPanel.getScrollableTable();
		int selectedRowIndex = tablePanel.getTable().getSelectedRow();

		if (selectedRowIndex == -1) {
			UtilFunctions.showWarningMessage(rentalInvoiceListPanel, "Remove rental invoice", "You must select a row.");
		} else {
			int option = JOptionPane.showConfirmDialog(
					rentalInvoiceListPanel,
					"Are you sure to remove this invoice?",
					"Remove rental invoice",
					JOptionPane.YES_NO_OPTION
			);

			if (option == JOptionPane.YES_OPTION) {
				int rentalInvoiceId = (int) tablePanel.getTableModel()
											 .getValueAt(selectedRowIndex, RentalInvoiceListPanel.HIDDEN_COLUMN_RENTAL_INVOICE_ID);

				try {
					daoModel.delete(rentalInvoiceId);

					UtilFunctions.showInfoMessage(
							rentalInvoiceListPanel,
							"Remove rental invoice",
							"This invoice is removed successfully."
					);
				} catch (DBConnectionException e) {
					SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
					System.out.println("RentalInvoiceListController.java - removeButtonAction - catch - Unavailable connection.");
				}
			}
		}
	}

	private void reconnectButtonAction() {
		connectionErrorDialog.setExitOnCloseButton(false);
		connectionErrorDialog.setVisible(false);

		SingletonDBConnection.getInstance().connect();
		loadRentalInvoiceListAndReloadTableData();
	}

	private void doubleClicksInRowAction() {
		JTable table = rentalInvoiceListPanel.getScrollableTable().getTable();
		NonEditableTableModel tableModel = (NonEditableTableModel) table.getModel();
		Vector<Object> selectedRowValue = tableModel.getRowValue(table.getSelectedRow());
		RentalInvoice selectedRentalInvoice = mapRowValueToRentalInvoiceInstance(selectedRowValue);

		DetailDialogModeEnum viewMode = DetailDialogModeEnum.VIEW_ONLY;
		RentalInvoiceDetailDialog rentalInvoiceDetailDialog = new RentalInvoiceDetailDialog(mainFrame, viewMode);
		RentalInvoiceDetailController rentalInvoiceDetailController = new RentalInvoiceDetailController(
				rentalInvoiceDetailDialog, mainFrame, selectedRentalInvoice, viewMode, this
		);

		rentalInvoiceDetailController.displayUI();
	}

}
