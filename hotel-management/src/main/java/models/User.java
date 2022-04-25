package models;

import utils.UtilFunctions;

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

	public int getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public byte getRole() {
		return role;
	}

	public String getFullName() {
		return fullName;
	}

	public byte getGender() {
		return gender;
	}

	public short getYearOfBirth() {
		return yearOfBirth;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public enum GenderEnum {
		MALE, FEMALE;

		public static GenderEnum valueOfIgnoreCase(String name) {
			if (name.equalsIgnoreCase(FEMALE.name()))
				return FEMALE;

			return MALE;
		}

		public static GenderEnum valueOf(byte value) {
			if (value == FEMALE.ordinal()) {
				return FEMALE;
			}

			return MALE;
		}

		public byte byteValue() {
			return (byte) this.ordinal();
		}

		public static String[] allCases() {
			return new String[]{
					UtilFunctions.capitalizeFirstLetterInString(MALE.name()),
					UtilFunctions.capitalizeFirstLetterInString(FEMALE.name()),
			};
		}
	}

}
