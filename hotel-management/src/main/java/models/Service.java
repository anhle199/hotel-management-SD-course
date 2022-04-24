package models;

public class Service {

	private int id;
	private String name;
	private String description;
	private int price;
	private String note;

	public Service(int id, String name, String description, int price, String note) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.note = note;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public int getPrice() {
		return price;
	}

	public String getNote() {
		return note;
	}

	public boolean equals(Service another) {
		return id == another.id
				&& name.equals(another.name)
				&& description.equals(another.description)
				&& price == another.price
				&& note.equals(another.note);
	}

}
