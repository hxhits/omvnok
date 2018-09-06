package vn.com.omart.backend.application.response;

import lombok.Data;
import vn.com.omart.backend.domain.model.Province;

@Data
public class ProvinceDTO {

	private Long id;
	private String name;

	public ProvinceDTO() {
	}

	public ProvinceDTO(Long id) {
		this.setId(id);
	}

	public static ProvinceDTO from(Province model) {

		ProvinceDTO dto = new ProvinceDTO();

		if (model != null) {
			dto.id = model.id();
			dto.name = model.name();
		}

		return dto;

	}
}
