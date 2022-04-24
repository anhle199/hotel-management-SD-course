package models;

import utils.UtilFunctions;

public class Room {

	private int id;
	private String name;
	private String description;
	private byte status;
	private int roomTypeId;

	public Room(int id, String name, String description, byte status, int roomTypeId) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.status = status;
		this.roomTypeId = roomTypeId;
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

	public byte getStatus() {
		return status;
	}

	public int getRoomTypeId() {
		return roomTypeId;
	}

	public enum RoomStatusEnum {
		AVAILABLE, RESERVED, RENTED;

		public static RoomStatusEnum valueOfIgnoreCase(String name) {
			if (name.equalsIgnoreCase(RESERVED.name()))
				return RESERVED;
			if (name.equalsIgnoreCase(RENTED.name()))
				return RENTED;

			return AVAILABLE;
		}

		public static RoomStatusEnum valueOf(byte value) {
			if (value == RESERVED.ordinal()) {
				return RESERVED;
			}
			if (value == RENTED.ordinal()) {
				return RENTED;
			}

			return AVAILABLE;
		}

		public byte byteValue() {
			return (byte) this.ordinal();
		}

		public static String[] allCases() {
			return new String[]{
					"All",
					UtilFunctions.capitalizeFirstLetterInString(AVAILABLE.name()),
					UtilFunctions.capitalizeFirstLetterInString(RESERVED.name()),
					UtilFunctions.capitalizeFirstLetterInString(RENTED.name()),
			};
		}
	}

	public boolean equals(Room another) {
		return id == another.id
				&& name.equals(another.name)
				&& description.equals(another.description)
				&& status == another.status
				&& roomTypeId == another.roomTypeId;
	}

}
