package views.panels;

import shared.NonEditableTableModel;
import shared.panels.ImagePanel;
import shared.panels.ScrollableTablePanel;
import shared.panels.TextFieldPanel;
import utils.Constants;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class EmployeeManagementPanel extends JPanel {
	// Top Bar
//	private TextFieldPanel searchBar;
	private JButton addButton;
	private JButton removeButton;

	private ScrollableTablePanel scrollableTable;

	public EmployeeManagementPanel() {
		super();
		setLayout(null);

		initTopBarPanel();
		initTable();
	}

	private void initTopBarPanel() {
		JPanel topBarPanel = new JPanel();
		topBarPanel.setBounds(0, 20, 1078, 44);
		topBarPanel.setLayout(null);
		add(topBarPanel);

//		ImagePanel searchIcon = new ImagePanel(Constants.IconNames.SEARCH, 24, 24);
//		Dimension searchBarSize = new Dimension(600, 44);
//		searchBar = new TextFieldPanel("Search", searchIcon, TextFieldPanel.IconPosition.LEADING, searchBarSize);
//		searchBar.setBounds(20, 0, searchBarSize.width, searchBarSize.height);
//		topBarPanel.add(searchBar);

		addButton = new JButton("Add");
		addButton.setBounds(886, 0, 60, 44);
		addButton.setFocusPainted(false);
		addButton.setRolloverEnabled(false);
		addButton.setForeground(Constants.Colors.WHITE);
		addButton.setBackground(Constants.Colors.TERTIARY);
		topBarPanel.add(addButton);

		removeButton = new JButton("Remove");
		removeButton.setBounds(958, 0, 100, 44);
		removeButton.setFocusPainted(false);
		removeButton.setRolloverEnabled(false);
		removeButton.setForeground(Constants.Colors.WHITE);
		removeButton.setBackground(Constants.Colors.RED);
		topBarPanel.add(removeButton);
	}

	private void initTable() {
		final String[] columnNames = {
				"",  // no
				"Employee name",
				"Username",
				"Gender",
				"Birthdaty (year)"
		};
		final int [] columnWidths = {50, 243, 243, 242, 242};
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
		scrollableTable.setBounds(20, 84, 1038, 700);

		NonEditableTableModel model = (NonEditableTableModel) scrollableTable.getTableModel();
		model.addRow(new Object[]{1, "Employee name", "Username", "Gender", "Birthdaty (year)"});
		model.addRow(new Object[]{2, "Employee name", "Username", "Gender", "Birthdaty (year)"});
		model.addRow(new Object[]{3, "Employee name", "Username", "Gender", "Birthdaty (year)"});
		model.addRow(new Object[]{4, "Employee name", "Username", "Gender", "Birthdaty (year)"});
		model.addRow(new Object[]{5, "Employee name", "Username", "Gender", "Birthdaty (year)"});
	}
}
