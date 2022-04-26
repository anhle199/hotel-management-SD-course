package models;

public class ReceiptDetail {

	private int id;
	private int receiptId;
	private byte quantity;
	private String productName;
	private int productType;
	private int price;

	public ReceiptDetail(int id, int receiptId, byte quantity, String productName, int productType, int price) {
		this.id = id;
		this.receiptId = receiptId;
		this.quantity = quantity;
		this.productName = productName;
		this.productType = productType;
		this.price = price;
	}

	public int getId() {
		return id;
	}

	public int getReceiptId() {
		return receiptId;
	}

	public byte getQuantity() {
		return quantity;
	}

	public String getProductName() {
		return productName;
	}

	public int getProductType() {
		return productType;
	}

	public int getPrice() {
		return price;
	}

	public boolean equals(ReceiptDetail another) {
		return id == another.id
				&& receiptId == another.receiptId
				&& quantity == another.quantity
				&& productName.equals(another.productName)
				&& productType == another.productType
				&& price == another.price;
	}

}
