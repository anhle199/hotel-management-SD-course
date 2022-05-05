package models;

import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

public class RentalReceipt {

	private int id;
	private Timestamp startDate;
	private Timestamp endDate;
	private int price;
	private int totalPrice;
	private String roomName;
	private String roomTypeName;
	private int roomId;

	public RentalReceipt(
			int id,
			Timestamp startDate,
			Timestamp endDate,
			int price,
			int totalPrice,
			String roomName,
			String roomTypeName,
			int roomId
	) {
		this.id = id;
		this.startDate = startDate;
		this.endDate = endDate;
		this.price = price;
		this.totalPrice = totalPrice;
		this.roomName = roomName;
		this.roomTypeName = roomTypeName;
		this.roomId = roomId;
	}

	public int getId() {
		return id;
	}

	public Timestamp getStartDate() {
		return startDate;
	}

	public Timestamp getEndDate() {
		return endDate;
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

	public String getRoomTypeName() {
		return roomTypeName;
	}

	public int getRoomId() {
		return roomId;
	}

	public long calculateRentedDays() {
		long diffInTime = endDate.getTime() - startDate.getTime();
		return TimeUnit.DAYS.convert(diffInTime, TimeUnit.MILLISECONDS);
	}

}
