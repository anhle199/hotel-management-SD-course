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
			LoginView loginView = new LoginView(mainFrame);
			loginView.display();
		});
	}

}
