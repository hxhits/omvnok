package vn.com.omart.backend.application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.com.omart.backend.application.response.RecruitmentApplyDTO;
import vn.com.omart.backend.application.response.RecruitmentPositionDTO;
import vn.com.omart.backend.domain.model.District;
import vn.com.omart.backend.domain.model.DistrictRepository;
import vn.com.omart.backend.domain.model.PointOfInterest;
import vn.com.omart.backend.domain.model.PointOfInterestRepository;
import vn.com.omart.backend.domain.model.Province;
import vn.com.omart.backend.domain.model.ProvinceRepository;
import vn.com.omart.backend.domain.model.Recruitment;
import vn.com.omart.backend.domain.model.RecruitmentApply;
import vn.com.omart.backend.domain.model.RecruitmentApplyRepository;
import vn.com.omart.backend.domain.model.RecruitmentPosition;
import vn.com.omart.backend.domain.model.RecruitmentPositionRepository;
import vn.com.omart.backend.domain.model.RecruitmentRepository;
import vn.com.omart.backend.domain.model.Ward;
import vn.com.omart.backend.domain.model.WardRepository;
import vn.com.omart.backend.port.adapter.userprofile.UserService;
import vn.com.omart.sharedkernel.application.model.error.NotFoundException;
import vn.com.omart.sharedkernel.application.model.error.UnauthorizedAccessException;

@Service
public class RecruitmentPositionService {

	@Autowired
	private RecruitmentPositionRepository recPosRepository;

	@Autowired
	private RecruitmentApplyRepository recApplyRepository;

	@Autowired
	private ProvinceRepository provinceRepository;
	@Autowired
	private DistrictRepository districtRepository;
	@Autowired
	private WardRepository wardRepository;

	@Autowired
	private PointOfInterestRepository pointOfInterestRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private UserCvService userCvService;

	@Autowired
	private RecruitmentRepository recruitmentRepository;
	
	@Autowired
	private UserCvNotificationService userCvNotificationService;

	/**
	 * Get All Recruit Position.
	 * 
	 * @param userId
	 * @return List of RecruitmentPositionDTO
	 */
	public List<RecruitmentPositionDTO> getAllRecruitPosition(String userId) {
		List<RecruitmentPosition> entitys = recPosRepository.findAllByIsDisabledOrderByOrder(false);
		return entitys.stream().map(RecruitmentPositionDTO::toDTO).collect(Collectors.toList());
	}

	/**
	 * Save.
	 * 
	 * @param dto
	 */
	@Transactional(readOnly = false)
	public void save(RecruitmentApplyDTO dto, String userId) {
		if (null == userService.getOwner(userId)) {
			throw new UnauthorizedAccessException("User is Unauthorized");
		}

		// save RecruitmentApply.
		RecruitmentApply entity = RecruitmentApplyDTO.fromDTO(dto);

		Province province = this.provinceRepository.findOne(dto.getProvince());
		if (null == province) {
			throw new NotFoundException("Province not exists");
		}
		District district = this.districtRepository.findByIdAndProvinceId(dto.getDistrict(), province.id());
		if (null == district) {
			throw new NotFoundException("District not exists");
		}
		Ward ward = this.wardRepository.findByIdAndDistrictId(dto.getWard(), district.id());
		if (null == ward) {
			throw new NotFoundException("Ward not exists");
		}

		Recruitment recruitment = recruitmentRepository.findOne(dto.getRecruitmentId());
		if (null == recruitment) {
			throw new NotFoundException("Recruitment not exists");
		}

		entity.setUserId(userId);
		entity.setRecruitment(recruitment);
		entity.setDistrict(district);
		entity.setProvince(province);
		entity.setWard(ward);
		entity = recApplyRepository.save(entity);
		// override user CV.
		if (dto.isOverrideUserCV()) {
			userCvService.updateUserCVFromApply(entity, dto.getUserCVId());
		}
	}

