package models;

import utils.UtilFunctions;

import java.sql.Timestamp;

public class RentalInvoice {

	private int id;
	private Timestamp startDate;
	private Timestamp endDate;
	private int roomId;
	private String roomName;
	private int roomTypeId;
	private String roomTypeName;
	private int roomTypePrice;
	private String customerName;
	private String identityNumber;
	private String address;
	private int customerType;
	private byte isPaid;

	public RentalInvoice(
			int id,
			Timestamp startDate,
			Timestamp endDate,
			int roomId,
			String roomName,
			int roomTypeId,
			String roomTypeName,
			int roomTypePrice,
			String customerName,
			String identityNumber,
			String address,
			int customerType,
			byte isPaid
	) {
		this.id = id;
		this.startDate = startDate;
		this.endDate = endDate;
		this.roomId = roomId;
		this.roomName = roomName;
		this.roomTypeId = roomTypeId;
		this.roomTypeName = roomTypeName;
		this.roomTypePrice = roomTypePrice;
		this.customerName = customerName;
		this.identityNumber = identityNumber;
		this.address = address;
		this.customerType = customerType;
		this.isPaid = isPaid;
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

	public int getRoomId() {
		return roomId;
	}

	public String getRoomName() {
		return roomName;
	}

	public int getRoomTypeId() {
		return roomTypeId;
	}

	public String getRoomTypeName() {
		return roomTypeName;
	}

	public int getRoomTypePrice() {
		return roomTypePrice;
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

	public byte getIsPaid() {
		return isPaid;
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

	public enum PaymentStatusEnum {
		NOT_PAID, PAID;

		public byte byteValue() {
			return (byte) this.ordinal();
		}

		public String capitalizedName() {
			return this == NOT_PAID ? "Not paid" : "Paid";
		}

		public static PaymentStatusEnum valueOfIgnoreCase(String name) {
			if (name.equalsIgnoreCase(PAID.name()))
				return PAID;

			return NOT_PAID;
		}

		public static PaymentStatusEnum valueOf(byte byteValue) {
			if (byteValue == PAID.ordinal())
				return PAID;

			return NOT_PAID;
		}

		public static String[] allCases() {
			return new String[]{
					UtilFunctions.capitalizeFirstLetterInString(NOT_PAID.capitalizedName()),
					UtilFunctions.capitalizeFirstLetterInString(PAID.capitalizedName()),
			};
		}
	}

	public boolean equals(RentalInvoice another) {
		return id == another.id
				&& startDate.compareTo(another.startDate) == 0
				&& endDate.compareTo(another.endDate) == 0
				&& roomId == another.roomId
				&& roomName.equals(another.roomName)
				&& roomTypeId == another.roomTypeId
				&& roomTypeName.equals(another.roomTypeName)
				&& roomTypePrice == another.roomTypePrice
				&& customerName.equals(another.customerName)
				&& identityNumber.equals(another.identityNumber)
				&& address.equals(another.address)
				&& customerType == another.customerType
				&& isPaid == another.isPaid;
	}

}
