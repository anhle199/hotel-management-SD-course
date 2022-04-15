package views.panels.rooms;

import shared.NonEditableTableModel;
import shared.panels.ScrollableTablePanel;
import utils.UtilFunctions;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class RentalInvoiceListPanel extends JPanel {

	// Top Bar.
	private JButton removeButton;

	private ScrollableTablePanel scrollableTable;

	public RentalInvoiceListPanel() {
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

		// Remove Button.
		removeButton = new JButton("Remove");
		removeButton.setBounds(923, 0, 115, 40);
		UtilFunctions.configureTopBarButtonOnMainThread(removeButton);
		topBarPanel.add(removeButton);
	}

	private void initTable() {
		final String[] columnNames = {
				"",  // no
				"Room name",
				"Renting start date",
				"Customer name",
				"Customer type",
				"Identifier number",
				"Address"
		};
		final int [] columnWidths = {40, 300, 120, 200, 120, 150, 200};
		final int[] columnHorizontalAlignments = {
				DefaultTableCellRenderer.CENTER,
				DefaultTableCellRenderer.LEFT,
				DefaultTableCellRenderer.LEFT,
				DefaultTableCellRenderer.LEFT,
				DefaultTableCellRenderer.LEFT,
				DefaultTableCellRenderer.LEFT,
				DefaultTableCellRenderer.LEFT
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
		model.addRow(new Object[]{1, "Room name", "Renting start date", "Customer name", "Customer type", "Identifier Number", "Address"});
		model.addRow(new Object[]{2, "Room name", "Renting start date", "Customer name", "Customer type", "Identifier Number", "Address"});
		model.addRow(new Object[]{3, "Room name", "Renting start date", "Customer name", "Customer type", "Identifier Number", "Address"});
		model.addRow(new Object[]{4, "Room name", "Renting start date", "Customer name", "Customer type", "Identifier Number", "Address"});
		model.addRow(new Object[]{5, "Room name", "Renting start date", "Customer name", "Customer type", "Identifier Number", "Address"});
	}

}
