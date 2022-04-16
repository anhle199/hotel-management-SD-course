package views.tabbed_panels;

import views.panels.rooms.RentalInvoiceListPanel;
import views.panels.rooms.RentalReceiptListPanel;
import views.panels.rooms.RoomListPanel;

import javax.swing.*;

public class RoomManagementTabbed extends JTabbedPane {

	// Constants
	public static final String ROOM_LIST_PANEL = "Rooms";
	public static final String RENTAL_INVOICE_LIST_PANEL = "Rental Invoices";
	public static final String RENTAL_RECEIPT_LIST_PANEL = "Rental Receipts";

	// Components
	private final RoomListPanel roomListPanel;
	private final RentalInvoiceListPanel rentalInvoiceListPanel;
	private final RentalReceiptListPanel rentalReceiptListPanel;

	public RoomManagementTabbed() {
		super();

		// Room List Panel.
		roomListPanel = new RoomListPanel();
		addTab(ROOM_LIST_PANEL, roomListPanel);

		// Rental Invoice List Panel.
		rentalInvoiceListPanel = new RentalInvoiceListPanel();
		addTab(RENTAL_INVOICE_LIST_PANEL, rentalInvoiceListPanel);

		// Rental Receipt List Panel.
		rentalReceiptListPanel = new RentalReceiptListPanel();
		addTab(RENTAL_RECEIPT_LIST_PANEL, rentalReceiptListPanel);
	}

}
