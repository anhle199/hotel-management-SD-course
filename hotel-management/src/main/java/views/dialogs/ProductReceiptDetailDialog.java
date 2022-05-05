package views.dialogs;

import utils.Constants;
import utils.DetailDialogModeEnum;
import utils.UtilFunctions;
import views.components.panels.DateChooserPanel;
import views.components.panels.ScrollableTablePanel;
import views.components.table_model.NonEditableTableModel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class ProductReceiptDetailDialog extends JDialog {

	public static final int HIDDEN_COLUMN_PRODUCT_ID = 1;

	private DetailDialogModeEnum viewMode;

	private JTextField customerNameTextField;
	private JTextField purchasedDateTextField;
	private DateChooserPanel purchasedDatePanel;
	private JTextField totalPriceTextField;
	private JTextField noteTextField;
	private JButton addButton;
	private JButton removeButton;
	private ScrollableTablePanel scrollableTable;
	private JButton createButton;
	private JButton cancelButton;

	public ProductReceiptDetailDialog(JFrame frame, DetailDialogModeEnum mode) {
		super(frame, "Product Receipt Detail", true);
		this.viewMode = mode;

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setPreferredSize(new Dimension(1000, 600));

		initSubviews(panel);
		setViewMode(viewMode);

		setResizable(false);
		setContentPane(panel);
		pack();
		setLocationRelativeTo(null);
	}

	private void initSubviews(JPanel panel) {
		// Sizes and coordinates
		Dimension labelSize = new Dimension(100, 40);
		Dimension textFieldSize = new Dimension(340, 40);
		int padding = 20;
		int spacingTextFields = 12;
		int xTextField = padding * 2 + labelSize.width;

		// Customer Name Label.
		JLabel customerNameLabel = new JLabel("Customer name");
		customerNameLabel.setBounds(padding, padding, labelSize.width, labelSize.height);
		panel.add(customerNameLabel);

		// Customer Name Text Field.
		customerNameTextField = new JTextField();
		customerNameTextField.setBounds(xTextField, customerNameLabel.getY(), textFieldSize.width, textFieldSize.height);
		UtilFunctions.configureDialogTextFieldOnMainThread(customerNameTextField);
		panel.add(customerNameTextField);

		// Product Type Label.
		JLabel purchasedDateLabel = new JLabel("Purchased date");
		purchasedDateLabel.setBounds(padding, customerNameLabel.getY() + labelSize.height + spacingTextFields, labelSize.width, labelSize.height);
		panel.add(purchasedDateLabel);

		if (viewMode == DetailDialogModeEnum.CREATE) {
			// Purchased Date Panel.
			purchasedDatePanel = new DateChooserPanel(2022, 2023);
			purchasedDatePanel.setBounds(xTextField, purchasedDateLabel.getY(), textFieldSize.width, textFieldSize.height);
			panel.add(purchasedDatePanel);
		} else {
			// Purchased Date Text Field.
			purchasedDateTextField = new JTextField();
			purchasedDateTextField.setBounds(xTextField, purchasedDateLabel.getY(), textFieldSize.width, textFieldSize.height);
			UtilFunctions.configureDialogTextFieldOnMainThread(purchasedDateTextField);
			panel.add(purchasedDateTextField);
		}

		// Total Price Label.
		JLabel totalPriceLabel = new JLabel("Total price ($)");
		totalPriceLabel.setBounds(customerNameTextField.getX() + textFieldSize.width + 40, customerNameLabel.getY(), labelSize.width, labelSize.height);
		panel.add(totalPriceLabel);

		// Total Price Text Field.
		totalPriceTextField = new JFormattedTextField();
		totalPriceTextField.setBounds(totalPriceLabel.getX() + labelSize.width + padding, totalPriceLabel.getY(), textFieldSize.width, textFieldSize.height);
		UtilFunctions.configureDialogTextFieldOnMainThread(totalPriceTextField);
		panel.add(totalPriceTextField);

		// Note Label.
		JLabel noteLabel = new JLabel("Note");
		noteLabel.setBounds(totalPriceLabel.getX(), totalPriceLabel.getY() + labelSize.height + spacingTextFields, labelSize.width, labelSize.height);
		panel.add(noteLabel);

		// Note Text Field.
		noteTextField = new JTextField();
		noteTextField.setBounds(noteLabel.getX() + labelSize.width + padding, noteLabel.getY(), textFieldSize.width, textFieldSize.height);
		UtilFunctions.configureDialogTextFieldOnMainThread(noteTextField);
		panel.add(noteTextField);

		if (viewMode == DetailDialogModeEnum.CREATE) {
			JPanel divider = new JPanel();
			divider.setBounds(20, 132, 960, 1);
			divider.setBackground(Constants.Colors.LIGHT_GRAY);
			panel.add(divider);

			// Add Button.
			addButton = new JButton("Add");
			addButton.setBounds(765, 153, 85, textFieldSize.height);
			UtilFunctions.configureTopBarButtonOnMainThread(addButton);
			panel.add(addButton);

			// Remove Button.
			removeButton = new JButton("Remove");
			removeButton.setBounds(870, 153, 110, textFieldSize.height);
			UtilFunctions.configureTopBarButtonOnMainThread(removeButton);
			panel.add(removeButton);
		}

		initTable(panel);

		if (viewMode == DetailDialogModeEnum.CREATE) {
			// Create Button.
			createButton = new JButton(viewMode.getPositiveButtonTitle());
			createButton.setBounds(390, 540, 100, textFieldSize.height);
			UtilFunctions.configureTopBarButtonOnMainThread(createButton);
			panel.add(createButton);

			// Cancel Button.
			cancelButton = new JButton(viewMode.getNegativeButtonTitle());
			cancelButton.setBounds(510, 540, 100, textFieldSize.height);
			UtilFunctions.configureBorderedButtonOnMainThread(cancelButton);
			UtilFunctions.addHoverEffectsForBorderedButton(cancelButton);
			panel.add(cancelButton);
		}
	}

	private void initTable(JPanel panel) {
		final String[] columnNames = {
				"",  // no
				"productId",
				"Product name",
				"Product type",
				"Price",
				"Quantity",
		};
		final int [] columnWidths = {50, 0, 305, 195, 195, 195};
		final int[] columnHorizontalAlignments = {
				DefaultTableCellRenderer.CENTER,
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
		panel.add(scrollableTable);

		final int tableWidth = scrollableTable.getTableWidth();

		scrollableTable.setHeaderSize(new Dimension(tableWidth, 40));
		scrollableTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		int yTable = viewMode == DetailDialogModeEnum.CREATE ? 213 : 132;
		int heightTable = viewMode == DetailDialogModeEnum.CREATE ? 307 : 448;
		scrollableTable.setBounds(20, yTable, 960, heightTable);
	}

	private void setViewMode(DetailDialogModeEnum viewMode) {
		this.viewMode = viewMode;

		boolean fieldEditable = viewMode.getFieldEditable();
		customerNameTextField.setEnabled(fieldEditable);
		totalPriceTextField.setEnabled(false);
		noteTextField.setEnabled(fieldEditable);

		if (viewMode == DetailDialogModeEnum.CREATE) {
			purchasedDatePanel.setEnabled(true);
		} else {
			purchasedDateTextField.setEnabled(false);
		}
	}

	public JTextField getCustomerNameTextField() {
		return customerNameTextField;
	}

	public JTextField getPurchasedDateTextField() {
		return purchasedDateTextField;
	}

	public DateChooserPanel getPurchasedDatePanel() {
		return purchasedDatePanel;
	}

	public JTextField getTotalPriceTextField() {
		return totalPriceTextField;
	}

	public JTextField getNoteTextField() {
		return noteTextField;
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

	public JButton getCreateButton() {
		return createButton;
	}

	public JButton getCancelButton() {
		return cancelButton;
	}

}
