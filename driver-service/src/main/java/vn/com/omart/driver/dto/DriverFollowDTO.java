package vn.com.omart.driver.dto;

public class DriverFollowDTO {
	
	private Long id;
	private String carTypeSelectedStr ="";
	private String provinceDistrictSelectedStr ="";
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getCarTypeSelectedStr() {
		return carTypeSelectedStr;
	}
	public void setCarTypeSelectedStr(String carTypeSelectedStr) {
		this.carTypeSelectedStr = carTypeSelectedStr;
	}
	public String getProvinceDistrictSelectedStr() {
		return provinceDistrictSelectedStr;
	}
	public void setProvinceDistrictSelectedStr(String provinceDistrictSelectedStr) {
		this.provinceDistrictSelectedStr = provinceDistrictSelectedStr;
	}
	
}
