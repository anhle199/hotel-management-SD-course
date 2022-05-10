package controllers.rooms;

import dao.RoomDAO;
import dao.RoomTypeDAO;
import db.DBConnectionException;
import db.SingletonDBConnection;
import models.Room;
import models.RoomType;
import utils.DetailDialogModeEnum;
import utils.UtilFunctions;
import views.components.dialogs.ConnectionErrorDialog;
import views.dialogs.RoomDetailDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Vector;

public class RoomDetailController implements ActionListener, ItemListener {

	private final RoomDetailDialog roomDetailDialog;
	private final ConnectionErrorDialog connectionErrorDialog;

	private final Room room;
	private DetailDialogModeEnum viewMode;
	private ArrayList<RoomType> roomTypeList;

	private final RoomListController roomListController;

	public RoomDetailController(
			RoomDetailDialog dialog,
			JFrame mainFrame,
			DetailDialogModeEnum viewMode,
			RoomListController roomListController
	) {
		this(dialog, mainFrame, null, viewMode, roomListController);
	}

	public RoomDetailController(
			RoomDetailDialog dialog,
			JFrame mainFrame,
			Room room,
			DetailDialogModeEnum viewMode,
			RoomListController roomListController
	) {
		this.roomDetailDialog = dialog;
		this.connectionErrorDialog = new ConnectionErrorDialog(mainFrame);
		this.room = room;
		this.viewMode = viewMode;
		this.roomListController = roomListController;

		// Add action listeners
		this.roomDetailDialog.getPositiveButton().addActionListener(this);
		this.roomDetailDialog.getNegativeButton().addActionListener(this);
		this.connectionErrorDialog.getReconnectButton().addActionListener(this);

		// Add item listener
		this.roomDetailDialog.getRoomTypeComboBox().addItemListener(this);

		this.roomDetailDialog.getPriceTextField().setEnabled(false);
	}

	public void displayUI() {
		loadRoomTypesAndUpdateUI();

		if (room != null) {
			RoomType roomType = roomTypeList.get(findRoomTypeIndexByRoomTypeId(room.getRoomTypeId()));

			roomDetailDialog.getRoomNameTextField()
							.setText(room.getName());
			roomDetailDialog.getRoomTypeComboBox()
							.setSelectedItem(roomType.getName());
			roomDetailDialog.getPriceTextField()
							.setText(String.valueOf(roomType.getPrice()));
			roomDetailDialog.getNoteTextArea()
							.setText(room.getDescription());
		}

		roomDetailDialog.setVisible(true);
	}

