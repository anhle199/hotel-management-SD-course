package controllers.rooms;

import dao.RentalReceiptDAO;
import db.DBConnectionException;
import db.SingletonDBConnection;
import models.RentalReceipt;
import views.components.dialogs.ConnectionErrorDialog;
import views.components.table_model.NonEditableTableModel;
import views.panels.rooms.RentalReceiptListPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class RentalReceiptListController implements ActionListener {

	private final RentalReceiptListPanel rentalReceiptListPanel;
	private final ConnectionErrorDialog connectionErrorDialog;
	private final RentalReceiptDAO daoModel;

	public RentalReceiptListController(RentalReceiptListPanel rentalReceiptListPanel, JFrame mainFrame) {
		this.rentalReceiptListPanel = rentalReceiptListPanel;
		this.connectionErrorDialog = new ConnectionErrorDialog(mainFrame);
		this.daoModel = new RentalReceiptDAO();

		// Add action listener
		this.connectionErrorDialog.getReconnectButton().addActionListener(this);
	}

	public void loadRentalReceiptListAndReloadTableData() {
		try {
			ArrayList<RentalReceipt> rentalReceiptList = daoModel.getAll();
			addRoomListToTable(rentalReceiptList);
		} catch (DBConnectionException e) {
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
			System.out.println("RentalReceiptListController.java - loadRentalReceiptListAndReloadTableData - catch - Unavailable connection.");
		}
	}

	public void addRoomListToTable(ArrayList<RentalReceipt> rentalReceiptList) {
		NonEditableTableModel tableModel = (NonEditableTableModel) rentalReceiptListPanel.getScrollableTable()
																						 .getTableModel();

		tableModel.removeAllRows();
		for (int i = 0; i < rentalReceiptList.size(); i++) {
			Object[] rowValue = mapRentalReceiptInstanceToRowValue(i + 1, rentalReceiptList.get(i));
			tableModel.addRow(rowValue);
		}
	}

	private Object[] mapRentalReceiptInstanceToRowValue(int no, RentalReceipt rentalReceipt) {
		return new Object[]{
				no,
				rentalReceipt.getRoomName(),
				rentalReceipt.calculateRentedDays(),
				rentalReceipt.getPrice(),
				rentalReceipt.getTotalPrice(),
		};
	}

	public void displayUI() {
		loadRentalReceiptListAndReloadTableData();
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == connectionErrorDialog.getReconnectButton()) {
			reconnectButtonAction();
		}
	}

	private void reconnectButtonAction() {
		connectionErrorDialog.setExitOnCloseButton(false);
		connectionErrorDialog.setVisible(false);

		SingletonDBConnection.getInstance().connect();
		loadRentalReceiptListAndReloadTableData();
	}

}
