package controllers.rooms;

import dao.RoomDAO;
import db.DBConnectionException;
import db.SingletonDBConnection;
import models.Room;
import models.RoomType;
import utils.Constants;
import utils.Pair;
import utils.RoleManager;
import utils.UtilFunctions;
import views.components.dialogs.ConnectionErrorDialog;
import views.components.panels.ScrollableTablePanel;
import views.components.table_model.NonEditableTableModel;
import views.panels.rooms.RoomListPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class RoomListController implements ActionListener {

	private final RoomListPanel roomListPanel;
	private final ConnectionErrorDialog connectionErrorDialog;
	private final RoomDAO daoModel;

	public RoomListController(RoomListPanel roomListPanel, JFrame mainFrame) {
		this.roomListPanel = roomListPanel;
		this.connectionErrorDialog = new ConnectionErrorDialog(mainFrame);
		this.daoModel = new RoomDAO();

		// Add action listeners
		this.roomListPanel.getSearchButton().addActionListener(this);
		this.roomListPanel.getAddButton().addActionListener(this);
		this.roomListPanel.getRemoveButton().addActionListener(this);
		this.roomListPanel.getUpdateRulesMenuItem().addActionListener(this);
		this.connectionErrorDialog.getReconnectButton().addActionListener(this);
	}

	private void loadRoomListAndReloadTableData(String roomNameToken) {
		try {
			ArrayList<Pair<Room, RoomType>> data;
			if (roomNameToken.isEmpty()) {
				data = daoModel.getAllWithRoomType();
			} else {
				data = daoModel.searchByRoomNameReturnWithRoomType(roomNameToken);
			}

			NonEditableTableModel tableModel = (NonEditableTableModel) roomListPanel.getScrollableTable().getTableModel();
			tableModel.removeAllRows();

			for (int i = 0; i < data.size(); i++) {
				Pair<Room, RoomType> element = data.get(i);
				Object[] rowValue = mapToRowValue(i + 1, element.getLeftValue(), element.getRightValue());

				tableModel.addRow(rowValue);
			}

		} catch (DBConnectionException e) {
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
			System.out.println("RoomListController.java - loadRoomListAndReloadTableData - catch - Unavailable connection.");
		}
	}

	private Object[] mapToRowValue(int no, Room room, RoomType roomType) {
		String capitalizedStatusString = UtilFunctions.capitalizeFirstLetterInString(
				Room.RoomStatusEnum.valueOf(room.getStatus()).name()
		);

		return new Object[]{no, room.getId(), room.getName(), roomType.getName(), roomType.getPrice(), capitalizedStatusString};
	}

	public void displayUI() {
		loadRoomListAndReloadTableData("");
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == roomListPanel.getSearchButton()) {
			searchButtonAction();
		} else if (event.getSource() == roomListPanel.getAddButton()) {
//			addButtonAction();
		} else if (event.getSource() == roomListPanel.getRemoveButton()) {
			removeButtonAction();
		} else if (event.getSource() == roomListPanel.getUpdateRulesMenuItem()) {
//			updateRulesMenuItemAction();
		} else if (event.getSource() == connectionErrorDialog.getReconnectButton()) {
			reconnectButtonAction();
		}
	}

	private void searchButtonAction() {
		String searchText = UtilFunctions.removeRedundantWhiteSpace(
				roomListPanel.getSearchBar().getText()
		);

		loadRoomListAndReloadTableData(searchText);
	}

//	private void addButtonAction() {
//
//	}

	private void removeButtonAction() {
		ScrollableTablePanel tablePanel = roomListPanel.getScrollableTable();
		int selectedRowIndex = tablePanel.getTable().getSelectedRow();

		if (selectedRowIndex == -1) {
			UtilFunctions.showWarningMessage(roomListPanel, "Remove room", "You must select a row.");
		} else {
			int option = JOptionPane.showConfirmDialog(
					roomListPanel,
					"Are you sure to remove this room?",
					"Remove room",
					JOptionPane.YES_NO_OPTION
			);

			if (option == JOptionPane.YES_OPTION) {
				int roomId = (int) tablePanel.getTableModel()
											 .getValueAt(selectedRowIndex, Constants.ID_COLUMN_INDEX);

				try {
					daoModel.delete(roomId);

					UtilFunctions.showInfoMessage(
							roomListPanel,
							"Remove room",
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

}
