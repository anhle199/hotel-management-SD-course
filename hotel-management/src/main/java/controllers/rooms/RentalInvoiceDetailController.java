package controllers.rooms;

import dao.RentalInvoiceDAO;
import dao.RoomDAO;
import db.DBConnectionException;
import db.SingletonDBConnection;
import models.RentalInvoice;
import models.Room;
import models.RoomType;
import utils.DetailDialogModeEnum;
import utils.Pair;
import utils.UtilFunctions;
import views.components.dialogs.ConnectionErrorDialog;
import views.dialogs.RentalInvoiceDetailDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Vector;

public class RentalInvoiceDetailController implements ActionListener, ItemListener {

	private final RentalInvoiceDetailDialog rentalInvoiceDetailDialog;
	private final ConnectionErrorDialog connectionErrorDialog;

	private final RentalInvoice rentalInvoice;
	private DetailDialogModeEnum viewMode;
	private ArrayList<Pair<Room, RoomType>> roomList = new ArrayList<>();

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

		// Add item listeners
		this.rentalInvoiceDetailDialog.getRoomNameComboBox().addItemListener(this);
		this.rentalInvoiceDetailDialog.getRentingStartDatePicker().getYearOptions().addItemListener(this);
		this.rentalInvoiceDetailDialog.getRentingStartDatePicker().getMonthOptions().addItemListener(this);
		this.rentalInvoiceDetailDialog.getRentingStartDatePicker().getDayOptions().addItemListener(this);
		this.rentalInvoiceDetailDialog.getRentingEndDatePicker().getYearOptions().addItemListener(this);
		this.rentalInvoiceDetailDialog.getRentingEndDatePicker().getMonthOptions().addItemListener(this);
		this.rentalInvoiceDetailDialog.getRentingEndDatePicker().getDayOptions().addItemListener(this);
	}

	public void displayUI() {
		if (rentalInvoice != null) {
			Timestamp now = UtilFunctions.getTimestamp(LocalDateTime.now());

			// The now is after the end date
			boolean isPaid = rentalInvoice.getIsPaid() == RentalInvoice.PaymentStatusEnum.PAID.ordinal();
			if (now.after(rentalInvoice.getEndDate()) || isPaid || rentalInvoice.getRoomId() == 0) {
				setViewMode(DetailDialogModeEnum.VIEW_ONLY);
				rentalInvoiceDetailDialog.getPositiveButton().setEnabled(false);
				rentalInvoiceDetailDialog.getRoomNameComboBox().addItem(rentalInvoice.getRoomName());
			} else {
				loadRoomsAndUpdateUI();
			}

			rentalInvoiceDetailDialog.getPriceTextField()
									 .setText(String.valueOf(rentalInvoice.getRoomTypePrice()));
			rentalInvoiceDetailDialog.getRentingStartDatePicker()
									 .setSelectedDate(rentalInvoice.getStartDate());
			rentalInvoiceDetailDialog.getRentingEndDatePicker()
									 .setSelectedDate(rentalInvoice.getEndDate());
			rentalInvoiceDetailDialog.getCustomerNameTextField()
									 .setText(rentalInvoice.getCustomerName());
			rentalInvoiceDetailDialog.getIdentifierNumberTextField()
									 .setText(rentalInvoice.getIdentityNumber());
			rentalInvoiceDetailDialog.getAddressTextField()
									 .setText(rentalInvoice.getAddress());
		} else {
			loadRoomsAndUpdateUI();

			LocalDateTime now = LocalDateTime.now();

			Timestamp nowAfterTwoHour = UtilFunctions.getTimestamp(now, 2);
			rentalInvoiceDetailDialog.getRentingStartDatePicker().setSelectedDate(nowAfterTwoHour);

			Timestamp tomorrowAfterTwoHour = UtilFunctions.getTimestamp(now.plusDays(1), 2);
			rentalInvoiceDetailDialog.getRentingEndDatePicker().setSelectedDate(tomorrowAfterTwoHour);

			if (!roomList.isEmpty()) {
				rentalInvoiceDetailDialog.getPriceTextField()
						.setText(String.valueOf(roomList.get(0).getRightValue().getPrice()));
			}
		}

		rentalInvoiceDetailDialog.getCustomerTypeComboBox()
								 .setModel(new DefaultComboBoxModel<>(
										 RentalInvoice.CustomerTypeEnum.allCases()
								 ));

		rentalInvoiceDetailDialog.setVisible(true);
	}

	private void loadRoomsAndUpdateUI() {
		try {
			RoomDAO daoModel = new RoomDAO();
			roomList = daoModel.getAllWithRoomTypeByStatus(Room.RoomStatusEnum.AVAILABLE);
			System.out.println(roomList);
			// If the condition is true => view only or edit
			boolean isEditable = true;
			boolean needToSetRoomName = false;
			if (rentalInvoice != null) {
				Optional<Pair<Room, RoomType>> optionalValue = daoModel.getWithRoomType(rentalInvoice.getRoomId());
				if (optionalValue.isPresent()) {
					if (!roomList.contains(optionalValue.get())) {
						roomList.add(optionalValue.get());
						needToSetRoomName = true;
					}
				} else {
					rentalInvoiceDetailDialog.getPositiveButton().setEnabled(false);
					isEditable = false;
				}
			}

			if (isEditable) {
				Vector<String> roomNameList = new Vector<>();
				for (Pair<Room, RoomType> item: roomList) {
					roomNameList.add(item.getLeftValue().getName());
				}

				DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>(roomNameList);
				this.rentalInvoiceDetailDialog.getRoomNameComboBox()
											  .setModel(comboBoxModel);

				if (needToSetRoomName)
					this.rentalInvoiceDetailDialog.getRoomNameComboBox()
												  .setSelectedItem(rentalInvoice.getRoomName());
			}
		} catch (DBConnectionException e) {
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
			System.out.println("RentalInvoiceDetailController.java - loadRoomsAndUpdateUI - catch - Unavailable connection.");
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

	@Override
	public void itemStateChanged(ItemEvent event) {
		Timestamp startDate = UtilFunctions.getStartTimeOf(
				rentalInvoiceDetailDialog.getRentingStartDatePicker().getSelectedYear(),
				rentalInvoiceDetailDialog.getRentingStartDatePicker().getSelectedMonth(),
				rentalInvoiceDetailDialog.getRentingStartDatePicker().getSelectedDay()
		);
		Timestamp endDate = UtilFunctions.getEndTimeOf(
				rentalInvoiceDetailDialog.getRentingEndDatePicker().getSelectedYear(),
				rentalInvoiceDetailDialog.getRentingEndDatePicker().getSelectedMonth(),
				rentalInvoiceDetailDialog.getRentingEndDatePicker().getSelectedDay()
		);
		String roomName = UtilFunctions.removeRedundantWhiteSpace(
				String.valueOf(rentalInvoiceDetailDialog.getRoomNameComboBox().getSelectedItem())
		);

		int price = 0;
		if (!roomList.isEmpty()) {
			Pair<Room, RoomType> selectedRoomAndRoomType = roomList.get(findRoomIndexByRoomName(roomName));
			price = selectedRoomAndRoomType.getRightValue().getPrice();
		} else if (rentalInvoice != null) {
			price = rentalInvoice.getRoomTypePrice();
		}

		int totalPrice = UtilFunctions.calculateTotalPrice(price, startDate, endDate);
		System.out.println(startDate);
		System.out.println(endDate);
		System.out.println(price);
		System.out.println(totalPrice);
		rentalInvoiceDetailDialog.getPriceTextField().setText(String.valueOf(totalPrice));
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
		setViewMode(DetailDialogModeEnum.EDITING);

		if (rentalInvoice != null && (viewMode == DetailDialogModeEnum.VIEW_ONLY || viewMode == DetailDialogModeEnum.EDITING)) {
			Timestamp now = UtilFunctions.getTimestamp(LocalDateTime.now());

			// The now is after the end date
			if (now.after(rentalInvoice.getEndDate())) {
				rentalInvoiceDetailDialog.getRentingStartDatePicker().setEnabledSelection(false);
				rentalInvoiceDetailDialog.getRentingEndDatePicker().setEnabledSelection(false);
				rentalInvoiceDetailDialog.getPositiveButton().setEnabled(false);
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
		} else if (validateFields(newRentalInvoice)) {
			try {
				int option = UtilFunctions.showConfirmDialog(
						rentalInvoiceDetailDialog,
						"Edit Rental Invoice",
						"Are you sure to save new information for this invoice?"
				);

				if (option == JOptionPane.YES_OPTION) {
					RentalInvoiceDAO daoModel = new RentalInvoiceDAO();
					daoModel.update(newRentalInvoice, rentalInvoice.getRoomId());
					UtilFunctions.showInfoMessage(
							rentalInvoiceDetailDialog,
							"Edit Rental Invoice",
							"Save successfully."
					);

					this.rentalInvoice.copyFrom(newRentalInvoice);
					setViewMode(DetailDialogModeEnum.VIEW_ONLY);
					rentalInvoiceListController.loadRentalInvoiceListAndReloadTableData();
				}
			} catch (DBConnectionException e) {
				SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
				System.out.println("RentalInvoiceDetailController.java - saveButtonAction - catch - Unavailable connection.");
			}
		}
	}

	private void createButtonAction() {
		RentalInvoice newRentalInvoice = getRentalInvoiceInstanceFromInputFields();

		if (validateFields(newRentalInvoice)) {
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

		setViewMode(DetailDialogModeEnum.VIEW_ONLY);
	}

	public int findRoomIndexByRoomName(String roomName) {
		for (int i = 0; i < roomList.size(); i++) {
			if (roomList.get(i).getLeftValue().getName().equals(roomName)) {
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
		Timestamp rentingEndDate = UtilFunctions.getEndTimeOf(
				rentalInvoiceDetailDialog.getRentingEndDatePicker().getSelectedYear(),
				rentalInvoiceDetailDialog.getRentingEndDatePicker().getSelectedMonth(),
				rentalInvoiceDetailDialog.getRentingEndDatePicker().getSelectedDay()
		);
		String roomName = UtilFunctions.removeRedundantWhiteSpace(
				String.valueOf(rentalInvoiceDetailDialog.getRoomNameComboBox().getSelectedItem())
		);
		Pair<Room, RoomType> selectedRoomAndRoomType = roomList.get(findRoomIndexByRoomName(roomName));
		RentalInvoice.CustomerTypeEnum customerType = RentalInvoice.CustomerTypeEnum.valueOfIgnoreCase(
				String.valueOf(rentalInvoiceDetailDialog.getCustomerTypeComboBox().getSelectedItem())
		);
		byte isPaid = rentalInvoice != null ? rentalInvoice.getIsPaid() : 0;

		return new RentalInvoice(
				rentalInvoiceId,
				rentingStartDate,
				rentingEndDate,
				selectedRoomAndRoomType.getLeftValue().getId(),
				roomName,
				selectedRoomAndRoomType.getRightValue().getId(),
				selectedRoomAndRoomType.getRightValue().getName(),
				selectedRoomAndRoomType.getRightValue().getPrice(),
				customerName,
				identifierNumber,
				address,
				customerType.ordinal(),
				isPaid
		);
	}

	private boolean checkNotEmptyFields(String customerName, String identifierNumber, String address) {
		return !customerName.isEmpty() && !identifierNumber.isEmpty() && !address.isEmpty();
	}

	private boolean validateFields(RentalInvoice newRentalInvoice) {
		if (!checkNotEmptyFields(
				newRentalInvoice.getCustomerName(),
				newRentalInvoice.getIdentityNumber(),
				newRentalInvoice.getAddress()
		)) {
			UtilFunctions.showErrorMessage(
					rentalInvoiceDetailDialog,
					"Create Rental Invoice",
					"All fields must not be empty."
			);
			return false;
		} else if (!(newRentalInvoice.getIdentityNumber().length() == 9 || newRentalInvoice.getIdentityNumber().length() == 12)) {
			UtilFunctions.showErrorMessage(
					rentalInvoiceDetailDialog,
					"Edit Rental Invoice",
					"The length of the identity number must be 9 or 12."
			);
			return false;
		} else if (newRentalInvoice.getStartDate().after(newRentalInvoice.getEndDate())) {
			UtilFunctions.showErrorMessage(
					rentalInvoiceDetailDialog,
					"Edit Rental Invoice",
					"The end date must be greater than or equal to the start date."
			);
			return false;
		}

		return true;
	}

	private void setViewMode(DetailDialogModeEnum mode) {
		rentalInvoiceDetailDialog.setViewMode(mode);
		viewMode = mode;
	}

}
