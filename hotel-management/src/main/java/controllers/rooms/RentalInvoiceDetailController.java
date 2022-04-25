package controllers.rooms;

import dao.RentalInvoiceDAO;
import dao.RoomDAO;
import db.DBConnectionException;
import db.SingletonDBConnection;
import models.RentalInvoice;
import models.Room;
import utils.DetailDialogModeEnum;
import utils.UtilFunctions;
import views.components.dialogs.ConnectionErrorDialog;
import views.dialogs.RentalInvoiceDetailDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Vector;

public class RentalInvoiceDetailController implements ActionListener {

	private final RentalInvoiceDetailDialog rentalInvoiceDetailDialog;
	private final ConnectionErrorDialog connectionErrorDialog;

	private final RentalInvoice rentalInvoice;
	private final DetailDialogModeEnum viewMode;
	private ArrayList<Room> roomList;

	private final RentalInvoiceListController rentalInvoiceListController;

	public RentalInvoiceDetailController(
			RentalInvoiceDetailDialog dialog,
			JFrame mainFrame,
			DetailDialogModeEnum viewMode,
			RentalInvoiceListController rentalInvoiceListController
	) {
		this(dialog, mainFrame, null, viewMode, rentalInvoiceListController);
	}

	public RentalInvoiceDetailController(
			RentalInvoiceDetailDialog dialog,
			JFrame mainFrame,
			RentalInvoice rentalInvoice,
			DetailDialogModeEnum viewMode,
			RentalInvoiceListController rentalInvoiceListController
	) {
		this.rentalInvoiceDetailDialog = dialog;
		this.connectionErrorDialog = new ConnectionErrorDialog(mainFrame);
		this.rentalInvoice = rentalInvoice;
		this.viewMode = viewMode;
		this.rentalInvoiceListController = rentalInvoiceListController;

		// Add action listeners
		this.rentalInvoiceDetailDialog.getPositiveButton().addActionListener(this);
		this.rentalInvoiceDetailDialog.getNegativeButton().addActionListener(this);
		this.connectionErrorDialog.getReconnectButton().addActionListener(this);
	}

	public void displayUI() {
		loadRoomNamesAndUpdateUI();

		if (rentalInvoice != null) {
			rentalInvoiceDetailDialog.getRentingStartDatePicker()
									 .setSelectedDate(rentalInvoice.getStartDate());
			rentalInvoiceDetailDialog.getCustomerNameTextField()
									 .setText(rentalInvoice.getCustomerName());
			rentalInvoiceDetailDialog.getIdentifierNumberTextField()
									 .setText(rentalInvoice.getIdentityNumber());
			rentalInvoiceDetailDialog.getAddressTextField()
									 .setText(rentalInvoice.getAddress());

			if (viewMode == DetailDialogModeEnum.VIEW_ONLY || viewMode == DetailDialogModeEnum.EDITING) {
				Timestamp now = UtilFunctions.getTimestamp(LocalDateTime.now());

				// day(now) > day(start date)
				if (now.compareTo(rentalInvoice.getStartDate()) > 0) {
					rentalInvoiceDetailDialog.setViewMode(DetailDialogModeEnum.VIEW_ONLY);
					rentalInvoiceDetailDialog.getPositiveButton().setEnabled(false);
				}
			}
		} else {
			Timestamp nowAfterTwoHour = UtilFunctions.getTimestamp(LocalDateTime.now(), 2);
			rentalInvoiceDetailDialog.getRentingStartDatePicker().setSelectedDate(nowAfterTwoHour);
		}

		rentalInvoiceDetailDialog.getCustomerTypeComboBox()
								 .setModel(new DefaultComboBoxModel<>(
										 RentalInvoice.CustomerTypeEnum.allCases()
								 ));

		rentalInvoiceDetailDialog.setVisible(true);
	}

