package vn.com.omart.backend.application.response;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import vn.com.omart.backend.domain.model.Deliverer;

public class DelivererDTO {

	private Long id;
	
	@NotNull
	private Long poiId;
	
	@NotEmpty
	private String phoneNumber;
	
	private String name;

	public static DelivererDTO toDTO(Deliverer entity) {
		DelivererDTO dto = new DelivererDTO();
		dto.setId(entity.getId());
		dto.setPhoneNumber(entity.getDriver() != null ? entity.getDriver().getPhoneNumber() : "");
		dto.setPoiId(entity.getPoi() != null ? entity.getPoi().id() : 0);
		dto.setName(entity.getDriver() != null ? entity.getDriver().getFullName() : "");
		return dto;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPoiId() {
		return poiId;
	}

	public void setPoiId(Long poiId) {
		this.poiId = poiId;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
