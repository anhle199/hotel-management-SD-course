package views.dialogs;

import utils.Constants;
import utils.UtilFunctions;
import views.components.panels.PasswordFieldPanel;
import views.components.panels.PasswordTextField;

import javax.swing.*;
import java.awt.*;

public class AddEmployeeDialog extends JDialog {

	// Components for basic information panel.
	private JComboBox<String> employeeNameComboBox;
	private JComboBox<String> genderComboBox;
	private JComboBox<String> yearOfBirthComboBox;
	private JTextField usernameTextField;
	private PasswordTextField passwordTextField;
	private JButton editButton;
	private JButton closeButton;

	public AddEmployeeDialog(JFrame frame) {
		super(frame, "Add Employee", true);

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setPreferredSize(new Dimension(500, 348));
		initSubviews(panel);

		setResizable(false);
		setContentPane(panel);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void initSubviews(JPanel panel) {
		// Sizes and coordinates
		Dimension labelSize = new Dimension(100, 40);
		Dimension textFieldSize = new Dimension(340, 40);
		int padding = 20;
		int spacingTextFields = 12;
		int xTextField = padding * 2 + labelSize.width;

		// Employee Name Label.
		JLabel employeeNameLabel = new JLabel("Employee name");
		employeeNameLabel.setBounds(padding, padding, labelSize.width, labelSize.height);
		panel.add(employeeNameLabel);

		// Employee Name Combo Box.
		employeeNameComboBox = new JComboBox<>();
		employeeNameComboBox.setBounds(xTextField, employeeNameLabel.getY(), textFieldSize.width, textFieldSize.height);
		employeeNameComboBox.setEnabled(false);
		panel.add(employeeNameComboBox);

		// Gender Label.
		JLabel genderLabel = new JLabel("Gender");
		genderLabel.setBounds(padding, employeeNameLabel.getY() + labelSize.height + spacingTextFields, labelSize.width, labelSize.height);
		panel.add(genderLabel);

		// Gender Combo Box.
		genderComboBox = new JComboBox<>(Constants.GENDERS);
		genderComboBox.setBounds(xTextField, genderLabel.getY(), textFieldSize.width, textFieldSize.height);
		genderComboBox.setEnabled(false);
		panel.add(genderComboBox);

		// Year Of Birth Label.
		JLabel yearOfBirthLabel = new JLabel("Year of birth");
		yearOfBirthLabel.setBounds(padding, genderLabel.getY() + labelSize.height + spacingTextFields, labelSize.width, labelSize.height);
		panel.add(yearOfBirthLabel);

		// Year Of Birth Combo Box.
		yearOfBirthComboBox = new JComboBox<>();
		yearOfBirthComboBox.setBounds(xTextField, yearOfBirthLabel.getY(), textFieldSize.width, textFieldSize.height);
		yearOfBirthComboBox.setEnabled(false);
		panel.add(yearOfBirthComboBox);

		// Username Label.
		JLabel usernameLabel = new JLabel("Username");
		usernameLabel.setBounds(padding, yearOfBirthLabel.getY() + labelSize.height + spacingTextFields, labelSize.width, labelSize.height);
		panel.add(usernameLabel);

		// Username Text Field
		usernameTextField = new JTextField();
		usernameTextField.setBounds(xTextField, usernameLabel.getY(), textFieldSize.width, textFieldSize.height);
		usernameTextField.setEnabled(false);
		panel.add(usernameTextField);

		// Password Label.
		JLabel passwordLabel = new JLabel("Password");
		passwordLabel.setBounds(padding, usernameLabel.getY() + labelSize.height + spacingTextFields, labelSize.width, labelSize.height);
		panel.add(passwordLabel);

		// Password Text Field
		passwordTextField = new PasswordTextField(textFieldSize);
		passwordTextField.setBounds(xTextField, passwordLabel.getY(), textFieldSize.width, textFieldSize.height);
		passwordTextField.setEnabled(false);
		panel.add(passwordTextField);

		// Edit Button.
		editButton = new JButton("Create");
		editButton.setBounds(xTextField, passwordLabel.getY() + passwordTextField.getHeight() + padding, 100, textFieldSize.height);
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
