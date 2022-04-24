package views.dialogs;

import utils.Constants;
import utils.UtilFunctions;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.NumberFormat;

public class ServiceInvoiceDetailDialog extends JDialog {

	// Components for basic information panel.
	private JComboBox<String> roomNameComboBox;
	private JComboBox<String> serviceNameComboBox;
	private JFormattedTextField priceTextField;
	private JTextField numberOfCustomersTextField;
	private JFormattedTextField totalPriceTextField;
	private JTextField timeUsedTextField;
	private JTextArea noteTextArea;
	private JButton editButton;
	private JButton closeButton;

	public ServiceInvoiceDetailDialog(JFrame frame) {
		super(frame, "Service Invoice Detail", true);

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setPreferredSize(new Dimension(550, 512));
		initSubviews(panel);

		setResizable(false);
		setContentPane(panel);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void initSubviews(JPanel panel) {
		// Sizes and coordinates
		Dimension labelSize = new Dimension(125, 40);
		Dimension textFieldSize = new Dimension(365, 40);
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

		// Service Name Label.
		JLabel serviceNameLabel = new JLabel("Service name");
		serviceNameLabel.setBounds(padding, roomNameLabel.getY() + labelSize.height + spacingTextFields, labelSize.width, labelSize.height);
		panel.add(serviceNameLabel);

		// Service Name Combo Box.
		serviceNameComboBox = new JComboBox<>();
		serviceNameComboBox.setBounds(xTextField, serviceNameLabel.getY(), textFieldSize.width, textFieldSize.height);
		serviceNameComboBox.setEnabled(false);
		panel.add(serviceNameComboBox);

		// Price Label.
		JLabel priceLabel = new JLabel("Price ($)");
		priceLabel.setBounds(padding, serviceNameLabel.getY() + labelSize.height + spacingTextFields, labelSize.width, labelSize.height);
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

		// Number Of Customers Label.
		JLabel numberOfCustomersLabel = new JLabel("Number of customers");
		numberOfCustomersLabel.setBounds(padding, priceLabel.getY() + labelSize.height + spacingTextFields, labelSize.width, labelSize.height);
		panel.add(numberOfCustomersLabel);

		// Number Of Customers Text Field.
		numberOfCustomersTextField = new JTextField();
		numberOfCustomersTextField.setBounds(xTextField, numberOfCustomersLabel.getY(), textFieldSize.width, textFieldSize.height);
		numberOfCustomersTextField.setEnabled(false);
		UtilFunctions.configureDialogTextFieldOnMainThread(numberOfCustomersTextField);
		panel.add(numberOfCustomersTextField);

		// Total Price Label.
		JLabel totalPriceLabel = new JLabel("Total price ($)");
		totalPriceLabel.setBounds(padding, numberOfCustomersLabel.getY() + labelSize.height + spacingTextFields, labelSize.width, labelSize.height);
		panel.add(totalPriceLabel);

		// Total Price Text Field.
		totalPriceTextField = new JFormattedTextField(priceFormatter);
		totalPriceTextField.setBounds(xTextField, totalPriceLabel.getY(), textFieldSize.width, textFieldSize.height);
		totalPriceTextField.setEnabled(false);
		totalPriceTextField.setValue(Constants.MIN_PRICE);
		totalPriceTextField.setHorizontalAlignment(SwingConstants.RIGHT);
		UtilFunctions.configureDialogTextFieldOnMainThread(totalPriceTextField);
		panel.add(totalPriceTextField);

		// Time Used Label.
		JLabel timeUsedLabel = new JLabel("Time used");
		timeUsedLabel.setBounds(padding, totalPriceLabel.getY() + labelSize.height + spacingTextFields, labelSize.width, labelSize.height);
		panel.add(timeUsedLabel);

		// Time Used Text Field.
		timeUsedTextField = new JTextField();
		timeUsedTextField.setBounds(xTextField, timeUsedLabel.getY(), textFieldSize.width, textFieldSize.height);
		timeUsedTextField.setEnabled(false);
		UtilFunctions.configureDialogTextFieldOnMainThread(timeUsedTextField);
		panel.add(timeUsedTextField);

		// Note Label.
		JLabel noteLabel = new JLabel("Notes");
		noteLabel.setBounds(padding, timeUsedLabel.getY() + labelSize.height + spacingTextFields, labelSize.width, labelSize.height);
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
