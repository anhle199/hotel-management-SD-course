package models;

import java.sql.Timestamp;

public class Receipt {

	private int id;
	private String customerName;
	private Timestamp purchasedDate;
	private String note;
	private int totalPrice;

	public Receipt(int id, String customerName, Timestamp purchasedDate, String note, int totalPrice) {
		this.id = id;
		this.customerName = customerName;
		this.purchasedDate = purchasedDate;
		this.note = note;
		this.totalPrice = totalPrice;
	}

	public int getId() {
		return id;
	}

	public String getCustomerName() {
		return customerName;
	}

	public Timestamp getPurchasedDate() {
		return purchasedDate;
	}

	public String getNote() {
		return note;
	}

	public int getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}

	public boolean equals(Receipt another) {
		return id == another.id
				&& customerName.equals(another.customerName)
				&& purchasedDate.compareTo(another.purchasedDate) == 0
				&& note.equals(another.note)
				&& totalPrice == another.totalPrice;
	}

}
