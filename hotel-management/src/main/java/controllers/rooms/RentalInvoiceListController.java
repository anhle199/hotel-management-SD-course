package controllers.rooms;

import dao.RentalInvoiceDAO;
import dao.RentalReceiptDAO;
import db.DBConnectionException;
import db.SingletonDBConnection;
import models.RentalInvoice;
import models.RentalReceipt;
import models.Room;
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
import java.time.LocalDateTime;
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
		this.rentalInvoiceListPanel.getPayButton().addActionListener(this);
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
				UtilFunctions.formatTimestamp(Constants.DATE_PATTERN, rentalInvoice.getStartDate()),
				UtilFunctions.formatTimestamp(Constants.DATE_PATTERN, rentalInvoice.getEndDate()),
				RentalInvoice.PaymentStatusEnum.valueOf(rentalInvoice.getIsPaid()).capitalizedName(),
				rentalInvoice.getCustomerName(),
				capitalizedCustomerType,
				rentalInvoice.getIdentityNumber(),
				rentalInvoice.getAddress(),
				rentalInvoice.getRoomTypeId(),
				rentalInvoice.getRoomTypeName(),
				rentalInvoice.getRoomTypePrice(),
		};
	}

	private RentalInvoice mapRowValueToRentalInvoiceInstance(Vector<Object> rowValue) {
		RentalInvoice.CustomerTypeEnum customerType = RentalInvoice.CustomerTypeEnum.valueOfIgnoreCase(
				String.valueOf(rowValue.get(8))
		);
		RentalInvoice.PaymentStatusEnum paymentStatus = RentalInvoice.PaymentStatusEnum.valueOfIgnoreCase(
				String.valueOf(rowValue.get(6))
		);

		String startDateInString = rowValue.get(4) + " " + Constants.START_TIME_STRING_VALUE;
		String endDateInString = rowValue.get(5) + " " + Constants.END_TIME_STRING_VALUE;

		return new RentalInvoice(
				(int) rowValue.get(RentalInvoiceListPanel.HIDDEN_COLUMN_RENTAL_INVOICE_ID),
				Timestamp.valueOf(startDateInString),
				Timestamp.valueOf(endDateInString),
				(int) rowValue.get(RentalInvoiceListPanel.HIDDEN_COLUMN_ROOM_ID),
				String.valueOf(rowValue.get(3)),
				(int) rowValue.get(RentalInvoiceListPanel.HIDDEN_COLUMN_ROOM_TYPE_ID),
				String.valueOf(rowValue.get(RentalInvoiceListPanel.HIDDEN_COLUMN_ROOM_TYPE_NAME)),
				(int) rowValue.get(RentalInvoiceListPanel.HIDDEN_COLUMN_ROOM_TYPE_PRICE),
				String.valueOf(rowValue.get(7)),
				String.valueOf(rowValue.get(9)),
				String.valueOf(rowValue.get(10)),
				customerType.ordinal(),
				paymentStatus.byteValue()
		);
	}

	public void displayUI() {
		loadRentalInvoiceListAndReloadTableData();
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == rentalInvoiceListPanel.getPayButton()) {
			payButtonAction();
		} else if (event.getSource() == rentalInvoiceListPanel.getAddButton()) {
			addButtonAction();
		} else if (event.getSource() == rentalInvoiceListPanel.getRemoveButton()) {
			removeButtonAction();
		} else if (event.getSource() == connectionErrorDialog.getReconnectButton()) {
			reconnectButtonAction();
		}
	}

	private void payButtonAction() {
		ScrollableTablePanel tablePanel = rentalInvoiceListPanel.getScrollableTable();
		int selectedRowIndex = tablePanel.getTable().getSelectedRow();

		if (selectedRowIndex == -1) {
			UtilFunctions.showWarningMessage(
					rentalInvoiceListPanel,
					"Pay Rental Invoice",
					"You must select a row."
			);
		} else {
			NonEditableTableModel tableModel = (NonEditableTableModel) tablePanel.getTableModel();
			RentalInvoice.PaymentStatusEnum paymentStatus = RentalInvoice.PaymentStatusEnum.valueOfIgnoreCase(
					String.valueOf(tableModel.getValueAt(selectedRowIndex, 6))
			);
			Timestamp endDate = Timestamp.valueOf(
					tableModel.getValueAt(selectedRowIndex, 5) + " " + Constants.END_TIME_STRING_VALUE
			);


			if (paymentStatus == RentalInvoice.PaymentStatusEnum.PAID) {
				UtilFunctions.showWarningMessage(
						rentalInvoiceListPanel,
						"Pay Rental Invoice",
						"This invoice has been paid."
				);
			} else if (!UtilFunctions.isInToday(endDate)) {
				UtilFunctions.showWarningMessage(
						rentalInvoiceListPanel,
						"Pay Rental Invoice",
						"You can only pay the room rental charges on the last day."
				);
			} else {
				int option = JOptionPane.showConfirmDialog(
						rentalInvoiceListPanel,
						"Are you sure to pay for this invoice?",
						"Pay Rental Invoice",
						JOptionPane.YES_NO_OPTION
				);

				if (option == JOptionPane.YES_OPTION) {
					try {
						RentalReceiptDAO rentalReceiptDaoModel = new RentalReceiptDAO();
						RentalReceipt rentalReceipt = getRentalReceiptInstanceFromSpecificRow(selectedRowIndex);
						int rentalInvoiceId = (int) tableModel.getValueAt(selectedRowIndex, RentalInvoiceListPanel.HIDDEN_COLUMN_RENTAL_INVOICE_ID);
						rentalReceiptDaoModel.insert(rentalReceipt, rentalInvoiceId);

						UtilFunctions.showInfoMessage(
								rentalInvoiceListPanel,
								"Pay Rental Invoice",
								"Pay successfully."
						);
						loadRentalInvoiceListAndReloadTableData();
					} catch (DBConnectionException e) {
						SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
						System.out.println("RentalInvoiceListController.java - payButtonAction - catch - Unavailable connection.");
					}
				}
			}
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
			UtilFunctions.showWarningMessage(
					rentalInvoiceListPanel,
					"Remove Rental Invoice",
					"You must select a row."
			);
		} else {
			int option = JOptionPane.showConfirmDialog(
					rentalInvoiceListPanel,
					"Are you sure to remove this invoice?",
					"Remove Rental Invoice",
					JOptionPane.YES_NO_OPTION
			);

			if (option == JOptionPane.YES_OPTION) {
				NonEditableTableModel tableModel = (NonEditableTableModel) tablePanel.getTableModel();
				Vector<Object> rowValue = tableModel.getRowValue(selectedRowIndex);
				int rentalInvoiceId = (int) rowValue.get(RentalInvoiceListPanel.HIDDEN_COLUMN_RENTAL_INVOICE_ID);
				int roomId = (int) rowValue.get(RentalInvoiceListPanel.HIDDEN_COLUMN_ROOM_ID);

				Timestamp endDate = Timestamp.valueOf(rowValue.get(5) + " " + Constants.END_TIME_STRING_VALUE);
				RentalInvoice.PaymentStatusEnum paymentStatus = RentalInvoice.PaymentStatusEnum.valueOfIgnoreCase(
						String.valueOf(rowValue.get(6))
				);

				byte newRoomStatus = -1;
				Timestamp now = UtilFunctions.getTimestamp(LocalDateTime.now());
				if (now.before(endDate) && paymentStatus == RentalInvoice.PaymentStatusEnum.NOT_PAID) {
					newRoomStatus = Room.RoomStatusEnum.AVAILABLE.byteValue();
				}

				try {
					daoModel.delete(rentalInvoiceId, roomId, newRoomStatus);

					UtilFunctions.showInfoMessage(
							rentalInvoiceListPanel,
							"Remove Rental Invoice",
							"This invoice is removed successfully."
					);
					loadRentalInvoiceListAndReloadTableData();
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
		int selectedRow = table.getSelectedRow();

		if (selectedRow >= 0 && selectedRow < table.getRowCount()) {
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

	private RentalReceipt getRentalReceiptInstanceFromSpecificRow(int row) {
		NonEditableTableModel tableModel = (NonEditableTableModel) rentalInvoiceListPanel.getScrollableTable().getTableModel();
		Vector<Object> rowValue = tableModel.getRowValue(row);

		String startDateInString = rowValue.get(4) + " " + Constants.START_TIME_STRING_VALUE;
		String endDateInString = rowValue.get(5) + " " + Constants.END_TIME_STRING_VALUE;
		Timestamp startDate = Timestamp.valueOf(startDateInString);
		Timestamp endDate = Timestamp.valueOf(endDateInString);

		int price = (int) rowValue.get(RentalInvoiceListPanel.HIDDEN_COLUMN_ROOM_TYPE_PRICE);
		int diffInDate = (int) (((endDate.getTime() - startDate.getTime()) / Constants.ONE_DAY_IN_MILLISECONDS) % 365);
		diffInDate += (diffInDate == 0) ? 1 : 0;
		int totalPrice = price * diffInDate;

		return new RentalReceipt(
				-1,
				startDate,
				endDate,
				price,
				totalPrice,
				String.valueOf(rowValue.get(3)),
				String.valueOf(rowValue.get(RentalInvoiceListPanel.HIDDEN_COLUMN_ROOM_TYPE_NAME)),
				(int) rowValue.get(RentalInvoiceListPanel.HIDDEN_COLUMN_ROOM_ID)
		);
	}

}
