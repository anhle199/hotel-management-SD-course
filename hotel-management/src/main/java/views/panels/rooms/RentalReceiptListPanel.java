package views.panels.rooms;

import shared.NonEditableTableModel;
import shared.panels.ScrollableTablePanel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class RentalReceiptListPanel extends JPanel {

	private ScrollableTablePanel scrollableTable;

	public RentalReceiptListPanel() {
		super();
		setLayout(null);

		initTable();
	}

	private void initTable() {
		final String[] columnNames = {
				"",  // no
				"Room name",
				"Rented days",
				"Price",
				"Total price",
		};
		final int [] columnWidths = {40, 560, 120, 150, 150};
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
		scrollableTable.setBounds(20, 20, 1038, 742);

		NonEditableTableModel model = (NonEditableTableModel) scrollableTable.getTableModel();
		model.addRow(new Object[]{1, "Room name", "Rented days", "Price", "Total price"});
		model.addRow(new Object[]{2, "Room name", "Rented days", "Price", "Total price"});
		model.addRow(new Object[]{3, "Room name", "Rented days", "Price", "Total price"});
		model.addRow(new Object[]{4, "Room name", "Rented days", "Price", "Total price"});
		model.addRow(new Object[]{5, "Room name", "Rented days", "Price", "Total price"});
	}

}
