package views.dialogs;

import utils.Constants;
import utils.UtilFunctions;
import views.components.panels.PriceFieldPanel;

import javax.swing.*;
import java.awt.*;

public class RangePriceInputDialog extends JDialog {

	private PriceFieldPanel minPriceField;
	private PriceFieldPanel maxPriceField;
	private JButton saveButton;
	private JButton cancelButton;

	public RangePriceInputDialog(JFrame frame) {
		super(frame, "Range Price Input", true);

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setPreferredSize(new Dimension(300, 200));
		initSubviews(panel);

		setResizable(false);
		setContentPane(panel);
		pack();
		setLocationRelativeTo(null);
	}

	private void initSubviews(JPanel panel) {
		// Sizes and coordinates
		Dimension labelSize = new Dimension(80, 40);
		Dimension fieldSize = new Dimension(160, 40);
		int padding = 20;
		int spacingTextFields = 12;
		int xTextField = padding * 2 + labelSize.width;

		// Min Price Label.
		JLabel minPriceLabel = new JLabel("Min price");
		minPriceLabel.setBounds(padding, padding, labelSize.width, labelSize.height);
		minPriceLabel.setFont(Constants.Fonts.HEADLINE);
		panel.add(minPriceLabel);

		// Min Price Field Panel.
		minPriceField = new PriceFieldPanel("$", fieldSize);
		minPriceField.setBounds(xTextField, minPriceLabel.getY(), fieldSize.width, fieldSize.height);
		panel.add(minPriceField);

		// Max Price Label.
		JLabel maxPriceLabel = new JLabel("Max price");
		maxPriceLabel.setBounds(padding, minPriceLabel.getY() + labelSize.height + spacingTextFields, labelSize.width, labelSize.height);
		maxPriceLabel.setFont(Constants.Fonts.HEADLINE);
		panel.add(maxPriceLabel);

		// Max Price Field Panel.
		maxPriceField = new PriceFieldPanel("$", fieldSize);
		maxPriceField.setBounds(xTextField, maxPriceLabel.getY(), fieldSize.width, fieldSize.height);
		panel.add(maxPriceField);

		// Edit Button.
		saveButton = new JButton("Save");
		saveButton.setBounds(40, maxPriceLabel.getY() + maxPriceField.getHeight() + padding, 100, fieldSize.height);
		UtilFunctions.configureTopBarButtonOnMainThread(saveButton);
		panel.add(saveButton);

		// Close Button.
		cancelButton = new JButton("Cancel");
		cancelButton.setBounds(saveButton.getX() + saveButton.getWidth() + padding, saveButton.getY(), 100, fieldSize.height);
		UtilFunctions.configureBorderedButtonOnMainThread(cancelButton);
		UtilFunctions.addHoverEffectsForBorderedButton(cancelButton);
		panel.add(cancelButton);
	}

	public PriceFieldPanel getMinPriceField() {
		return minPriceField;
	}

	public PriceFieldPanel getMaxPriceField() {
		return maxPriceField;
	}

	public JButton getSaveButton() {
		return saveButton;
	}

	public JButton getCancelButton() {
		return cancelButton;
	}

}
