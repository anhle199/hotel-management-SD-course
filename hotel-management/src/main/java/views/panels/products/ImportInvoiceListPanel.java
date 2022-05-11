package views.panels.products;

import utils.UtilFunctions;
import views.components.panels.ScrollableTablePanel;
import views.components.table_model.NonEditableTableModel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class ImportInvoiceListPanel extends JPanel {

	public static final int HIDDEN_COLUMN_INVOICE_ID = 1;

	// Top Bar.
	private JButton addButton;

	private ScrollableTablePanel scrollableTable;

	public ImportInvoiceListPanel() {
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
		final String[] columnNames = {"", "id", "Imported date", "Total price ($)", "Note"};
		final int [] columnWidths = {40, 0, 150, 100, 730};
		final int[] columnHorizontalAlignments = {
				DefaultTableCellRenderer.CENTER,
				DefaultTableCellRenderer.LEFT,
				DefaultTableCellRenderer.CENTER,
				DefaultTableCellRenderer.RIGHT,
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

	public JButton getAddButton() {
		return addButton;
	}

	public ScrollableTablePanel getScrollableTable() {
		return scrollableTable;
	}

}
