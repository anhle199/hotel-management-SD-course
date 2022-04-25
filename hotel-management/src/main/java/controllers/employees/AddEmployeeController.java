package controllers.employees;

import controllers.ValidationHandler;
import dao.UserDAO;
import db.DBConnectionException;
import db.SingletonDBConnection;
import models.User;
import utils.RoleManager;
import utils.UtilFunctions;
import views.components.dialogs.ConnectionErrorDialog;
import views.dialogs.AddEmployeeDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddEmployeeController implements ActionListener {

	private final AddEmployeeDialog addEmployeeDialog;
	private final ConnectionErrorDialog connectionErrorDialog;

	private final EmployeeManagementController employeeManagementController;

	public AddEmployeeController(
			AddEmployeeDialog dialog,
			JFrame mainFrame,
			EmployeeManagementController employeeManagementController
	) {
		this.addEmployeeDialog = dialog;
		this.connectionErrorDialog = new ConnectionErrorDialog(mainFrame);
		this.employeeManagementController = employeeManagementController;

		// Add action listeners
		this.addEmployeeDialog.getCreateButton().addActionListener(this);
		this.addEmployeeDialog.getCancelButton().addActionListener(this);
		this.connectionErrorDialog.getReconnectButton().addActionListener(this);
	}

	public void displayUI() {
		addEmployeeDialog.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == addEmployeeDialog.getCreateButton()) {
			createButtonAction();
		} else if (event.getSource() == addEmployeeDialog.getCancelButton()) {
			addEmployeeDialog.setVisible(false);
		} else if (event.getSource() == connectionErrorDialog.getReconnectButton()) {
			reconnectButtonAction();
		}
	}

	private void createButtonAction() {
		try {
			User newEmployee = getUserInstanceFromInputFields();

			if (validateEmployeeInfoAndShowMAlert(newEmployee)) {
				int option = UtilFunctions.showConfirmDialog(
						addEmployeeDialog,
						"Add Employee",
						"Are you sure to add this employee?"
				);

				if (option == JOptionPane.YES_OPTION) {
					// Hash password.
					String encodedPassword = UtilFunctions.hashPassword(newEmployee.getPassword());
					newEmployee.setPassword(encodedPassword);

					UserDAO daoModel = new UserDAO();
					daoModel.insert(newEmployee);
					UtilFunctions.showInfoMessage(addEmployeeDialog, "Add Employee", "Add successfully.");

					addEmployeeDialog.setVisible(false);
					employeeManagementController.loadEmployeeListAndReloadTableData();
				}
			}
		} catch (DBConnectionException e) {
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
			System.out.println("AddEmployeeController.java - createButtonAction - catch - Unavailable connection.");
		}
	}

	private void reconnectButtonAction() {
		connectionErrorDialog.setExitOnCloseButton(false);
		connectionErrorDialog.setVisible(false);

		SingletonDBConnection.getInstance().connect();
		displayUI();
	}

	private boolean checkEmptyFields(String fullName, String username, String password) {
		return !fullName.isEmpty() && !username.isEmpty() && !password.isEmpty();
	}

	private boolean validateEmployeeInfoAndShowMAlert(User employee) throws DBConnectionException {
		UserDAO daoModel = new UserDAO();

		if (checkEmptyFields(employee.getFullName(), employee.getUsername(), employee.getPassword())) {
			UtilFunctions.showErrorMessage(
					addEmployeeDialog,
					"Add Employee",
					"All fields must not be empty."
			);
			return false;
		} else if (ValidationHandler.validateFullName(employee.getFullName())) {
			UtilFunctions.showErrorMessage(
					addEmployeeDialog,
					"Add Employee",
					"Username is invalid."
			);
			return false;
		} else if (ValidationHandler.validateUsername(employee.getUsername())) {
			UtilFunctions.showErrorMessage(
					addEmployeeDialog,
					"Add Employee",
					"Username is invalid."
			);
			return false;
		} else if (ValidationHandler.validatePassword(employee.getPassword())) {
			UtilFunctions.showErrorMessage(
					addEmployeeDialog,
					"Add Employee",
					"Username is invalid."
			);
			return false;
		} else if (daoModel.isExistingUsername(employee.getUsername())) {
			UtilFunctions.showErrorMessage(
					addEmployeeDialog,
					"Add Employee",
					"This username is existing."
			);
			return false;
		}

		return true;
	}

	private User getUserInstanceFromInputFields() {
		String username = UtilFunctions.removeRedundantWhiteSpace(
				addEmployeeDialog.getUsernameTextField().getText()
		);
		String password = UtilFunctions.removeRedundantWhiteSpace(
				addEmployeeDialog.getPasswordTextField().getPassword()
		);
		String employeeName = UtilFunctions.removeRedundantWhiteSpace(
				addEmployeeDialog.getEmployeeNameTextField().getText()
		);
		User.GenderEnum gender = User.GenderEnum.valueOfIgnoreCase(
				String.valueOf(addEmployeeDialog.getGenderComboBox().getSelectedItem())
		);
		short yearOfBirth = Short.parseShort(
				String.valueOf(addEmployeeDialog.getYearOfBirthComboBox().getSelectedItem())
		);

		return new User(
				-1,
				username,
				password,
				RoleManager.RoleEnum.EMPLOYEE.byteValue(),
				employeeName,
				gender.byteValue(),
				yearOfBirth
		);
	}

}
