package vn.com.omart.backend.application.response;

import lombok.Data;
import vn.com.omart.backend.domain.model.RecruitmentPositionLevel;

@Data
public class RecruitmentPositionLevelDTO {

	private Long id;
	private String name;
	private String nameEn;
	private String title;
	private String titleEn;

	public RecruitmentPositionLevelDTO(Long id) {
		super();
		this.id = id;
	}

	public RecruitmentPositionLevelDTO() {
		super();
	}

	public static RecruitmentPositionLevelDTO toDTO(RecruitmentPositionLevel entity) {
		RecruitmentPositionLevelDTO dto = new RecruitmentPositionLevelDTO();
		if (entity != null) {
			dto.setId(entity.getId());
			dto.setName(entity.getName());
			dto.setNameEn(entity.getNameEn());
			dto.setTitle(entity.getTitle());
			dto.setTitleEn(entity.getTitleEn());
		}
		return dto;
	}

}
