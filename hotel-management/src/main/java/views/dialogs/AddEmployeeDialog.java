package views.dialogs;

import models.User;
import utils.UtilFunctions;
import views.components.panels.PasswordTextField;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.Vector;

public class AddEmployeeDialog extends JDialog {

	private JTextField employeeNameTextField;
	private JComboBox<String> genderComboBox;
	private JComboBox<Integer> yearOfBirthComboBox;
	private JTextField usernameTextField;
	private PasswordTextField passwordTextField;
	private JButton createButton;
	private JButton cancelButton;

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
		employeeNameTextField = new JTextField();
		employeeNameTextField.setBounds(xTextField, employeeNameLabel.getY(), textFieldSize.width, textFieldSize.height);
		employeeNameTextField.setEnabled(false);
		panel.add(employeeNameTextField);

		// Gender Label.
		JLabel genderLabel = new JLabel("Gender");
		genderLabel.setBounds(padding, employeeNameLabel.getY() + labelSize.height + spacingTextFields, labelSize.width, labelSize.height);
		panel.add(genderLabel);

		// Gender Combo Box.
		genderComboBox = new JComboBox<>(User.GenderEnum.allCases());
		genderComboBox.setBounds(xTextField, genderLabel.getY(), textFieldSize.width, textFieldSize.height);
		genderComboBox.setEnabled(false);
		panel.add(genderComboBox);

		// Year Of Birth Label.
		JLabel yearOfBirthLabel = new JLabel("Year of birth");
		yearOfBirthLabel.setBounds(padding, genderLabel.getY() + labelSize.height + spacingTextFields, labelSize.width, labelSize.height);
		panel.add(yearOfBirthLabel);

		int currentYear = LocalDate.now().getYear();
		Vector<Integer> yearList = new Vector<>();
		for (int i = 1970; i < currentYear - 16; i++) {
			yearList.add(i);
		}

		// Year Of Birth Combo Box.
		yearOfBirthComboBox = new JComboBox<>(yearList);
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
		createButton = new JButton("Create");
		createButton.setBounds(xTextField, passwordLabel.getY() + passwordTextField.getHeight() + padding, 100, textFieldSize.height);
		UtilFunctions.configureTopBarButtonOnMainThread(createButton);
		panel.add(createButton);

		// Close Button.
		cancelButton = new JButton("Cancel");
		cancelButton.setBounds(createButton.getX() + createButton.getWidth() + padding, createButton.getY(), 100, textFieldSize.height);
		UtilFunctions.configureBorderedButtonOnMainThread(cancelButton);
		UtilFunctions.addHoverEffectsForBorderedButton(cancelButton);
		panel.add(cancelButton);
	}

	public JTextField getEmployeeNameTextField() {
		return employeeNameTextField;
	}

	public JComboBox<String> getGenderComboBox() {
		return genderComboBox;
	}

	public JComboBox<Integer> getYearOfBirthComboBox() {
		return yearOfBirthComboBox;
	}

	public JTextField getUsernameTextField() {
		return usernameTextField;
	}

	public PasswordTextField getPasswordTextField() {
		return passwordTextField;
	}

	public JButton getCreateButton() {
		return createButton;
	}

	public JButton getCancelButton() {
		return cancelButton;
	}

}
