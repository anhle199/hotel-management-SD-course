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

public class ImportInvoiceDetailDialog extends JDialog {

	public static final int HIDDEN_COLUMN_PRODUCT_ID = 1;

	private DetailDialogModeEnum viewMode;

	private JTextField importDateTextField;
	private DateChooserPanel importedDatePanel;
	private JTextField totalPriceTextField;
	private JTextField noteTextField;
	private JButton addButton;
	private JButton removeButton;
	private ScrollableTablePanel scrollableTable;
	private JButton createButton;
	private JButton cancelButton;

	public ImportInvoiceDetailDialog(JFrame frame, DetailDialogModeEnum mode) {
		super(frame, "Import Invoice Detail", true);
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
		Dimension leftLabelSize = new Dimension(100, 40);
		Dimension rightLabelSize = new Dimension(90, 40);
		Dimension textFieldSize = new Dimension(340, 40);
		int padding = 20;
		int spacingTextFields = 12;
		int xTextField = padding * 2 + leftLabelSize.width;

		// Imported Date Label.
		JLabel importedDateLabel = new JLabel("Import date");
		importedDateLabel.setBounds(padding, padding, leftLabelSize.width, leftLabelSize.height);
		panel.add(importedDateLabel);

		if (viewMode == DetailDialogModeEnum.CREATE) {
			// Imported Date Panel.
			importedDatePanel = new DateChooserPanel(2022, 2023);
			importedDatePanel.setBounds(xTextField, importedDateLabel.getY(), textFieldSize.width, textFieldSize.height);
			panel.add(importedDatePanel);
		} else {
			// Imported Date Text Field.
			importDateTextField = new JTextField();
			importDateTextField.setBounds(xTextField, importedDateLabel.getY(), textFieldSize.width, textFieldSize.height);
			UtilFunctions.configureDialogTextFieldOnMainThread(importDateTextField);
			panel.add(importDateTextField);
		}


		// Total Price Label.
		JLabel totalPriceLabel = new JLabel("Total price ($)");
		totalPriceLabel.setBounds(
				importedDateLabel.getX() + leftLabelSize.width + 20 + textFieldSize.width + 50,
				importedDateLabel.getY(),
				rightLabelSize.width,
				rightLabelSize.height
		);
		panel.add(totalPriceLabel);

		// Total Price Text Field.
		totalPriceTextField = new JTextField(String.valueOf(Constants.MIN_PRICE));
		totalPriceTextField.setBounds(
				totalPriceLabel.getX() + rightLabelSize.width + padding,
				totalPriceLabel.getY(),
				textFieldSize.width,
				textFieldSize.height
		);
		totalPriceTextField.setHorizontalAlignment(SwingConstants.RIGHT);
		UtilFunctions.configureDialogTextFieldOnMainThread(totalPriceTextField);
		panel.add(totalPriceTextField);

		// Note Label.
		JLabel noteLabel = new JLabel("Note");
		noteLabel.setBounds(
				importedDateLabel.getX(),
				importedDateLabel.getY() + leftLabelSize.height + spacingTextFields,
				leftLabelSize.width,
				leftLabelSize.height
		);
		panel.add(noteLabel);

		// Note Text Field.
		noteTextField = new JTextField();
		noteTextField.setBounds(
				noteLabel.getX() + leftLabelSize.width + padding,
				noteLabel.getY(),
				(textFieldSize.width * 2) + 40 + leftLabelSize.width + 20,
				textFieldSize.height
		);
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
		final String[] columnNames = {"", "productId", "Product name", "Product type", "Price ($)", "Quantity"};
		final int [] columnWidths = {40, 0, 645, 95, 80, 80};
		final int[] columnHorizontalAlignments = {
				DefaultTableCellRenderer.CENTER,
				DefaultTableCellRenderer.LEFT,
				DefaultTableCellRenderer.LEFT,
				DefaultTableCellRenderer.LEFT,
				DefaultTableCellRenderer.RIGHT,
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

		totalPriceTextField.setEnabled(false);
		noteTextField.setEnabled(viewMode.getFieldEditable());

		if (viewMode == DetailDialogModeEnum.CREATE) {
			importedDatePanel.setEnabled(true);
		} else {
			importDateTextField.setEnabled(false);
		}
	}

	public JTextField getImportDateTextField() {
		return importDateTextField;
	}

	public DateChooserPanel getImportedDatePanel() {
		return importedDatePanel;
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
