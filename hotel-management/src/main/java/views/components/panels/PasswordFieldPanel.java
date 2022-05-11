package views.components.panels;

import utils.Constants;
import views.components.TextFieldPlaceholder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PasswordFieldPanel extends JPanel {
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

	public PasswordFieldPanel(String placeholderText, Dimension size) {
		super();
		this.size = size;

		initSubviews(placeholderText);
		setUpBoundsForSubviews();

		setLayout(null);
		setPreferredSize(size);

		setBackground(Constants.Colors.WHITE);
		setBorder(BorderFactory.createLineBorder(Constants.Colors.LIGHT_GRAY, 1));
	}

	public PasswordFieldPanel(Dimension size) {
		this("Password", size);
	}

	private void initSubviews(String placeholderText) {
		this.passwordField = new JPasswordField();
		this.passwordField.setForeground(Constants.Colors.BLACK);
		this.passwordField.setFont(Constants.Fonts.BODY);
		this.passwordField.setBorder(null);
		add(passwordField);

		this.placeholder = new TextFieldPlaceholder(placeholderText, passwordField);
		this.passwordField.setLayout(null);

		this.leadingIcon = new ImagePanel(Constants.IconNames.LOCK_BLACK, 24, 24);
		this.leadingIcon.setForeground(Constants.Colors.GRAY);
		this.leadingIcon.setBackground(Constants.Colors.TRANSPARENT);
		add(this.leadingIcon);

		this.visibilityIcon = new ImagePanel(Constants.IconNames.VISIBILITY_BLACK, 24, 24);
		this.visibilityIcon.setForeground(Constants.Colors.GRAY);
		this.visibilityIcon.setBackground(Constants.Colors.TRANSPARENT);
		this.visibilityIcon.setVisible(false);
		add(visibilityIcon);

		this.visibilityOffIcon = new ImagePanel(Constants.IconNames.VISIBILITY_OFF_BLACK, 24, 24);
		this.visibilityOffIcon.setForeground(Constants.Colors.GRAY);
		this.visibilityOffIcon.setBackground(Constants.Colors.TRANSPARENT);
		add(visibilityOffIcon);

		visibilityIcon.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				setPasswordVisibility(false);
			}
		});

		visibilityOffIcon.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				setPasswordVisibility(true);
			}
		});
	}

	private void setUpBoundsForSubviews() {
		// Set location for leading icon.
		Dimension leadingIconSize = leadingIcon.getImageSize();
		leadingIcon.setBounds(HORIZONTAL_PADDING, VERTICAL_PADDING, leadingIconSize.width, leadingIconSize.height);

		// Set location for trailing (visibility) icon.
		Dimension visibilityIconSize = visibilityIcon.getImageSize();
		int xVisibilityIcon = size.width - HORIZONTAL_PADDING - visibilityIconSize.width;
		visibilityIcon.setBounds(xVisibilityIcon, VERTICAL_PADDING, visibilityIconSize.width, visibilityIconSize.height);
		visibilityOffIcon.setBounds(xVisibilityIcon, VERTICAL_PADDING, visibilityIconSize.width, visibilityIconSize.height);

		// Set location for text field.
		int xTextField = leadingIcon.getX() + leadingIconSize.width + SPACING;
		int yTextField = leadingIcon.getY();
		int passwordFieldWidth = xVisibilityIcon - xTextField - SPACING;
		passwordField.setBounds(xTextField, yTextField, passwordFieldWidth, leadingIconSize.height);
		placeholder.setBounds(0, 0, passwordFieldWidth, leadingIconSize.height);
	}

	public String getPassword() {
		return String.valueOf(passwordField.getPassword());
	}

	public void setPassword(String value) {
		passwordField.setText(value);
	}

	public void setPasswordVisibility(boolean isVisiblePassword) {
		char passwordEcho = isVisiblePassword ? SHOW_PASSWORD_ECHO_CHAR : HIDE_PASSWORD_ECHO_CHAR;

		passwordField.setEchoChar(passwordEcho);
		visibilityIcon.setVisible(isVisiblePassword);
		visibilityOffIcon.setVisible(!isVisiblePassword);
	}

}
