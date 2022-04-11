package views.tabbed_panels;

import views.panels.RentalInvoiceListPanel;
import views.panels.RoomListPanel;

import javax.swing.*;

public class RoomManagementTabbed extends JTabbedPane {

	// Constants
	public static final String ROOM_LIST_PANEL = "Rooms";
	public static final String RENTAL_INVOICE_LIST_PANEL = "Rental Invoices";
	public static final int ROOM_LIST_INDEX = 0;
	public static final int RENTAL_INVOICE_LIST_INDEX = 1;

	// Components
	final private RoomListPanel roomListPanel;
	final private RentalInvoiceListPanel rentalInvoiceListPanel;

	public RoomManagementTabbed() {
		super();

		// Necessaries list panel
		roomListPanel = new RoomListPanel();
		addTab(ROOM_LIST_PANEL, roomListPanel);

		// Cart panel
		rentalInvoiceListPanel = new RentalInvoiceListPanel();
		addTab(RENTAL_INVOICE_LIST_PANEL, rentalInvoiceListPanel);
	}

}
