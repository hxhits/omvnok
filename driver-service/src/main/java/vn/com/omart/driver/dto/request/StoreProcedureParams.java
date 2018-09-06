package vn.com.omart.driver.dto.request;

import java.io.Serializable;

public class StoreProcedureParams implements Serializable {

	private static final long serialVersionUID = 7549755458314206484L;

	private Double latitude;
	private Double longitude;
	private int radius = 1000000000;
	private int page = 0;
	private int size = 20;
	private int carTypeId = 0;
	
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
		this.radius = radius;
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
		this.size = size;
	}

	public int getCarTypeId() {
		return carTypeId;
	}

	public void setCarTypeId(int carTypeId) {
		this.carTypeId = carTypeId;
	}

}
