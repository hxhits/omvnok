package vn.com.omart.messenger.application.response;

public class CallStatusDTO {

	private String callSid;
	private String status;

	public String getCallSid() {
		return callSid;
	}

	public void setCallSid(String callSid) {
		this.callSid = callSid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
