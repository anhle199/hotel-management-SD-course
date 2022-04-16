package utils;

public class RoleManager {

	private RoleEnum role;

	private RoleManager() {
		this.role = RoleEnum.NONE;
	}

	private static class BillPughSingleton {
		private static final RoleManager INSTANCE = new RoleManager();
	}

	public static RoleManager getInstance() {
		return BillPughSingleton.INSTANCE;
	}

	public boolean isManager() {
		return role == RoleEnum.MANAGER;
	}

	public boolean isEmployee() {
		return role == RoleEnum.EMPLOYEE;
	}

	public void setRole(RoleEnum role) {
		this.role = role;
	}

	public void setRoleInByteType(byte value) {
		setRole(RoleEnum.valueOf(value));
	}

	// NONE means that user not logged into the application.
	public enum RoleEnum {
		NONE, MANAGER, EMPLOYEE;

		public static RoleEnum valueOf(byte value) {
			if (MANAGER.ordinal() == value) {
				return MANAGER;
			} else if (EMPLOYEE.ordinal() == value) {
				return EMPLOYEE;
			}

			return NONE;
		}
	}

}
