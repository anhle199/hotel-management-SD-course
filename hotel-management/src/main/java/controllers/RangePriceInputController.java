package controllers;

import controllers.rooms.FilterBarController;
import views.components.panels.DateChooserPanel;
import views.components.panels.PriceFieldPanel;
import views.components.panels.TextFieldPanel;
import views.dialogs.RangeDatePickerDialog;
import views.dialogs.RangePriceInputDialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class RangePriceInputController implements ActionListener {
	private final RangePriceInputDialog rangePriceInputDialog;
	private final TextFieldPanel rangePriceInputPanel;
	private final FilterBarController filterBarController;

	// year, month, day
	private final int initialMinPrice;
	private final int initialMaxPrice;

	public RangePriceInputController(
			RangePriceInputDialog dialog,
			TextFieldPanel rangePriceInputPanel,
			FilterBarController filterBarController
	) {
		this.rangePriceInputDialog = dialog;
		this.rangePriceInputPanel = rangePriceInputPanel;
		this.filterBarController = filterBarController;

		// Save initial min and max price.
		String[] prices = rangePriceInputPanel.getText().split(" - ");
		this.initialMinPrice = Integer.parseInt(prices[0].substring(0, prices[0].length() - 1));
		this.initialMaxPrice = Integer.parseInt(prices[1].substring(0, prices[1].length() - 1));

		// Add action listeners
		this.rangePriceInputDialog.getSaveButton().addActionListener(this);
		this.rangePriceInputDialog.getCancelButton().addActionListener(this);
	}

	public void displayUI() {
		rangePriceInputDialog.getMinPriceField().setValue(initialMinPrice);
		rangePriceInputDialog.getMaxPriceField().setValue(initialMaxPrice);
		rangePriceInputDialog.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == rangePriceInputDialog.getSaveButton()) {
			saveButtonAction();
		} else if (event.getSource() == rangePriceInputDialog.getCancelButton()) {
			rangePriceInputDialog.setVisible(false);
		}
	}

	private void saveButtonAction() {
		// Obtain min and max price.
		int minPrice = rangePriceInputDialog.getMinPriceField().getValue();
		int maxPrice = rangePriceInputDialog.getMaxPriceField().getValue();

		if (initialMinPrice != minPrice || initialMaxPrice != maxPrice) {
			rangePriceInputPanel.setText(String.format("%d$ - %d$", minPrice, maxPrice));
			rangePriceInputDialog.setVisible(false);
			filterBarController.filterAction();
		}
	}

}
