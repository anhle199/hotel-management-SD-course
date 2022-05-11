package models;

public class ImportInvoiceDetail {

	private int id;
	private int importInvoiceId;
	private int quantity;
	private int productId;
	private String productName;
	private int productType;
	private int price;

	public ImportInvoiceDetail(int id, int importInvoiceId, int quantity, int productId, String productName, int productType, int price) {
		this.id = id;
		this.importInvoiceId = importInvoiceId;
		this.quantity = quantity;
		this.productId = productId;
		this.productName = productName;
		this.productType = productType;
		this.price = price;
	}

	public int getId() {
		return id;
	}

	public int getImportInvoiceId() {
		return importInvoiceId;
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

	public boolean equals(ImportInvoiceDetail another) {
		return id == another.id
				&& importInvoiceId == another.importInvoiceId
				&& quantity == another.quantity
				&& productId == another.productId
				&& productName.equals(another.productName)
				&& productType == another.productType
				&& price == another.price;
	}

}
