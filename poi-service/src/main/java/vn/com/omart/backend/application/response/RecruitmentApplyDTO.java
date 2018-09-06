package vn.com.omart.backend.application.response;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Data;
import vn.com.omart.backend.domain.model.PointOfInterest;
import vn.com.omart.backend.domain.model.RecruitmentApply;

@Data
public class RecruitmentApplyDTO {

	private Long id;
	private String userId;
	private Long recruitmentId;
	private Long recruitmentPositionLevelId;
	private RecruitmentPositionDTO recruitmentPosition;
	private String fullname;
	private String imageUrl;
	private Long dateOfBirth;
	private int sex;
	private String phoneNumber;
	private String email;
	private Long district;
	private Long province;
	private Long ward;
	private String street;
	private String address;
	private int experienceLevel;
	private int educationLevel;
	private String educationName;
	private int language;
	private String salaryExpected;
	private String description;
	private Date createdAt;
	private Date updatedAt;
	private int status;
	private PointOfInterestDTO poi;
	private Long userCVId;
	private boolean overrideUserCV;
	private RecruitmentDTO recruitment;

	public RecruitmentApplyDTO() {
		super();
	}

	public static RecruitmentApplyDTO toDTO(RecruitmentApply entity) {
		RecruitmentApplyDTO dto = new RecruitmentApplyDTO();
		dto.setId(entity.getId());
		dto.setUserId(entity.getUserId());
		dto.setRecruitmentId(entity.getRecruitment().getId());
		dto.setRecruitmentPosition(RecruitmentPositionDTO.toDTO(entity.getRecruitment().getRecruitmentPositionLevel()));
		dto.setFullname(entity.getFullname());
		dto.setImageUrl(entity.getImageUrl());
		dto.setDateOfBirth(entity.getDateOfBirth().getTime());
		dto.setSex(entity.getSex());
		dto.setPhoneNumber(entity.getPhoneNumber());
		dto.setEmail(entity.getEmail());
		dto.setDistrict(entity.getDistrict().id());
		dto.setProvince(entity.getProvince().id());
		dto.setWard(entity.getWard().id());
		dto.setStreet(entity.getStreet());
		dto.setAddress(entity.getAddress());
		dto.setExperienceLevel(entity.getExperienceLevel());
		dto.setEducationLevel(entity.getEducationLevel());
		dto.setEducationName(entity.getEducationName());
		dto.setLanguage(entity.getLanguage());
		dto.setSalaryExpected(entity.getSalaryExpected());
		dto.setDescription(entity.getDescription());
		dto.setCreatedAt(entity.getCreatedAt());
		dto.setUpdatedAt(entity.getUpdatedAt());
		dto.setStatus(entity.getStatus());

		if (entity.getRecruitment() != null) {
			PointOfInterest poiEntity = entity.getRecruitment().getPoi();
			if (poiEntity != null) {
				PointOfInterestDTO poiDTO = PointOfInterestDTO.toBasicDTO(poiEntity);
				dto.setPoi(poiDTO);
			}
			RecruitmentDTO recruitmentDTO = new RecruitmentDTO();
			recruitmentDTO.setQuantity(entity.getRecruitment().getQuantity());
			recruitmentDTO.setExpiredAt(entity.getRecruitment().getExpiredAt().getTime());
			dto.setRecruitment(recruitmentDTO);
		}

		return dto;
	}

	public static RecruitmentApply fromDTO(RecruitmentApplyDTO entity) {
		RecruitmentApply dto = new RecruitmentApply();
		dto.setId(entity.getId());
		dto.setFullname(entity.getFullname());
		dto.setImageUrl(entity.getImageUrl());
		dto.setDateOfBirth(new Date(entity.getDateOfBirth()));
		dto.setSex(entity.getSex());
		dto.setPhoneNumber(entity.getPhoneNumber());
		dto.setEmail(entity.getEmail());
		dto.setStreet(entity.getStreet());
		dto.setExperienceLevel(entity.getExperienceLevel());
		dto.setEducationLevel(entity.getEducationLevel());
		dto.setEducationName(entity.getEducationName());
		dto.setLanguage(entity.getLanguage());
		dto.setSalaryExpected(entity.getSalaryExpected());
		dto.setDescription(entity.getDescription());
		dto.setCreatedAt(entity.getCreatedAt());
		dto.setUpdatedAt(entity.getUpdatedAt());
		dto.setStatus(entity.getStatus());
		return dto;
	}
}
