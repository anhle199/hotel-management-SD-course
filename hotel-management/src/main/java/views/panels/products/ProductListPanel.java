package views.panels.products;

import utils.Constants;
import utils.UtilFunctions;
import views.components.table_model.NonEditableTableModel;
import views.components.panels.ImagePanel;
import views.components.panels.ScrollableTablePanel;
import views.components.panels.TextFieldPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class ProductListPanel extends JPanel {
	// Top Bar
	private TextFieldPanel searchBar;
	private JButton addButton;
	private JButton removeButton;

	private ScrollableTablePanel scrollableTable;

	public ProductListPanel() {
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
				"Product name",
				"Product type",
				"Price",
				"Quantity",
				"Notes"
		};
		final int [] columnWidths = {50, 195, 195, 195, 195, 190};
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
		scrollableTable.setBounds(20, 84, 1038, 680);

		NonEditableTableModel model = (NonEditableTableModel) scrollableTable.getTableModel();
		model.addRow(new Object[]{1, "Product name", "Product type", "Price", "Quantity", "Notes"});
		model.addRow(new Object[]{2, "Product name", "Product type", "Price", "Quantity", "Notes"});
		model.addRow(new Object[]{3, "Product name", "Product type", "Price", "Quantity", "Notes"});
		model.addRow(new Object[]{4, "Product name", "Product type", "Price", "Quantity", "Notes"});
		model.addRow(new Object[]{5, "Product name", "Product type", "Price", "Quantity", "Notes"});
	}
}
