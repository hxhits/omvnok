package vn.com.omart.backend.application.response;

import vn.com.omart.backend.domain.model.DriverInfo;

public class DriverInfoDTO {
	private String userId;
	private String fullName;
	private String avatar;
	private String phoneNumber;

	public static DriverInfoDTO toBasicDTO(DriverInfo entity) {
		DriverInfoDTO dto = new DriverInfoDTO();
		dto.setUserId(entity.getUserId());
		dto.setFullName(entity.getFullName());
		dto.setAvatar(entity.getAvatar());
		dto.setPhoneNumber(entity.getPhoneNumber());
		return dto;
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
}
