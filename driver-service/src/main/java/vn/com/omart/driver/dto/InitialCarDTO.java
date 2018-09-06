package vn.com.omart.driver.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;

import vn.com.omart.driver.entity.CarType;
import vn.com.omart.driver.jsonview.InitialView;

public class InitialCarDTO {
	
	@JsonView(InitialView.OveralView.class)
	private String[] carDurations;

	@JsonView(InitialView.OveralView.class)
	private List<CarTypeDTO> carTypes;
	
	private String carTypeSelectedStr ="";
	
	private String provinceDistrictSelectedStr ="";

	public String[] getCarDurations() {
		return carDurations;
	}

	public void setCarDurations(String[] carDurations) {
		this.carDurations = carDurations;
	}

	public List<CarTypeDTO> getCarTypes() {
		return carTypes;
	}

	public void setCarTypes(List<CarTypeDTO> carTypes) {
		this.carTypes = carTypes;
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
