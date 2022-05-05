package views.dialogs;

import utils.Constants;
import utils.UtilFunctions;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.NumberFormat;

public class AddProductToImportInvoiceDialog extends JDialog {

	private JComboBox<String> productNameComboBox;
	private JFormattedTextField quantityTextField;
	private JFormattedTextField priceTextField;
	private JTextField totalPriceTextField;
	private JButton addButton;
	private JButton cancelButton;

	public AddProductToImportInvoiceDialog(JFrame frame) {
		super(frame, "Add Product To Import Invoice", true);

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setPreferredSize(new Dimension(500, 296));
		initSubviews(panel);

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

		// Product Name Label.
		JLabel productNameLabel = new JLabel("Product name");
		productNameLabel.setBounds(padding, padding, labelSize.width, labelSize.height);
		panel.add(productNameLabel);

		// Product Name Text Field.
		productNameComboBox = new JComboBox<>();
		productNameComboBox.setBounds(xTextField, productNameLabel.getY(), textFieldSize.width, textFieldSize.height);
		productNameComboBox.setEnabled(false);
		panel.add(productNameComboBox);

		// Quantity Label.
		JLabel quantityLabel = new JLabel("Quantity");
		quantityLabel.setBounds(padding, productNameLabel.getY() + labelSize.height + spacingTextFields, labelSize.width, labelSize.height);
		panel.add(quantityLabel);

		// Number formatter without grouping separator
		NumberFormat numberFormat = NumberFormat.getIntegerInstance();
		numberFormat.setGroupingUsed(false);

		// Quantity formatter
		NumberFormatter quantityFormatter = new NumberFormatter(numberFormat);
		quantityFormatter.setMinimum(Constants.MIN_QUANTITY);
		quantityFormatter.setMaximum(Constants.MAX_QUANTITY);
		quantityFormatter.setAllowsInvalid(false);
		quantityFormatter.setCommitsOnValidEdit(true);

		// Quantity Text Field.
		quantityTextField = new JFormattedTextField(quantityFormatter);
		quantityTextField.setBounds(xTextField, quantityLabel.getY(), textFieldSize.width, textFieldSize.height);
		quantityTextField.setEnabled(false);
		panel.add(quantityTextField);

		// Price Label.
		JLabel priceLabel = new JLabel("Price ($)");
		priceLabel.setBounds(padding, quantityLabel.getY() + labelSize.height + spacingTextFields, labelSize.width, labelSize.height);
		panel.add(priceLabel);

		// Price formatter
		NumberFormatter priceFormatter = new NumberFormatter(numberFormat);
		priceFormatter.setMinimum(Constants.MIN_PRICE);
		priceFormatter.setMaximum(Constants.MAX_PRICE);
		priceFormatter.setAllowsInvalid(false);
		priceFormatter.setCommitsOnValidEdit(true);

		// Price Text Field.
		priceTextField = new JFormattedTextField(priceFormatter);
		priceTextField.setBounds(xTextField, priceLabel.getY(), textFieldSize.width, textFieldSize.height);
		totalPriceTextField.setHorizontalAlignment(SwingConstants.RIGHT);
		priceTextField.setEnabled(false);
		panel.add(priceTextField);

		// Total Price Label.
		JLabel totalPriceLabel = new JLabel("Total price ($)");
		totalPriceLabel.setBounds(padding, priceTextField.getY() + labelSize.height + spacingTextFields, labelSize.width, labelSize.height);
		panel.add(totalPriceLabel);

		// Total Price Text Field.
		totalPriceTextField = new JTextField();
		totalPriceTextField.setBounds(xTextField, totalPriceLabel.getY(), textFieldSize.width, textFieldSize.height);
		totalPriceTextField.setHorizontalAlignment(SwingConstants.RIGHT);
		totalPriceTextField.setEnabled(false);
		panel.add(totalPriceTextField);

		// Add Button.
		addButton = new JButton("Add");
		addButton.setBounds(xTextField, totalPriceLabel.getY() + totalPriceTextField.getHeight() + padding, 100, textFieldSize.height);
		UtilFunctions.configureTopBarButtonOnMainThread(addButton);
		panel.add(addButton);

		// Cancel Button.
		cancelButton = new JButton("Cancel");
		cancelButton.setBounds(addButton.getX() + addButton.getWidth() + padding, addButton.getY(), 100, textFieldSize.height);
		UtilFunctions.configureBorderedButtonOnMainThread(cancelButton);
		UtilFunctions.addHoverEffectsForBorderedButton(cancelButton);
		panel.add(cancelButton);
	}

	public JComboBox<String> getProductNameComboBox() {
		return productNameComboBox;
	}

	public JFormattedTextField getQuantityTextField() {
		return quantityTextField;
	}

	public JFormattedTextField getPriceTextField() {
		return priceTextField;
	}

	public JTextField getTotalPriceTextField() {
		return totalPriceTextField;
	}

	public JButton getAddButton() {
		return addButton;
	}

	public JButton getCancelButton() {
		return cancelButton;
	}

}
