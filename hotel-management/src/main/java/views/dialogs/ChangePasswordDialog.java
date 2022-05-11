package views.dialogs;

import utils.UtilFunctions;
import views.components.panels.PasswordTextField;

import javax.swing.*;
import java.awt.*;

public class ChangePasswordDialog extends JDialog {

	// Components
	private PasswordTextField currentPasswordTextField;
	private PasswordTextField newPasswordTextField;
	private PasswordTextField confirmNewPasswordTextField;
	private JButton cancelButton;
	private JButton saveButton;

	public ChangePasswordDialog(JFrame frame) {
		super(frame, "Change Password", true);

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setPreferredSize(new Dimension(600, 244));

		initComponents(panel);

		setAlwaysOnTop(true);
		setResizable(false);
		setContentPane(panel);
		pack();
		setLocationRelativeTo(null);
	}

	private void initComponents(JPanel panel) {
		Dimension labelSize = new Dimension(150, 40);
		Dimension fieldSize = new Dimension(390, 40);
		int spacingBetweenFields = 12;
		int spacingBetweenLabelAndField = 20;

		// Current password label
		JLabel currentPasswordLabel = new JLabel("Current password");
		currentPasswordLabel.setBounds(20, 20, labelSize.width, labelSize.height);
		panel.add(currentPasswordLabel);

		currentPasswordTextField = new PasswordTextField(fieldSize);
		currentPasswordTextField.setBounds(
				currentPasswordLabel.getX() + labelSize.width + spacingBetweenLabelAndField,
				currentPasswordLabel.getY(),
				fieldSize.width,
				fieldSize.height
		);
		currentPasswordTextField.setVisible(true);
		panel.add(currentPasswordTextField);

		// New password label
		JLabel newPasswordLabel = new JLabel("New password");
		newPasswordLabel.setBounds(
				currentPasswordLabel.getX(),
				currentPasswordLabel.getY() + labelSize.height + spacingBetweenFields,
				labelSize.width,
				labelSize.height
		);
		panel.add(newPasswordLabel);

		newPasswordTextField = new PasswordTextField(fieldSize);
		newPasswordTextField.setBounds(
				newPasswordLabel.getX() + labelSize.width + spacingBetweenLabelAndField,
				newPasswordLabel.getY(),
				fieldSize.width,
				fieldSize.height
		);
		newPasswordTextField.setVisible(true);
		panel.add(newPasswordTextField);

		// Confirm new password label
		JLabel confirmNewPasswordLabel = new JLabel("Confirm new password");
		confirmNewPasswordLabel.setBounds(
				newPasswordLabel.getX(),
				newPasswordLabel.getY() + labelSize.height + spacingBetweenFields,
				labelSize.width,
				labelSize.height
		);
		panel.add(confirmNewPasswordLabel);

		confirmNewPasswordTextField = new PasswordTextField(fieldSize);
		confirmNewPasswordTextField.setBounds(
				confirmNewPasswordLabel.getX() + labelSize.width + spacingBetweenLabelAndField,
				confirmNewPasswordLabel.getY(),
				fieldSize.width,
				fieldSize.height
		);
		confirmNewPasswordTextField.setVisible(true);
		panel.add(confirmNewPasswordTextField);

		// Create button
		saveButton = new JButton("Save");
		saveButton.setBounds(190, 184, 100, 40);
		UtilFunctions.configureTopBarButtonOnMainThread(saveButton);
		panel.add(saveButton);

		// Cancel button
		cancelButton = new JButton("Cancel");
		cancelButton.setBounds(310, 184, 100, 40);
		UtilFunctions.configureBorderedButtonOnMainThread(cancelButton);
		UtilFunctions.addHoverEffectsForBorderedButton(cancelButton);
		panel.add(cancelButton);
	}

	public PasswordTextField getCurrentPasswordTextField() {
		return currentPasswordTextField;
	}

	public PasswordTextField getNewPasswordTextField() {
		return newPasswordTextField;
	}

	public PasswordTextField getConfirmNewPasswordTextField() {
		return confirmNewPasswordTextField;
	}

	public JButton getSaveButton() {
		return saveButton;
	}

	public JButton getCancelButton() {
		return cancelButton;
	}

}
