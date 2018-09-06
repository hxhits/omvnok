package vn.com.omart.backend.application.response;

public class DriverLocationDTO {

	private String userId;
	private Long carTypeId;
	private double latitude;
	private double longitude;
	private String name;

	public static DriverLocationDTO toDTO(Object[] objs) {
		DriverLocationDTO dto = new DriverLocationDTO();
		dto.setUserId(String.valueOf(objs[0]));
		dto.setCarTypeId(Long.valueOf(String.valueOf(objs[1])));
		dto.setLatitude(Double.valueOf(String.valueOf(objs[2])));
		dto.setLongitude(Double.valueOf(String.valueOf(objs[3])));
		dto.setName(String.valueOf(objs[4]));
		return dto;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Long getCarTypeId() {
		return carTypeId;
	}

	public void setCarTypeId(Long carTypeId) {
		this.carTypeId = carTypeId;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
