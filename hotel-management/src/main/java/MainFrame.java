import controllers.login.LoginController;
import db.SingletonDBConnection;
import utils.RoleManager;
import views.LoginView;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.Arrays;

public class MainFrame {

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch(Exception exception) {
			System.out.println("MainFrame.java - catch setLookAndFeel - " + exception.getMessage());
			System.out.println("MainFrame.java - catch setLookAndFeel - " + Arrays.toString(exception.getStackTrace()));
		}

		SwingUtilities.invokeLater(() -> {
			JFrame mainFrame = new JFrame();
			mainFrame.setTitle("Hotel Management");
			mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			mainFrame.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					try {
						System.out.println("MainFrame.java - windowClosing - try - The HotelManagement app is closing");
						SingletonDBConnection.getInstance().closeConnection();
						System.out.println("MainFrame.java - windowClosing - try - The HotelManagement app is closed");
					} catch (SQLException sqlException) {
						System.out.println("MainFrame.java - windowClosing - catch - " + sqlException.getMessage());
						System.out.println("MainFrame.java - windowClosing - catch - " + Arrays.toString(sqlException.getStackTrace()));
					}
				}
			});

			// Not sign in
			RoleManager.getInstance().setRole(RoleManager.RoleEnum.NONE);

			LoginView loginView = new LoginView(mainFrame);
			LoginController loginController = new LoginController(loginView);

			loginView.display();
		});
	}

}
