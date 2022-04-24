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
	// Top Bar
	private TextFieldPanel searchBar;
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
		Dimension searchBarSize = new Dimension(500, 40);
		searchBar = new TextFieldPanel("Search", searchIcon, TextFieldPanel.IconPosition.LEADING, searchBarSize);
		searchBar.setBounds(0, 0, searchBarSize.width, searchBarSize.height);
		topBarPanel.add(searchBar);

		// Remove Button.
		removeButton = new JButton("Remove");
		removeButton.setBounds(923, 0, 115, 40);
		UtilFunctions.configureTopBarButtonOnMainThread(removeButton);
		topBarPanel.add(removeButton);
	}

	private void initTable() {
		final String[] columnNames = {
				"",  // no
				"Service type",
				"Description",
				"Price",
				"Notes"
		};
		final int [] columnWidths = {50, 241, 242, 242, 244};
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
		model.addRow(new Object[]{1, "Service type", "Description", "Price", "Notes"});
		model.addRow(new Object[]{2, "Service type", "Description", "Price", "Notes"});
		model.addRow(new Object[]{3, "Service type", "Description", "Price", "Notes"});
		model.addRow(new Object[]{4, "Service type", "Description", "Price", "Notes"});
		model.addRow(new Object[]{5, "Service type", "Description", "Price", "Notes"});
	}
}
