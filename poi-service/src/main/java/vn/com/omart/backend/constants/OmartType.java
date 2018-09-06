package vn.com.omart.backend.constants;

public class OmartType {

	public static enum AppType {

		OMART(0, "omart"), DRIVER(1, "driver");

		private int id;
		private String lable;

		private AppType(int id, String lable) {
			this.id = id;
			this.lable = lable;
		}

		public int getId() {
			return id;
		}

		public String getLable() {
			return lable;
		}

		public static AppType getById(int id) {
			for (AppType nType : AppType.values()) {
				if (nType.getId() == id) {
					return nType;
				}
			}
			return null;
		}
	}

	public static enum NotificationType {
		SYS(0, "omart system"), POI(1, "shop owner"), RECRUIT(2, "recruit"), BOOK_CAR(3, "book car"), ALL(99, "all");

		private int id;
		private String lable;

		private NotificationType(int id, String lable) {
			this.id = id;
			this.lable = lable;
		}

		public int getId() {
			return id;
		}

		public String getLable() {
			return lable;
		}

		public static NotificationType getById(int id) {
			for (NotificationType nType : NotificationType.values()) {
				if (nType.getId() == id) {
					return nType;
				}
			}
			return null;
		}
	}

	public static enum TimelineType {
		STAFT(0, "omart staft"), USER(1, "normal user"), ADMIN(2, "administrator user");

		private int id;
		private String lable;

		private TimelineType(int id, String lable) {
			this.id = id;
			this.lable = lable;
		}

		public int getId() {
			return id;
		}

		public String getLable() {
			return lable;
		}

		public static TimelineType getById(int id) {
			for (TimelineType nType : TimelineType.values()) {
				if (nType.getId() == id) {
					return nType;
				}
			}
			return null;
		}
	}

	public static enum BookCarState {
		WAITING(0, "waiting"), BOOKED(1, "booked"), CANCELLED(2, "cancelled");

		private int id;
		private String lable;

		private BookCarState(int id, String lable) {
			this.id = id;
			this.lable = lable;
		}

		public int getId() {
			return id;
		}

		public String getLable() {
			return lable;
		}

		public static BookCarState getById(int id) {
			for (BookCarState nType : BookCarState.values()) {
				if (nType.getId() == id) {
					return nType;
				}
			}
			return null;
		}
	}

	public static enum PoiType {
		POI(0, "poi"), BUZ(1, "business");

		private int id;
		private String lable;

		private PoiType(int id, String lable) {
			this.id = id;
			this.lable = lable;
		}

		public int getId() {
			return id;
		}

		public String getLable() {
			return lable;
		}

		public static PoiType getById(int id) {
			for (PoiType nType : PoiType.values()) {
				if (nType.getId() == id) {
					return nType;
				}
			}
			return null;
		}
	}

	public static enum ApplicantAction {

		UNDEFINE(0, "undefine"), ACCEPT(1, "accept working"), REJECT(2, "reject working");

		private int id;
		private String lable;

		private ApplicantAction(int id, String lable) {
			this.id = id;
			this.lable = lable;
		}

		public int getId() {
			return id;
		}

		public String getLable() {
			return lable;
		}

		public static ApplicantAction getById(int id) {
			for (ApplicantAction nType : ApplicantAction.values()) {
				if (nType.getId() == id) {
					return nType;
				}
			}
			return null;
		}
	}

	public static enum RecruitmentApplyStatus {

		NEW_APPLY(0, "new apply"), INVITE(1, "invite"), ACCEPT(2, "accept"), REJECT(3, "reject"), FAIL(4,
				"fail"), PASS(5, "pass"), ACCEPT_ONBOARD(6, "Accept Onboard"), REJECT_ONBOARD(7, "Reject Onboard");

		private int id;
		private String lable;

		private RecruitmentApplyStatus(int id, String lable) {
			this.id = id;
			this.lable = lable;
		}

		public int getId() {
			return id;
		}

		public String getLable() {
			return lable;
		}

		public static RecruitmentApplyStatus getById(int id) {
			for (RecruitmentApplyStatus nType : RecruitmentApplyStatus.values()) {
				if (nType.getId() == id) {
					return nType;
				}
			}
			return null;
		}
	}

	public static enum InterviewResult {

		NO_RESULT(0, "not yet have result"), PASS(1, "pass"), FAIL(2, "fail");

		private int id;
		private String lable;

		private InterviewResult(int id, String lable) {
			this.id = id;
			this.lable = lable;
		}

