package vn.com.omart.backend.application.response;

import lombok.Data;
import vn.com.omart.backend.domain.model.District;

@Data
public class DistrictDTO {

    private Long id;
    private String name;
    private Long provinceId;

    public DistrictDTO() {
    }

    public DistrictDTO(Long id) {
    	this.setId(id);
    }

    public static DistrictDTO from (District model){

        DistrictDTO dto = new DistrictDTO();

		if (model != null) {
			dto.id = model.id();
			dto.name = model.name();
		}

        return dto;

    }

}
