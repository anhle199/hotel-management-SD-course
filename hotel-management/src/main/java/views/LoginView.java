package views;

import utils.Constants;
import views.components.panels.ImagePanel;
import views.components.panels.PasswordFieldPanel;
import views.components.panels.TextFieldPanel;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class LoginView extends JPanel {

	// Components
	private TextFieldPanel usernameField;
	private PasswordFieldPanel passwordField;
	private JButton loginButton;

	// Main frame
	final private JFrame mainFrame;

	public LoginView(JFrame mainFrame) {
		super();
		this.mainFrame = mainFrame;

		setLayout(null);
		initComponents();
		setPreferredSize(new Dimension(440, 426));
		setBackground(Constants.Colors.TERTIARY_50);
	}

	private void initComponents() {
		Dimension inputFieldSize = new Dimension(320, 44);

		JLabel loginTitleLabel = new JLabel("LOGIN");
		loginTitleLabel.setBounds(149, 60, 160, 40);
		loginTitleLabel.setFont(new Font("SF Pro Display", Font.BOLD, 48));
		loginTitleLabel.setForeground(Constants.Colors.PRIMARY);
		add(loginTitleLabel);

		Border lineBorder = BorderFactory.createLineBorder(Constants.Colors.TABLE_BORDER_COLOR, 1);

		// username text field
		ImagePanel usernameIcon = new ImagePanel(Constants.IconNames.ACCOUNT_BOX_BLACK, 24, 24);
		usernameField = new TextFieldPanel("Username", usernameIcon, TextFieldPanel.IconPosition.LEADING, inputFieldSize);
		usernameField.setBounds(60, 152, inputFieldSize.width, inputFieldSize.height);
		usernameField.setBorder(lineBorder);
		add(usernameField);

		// password text field
		passwordField = new PasswordFieldPanel(inputFieldSize);
		passwordField.setBounds(60, 216, inputFieldSize.width, inputFieldSize.height);
		passwordField.setBorder(lineBorder);
		add(passwordField);

		// login button
		loginButton = new JButton("LOGIN");
		loginButton.setBounds(60, 320, 320, 46);
		loginButton.setBorder(lineBorder);
		loginButton.setFocusPainted(false);
		loginButton.setRolloverEnabled(false);
		loginButton.setFont(Constants.Fonts.EMPHASIZED_TITLE_2);
		loginButton.setForeground(Constants.Colors.WHITE);
		loginButton.setBackground(Constants.Colors.PRIMARY);
		add(loginButton);
	}

	public void display() {
		mainFrame.setResizable(false);
		mainFrame.setContentPane(this);
		mainFrame.pack();
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setVisible(true);

		usernameField.setText("");
		passwordField.setPassword("");
		passwordField.setPasswordVisibility(false);
	}

	public JFrame getMainFrame() {
		return mainFrame;
	}

	public TextFieldPanel getUsernameField() {
		return usernameField;
	}

	public PasswordFieldPanel getPasswordField() {
		return passwordField;
	}

	public JButton getLoginButton() {
		return loginButton;
	}

}

