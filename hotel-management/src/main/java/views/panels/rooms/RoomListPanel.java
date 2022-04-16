package views.panels.rooms;

import utils.Constants;
import utils.RoleManager;
import utils.UtilFunctions;
import views.components.ButtonWithResizableIcon;
import views.components.table_model.NonEditableTableModel;
import views.components.panels.ImagePanel;
import views.components.panels.ScrollableTablePanel;
import views.components.panels.TextFieldPanel;
import views.components.panels.TextFieldPanel.IconPosition;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class RoomListPanel extends JPanel {

	// Top Bar.
	private TextFieldPanel searchBar;
	private JButton filterButton;
	private JButton addButton;
	private JButton removeButton;
	private ButtonWithResizableIcon moreButton;

	// Filter Bar.
	private JPanel filterBarPanel;
	private JComboBox<String> roomStatusComboBox;
	private JComboBox<String> roomTypeComboBox;
	private TextFieldPanel rangeDatePicker;
	private TextFieldPanel rangePricePicker;
	private JComboBox<String> sortCriterionComboBox;

	private ScrollableTablePanel scrollableTable;

	public RoomListPanel() {
		super();
		setLayout(null);

		initTopBarPanel();
		initFilterBarPanel();
		initTable();

		// Action for filter button.
		filterButton.addActionListener(event -> {
			boolean isShowingFilterBar = filterBarPanel.isVisible();
			int yTable = isShowingFilterBar ? 80 : 140;
			int heightTable = isShowingFilterBar ? 682 : 622;
			String filterButtonTitle = isShowingFilterBar ? "Show Filter" : "Hide Filter";

			scrollableTable.setBounds(20, yTable, 1038, heightTable);
			filterButton.setText(filterButtonTitle);
			filterBarPanel.setVisible(!isShowingFilterBar);
		});
	}

	private void initTopBarPanel() {
		// Top Bar Panel.
		JPanel topBarPanel = new JPanel();
		topBarPanel.setBounds(20, 20, 1038, 40);
		topBarPanel.setLayout(null);
		add(topBarPanel);

		// Search Bar.
		ImagePanel searchIcon = new ImagePanel(Constants.IconNames.SEARCH_BLACK, 24, 24);
		Dimension searchBarSize = new Dimension(500, 40);
		searchBar = new TextFieldPanel("Search", searchIcon, IconPosition.LEADING, searchBarSize);
		searchBar.setBounds(0, 0, searchBarSize.width, searchBarSize.height);
		topBarPanel.add(searchBar);

		// Top Bar Buttons Panel.
		JPanel topBarButtonsPanel = new JPanel();
		topBarButtonsPanel.setBounds(617, 0, 426, 40);
		topBarButtonsPanel.setLayout(new FlowLayout(FlowLayout.TRAILING, 0, 0));
		topBarPanel.add(topBarButtonsPanel);

		// Filter Button.
		filterButton = new JButton("Show Filter");
		filterButton.setPreferredSize(new Dimension(140, 40));
		UtilFunctions.configureTopBarButtonOnMainThread(filterButton);
		topBarButtonsPanel.add(filterButton);

		// Add Button.
		addButton = new JButton("Add");
		addButton.setPreferredSize(new Dimension(85, 40));
		UtilFunctions.configureTopBarButtonOnMainThread(addButton);
		topBarButtonsPanel.add(Box.createHorizontalStrut(12));
		topBarButtonsPanel.add(addButton);

		// Remove Button.
		removeButton = new JButton("Remove");
		removeButton.setPreferredSize(new Dimension(115, 40));
		UtilFunctions.configureTopBarButtonOnMainThread(removeButton);
		topBarButtonsPanel.add(Box.createHorizontalStrut(12));
		topBarButtonsPanel.add(removeButton);

		if (RoleManager.getInstance().isManager()) {
			Icon moreIcon = new ImageIcon(Constants.IconNames.MORE_HORIZ_WHITE);
			Dimension iconSize = new Dimension(24, 24);

			// More Icon Button.
			moreButton = new ButtonWithResizableIcon(moreIcon, iconSize);
			moreButton.setPreferredSize(new Dimension(50, 40));
			moreButton.setHorizontalAlignment(SwingConstants.CENTER);
			UtilFunctions.configureTopBarButtonOnMainThread(moreButton);
			topBarButtonsPanel.add(Box.createHorizontalStrut(12));
			topBarButtonsPanel.add(moreButton);
		}
	}

	private void initFilterBarPanel() {
		// Filter Bar Panel.
		filterBarPanel = new JPanel();
		filterBarPanel.setBounds(20, 84, 1038, 40);
		filterBarPanel.setLayout(null);
		filterBarPanel.setVisible(false);
		add(filterBarPanel);

		// Filter Criterion Panel.
		JPanel filterCriterionPanel = new JPanel();
		filterCriterionPanel.setBounds(0, 0, 581, 40);
		filterCriterionPanel.setLayout(new FlowLayout(FlowLayout.TRAILING, 0, 0));
		filterBarPanel.add(filterCriterionPanel);

		// Room Status Combo Box.
		roomStatusComboBox = new JComboBox<>(new String[]{"All", "Available", "Reserved", "Renting"});
		roomStatusComboBox.setPreferredSize(new Dimension(90, 40));
		roomStatusComboBox.setFont(Constants.Fonts.BODY);
		filterCriterionPanel.add(roomStatusComboBox);
		filterCriterionPanel.add(Box.createHorizontalStrut(12));

		// Room Type Combo Box.
		roomTypeComboBox = new JComboBox<>(new String[]{"Affordable", "Normal", "Luxury"});
		roomTypeComboBox.setPreferredSize(new Dimension(100, 40));
		roomTypeComboBox.setFont(Constants.Fonts.BODY);
		filterCriterionPanel.add(roomTypeComboBox);
		filterCriterionPanel.add(Box.createHorizontalStrut(12));

		// Range Date Picker.
		ImagePanel calendarIcon = new ImagePanel(Constants.IconNames.CALENDAR_MONTH_BLACK, 24, 24);
		Dimension rangeDatePickerSize = new Dimension(205, 40);
		rangeDatePicker = new TextFieldPanel("", calendarIcon, IconPosition.TRAILING, rangeDatePickerSize);
		rangeDatePicker.getTextField().setEditable(false);
		rangeDatePicker.getTextField().setText("31/12/2022 - 31/12/2022");
		filterCriterionPanel.add(rangeDatePicker);
		filterCriterionPanel.add(Box.createHorizontalStrut(12));

		// Range Price Picker.
		ImagePanel filterIcon = new ImagePanel(Constants.IconNames.FILTER_ALT_BLACK, 24, 24);
		Dimension rangePricePickerSize = new Dimension(150, 40);
		rangePricePicker = new TextFieldPanel("", filterIcon, IconPosition.TRAILING, rangePricePickerSize);
		rangePricePicker.getTextField().setEditable(false);
		rangePricePicker.getTextField().setHorizontalAlignment(SwingConstants.CENTER);
		rangePricePicker.getTextField().setText("200$ - 500$");
		filterCriterionPanel.add(rangePricePicker);

		// Sort Criterion Combo Box.
		sortCriterionComboBox = new JComboBox<>(new String[]{"Lowest Price", "Highest Price"});
		sortCriterionComboBox.setBounds(918, 0, 120, 40);
		sortCriterionComboBox.setFont(Constants.Fonts.BODY);
		filterBarPanel.add(sortCriterionComboBox);
	}

	private void initTable() {
		final String[] columnNames = {"", "Room name", "Room type", "Price", "Status"};
		final int [] columnWidths = {40, 450, 200, 130, 199};
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

		scrollableTable.setHeaderSize(new Dimension(tableWidth, 40));
		scrollableTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		scrollableTable.setBounds(20, 80, 1038, 682);

		NonEditableTableModel model = (NonEditableTableModel) scrollableTable.getTableModel();
		model.addRow(new Object[]{1, "Room name", "Room type", "Price", "Status"});
		model.addRow(new Object[]{2, "Room name", "Room type", "Price", "Status"});
		model.addRow(new Object[]{3, "Room name", "Room type", "Price", "Status"});
		model.addRow(new Object[]{4, "Room name", "Room type", "Price", "Status"});
		model.addRow(new Object[]{5, "Room name", "Room type", "Price", "Status"});
	}

}
