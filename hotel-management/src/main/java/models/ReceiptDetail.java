package models;

public class ReceiptDetail {

	private int id;
	private int receiptId;
	private int quantity;
	private int productId;
	private String productName;
	private int productType;
	private int price;

	public ReceiptDetail(int id, int receiptId, int quantity, int productId, String productName, int productType, int price) {
		this.id = id;
		this.receiptId = receiptId;
		this.quantity = quantity;
		this.productId = productId;
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

	public int getQuantity() {
		return quantity;
	}

	public int getProductId() {
		return productId;
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
				&& productId == another.productId
				&& productName.equals(another.productName)
				&& productType == another.productType
				&& price == another.price;
	}

}
