package models;

public class ServiceInvoice {

	private int id;
	private String serviceName;
	private int numberOfCustomers;
	private int totalPrice;
	private int timeUsed;
	private String note;
	private int roomId;
	private String roomName;
	private int serviceId;

	public ServiceInvoice(
			int id,
			String serviceName,
			int numberOfCustomers,
			int totalPrice,
			int timeUsed,
			String note,
			int roomId,
			String roomName,
			int serviceId
	) {
		this.id = id;
		this.serviceName = serviceName;
		this.numberOfCustomers = numberOfCustomers;
		this.totalPrice = totalPrice;
		this.timeUsed = timeUsed;
		this.note = note;
		this.roomId = roomId;
		this.roomName = roomName;
		this.serviceId = serviceId;
	}

	public int getId() {
		return id;
	}

	public String getServiceName() {
		return serviceName;
	}

	public int getNumberOfCustomers() {
		return numberOfCustomers;
	}

	public int getTotalPrice() {
		return totalPrice;
	}

	public int getTimeUsed() {
		return timeUsed;
	}

	public String getNote() {
		return note;
	}

	public int getRoomId() {
		return roomId;
	}

	public String getRoomName() {
		return roomName;
	}

	public int getServiceId() {
		return serviceId;
	}

	public boolean equals(ServiceInvoice another) {
		return id == another.id
				&& serviceName.equals(another.serviceName)
				&& numberOfCustomers == another.numberOfCustomers
				&& totalPrice == another.totalPrice
				&& timeUsed == another.timeUsed
				&& note.equals(another.note)
				&& roomId == another.roomId
				&& roomName.equals(another.roomName)
				&& serviceId == another.serviceId;
	}

}
