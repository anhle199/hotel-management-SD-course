package utils;

import java.awt.*;

public class Constants {

	public static class Fonts {
		public static final String FONT_NAME = "SF Pro Display";
		public static final Font EMPHASIZED_TITLE_2 = new Font(FONT_NAME, Font.BOLD, 17);
		public static final Font HEADLINE = new Font(FONT_NAME, Font.BOLD, 13);
		public static final Font BODY = new Font(FONT_NAME, Font.PLAIN, 13);
	}

	public static class Colors {
		public static final Color PRIMARY = new Color(20, 39, 155);
		public static final Color SECONDARY = new Color(61, 86, 178);
		public static final Color TERTIARY = new Color(92, 122, 234);
		public static final Color TERTIARY_90 = new Color(92, 122, 234, (int) Math.round(255.0 * 0.9));
		public static final Color TERTIARY_50 = new Color(92, 122, 234, (int) Math.round(255.0 * 0.5));
		public static final Color WHITE = Color.WHITE;
		public static final Color BLACK = Color.BLACK;
		public static final Color TRANSPARENT = new Color(0, 0, 0, 0);
		public static final Color DARK_GRAY = new Color(169, 169, 169);
		public static final Color LIGHT_GRAY = new Color(211, 211, 211);
		public static final Color RED = Color.RED;
	}

	public static class IconNames {
		public static final String ACCOUNT_BOX = "src/main/resources/icons/ic_round_account_box_black_24dp.png";
		public static final String LOCK = "src/main/resources/icons/ic_round_lock_black_24dp.png";
		public static final String VISIBILITY = "src/main/resources/icons/ic_round_visibility_black_24dp.png";
		public static final String VISIBILITY_OFF = "src/main/resources/icons/ic_round_visibility_off_black_24dp.png";

		public static final String HOTEL = "src/main/resources/icons/ic_round_hotel_white_24dp.png";
		public static final String ROOM_SERVICE = "src/main/resources/icons/ic_round_room_service_white_24dp.png";
		public static final String SHOPPING_CART = "src/main/resources/icons/ic_round_shopping_cart_white_24dp.png";
		public static final String HOME_REPAIR_SERVICE = "src/main/resources/icons/ic_round_home_repair_service_white_24dp.png";
		public static final String BADGE = "src/main/resources/icons/ic_round_badge_white_24dp.png";
		public static final String INSERT_CHART = "src/main/resources/icons/ic_round_insert_chart_white_24dp.png";
		public static final String LOGOUT = "src/main/resources/icons/ic_round_logout_white_24dp.png";

		public static final String SEARCH = "src/main/resources/icons/ic_round_search_black_24dp.png";
		public static final String ADD = "src/main/resources/icons/ic_round_add_white_24dp.png";
		public static final String MORE_HORIZ = "src/main/resources/icons/ic_round_more_horiz_white_24dp.png";
		public static final String CALENDAR_MONTH = "src/main/resources/icons/ic_round_calendar_month_black_24dp.png";
		public static final String FILTER_ALT_BLACK = "src/main/resources/icons/ic_round_filter_alt_black_24dp.png";
		public static final String FILTER_ALT_WHITE = "src/main/resources/icons/ic_round_filter_alt_white_24dp.png";
	}

	public static final String APP_ICON_ROUNDED_BORDER = "src/main/resources/icons/app_icon_rounded_border.png";

	public static enum Role { MANAGER, EMPLOYEE }

}
