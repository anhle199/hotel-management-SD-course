package controllers.rooms;

import dao.RoomDAO;
import db.DBConnectionException;
import db.SingletonDBConnection;
import models.Room;
import models.RoomType;
import utils.DetailDialogModeEnum;
import utils.Pair;
import utils.UtilFunctions;
import views.components.dialogs.ConnectionErrorDialog;
import views.components.panels.ScrollableTablePanel;
import views.components.table_model.NonEditableTableModel;
import views.dialogs.RoomDetailDialog;
import views.panels.rooms.RoomListPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Vector;

public class RoomListController implements ActionListener {

	private final RoomListPanel roomListPanel;
	private final JFrame mainFrame;
	private final ConnectionErrorDialog connectionErrorDialog;
	private final RoomDAO daoModel;
	private final FilterBarController filterBarController;

	public RoomListController(RoomListPanel roomListPanel, JFrame mainFrame) {
		this.roomListPanel = roomListPanel;
		this.mainFrame = mainFrame;
		this.connectionErrorDialog = new ConnectionErrorDialog(mainFrame);
		this.daoModel = new RoomDAO();
		this.filterBarController = new FilterBarController(roomListPanel, this, mainFrame);

		// Add action listeners
		this.roomListPanel.getSearchButton().addActionListener(this);
		this.roomListPanel.getAddButton().addActionListener(this);
		this.roomListPanel.getRemoveButton().addActionListener(this);
		this.roomListPanel.getUpdateRulesMenuItem().addActionListener(this);
		this.connectionErrorDialog.getReconnectButton().addActionListener(this);

		// Add double-click listener for each row on the table
		roomListPanel.getScrollableTable().getTable().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent mouseEvent) {
				if (mouseEvent.getClickCount() == 2) {
					doubleClicksInRowAction();
				}
			}
		});
	}

	public void loadRoomListAndReloadTableData(String roomNameToken) {
		try {
			ArrayList<Pair<Room, RoomType>> data;
			if (roomNameToken.isEmpty()) {
				data = daoModel.getAllWithRoomType();
			} else {
				data = daoModel.searchByRoomNameReturnWithRoomType(roomNameToken);
			}

			addRoomListToTable(data);
		} catch (DBConnectionException e) {
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
			System.out.println("RoomListController.java - loadRoomListAndReloadTableData - catch - Unavailable connection.");
		}
	}

	public void addRoomListToTable(ArrayList<Pair<Room, RoomType>> data) {
		NonEditableTableModel tableModel = (NonEditableTableModel) roomListPanel.getScrollableTable().getTableModel();
		tableModel.removeAllRows();

		for (int i = 0; i < data.size(); i++) {
			Pair<Room, RoomType> element = data.get(i);
			Object[] rowValue = mapRoomInstanceToRowValue(i + 1, element.getLeftValue(), element.getRightValue());

			tableModel.addRow(rowValue);
		}
	}

	private Object[] mapRoomInstanceToRowValue(int no, Room room, RoomType roomType) {
		String capitalizedStatusString = UtilFunctions.capitalizeFirstLetterInString(
				Room.RoomStatusEnum.valueOf(room.getStatus()).name()
		);

		return new Object[]{
				no,
				room.getId(),
				room.getName(),
				roomType.getName(),
				roomType.getPrice(),
				capitalizedStatusString,
				room.getDescription(),
				room.getRoomTypeId()
		};
	}

	private Room mapRowValueToRoomInstance(Vector<Object> rowValue) {
		Room.RoomStatusEnum status = Room.RoomStatusEnum.valueOfIgnoreCase(
				String.valueOf(rowValue.get(5))
		);

		return new Room(
				(int) rowValue.get(RoomListPanel.HIDDEN_COLUMN_ROOM_ID),
				(String) rowValue.get(2),
				(String) rowValue.get(6),
				status.byteValue(),
				(int) rowValue.get(RoomListPanel.HIDDEN_COLUMN_ROOM_TYPE_ID)
		);
	}

	public void displayUI() {
		loadRoomListAndReloadTableData("");
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == roomListPanel.getSearchButton()) {
			searchButtonAction();
		} else if (event.getSource() == roomListPanel.getAddButton()) {
			addButtonAction();
		} else if (event.getSource() == roomListPanel.getRemoveButton()) {
			removeButtonAction();
		} else if (event.getSource() == roomListPanel.getUpdateRulesMenuItem()) {
//			updateRulesMenuItemAction();
		} else if (event.getSource() == connectionErrorDialog.getReconnectButton()) {
			reconnectButtonAction();
		}
	}

	private void searchButtonAction() {
		roomListPanel.setVisibleForFilterBar(false);

		String searchText = UtilFunctions.removeRedundantWhiteSpace(
				roomListPanel.getSearchBar().getText()
		);
		loadRoomListAndReloadTableData(searchText);
	}

	private void addButtonAction() {
		DetailDialogModeEnum viewMode = DetailDialogModeEnum.CREATE;
		RoomDetailDialog roomDetailDialog = new RoomDetailDialog(mainFrame, viewMode);
		RoomDetailController roomDetailController = new RoomDetailController(
				roomDetailDialog, mainFrame, viewMode, this
		);

		roomDetailController.displayUI();
	}

	private void removeButtonAction() {
		ScrollableTablePanel tablePanel = roomListPanel.getScrollableTable();
		int selectedRowIndex = tablePanel.getTable().getSelectedRow();

		if (selectedRowIndex == -1) {
			UtilFunctions.showWarningMessage(roomListPanel, "Remove Room", "You must select a row.");
		} else {
			int option = JOptionPane.showConfirmDialog(
					roomListPanel,
					"Are you sure to remove this room?",
					"Remove Room",
					JOptionPane.YES_NO_OPTION
			);

			if (option == JOptionPane.YES_OPTION) {
				int roomId = (int) tablePanel.getTableModel()
						.getValueAt(selectedRowIndex, RoomListPanel.HIDDEN_COLUMN_ROOM_ID);

				try {
					daoModel.delete(roomId);

					UtilFunctions.showInfoMessage(
							roomListPanel,
							"Remove Room",
							"This room is removed successfully."
					);
				} catch (DBConnectionException e) {
					SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
					System.out.println("RoomListController.java - removeButtonAction - catch - Unavailable connection.");
				}
			}
		}
	}

//	private void updateRulesMenuItemAction() {
//
//	}

	private void reconnectButtonAction() {
		connectionErrorDialog.setExitOnCloseButton(false);
		connectionErrorDialog.setVisible(false);

		SingletonDBConnection.getInstance().connect();
		roomListPanel.getSearchBar().setText("");
		loadRoomListAndReloadTableData("");
	}

	private void doubleClicksInRowAction() {
		JTable table = roomListPanel.getScrollableTable().getTable();
		NonEditableTableModel tableModel = (NonEditableTableModel) table.getModel();
		Vector<Object> selectedRowValue = tableModel.getRowValue(table.getSelectedRow());
		Room selectedRoom = mapRowValueToRoomInstance(selectedRowValue);

		DetailDialogModeEnum viewMode = DetailDialogModeEnum.VIEW_ONLY;
		RoomDetailDialog roomDetailDialog = new RoomDetailDialog(mainFrame, viewMode);
		RoomDetailController roomDetailController = new RoomDetailController(
				roomDetailDialog, mainFrame, selectedRoom, viewMode, this
		);

		roomDetailController.displayUI();
	}

}
