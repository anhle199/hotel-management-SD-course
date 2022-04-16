package controllers.login;

import controllers.ValidationHandler;
import dao.UserDAO;
import db.DBConnectionException;
import db.SingletonDBConnection;
import models.User;
import utils.RoleManager;
import utils.UtilFunctions;
import views.DashboardView;
import views.LoginView;
import views.components.dialogs.ConnectionErrorDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Optional;

public class LoginController implements ActionListener {
	private final LoginView loginView;
	private final ConnectionErrorDialog connectionErrorDialog;
	private final UserDAO daoModel;

	public LoginController(LoginView loginView) {
		this.loginView = loginView;
		this.connectionErrorDialog = new ConnectionErrorDialog(loginView.getMainFrame());
		this.daoModel = new UserDAO();

		// Add action listeners
		this.loginView.getLoginButton().addActionListener(this);
		this.connectionErrorDialog.getReconnectButton().addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == loginView.getLoginButton()) {
			loginButtonAction();
		} else if (event.getSource() == connectionErrorDialog.getReconnectButton()) {
			reconnectButtonAction();
		}
	}

	private void loginButtonAction() {
		final String username = loginView.getUsernameField().getText();
		final String password = loginView.getPasswordField().getPassword();

		if (!ValidationHandler.validateUsername(username) || !ValidationHandler.validatePassword(password)) {
			UtilFunctions.showErrorMessage(loginView, "Login", "Invalid username or password");
		} else {
			try {
				// Loads user data from the database.
				Optional<User> optionalUser = daoModel.getByUsername(username);

				if (optionalUser.isEmpty()) {
					UtilFunctions.showErrorMessage(loginView, "Login", "This account is not exists.");
				} else {
					// Unwrap optional variable.
					User user = optionalUser.get();

					// Set role for the entire app.
					RoleManager.getInstance().setRoleInByteType(user.getRole());

					// Switch to dashboard view for this user.
					DashboardView dashboardView = new DashboardView(loginView.getMainFrame());
//					DashboardController dashboardController = new DashboardController(dashboardView, user);
					dashboardView.display();
				}
			} catch (DBConnectionException e) {
				SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
				e.printStackTrace();
			}
		}
	}

	private void reconnectButtonAction() {
		connectionErrorDialog.setExitOnCloseButton(false);
		connectionErrorDialog.setVisible(false);

		SingletonDBConnection.getInstance().connect();
//		loginView.getUsernameField().setText("");
//		loginView.getPasswordField().setPassword("");
	}

}
