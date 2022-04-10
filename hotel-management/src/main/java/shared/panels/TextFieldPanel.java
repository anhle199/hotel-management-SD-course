package shared.panels;

import utils.Constants;
import shared.TextFieldPlaceholder;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class TextFieldPanel extends JPanel {

	private static final int VERTICAL_PADDING = 10;
	private static final int HORIZONTAL_PADDING = 10;
	private static final int SPACING = 10;

	private final Dimension size;
	private final int cornerRadius;
	private Shape shape;

	private JTextField textField;
	private TextFieldPlaceholder placeholder;
	private ImagePanel leadingIcon;

	public TextFieldPanel(String placeholderText, ImagePanel leadingIcon, Dimension size, int cornerRadius) {
		super();
		this.size = size;
		this.cornerRadius = cornerRadius;

		initSubviews(placeholderText, leadingIcon);
		setUpBoundsForSubviews();

		setLayout(null);
		setOpaque(false);
	}

	private void initSubviews(String placeholderText, ImagePanel leadingIcon) {
		this.textField = new JTextField();
		this.textField.setForeground(Constants.Colors.BLACK);
		this.textField.setFont(Constants.Fonts.BODY);
		this.textField.setBorder(null);
		add(this.textField);

		this.placeholder = new TextFieldPlaceholder(placeholderText, textField);
		this.textField.setLayout(null);

		this.leadingIcon = leadingIcon;
		this.leadingIcon.setForeground(Constants.Colors.DARK_GRAY);
		this.leadingIcon.setBackground(Constants.Colors.TRANSPARENT);
		add(this.leadingIcon);
	}

	private void setUpBoundsForSubviews() {
		// Set location for leading icon.
		Dimension iconSize = leadingIcon.getImageSize();
		leadingIcon.setBounds(HORIZONTAL_PADDING, VERTICAL_PADDING, iconSize.width, iconSize.height);

		// Set location for text field.
		int xTextField = leadingIcon.getX() + iconSize.width + SPACING;
		int yTextField = leadingIcon.getY();
		int textFieldWidth = size.width - xTextField - HORIZONTAL_PADDING;
		textField.setBounds(xTextField, yTextField, textFieldWidth, iconSize.height);
		placeholder.setBounds(0, 0, textFieldWidth, iconSize.height);
	}

	protected void paintComponent(Graphics graphics) {
		graphics.setColor(getBackground());
		graphics.fillRoundRect(
				0, 0,
				getWidth() - 1, getHeight() - 1,
				cornerRadius, cornerRadius
		);

		setUpBoundsForSubviews();

		super.paintComponent(graphics);
	}

	protected void paintBorder(Graphics graphics) {
		graphics.setColor(Constants.Colors.TRANSPARENT);
		graphics.drawRoundRect(
				0, 0,
				getWidth() - 1, getHeight() - 1,
				cornerRadius, cornerRadius
		);

		setUpBoundsForSubviews();
	}

	public boolean contains(int x, int y) {
		if (shape == null || !shape.getBounds().equals(getBounds())) {
			shape = new RoundRectangle2D.Float(
					0, 0,
					getWidth() - 1, getHeight() - 1,
					cornerRadius, cornerRadius
			);
		}

		return shape.contains(x, y);
	}

	public JTextField getTextField() {
		return textField;
	}

}
