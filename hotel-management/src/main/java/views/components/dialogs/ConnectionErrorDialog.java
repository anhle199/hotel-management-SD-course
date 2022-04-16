package views.components.dialogs;

import utils.Constants;
import utils.UtilFunctions;
import views.components.panels.ImagePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ConnectionErrorDialog extends JDialog {
	private final JFrame mainFrame;
//	private JLabel message;
//	private JLabel reconnectMessageLabel;
	private JButton reconnectButton;
	private JButton quitButton;
	private boolean exitOnCloseButton = true;

	public ConnectionErrorDialog(JFrame frame) {
		super(frame, "Connection Failed", true);
		this.mainFrame = frame;

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setPreferredSize(new Dimension(420, 160));

		initComponents(panel);
		addHoverEffectsForQuitButton();

		quitButton.addActionListener((event) -> {
			exitOnCloseButton = false;
			UtilFunctions.quitApp(mainFrame);
		});

		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentHidden(ComponentEvent e) {
				if (exitOnCloseButton)
					UtilFunctions.quitApp(mainFrame);
				else
					exitOnCloseButton = true;
			}
		});

		setAlwaysOnTop(true);
		setResizable(false);
		setContentPane(panel);
		pack();
		setLocationRelativeTo(null);
	}

	private void initComponents(JPanel panel) {
		ImagePanel errorMessageIcon = new ImagePanel(Constants.IconNames.ERROR_MESSAGE, 64, 64);
		errorMessageIcon.setBounds(25, 18, 64, 64);
		panel.add(errorMessageIcon);

		JLabel message = new JLabel("Can not connect to the application.");
		message.setBounds(100, 24, 295, 20);
		panel.add(message);

		JLabel reconnectMessageLabel = new JLabel("You can click Reconnect button to reconnect.");
		reconnectMessageLabel.setBounds(100, 46, 295, 20);
		panel.add(reconnectMessageLabel);

		reconnectButton = new JButton("Reconnect");
		reconnectButton.setBounds(84, 100, 120, 40);
		reconnectButton.setBorder(BorderFactory.createLineBorder(Constants.Colors.WHITE, 1));
		reconnectButton.setFocusPainted(false);
		reconnectButton.setRolloverEnabled(false);
		reconnectButton.setFont(Constants.Fonts.HEADLINE);
		reconnectButton.setForeground(Constants.Colors.WHITE);
		reconnectButton.setBackground(Constants.Colors.SECONDARY);
		panel.add(reconnectButton);

		quitButton = new JButton("Quit App");
		quitButton.setBounds(216, 100, 120, 40);
		quitButton.setBorder(BorderFactory.createLineBorder(Constants.Colors.RED, 2));
		quitButton.setFocusPainted(false);
		quitButton.setRolloverEnabled(false);
		quitButton.setFont(Constants.Fonts.BODY);
		quitButton.setForeground(Constants.Colors.RED);
		quitButton.setBackground(Constants.Colors.SYSTEM_PANEL_BACKGROUND);
		panel.add(quitButton);
	}

	public JButton getReconnectButton() {
		return reconnectButton;
	}

	public void setExitOnCloseButton(boolean exitOnCloseButton) {
		this.exitOnCloseButton = exitOnCloseButton;
	}

	private void addHoverEffectsForQuitButton() {
		quitButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				quitButton.setBorderPainted(false);
				quitButton.setBackground(Constants.Colors.RED);
				quitButton.setForeground(Constants.Colors.WHITE);
				quitButton.setFont(Constants.Fonts.HEADLINE);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				quitButton.setBorderPainted(true);
				quitButton.setBackground(Constants.Colors.SYSTEM_PANEL_BACKGROUND);
				quitButton.setForeground(Constants.Colors.RED);
				quitButton.setFont(Constants.Fonts.BODY);
			}
		});
	}

}
