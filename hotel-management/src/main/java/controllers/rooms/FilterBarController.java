package controllers.rooms;

import controllers.RangeDatePickerController;
import controllers.RangePriceInputController;
import dao.RoomDAO;
import dao.RoomTypeDAO;
import db.DBConnectionException;
import models.Room;
import models.RoomType;
import utils.Pair;
import views.components.dialogs.ConnectionErrorDialog;
import views.dialogs.RangeDatePickerDialog;
import views.dialogs.RangePriceInputDialog;
import views.panels.rooms.RoomListPanel;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Vector;

public class FilterBarController implements ItemListener {

	private final RoomListPanel roomListPanel;
	private final RoomListController roomListController;
	private final JFrame mainFrame;
	private final ConnectionErrorDialog connectionErrorDialog;
	private final RoomDAO daoModel;

	private final String[] allRoomStatuses = Room.RoomStatusEnum.allCases();
	private final Vector<String> allRoomTypes = new Vector<>();
	private final String[] sortCriteria = {"Lowest Price", "Highest Price"};

	public FilterBarController(RoomListPanel roomListPanel, RoomListController roomListController, JFrame mainFrame) {
		this.roomListPanel = roomListPanel;
		this.roomListController = roomListController;
		this.mainFrame = mainFrame;
		this.connectionErrorDialog = new ConnectionErrorDialog(mainFrame);
		this.daoModel = new RoomDAO();

		setComboBoxModels();

		// Add item listeners
		this.roomListPanel.getRoomStatusComboBox().addItemListener(this);
		this.roomListPanel.getRoomTypeComboBox().addItemListener(this);
		this.roomListPanel.getSortCriterionComboBox().addItemListener(this);

		// Add mouse listeners
		this.roomListPanel.getRangeDatePicker().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent mouseEvent) {
				if (mouseEvent.getClickCount() == 1) {
					rangeDatePickerClickAction();
				}
			}
		});
		this.roomListPanel.getRangePriceInput().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent mouseEvent) {
				if (mouseEvent.getClickCount() == 1) {
					rangePricePickerClickAction();
				}
			}
		});
	}

	private void setComboBoxModels() {
		try {
			RoomTypeDAO roomTypeDAO = new RoomTypeDAO();
			ArrayList<RoomType> roomTypeList = roomTypeDAO.getAll();

			allRoomTypes.add("All");
			for (RoomType item: roomTypeList) {
				allRoomTypes.add(item.getName());
			}

			roomListPanel.getRoomStatusComboBox().setModel(new DefaultComboBoxModel<>(allRoomStatuses));
			roomListPanel.getRoomTypeComboBox().setModel(new DefaultComboBoxModel<>(allRoomTypes));
			roomListPanel.getSortCriterionComboBox().setModel(new DefaultComboBoxModel<>(sortCriteria));
		} catch (DBConnectionException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void itemStateChanged(ItemEvent event) {
		filterAction();
	}

	public void filterAction() {
		roomListPanel.getSearchBar().setText("");

		String statusName = String.valueOf(roomListPanel.getRoomStatusComboBox().getSelectedItem());
		String roomTypeName = String.valueOf(roomListPanel.getRoomTypeComboBox().getSelectedItem());
		String sortCriterionName = String.valueOf(roomListPanel.getRoomTypeComboBox().getSelectedItem());

		String rangeDateInString = roomListPanel.getRangeDatePicker().getText();
		String[] dates = rangeDateInString.split(" - ");
		dates[0] = dates[0].replace("/", "-") + "00:00:00";
		dates[1] = dates[1].replace("/", "-") + "23:59:59";

		String rangePriceInString = roomListPanel.getRangeDatePicker().getText();
		String[] priceInStrings = rangePriceInString.split(" - ");
		int[] prices = new int[2];
		for (int i = 0; i < priceInStrings.length; i++) {  // remove $ character and parse it to int value
			String removedDollarCharacter = priceInStrings[i].substring(0, priceInStrings[i].length() - 1);
			prices[i] = Integer.parseInt(removedDollarCharacter);
		}

		try {
			ArrayList<Pair<Room, RoomType>> filteredData = daoModel.filterByAndReturnWithRoomType(
					roomTypeName,
					statusName,
					dates[0], dates[1],
					prices[0], prices[1],
					sortCriterionName.equals(sortCriteria[0])
			);
			roomListController.addRoomListToTable(filteredData);
		} catch (DBConnectionException e) {
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
			System.out.println("FilterBarController.java - filterAction - catch - Unavailable connection.");
		}
	}

	private void rangeDatePickerClickAction() {
		RangeDatePickerDialog dialog = new RangeDatePickerDialog(mainFrame);
		RangeDatePickerController controller = new RangeDatePickerController(
				dialog, roomListPanel.getRangeDatePicker(), this
		);

		controller.displayUI();
	}

	private void rangePricePickerClickAction() {
		RangePriceInputDialog dialog = new RangePriceInputDialog(mainFrame);
		RangePriceInputController controller = new RangePriceInputController(
				dialog, roomListPanel.getRangePriceInput(), this
		);

		controller.displayUI();
	}

}
