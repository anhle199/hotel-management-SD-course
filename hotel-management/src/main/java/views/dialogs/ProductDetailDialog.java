package views.dialogs;

import utils.Constants;
import utils.DetailDialogModeEnum;
import utils.UtilFunctions;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.NumberFormat;

public class ProductDetailDialog extends JDialog {

	private DetailDialogModeEnum viewMode;

	private JTextField productNameTextField;
	private JComboBox<String> productTypeComboBox;
	private JFormattedTextField priceTextField;
	private JFormattedTextField quantityTextField;
	private JTextArea descriptionTextArea;
	private JButton positiveButton;
	private JButton negativeButton;

	public ProductDetailDialog(JFrame frame, DetailDialogModeEnum mode) {
		super(frame, "Product Detail", true);
		this.viewMode = mode;

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setPreferredSize(new Dimension(480, 408));

		initSubviews(panel);
		setViewMode(viewMode);

		setResizable(false);
		setContentPane(panel);
		pack();
		setLocationRelativeTo(null);
	}

	private void initSubviews(JPanel panel) {
		// Sizes and coordinates
		Dimension labelSize = new Dimension(90, 40);
		Dimension textFieldSize = new Dimension(330, 40);
		int padding = 20;
		int spacingTextFields = 12;
		int xTextField = padding * 2 + labelSize.width;

		// Product Name Label.
		JLabel productNameLabel = new JLabel("Product name");
		productNameLabel.setBounds(padding, padding, labelSize.width, labelSize.height);
		panel.add(productNameLabel);

		// Product Name Text Field.
		productNameTextField = new JTextField();
		productNameTextField.setBounds(xTextField, productNameLabel.getY(), textFieldSize.width, textFieldSize.height);
		UtilFunctions.configureDialogTextFieldOnMainThread(productNameTextField);
		panel.add(productNameTextField);

		// Product Type Label.
		JLabel productTypeLabel = new JLabel("Product type");
		productTypeLabel.setBounds(padding, productNameLabel.getY() + labelSize.height + spacingTextFields, labelSize.width, labelSize.height);
		panel.add(productTypeLabel);

		// Product Type Combo Box.
		productTypeComboBox = new JComboBox<>();
		productTypeComboBox.setBounds(xTextField, productTypeLabel.getY(), textFieldSize.width, textFieldSize.height);
		panel.add(productTypeComboBox);

		// Price Label.
		JLabel priceLabel = new JLabel("Price ($)");
		priceLabel.setBounds(padding, productTypeLabel.getY() + labelSize.height + spacingTextFields, labelSize.width, labelSize.height);
		panel.add(priceLabel);

		// Number formatter without grouping separator
		NumberFormat numberFormat = NumberFormat.getIntegerInstance();
		numberFormat.setGroupingUsed(false);

		// Number formatter
		NumberFormatter priceFormatter = new NumberFormatter(numberFormat);
		priceFormatter.setMinimum(Constants.MIN_PRICE);
		priceFormatter.setMaximum(Constants.MAX_PRICE);
		priceFormatter.setAllowsInvalid(false);
		priceFormatter.setCommitsOnValidEdit(true);

		// Price Text Field.
		priceTextField = new JFormattedTextField(priceFormatter);
		priceTextField.setBounds(xTextField, priceLabel.getY(), textFieldSize.width, textFieldSize.height);
		priceTextField.setValue(Constants.MIN_PRICE);
		priceTextField.setHorizontalAlignment(SwingConstants.RIGHT);
		UtilFunctions.configureDialogTextFieldOnMainThread(priceTextField);
		panel.add(priceTextField);

		// Quantity Label.
		JLabel quantityLabel = new JLabel("Quantity");
		quantityLabel.setBounds(padding, priceLabel.getY() + labelSize.height + spacingTextFields, labelSize.width, labelSize.height);
		panel.add(quantityLabel);

		// Number formatter
		NumberFormatter quantityFormatter = new NumberFormatter(numberFormat);
		quantityFormatter.setMinimum(Constants.MIN_QUANTITY_IN_ADDING_PRODUCT);
		quantityFormatter.setMaximum(Constants.MAX_QUANTITY);
		quantityFormatter.setAllowsInvalid(false);
		quantityFormatter.setCommitsOnValidEdit(true);

		// Quantity Text Field.
		quantityTextField = new JFormattedTextField(quantityFormatter);
		quantityTextField.setBounds(xTextField, quantityLabel.getY(), textFieldSize.width, textFieldSize.height);
		quantityTextField.setValue(Constants.MIN_QUANTITY_IN_ADDING_PRODUCT);
		quantityTextField.setHorizontalAlignment(SwingConstants.RIGHT);
		UtilFunctions.configureDialogTextFieldOnMainThread(quantityTextField);
		panel.add(quantityTextField);

		// Description Label.
		JLabel descriptionLabel = new JLabel("Description");
		descriptionLabel.setBounds(padding, quantityLabel.getY() + labelSize.height + spacingTextFields, labelSize.width, labelSize.height);
		panel.add(descriptionLabel);

		// Description Text Field.
		descriptionTextArea = new JTextArea();
		descriptionTextArea.setBounds(xTextField, descriptionLabel.getY(), textFieldSize.width, 100);
		descriptionTextArea.setLineWrap(true);
		descriptionTextArea.setWrapStyleWord(true);
		UtilFunctions.configureDialogTextFieldOnMainThread(descriptionTextArea);
		panel.add(descriptionTextArea);

		// Positive Button.
		positiveButton = new JButton(viewMode.getPositiveButtonTitle());
		positiveButton.setBounds(xTextField, descriptionLabel.getY() + descriptionTextArea.getHeight() + padding, 100, textFieldSize.height);
		UtilFunctions.configureTopBarButtonOnMainThread(positiveButton);
		panel.add(positiveButton);

		// Negative Button.
		negativeButton = new JButton(viewMode.getNegativeButtonTitle());
		negativeButton.setBounds(positiveButton.getX() + positiveButton.getWidth() + padding, positiveButton.getY(), 100, textFieldSize.height);
		UtilFunctions.configureBorderedButtonOnMainThread(negativeButton);
		UtilFunctions.addHoverEffectsForBorderedButton(negativeButton);
		panel.add(negativeButton);
	}

	public void setViewMode(DetailDialogModeEnum viewMode) {
		this.viewMode = viewMode;

		boolean fieldEditable = viewMode.getFieldEditable();
		productNameTextField.setEnabled(fieldEditable);
		productTypeComboBox.setEnabled(fieldEditable);
		priceTextField.setEnabled(fieldEditable);
		quantityTextField.setEnabled(false);
		descriptionTextArea.setEnabled(fieldEditable);

		positiveButton.setText(viewMode.getPositiveButtonTitle());
		negativeButton.setText(viewMode.getNegativeButtonTitle());
	}

	public JTextField getProductNameTextField() {
		return productNameTextField;
	}

	public JComboBox<String> getProductTypeComboBox() {
		return productTypeComboBox;
	}

	public JFormattedTextField getPriceTextField() {
		return priceTextField;
	}

	public JFormattedTextField getQuantityTextField() {
		return quantityTextField;
	}

	public JTextArea getDescriptionTextArea() {
		return descriptionTextArea;
	}

	public JButton getPositiveButton() {
		return positiveButton;
	}

	public JButton getNegativeButton() {
		return negativeButton;
	}

}
