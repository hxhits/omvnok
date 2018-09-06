package vn.com.omart.backend.constants;

public class ConstantUtils {
	public static final String LIKE = "like";
	public static final String DISLIKE = "dislike";
	public static final String FAVORITE = "favorite";
	public static final String USER_ACTION = "userAction";
	public static final String LIKE_NUMBER = "likeNumber";
	public static final String DISLIKE_NUMBER = "dislikeNumber";
	public static final String COMMENT_NUMBER = "commentNumber";
	public static final String COUNT = "count";
	public static final String ALL = "all";

	/*
	 * Xpath XML Query.
	 */
	public static final String ADMINISTRATIVE_AREA_LEVEL_3 = "administrative_area_level_3"; // Commune
	public static final String ADMINISTRATIVE_AREA_LEVEL_2 = "administrative_area_level_2"; // District
	public static final String ADMINISTRATIVE_AREA_LEVEL_1 = "administrative_area_level_1"; // Province
	public static final String ROUTE = "route"; // Street name
	public static final String STREET_NUMBER = "street_number";
	public static final String SUBLOCALITY_LEVEL_1 = "sublocality_level_1";
	public static final String XML = "xml";
	public static final String JSON = "json";
	public static final String UNNAMED_ROAD = "Unnamed Road";

	/*
	 * Language.
	 */
	public static final String LANGUAGE_VI = "vi";
	public static final String LANGUAGE_EN = "en";

	/*
	 * Message
	 */
	public static final String PUSH_TITLE = "Bạn có thông báo mới";
	public static final String TIMELINE_SHARE_TITLE = "Nhật ký của ";
	public static final String RECRUIT_SHARE_TITLE = "Tuyển dụng ";
	public static final String SOUND_DEFAULT = "Default";
	public static final String[] SALARIES_V1 = { "Thoả thuận", "6-8 triệu", "8-10 triệu", "10-12 triệu", "12-14 triệu",
			"14-16 triệu", "16-18 triệu", "18-20 triệu", "20-25 triệu", "25-30 triệu", "Trên 30 triệu" };
	public static final String[] SALARIES = { "Dưới 3 triệu", "3-5 triệu", "5-10 triệu", "10-20 triệu", "20-30 triệu",
			"30-50 triệu", "50-100 triệu", "Thoả thuận" };
	public static final String[] RECRUIT_TITLES = { "Nhân viên", "Cộng tác viên", "Kỹ sư", "Chuyên viên", "Quản lý",
			"Phó phòng", "Trưởng phòng", "Phó giám đốc", "Giám đốc", "Phó tổng giám đốc", "Tổng giám đốc",
			"Giám đốc điều hành", "Khác" };
	public static final String RECRUIT_PUSH_BODY = "Tuyển: %s\nTại: %s\nLương: %s/tháng";// "Địa điểm: %s\nCần tuyển %s
																							// %s / Lương: %s";
	public static final String POI_NOTIFICATION_PUSH_BODY = "Địa điểm: %s\n%s";
	public static final String POI_NOTIFICATION_PUSH_TITLE = "%s";

	/**
	 * Recruit.
	 */
	public static final Long[] RECRUIT_POSITION_IDS = { 2L, 7L, 8L, 9L, 10L, 13L, 14L, 15L, 16L, 17L, 18L, 19L, 21L,
			22L, 23L, 25L, 26L, 27L, 29L, 30L };

	/**
	 * POI
	 */
	public static final String ACCEPT = "ACCEPT";
	public static final String REJECT = "REJECT";
	public static final String DELETE = "DELETE";
	public static final String REVERT = "REVERT";

	/**
	 * Date
	 */
	public static final String YYYYMMDD = "yyyy-MM-dd";
	public static final String YYMMDD = "yyMMdd";

	/**
	 * Timeline
	 */
	// Timeline Type
	public static final int SYS_TIMELINE = 0;
	public static final int USER_TIMELINE = 1;
	public static final int SUB_SYS_TIMELINE = 2;
	// Timeline Radius
	public static final int USER_RADIUS = 50000;
	public static final int SYS_RADIUS = 50000;
	// My Timeline Radius
	public static final int MYTL_RADIUS = 5000;

}