		public int getId() {
			return id;
		}

		public String getLable() {
			return lable;
		}

		public static InterviewResult getById(int id) {
			for (InterviewResult nType : InterviewResult.values()) {
				if (nType.getId() == id) {
					return nType;
				}
			}
			return null;
		}
	}

	public static enum UserCVNotificatinType {

		INVITE_INTERVIEW(0, "invite interview"), INVITE_ONBOARD(1, "invite working"), INTERVIEW_FAIL(2,
				"interview fail"), ONBOARD_STATUS(3, "onboard status");

		private int id;
		private String lable;

		private UserCVNotificatinType(int id, String lable) {
			this.id = id;
			this.lable = lable;
		}

		public int getId() {
			return id;
		}

		public String getLable() {
			return lable;
		}

		public static UserCVNotificatinType getById(int id) {
			for (UserCVNotificatinType nType : UserCVNotificatinType.values()) {
				if (nType.getId() == id) {
					return nType;
				}
			}
			return null;
		}
	}

	public static enum OrderSellerState {

		WAITING(0, "waiting", "Đang chờ"), INVALIDED(1, "invalided", "Không hợp lệ"), CANCELLED(2, "cancelled",
				"đã hủy"), PROCESSING(3, "processing", "Đang được xử lý"), DELIVERY(4, "delivery",
						"Đang giao hàng"), COMPLETED(5, "completed", "Đã hoàn thành");

		private int id;
		private String label;
		private String labelVI;

		private OrderSellerState(int id, String label, String labelVI) {
			this.id = id;
			this.label = label;
			this.labelVI = labelVI;
		}

		public int getId() {
			return id;
		}

		public String getLabel() {
			return label;
		}

		public String getLabelVI() {
			return labelVI;
		}

		public static OrderSellerState getById(int id) {
			for (OrderSellerState nType : OrderSellerState.values()) {
				if (nType.getId() == id) {
					return nType;
				}
			}
			return null;
		}
	}

	public static enum OrderBuyerState {

		ACCEPT(0, "accept"), CANCEL(1, "cancel");

		private int id;
		private String lable;

		private OrderBuyerState(int id, String lable) {
			this.id = id;
			this.lable = lable;
		}

		public int getId() {
			return id;
		}

		public String getLable() {
			return lable;
		}

		public static OrderBuyerState getById(int id) {
			for (OrderBuyerState nType : OrderBuyerState.values()) {
				if (nType.getId() == id) {
					return nType;
				}
			}
			return null;
		}
	}

	public static enum AppFeature {

		DRIVER(1, "driver"), RECRUIT(2, "recruit"), SHOP(3, "shop");

		private int id;
		private String lable;

		private AppFeature(int id, String lable) {
			this.id = id;
			this.lable = lable;
		}

		public int getId() {
			return id;
		}

		public String getLable() {
			return lable;
		}

		public static AppFeature getById(int id) {
			for (AppFeature nType : AppFeature.values()) {
				if (nType.getId() == id) {
					return nType;
				}
			}
			return null;
		}
	}

	public static enum PoiFeature {
		ENABLE_SALE, DISABLE_SALE;
	}
	
	public static enum Commons {
		POST, UNPOST, NONE, WAIT, FRIEND;
	}

	public static enum UserFriendRequestState {
		WAIT(0, "wait"), ACCEPT(1, "accept"), REJECT(2, "reject"),UNFRIEND(3, "unfriend");

		private int id;
		private String lable;

		private UserFriendRequestState(int id, String lable) {
			this.id = id;
			this.lable = lable;
		}

		public int getId() {
			return id;
		}

		public String getLable() {
			return lable;
		}

		public static UserFriendRequestState getById(int id) {
			for (UserFriendRequestState nType : UserFriendRequestState.values()) {
				if (nType.getId() == id) {
					return nType;
				}
			}
			return null;
		}
	}
	
	
	public static enum TimeLineReportType {
		ABUSE(0, "abuse"), HIDE(1, "hide") ;

		private int id;
		private String lable;

		private TimeLineReportType(int id, String lable) {
			this.id = id;
			this.lable = lable;
		}

		public int getId() {
			return id;
		}

		public String getLable() {
			return lable;
		}

		public static TimeLineReportType getById(int id) {
			for (TimeLineReportType nType : TimeLineReportType.values()) {
				if (nType.getId() == id) {
					return nType;
				}
			}
			return null;
		}
	}
}
