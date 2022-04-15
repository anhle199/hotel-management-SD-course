package utils;

import javax.swing.*;

public class UtilFunctions {
	private UtilFunctions() {}

	public static int sum(int[] a) {
		int result = 0;
		for (int item : a)
			result += item;

		return result;
	}

	public static void configureTopBarButtonOnMainThread(JButton button) {
		SwingUtilities.invokeLater(() -> {
			button.setBorderPainted(false);
			button.setFocusPainted(false);
			button.setRolloverEnabled(false);
			button.setFont(Constants.Fonts.HEADLINE);
			button.setForeground(Constants.Colors.WHITE);
			button.setBackground(Constants.Colors.SECONDARY);
		});
	}

	public static void configureSidebarButtonOnMainThread(JButton button, boolean activated) {
		SwingUtilities.invokeLater(() -> {
			button.setFont(Constants.Fonts.HEADLINE);
			button.setHorizontalAlignment(SwingConstants.LEFT);

			if (activated) {
				button.setBackground(Constants.Colors.SECONDARY);
				button.setForeground(Constants.Colors.WHITE);
			} else {
				button.setBackground(Constants.Colors.TERTIARY_90);
				button.setForeground(Constants.Colors.LIGHT_GRAY);
			}
		});
	}

	public static void switchSidebarButtonActiveStateOnMainThread(JButton button, boolean activated) {
		SwingUtilities.invokeLater(() -> {
			if (activated) {
				button.setBackground(Constants.Colors.SECONDARY);
				button.setForeground(Constants.Colors.WHITE);
			} else {
				button.setBackground(Constants.Colors.TERTIARY_90);
				button.setForeground(Constants.Colors.LIGHT_GRAY);
			}
		});
	}

}