	/**
	 * Get All RecruitApply.
	 * 
	 * @param userId
	 * @return List of RecruitmentApplyDTO
	 */
	public List<RecruitmentApplyDTO> getAllRecruitApply(String userId) {
		if (null == userService.getOwner(userId)) {
			throw new UnauthorizedAccessException("User is Unauthorized");
		}

		List<RecruitmentApply> entities = recApplyRepository.findAllByIsDeleted(false);
		return entities.stream().map(RecruitmentApplyDTO::toDTO).collect(Collectors.toList());
	}

	/**
	 * Get RecruitApply of Poi.
	 * 
	 * @param userId
	 * @param poiId
	 * @return List of RecruitmentApplyDTO
	 */
	public List<RecruitmentApplyDTO> getRecruitApplyByPoi(String userId, Long poiId) {
		if (null == userService.getOwner(userId)) {
			throw new UnauthorizedAccessException("User is Unauthorized");
		}

		PointOfInterest poi = pointOfInterestRepository.findOne(poiId);
		if (null == poi) {
			throw new NotFoundException("POI not found.");
		}

		List<Recruitment> entities = recruitmentRepository.findByPoiOrderByCreatedAtDesc(poi);

		List<RecruitmentApplyDTO> dtos = new ArrayList<RecruitmentApplyDTO>();
		for (Recruitment rec : entities) {
			dtos.addAll(rec.getRecruitmentApply(false).stream().map(RecruitmentApplyDTO::toDTO)
					.collect(Collectors.toList()));
		}

		Collections.sort(dtos, new Comparator<RecruitmentApplyDTO>() {
			@Override
			public int compare(RecruitmentApplyDTO o1, RecruitmentApplyDTO o2) {
				return o2.getCreatedAt().compareTo(o1.getCreatedAt());
			}
		});

		return dtos;
	}

	/**
	 * Get RecruitApply of User.
	 * 
	 * @param userId
	 * @return List of RecruitmentApplyDTO
	 */
	public List<RecruitmentApplyDTO> getRecruitApplyOfUser(String userId) {
		if (null == userService.getOwner(userId)) {
			throw new UnauthorizedAccessException("User is Unauthorized");
		}

		List<RecruitmentApply> entities = recApplyRepository.findAllByIsDeletedAndUserIdOrderByUpdatedAtDesc(false, userId);
		return entities.stream().map(RecruitmentApplyDTO::toDTO).collect(Collectors.toList());
	}

	/**
	 * Delete.
	 * 
	 * @param id
	 */
	public void deleteApply(Long id) {
		RecruitmentApply entity = recApplyRepository.findOne(id);
		if (null == entity) {
			throw new NotFoundException("RecruitmentApply not found");
		}
		entity.setDeleted(true);
		recApplyRepository.save(entity);
	}

	/**
	 * Update Status.
	 * 
	 * @param id
	 */
	public void updateStatus(Long id, RecruitmentApplyDTO dto) {
		RecruitmentApply entity = recApplyRepository.findOne(id);
		if (null == entity) {
			throw new NotFoundException("RecruitmentApply not found");
		}
		entity.setStatus(dto.getStatus());
		recApplyRepository.save(entity);
	}

	/**
	 * Update Status.
	 * 
	 * @param id
	 */
	public void updateStatus(List<RecruitmentApplyDTO> dtos) {
		Map<Long, RecruitmentApplyDTO> dicDto = dtos.stream()
				.collect(Collectors.toMap(RecruitmentApplyDTO::getId, Function.identity()));
		List<RecruitmentApply> entities = recApplyRepository
				.findAll(dtos.stream().map(dto -> dto.getId()).collect(Collectors.toList()));
		for (RecruitmentApply entity : entities) {
			entity.setStatus(dicDto.get(entity.getId()).getStatus());
		}
		recApplyRepository.save(entities);
	}

}
