package views.components.table_model;

import javax.swing.table.DefaultTableModel;
import java.util.Vector;

public class NonEditableTableModel extends DefaultTableModel {

	private boolean isPreventedUpdateNoColumn = false;

	public NonEditableTableModel(Object[][] data, Object[] columnNames) {
		super(data, columnNames);
	}

	public NonEditableTableModel(Object[] columnNames, int rowCount) {
		super(columnNames, rowCount);
	}

	public NonEditableTableModel(Vector<?> columnNames, int rowCount) {
		super(columnNames, rowCount);
	}

	public NonEditableTableModel(Vector<? extends Vector> data, Vector<?> columnNames) {
		super(data, columnNames);
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}

	public Vector<Object> getRowValue(int row) {
		Vector<Object> rowValue = new Vector<>();

		for (int i = 0; i < getColumnCount(); i++)
			rowValue.add(getValueAt(row, i));

		return rowValue;
	}

	@Override
	public void removeRow(int row) {
		super.removeRow(row);

		// Update `No.` column if possible
		if (!isPreventedUpdateNoColumn) {
			for (int i = row; i < getRowCount(); i++) {
				setValueAt(i + 1, i, 0);
			}
		}
	}

	public void removeAllRows() {
		int rowCount = getRowCount();

		isPreventedUpdateNoColumn = true;
		while (rowCount > 0) {
			removeRow(0);
			--rowCount;
		}
		isPreventedUpdateNoColumn = false;
	}

}
