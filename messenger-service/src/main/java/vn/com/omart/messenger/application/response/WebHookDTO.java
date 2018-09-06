package vn.com.omart.messenger.application.response;

public class WebHookDTO {
	private String eventType;
	private String channelSid;
	private String body;
	private String attribute;
	private String from;
	private int messageIndex;

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getChannelSid() {
		return channelSid;
	}

	public void setChannelSid(String channelSid) {
		this.channelSid = channelSid;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public int getMessageIndex() {
		return messageIndex;
	}

	public void setMessageIndex(int messageIndex) {
		this.messageIndex = messageIndex;
	}

}
