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
	private final DetailDialogModeEnum viewMode;
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
			this.roomDetailDialog.getRoomTypeComboBox().setModel(comboBoxModel);
			this.roomDetailDialog.getPriceTextField()
								 .setText(String.valueOf(
										 roomTypeNameList.isEmpty()
												 ? 0 : roomTypeList.get(0).getPrice()
								 ));
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
			if (roomTypeList.isEmpty()) {
				this.roomDetailDialog.getPriceTextField().setText("0");
			} else {
				for (RoomType item: roomTypeList) {
					if (item.getName().equals(event.getItem())) {
						this.roomDetailDialog.getPriceTextField()
											 .setText(String.valueOf(item.getPrice()));
					}
				}
			}
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
	}

	private void editButtonAction() {
		roomDetailDialog.setViewMode(DetailDialogModeEnum.EDITING);
	}

	private void saveButtonAction() {
		try {
			RoomDAO daoModel = new RoomDAO();
			String roomName = UtilFunctions.removeRedundantWhiteSpace(
					roomDetailDialog.getRoomNameTextField().getText()
			);

			if (daoModel.isExistingRoomName(roomName)) {
				UtilFunctions.showErrorMessage(roomDetailDialog, "Save Room", "This room is existing.");
			} else {
				int option = UtilFunctions.showConfirmDialog(
						roomDetailDialog,
						"Save Room",
						"Are you sure to save new information for this room?"
				);

				if (option == JOptionPane.YES_OPTION) {
					// Description and status
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

					daoModel.update(new Room(room.getId(), roomName, description, status, roomTypeId));
					UtilFunctions.showInfoMessage(roomDetailDialog, "Save Room", "Save successfully.");
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
			String roomName = UtilFunctions.removeRedundantWhiteSpace(
					roomDetailDialog.getRoomNameTextField().getText()
			);

			if (daoModel.isExistingRoomName(roomName)) {
				UtilFunctions.showErrorMessage(roomDetailDialog, "Create Room", "This room is existing.");
			} else {
				int option = UtilFunctions.showConfirmDialog(
						roomDetailDialog,
						"Create Room",
						"Are you sure to create this room?"
				);

				if (option == JOptionPane.YES_OPTION) {
					// Description and status
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

					daoModel.insert(new Room(-1, roomName, description, status, roomTypeId));
					UtilFunctions.showInfoMessage(roomDetailDialog, "Create Room", "Create successfully.");

					roomDetailDialog.setVisible(false);
					roomListController.loadRoomListAndReloadTableData("");
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

		roomDetailDialog.setViewMode(DetailDialogModeEnum.VIEW_ONLY);
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

}
