package utils;

import java.awt.*;

public class Constants {

	public static class Fonts {
		private static final String FONT_NAME = "SF Pro Display";
		public static final Font EMPHASIZED_TITLE_2 = new Font(FONT_NAME, Font.BOLD, 17);
		public static final Font BODY = new Font(FONT_NAME, Font.PLAIN, 13);
	}

	public static class Colors {
		public static final Color PRIMARY = new Color(20, 39, 155);
		public static final Color TERTIARY_50 = new Color(92, 122, 234, 50);
		public static final Color WHITE = Color.WHITE;
		public static final Color BLACK = Color.BLACK;
		public static final Color TRANSPARENT = new Color(0, 0, 0, 0);
		public static final Color DARK_GRAY = new Color(169, 169, 169);
	}

	public static class IconNames {
		public static final String ACCOUNT_BOX = "src/main/resources/icons/ic_round_account_box_black_24dp.png";
		public static final String LOCK = "src/main/resources/icons/ic_round_lock_black_24dp.png";
		public static final String VISIBILITY = "src/main/resources/icons/ic_round_visibility_black_24dp.png";
		public static final String VISIBILITY_OFF = "src/main/resources/icons/ic_round_visibility_off_black_24dp.png";
	}

}
