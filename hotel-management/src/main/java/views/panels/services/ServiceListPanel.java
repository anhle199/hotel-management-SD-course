package views.panels.services;

import utils.Constants;
import utils.UtilFunctions;
import views.components.panels.ImagePanel;
import views.components.panels.ScrollableTablePanel;
import views.components.panels.TextFieldPanel;
import views.components.table_model.NonEditableTableModel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class ServiceListPanel extends JPanel {

	public static final int HIDDE_COLUMN_SERVICE_ID = 1;

	// Top Bar
	private TextFieldPanel searchBar;
	private JButton searchButton;
	private JButton addButton;
	private JButton removeButton;

	private ScrollableTablePanel scrollableTable;

	public ServiceListPanel() {
		super();
		setLayout(null);

		initTopBarPanel();
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
		searchBar = new TextFieldPanel("Search", searchIcon, TextFieldPanel.IconPosition.LEADING, searchBarSize);
		searchBar.setBounds(0, 0, searchBarSize.width, searchBarSize.height);
		topBarPanel.add(searchBar);

		searchButton = new JButton("Search");
		searchButton.setBounds(412, 0, 115, 40);
		UtilFunctions.configureTopBarButtonOnMainThread(searchButton);
		topBarPanel.add(searchButton);

		// Top Bar Buttons Panel.
		JPanel topBarButtonsPanel = new JPanel();
		topBarButtonsPanel.setBounds(826, 0, 212, 40);
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
	}

	private void initTable() {
		final String[] columnNames = {
				"",  // no
				"id",
				"Service name",
				"Price ($)",
				"Description",
				"Note"
		};
		final int [] columnWidths = {40, 0, 150, 80, 380, 369};
		final int[] columnHorizontalAlignments = {
				DefaultTableCellRenderer.CENTER,
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

	public ScrollableTablePanel getScrollableTable() {
		return scrollableTable;
	}

}
