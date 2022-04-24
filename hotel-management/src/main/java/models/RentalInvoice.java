package models;

import utils.UtilFunctions;

import java.sql.Timestamp;

public class RentalInvoice {

	private int id;
	private Timestamp startDate;
	private int roomId;
	private String roomName;
	private String customerName;
	private String identityNumber;
	private String address;
	private int customerType;

	public RentalInvoice(
			int id,
			Timestamp startDate,
			int roomId,
			String roomName,
			String customerName,
			String identityNumber,
			String address,
			int customerType
	) {
		this.id = id;
		this.startDate = startDate;
		this.roomId = roomId;
		this.roomName = roomName;
		this.customerName = customerName;
		this.identityNumber = identityNumber;
		this.address = address;
		this.customerType = customerType;
	}

	public int getId() {
		return id;
	}

	public Timestamp getStartDate() {
		return startDate;
	}

	public int getRoomId() {
		return roomId;
	}

	public String getRoomName() {
		return roomName;
	}

	public String getCustomerName() {
		return customerName;
	}

	public String getIdentityNumber() {
		return identityNumber;
	}

	public String getAddress() {
		return address;
	}

	public int getCustomerType() {
		return customerType;
	}

	public enum CustomerTypeEnum {
		DOMESTIC, FOREIGN;

		public static CustomerTypeEnum valueOfIgnoreCase(String name) {
			if (name.equalsIgnoreCase(FOREIGN.name()))
				return FOREIGN;

			return DOMESTIC;
		}

		public static CustomerTypeEnum valueOf(int ordinalValue) {
			if (ordinalValue == FOREIGN.ordinal())
				return FOREIGN;

			return DOMESTIC;
		}

		public static String[] allCases() {
			return new String[]{
					UtilFunctions.capitalizeFirstLetterInString(DOMESTIC.name()),
					UtilFunctions.capitalizeFirstLetterInString(FOREIGN.name()),
			};
		}
	}

}