	private void loadRoomTypesAndUpdateUI() {
		try {
			RoomTypeDAO daoModel = new RoomTypeDAO();
			roomTypeList = daoModel.getAll();

			Vector<String> roomTypeNameList = new Vector<>();
			for (RoomType item: roomTypeList) {
				roomTypeNameList.add(item.getName());
			}

			DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>(roomTypeNameList);
			this.roomDetailDialog.getRoomTypeComboBox()
								 .setModel(comboBoxModel);
			this.roomDetailDialog.getPriceTextField()
								 .setText(String.valueOf(roomTypeList.get(0).getPrice()));
		} catch (DBConnectionException e) {
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
			System.out.println("RoomDetailController.java - loadRoomTypesAndUpdateUI - catch - Unavailable connection.");
		}
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == roomDetailDialog.getPositiveButton()) {
			positiveButtonAction();
		} else if (event.getSource() == roomDetailDialog.getNegativeButton()) {
			negativeButtonAction();
		} else if (event.getSource() == connectionErrorDialog.getReconnectButton()) {
			reconnectButtonAction();
		}
	}

	@Override
	public void itemStateChanged(ItemEvent event) {
		if (event.getSource() == roomDetailDialog.getRoomTypeComboBox()) {
			String roomTypeName = String.valueOf(roomDetailDialog.getRoomTypeComboBox().getSelectedItem());
			RoomType roomType = roomTypeList.get(findRoomTypeIndexByRoomName(roomTypeName));

			roomDetailDialog.getPriceTextField().setText(String.valueOf(roomType.getPrice()));
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
			roomDetailDialog.setVisible(false);
		} else if (viewMode == DetailDialogModeEnum.EDITING) {
			rollbackAllChanges();
		} else if (viewMode == DetailDialogModeEnum.CREATE) {
			roomDetailDialog.setVisible(false);
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
	}

	private void saveButtonAction() {
		try {
			RoomDAO roomDAO = new RoomDAO();
			Room newRoom = getRoomInstanceFromInputFields();

			if (room.equals(newRoom)) {
				UtilFunctions.showInfoMessage(roomDetailDialog, "Edit Room", "Information does not change.");
			} else if (newRoom.getName().isEmpty()) {
				UtilFunctions.showErrorMessage(roomDetailDialog, "Edit Room", "Room name must not be empty.");
			} else if (roomDAO.isExistingRoomName(newRoom.getName())) {
				UtilFunctions.showErrorMessage(roomDetailDialog, "Edit Room", "This room is existing.");
			} else {
				int option = UtilFunctions.showConfirmDialog(
						roomDetailDialog,
						"Edit Room",
						"Are you sure to save new information for this room?"
				);

				if (option == JOptionPane.YES_OPTION) {
					int index = findRoomTypeIndexByRoomTypeId(newRoom.getRoomTypeId());
					roomDAO.update(newRoom, roomTypeList.get(index));
					UtilFunctions.showInfoMessage(roomDetailDialog, "Edit Room", "Save successfully.");

					this.room.copyFrom(newRoom);
					setViewMode(DetailDialogModeEnum.VIEW_ONLY);
					roomListController.reloadTableDataWithCurrentSearchValue();
				}
			}
		} catch (DBConnectionException e) {
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
			System.out.println("RoomDetailController.java - saveButtonAction - catch - Unavailable connection.");
		}
	}

	private void createButtonAction() {
		try {
			RoomDAO daoModel = new RoomDAO();
			Room newRoom = getRoomInstanceFromInputFields();

			if (newRoom.getName().isEmpty()) {
				UtilFunctions.showErrorMessage(roomDetailDialog, "Create Room", "Room name must not be empty.");
			} else if (daoModel.isExistingRoomName(newRoom.getName())) {
				UtilFunctions.showErrorMessage(roomDetailDialog, "Create Room", "This room is existing.");
			} else {
				int option = UtilFunctions.showConfirmDialog(
						roomDetailDialog,
						"Create Room",
						"Are you sure to create this room?"
				);

				if (option == JOptionPane.YES_OPTION) {
					daoModel.insert(newRoom);
					UtilFunctions.showInfoMessage(roomDetailDialog, "Create Room", "Create successfully.");

					roomDetailDialog.setVisible(false);
					roomListController.reloadTableDataWithCurrentSearchValue();
				}
			}
		} catch (DBConnectionException e) {
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
			System.out.println("RoomDetailController.java - createButtonAction - catch - Unavailable connection.");
		}
	}

	private void rollbackAllChanges() {
		roomDetailDialog.getRoomNameTextField().setText(room.getName());
		roomDetailDialog.getNoteTextArea().setText(room.getDescription());

		int roomTypeIndex = findRoomTypeIndexByRoomTypeId(room.getRoomTypeId());
		if (roomTypeIndex != -1) {
			roomDetailDialog.getRoomTypeComboBox()
							.setSelectedItem(
									roomTypeList.get(roomTypeIndex).getName()
							);
		}

		setViewMode(DetailDialogModeEnum.VIEW_ONLY);
	}

	public int findRoomTypeIndexByRoomName(String roomName) {
		for (int i = 0; i < roomTypeList.size(); i++) {
			if (roomTypeList.get(i).getName().equals(roomName)) {
				return i;
			}
		}

		return -1;
	}

	public int findRoomTypeIndexByRoomTypeId(int roomTypeId) {
		for (int i = 0; i < roomTypeList.size(); i++) {
			if (roomTypeList.get(i).getId() == roomTypeId) {
				return i;
			}
		}

		return -1;
	}

	private Room getRoomInstanceFromInputFields() {
		int roomId = room != null ? room.getId() : -1;
		String roomName = UtilFunctions.removeRedundantWhiteSpace(
				roomDetailDialog.getRoomNameTextField().getText()
		);
		String description = UtilFunctions.removeRedundantWhiteSpace(
				roomDetailDialog.getNoteTextArea().getText()
		);
		byte status = (byte) Room.RoomStatusEnum.AVAILABLE.ordinal();

		// Get room type id
		int roomTypeId;
		if (roomTypeList.isEmpty()) {
			roomTypeId = -1;
		} else {
			String roomTypeName = String.valueOf(roomDetailDialog.getRoomTypeComboBox().getSelectedItem());
			roomTypeId = roomTypeList.get(findRoomTypeIndexByRoomName(roomTypeName)).getId();
		}

		return new Room(roomId, roomName, description, status, roomTypeId);
	}

	private void setViewMode(DetailDialogModeEnum mode) {
		roomDetailDialog.setViewMode(mode);
		viewMode = mode;
	}

}
