package controllers;

import controllers.rooms.FilterBarController;
import views.components.panels.DateChooserPanel;
import views.components.panels.TextFieldPanel;
import views.dialogs.RangeDatePickerDialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class RangeDatePickerController implements ActionListener {
	private final RangeDatePickerDialog rangeDatePickerDialog;
	private final TextFieldPanel rangeDatePickerPanel;
	private final FilterBarController filterBarController;

	// year, month, day
	private final ArrayList<Integer> initialStartDate = new ArrayList<>();
	private final ArrayList<Integer> initialEndDate = new ArrayList<>();

	public RangeDatePickerController(
			RangeDatePickerDialog dialog,
			TextFieldPanel rangeDatePickerPanel,
			FilterBarController filterBarController
	) {
		this.rangeDatePickerDialog = dialog;
		this.rangeDatePickerPanel = rangeDatePickerPanel;
		this.filterBarController = filterBarController;


		String rangeDateInString = rangeDatePickerPanel.getText();
		String[] dates = rangeDateInString.split(" - ");

		// Save initial start date.
		String[] startDate = dates[0].split("/");
		this.initialStartDate.add(Integer.parseInt(startDate[0]));
		this.initialStartDate.add(Integer.parseInt(startDate[1]));
		this.initialStartDate.add(Integer.parseInt(startDate[2]));

		// Save initial end date.
		String[] endDate = dates[0].split("/");
		this.initialEndDate.add(Integer.parseInt(endDate[0]));
		this.initialEndDate.add(Integer.parseInt(endDate[1]));
		this.initialEndDate.add(Integer.parseInt(endDate[2]));

		// Add action listeners
		this.rangeDatePickerDialog.getSaveButton().addActionListener(this);
		this.rangeDatePickerDialog.getCancelButton().addActionListener(this);
	}

	public void displayUI() {
		rangeDatePickerDialog.getStartDateChooserPanel()
							 .setSelectedDate(
									 initialStartDate.get(0),
									 initialStartDate.get(1),
									 initialStartDate.get(2)
							 );

		rangeDatePickerDialog.getEndDateChooserPanel()
							 .setSelectedDate(
									 initialStartDate.get(0),
									 initialStartDate.get(1),
									 initialStartDate.get(2)
							 );

		rangeDatePickerDialog.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == rangeDatePickerDialog.getSaveButton()) {
			saveButtonAction();
		} else if (event.getSource() == rangeDatePickerDialog.getCancelButton()) {
			rangeDatePickerDialog.setVisible(false);
		}
	}

	private void saveButtonAction() {
		// Obtain start date.
		DateChooserPanel startDateChooserPanel = rangeDatePickerDialog.getStartDateChooserPanel();
		ArrayList<Integer> startDate = new ArrayList<>();
		startDate.add(startDateChooserPanel.getSelectedYear());
		startDate.add(startDateChooserPanel.getSelectedMonth());
		startDate.add(startDateChooserPanel.getSelectedDay());

		// Obtain end date.
		DateChooserPanel endDateChooserPanel = rangeDatePickerDialog.getEndDateChooserPanel();
		ArrayList<Integer> endDate = new ArrayList<>();
		endDate.add(endDateChooserPanel.getSelectedYear());
		endDate.add(endDateChooserPanel.getSelectedMonth());
		endDate.add(endDateChooserPanel.getSelectedDay());

		if (!initialStartDate.equals(startDate) || !initialEndDate.equals(endDate)) {
			String rangeDateText = String.format(
					"%d/%d/%d - %d/%d/%d",
					startDate.get(0), startDate.get(1), startDate.get(2),
					endDate.get(0), endDate.get(1), endDate.get(2)
			);
			rangeDatePickerPanel.setText(rangeDateText);

			rangeDatePickerDialog.setVisible(false);
			filterBarController.filterAction();
		}
	}

}
