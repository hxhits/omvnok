package vn.com.omart.messenger.application.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import vn.com.omart.messenger.common.constant.VideoCallStatus;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class VideoRoomDTO {
	
	private Long id;
	private String name;
	private String senderToken;
	private String recipientToken;
	private String avatar;
	private String userName;
	
	private VideoCallStatus status;
	
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getSenderToken() {
		return senderToken;
	}
	public void setSenderToken(String senderToken) {
		this.senderToken = senderToken;
	}
	public String getRecipientToken() {
		return recipientToken;
	}
	public void setRecipientToken(String recipientToken) {
		this.recipientToken = recipientToken;
	}
	public VideoCallStatus getStatus() {
		return status;
	}
	public void setStatus(VideoCallStatus status) {
		this.status = status;
	}
}

