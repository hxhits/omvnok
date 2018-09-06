package vn.com.omart.driver.dto;

import java.util.Date;

import vn.com.omart.driver.common.util.DateUtils;
import vn.com.omart.driver.entity.DriverLocation;

public class DriverLocationDTO {
	private String userId;
	private double latitude;
	private double longitude;
	private String location;
	private Date updatedAt;
	private Long carTypeId;
	private String name;

	public static DriverLocation toEntity(DriverLocationDTO dto) {
		DriverLocation entity = new DriverLocation();
		entity.setUserId(dto.getUserId());
		String[] locations = dto.getLocation().split(",");
		entity.setLatitude(Double.parseDouble(locations[0]));
		entity.setLongitude(Double.parseDouble(locations[1]));
		entity.setUpdatedAt(DateUtils.getCurrentDate());
		entity.setName(dto.getName());
		return entity;
	}
	
	public static DriverLocationDTO toDTO(Object[] objs) {
		DriverLocationDTO dto = new DriverLocationDTO();
		dto.setUserId(String.valueOf(objs[0]));
		dto.setCarTypeId(Long.valueOf(String.valueOf(objs[1])));
		dto.setLatitude(Double.valueOf(String.valueOf(objs[2])));
		dto.setLongitude(Double.valueOf(String.valueOf(objs[3])));
		if(objs[4]!=null) {
			dto.setName(String.valueOf(objs[4]));
		}
		return dto;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Long getCarTypeId() {
		return carTypeId;
	}

	public void setCarTypeId(Long carTypeId) {
		this.carTypeId = carTypeId;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
