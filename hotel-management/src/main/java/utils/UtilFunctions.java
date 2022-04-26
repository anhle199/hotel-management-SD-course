package utils;

import org.apache.commons.codec.digest.DigestUtils;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class UtilFunctions {

	private UtilFunctions() {}

	public static int sum(int[] a) {
		int result = 0;
		for (int item : a)
			result += item;

		return result;
	}

	public static void configureDialogTextFieldOnMainThread(JTextComponent textComponent) {
		SwingUtilities.invokeLater(() -> {
			textComponent.setBorder(BorderFactory.createLineBorder(Constants.Colors.LIGHT_GRAY, 1));
			textComponent.setFont(Constants.Fonts.BODY);
			textComponent.setForeground(Constants.Colors.BLACK);
			textComponent.setBackground(Constants.Colors.WHITE);
		});
	}

	public static void configureBorderedButtonOnMainThread(JButton button) {
		SwingUtilities.invokeLater(() -> {
			button.setFocusPainted(false);
			button.setRolloverEnabled(false);
			button.setBorder(BorderFactory.createLineBorder(Constants.Colors.SECONDARY, 1));
			button.setFont(Constants.Fonts.BODY);
			button.setForeground(Constants.Colors.SECONDARY);
			button.setBackground(Constants.Colors.WHITE);
		});
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

	public static void addHoverEffectsForBorderedButton(JButton button) {
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				button.setBorderPainted(false);
				button.setBackground(Constants.Colors.SECONDARY);
				button.setForeground(Constants.Colors.WHITE);
				button.setFont(Constants.Fonts.HEADLINE);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				button.setBorderPainted(true);
				button.setBackground(Constants.Colors.WHITE);
				button.setForeground(Constants.Colors.SECONDARY);
				button.setFont(Constants.Fonts.BODY);
			}
		});
	}

	public static void quitApp(JFrame mainFrame) {
		mainFrame.dispatchEvent(new WindowEvent(mainFrame, WindowEvent.WINDOW_CLOSING));
	}

	public static void showErrorMessage(Component component, String title, String message) {
		JOptionPane.showMessageDialog(component, message, title, JOptionPane.ERROR_MESSAGE);
	}

	public static void showWarningMessage(Component component, String title, String message) {
		JOptionPane.showMessageDialog(component, message, title, JOptionPane.WARNING_MESSAGE);
	}

	public static void showInfoMessage(Component component, String title, String message) {
		JOptionPane.showMessageDialog(component, message, title, JOptionPane.INFORMATION_MESSAGE);
	}

	public static int showConfirmDialog(Component component, String title, String message) {
		return JOptionPane.showConfirmDialog(component, message, title, JOptionPane.YES_NO_OPTION);
	}

	public static String hashPassword(String password) {
		return DigestUtils.sha256Hex(password);
	}

	public static String capitalizeFirstLetterInString(String str) {
		return Character.toUpperCase(str.charAt(0)) + str.substring(1).toLowerCase();
	}

	public static String removeRedundantWhiteSpace(String str) {
		return str.trim().replaceAll("\\s{2,}", " ");
	}

	public static String formatTimestamp(String pattern, Timestamp timestamp) {
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		return formatter.format(timestamp);
	}

	public static Timestamp getTimestamp(int year, int month, int day) {
		LocalDateTime localDateTime = LocalDateTime.now();

		return Timestamp.valueOf(
				String.format(
						"%d-%d-%d %d:%d:%d",
						year, month, day,
						localDateTime.getHour(), localDateTime.getMinute(), localDateTime.getSecond()
				)
		);
	}

	public static Timestamp getTimestamp(LocalDateTime localDateTime) {
		return getTimestamp(localDateTime, 0);
	}

	public static Timestamp getTimestamp(LocalDateTime localDateTime, int addedHour) {
		localDateTime = localDateTime.plusHours(addedHour);

		return Timestamp.valueOf(
				String.format(
						"%d-%d-%d %d:%d:%d",
						localDateTime.getYear(), localDateTime.getMonthValue(), localDateTime.getDayOfMonth(),
						localDateTime.getHour(), localDateTime.getMinute(), localDateTime.getSecond()
				)
		);
	}

	public static Timestamp getStartTimeOf(int year, int month, int day) {
		return Timestamp.valueOf(String.format("%d-%d-%d 00:00:00", year, month, day));
	}

	public static Timestamp getStartTimeOf(LocalDate localDate) {
		return getStartTimeOf(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth());
	}

}
