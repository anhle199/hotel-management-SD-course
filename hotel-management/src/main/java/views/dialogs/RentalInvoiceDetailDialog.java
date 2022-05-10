package views.dialogs;

import utils.Constants;
import utils.DetailDialogModeEnum;
import utils.UtilFunctions;
import views.components.panels.DateChooserPanel;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.NumberFormat;

public class RentalInvoiceDetailDialog extends JDialog {

	private DetailDialogModeEnum viewMode;

	private JComboBox<String> roomNameComboBox;
	private JFormattedTextField priceTextField;
	private DateChooserPanel rentingStartDatePicker;
	private DateChooserPanel rentingEndDatePicker;
	private JTextField customerNameTextField;
	private JComboBox<String> customerTypeComboBox;
	private JTextField identifierNumberTextField;
	private JTextField addressTextField;
	private JButton positiveButton;
	private JButton negativeButton;

	public RentalInvoiceDetailDialog(JFrame frame, DetailDialogModeEnum mode) {
		super(frame, "Rental Invoice Detail", true);
		this.viewMode = mode;

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setPreferredSize(new Dimension(540, 504));
		initSubviews(panel);

		setResizable(false);
		setContentPane(panel);
		pack();
		setLocationRelativeTo(null);
	}

	private void initSubviews(JPanel panel) {
		// Sizes and coordinates
		Dimension labelSize = new Dimension(120, 40);
		Dimension textFieldSize = new Dimension(360, 40);
		int padding = 20;
		int spacingTextFields = 12;
		int xTextField = padding * 2 + labelSize.width;

		boolean fieldEditable = viewMode.getFieldEditable();

		// Room Name Label.
		JLabel roomNameLabel = new JLabel("Room name");
		roomNameLabel.setBounds(padding, padding, labelSize.width, labelSize.height);
		panel.add(roomNameLabel);

		// Room Name Combo Box.
		roomNameComboBox = new JComboBox<>();
		roomNameComboBox.setBounds(xTextField, roomNameLabel.getY(), textFieldSize.width, textFieldSize.height);
		roomNameComboBox.setEnabled(fieldEditable);
		panel.add(roomNameComboBox);

		// Number formatter without grouping separator
		NumberFormat numberFormat = NumberFormat.getIntegerInstance();
		numberFormat.setGroupingUsed(false);

		// Number formatter
		NumberFormatter priceFormatter = new NumberFormatter(numberFormat);
		priceFormatter.setMinimum(Constants.MIN_PRICE);
		priceFormatter.setMaximum(Constants.MAX_PRICE);
		priceFormatter.setAllowsInvalid(false);
		priceFormatter.setCommitsOnValidEdit(true);

		// Room Name Label.
		JLabel priceLabel = new JLabel("Total price ($)");
		priceLabel.setBounds(padding, roomNameLabel.getY() + labelSize.height + spacingTextFields, labelSize.width, labelSize.height);
		panel.add(priceLabel);

		// Room Name Combo Box.
		priceTextField = new JFormattedTextField(priceFormatter);
		priceTextField.setBounds(xTextField, priceLabel.getY(), textFieldSize.width, textFieldSize.height);
		priceTextField.setEnabled(false);
		panel.add(priceTextField);

		// Renting Start Date Label.
		JLabel rentingStartDateLabel = new JLabel("Renting start date");
		rentingStartDateLabel.setBounds(padding, priceLabel.getY() + labelSize.height + spacingTextFields, labelSize.width, labelSize.height);
		panel.add(rentingStartDateLabel);

		// Range Date Picker.
		rentingStartDatePicker = new DateChooserPanel(2022, 2023);
		rentingStartDatePicker.setBounds(xTextField, priceLabel.getY() + labelSize.height + spacingTextFields, textFieldSize.width, textFieldSize.height);
		rentingStartDatePicker.setEnabledSelection(fieldEditable);
		panel.add(rentingStartDatePicker);

		// Renting End Date Label.
		JLabel rentingEndDateLabel = new JLabel("Renting end date");
		rentingEndDateLabel.setBounds(padding, rentingStartDateLabel.getY() + labelSize.height + spacingTextFields, labelSize.width, labelSize.height);
		panel.add(rentingEndDateLabel);

		// Range Date Picker.
		rentingEndDatePicker = new DateChooserPanel(2022, 2023);
		rentingEndDatePicker.setBounds(xTextField, rentingEndDateLabel.getY(), textFieldSize.width, textFieldSize.height);
		rentingEndDatePicker.setEnabledSelection(fieldEditable);
		panel.add(rentingEndDatePicker);

		// Customer Name Label.
		JLabel customerNameLabel = new JLabel("Customer name");
		customerNameLabel.setBounds(padding, rentingEndDateLabel.getY() + labelSize.height + spacingTextFields, labelSize.width, labelSize.height);
		panel.add(customerNameLabel);

		// Customer Name Text Field.
		customerNameTextField = new JTextField();
		customerNameTextField.setBounds(xTextField, customerNameLabel.getY(), textFieldSize.width, textFieldSize.height);
		UtilFunctions.configureDialogTextFieldOnMainThread(customerNameTextField);
		customerNameTextField.setEnabled(fieldEditable);
		panel.add(customerNameTextField);

		// Customer Type Label.
		JLabel customerTypeLabel = new JLabel("Customer type");
		customerTypeLabel.setBounds(padding, customerNameLabel.getY() + labelSize.height + spacingTextFields, labelSize.width, labelSize.height);
		panel.add(customerTypeLabel);

		// Customer Type Combo Box.
		customerTypeComboBox = new JComboBox<>();
		customerTypeComboBox.setBounds(xTextField, customerTypeLabel.getY(), textFieldSize.width, textFieldSize.height);
		customerTypeComboBox.setEnabled(fieldEditable);
		panel.add(customerTypeComboBox);

		// Identifier Label.
		JLabel identifierNumberLabel = new JLabel("Identifier number");
		identifierNumberLabel.setBounds(padding, customerTypeLabel.getY() + labelSize.height + spacingTextFields, labelSize.width, labelSize.height);
		panel.add(identifierNumberLabel);

		// Identifier Text Field.
		identifierNumberTextField = new JTextField();
		identifierNumberTextField.setBounds(xTextField, identifierNumberLabel.getY(), textFieldSize.width, textFieldSize.height);
		identifierNumberTextField.setEnabled(fieldEditable);
		UtilFunctions.configureDialogTextFieldOnMainThread(identifierNumberTextField);
		panel.add(identifierNumberTextField);

		// Address Label.
		JLabel addressLabel = new JLabel("Address");
		addressLabel.setBounds(padding, identifierNumberLabel.getY() + labelSize.height + spacingTextFields, labelSize.width, labelSize.height);
		panel.add(addressLabel);

		// Address Text Field.
		addressTextField = new JTextField();
		addressTextField.setBounds(xTextField, addressLabel.getY(), textFieldSize.width, textFieldSize.height);
		addressTextField.setEnabled(fieldEditable);
		UtilFunctions.configureDialogTextFieldOnMainThread(addressTextField);
		panel.add(addressTextField);

		// Edit Button.
		positiveButton = new JButton(viewMode.getPositiveButtonTitle());
		positiveButton.setBounds(xTextField, addressLabel.getY() + addressTextField.getHeight() + padding, 100, textFieldSize.height);
		UtilFunctions.configureTopBarButtonOnMainThread(positiveButton);
		panel.add(positiveButton);

		// Close Button.
		negativeButton = new JButton(viewMode.getNegativeButtonTitle());
		negativeButton.setBounds(positiveButton.getX() + positiveButton.getWidth() + padding, positiveButton.getY(), 100, textFieldSize.height);
		UtilFunctions.configureBorderedButtonOnMainThread(negativeButton);
		UtilFunctions.addHoverEffectsForBorderedButton(negativeButton);
		panel.add(negativeButton);
	}

