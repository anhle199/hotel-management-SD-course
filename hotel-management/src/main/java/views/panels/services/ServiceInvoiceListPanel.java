package views.panels.services;

import utils.UtilFunctions;
import views.components.table_model.NonEditableTableModel;
import views.components.panels.ScrollableTablePanel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class ServiceInvoiceListPanel extends JPanel {
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
				"Room",
				"Service type",
				"Price",
				"Number of customer",
				"Total price",
				"Time used",
				"Notes"
		};
		final int [] columnWidths = {50, 122, 127, 112, 167, 166, 129, 146};
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
		model.addRow(new Object[]{1, "Room", "Service type", "Price", "Number of customer", "Total price", "Time used", "Notes"});
		model.addRow(new Object[]{2, "Room", "Service type", "Price", "Number of customer", "Total price", "Time used", "Notes"});
		model.addRow(new Object[]{3, "Room", "Service type", "Price", "Number of customer", "Total price", "Time used", "Notes"});
		model.addRow(new Object[]{4, "Room", "Service type", "Price", "Number of customer", "Total price", "Time used", "Notes"});
		model.addRow(new Object[]{5, "Room", "Service type", "Price", "Number of customer", "Total price", "Time used", "Notes"});
	}
}
