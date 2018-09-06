package vn.com.omart.backend.application.response;

import java.util.Date;

import lombok.Data;
import vn.com.omart.backend.domain.model.UserCv;

@Data
public class UserCvDTO {

	private Long id;
	private String userId;
	private String idCardNumber;
	private Long idCardDate;
	private String idCardPlace;
	private String fullname;
	private String imageUrl;
	private Long dateOfBirth;
	private int sex;
	private String phoneNumber;
	private String email;
	private Long districtId;
	private Long provinceId;
	private Long wardId;
	private DistrictDTO district;
	private ProvinceDTO province;
	private WardDTO ward;
	private String street;
	private String address;
	private int experienceLevel;
	private int educationLevel;
	private String educationName;
	private int language;
	private String description;
	private Date createdAt;
	private Date updatedAt;
	
	public UserCvDTO() {
		super();
	}

	public static UserCvDTO toDTO(UserCv entity) {
		UserCvDTO dto = new UserCvDTO();
		dto.setId(entity.getId());
		dto.setUserId(entity.getUserId());
		dto.setIdCardNumber(entity.getIdCardNumber());
		dto.setIdCardDate(entity.getIdCardDate() != null ? entity.getIdCardDate().getTime() : 0);
		dto.setIdCardPlace(entity.getIdCardPlace());
		dto.setFullname(entity.getFullname());
		dto.setImageUrl(entity.getImageUrl());
		dto.setDateOfBirth(entity.getDateOfBirth() != null ? entity.getDateOfBirth().getTime() : 0);
		dto.setSex(entity.getSex());
		dto.setPhoneNumber(entity.getPhoneNumber());
		dto.setEmail(entity.getEmail());
		
		dto.setDistrictId(entity.getDistrict() != null ? entity.getDistrict().id() : 0);
		dto.setProvinceId(entity.getProvince() != null ? entity.getProvince().id() : 0);
		dto.setWardId(entity.getWard() != null ? entity.getWard().id() : 0);

		dto.setDistrict(DistrictDTO.from(entity.getDistrict()));
		dto.setProvince(ProvinceDTO.from(entity.getProvince()));
		dto.setWard(WardDTO.from(entity.getWard()));
		
		dto.setStreet(entity.getStreet());
		dto.setAddress(entity.getAddress());
		dto.setExperienceLevel(entity.getExperienceLevel());
		dto.setEducationLevel(entity.getEducationLevel());
		dto.setEducationName(entity.getEducationName());
		dto.setLanguage(entity.getLanguage());
		dto.setDescription(entity.getDescription());
		dto.setCreatedAt(entity.getCreatedAt());
		dto.setUpdatedAt(entity.getUpdatedAt());
		return dto;
	}

	public static UserCv fromDTO(UserCvDTO entity) {
		UserCv dto = new UserCv();
		dto = fromDTO(entity, dto);
		return dto;
	}

	public static UserCv fromDTO(UserCvDTO entity, UserCv dto) {
		dto.setIdCardNumber(entity.getIdCardNumber());
		dto.setIdCardDate(new Date(entity.getIdCardDate()));
		dto.setIdCardPlace(entity.getIdCardPlace());
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
		dto.setDescription(entity.getDescription());
		return dto;
	}
}
