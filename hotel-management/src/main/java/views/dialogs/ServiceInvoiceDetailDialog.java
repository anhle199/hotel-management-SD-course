package views.dialogs;

import utils.Constants;
import utils.DetailDialogModeEnum;
import utils.UtilFunctions;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.NumberFormat;

public class ServiceInvoiceDetailDialog extends JDialog {

	private DetailDialogModeEnum viewMode;

	private JComboBox<String> roomNameComboBox;
	private JComboBox<String> serviceNameComboBox;
	private JFormattedTextField priceTextField;
	private JTextField numberOfCustomersTextField;
	private JFormattedTextField totalPriceTextField;
	private JFormattedTextField timeUsedTextField;
	private JTextArea noteTextArea;
	private JButton positiveButton;
	private JButton negativeButton;

	public ServiceInvoiceDetailDialog(JFrame frame) {
		this(frame, DetailDialogModeEnum.VIEW_ONLY);
	}

	public ServiceInvoiceDetailDialog(JFrame frame, DetailDialogModeEnum mode) {
		super(frame, "Service Invoice Detail", true);
		this.viewMode = mode;

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setPreferredSize(new Dimension(550, 512));
		initSubviews(panel);

		setResizable(false);
		setContentPane(panel);
		pack();
		setLocationRelativeTo(null);
	}

	private void initSubviews(JPanel panel) {
		// Sizes and coordinates
		Dimension labelSize = new Dimension(125, 40);
		Dimension textFieldSize = new Dimension(365, 40);
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

		// Service Name Label.
		JLabel serviceNameLabel = new JLabel("Service name");
		serviceNameLabel.setBounds(padding, roomNameLabel.getY() + labelSize.height + spacingTextFields, labelSize.width, labelSize.height);
		panel.add(serviceNameLabel);

		// Service Name Combo Box.
		serviceNameComboBox = new JComboBox<>();
		serviceNameComboBox.setBounds(xTextField, serviceNameLabel.getY(), textFieldSize.width, textFieldSize.height);
		serviceNameComboBox.setEnabled(fieldEditable);
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
		priceFormatter.setMinimum(Constants.MIN_CUSTOMERS);
		priceFormatter.setMaximum(Constants.MAX_CUSTOMERS);
		priceFormatter.setAllowsInvalid(false);
		priceFormatter.setCommitsOnValidEdit(true);

		// Price Text Field.
		priceTextField = new JFormattedTextField(priceFormatter);
		priceTextField.setBounds(xTextField, priceLabel.getY(), textFieldSize.width, textFieldSize.height);
		priceTextField.setEnabled(fieldEditable);
		priceTextField.setValue(Constants.MIN_CUSTOMERS);
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
		numberOfCustomersTextField.setEnabled(fieldEditable);
		UtilFunctions.configureDialogTextFieldOnMainThread(numberOfCustomersTextField);
		panel.add(numberOfCustomersTextField);

		// Total Price Label.
		JLabel totalPriceLabel = new JLabel("Total price ($)");
		totalPriceLabel.setBounds(padding, numberOfCustomersLabel.getY() + labelSize.height + spacingTextFields, labelSize.width, labelSize.height);
		panel.add(totalPriceLabel);

		// Total Price Text Field.
		totalPriceTextField = new JFormattedTextField(priceFormatter);
		totalPriceTextField.setBounds(xTextField, totalPriceLabel.getY(), textFieldSize.width, textFieldSize.height);
		totalPriceTextField.setEnabled(fieldEditable);
		totalPriceTextField.setValue(Constants.MIN_PRICE);
		totalPriceTextField.setHorizontalAlignment(SwingConstants.RIGHT);
		UtilFunctions.configureDialogTextFieldOnMainThread(totalPriceTextField);
		panel.add(totalPriceTextField);

		// Time Used Label.
		JLabel timeUsedLabel = new JLabel("Time used");
		timeUsedLabel.setBounds(padding, totalPriceLabel.getY() + labelSize.height + spacingTextFields, labelSize.width, labelSize.height);
		panel.add(timeUsedLabel);

		// Number formatter
		NumberFormatter numberFormatter = new NumberFormatter(numberFormat);
		numberFormatter.setMinimum(Constants.MIN_TIME_USED);
		numberFormatter.setMaximum(Constants.MAX_TIME_USED);
		numberFormatter.setAllowsInvalid(false);
		numberFormatter.setCommitsOnValidEdit(true);

		// Time Used Text Field.
		timeUsedTextField = new JFormattedTextField(numberFormatter);
		timeUsedTextField.setBounds(xTextField, timeUsedLabel.getY(), textFieldSize.width, textFieldSize.height);
		timeUsedTextField.setEnabled(fieldEditable);
		UtilFunctions.configureDialogTextFieldOnMainThread(timeUsedTextField);
		panel.add(timeUsedTextField);

		// Note Label.
		JLabel noteLabel = new JLabel("Note");
		noteLabel.setBounds(padding, timeUsedLabel.getY() + labelSize.height + spacingTextFields, labelSize.width, labelSize.height);
		panel.add(noteLabel);

		// Note Text Field.
		noteTextArea = new JTextArea();
		noteTextArea.setBounds(xTextField, noteLabel.getY(), textFieldSize.width, 100);
		noteTextArea.setEnabled(fieldEditable);
		UtilFunctions.configureDialogTextFieldOnMainThread(noteTextArea);
		panel.add(noteTextArea);

		// Edit Button.
		positiveButton = new JButton(viewMode.getPositiveButtonTitle());
		positiveButton.setBounds(xTextField, noteLabel.getY() + noteTextArea.getHeight() + padding, 100, textFieldSize.height);
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

		roomNameComboBox.setEnabled(viewMode == DetailDialogModeEnum.CREATE);
		serviceNameComboBox.setEnabled(fieldEditable);
		priceTextField.setEnabled(false);
		numberOfCustomersTextField.setEnabled(fieldEditable);
		totalPriceTextField.setEnabled(false);
		timeUsedTextField.setEnabled(fieldEditable);
		noteTextArea.setEnabled(fieldEditable);
		positiveButton.setText(viewMode.getPositiveButtonTitle());
		negativeButton.setText(viewMode.getNegativeButtonTitle());
	}

	public JComboBox<String> getRoomNameComboBox() {
		return roomNameComboBox;
	}

	public JComboBox<String> getServiceNameComboBox() {
		return serviceNameComboBox;
	}

	public JFormattedTextField getPriceTextField() {
		return priceTextField;
	}

	public JTextField getNumberOfCustomersTextField() {
		return numberOfCustomersTextField;
	}

	public JFormattedTextField getTotalPriceTextField() {
		return totalPriceTextField;
	}

	public JFormattedTextField getTimeUsedTextField() {
		return timeUsedTextField;
	}

	public JTextArea getNoteTextArea() {
		return noteTextArea;
	}

	public JButton getPositiveButton() {
		return positiveButton;
	}

	public JButton getNegativeButton() {
		return negativeButton;
	}

}
