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
	private JFormattedTextField numberOfCustomersTextField;
	private JFormattedTextField totalPriceTextField;
	private JTextArea noteTextArea;
	private JButton positiveButton;
	private JButton negativeButton;

	public ServiceInvoiceDetailDialog(JFrame frame, DetailDialogModeEnum mode) {
		super(frame, "Service Invoice Detail", true);
		this.viewMode = mode;

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setPreferredSize(new Dimension(570, 460));
		initSubviews(panel);

		setResizable(false);
		setContentPane(panel);
		pack();
		setLocationRelativeTo(null);
	}

	private void initSubviews(JPanel panel) {
		// Sizes and coordinates
		Dimension labelSize = new Dimension(135, 40);
		Dimension textFieldSize = new Dimension(375, 40);
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
		priceFormatter.setMinimum(Constants.MIN_PRICE);
		priceFormatter.setMaximum(Constants.MAX_PRICE);
		priceFormatter.setAllowsInvalid(false);
		priceFormatter.setCommitsOnValidEdit(true);

		// Price Text Field.
		priceTextField = new JFormattedTextField(priceFormatter);
		priceTextField.setBounds(xTextField, priceLabel.getY(), textFieldSize.width, textFieldSize.height);
		priceTextField.setEnabled(fieldEditable);
		priceTextField.setValue(Constants.MIN_PRICE);
		priceTextField.setHorizontalAlignment(SwingConstants.RIGHT);
		UtilFunctions.configureDialogTextFieldOnMainThread(priceTextField);
		panel.add(priceTextField);

		// Number Of Customers Label.
		JLabel numberOfCustomersLabel = new JLabel("Number of customers");
		numberOfCustomersLabel.setBounds(padding, priceLabel.getY() + labelSize.height + spacingTextFields, labelSize.width, labelSize.height);
		panel.add(numberOfCustomersLabel);

		// Number formatter
		NumberFormatter numberOfCustomersFormatter = new NumberFormatter(numberFormat);
		numberOfCustomersFormatter.setMinimum(Constants.MIN_CUSTOMERS);
		numberOfCustomersFormatter.setMaximum(Constants.MAX_CUSTOMERS);
		numberOfCustomersFormatter.setAllowsInvalid(false);
		numberOfCustomersFormatter.setCommitsOnValidEdit(true);

		// Number Of Customers Text Field.
		numberOfCustomersTextField = new JFormattedTextField(numberOfCustomersFormatter);
		numberOfCustomersTextField.setBounds(xTextField, numberOfCustomersLabel.getY(), textFieldSize.width, textFieldSize.height);
		numberOfCustomersTextField.setEnabled(fieldEditable);
		numberOfCustomersTextField.setValue(Constants.MIN_CUSTOMERS);
		numberOfCustomersTextField.setHorizontalAlignment(SwingConstants.RIGHT);
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

		// Note Label.
		JLabel noteLabel = new JLabel("Note");
		noteLabel.setBounds(padding, totalPriceLabel.getY() + labelSize.height + spacingTextFields, labelSize.width, labelSize.height);
		panel.add(noteLabel);

		// Note Text Field.
		noteTextArea = new JTextArea();
		noteTextArea.setBounds(xTextField, noteLabel.getY(), textFieldSize.width, 100);
		noteTextArea.setLineWrap(true);
		noteTextArea.setWrapStyleWord(true);
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

	public JFormattedTextField getNumberOfCustomersTextField() {
		return numberOfCustomersTextField;
	}

	public JFormattedTextField getTotalPriceTextField() {
		return totalPriceTextField;
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
