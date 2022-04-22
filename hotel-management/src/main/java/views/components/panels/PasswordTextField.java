package views.components.panels;

import utils.Constants;
import views.components.TextFieldPlaceholder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PasswordTextField extends JPanel {
	// Constants
	private static final char HIDE_PASSWORD_ECHO_CHAR = (char) UIManager.get("PasswordField.echoChar");
	private static final char SHOW_PASSWORD_ECHO_CHAR = (char) 0;  // null

	private static final int VERTICAL_PADDING = 10;
	private static final int HORIZONTAL_PADDING = 10;
	private static final int SPACING = 10;

	private final Dimension size;

	private JPasswordField passwordField;
	private TextFieldPlaceholder placeholder;
	private ImagePanel leadingIcon;
	private ImagePanel visibilityIcon;
	private ImagePanel visibilityOffIcon;

	public PasswordTextField(String placeholderText, Dimension size) {
		super();
		this.size = size;

		initSubviews(placeholderText);
		setUpBoundsForSubviews();

		setLayout(null);
		setPreferredSize(size);

		setBackground(Constants.Colors.WHITE);
		setBorder(BorderFactory.createLineBorder(Constants.Colors.LIGHT_GRAY, 1));
	}

	public PasswordTextField(Dimension size) {
		this("", size);
	}

	private void initSubviews(String placeholderText) {
		this.passwordField = new JPasswordField();
		this.passwordField.setForeground(Constants.Colors.BLACK);
		this.passwordField.setFont(Constants.Fonts.BODY);
		this.passwordField.setBorder(null);
		add(passwordField);

		this.placeholder = new TextFieldPlaceholder(placeholderText, passwordField);
		this.passwordField.setLayout(null);

		this.visibilityIcon = new ImagePanel(Constants.IconNames.VISIBILITY_BLACK, 24, 24);
		this.visibilityIcon.setForeground(Constants.Colors.GRAY);
		this.visibilityIcon.setBackground(Constants.Colors.TRANSPARENT);
		add(visibilityIcon);

		this.visibilityOffIcon = new ImagePanel(Constants.IconNames.VISIBILITY_OFF_BLACK, 24, 24);
		this.visibilityOffIcon.setForeground(Constants.Colors.GRAY);
		this.visibilityOffIcon.setBackground(Constants.Colors.TRANSPARENT);
		this.visibilityOffIcon.setVisible(false);
		add(visibilityOffIcon);

		visibilityIcon.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				toggleVisibilityPasswordAction();
			}
		});

		visibilityOffIcon.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				toggleVisibilityPasswordAction();
			}
		});
	}

	private void setUpBoundsForSubviews() {
		// Set location for trailing (visibility) icon.
		Dimension visibilityIconSize = visibilityIcon.getImageSize();
		int xVisibilityIcon = size.width - HORIZONTAL_PADDING - visibilityIconSize.width;
		visibilityIcon.setBounds(xVisibilityIcon, VERTICAL_PADDING, visibilityIconSize.width, visibilityIconSize.height);
		visibilityOffIcon.setBounds(xVisibilityIcon, VERTICAL_PADDING, visibilityIconSize.width, visibilityIconSize.height);

		// Set location for text field.
		int xTextField = HORIZONTAL_PADDING;
		int yTextField = VERTICAL_PADDING;
		int passwordFieldWidth = xVisibilityIcon - xTextField - SPACING;
		passwordField.setBounds(xTextField, yTextField, passwordFieldWidth, 24);
		placeholder.setBounds(0, 0, passwordFieldWidth, 24);
	}

//	public JPasswordField getPasswordField() {
//		return passwordField;
//	}

	public String getPassword() {
		return String.valueOf(passwordField.getPassword());
	}

	public void setPassword(String value) {
		passwordField.setText(value);
	}

	public void toggleVisibilityPasswordAction() {
		boolean showingPassword = visibilityOffIcon.isVisible();
		char passwordEcho = showingPassword ? HIDE_PASSWORD_ECHO_CHAR : SHOW_PASSWORD_ECHO_CHAR;

		passwordField.setEchoChar(passwordEcho);
		visibilityIcon.setVisible(showingPassword);
		visibilityOffIcon.setVisible(!showingPassword);
	}

	public void setEnabled(boolean bool) { this.passwordField.setEnabled(bool); }
}
