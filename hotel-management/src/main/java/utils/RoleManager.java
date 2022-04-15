package utils;

public class RoleManager {

	private Constants.Role role;

	private RoleManager() {
		this.role = Constants.Role.NONE;
	}

	private static class BillPughSingleton {
		private static final RoleManager INSTANCE = new RoleManager();
	}

	public static RoleManager getInstance() {
		return BillPughSingleton.INSTANCE;
	}

	public boolean isManager() {
		return role == Constants.Role.MANAGER;
	}

	public boolean isEmployee() {
		return role == Constants.Role.EMPLOYEE;
	}

	public void setRole(Constants.Role role) {
		this.role = role;
	}

}
