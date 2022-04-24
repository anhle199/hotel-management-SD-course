package views.tabbed_panels;

import views.panels.rooms.RentalInvoiceListPanel;
import views.panels.rooms.RentalReceiptListPanel;
import views.panels.rooms.RoomListPanel;

import javax.swing.*;

public class RoomManagementTabbed extends JTabbedPane {

	// Constants
	public static final String ROOM_LIST_PANEL_TITLE = "Rooms";
	public static final String RENTAL_INVOICE_LIST_PANEL_TITLE = "Rental Invoices";
	public static final String RENTAL_RECEIPT_LIST_PANEL_TITLE = "Rental Receipts";
	public static final int ROOM_LIST_PANEL_INDEX = 0;
	public static final int RENTAL_INVOICE_LIST_PANEL_INDEX = 1;
	public static final int RENTAL_RECEIPT_LIST_PANEL_INDEX = 2;

	// Components
	private final RoomListPanel roomListPanel;
	private final RentalInvoiceListPanel rentalInvoiceListPanel;
	private final RentalReceiptListPanel rentalReceiptListPanel;

	public RoomManagementTabbed() {
		super();

		// Room List Panel.
		roomListPanel = new RoomListPanel();
		addTab(ROOM_LIST_PANEL_TITLE, roomListPanel);

		// Rental Invoice List Panel.
		rentalInvoiceListPanel = new RentalInvoiceListPanel();
		addTab(RENTAL_INVOICE_LIST_PANEL_TITLE, rentalInvoiceListPanel);

		// Rental Receipt List Panel.
		rentalReceiptListPanel = new RentalReceiptListPanel();
		addTab(RENTAL_RECEIPT_LIST_PANEL_TITLE, rentalReceiptListPanel);
	}

	public RoomListPanel getRoomListPanel() {
		return roomListPanel;
	}

	public RentalInvoiceListPanel getRentalInvoiceListPanel() {
		return rentalInvoiceListPanel;
	}

	public RentalReceiptListPanel getRentalReceiptListPanel() {
		return rentalReceiptListPanel;
	}

}
