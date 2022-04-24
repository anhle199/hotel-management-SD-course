package controllers;

import controllers.rooms.RoomManagementController;
import controllers.services.ServiceManagementController;
import models.User;
import utils.RoleManager;
import utils.UtilFunctions;
import views.DashboardView;
import views.LoginView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DashboardController implements ActionListener {

	private final DashboardView dashboardView;
	private final LoginView loginView;
	private final RoomManagementController roomManagementController;
	private final ServiceManagementController serviceManagementController;
//	private final User user;

	public DashboardController(DashboardView dashboardView, LoginView loginView, User user) {
		this.dashboardView = dashboardView;
		this.loginView = loginView;
		this.roomManagementController = new RoomManagementController(
				dashboardView.getRoomManagementTabbed(),
				dashboardView.getMainFrame()
		);
		this.serviceManagementController = new ServiceManagementController(
				dashboardView.getServiceManagementTabbed(),
				dashboardView.getMainFrame()
		);
//		this.user = user;

		// Add action listeners
		this.dashboardView.getRoomManagementButton().addActionListener(this);
		this.dashboardView.getServiceManagementButton().addActionListener(this);
		this.dashboardView.getProductManagementButton().addActionListener(this);
		this.dashboardView.getEmployeeManagementButton().addActionListener(this);
		this.dashboardView.getStatisticsButton().addActionListener(this);
		this.dashboardView.getLogoutButton().addActionListener(this);

		// Update user info
		this.dashboardView.getUsernameLabel().setText(user.getFullName());
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
	}

}
