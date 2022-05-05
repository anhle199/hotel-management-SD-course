package controllers.statistics;

import dao.ImportInvoiceDAO;
import dao.ReceiptDAO;
import dao.RentalReceiptDAO;
import db.DBConnectionException;
import db.SingletonDBConnection;
import utils.Constants;
import utils.Pair;
import views.components.dialogs.ConnectionErrorDialog;
import views.panels.statistics.StatisticsPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StatisticsController implements ActionListener, ItemListener {

	private final StatisticsPanel statisticsPanel;
	private final ConnectionErrorDialog connectionErrorDialog;

	public StatisticsController(StatisticsPanel statisticsPanel, JFrame mainFrame) {
		this.statisticsPanel = statisticsPanel;
		this.connectionErrorDialog = new ConnectionErrorDialog(mainFrame);

		// Add action listeners
		this.statisticsPanel.getRefreshButton().addActionListener(this);
		this.connectionErrorDialog.getReconnectButton().addActionListener(this);

		// Add item listeners
		this.statisticsPanel.getStatisticComboBox().addItemListener(this);
		this.statisticsPanel.getMonthComboBox().addItemListener(this);
	}

	public void displayUI() {
		refreshButtonAction();
		statisticsPanel.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == statisticsPanel.getRefreshButton()) {
			refreshButtonAction();
		} else if (event.getSource() == connectionErrorDialog.getReconnectButton()) {
			reconnectButtonAction();
		}
	}

	@Override
	public void itemStateChanged(ItemEvent event) {
		refreshButtonAction();
	}

	private void refreshButtonAction() {
		statisticsPanel.resetChart();
		statisticActionImp();
	}

	private void reconnectButtonAction() {
		connectionErrorDialog.setExitOnCloseButton(false);
		connectionErrorDialog.setVisible(false);

		SingletonDBConnection.getInstance().connect();
		refreshButtonAction();
	}

	private void statisticActionImp() {
		try {
			String statsOption = String.valueOf(statisticsPanel.getStatisticComboBox().getSelectedItem());

			ArrayList<Pair<String, Number>> rowKeysAndStatsValues = getRowKeysAndStatisticValues(statsOption);
			ArrayList<String> rowKeys = new ArrayList<>();
			ArrayList<Number> values = new ArrayList<>();
			for (Pair<String, Number> item: rowKeysAndStatsValues) {
				rowKeys.add(item.getLeftValue());
				values.add(item.getRightValue());
			}

			ArrayList<String> columnKeys = new ArrayList<>(List.of(""));

			String month = String.valueOf(statisticsPanel.getMonthComboBox().getSelectedItem());
			String chartTitle = statisticsPanel.createChartTitleFrom(statsOption, month);

			statisticsPanel.setChartTitle(chartTitle);
			statisticsPanel.setValues(values, rowKeys, columnKeys);
		} catch (DBConnectionException e) {
			SwingUtilities.invokeLater(() -> connectionErrorDialog.setVisible(true));
			e.printStackTrace();
		}
	}

	private ArrayList<Pair<String, Number>> getRowKeysAndStatisticValues(String statsOption)
	throws DBConnectionException {
		ArrayList<Pair<String, Number>> rowKeysAndStatsValues = new ArrayList<>();
		String monthInEnglish = String.valueOf(statisticsPanel.getMonthComboBox().getSelectedItem());
		int month = getMonthValue(monthInEnglish);
		int year = LocalDate.now().getYear();

		switch (statsOption) {
			case "Statistics of revenue for each room type in month": {
				RentalReceiptDAO daoModel = new RentalReceiptDAO();
				rowKeysAndStatsValues.addAll(daoModel.getRevenueForEachRoomTypeByMonthAndYear(month, year));
				break;
			}
			case "Statistics of room occupancy rate (top 5 rooms) in month": {
				RentalReceiptDAO daoModel = new RentalReceiptDAO();
				rowKeysAndStatsValues.addAll(daoModel.getTopFiveRoomOccupancyRateByMonthAndYear(month, year));
				break;
			}
			case "Statistics of revenue of purchased products (top 5 products) in month": {
				ReceiptDAO daoModel = new ReceiptDAO();
				rowKeysAndStatsValues.addAll(daoModel.getTopFiveMostPurchasedProductsByMonthAndYear(month, year));
				break;
			}
			case "Statistics of costs of imported products (top 5 products) in month": {
				ImportInvoiceDAO daoModel = new ImportInvoiceDAO();
				rowKeysAndStatsValues.addAll(daoModel.getTopFiveMostImportedProductsByMonthAndYear(month, year));
				break;
			}
		}

		return rowKeysAndStatsValues;
	}

	private int getMonthValue(String monthInEnglish) {
		for (int i = 0; i < Constants.MONTHS_IN_ENGLISH.length; i++)
			if (monthInEnglish.equals(Constants.MONTHS_IN_ENGLISH[i]))
				return i + 1;

		return 1;
	}

}
