package vn.com.omart.messenger.common.constant;

public enum VideoCallStatus {

	INITIATED(0, "initiated"),REJECT(1, "reject");

	private int id;
	private String lable;

	private VideoCallStatus(int id, String lable) {
		this.id = id;
		this.lable = lable;
	}

	public int getId() {
		return id;
	}

	public String getLable() {
		return lable;
	}

	public void setLable(String lable) {
		this.lable = lable;
	}

	public static boolean contains(int id) {
		for (VideoCallStatus videoCall : VideoCallStatus.values()) {
			if (videoCall.getId() == id) {
				return true;
			}
		}
		return false;
	}

	public static VideoCallStatus getById(int id) {
		for (VideoCallStatus videoCall : VideoCallStatus.values()) {
			if (videoCall.getId() == id) {
				return videoCall;
			}
		}
		return null;
	}

	public static VideoCallStatus getByLable(String label) {
		for (VideoCallStatus videoCall : VideoCallStatus.values()) {
			if (videoCall.getLable().equalsIgnoreCase(label)) {
				return videoCall;
			}
		}
		return null;
	}
}
