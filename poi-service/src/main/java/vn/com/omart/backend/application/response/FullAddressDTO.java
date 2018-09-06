package vn.com.omart.backend.application.response;

public class FullAddressDTO {
	private ProvinceDTO province;
	private DistrictDTO district;
	private WardDTO ward;
	private String street;
	private String fullAddress;

	public FullAddressDTO() {
	}

	public FullAddressDTO(ProvinceDTO province, DistrictDTO district, WardDTO ward, String street) {
		super();
		this.province = province;
		this.district = district;
		this.ward = ward;
		this.street = street;
	}

	public ProvinceDTO getProvince() {
		return province;
	}

	public void setProvince(ProvinceDTO province) {
		this.province = province;
	}

	public DistrictDTO getDistrict() {
		return district;
	}

	public void setDistrict(DistrictDTO district) {
		this.district = district;
	}

	public WardDTO getWard() {
		return ward;
	}

	public void setWard(WardDTO ward) {
		this.ward = ward;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getFullAddress() {
		return fullAddress;
	}

	public void setFullAddress(String fullAddress) {
		this.fullAddress = fullAddress;
	}

}
