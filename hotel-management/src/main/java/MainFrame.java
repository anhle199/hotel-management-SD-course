import utils.Constants;
import utils.RoleManager;
import views.DashboardView;
import views.LoginView;

import javax.swing.*;

public class MainFrame {

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch(Exception exception) {
			System.out.println();
		}

		SwingUtilities.invokeLater(() -> {
			JFrame mainFrame = new JFrame();
			mainFrame.setTitle("Hotel Management");
			mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			RoleManager.getInstance().setRole(Constants.Role.MANAGER);
//			RoleManager.getInstance().setRole(Constants.Role.EMPLOYEE);
			DashboardView dashboardView = new DashboardView(mainFrame);
			dashboardView.display();
//			LoginView loginView = new LoginView(mainFrame);
//			loginView.display();
		});
	}

}
