package views.dialogs;

import utils.Constants;
import utils.DetailDialogModeEnum;
import utils.UtilFunctions;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.NumberFormat;

public class RoomDetailDialog extends JDialog {

	private DetailDialogModeEnum viewMode;

	// Components for basic information panel.
	private JTextField roomNameTextField;
	private JComboBox<String> roomTypeComboBox;
	private JFormattedTextField priceTextField;
	private JTextArea noteTextArea;
	private JButton positiveButton;
	private JButton negativeButton;

	public RoomDetailDialog(JFrame frame) {
		this(frame, DetailDialogModeEnum.VIEW_ONLY);
	}

	public RoomDetailDialog(JFrame frame, DetailDialogModeEnum mode) {
		super(frame, "Room Detail", true);
		this.viewMode = mode;

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setPreferredSize(new Dimension(450, 356));
		initSubviews(panel);

		setResizable(false);
		setContentPane(panel);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void initSubviews(JPanel panel) {
		// Sizes and coordinates
		Dimension labelSize = new Dimension(75, 40);
		Dimension textFieldSize = new Dimension(315, 40);
		int padding = 20;
		int spacingTextFields = 12;
		int xTextField = padding * 2 + labelSize.width;

		boolean fieldEditable = viewMode.getFieldEditable();

		// Room Name Label.
		JLabel roomNameLabel = new JLabel("Room name");
		roomNameLabel.setBounds(padding, padding, labelSize.width, labelSize.height);
		panel.add(roomNameLabel);

		// Room Name Text Field.
		roomNameTextField = new JTextField();
		roomNameTextField.setBounds(xTextField, roomNameLabel.getY(), textFieldSize.width, textFieldSize.height);
		UtilFunctions.configureDialogTextFieldOnMainThread(roomNameTextField);
		roomNameTextField.setEnabled(fieldEditable);
		panel.add(roomNameTextField);

		// Room Type Label.
		JLabel roomTypeLabel = new JLabel("Room type");
		roomTypeLabel.setBounds(padding, roomNameLabel.getY() + labelSize.height + spacingTextFields, labelSize.width, labelSize.height);
		panel.add(roomTypeLabel);

		// Room Type Combo Box.
		roomTypeComboBox = new JComboBox<>(Constants.ROOM_TYPES);
		roomTypeComboBox.setBounds(xTextField, roomTypeLabel.getY(), textFieldSize.width, textFieldSize.height);
		roomTypeComboBox.setEnabled(fieldEditable);
		panel.add(roomTypeComboBox);

		// Price Label.
		JLabel priceLabel = new JLabel("Price ($)");
		priceLabel.setBounds(padding, roomTypeLabel.getY() + labelSize.height + spacingTextFields, labelSize.width, labelSize.height);
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
		JLabel noteLabel = new JLabel("Notes");
		noteLabel.setBounds(padding, priceLabel.getY() + labelSize.height + spacingTextFields, labelSize.width, labelSize.height);
		panel.add(noteLabel);

		// Note Text Field.
		noteTextArea = new JTextArea();
		noteTextArea.setBounds(xTextField, noteLabel.getY(), textFieldSize.width, 100);
		noteTextArea.setEnabled(false);
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
		boolean positiveButtonEnabled = viewMode != DetailDialogModeEnum.EDITING;

		roomNameTextField.setEnabled(fieldEditable);
		roomTypeComboBox.setEnabled(fieldEditable);
		priceTextField.setEnabled(false);
		noteTextArea.setEnabled(fieldEditable);
		positiveButton.setEnabled(positiveButtonEnabled);
		positiveButton.setText(viewMode.getPositiveButtonTitle());
		negativeButton.setText(viewMode.getNegativeButtonTitle());
	}

	public JTextField getRoomNameTextField() {
		return roomNameTextField;
	}

	public JComboBox<String> getRoomTypeComboBox() {
		return roomTypeComboBox;
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
