package views.panels.employees;

import utils.UtilFunctions;
import views.components.panels.ScrollableTablePanel;
import views.components.table_model.NonEditableTableModel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class EmployeeManagementPanel extends JPanel {

	public static final int HIDDEN_COLUMN_USER_ID = 1;

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
				"id",
				"Employee name",
				"Username",
				"Gender",
				"Year of birth",
		};
		final int [] columnWidths = {40, 0, 560, 250, 70, 100};
		final int[] columnHorizontalAlignments = {
				DefaultTableCellRenderer.CENTER,
				DefaultTableCellRenderer.LEFT,
				DefaultTableCellRenderer.LEFT,
				DefaultTableCellRenderer.LEFT,
				DefaultTableCellRenderer.LEFT,
				DefaultTableCellRenderer.CENTER,
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
