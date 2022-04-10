package views;

import shared.RoundedButton;
import shared.panels.ImagePanel;
import shared.panels.PasswordFieldPanel;
import shared.panels.TextFieldPanel;
import utils.Constants;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JPanel {
	// Components
	private JLabel loginTitleLabel;
	private TextFieldPanel usernameField;
	private PasswordFieldPanel passwordField;
	private RoundedButton loginButton;

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

		loginTitleLabel = new JLabel("LOGIN");
		loginTitleLabel.setBounds(149, 60, 160, 40);
		loginTitleLabel.setFont(new Font("SF Pro Display", Font.BOLD, 48));
		loginTitleLabel.setForeground(Constants.Colors.PRIMARY);
		add(loginTitleLabel);

		// username text field
		ImagePanel usernameIcon = new ImagePanel(Constants.IconNames.ACCOUNT_BOX, 24, 24);
		usernameField = new TextFieldPanel("Username", usernameIcon, inputFieldSize, 12);
		usernameField.setBounds(60, 152, inputFieldSize.width, inputFieldSize.height);
		usernameField.setBackground(Constants.Colors.WHITE);
		add(usernameField);

		// password text field
		passwordField = new PasswordFieldPanel(inputFieldSize, 12);
		passwordField.setBounds(60, 216, inputFieldSize.width, inputFieldSize.height);
		passwordField.setBackground(Constants.Colors.WHITE);
		add(passwordField);

		// login button
		loginButton = new RoundedButton("LOGIN", 12);
		loginButton.setBounds(60, 320, 320, 46);
		loginButton.setBackground(Constants.Colors.PRIMARY);
		loginButton.setForeground(Constants.Colors.WHITE);
		loginButton.setFont(Constants.Fonts.EMPHASIZED_TITLE_2);
		add(loginButton);
	}

	public void display() {
		mainFrame.setVisible(false);
		mainFrame.setResizable(true);

		mainFrame.setTitle("Login");
		mainFrame.setResizable(false);
		mainFrame.setContentPane(this);
		mainFrame.pack();
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setVisible(true);
	}

	public JFrame getMainFrame() {
		return mainFrame;
	}
}

