package vn.com.omart.driver.common.constant;

public class DriverType {

	public enum Device {
		android(1, "android"), ios(2, "ios"), web(3, "web"), desktop(4, "desktop");

		private int deviceId;
		private String lable;

		private Device(int deviceId, String lable) {
			this.deviceId = deviceId;
			this.lable = lable;
		}

		public int getId() {
			return deviceId;
		}

		public String getLable() {
			return lable;
		}

		public void setLable(String lable) {
			this.lable = lable;
		}

		public static boolean contains(int deviceType) {
			for (Device device : Device.values()) {
				if (device.getId() == deviceType) {
					return true;
				}
			}
			return false;
		}

		public static Device getById(int id) {
			for (Device device : Device.values()) {
				if (device.getId() == id) {
					return device;
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

	public static enum CallLogState {

		CALLING(0, "calling"), REJECTED(1, "rejected"), APPROVED(2, "approved"), APPROVED_ME(2,
				"approved for you (driver)");

		private int id;
		private String lable;

		private CallLogState(int id, String lable) {
			this.id = id;
			this.lable = lable;
		}

		public int getId() {
			return id;
		}

		public String getLable() {
			return lable;
		}

		public static CallLogState getById(int id) {
			for (CallLogState nType : CallLogState.values()) {
				if (nType.getId() == id) {
					return nType;
				}
			}
			return null;
		}
	}

	public static enum AccountType {
		GENERAL(1, "general"), OTHER(2, "other");

		private int id;
		private String lable;

		private AccountType(int id, String lable) {
			this.id = id;
			this.lable = lable;
		}

		public int getId() {
			return id;
		}

		public String getLable() {
			return lable;
		}

		public static AccountType getById(int id) {
			for (AccountType nType : AccountType.values()) {
				if (nType.getId() == id) {
					return nType;
				}
			}
			return null;
		}
	}

	public static enum LockType {
		UNLOCK(0, "unlock"), LOCK(1, "lock");

		private int id;
		private String lable;

		private LockType(int id, String lable) {
			this.id = id;
			this.lable = lable;
		}

		public int getId() {
			return id;
		}

		public String getLable() {
			return lable;
		}

		public static LockType getById(int id) {
			for (LockType nType : LockType.values()) {
				if (nType.getId() == id) {
					return nType;
				}
			}
			return null;
		}
	}

}
