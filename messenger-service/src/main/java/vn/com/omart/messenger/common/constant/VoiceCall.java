package vn.com.omart.messenger.common.constant;

public enum VoiceCall {

	INITIATED(1, "initiated"), RINGING(2, "ringing"), IN_PROGRESS(3, "in-progress"), COMPLETED(4,
			"completed"), NO_ANSWER(5, "no-answer"), FAILED(6, "failed");

	/*
	 * initiated,ringing,in-progress,completed no-answer failed
	 */

	private int id;
	private String lable;

	private VoiceCall(int id, String lable) {
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
		for (VoiceCall voiceCall : VoiceCall.values()) {
			if (voiceCall.getId() == id) {
				return true;
			}
		}
		return false;
	}

	public static VoiceCall getById(int id) {
		for (VoiceCall voiceCall : VoiceCall.values()) {
			if (voiceCall.getId() == id) {
				return voiceCall;
			}
		}
		return null;
	}

	public static VoiceCall getByLable(String label) {
		for (VoiceCall voiceCall : VoiceCall.values()) {
			if (voiceCall.getLable().equalsIgnoreCase(label)) {
				return voiceCall;
			}
		}
		return null;
	}
}
