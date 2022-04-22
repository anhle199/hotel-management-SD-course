package views.dialogs;

import utils.Constants;
import utils.UtilFunctions;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.NumberFormat;

public class ProductDetailDialog extends JDialog {

	// Components for basic information panel.
	private JTextField productNameTextField;
	private JComboBox<String> productTypeComboBox;
	private JFormattedTextField priceTextField;
	private JTextField quantityTextField;
	private JTextArea noteTextArea;
	private JButton editButton;
	private JButton closeButton;

	public ProductDetailDialog(JFrame frame) {
		super(frame, "Product Detail", true);

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setPreferredSize(new Dimension(460, 408));
		initSubviews(panel);

		setResizable(false);
		setContentPane(panel);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void initSubviews(JPanel panel) {
		// Sizes and coordinates
		Dimension labelSize = new Dimension(80, 40);
		Dimension textFieldSize = new Dimension(320, 40);
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
		productNameTextField.setEnabled(false);
		panel.add(productNameTextField);

		// Product Type Label.
		JLabel productTypeLabel = new JLabel("Product type");
		productTypeLabel.setBounds(padding, productNameLabel.getY() + labelSize.height + spacingTextFields, labelSize.width, labelSize.height);
		panel.add(productTypeLabel);

		// Product Type Combo Box.
		productTypeComboBox = new JComboBox<>(Constants.PRODUCT_TYPES);
		productTypeComboBox.setBounds(xTextField, productTypeLabel.getY(), textFieldSize.width, textFieldSize.height);
		productTypeComboBox.setEnabled(false);
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
		priceTextField.setEnabled(false);
		priceTextField.setValue(Constants.MIN_PRICE);
		priceTextField.setHorizontalAlignment(SwingConstants.RIGHT);
		UtilFunctions.configureDialogTextFieldOnMainThread(priceTextField);
		panel.add(priceTextField);

		// Quantity Label.
		JLabel quantityLabel = new JLabel("Quantity");
		quantityLabel.setBounds(padding, priceLabel.getY() + labelSize.height + spacingTextFields, labelSize.width, labelSize.height);
		panel.add(quantityLabel);

		// Quantity Text Field.
		quantityTextField = new JTextField();
		quantityTextField.setBounds(xTextField, quantityLabel.getY(), textFieldSize.width, textFieldSize.height);
		UtilFunctions.configureDialogTextFieldOnMainThread(quantityTextField);
		quantityTextField.setEnabled(false);
		panel.add(quantityTextField);

		// Note Label.
		JLabel noteLabel = new JLabel("Notes");
		noteLabel.setBounds(padding, quantityLabel.getY() + labelSize.height + spacingTextFields, labelSize.width, labelSize.height);
		panel.add(noteLabel);

		// Note Text Field.
		noteTextArea = new JTextArea();
		noteTextArea.setBounds(xTextField, noteLabel.getY(), textFieldSize.width, 100);
		noteTextArea.setEnabled(false);
		UtilFunctions.configureDialogTextFieldOnMainThread(noteTextArea);
		panel.add(noteTextArea);

		// Edit Button.
		editButton = new JButton("Edit");
		editButton.setBounds(xTextField, noteLabel.getY() + noteTextArea.getHeight() + padding, 100, textFieldSize.height);
		UtilFunctions.configureTopBarButtonOnMainThread(editButton);
		panel.add(editButton);

		// Close Button.
		closeButton = new JButton("Close");
		closeButton.setBounds(editButton.getX() + editButton.getWidth() + padding, editButton.getY(), 100, textFieldSize.height);
		UtilFunctions.configureBorderedButtonOnMainThread(closeButton);
		UtilFunctions.addHoverEffectsForBorderedButton(closeButton);
		panel.add(closeButton);
	}

}