	public void setViewMode(DetailDialogModeEnum viewMode) {
		this.viewMode = viewMode;

		boolean fieldEditable = viewMode.getFieldEditable();

		roomNameComboBox.setEnabled(fieldEditable);
		priceTextField.setEnabled(false);
		rentingStartDatePicker.setEnabledSelection(fieldEditable);
		rentingEndDatePicker.setEnabledSelection(fieldEditable);
		customerNameTextField.setEnabled(fieldEditable);
		customerTypeComboBox.setEnabled(fieldEditable);
		identifierNumberTextField.setEnabled(fieldEditable);
		addressTextField.setEnabled(fieldEditable);
		positiveButton.setText(viewMode.getPositiveButtonTitle());
		negativeButton.setText(viewMode.getNegativeButtonTitle());
	}

	public JComboBox<String> getRoomNameComboBox() {
		return roomNameComboBox;
	}

	public JFormattedTextField getPriceTextField() {
		return priceTextField;
	}

	public DateChooserPanel getRentingStartDatePicker() {
		return rentingStartDatePicker;
	}

	public DateChooserPanel getRentingEndDatePicker() {
		return rentingEndDatePicker;
	}

	public JTextField getCustomerNameTextField() {
		return customerNameTextField;
	}

	public JComboBox<String> getCustomerTypeComboBox() {
		return customerTypeComboBox;
	}

	public JTextField getIdentifierNumberTextField() {
		return identifierNumberTextField;
	}

	public JTextField getAddressTextField() {
		return addressTextField;
	}

	public JButton getPositiveButton() {
		return positiveButton;
	}

	public JButton getNegativeButton() {
		return negativeButton;
	}
}
