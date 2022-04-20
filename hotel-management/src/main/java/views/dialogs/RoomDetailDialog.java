package views.dialogs;

import utils.Constants;
import utils.UtilFunctions;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.NumberFormat;

public class RoomDetailDialog extends JDialog {

	// Components for basic information panel.
	private JTextField roomNameTextField;
	private JComboBox<String> roomTypeComboBox;
	private JFormattedTextField priceTextField;
	private JTextArea noteTextArea;
	private JButton editButton;
	private JButton closeButton;

	public RoomDetailDialog(JFrame frame) {
		super(frame, "Room Detail", true);

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setPreferredSize(new Dimension(450, 356));
		initSubviews(panel);

		setResizable(false);
		setContentPane(panel);
		pack();
		setLocationRelativeTo(null);
	}

	private void initSubviews(JPanel panel) {
		// Sizes and coordinates
		Dimension labelSize = new Dimension(75, 40);
		Dimension textFieldSize = new Dimension(315, 40);
		int padding = 20;
		int spacingTextFields = 12;
		int xTextField = padding * 2 + labelSize.width;

		// Room Name Label.
		JLabel roomNameLabel = new JLabel("Room name");
		roomNameLabel.setBounds(padding, padding, labelSize.width, labelSize.height);
		panel.add(roomNameLabel);

		// Room Name Text Field.
		roomNameTextField = new JTextField();
		roomNameTextField.setBounds(xTextField, roomNameLabel.getY(), textFieldSize.width, textFieldSize.height);
		UtilFunctions.configureDialogTextFieldOnMainThread(roomNameTextField);
		roomNameTextField.setEnabled(false);
		panel.add(roomNameTextField);

		// Room Type Label.
		JLabel roomTypeLabel = new JLabel("Room type");
		roomTypeLabel.setBounds(padding, roomNameLabel.getY() + labelSize.height + spacingTextFields, labelSize.width, labelSize.height);
		panel.add(roomTypeLabel);

		// Room Type Combo Box.
		roomTypeComboBox = new JComboBox<>(Constants.ROOM_TYPES);
		roomTypeComboBox.setBounds(xTextField, roomTypeLabel.getY(), textFieldSize.width, textFieldSize.height);
		roomTypeComboBox.setEnabled(false);
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
		priceTextField.setEnabled(false);
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
