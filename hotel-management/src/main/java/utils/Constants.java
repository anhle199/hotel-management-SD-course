package utils;

import java.awt.*;

public class Constants {

	public static class Fonts {
		public static final String FONT_NAME = "SF Pro Display";
		public static final Font EMPHASIZED_TITLE_2 = new Font(FONT_NAME, Font.BOLD, 17);
		public static final Font HEADLINE = new Font(FONT_NAME, Font.BOLD, 13);
		public static final Font BODY = new Font(FONT_NAME, Font.PLAIN, 13);
		public static final Font BODY_ITALIC = new Font(FONT_NAME, Font.ITALIC, 13);
	}

	public static class Colors {
		// Custom Colors.
		public static final Color PRIMARY = new Color(20, 39, 155);
		public static final Color SECONDARY = new Color(61, 86, 178);
		public static final Color TERTIARY = new Color(92, 122, 234);
		public static final Color TERTIARY_90 = new Color(103, 131, 241);
		public static final Color TERTIARY_50 = new Color(160, 171, 231);
		public static final Color TABLE_BORDER_COLOR = new Color(119, 138, 155);
		public static final Color TRANSPARENT = new Color(0, 0, 0, 0);

		// System Colors.
		public static final Color WHITE = Color.WHITE;
		public static final Color BLACK = Color.BLACK;
		public static final Color GRAY = Color.GRAY;
		public static final Color DARK_GRAY = Color.DARK_GRAY;
		public static final Color LIGHT_GRAY = Color.LIGHT_GRAY;
		public static final Color RED = Color.RED;

		public static final Color SYSTEM_PANEL_BACKGROUND = new Color(238, 238, 238);
	}

	public static class IconNames {
		public static final String ACCOUNT_BOX_BLACK = "src/main/resources/icons/ic_round_account_box_black_24dp.png";
		public static final String LOCK_BLACK = "src/main/resources/icons/ic_round_lock_black_24dp.png";
		public static final String VISIBILITY_BLACK = "src/main/resources/icons/ic_round_visibility_black_24dp.png";
		public static final String VISIBILITY_OFF_BLACK = "src/main/resources/icons/ic_round_visibility_off_black_24dp.png";

		public static final String ACCOUNT_CIRCLE_WHITE = "src/main/resources/icons/ic_round_account_circle_white_48dp.png";
		public static final String HOTEL_WHITE = "src/main/resources/icons/ic_round_hotel_white_24dp.png";
		public static final String ROOM_SERVICE_WHITE = "src/main/resources/icons/ic_round_room_service_white_24dp.png";
		public static final String SHOPPING_CART_WHITE = "src/main/resources/icons/ic_round_shopping_cart_white_24dp.png";
		public static final String BADGE_WHITE = "src/main/resources/icons/ic_round_badge_white_24dp.png";
		public static final String INSERT_CHART_WHITE = "src/main/resources/icons/ic_round_insert_chart_white_24dp.png";
		public static final String LOGOUT_WHITE = "src/main/resources/icons/ic_round_logout_white_24dp.png";

		public static final String SEARCH_BLACK = "src/main/resources/icons/ic_round_search_black_24dp.png";
		public static final String ADD_WHITE = "src/main/resources/icons/ic_round_add_white_24dp.png";
		public static final String MORE_HORIZ_WHITE = "src/main/resources/icons/ic_round_more_horiz_white_24dp.png";
		public static final String CALENDAR_MONTH_BLACK = "src/main/resources/icons/ic_round_calendar_month_black_24dp.png";
		public static final String FILTER_ALT_BLACK = "src/main/resources/icons/ic_round_filter_alt_black_24dp.png";
		public static final String FILTER_ALT_WHITE = "src/main/resources/icons/ic_round_filter_alt_white_24dp.png";

		public static final String ERROR_MESSAGE = "src/main/resources/icons/ic_error_message_64dp.png";
	}

	public static final String[] MONTHS_IN_ENGLISH = {
			"January", "February", "March", "April",
			"May", "June", "July", "August",
			"September", "October", "November", "December",
	};

	public static final int MIN_PRICE = 0;
	public static final int MAX_PRICE = 10000;

	public static final int MIN_CUSTOMERS = 1;
	public static final int MAX_CUSTOMERS = 1000;

	public static final int MIN_TIME_USED = 30;
	public static final int MAX_TIME_USED = 360;

	public static final int MIN_QUANTITY = 1;
	public static final int MAX_QUANTITY = 100;

	public static final String TIMESTAMP_WITHOUT_NANOSECOND = "yyyy-MM-dd HH:mm:ss";

}
