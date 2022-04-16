package controllers;

public class ValidationHandler {

	private ValidationHandler() {}

	public static boolean validateFullName(String fullName) {
		return fullName.matches("[a-zA-ZÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỂưạảấầẩẫậắằẳẵặẹẻẽềểỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ\\s]{1,50}");
	}

	// Username:
	// - Length of 6 to 20 characters.
	// - Must contain at least one letter.
	// - Optionally contain digits.
	public static boolean validateUsername(String username) {
		return username.matches("[a-zA-Z\\d]{6,20}");
	}

	// Password:
	// - Length of 6 to 20 characters.
	// - Must contain both letters and digits.
	public static boolean validatePassword(String password) {
		boolean containEntireLettersOrDigits = password.matches("([a-zA-Z]{6,20})|(\\d{6,20})");
		boolean allCases = password.matches("[a-zA-Z\\d]{6,20}");

		return allCases && !containEntireLettersOrDigits;
	}

}
