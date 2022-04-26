package models;

import java.sql.Timestamp;

public class ImportInvoice {

	private int id;
	private Timestamp importedDate;
	private String note;
	private int totalPrice;

	public ImportInvoice(int id, Timestamp importedDate, String note, int totalPrice) {
		this.id = id;
		this.importedDate = importedDate;
		this.note = note;
		this.totalPrice = totalPrice;
	}

	public int getId() {
		return id;
	}

	public Timestamp getImportedDate() {
		return importedDate;
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

}
