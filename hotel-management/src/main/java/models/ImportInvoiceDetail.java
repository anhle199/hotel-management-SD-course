package models;

public class ImportInvoiceDetail {

	private int id;
	private int importInvoiceId;
	private byte quantity;
	private String productName;
	private int productType;
	private int price;

	public ImportInvoiceDetail(int id, int importInvoiceId, byte quantity, String productName, int productType, int price) {
		this.id = id;
		this.importInvoiceId = importInvoiceId;
		this.quantity = quantity;
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

	public boolean equals(ImportInvoiceDetail another) {
		return id == another.id
				&& importInvoiceId == another.importInvoiceId
				&& quantity == another.quantity
				&& productName.equals(another.productName)
				&& productType == another.productType
				&& price == another.price;
	}

}
