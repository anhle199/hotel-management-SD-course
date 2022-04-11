package shared.panels;

import shared.TextFieldPlaceholder;
import utils.Constants;

import javax.swing.*;
import java.awt.*;

public class TextFieldPanel extends JPanel {

	private final int verticalPadding;
	private static final int HORIZONTAL_PADDING = 10;
	private static final int SPACING = 10;

	private final Dimension size;

	private JTextField textField;
	private TextFieldPlaceholder placeholder;
	private ImagePanel icon;
	private IconPosition position;

	public enum IconPosition { NONE, LEADING, TRAILING };

	public TextFieldPanel(String placeholderText, Dimension size) {
		super();
		this.size = size;

		verticalPadding = 10;

		initSubviewsWithoutIcon(placeholderText);
		setUpBoundsForSubviewsWithoutIcon();

		setLayout(null);
		setPreferredSize(size);

		this.textField.setBackground(Constants.Colors.WHITE);
		this.placeholder.setBackground(Constants.Colors.WHITE);
		setBackground(Constants.Colors.WHITE);
		setBorder(BorderFactory.createLineBorder(Constants.Colors.DARK_GRAY, 1));
	}

	public TextFieldPanel(String placeholderText, ImagePanel icon, IconPosition position, Dimension size) {
		super();
		this.size = size;
		this.position = position;

		Dimension iconSize = icon.getImageSize();
		if (iconSize.width != 0 && iconSize.height != 0) {
			verticalPadding = (size.height - iconSize.height) / 2;
		} else {
			verticalPadding = 10;
		}

		initSubviewsWithIcon(placeholderText, icon);
		setUpBoundsForSubviewsWithIcon();

		setLayout(null);
		setPreferredSize(size);

		this.textField.setBackground(Constants.Colors.WHITE);
		this.placeholder.setBackground(Constants.Colors.WHITE);
		this.icon.setBackground(Constants.Colors.WHITE);
		setBackground(Constants.Colors.WHITE);
		setBorder(BorderFactory.createLineBorder(Constants.Colors.DARK_GRAY, 1));
	}

	private void initSubviewsWithoutIcon(String placeholderText) {
		this.textField = new JTextField();
		this.textField.setForeground(Constants.Colors.BLACK);
		this.textField.setFont(Constants.Fonts.BODY);
		this.textField.setBorder(null);
		add(this.textField);

		this.placeholder = new TextFieldPlaceholder(placeholderText, textField);
		this.textField.setLayout(null);
	}

	private void initSubviewsWithIcon(String placeholderText, ImagePanel icon) {
		initSubviewsWithoutIcon(placeholderText);

		this.icon = icon;
		this.icon.setForeground(Constants.Colors.DARK_GRAY);
		add(this.icon);
	}

	private void setUpBoundsForSubviewsWithoutIcon() {
		int textFieldWidth = size.width - (2 * HORIZONTAL_PADDING);
		int textFieldHeight = size.height - (2 * verticalPadding);
		textField.setBounds(verticalPadding, HORIZONTAL_PADDING, textFieldWidth, textFieldHeight);
		placeholder.setBounds(0, 0, textFieldWidth, textFieldHeight);
	}

	private void setUpBoundsForSubviewsWithIcon() {
		Dimension iconSize = icon.getImageSize();
		int xIcon, xTextField;
		if (position == IconPosition.LEADING) {
			xIcon = HORIZONTAL_PADDING;
			xTextField = xIcon + iconSize.width + SPACING;
		} else {
			xIcon = size.width - HORIZONTAL_PADDING - iconSize.width;
			xTextField = HORIZONTAL_PADDING;
		}

		// Set location for icon.
		icon.setBounds(xIcon, verticalPadding, iconSize.width, iconSize.height);

		// Set location for text field.
		int yTextField = icon.getY();
		int textFieldWidth = size.width - iconSize.width - (HORIZONTAL_PADDING * 2) - SPACING;
		textField.setBounds(xTextField, yTextField, textFieldWidth, iconSize.height);
		placeholder.setBounds(0, 0, textFieldWidth, iconSize.height);
	}

	public JTextField getTextField() {
		return textField;
	}

}
