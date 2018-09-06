package vn.com.omart.backend.application.response;

import lombok.Data;
import vn.com.omart.backend.domain.model.Ward;

@Data
public class WardDTO {

	private Long id;
	private String name;
	private Long provinceId;
	private Long districtId;

	public WardDTO() {
	}

	public WardDTO(Long id) {
		this.setId(id);
	}

	public static WardDTO from(Ward model) {

		WardDTO dto = new WardDTO();

		if (model != null) {
			dto.id = model.id();
			dto.name = model.name();
		}

		return dto;

	}

}
