package views.dialogs;

import utils.Constants;
import utils.UtilFunctions;
import views.components.panels.DateChooserPanel;

import javax.swing.*;
import java.awt.*;

public class RangeDatePickerDialog extends JDialog {

	private DateChooserPanel startDateChooserPanel;
	private DateChooserPanel endDateChooserPanel;
	private JButton saveButton;
	private JButton cancelButton;

	public RangeDatePickerDialog(JFrame frame) {
		super(frame, "Range Date Picker", true);

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setPreferredSize(new Dimension(360, 200));
		initSubviews(panel);

		setResizable(false);
		setContentPane(panel);
		pack();
		setLocationRelativeTo(null);
	}

	private void initSubviews(JPanel panel) {
		// Sizes and coordinates
		Dimension labelSize = new Dimension(80, 40);
		Dimension dateChooserSize = new Dimension(220, 40);
		int padding = 20;
		int spacingTextFields = 12;
		int xTextField = padding * 2 + labelSize.width;

		// Start Date Label.
		JLabel startDateLabel = new JLabel("Start date");
		startDateLabel.setBounds(padding, padding, labelSize.width, labelSize.height);
		startDateLabel.setFont(Constants.Fonts.HEADLINE);
		panel.add(startDateLabel);

		// Start Date Chooser Panel.
		startDateChooserPanel = new DateChooserPanel(2022, 2023);
		startDateChooserPanel.setBounds(xTextField, startDateLabel.getY(), dateChooserSize.width, dateChooserSize.height);
		panel.add(startDateChooserPanel);

		// End Date Label.
		JLabel endDateLabel = new JLabel("End date");
		endDateLabel.setBounds(padding, startDateLabel.getY() + labelSize.height + spacingTextFields, labelSize.width, labelSize.height);
		endDateLabel.setFont(Constants.Fonts.HEADLINE);
		panel.add(endDateLabel);

		// End Date Chooser Panel.
		endDateChooserPanel = new DateChooserPanel(2022, 2023);
		endDateChooserPanel.setBounds(xTextField, endDateLabel.getY(), dateChooserSize.width, dateChooserSize.height);
		panel.add(endDateChooserPanel);

		// Edit Button.
		saveButton = new JButton("Save");
		saveButton.setBounds(70, endDateLabel.getY() + endDateChooserPanel.getHeight() + padding, 100, dateChooserSize.height);
		UtilFunctions.configureTopBarButtonOnMainThread(saveButton);
		panel.add(saveButton);

		// Close Button.
		cancelButton = new JButton("Cancel");
		cancelButton.setBounds(saveButton.getX() + saveButton.getWidth() + padding, saveButton.getY(), 100, dateChooserSize.height);
		UtilFunctions.configureBorderedButtonOnMainThread(cancelButton);
		UtilFunctions.addHoverEffectsForBorderedButton(cancelButton);
		panel.add(cancelButton);
	}

	public DateChooserPanel getStartDateChooserPanel() {
		return startDateChooserPanel;
	}

	public DateChooserPanel getEndDateChooserPanel() {
		return endDateChooserPanel;
	}

	public JButton getSaveButton() {
		return saveButton;
	}

	public JButton getCancelButton() {
		return cancelButton;
	}

}
