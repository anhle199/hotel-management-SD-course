package views.components.panels;

import utils.Constants;
import utils.UtilFunctions;

import javax.swing.*;
import java.awt.*;
import java.sql.Timestamp;
import java.time.Year;

public class DateChooserPanel extends JPanel {
	// Constants
	private static final Integer[] MONTHS = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};

	// Components
	private JComboBox<Integer> yearOptions;
	private JComboBox<Integer> monthOptions;
	private JComboBox<Integer> dayOptions;

	public DateChooserPanel(int minYear, int maxYear) {
		super();

		setLayout(null);
		setPreferredSize(new Dimension(220, 40));
		initComponents();

		minYear = Math.max(minYear, 2022);
		maxYear = Math.max(maxYear, minYear);

		setRangeYear(minYear, maxYear);
		resetRangeDay();

		// -------------------------------------------------
		yearOptions.addActionListener((event) -> monthOptions.setSelectedIndex(0));
		monthOptions.addActionListener((event) -> resetRangeDay());
		// -------------------------------------------------
	}

	private void initComponents() {
		yearOptions = new JComboBox<>();
		yearOptions.setBounds(0, 0, 70, 40);
		add(yearOptions);

		monthOptions = new JComboBox<>(MONTHS);
		monthOptions.setBounds(90, 0, 55, 40);
		add(monthOptions);

		dayOptions = new JComboBox<>();
		dayOptions.setBounds(165, 0, 55, 40);
		add(dayOptions);
	}

	public void setRangeYear(int minYear, int maxYear) {
		if (minYear >= 1930 && maxYear >= minYear) {
			int count = maxYear - minYear + 1;
			Integer[] years = new Integer[count];
			for (int i = 0; i < count; i++)
				years[i] = minYear + i;

			yearOptions.setModel(new DefaultComboBoxModel<>(years));
			resetRangeDay();
		}
	}

	public void setSelectedDate(int year, int month, int day) {
		yearOptions.setSelectedItem(year);
		monthOptions.setSelectedItem(month);
		dayOptions.setSelectedItem(day);
	}

	public void setSelectedDate(Timestamp timestamp) {
		String timestampAsString = UtilFunctions.formatTimestamp(
				Constants.TIMESTAMP_WITHOUT_NANOSECOND,
				timestamp
		);

		setSelectedDate(
				Integer.parseInt(timestampAsString.substring(0, 4)),
				Integer.parseInt(timestampAsString.substring(5, 7)),
				Integer.parseInt(timestampAsString.substring(8, 10))
		);
	}

	public int getSelectedYear() {
		return (int) yearOptions.getSelectedItem();
	}

	public int getSelectedMonth() {
		return (int) monthOptions.getSelectedItem();
	}

	public int getSelectedDay() {
		return (int) dayOptions.getSelectedItem();
	}

	private Integer[] getRangeDay(int year, int month) {
		int countDay;
		switch (month) {
			case 2:
				Year yearInstance = Year.of(year);
				countDay = (byte) (yearInstance.isLeap() ? 29 : 28);
				break;
			case 4: case 6: case 9: case 11:
				countDay = 30;
				break;
			default:  // 1, 3, 5, 7, 8, 10, 12
				countDay = 31;
				break;
		}

		Integer[] days = new Integer[countDay];
		for (int i = 0; i < countDay; i++)
			days[i] = i + 1;

		return days;
	}

	private void resetRangeDay() {
		int year = (int) yearOptions.getSelectedItem();
		int month = (int) monthOptions.getSelectedItem();

		dayOptions.setModel(new DefaultComboBoxModel<>(getRangeDay(year, month)));
	}

	public void makeDefaultSelectedItem() {
		yearOptions.setSelectedIndex(0);
	}

	public void setEnabledSelection(boolean enabled) {
		yearOptions.setEnabled(enabled);
		monthOptions.setEnabled(enabled);
		dayOptions.setEnabled(enabled);
	}

}
