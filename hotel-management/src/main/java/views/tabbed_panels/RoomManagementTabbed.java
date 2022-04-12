package views.tabbed_panels;

import views.panels.RentalInvoiceListPanel;
import views.panels.RentalReceiptListPanel;
import views.panels.RoomListPanel;

import javax.swing.*;

public class RoomManagementTabbed extends JTabbedPane {

	// Constants
	public static final String ROOM_LIST_PANEL = "Rooms";
	public static final String RENTAL_INVOICE_LIST_PANEL = "Rental Invoices";
	public static final String RENTAL_RECEIPT_LIST_PANEL = "Rental Receipts";
	public static final int ROOM_LIST_INDEX = 0;
	public static final int RENTAL_INVOICE_LIST_INDEX = 1;
	public static final int RENTAL_RECEIPT_LIST_INDEX = 2;

	// Components
	final private RoomListPanel roomListPanel;
	final private RentalInvoiceListPanel rentalInvoiceListPanel;
	final private RentalReceiptListPanel rentalReceiptListPanel;

	public RoomManagementTabbed() {
		super();

		// Room list panel
		roomListPanel = new RoomListPanel();
		addTab(ROOM_LIST_PANEL, roomListPanel);

		// Rental invoice panel
		rentalInvoiceListPanel = new RentalInvoiceListPanel();
		addTab(RENTAL_INVOICE_LIST_PANEL, rentalInvoiceListPanel);

		// Rental receipt panel
		rentalReceiptListPanel = new RentalReceiptListPanel();
		addTab(RENTAL_INVOICE_LIST_PANEL, rentalReceiptListPanel);
	}


}
