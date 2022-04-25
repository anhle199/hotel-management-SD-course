package models;

import utils.UtilFunctions;

public class Product {

	private int id;
	private String name;
	private int price;
	private int stock;
	private String note;
	private int productType;

	public Product(int id, String name, int price, int stock, String note, int productType) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.stock = stock;
		this.note = note;
		this.productType = productType;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getPrice() {
		return price;
	}

	public int getStock() {
		return stock;
	}

	public String getNote() {
		return note;
	}

	public int getProductType() {
		return productType;
	}

	public boolean equals(Product another) {
		return id == another.id
				&& name.equals(another.name)
				&& price == another.price
				&& stock == another.stock
				&& note.equals(another.note)
				&& productType == another.productType;
	}

	public enum ProductTypeEnum {
		FOOD, DRINK, SOUVENIR;

		public static ProductTypeEnum valueOfIgnoreCase(String name) {
			if (name.equalsIgnoreCase(DRINK.name()))
				return DRINK;
			if (name.equalsIgnoreCase(SOUVENIR.name()))
				return SOUVENIR;

			return FOOD;
		}

		public static ProductTypeEnum valueOf(int ordinalValue) {
			if (ordinalValue == DRINK.ordinal())
				return DRINK;
			if (ordinalValue == SOUVENIR.ordinal())
				return SOUVENIR;

			return FOOD;
		}

		public static String[] allCases() {
			return new String[]{
					"All",
					UtilFunctions.capitalizeFirstLetterInString(FOOD.name()),
					UtilFunctions.capitalizeFirstLetterInString(DRINK.name()),
					UtilFunctions.capitalizeFirstLetterInString(SOUVENIR.name()),
			};
		}
	}

}
