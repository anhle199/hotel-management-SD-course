package views.panels.employees;

import shared.NonEditableTableModel;
import shared.panels.ImagePanel;
import shared.panels.ScrollableTablePanel;
import shared.panels.TextFieldPanel;
import utils.Constants;
import utils.UtilFunctions;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class EmployeeManagementPanel extends JPanel {
	// Top Bar.
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
		topBarPanel.setBounds(20, 20, 1040, 40);
		topBarPanel.setLayout(null);
		add(topBarPanel);

		addButton = new JButton("Add");
		addButton.setBounds(828, 0, 85, 40);
		UtilFunctions.configureTopBarButtonOnMainThread(addButton);
		topBarPanel.add(addButton);

		removeButton = new JButton("Remove");
		removeButton.setBounds(925, 0, 115, 40);
		UtilFunctions.configureTopBarButtonOnMainThread(removeButton);
		topBarPanel.add(removeButton);
	}

	private void initTable() {
		final String[] columnNames = {
				"",  // no
				"Employee name",
				"Username",
				"Gender",
				"Birthday (year)",
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

		scrollableTable.setHeaderSize(new Dimension(tableWidth, 40));
		scrollableTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		scrollableTable.setBounds(20, 80, 1040, 707);

		NonEditableTableModel model = (NonEditableTableModel) scrollableTable.getTableModel();
		model.addRow(new Object[]{1, "Employee name", "Username", "Gender", "Birthday (year)"});
		model.addRow(new Object[]{2, "Employee name", "Username", "Gender", "Birthday (year)"});
		model.addRow(new Object[]{3, "Employee name", "Username", "Gender", "Birthday (year)"});
		model.addRow(new Object[]{4, "Employee name", "Username", "Gender", "Birthday (year)"});
		model.addRow(new Object[]{5, "Employee name", "Username", "Gender", "Birthday (year)"});
	}
}
