package views.panels.statistics;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import utils.Constants;
import utils.UtilFunctions;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StatisticsPanel extends JPanel {
	// Constants.
	public static final String[] STATISTIC_OPTION_NAMES = {
			"Statistics of revenue by room type in month",
			"Statistics of room occupancy rate (top 5 rooms) in month",
			"Statistics of revenue of purchased products (top 5 products) in month",
			"Statistics of costs of imported products (top 5 products) in month",
	};
	public static final String[] VALUE_AXIS_NAMES = {
			"Number of rooms", "Number of rented times", "Prices", "Prices"
	};
	public static final String[] CATEGORY_NAMES = {
			"Room type", "Room name", "Product name", "Product name"
	};

	// Bar chart.
	private DefaultCategoryDataset dataset;

	// Components.
	private JComboBox<String> statisticComboBox;
	private JComboBox<String> monthComboBox;
	private JButton refreshButton;
	private ChartPanel chartPanel;

	public StatisticsPanel() {
		super();
		setLayout(null);

		initTopBar();
		initBarChart();
	}

	private void initTopBar() {
		// Statistic Combo Box.
		statisticComboBox = new JComboBox<>(STATISTIC_OPTION_NAMES);
		statisticComboBox.setBounds(20, 20, 480, 40);
		add(statisticComboBox);

		// Month Combo Box.
		monthComboBox = new JComboBox<>(Constants.MONTHS_IN_ENGLISH);
		monthComboBox.setBounds(512, 20, 100, 40);
		add(monthComboBox);

		// Refresh Button.
		refreshButton = new JButton("Refresh");
		refreshButton.setBounds(950, 20, 110, 40);
		UtilFunctions.configureTopBarButtonOnMainThread(refreshButton);
		add(refreshButton);

		statisticComboBox.addItemListener(event -> {
			resetChart();
			generateChartData();
		});
		monthComboBox.addItemListener(event -> {
			resetChart();
			generateChartData();
		});
		refreshButton.addActionListener(event -> {
			resetChart();
			generateChartData();
		});
	}

	private void initBarChart() {
		dataset = new DefaultCategoryDataset();

		chartPanel = new ChartPanel(createBarChart());
		chartPanel.setBounds(20, 80, 1040, 707);
		chartPanel.setBorder(BorderFactory.createLineBorder(Constants.Colors.TABLE_BORDER_COLOR, 1));
		add(chartPanel);
	}

	// Column key is one of all categories.
	// Row key is one of all columns in column key.
	public void setValue(Number value, String rowKey, String columnKey) {
		dataset.setValue(value, rowKey, columnKey);
	}

	public void setValues(ArrayList<Number> values, ArrayList<String> rowKeys, ArrayList<String> columnKeys) {
		int index = 0;

		for (String columnKey : columnKeys) {
			for (String rowKey : rowKeys) {
				dataset.setValue(values.get(index), rowKey, columnKey);
				++index;
			}
		}

		double upperBound = chartPanel.getChart()
									  .getCategoryPlot()
									  .getRangeAxis()
									  .getUpperBound();

		if (upperBound < 10.0) {
			chartPanel.getChart()
					  .getCategoryPlot()
					  .getRangeAxis()
					  .setUpperBound(upperBound + 10.0);
		}
	}

	public void removeAllRows() {
		List<String> rowKeys = dataset.getRowKeys();
		for (String rowKey : rowKeys)
			dataset.removeRow(rowKey);
	}

	public void removeAllColumns() {
		List<String> columnKeys = dataset.getColumnKeys();
		for (String columnKey : columnKeys)
			dataset.removeColumn(columnKey);
	}

	public void removeAllValues() {
		dataset.clear();
	}

	public String createChartTitleFrom(String statisticOption, String monthInEnglish) {
		StringBuilder stringBuilder = new StringBuilder(statisticOption);

		// Remove "month" string in statistic option.
		// 5 is the length of "month" string.
		int length = statisticOption.length();
		stringBuilder.delete(length - 5, length);

		return stringBuilder.append(monthInEnglish).toString();
	}

	public void setChartTitle(String chartTitle) {
		chartPanel.getChart().setTitle(chartTitle);
	}

	private JFreeChart createBarChart() {
		String monthInEnglish = String.valueOf(monthComboBox.getSelectedItem());
		String statisticOption = String.valueOf(statisticComboBox.getSelectedItem());

		int index = getRowKeyIndex(statisticOption);

		return ChartFactory.createBarChart(
				createChartTitleFrom(statisticOption, monthInEnglish),
				CATEGORY_NAMES[index],
				VALUE_AXIS_NAMES[index],
				dataset,
				PlotOrientation.VERTICAL,
				true,
				true,
				false
		);
	}

	public void resetChart() {
		removeAllValues();
		removeAllRows();
		removeAllColumns();

		chartPanel.setChart(createBarChart());
	}

	private int getRowKeyIndex(String statisticOption) {
		for (int i = 0; i < STATISTIC_OPTION_NAMES.length; i++)
			if (statisticOption.equals(STATISTIC_OPTION_NAMES[i]))
				return i;

		return 0;
	}

	private void generateChartData() {
		String statisticOption = String.valueOf(statisticComboBox.getSelectedItem());
		ArrayList<String> columnKeys = new ArrayList<>(List.of(""));
		ArrayList<Number> values = new ArrayList<>();

		switch (statisticOption) {
			case "Statistics of revenue by room type in month": {
				ArrayList<String> rowKeys = new ArrayList<>(
						List.of("Affordable", "Normal", "Luxury")
				);

				Random numberGenerator = new Random();
				for (int i = 0; i < rowKeys.size(); i++) {
					values.add(numberGenerator.nextInt(190) + 10);
				}

				setValues(values, rowKeys, columnKeys);
				break;
			}
			case "Statistics of room occupancy rate (top 5 rooms) in month": {
				ArrayList<String> rowKeys = new ArrayList<>(
						List.of("Room 101", "Room 103", "Room 201", "Room 303", "Room 405")
				);

				Random numberGenerator = new Random();
				for (int i = 0; i < rowKeys.size(); i++) {
					values.add(numberGenerator.nextInt(190) + 10);
				}

				setValues(values, rowKeys, columnKeys);
				break;
			}
			case "Statistics of revenue of purchased products (top 5 products) in month":
			case "Statistics of costs of imported products (top 5 products) in month": {

				ArrayList<String> rowKeys = new ArrayList<>(
						List.of("Product 1", "Product 2", "Product 3", "Product 4", "Product 5")
				);

				Random numberGenerator = new Random();
				for (int i = 0; i < rowKeys.size(); i++) {
					values.add((numberGenerator.nextInt(190) + 10) * 10000);
				}

				setValues(values, rowKeys, columnKeys);
				break;
			}
		}
	}

	public JComboBox<String> getStatisticComboBox() {
		return statisticComboBox;
	}

	public JComboBox<String> getMonthComboBox() {
		return monthComboBox;
	}

	public JButton getRefreshButton() {
		return refreshButton;
	}

}
