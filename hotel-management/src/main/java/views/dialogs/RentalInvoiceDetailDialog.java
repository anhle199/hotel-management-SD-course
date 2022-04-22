package views.dialogs;

import utils.Constants;
import utils.UtilFunctions;
import views.components.panels.ImagePanel;
import views.components.panels.TextFieldPanel;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.NumberFormat;

public class RentalInvoiceDetailDialog extends JDialog {

	// Components for basic information panel.
	private JComboBox<String> roomNameComboBox;
	private TextFieldPanel rentingStartDatePicker;
	private JTextField customerNameTextField;
	private JComboBox<String> customerTypeComboBox;
	private JTextField identifierTextField;
	private JTextField addressTextField;
	private JButton editButton;
	private JButton closeButton;

	public RentalInvoiceDetailDialog(JFrame frame) {
		super(frame, "Rental Invoice Detail", true);

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setPreferredSize(new Dimension(510, 400));
		initSubviews(panel);

		setResizable(false);
		setContentPane(panel);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void initSubviews(JPanel panel) {
		// Sizes and coordinates
		Dimension labelSize = new Dimension(105, 40);
		Dimension textFieldSize = new Dimension(345, 40);
		int padding = 20;
		int spacingTextFields = 12;
		int xTextField = padding * 2 + labelSize.width;

		// Room Name Label.
		JLabel roomNameLabel = new JLabel("Room name");
		roomNameLabel.setBounds(padding, padding, labelSize.width, labelSize.height);
		panel.add(roomNameLabel);

		// Room Name Combo Box.
		roomNameComboBox = new JComboBox<>();
		roomNameComboBox.setBounds(xTextField, roomNameLabel.getY(), textFieldSize.width, textFieldSize.height);
		roomNameComboBox.setEnabled(false);
		panel.add(roomNameComboBox);

		// Renting Start Date Label.
		JLabel rentingStartDateLabel = new JLabel("Renting start date");
		rentingStartDateLabel.setBounds(padding, roomNameLabel.getY() + labelSize.height + spacingTextFields, labelSize.width, labelSize.height);
		panel.add(rentingStartDateLabel);

		// Range Date Picker.
		ImagePanel calendarIcon = new ImagePanel(Constants.IconNames.CALENDAR_MONTH_BLACK, 24, 24);
		Dimension rangeDatePickerSize = new Dimension(textFieldSize.width, textFieldSize.height);
		rentingStartDatePicker = new TextFieldPanel("", calendarIcon, TextFieldPanel.IconPosition.TRAILING, rangeDatePickerSize);
		rentingStartDatePicker.setBounds(xTextField, roomNameLabel.getY() + labelSize.height + spacingTextFields, textFieldSize.width, textFieldSize.height);
		rentingStartDatePicker.getTextField().setEditable(false);
		rentingStartDatePicker.getTextField().setText("31/12/2022 - 31/12/2022");
		panel.add(rentingStartDatePicker);

		// Customer Name Label.
		JLabel customerNameLabel = new JLabel("Customer name");
		customerNameLabel.setBounds(padding, rentingStartDateLabel.getY() + labelSize.height + spacingTextFields, labelSize.width, labelSize.height);
		panel.add(customerNameLabel);

		// Customer Name Text Field.
		customerNameTextField = new JTextField();
		customerNameTextField.setBounds(xTextField, customerNameLabel.getY(), textFieldSize.width, textFieldSize.height);
		UtilFunctions.configureDialogTextFieldOnMainThread(customerNameTextField);
		customerNameTextField.setEnabled(false);
		panel.add(customerNameTextField);

		// Customer Type Label.
		JLabel customerTypeLabel = new JLabel("Customer type");
		customerTypeLabel.setBounds(padding, customerNameLabel.getY() + labelSize.height + spacingTextFields, labelSize.width, labelSize.height);
		panel.add(customerTypeLabel);

		// Customer Type Combo Box.
		customerTypeComboBox = new JComboBox<>(Constants.CUSTOMER_TYPES);
		customerTypeComboBox.setBounds(xTextField, customerTypeLabel.getY(), textFieldSize.width, textFieldSize.height);
		customerTypeComboBox.setEnabled(false);
		panel.add(customerTypeComboBox);

		// Identifier Label.
		JLabel identifierLabel = new JLabel("Identifier");
		identifierLabel.setBounds(padding, customerTypeLabel.getY() + labelSize.height + spacingTextFields, labelSize.width, labelSize.height);
		panel.add(identifierLabel);

		// Identifier Text Field.
		identifierTextField = new JTextField();
		identifierTextField.setBounds(xTextField, identifierLabel.getY(), textFieldSize.width, textFieldSize.height);
		identifierTextField.setEnabled(false);
		UtilFunctions.configureDialogTextFieldOnMainThread(identifierTextField);
		panel.add(identifierTextField);

		// Address Label.
		JLabel addressLabel = new JLabel("Address");
		addressLabel.setBounds(padding, identifierLabel.getY() + labelSize.height + spacingTextFields, labelSize.width, labelSize.height);
		panel.add(addressLabel);

		// Address Text Field.
		addressTextField = new JTextField();
		addressTextField.setBounds(xTextField, addressLabel.getY(), textFieldSize.width, textFieldSize.height);
		addressTextField.setEnabled(false);
		UtilFunctions.configureDialogTextFieldOnMainThread(addressTextField);
		panel.add(addressTextField);

		// Edit Button.
		editButton = new JButton("Edit");
		editButton.setBounds(xTextField, addressLabel.getY() + addressTextField.getHeight() + padding, 100, textFieldSize.height);
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
