package vn.com.omart.backend.application.response;

import java.io.Serializable;

import vn.com.omart.backend.constants.OmartType.NotificationType;

public class StoreProcedureParams implements Serializable {

	private static final long serialVersionUID = 7549755458314206484L;
	private Double latitude;
	private Double longitude;
	private int radius = 1000000000;
	private String categoryIds = "ALL";
	private String positionLevelIds = "ALL";
	private String provinceDistrictSelectedStr = "";// string is formated : "provinceId,districtId"
	private int timelineType = 1;
	private String timelineTypes = "";
	private String userId;
	private String friendStr;
	private int page = 0;
	private int size = 20;
	private NotificationType notificationType = NotificationType.ALL;

	private Long categoryId;
	private Long provinceId;
	private Long districtId;
	private Long wardId;

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		if (radius <= 0) {
			radius = 1000000000;
		}
		this.radius = radius;

	}

	public String getCategoryIds() {
		return categoryIds;
	}

	public void setCategoryIds(String categoryIds) {
		this.categoryIds = categoryIds;
	}

	public String getPositionLevelIds() {
		return positionLevelIds;
	}

	public void setPositionLevelIds(String positionLevelIds) {
		this.positionLevelIds = positionLevelIds;
	}

	public int getTimelineType() {
		return timelineType;
	}

	public void setTimelineType(int timelineType) {
		this.timelineType = timelineType;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		if (size <= 0) {
			size = 20;
		}
		this.size = size;
	}

	public String getTimelineTypes() {
		return timelineTypes;
	}

	public void setTimelineTypes(String timelineTypes) {
		this.timelineTypes = timelineTypes;
	}

	public String getProvinceDistrictSelectedStr() {
		return provinceDistrictSelectedStr;
	}

	public void setProvinceDistrictSelectedStr(String provinceDistrictSelectedStr) {
		this.provinceDistrictSelectedStr = provinceDistrictSelectedStr;
	}

	public NotificationType getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(NotificationType notificationType) {
		this.notificationType = notificationType;
	}

	public Long getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Long provinceId) {
		this.provinceId = provinceId;
	}

	public Long getDistrictId() {
		return districtId;
	}

	public void setDistrictId(Long districtId) {
		this.districtId = districtId;
	}

	public Long getWardId() {
		return wardId;
	}

	public void setWardId(Long wardId) {
		this.wardId = wardId;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getFriendStr() {
		return friendStr;
	}

	public void setFriendStr(String friendStr) {
		this.friendStr = friendStr;
	}

}