	private void loadRoomNamesAndUpdateUI() {
		try {
			RoomDAO daoModel = new RoomDAO();
			roomList = daoModel.getAllByStatus(Room.RoomStatusEnum.RENTED);

			// If the condition is true => view only or edit
			if (rentalInvoice != null) {
				Optional<Room> roomOptional = daoModel.get(rentalInvoice.getRoomId());
				roomOptional.ifPresent(room -> {
					if (!roomList.contains(room)) {
						roomList.add(room);
					}
				});
			}

			Vector<String> roomNameList = new Vector<>();
			for (Room item: roomList) {
				roomNameList.add(item.getName());
			}

			DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>(roomNameList);
			this.rentalInvoiceDetailDialog.getRoomNameComboBox().setModel(comboBoxModel);
		} catch (DBConnectionException e) {
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
			System.out.println("RentalInvoiceDetailController.java - loadRoomNamesAndUpdateUI - catch - Unavailable connection.");
		}
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == rentalInvoiceDetailDialog.getPositiveButton()) {
			positiveButtonAction();
		} else if (event.getSource() == rentalInvoiceDetailDialog.getNegativeButton()) {
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
			rentalInvoiceDetailDialog.setVisible(false);
		} else if (viewMode == DetailDialogModeEnum.EDITING) {
			rollbackAllChanges();
		} else if (viewMode == DetailDialogModeEnum.CREATE) {
			rentalInvoiceDetailDialog.setVisible(false);
		}
	}

	private void reconnectButtonAction() {
		connectionErrorDialog.setExitOnCloseButton(false);
		connectionErrorDialog.setVisible(false);

		SingletonDBConnection.getInstance().connect();
		displayUI();
	}

	private void editButtonAction() {
		rentalInvoiceDetailDialog.setViewMode(DetailDialogModeEnum.EDITING);

		if (rentalInvoice != null && (viewMode == DetailDialogModeEnum.VIEW_ONLY || viewMode == DetailDialogModeEnum.EDITING)) {
			Timestamp today = UtilFunctions.getStartTimeOf(LocalDate.now());

			// day(today) > day(start date)
			if (today.compareTo(rentalInvoice.getStartDate()) > 0) {
				rentalInvoiceDetailDialog.getRentingStartDatePicker().setEnabledSelection(false);
			}
		}
	}

	private void saveButtonAction() {
		RentalInvoice newRentalInvoice = getRentalInvoiceInstanceFromInputFields();

		if (rentalInvoice.equals(newRentalInvoice)) {
			UtilFunctions.showInfoMessage(
					rentalInvoiceDetailDialog,
					"Edit Rental Invoice",
					"Information does not change."
			);
		} else if (checkEmptyFields(
				newRentalInvoice.getCustomerName(),
				newRentalInvoice.getIdentityNumber(),
				newRentalInvoice.getAddress()
		)) {
			UtilFunctions.showErrorMessage(
					rentalInvoiceDetailDialog,
					"Edit Rental Invoice",
					"All fields must not be empty."
			);
		} else if (newRentalInvoice.getIdentityNumber().length() != 9 || newRentalInvoice.getIdentityNumber().length() != 12) {
			UtilFunctions.showErrorMessage(
					rentalInvoiceDetailDialog,
					"Edit Rental Invoice",
					"The length of the identity number must be 9 or 12."
			);
		} else {
			try {
				int option = UtilFunctions.showConfirmDialog(
						rentalInvoiceDetailDialog,
						"Edit Rental Invoice",
						"Are you sure to save new information for this invoice?"
				);

				if (option == JOptionPane.YES_OPTION) {
					RentalInvoiceDAO daoModel = new RentalInvoiceDAO();
					daoModel.update(newRentalInvoice);
					UtilFunctions.showInfoMessage(
							rentalInvoiceDetailDialog,
							"Edit Rental Invoice",
							"Save successfully."
					);
				}
			} catch (DBConnectionException e) {
				SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
				System.out.println("RentalInvoiceDetailController.java - saveButtonAction - catch - Unavailable connection.");
			}
		}
	}

	private void createButtonAction() {
		RentalInvoice newRentalInvoice = getRentalInvoiceInstanceFromInputFields();

		if (checkEmptyFields(
				newRentalInvoice.getCustomerName(),
				newRentalInvoice.getIdentityNumber(),
				newRentalInvoice.getAddress()
		)) {
			UtilFunctions.showErrorMessage(
					rentalInvoiceDetailDialog,
					"Create Rental Invoice",
					"All fields must not be empty."
			);
		} else if (newRentalInvoice.getIdentityNumber().length() != 9 || newRentalInvoice.getIdentityNumber().length() != 12) {
			UtilFunctions.showErrorMessage(
					rentalInvoiceDetailDialog,
					"Edit Rental Invoice",
					"The length of the identity number must be 9 or 12."
			);
		} else {
			try {
				int option = UtilFunctions.showConfirmDialog(
						rentalInvoiceDetailDialog,
						"Create Rental Invoice",
						"Are you sure to create this invoice?"
				);

				if (option == JOptionPane.YES_OPTION) {
					RentalInvoiceDAO daoModel = new RentalInvoiceDAO();
					daoModel.insert(newRentalInvoice);
					UtilFunctions.showInfoMessage(
							rentalInvoiceDetailDialog,
							"Create Rental Invoice",
							"Create successfully."
					);

					rentalInvoiceDetailDialog.setVisible(false);
					rentalInvoiceListController.loadRentalInvoiceListAndReloadTableData();
				}
			} catch (DBConnectionException e) {
				SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
				System.out.println("RentalInvoiceDetailController.java - createButtonAction - catch - Unavailable connection.");
			}
		}
	}

	private void rollbackAllChanges() {
		rentalInvoiceDetailDialog.getRoomNameComboBox().setSelectedItem(rentalInvoice.getRoomName());
		rentalInvoiceDetailDialog.getRentingStartDatePicker().setSelectedDate(rentalInvoice.getStartDate());
		rentalInvoiceDetailDialog.getCustomerNameTextField().setText(rentalInvoice.getCustomerName());
		rentalInvoiceDetailDialog.getCustomerTypeComboBox().setSelectedItem(
				UtilFunctions.capitalizeFirstLetterInString(
						RentalInvoice.CustomerTypeEnum.valueOf(rentalInvoice.getCustomerType()).name()
				)
		);
		rentalInvoiceDetailDialog.getIdentifierNumberTextField().setText(rentalInvoice.getIdentityNumber());
		rentalInvoiceDetailDialog.getAddressTextField().setText(rentalInvoice.getAddress());

		rentalInvoiceDetailDialog.setViewMode(DetailDialogModeEnum.VIEW_ONLY);
	}

	public int findRoomIndexByRoomName(String roomName) {
		for (int i = 0; i < roomList.size(); i++) {
			if (roomList.get(i).getName().equals(roomName)) {
				return i;
			}
		}

		return -1;
	}

	private RentalInvoice getRentalInvoiceInstanceFromInputFields() {
		int rentalInvoiceId = rentalInvoice != null ? rentalInvoice.getId() : -1;
		String customerName = UtilFunctions.removeRedundantWhiteSpace(
				String.valueOf(rentalInvoiceDetailDialog.getCustomerNameTextField().getText())
		);
		String identifierNumber = UtilFunctions.removeRedundantWhiteSpace(
				String.valueOf(rentalInvoiceDetailDialog.getIdentifierNumberTextField().getText())
		);
		String address = UtilFunctions.removeRedundantWhiteSpace(
				String.valueOf(rentalInvoiceDetailDialog.getAddressTextField().getText())
		);
		Timestamp rentingStartDate = UtilFunctions.getStartTimeOf(
				rentalInvoiceDetailDialog.getRentingStartDatePicker().getSelectedYear(),
				rentalInvoiceDetailDialog.getRentingStartDatePicker().getSelectedMonth(),
				rentalInvoiceDetailDialog.getRentingStartDatePicker().getSelectedDay()
		);
		String roomName = UtilFunctions.removeRedundantWhiteSpace(
				String.valueOf(rentalInvoiceDetailDialog.getRoomNameComboBox().getSelectedItem())
		);
		int roomId = roomList.get(findRoomIndexByRoomName(roomName)).getId();
		RentalInvoice.CustomerTypeEnum customerType = RentalInvoice.CustomerTypeEnum.valueOfIgnoreCase(
				String.valueOf(rentalInvoiceDetailDialog.getCustomerTypeComboBox().getSelectedItem())
		);

		return new RentalInvoice(
				rentalInvoiceId,
				rentingStartDate,
				roomId,
				roomName,
				customerName,
				identifierNumber,
				address,
				customerType.ordinal()
		);
	}

	private boolean checkEmptyFields(String customerName, String identifierNumber, String address) {
		return !customerName.isEmpty() && !identifierNumber.isEmpty() && !address.isEmpty();
	}

}
