import utils.Constants;
import views.DashboardView;

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
			DashboardView dashboardView = new DashboardView(mainFrame, Constants.Role.EMPLOYEE);
			dashboardView.display();
//			LoginView loginView = new LoginView(mainFrame);
//			loginView.display();
		});
	}

}
