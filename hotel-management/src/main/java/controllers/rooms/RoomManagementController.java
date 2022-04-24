package controllers.rooms;

import views.tabbed_panels.RoomManagementTabbed;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class RoomManagementController implements ChangeListener {

	private final RoomManagementTabbed roomManagementTabbed;
	private final RoomListController roomListController;
	private int currentTabIndex;

	public RoomManagementController(RoomManagementTabbed roomManagementTabbed, JFrame mainFrame) {
		this.roomManagementTabbed = roomManagementTabbed;
		this.roomListController = new RoomListController(
				roomManagementTabbed.getRoomListPanel(),
				mainFrame
		);
		this.currentTabIndex = RoomManagementTabbed.ROOM_LIST_PANEL_INDEX;

		// Add change listener
		this.roomManagementTabbed.addChangeListener(this);
	}

	public void displayUI() {
		didSelectTab(currentTabIndex);
		roomManagementTabbed.setVisible(true);
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		currentTabIndex = roomManagementTabbed.getSelectedIndex();
		didSelectTab(currentTabIndex);
	}

	private void didSelectTab(int index) {
		switch (index) {
			case RoomManagementTabbed.ROOM_LIST_PANEL_INDEX:
				roomListController.displayUI();
				break;
//			case RoomManagementTabbed.RENTAL_INVOICE_LIST_PANEL_INDEX:
//				break;
//			case RoomManagementTabbed.RENTAL_RECEIPT_LIST_PANEL_INDEX:
//				break;
		}
	}

}