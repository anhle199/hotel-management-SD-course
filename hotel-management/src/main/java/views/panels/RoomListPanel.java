package views.panels;

import shared.ButtonWithResizableIcon;
import shared.NonEditableTableModel;
import shared.panels.ImagePanel;
import shared.panels.ScrollableTablePanel;
import shared.panels.TextFieldPanel;
import shared.panels.TextFieldPanel.IconPosition;
import utils.Constants;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class RoomListPanel extends JPanel {
	// Top Bar
	private TextFieldPanel searchBar;
	private ButtonWithResizableIcon addButton;
	private ButtonWithResizableIcon moreButton;

	// Filter bar
	private JComboBox<String> roomStatusComboBox;
	private JComboBox<String> roomTypeComboBox;
	private TextFieldPanel rangeDatePicker;
	private TextFieldPanel rangePricePicker;
	private JComboBox<String> sortComboBox;

	private ScrollableTablePanel scrollableTable;

	public RoomListPanel() {
		super();
		setLayout(null);

		initTopBarPanel();
		initFilterBarPanel();
		initTable();
	}

	private void initTopBarPanel() {
		JPanel topBarPanel = new JPanel();
		topBarPanel.setBounds(0, 20, 1078, 44);
		topBarPanel.setLayout(null);
		add(topBarPanel);

		ImagePanel searchIcon = new ImagePanel(Constants.IconNames.SEARCH, 24, 24);
		Dimension searchBarSize = new Dimension(600, 44);
		searchBar = new TextFieldPanel("Search", searchIcon, IconPosition.LEADING, searchBarSize);
		searchBar.setBounds(20, 0, searchBarSize.width, searchBarSize.height);
		topBarPanel.add(searchBar);


		Dimension iconSize = new Dimension(24, 24);

		Icon addIcon = new ImageIcon(Constants.IconNames.ADD);
		addButton = new ButtonWithResizableIcon("Add", addIcon, iconSize);
		addButton.setBounds(922, 0, 80, 44);
		addButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		addButton.setIconTextGap(4);
		addButton.setForeground(Constants.Colors.WHITE);
		addButton.setBackground(Constants.Colors.SECONDARY);
		topBarPanel.add(addButton);

		Icon moreIcon = new ImageIcon(Constants.IconNames.MORE_HORIZ);
		moreButton = new ButtonWithResizableIcon("", moreIcon, iconSize);
		moreButton.setBounds(1014, 0, 44, 44);
		moreButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		moreButton.setForeground(Constants.Colors.WHITE);
		moreButton.setBackground(Constants.Colors.SECONDARY);
		topBarPanel.add(moreButton);
	}

	private void initFilterBarPanel() {
		JPanel filterBarPanel = new JPanel();
		filterBarPanel.setBounds(0, 84, 1078, 40);
		filterBarPanel.setLayout(null);
		add(filterBarPanel);

		roomStatusComboBox = new JComboBox<>(new String[]{"All", "Available", "Reserved", "Renting"});
		roomStatusComboBox.setBounds(20, 0, 90, 40);
		filterBarPanel.add(roomStatusComboBox);

		roomTypeComboBox = new JComboBox<>(new String[]{"Affordable", "Normal", "Luxury"});
		roomTypeComboBox.setBounds(130, 0, 100, 40);
		filterBarPanel.add(roomTypeComboBox);

		ImagePanel calendarIcon = new ImagePanel(Constants.IconNames.CALENDAR_MONTH, 24, 24);
		Dimension rangeDatePickerSize = new Dimension(205, 40);
		rangeDatePicker = new TextFieldPanel("", calendarIcon, IconPosition.TRAILING, rangeDatePickerSize);
		rangeDatePicker.getTextField().setEditable(false);
		rangeDatePicker.getTextField().setText("31/12/2022 - 31/12/2022");
		rangeDatePicker.setBounds(250, 0, rangeDatePickerSize.width, rangeDatePickerSize.height);
		filterBarPanel.add(rangeDatePicker);

		ImagePanel filterIcon = new ImagePanel(Constants.IconNames.FILTER_ALT, 24, 24);
		Dimension rangePricePickerSize = new Dimension(150, 40);
		rangePricePicker = new TextFieldPanel("", filterIcon, IconPosition.TRAILING, rangePricePickerSize);
		rangePricePicker.getTextField().setEditable(false);
		rangePricePicker.getTextField().setHorizontalAlignment(SwingConstants.CENTER);
		rangePricePicker.getTextField().setText("200$ - 500$");
		rangePricePicker.setBounds(475, 0, rangePricePickerSize.width, rangePricePickerSize.height);
		filterBarPanel.add(rangePricePicker);

		sortComboBox = new JComboBox<>(new String[]{"Lowest Price", "Highest Price"});
		sortComboBox.setBounds(938, 0, 120, 40);
		filterBarPanel.add(sortComboBox);
	}

	private void initTable() {
		final String[] columnNames = {"", "Room name", "Room type", "Price", "Status"};
		final int [] columnWidths = {40, 450, 200, 130, 200};
		final int[] columnHorizontalAlignments = {
				DefaultTableCellRenderer.CENTER,
				DefaultTableCellRenderer.LEFT,
				DefaultTableCellRenderer.LEFT,
				DefaultTableCellRenderer.LEFT,
				DefaultTableCellRenderer.LEFT,
		};

		scrollableTable = new ScrollableTablePanel(
				new JTable(new NonEditableTableModel(columnNames, 0)) {
					public boolean getScrollableTracksViewportWidth() {
						return getPreferredSize().width < getParent().getWidth();
					}
				},
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
		);
		scrollableTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollableTable.setColumnWidths(columnWidths);
		scrollableTable.setColumnHorizontalAlignments(columnHorizontalAlignments);
		add(scrollableTable);

		final int tableWidth = scrollableTable.getTableWidth();

		scrollableTable.setRowHeight(40);
		scrollableTable.setIntercellSpacing(new Dimension(4, 4));
		scrollableTable.setHeaderSize(new Dimension(tableWidth, 40));
		scrollableTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		scrollableTable.setBounds(20, 144, 1038, 620);

		NonEditableTableModel model = (NonEditableTableModel) scrollableTable.getTableModel();
		model.addRow(new Object[]{1, "Room name", "Room type", "Price", "Status"});
		model.addRow(new Object[]{2, "Room name", "Room type", "Price", "Status"});
		model.addRow(new Object[]{3, "Room name", "Room type", "Price", "Status"});
		model.addRow(new Object[]{4, "Room name", "Room type", "Price", "Status"});
		model.addRow(new Object[]{5, "Room name", "Room type", "Price", "Status"});
	}

}
