package views.panels.rooms;

import utils.Constants;
import utils.RoleManager;
import utils.UtilFunctions;
import views.components.ButtonWithResizableIcon;
import views.components.panels.ImagePanel;
import views.components.panels.ScrollableTablePanel;
import views.components.panels.TextFieldPanel;
import views.components.panels.TextFieldPanel.IconPosition;
import views.components.table_model.NonEditableTableModel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class RoomListPanel extends JPanel {

	public static final int HIDDEN_COLUMN_ROOM_ID = 1;
	public static final int HIDDEN_COLUMN_ROOM_TYPE_ID = 7;

	// Top Bar.
	private TextFieldPanel searchBar;
	private JButton searchButton;
	private JButton addButton;
	private JButton removeButton;
	private ButtonWithResizableIcon moreButton;
	private JMenuItem filterMenuItem;
	private JMenuItem updateRulesMenuItem;

	// Filter Bar.
	private JPanel filterBarPanel;
	private JComboBox<String> roomStatusComboBox;
	private JComboBox<String> roomTypeComboBox;
//	private TextFieldPanel rangeDatePicker;
	private TextFieldPanel rangePriceInput;
	private JComboBox<String> sortCriterionComboBox;

	private ScrollableTablePanel scrollableTable;

	public RoomListPanel() {
		super();
		setLayout(null);

		initTopBarPanel();
		initFilterBarPanel();
		initTable();
	}

	private void initTopBarPanel() {
		// Top Bar Panel.
		JPanel topBarPanel = new JPanel();
		topBarPanel.setBounds(20, 20, 1038, 40);
		topBarPanel.setLayout(null);
		add(topBarPanel);

		// Search Bar.
		ImagePanel searchIcon = new ImagePanel(Constants.IconNames.SEARCH_BLACK, 24, 24);
		Dimension searchBarSize = new Dimension(400, 40);
		searchBar = new TextFieldPanel("Search", searchIcon, IconPosition.LEADING, searchBarSize);
		searchBar.setBounds(0, 0, searchBarSize.width, searchBarSize.height);
		topBarPanel.add(searchBar);

		searchButton = new JButton("Search");
		searchButton.setBounds(412, 0, 115, 40);
		UtilFunctions.configureTopBarButtonOnMainThread(searchButton);
		topBarPanel.add(searchButton);

		// Top Bar Buttons Panel.
		JPanel topBarButtonsPanel = new JPanel();
		topBarButtonsPanel.setBounds(764, 0, 274, 40);
		topBarButtonsPanel.setLayout(new FlowLayout(FlowLayout.TRAILING, 0, 0));
		topBarPanel.add(topBarButtonsPanel);

		// Add Button.
		addButton = new JButton("Add");
		addButton.setPreferredSize(new Dimension(85, 40));
		UtilFunctions.configureTopBarButtonOnMainThread(addButton);
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

			initMoreButtonMenu();
		}
	}

	private void initMoreButtonMenu() {
		JPopupMenu moreButtonMenu = new JPopupMenu();

		// Filter Menu Item.
		filterMenuItem = new JMenuItem("Show Filter");
		filterMenuItem.addActionListener((event) -> {
			setVisibleForFilterBar(!filterBarPanel.isVisible());
		});
		moreButtonMenu.add(filterMenuItem);

		// Update Rules Menu Item.
		updateRulesMenuItem = new JMenuItem("Update Rules");
		moreButtonMenu.add(updateRulesMenuItem);

		// Add an action listener for More button to show menu popup.
		moreButton.addActionListener((event) -> {
			Point location = moreButton.getLocationOnScreen();
			moreButtonMenu.show(this, 0, 0);
			moreButtonMenu.setLocation(location.x, location.y + moreButton.getHeight());
		});
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
		filterCriterionPanel.setBounds(0, 0, 202, 40);
		filterCriterionPanel.setLayout(new FlowLayout(FlowLayout.TRAILING, 0, 0));
		filterBarPanel.add(filterCriterionPanel);

		// Room Status Combo Box.
		roomStatusComboBox = new JComboBox<>();
		roomStatusComboBox.setPreferredSize(new Dimension(90, 40));
		roomStatusComboBox.setFont(Constants.Fonts.BODY);
		filterCriterionPanel.add(roomStatusComboBox);
		filterCriterionPanel.add(Box.createHorizontalStrut(12));

		// Room Type Combo Box.
		roomTypeComboBox = new JComboBox<>();
		roomTypeComboBox.setPreferredSize(new Dimension(100, 40));
		roomTypeComboBox.setFont(Constants.Fonts.BODY);
		filterCriterionPanel.add(roomTypeComboBox);
//		filterCriterionPanel.add(Box.createHorizontalStrut(12));

//		// Range Date Picker.
//		ImagePanel calendarIcon = new ImagePanel(Constants.IconNames.CALENDAR_MONTH_BLACK, 24, 24);
//		Dimension rangeDatePickerSize = new Dimension(205, 40);
//		rangeDatePicker = new TextFieldPanel("", calendarIcon, IconPosition.TRAILING, rangeDatePickerSize);
//		rangeDatePicker.getTextField().setEditable(false);
//		filterCriterionPanel.add(rangeDatePicker);
//		filterCriterionPanel.add(Box.createHorizontalStrut(12));

		// Range Price Picker.
//		ImagePanel filterIcon = new ImagePanel(Constants.IconNames.FILTER_ALT_BLACK, 24, 24);
//		Dimension rangePriceInputSize = new Dimension(150, 40);
//		rangePriceInput = new TextFieldPanel("", filterIcon, IconPosition.TRAILING, rangePriceInputSize);
//		rangePriceInput.getTextField().setEditable(false);
//		rangePriceInput.getTextField().setHorizontalAlignment(SwingConstants.CENTER);
//		filterCriterionPanel.add(rangePriceInput);

		// Sort Criterion Combo Box.
		sortCriterionComboBox = new JComboBox<>();
		sortCriterionComboBox.setBounds(918, 0, 120, 40);
		sortCriterionComboBox.setFont(Constants.Fonts.BODY);
		filterBarPanel.add(sortCriterionComboBox);
	}

	private void initTable() {
		final String[] columnNames = {"", "id", "Room name", "Room type", "Price ($)", "Status", "Note", "room_type_id"};
		final int[] columnWidths = {40, 0, 200, 100, 70, 90, 518, 0};
		final int[] columnHorizontalAlignments = {
				DefaultTableCellRenderer.CENTER,
				DefaultTableCellRenderer.LEFT,
				DefaultTableCellRenderer.LEFT,
				DefaultTableCellRenderer.LEFT,
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
	}

	public void setVisibleForFilterBar(boolean visible) {
		String filterMenuItemTitle = visible ? "Hide Filter" : "Show Filter";
		filterMenuItem.setText(filterMenuItemTitle);

		int yTable = visible ? 140 : 80;
		int heightTable = visible ? 622 : 682;
		scrollableTable.setBounds(20, yTable, 1038, heightTable);
		filterBarPanel.setVisible(visible);
	}

	public TextFieldPanel getSearchBar() {
		return searchBar;
	}

	public JButton getSearchButton() {
		return searchButton;
	}

	public JButton getAddButton() {
		return addButton;
	}

	public JButton getRemoveButton() {
		return removeButton;
	}

	public JMenuItem getUpdateRulesMenuItem() {
		return updateRulesMenuItem;
	}

	public ScrollableTablePanel getScrollableTable() {
		return scrollableTable;
	}

	public JComboBox<String> getRoomStatusComboBox() {
		return roomStatusComboBox;
	}

	public JComboBox<String> getRoomTypeComboBox() {
		return roomTypeComboBox;
	}

//	public TextFieldPanel getRangeDatePicker() {
//		return rangeDatePicker;
//	}

//	public TextFieldPanel getRangePriceInput() {
//		return rangePriceInput;
//	}

	public JComboBox<String> getSortCriterionComboBox() {
		return sortCriterionComboBox;
	}

}
