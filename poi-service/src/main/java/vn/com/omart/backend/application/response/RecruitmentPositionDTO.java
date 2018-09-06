package vn.com.omart.backend.application.response;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Data;
import vn.com.omart.backend.domain.model.RecruitmentPosition;
import vn.com.omart.backend.domain.model.RecruitmentPositionLevel;

@Data
public class RecruitmentPositionDTO {

	private Long id;
	private String name;
	private String nameEn;
	private List<RecruitmentPositionLevelDTO> PositionLevel;
	private String image;

	public RecruitmentPositionDTO(Long id) {
		super();
		this.id = id;
	}

	public RecruitmentPositionDTO() {
		super();
	}

	public static RecruitmentPositionDTO toDTO(RecruitmentPosition entity) {
		RecruitmentPositionDTO dto = new RecruitmentPositionDTO();
		dto.setId(entity.getId());
		dto.setPositionLevel(
				entity.getRecruitmentPositionLevels().stream().filter(item -> item.getIsDisabled() == false)
						.map(RecruitmentPositionLevelDTO::toDTO).collect(Collectors.toList()));
		dto.setName(entity.getName());
		dto.setNameEn(entity.getNameEn());
		dto.setImage(entity.getImage());
		return dto;
	}

	public static RecruitmentPositionDTO toDTO(RecruitmentPositionLevel entity) {
		RecruitmentPositionDTO dto = new RecruitmentPositionDTO();
		if (entity != null) {
			RecruitmentPosition recp = entity.getRecruitmentPosition();
			if (recp != null) {
				dto.setId(recp.getId());
				dto.setName(recp.getName());
				dto.setNameEn(recp.getNameEn());
			}
			dto.setPositionLevel(Arrays.asList(RecruitmentPositionLevelDTO.toDTO(entity)));
		}
		return dto;
	}

}
