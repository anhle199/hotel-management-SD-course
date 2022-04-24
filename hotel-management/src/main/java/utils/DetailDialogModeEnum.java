package utils;

public enum DetailDialogModeEnum {

	VIEW_ONLY, EDITING, CREATE;

	public boolean getFieldEditable() {
		return this != VIEW_ONLY;
	}

	public String getPositiveButtonTitle() {
		if (this == VIEW_ONLY)
			return "Edit";
		else if (this == EDITING)
			return "Save";
		else if (this == CREATE)
			return "Create";
		return "";
	}

	public String getNegativeButtonTitle() {
		if (this == VIEW_ONLY)
			return "Close";
		else if (this == EDITING)
			return "Cancel";
		else if (this == CREATE)
			return "Cancel";
		return "";
	}

}
