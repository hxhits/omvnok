package vn.com.omart.backend.application.response;

import lombok.Data;
import vn.com.omart.backend.domain.model.AdsBanner;

@Data
public class AdsBannerDTO {

    private Long id;
    private Long categoryId;
    private Long poiId;
    private ImageDTO image;

    public static AdsBannerDTO from(AdsBanner model) {
        AdsBannerDTO dto = new AdsBannerDTO();

        dto.id = model.id();
        dto.categoryId = model.category().id();
        dto.poiId = model.poi().id();
        dto.image = new ImageDTO(model.image());

        return dto;
    }

}
