package controllers;

import controllers.employees.EmployeeManagementController;
import controllers.products.ProductManagementController;
import controllers.rooms.RoomManagementController;
import controllers.services.ServiceManagementController;
import controllers.statistics.StatisticsController;
import dao.UserDAO;
import db.DBConnectionException;
import db.SingletonDBConnection;
import models.User;
import utils.RoleManager;
import utils.UtilFunctions;
import views.DashboardView;
import views.LoginView;
import views.components.dialogs.ConnectionErrorDialog;
import views.dialogs.ChangePasswordDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Optional;

public class DashboardController implements ActionListener {

	private final DashboardView dashboardView;
	private final LoginView loginView;
	private final ConnectionErrorDialog connectionErrorDialog;
	private final ChangePasswordDialog changePasswordDialog;
	private final RoomManagementController roomManagementController;
	private final ServiceManagementController serviceManagementController;
	private final ProductManagementController productManagementController;
	private EmployeeManagementController employeeManagementController;
	private StatisticsController statisticsController;

	private final User user;

	public DashboardController(DashboardView dashboardView, LoginView loginView, User user) {
		this.dashboardView = dashboardView;
		this.loginView = loginView;
		this.user = user;
		this.connectionErrorDialog = new ConnectionErrorDialog(loginView.getMainFrame());
		this.changePasswordDialog = new ChangePasswordDialog(dashboardView.getMainFrame());
		this.roomManagementController = new RoomManagementController(
				dashboardView.getRoomManagementTabbed(),
				dashboardView.getMainFrame()
		);
		this.serviceManagementController = new ServiceManagementController(
				dashboardView.getServiceManagementTabbed(),
				dashboardView.getMainFrame()
		);
		this.productManagementController = new ProductManagementController(
				dashboardView.getProductManagementTabbed(),
				dashboardView.getMainFrame()
		);
		if (RoleManager.getInstance().isManager()) {
			this.employeeManagementController = new EmployeeManagementController(
					dashboardView.getEmployeeManagementPanel(),
					dashboardView.getMainFrame()
			);
			this.statisticsController = new StatisticsController(
					dashboardView.getStatisticsPanel(),
					dashboardView.getMainFrame()
			);

			// Add action listeners
			this.dashboardView.getEmployeeManagementButton().addActionListener(this);
			this.dashboardView.getStatisticsButton().addActionListener(this);
		}

		// Add action listeners
		this.dashboardView.getRoomManagementButton().addActionListener(this);
		this.dashboardView.getServiceManagementButton().addActionListener(this);
		this.dashboardView.getProductManagementButton().addActionListener(this);
		this.dashboardView.getLogoutButton().addActionListener(this);
		this.changePasswordDialog.getSaveButton().addActionListener(this);
		this.changePasswordDialog.getCancelButton().addActionListener(this);
		this.connectionErrorDialog.getReconnectButton().addActionListener(this);

		// Add mouse listener
		this.dashboardView.getChangePasswordImage().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent mouseEvent) {
				if (mouseEvent.getClickCount() == 1) {
					changePasswordImageAction();
				}
			}
		});

		// Update user info
		this.dashboardView.getUsernameLabel().setText(user.getFullName());

		roomManagementController.displayUI();
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == dashboardView.getRoomManagementButton()) {
			roomManagementButtonAction();
		} else if (event.getSource() == dashboardView.getServiceManagementButton()) {
			serviceManagementButtonAction();
		} else if (event.getSource() == dashboardView.getProductManagementButton()) {
			productManagementButtonAction();
		} else if (event.getSource() == dashboardView.getEmployeeManagementButton()) {
			employeeManagementButtonAction();
		} else if (event.getSource() == dashboardView.getStatisticsButton()) {
			statisticsButtonAction();
		} else if (event.getSource() == dashboardView.getLogoutButton()) {
			loginView.display();
		} else if (event.getSource() == changePasswordDialog.getSaveButton()) {
			saveActionOfChangePasswordDialog();
		} else if (event.getSource() == changePasswordDialog.getCancelButton()) {
			changePasswordDialog.setVisible(false);
		} else if (event.getSource() == connectionErrorDialog.getReconnectButton()) {
			reconnectButtonAction();
		}
	}

	private void roomManagementButtonAction() {
		dashboardView.getRoomManagementTabbed().setVisible(true);
		dashboardView.getServiceManagementTabbed().setVisible(false);
		dashboardView.getProductManagementTabbed().setVisible(false);

		UtilFunctions.switchSidebarButtonActiveStateOnMainThread(dashboardView.getRoomManagementButton(), true);
		UtilFunctions.switchSidebarButtonActiveStateOnMainThread(dashboardView.getServiceManagementButton(), false);
		UtilFunctions.switchSidebarButtonActiveStateOnMainThread(dashboardView.getProductManagementButton(), false);

		if (RoleManager.getInstance().isManager()) {
			dashboardView.getEmployeeManagementPanel().setVisible(false);
			dashboardView.getStatisticsPanel().setVisible(false);

			UtilFunctions.switchSidebarButtonActiveStateOnMainThread(dashboardView.getEmployeeManagementButton(), false);
			UtilFunctions.switchSidebarButtonActiveStateOnMainThread(dashboardView.getStatisticsButton(), false);
		}

		roomManagementController.displayUI();
	}

	private void serviceManagementButtonAction() {
		dashboardView.getRoomManagementTabbed().setVisible(false);
		dashboardView.getServiceManagementTabbed().setVisible(true);
		dashboardView.getProductManagementTabbed().setVisible(false);

		UtilFunctions.switchSidebarButtonActiveStateOnMainThread(dashboardView.getRoomManagementButton(), false);
		UtilFunctions.switchSidebarButtonActiveStateOnMainThread(dashboardView.getServiceManagementButton(), true);
		UtilFunctions.switchSidebarButtonActiveStateOnMainThread(dashboardView.getProductManagementButton(), false);

		if (RoleManager.getInstance().isManager()) {
			dashboardView.getEmployeeManagementPanel().setVisible(false);
			dashboardView.getStatisticsPanel().setVisible(false);

			UtilFunctions.switchSidebarButtonActiveStateOnMainThread(dashboardView.getEmployeeManagementButton(), false);
			UtilFunctions.switchSidebarButtonActiveStateOnMainThread(dashboardView.getStatisticsButton(), false);
		}

		serviceManagementController.displayUI();
	}

	private void productManagementButtonAction() {
		dashboardView.getRoomManagementTabbed().setVisible(false);
		dashboardView.getServiceManagementTabbed().setVisible(false);
		dashboardView.getProductManagementTabbed().setVisible(true);

		UtilFunctions.switchSidebarButtonActiveStateOnMainThread(dashboardView.getRoomManagementButton(), false);
		UtilFunctions.switchSidebarButtonActiveStateOnMainThread(dashboardView.getServiceManagementButton(), false);
		UtilFunctions.switchSidebarButtonActiveStateOnMainThread(dashboardView.getProductManagementButton(), true);

		if (RoleManager.getInstance().isManager()) {
			dashboardView.getEmployeeManagementPanel().setVisible(false);
			dashboardView.getStatisticsPanel().setVisible(false);

			UtilFunctions.switchSidebarButtonActiveStateOnMainThread(dashboardView.getEmployeeManagementButton(), false);
			UtilFunctions.switchSidebarButtonActiveStateOnMainThread(dashboardView.getStatisticsButton(), false);
		}

		productManagementController.displayUI();
	}

	private void employeeManagementButtonAction() {
		dashboardView.getRoomManagementTabbed().setVisible(false);
		dashboardView.getServiceManagementTabbed().setVisible(false);
		dashboardView.getProductManagementTabbed().setVisible(false);

		UtilFunctions.switchSidebarButtonActiveStateOnMainThread(dashboardView.getRoomManagementButton(), false);
		UtilFunctions.switchSidebarButtonActiveStateOnMainThread(dashboardView.getServiceManagementButton(), false);
		UtilFunctions.switchSidebarButtonActiveStateOnMainThread(dashboardView.getProductManagementButton(), false);

		if (RoleManager.getInstance().isManager()) {
			dashboardView.getEmployeeManagementPanel().setVisible(true);
			dashboardView.getStatisticsPanel().setVisible(false);

			UtilFunctions.switchSidebarButtonActiveStateOnMainThread(dashboardView.getEmployeeManagementButton(), true);
			UtilFunctions.switchSidebarButtonActiveStateOnMainThread(dashboardView.getStatisticsButton(), false);
		}

		employeeManagementController.displayUI();
	}

	private void statisticsButtonAction() {
		dashboardView.getRoomManagementTabbed().setVisible(false);
		dashboardView.getServiceManagementTabbed().setVisible(false);
		dashboardView.getProductManagementTabbed().setVisible(false);

		UtilFunctions.switchSidebarButtonActiveStateOnMainThread(dashboardView.getRoomManagementButton(), false);
		UtilFunctions.switchSidebarButtonActiveStateOnMainThread(dashboardView.getServiceManagementButton(), false);
		UtilFunctions.switchSidebarButtonActiveStateOnMainThread(dashboardView.getProductManagementButton(), false);

		if (RoleManager.getInstance().isManager()) {
			dashboardView.getEmployeeManagementPanel().setVisible(false);
			dashboardView.getStatisticsPanel().setVisible(true);

			UtilFunctions.switchSidebarButtonActiveStateOnMainThread(dashboardView.getEmployeeManagementButton(), false);
			UtilFunctions.switchSidebarButtonActiveStateOnMainThread(dashboardView.getStatisticsButton(), true);
		}

		statisticsController.displayUI();
	}

	private void saveActionOfChangePasswordDialog() {
		String oldPassword = changePasswordDialog.getCurrentPasswordTextField().getPassword();
		String newPassword = changePasswordDialog.getNewPasswordTextField().getPassword();
		String confirmNewPassword = changePasswordDialog.getConfirmNewPasswordTextField().getPassword();

		if (!ValidationHandler.validatePassword(oldPassword)) {
			UtilFunctions.showErrorMessage(changePasswordDialog, "Change Password", "Invalid current password");
		} else if (
				!ValidationHandler.validatePassword(newPassword) ||
						!ValidationHandler.validatePassword(confirmNewPassword)
		) {
			UtilFunctions.showErrorMessage(
					changePasswordDialog,
					"Change Password",
					"Invalid new password or confirm new password"
			);
		} else if (!newPassword.equals(confirmNewPassword)) {
			UtilFunctions.showErrorMessage(
					changePasswordDialog,
					"Create Account Manager",
					"Confirm new password does not match with new password"
			);
		} else {
			int option = UtilFunctions.showConfirmDialog(
					changePasswordDialog,
					"Change Password",
					"Are you sure to change password"
			);

			if (option == JOptionPane.YES_OPTION) {
				try {
					UserDAO daoModel = new UserDAO();
					Optional<User> optionalUser = daoModel.getByUsername(user.getUsername());

					if (optionalUser.isPresent()) {
						User user = optionalUser.get();
						String oldPasswordEncoded = UtilFunctions.hashPassword(oldPassword);

						if (!oldPasswordEncoded.equals(user.getPassword())) {
							UtilFunctions.showErrorMessage(
									changePasswordDialog,
									"Change Password",
									"Incorrect current password"
							);
						} else {
							user.setPassword(UtilFunctions.hashPassword(newPassword));

							daoModel.update(user);
							UtilFunctions.showInfoMessage(
									changePasswordDialog,
									"Change Password",
									"Change password successfully, you must login again."
							);

							changePasswordDialog.setVisible(false);
							loginView.display();
						}
					}
				} catch (DBConnectionException e) {
					SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
					e.printStackTrace();
				}
			}
		}
	}

	private void reconnectButtonAction() {
		connectionErrorDialog.setExitOnCloseButton(false);
		connectionErrorDialog.setVisible(false);

		SingletonDBConnection.getInstance().connect();
		changePasswordDialog.setVisible(false);
		roomManagementController.displayUI();
	}

	private void changePasswordImageAction() {
		changePasswordDialog.getCurrentPasswordTextField().setPassword("");
		changePasswordDialog.getNewPasswordTextField().setPassword("");
		changePasswordDialog.getConfirmNewPasswordTextField().setPassword("");

		changePasswordDialog.getCurrentPasswordTextField().setPasswordVisibility(false);
		changePasswordDialog.getNewPasswordTextField().setPasswordVisibility(false);
		changePasswordDialog.getConfirmNewPasswordTextField().setPasswordVisibility(false);

		changePasswordDialog.setVisible(true);
	}

}
