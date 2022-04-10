package shared;

import utils.Constants;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class TextFieldPlaceholder extends JLabel implements DocumentListener, FocusListener {

	private final JTextComponent textField;

	public TextFieldPlaceholder(String placeholder, JTextComponent textField) {
		super(placeholder);
		this.textField = textField;

		setForeground(Constants.Colors.DARK_GRAY);
		setFont(Constants.Fonts.BODY);
		textField.setLayout(new BorderLayout());
		textField.add(this);

		textField.getDocument().addDocumentListener(this);
		textField.addFocusListener(this);
	}

	@Override
	public void focusGained(FocusEvent e) {}

	@Override
	public void focusLost(FocusEvent e) {
		setVisible(textField.getText().isEmpty());
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		setVisible(false);
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		if (textField.getText().isEmpty()) {
			setVisible(true);
		}
	}

	@Override
	public void changedUpdate(DocumentEvent e) {}

}
