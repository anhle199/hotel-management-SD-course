package models;

import jdk.jfr.DataAmount;
import utils.Constants;

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

		public static RoomStatusEnum valueOf(byte value) {
			if (value == RESERVED.ordinal()) {
				return RESERVED;
			}
			if (value == RENTED.ordinal()) {
				return RENTED;
			}

			return AVAILABLE;
		}
	}

}
