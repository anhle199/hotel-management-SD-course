package models;

public class ServiceInvoice {

	private int id;
	private String serviceName;
	private int numberOfCustomers;
	private int price;
	private String note;
	private int roomId;
	private String roomName;
	private int serviceId;

	public ServiceInvoice(
			int id,
			String serviceName,
			int numberOfCustomers,
			int price,
			String note,
			int roomId,
			String roomName,
			int serviceId
	) {
		this.id = id;
		this.serviceName = serviceName;
		this.numberOfCustomers = numberOfCustomers;
		this.price = price;
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

	public int getPrice() {
		return price;
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
				&& price == another.price
				&& note.equals(another.note)
				&& roomId == another.roomId
				&& roomName.equals(another.roomName)
				&& serviceId == another.serviceId;
	}

	public void copyFrom(ServiceInvoice another) {
		this.id = another.id;
		this.serviceName = another.serviceName;
		this.numberOfCustomers = another.numberOfCustomers;
		this.price = another.price;
		this.note = another.note;
		this.roomId = another.roomId;
		this.roomName = another.roomName;
		this.serviceId = another.serviceId;
	}

}
