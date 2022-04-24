package views.dialogs;

import utils.Constants;
import utils.DetailDialogModeEnum;
import utils.UtilFunctions;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.NumberFormat;

public class ServiceDetailDialog extends JDialog {

	private DetailDialogModeEnum viewMode;

	private JTextField serviceNameTextField;
	private JTextField descriptionTextField;
	private JFormattedTextField priceTextField;
	private JTextArea noteTextArea;
	private JButton positiveButton;
	private JButton negativeButton;

	public ServiceDetailDialog(JFrame frame) {
		this(frame, DetailDialogModeEnum.VIEW_ONLY);
	}

	public ServiceDetailDialog(JFrame frame, DetailDialogModeEnum mode) {
		super(frame, "Service Detail", true);
		this.viewMode = mode;

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setPreferredSize(new Dimension(460, 356));
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

		boolean fieldEditable = viewMode.getFieldEditable();

		// Service Name Label.
		JLabel serviceNameLabel = new JLabel("Service name");
		serviceNameLabel.setBounds(padding, padding, labelSize.width, labelSize.height);
		panel.add(serviceNameLabel);

		// Service Name Text Field.
		serviceNameTextField = new JTextField();
		serviceNameTextField.setBounds(xTextField, serviceNameLabel.getY(), textFieldSize.width, textFieldSize.height);
		UtilFunctions.configureDialogTextFieldOnMainThread(serviceNameTextField);
		serviceNameTextField.setEnabled(fieldEditable);
		panel.add(serviceNameTextField);

		// Description Label.
		JLabel descriptionLabel = new JLabel("Description");
		descriptionLabel.setBounds(padding, serviceNameLabel.getY() + labelSize.height + spacingTextFields, labelSize.width, labelSize.height);
		panel.add(descriptionLabel);

		// Description Text Field.
		descriptionTextField = new JTextField();
		descriptionTextField.setBounds(xTextField, descriptionLabel.getY(), textFieldSize.width, textFieldSize.height);
		UtilFunctions.configureDialogTextFieldOnMainThread(descriptionTextField);
		descriptionTextField.setEnabled(fieldEditable);
		panel.add(descriptionTextField);

		// Price Label.
		JLabel priceLabel = new JLabel("Price ($)");
		priceLabel.setBounds(padding, descriptionLabel.getY() + labelSize.height + spacingTextFields, labelSize.width, labelSize.height);
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

		// Note Label.
		JLabel noteLabel = new JLabel("Note");
		noteLabel.setBounds(padding, priceLabel.getY() + labelSize.height + spacingTextFields, labelSize.width, labelSize.height);
		panel.add(noteLabel);

		// Note Text Field.
		noteTextArea = new JTextArea();
		noteTextArea.setBounds(xTextField, noteLabel.getY(), textFieldSize.width, 100);
		noteTextArea.setEnabled(fieldEditable);
		UtilFunctions.configureDialogTextFieldOnMainThread(noteTextArea);
		panel.add(noteTextArea);

		// Positive Button.
		positiveButton = new JButton(viewMode.getPositiveButtonTitle());
		positiveButton.setBounds(xTextField, noteLabel.getY() + noteTextArea.getHeight() + padding, 100, textFieldSize.height);
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

		serviceNameTextField.setEnabled(fieldEditable);
		descriptionTextField.setEnabled(fieldEditable);
		priceTextField.setEnabled(fieldEditable);
		noteTextArea.setEnabled(fieldEditable);
		positiveButton.setText(viewMode.getPositiveButtonTitle());
		negativeButton.setText(viewMode.getNegativeButtonTitle());
	}

	public JTextField getServiceNameTextField() {
		return serviceNameTextField;
	}

	public JTextField getDescriptionTextField() {
		return descriptionTextField;
	}

	public JFormattedTextField getPriceTextField() {
		return priceTextField;
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
