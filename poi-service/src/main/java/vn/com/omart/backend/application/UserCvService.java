package vn.com.omart.backend.application;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.com.omart.backend.application.response.UserCvDTO;
import vn.com.omart.backend.application.util.DateUtils;
import vn.com.omart.backend.domain.model.District;
import vn.com.omart.backend.domain.model.DistrictRepository;
import vn.com.omart.backend.domain.model.Province;
import vn.com.omart.backend.domain.model.ProvinceRepository;
import vn.com.omart.backend.domain.model.RecruitmentApply;
import vn.com.omart.backend.domain.model.UserCvRepository;
import vn.com.omart.backend.domain.model.UserCv;
import vn.com.omart.backend.domain.model.Ward;
import vn.com.omart.backend.domain.model.WardRepository;
import vn.com.omart.backend.port.adapter.userprofile.UserService;
import vn.com.omart.sharedkernel.application.model.error.NotFoundException;
import vn.com.omart.sharedkernel.application.model.error.UnauthorizedAccessException;

@Service
public class UserCvService {

	@Autowired
	private UserCvRepository userCvRepository;

	@Autowired
	private ProvinceRepository provinceRepository;
	@Autowired
	private DistrictRepository districtRepository;
	@Autowired
	private WardRepository wardRepository;

	@Autowired
	private UserService userService;

	/**
	 * Save CV.
	 * 
	 * @param dto
	 */
	@Transactional(readOnly = false)
	public void save(UserCvDTO dto, String userId) {
		if (null == userService.getOwner(userId)) {
			throw new UnauthorizedAccessException("User is Unauthorized");
		}

		// save RecruitmentApply.
		UserCv entity = null;

		if (dto.getId() != null) {
			entity = userCvRepository.findOne(dto.getId());
		}

		// New/Update
		if (entity == null) {
			entity = UserCvDTO.fromDTO(dto);
		} else {
			entity = UserCvDTO.fromDTO(dto, entity);
			entity.setUpdatedAt(new Date());
		}

		Province province = this.provinceRepository.findOne(dto.getProvinceId());
		if (null == province) {
			throw new NotFoundException("Province not exists");
		}
		District district = this.districtRepository.findByIdAndProvinceId(dto.getDistrictId(), province.id());
		if (null == district) {
			throw new NotFoundException("District not exists");
		}
		Ward ward = this.wardRepository.findByIdAndDistrictId(dto.getWardId(), district.id());
		if (null == ward) {
			throw new NotFoundException("Ward not exists");
		}

		entity.setUserId(userId);
		entity.setDistrict(district);
		entity.setProvince(province);
		entity.setWard(ward);

		userCvRepository.save(entity);
	}

	/**
	 * Get User CV (Only First One).
	 * 
	 * @param userId
	 * @return UserCvDTO
	 */
	public UserCvDTO getUserCV(String userId) {
		if (null == userService.getOwner(userId)) {
			throw new UnauthorizedAccessException("User is Unauthorized");
		}

		List<UserCv> entities = userCvRepository.findAllByUserId(userId);
		if (entities.size() > 0) {
			return UserCvDTO.toDTO(entities.get(0));
		} else {
			return null;
		}
	}

	/**
	 * Update User CV From Apply.
	 * 
	 * @param recruitmentApply
	 * @param id
	 */
	public void updateUserCVFromApply(RecruitmentApply recruitmentApply, Long id) {
		UserCv entity = null;
		if (id == 0L) {
			// insert news
			entity = new UserCv();
		} else {
			// override CV
			entity = userCvRepository.findOne(id);
		}
		entity = this.mappingBasic(recruitmentApply, entity);
		userCvRepository.save(entity);
	}

	/**
	 * Mapping.
	 * 
	 * @param from
	 * @param to
	 * @return UserCv
	 */
	private UserCv mappingBasic(RecruitmentApply from, UserCv to) {
		to.setUserId(from.getUserId());
		to.setUpdatedAt(DateUtils.getCurrentDate());
		to.setDescription(from.getDescription());
		to.setEducationLevel(from.getEducationLevel());
		to.setDistrict(from.getDistrict());
		to.setEducationName(from.getEducationName());
		to.setEmail(from.getEmail());
		to.setExperienceLevel(from.getExperienceLevel());
		to.setFullname(from.getFullname());
		to.setImageUrl(from.getImageUrl());
		to.setLanguage(from.getLanguage());
		to.setPhoneNumber(from.getPhoneNumber());
		to.setProvince(from.getProvince());
		to.setSex(from.getSex());
		to.setStreet(from.getStreet());
		to.setWard(from.getWard());
		to.setDateOfBirth(from.getDateOfBirth());
		return to;
	}

}
