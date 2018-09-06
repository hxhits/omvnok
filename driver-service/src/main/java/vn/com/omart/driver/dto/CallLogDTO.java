package vn.com.omart.driver.dto;

import vn.com.omart.driver.common.constant.DriverType;

public class CallLogDTO {
	private DriverInfoDTO profile;
	private double latitude;
	private double longitude;
	// id
	private String driverId;
	private Long bookcarId;
	// end
	private DriverType.BookCarState bookCarState;
	private DriverType.CallLogState state;
	
	public DriverInfoDTO getProfile() {
		return profile;
	}

	public void setProfile(DriverInfoDTO profile) {
		this.profile = profile;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getDriverId() {
		return driverId;
	}

	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}

	public Long getBookcarId() {
		return bookcarId;
	}

	public void setBookcarId(Long bookcarId) {
		this.bookcarId = bookcarId;
	}

	public DriverType.CallLogState getState() {
		return state;
	}

	public void setState(DriverType.CallLogState state) {
		this.state = state;
	}

	public DriverType.BookCarState getBookCarState() {
		return bookCarState;
	}

	public void setBookCarState(DriverType.BookCarState bookCarState) {
		this.bookCarState = bookCarState;
	}

}
