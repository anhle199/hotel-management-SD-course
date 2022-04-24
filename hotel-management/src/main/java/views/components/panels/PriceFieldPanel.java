package views.components.panels;

import utils.Constants;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.NumberFormat;

public class PriceFieldPanel extends JPanel {

	private JFormattedTextField priceTextField;
	private final Dimension size;

	public PriceFieldPanel(String currency, Dimension size) {
		super();
		this.size = size;

		setLayout(null);
		setPreferredSize(size);

		initSubviews(currency);

		setBackground(Constants.Colors.WHITE);
		setBorder(BorderFactory.createLineBorder(Constants.Colors.LIGHT_GRAY, 1));
	}

	private void initSubviews(String currency) {
		// Number formatter without grouping separator
		NumberFormat numberFormat = NumberFormat.getIntegerInstance();
		numberFormat.setGroupingUsed(false);

		// Number formatter
		NumberFormatter priceFormatter = new NumberFormatter(numberFormat);
		priceFormatter.setMinimum(Constants.MIN_PRICE);
		priceFormatter.setMaximum(Constants.MAX_PRICE);
		priceFormatter.setAllowsInvalid(false);
		priceFormatter.setCommitsOnValidEdit(true);

		// Price Text Field.
		priceTextField = new JFormattedTextField(priceFormatter);
		priceTextField.setBounds(0, 0, size.width - 10, size.height);
		priceTextField.setBorder(null);
		priceTextField.setHorizontalAlignment(SwingConstants.RIGHT);
		add(priceTextField);

		// Currency Label.
		JLabel currencyLabel = new JLabel(currency, SwingConstants.LEFT);
		currencyLabel.setBounds(priceTextField.getWidth() + 2, 0, 8, size.height);
		currencyLabel.setBackground(Constants.Colors.WHITE);
		add(currencyLabel);
	}

	public int getValue() {
		return (int) priceTextField.getValue();
	}

	public void setValue(int value) {
		if (Constants.MIN_PRICE <= value && value <= Constants.MAX_PRICE)
			priceTextField.setValue(value);
	}

}
