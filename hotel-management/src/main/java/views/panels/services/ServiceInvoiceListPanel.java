package views.panels.services;

import utils.UtilFunctions;
import views.components.panels.ScrollableTablePanel;
import views.components.table_model.NonEditableTableModel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class ServiceInvoiceListPanel extends JPanel {

	public static final int HIDDEN_COLUMN_SERVICE_INVOICE_ID = 1;
	public static final int HIDDEN_COLUMN_ROOM_ID = 9;
	public static final int HIDDEN_COLUMN_SERVICE_ID = 10;

	// Top Bar.
	private JButton addButton;
	private JButton removeButton;

	private ScrollableTablePanel scrollableTable;

	public ServiceInvoiceListPanel() {
		super();
		setLayout(null);

		initTopBarPanel();
		initTable();
	}

	private void initTopBarPanel() {
		JPanel topBarPanel = new JPanel();
		topBarPanel.setBounds(20, 20, 1038, 40);
		topBarPanel.setLayout(null);
		add(topBarPanel);

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
				"Room name",
				"Service name",
				"Price",
				"Number of customer",
				"Total price",
				"Time used",
				"Note",
				"room_id",
				"service_id"
		};
		final int [] columnWidths = {50, 0, 122, 127, 112, 167, 166, 129, 146, 0, 0};
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
