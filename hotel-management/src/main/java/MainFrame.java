import javax.swing.*;

public class MainFrame {

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch(Exception exception) {
			System.out.println();
		}
	}

}
