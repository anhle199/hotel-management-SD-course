package models;

public class RentalReceipt {

	private int id;
	private byte rentedDays;
	private int price;
	private int totalPrice;
	private String roomName;
	private int roomId;

	public RentalReceipt(
			int id,
			byte rentedDays,
			int price,
			int totalPrice,
			String roomName,
			int roomId
	) {
		this.id = id;
		this.rentedDays = rentedDays;
		this.price = price;
		this.totalPrice = totalPrice;
		this.roomName = roomName;
		this.roomId = roomId;
	}

	public int getId() {
		return id;
	}

	public byte getRentedDays() {
		return rentedDays;
	}

	public int getPrice() {
		return price;
	}

	public int getTotalPrice() {
		return totalPrice;
	}

	public String getRoomName() {
		return roomName;
	}

	public int getRoomId() {
		return roomId;
	}
}
