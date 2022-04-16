package models;

public class User {

	private int id;
	private String username;
	private String password;
	private byte role;
	private String fullName;
	private byte gender;
	private short yearOfBirth;

	public User(
			int id, String username, String password, byte role,
			String fullName, byte gender, short yearOfBirth
	) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.role = role;
		this.fullName = fullName;
		this.gender = gender;
		this.yearOfBirth = yearOfBirth;
	}

	public byte getRole() {
		return role;
	}
}
