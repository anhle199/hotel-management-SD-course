package controllers.employees;

import dao.UserDAO;
import db.DBConnectionException;
import db.SingletonDBConnection;
import models.User;
import utils.RoleManager;
import utils.UtilFunctions;
import views.components.dialogs.ConnectionErrorDialog;
import views.components.panels.ScrollableTablePanel;
import views.components.table_model.NonEditableTableModel;
import views.dialogs.AddEmployeeDialog;
import views.panels.employees.EmployeeManagementPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class EmployeeManagementController implements ActionListener {

	private final EmployeeManagementPanel employeeManagementPanel;
	private final JFrame mainFrame;
	private final ConnectionErrorDialog connectionErrorDialog;
	private final UserDAO daoModel;

	public EmployeeManagementController(EmployeeManagementPanel employeeManagementPanel, JFrame mainFrame) {
		this.employeeManagementPanel = employeeManagementPanel;
		this.mainFrame = mainFrame;
		this.connectionErrorDialog = new ConnectionErrorDialog(mainFrame);
		this.daoModel = new UserDAO();

		// Add action listeners
		this.employeeManagementPanel.getAddButton().addActionListener(this);
		this.employeeManagementPanel.getRemoveButton().addActionListener(this);
		this.connectionErrorDialog.getReconnectButton().addActionListener(this);
	}

	public void loadEmployeeListAndReloadTableData() {
		try {
			ArrayList<User> employeeList = daoModel.getAllByRole(RoleManager.RoleEnum.EMPLOYEE);
			addRoomListToTable(employeeList);
		} catch (DBConnectionException e) {
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
			System.out.println("EmployeeManagementController.java - loadEmployeeListAndReloadTableData - catch - Unavailable connection.");
		}
	}

	public void addRoomListToTable(ArrayList<User> employeeList) {
		NonEditableTableModel tableModel = (NonEditableTableModel) employeeManagementPanel.getScrollableTable().getTableModel();
		tableModel.removeAllRows();

		for (int i = 0; i < employeeList.size(); i++) {
			Object[] rowValue = mapUserInstanceToRowValue(i + 1, employeeList.get(i));
			tableModel.addRow(rowValue);
		}
	}

	private Object[] mapUserInstanceToRowValue(int no, User user) {
		String capitalizedGender = UtilFunctions.capitalizeFirstLetterInString(
				User.GenderEnum.valueOf(user.getGender()).name()
		);

		return new Object[]{
				no,
				user.getId(),
				user.getFullName(),
				user.getUsername(),
				capitalizedGender,
				user.getYearOfBirth(),
		};
	}

	public void displayUI() {
		loadEmployeeListAndReloadTableData();
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == employeeManagementPanel.getAddButton()) {
			addButtonAction();
		} else if (event.getSource() == employeeManagementPanel.getRemoveButton()) {
			removeButtonAction();
		} else if (event.getSource() == connectionErrorDialog.getReconnectButton()) {
			reconnectButtonAction();
		}
	}

	private void addButtonAction() {
		AddEmployeeDialog addEmployeeDialog = new AddEmployeeDialog(mainFrame);
		AddEmployeeController addEmployeeController = new AddEmployeeController(
				addEmployeeDialog, mainFrame, this
		);

		addEmployeeController.displayUI();
	}

	private void removeButtonAction() {
		ScrollableTablePanel tablePanel = employeeManagementPanel.getScrollableTable();
		int selectedRowIndex = tablePanel.getTable().getSelectedRow();

		if (selectedRowIndex == -1) {
			UtilFunctions.showWarningMessage(
					employeeManagementPanel,
					"Remove Employee",
					"You must select a row."
			);
		} else {
			int option = JOptionPane.showConfirmDialog(
					employeeManagementPanel,
					"Are you sure to remove this employee?",
					"Remove Employee",
					JOptionPane.YES_NO_OPTION
			);

			if (option == JOptionPane.YES_OPTION) {
				int employeeId = (int) tablePanel.getTableModel()
						.getValueAt(selectedRowIndex, EmployeeManagementPanel.HIDDEN_COLUMN_USER_ID);

				try {
					daoModel.delete(employeeId);

					UtilFunctions.showInfoMessage(
							employeeManagementPanel,
							"Remove Employee",
							"This employee is removed successfully."
					);
					loadEmployeeListAndReloadTableData();
				} catch (DBConnectionException e) {
					SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
					System.out.println("EmployeeManagementController.java - removeButtonAction - catch - Unavailable connection.");
				}
			}
		}
	}

	private void reconnectButtonAction() {
		connectionErrorDialog.setExitOnCloseButton(false);
		connectionErrorDialog.setVisible(false);

		SingletonDBConnection.getInstance().connect();
		loadEmployeeListAndReloadTableData();
	}

}
