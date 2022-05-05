package views.panels.rooms;

import utils.UtilFunctions;
import views.components.panels.ScrollableTablePanel;
import views.components.table_model.NonEditableTableModel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class RentalInvoiceListPanel extends JPanel {

	public static final int HIDDEN_COLUMN_RENTAL_INVOICE_ID = 1;
	public static final int HIDDEN_COLUMN_ROOM_ID = 2;
	public static final int HIDDEN_COLUMN_ROOM_TYPE_ID = 11;
	public static final int HIDDEN_COLUMN_ROOM_TYPE_NAME = 12;
	public static final int HIDDEN_COLUMN_ROOM_TYPE_PRICE = 13;

	// Top Bar.
	private JButton payButton;
	private JButton addButton;
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

		// Pay Button.
		payButton = new JButton("Pay");
		payButton.setBounds(729, 0, 85, 40);
		UtilFunctions.configureTopBarButtonOnMainThread(payButton);
		topBarPanel.add(payButton);

		// Add Button.
		addButton = new JButton("Add");
		addButton.setBounds(826, 0, 85, 40);
		UtilFunctions.configureTopBarButtonOnMainThread(addButton);
		topBarPanel.add(addButton);

		// Remove Button.
		removeButton = new JButton("Remove");
		removeButton.setBounds(923, 0, 115, 40);
		UtilFunctions.configureTopBarButtonOnMainThread(removeButton);
		topBarPanel.add(removeButton);
	}

	private void initTable() {
		final String[] columnNames = {
				"",  // no
				"id",
				"room_id",
				"Room name",
				"Start date",
				"End date",
				"Status",
				"Customer name",
				"Customer type",
				"Identifier number",
				"Address",
				"room_type_id",
				"room_type_name",
				"room_type_price",
		};
		final int [] columnWidths = {40, 0, 0, 200, 145, 145, 120, 200, 120, 150, 200, 0, 0, 0};
		final int[] columnHorizontalAlignments = {
				DefaultTableCellRenderer.CENTER,
				DefaultTableCellRenderer.LEFT,
				DefaultTableCellRenderer.LEFT,
				DefaultTableCellRenderer.LEFT,
				DefaultTableCellRenderer.LEFT,
				DefaultTableCellRenderer.LEFT,
				DefaultTableCellRenderer.LEFT,
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

	public JButton getPayButton() {
		return payButton;
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
