package utils;

public class UtilFunctions {
	private UtilFunctions() {}

	public static int sum(int[] a) {
		int result = 0;
		for (int item : a)
			result += item;

		return result;
	}

}
