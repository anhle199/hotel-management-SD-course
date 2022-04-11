package shared.panels;

import shared.TextFieldPlaceholder;
import utils.Constants;

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
//	private final int cornerRadius;
//	private Shape shape;

	private JPasswordField passwordField;
	private TextFieldPlaceholder placeholder;
	private ImagePanel leadingIcon;
	private ImagePanel visibilityIcon;
	private ImagePanel visibilityOffIcon;

	public PasswordFieldPanel(String placeholderText, Dimension size) {
		super();
		this.size = size;
//		this.cornerRadius = cornerRadius;

		initSubviews(placeholderText);
		setUpBoundsForSubviews();

		setLayout(null);
		setPreferredSize(size);

		setBackground(Constants.Colors.WHITE);
		setBorder(BorderFactory.createLineBorder(Constants.Colors.DARK_GRAY, 1));
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

		this.leadingIcon = new ImagePanel(Constants.IconNames.LOCK, 24, 24);
		this.leadingIcon.setForeground(Constants.Colors.DARK_GRAY);
		this.leadingIcon.setBackground(Constants.Colors.TRANSPARENT);
		add(this.leadingIcon);

		this.visibilityIcon = new ImagePanel(Constants.IconNames.VISIBILITY, 24, 24);
		this.visibilityIcon.setForeground(Constants.Colors.DARK_GRAY);
		this.visibilityIcon.setBackground(Constants.Colors.TRANSPARENT);
		add(visibilityIcon);

		this.visibilityOffIcon = new ImagePanel(Constants.IconNames.VISIBILITY_OFF, 24, 24);
		this.visibilityOffIcon.setForeground(Constants.Colors.DARK_GRAY);
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

	public JPasswordField getPasswordField() {
		return passwordField;
	}

	public String getPasswordValue() {
		return String.valueOf(passwordField.getPassword());
	}

	public void toggleVisibilityPasswordAction() {
		boolean showingPassword = visibilityOffIcon.isVisible();
		char passwordEcho = showingPassword ? HIDE_PASSWORD_ECHO_CHAR : SHOW_PASSWORD_ECHO_CHAR;

		passwordField.setEchoChar(passwordEcho);
		visibilityIcon.setVisible(showingPassword);
		visibilityOffIcon.setVisible(!showingPassword);
	}

//	protected void paintComponent(Graphics graphics) {
//		graphics.setColor(getBackground());
//		graphics.fillRoundRect(
//				0, 0,
//				getWidth() - 1, getHeight() - 1,
//				cornerRadius, cornerRadius
//		);
//
//		setUpBoundsForSubviews();
//
//		super.paintComponent(graphics);
//	}
//
//	protected void paintBorder(Graphics graphics) {
//		graphics.setColor(Constants.Colors.TRANSPARENT);
//		graphics.drawRoundRect(
//				0, 0,
//				getWidth() - 1, getHeight() - 1,
//				cornerRadius, cornerRadius
//		);
//
//		setUpBoundsForSubviews();
//	}
//
//	public boolean contains(int x, int y) {
//		if (shape == null || !shape.getBounds().equals(getBounds())) {
//			shape = new RoundRectangle2D.Float(
//					0, 0,
//					getWidth() - 1, getHeight() - 1,
//					cornerRadius, cornerRadius
//			);
//		}
//
//		return shape.contains(x, y);
//	}
}
