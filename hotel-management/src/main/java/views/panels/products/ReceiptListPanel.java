package views.panels.products;

import utils.UtilFunctions;
import views.components.table_model.NonEditableTableModel;
import views.components.panels.ScrollableTablePanel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class ReceiptListPanel extends JPanel {
	// Top Bar.
	private JButton addButton;

	private ScrollableTablePanel scrollableTable;

	public ReceiptListPanel() {
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

		// Add Button.
		addButton = new JButton("Add");
		addButton.setBounds(953, 0, 85, 40);
		UtilFunctions.configureTopBarButtonOnMainThread(addButton);
		topBarPanel.add(addButton);
	}

	private void initTable() {
		final String[] columnNames = {
				"",  // no
				"Product name",
				"Product type",
				"Purchased date",
				"Price",
				"Quantity",
				"Total price",
				"Notes",
		};
		final int [] columnWidths = {50, 140, 140, 140, 140, 140, 140, 130};
		final int[] columnHorizontalAlignments = {
				DefaultTableCellRenderer.CENTER,
				DefaultTableCellRenderer.LEFT,
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
		model.addRow(new Object[]{1, "Product name", "Product type", "Purchased date", "Price", "Quantity", "Total price", "Notes"});
		model.addRow(new Object[]{2, "Product name", "Product type", "Purchased date", "Price", "Quantity", "Total price", "Notes"});
		model.addRow(new Object[]{3, "Product name", "Product type", "Purchased date", "Price", "Quantity", "Total price", "Notes"});
		model.addRow(new Object[]{4, "Product name", "Product type", "Purchased date", "Price", "Quantity", "Total price", "Notes"});
		model.addRow(new Object[]{5, "Product name", "Product type", "Purchased date", "Price", "Quantity", "Total price", "Notes"});
	}
}
