package vn.com.omart.backend.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.com.omart.backend.application.response.RecruitmentPositionFollowDTO;
import vn.com.omart.backend.domain.model.RecruitmentPositionFollow;
import vn.com.omart.backend.domain.model.RecruitmentPositionFollowRepository;
import vn.com.omart.backend.domain.model.RecruitmentPositionLevel;
import vn.com.omart.backend.domain.model.RecruitmentPositionLevelRepository;

@Service
public class RecruitmentPositionFollowService {

	@Autowired
	private RecruitmentPositionLevelRepository recruitmentPositionLevelRepository;

	@Autowired
	private RecruitmentPositionFollowRepository recruitmentPositionFollowRepository;
	
	@Autowired
	private RecruitmentDistrictFollowService recruitmentDistrictFollowService;

	/**
	 * Save.
	 * 
	 * @param userId
	 * @param dto
	 */
	@Transactional(readOnly = false)
	public void save(String userId, RecruitmentPositionFollowDTO dto) {
		// Delete.
		List<RecruitmentPositionFollow> rectPosFollows = recruitmentPositionFollowRepository.findByUserId(userId);
		if (!rectPosFollows.isEmpty()) {
			recruitmentPositionFollowRepository.delete(rectPosFollows);
		}
		if (!dto.getPositionsSelectedStr().isEmpty()) {
			List<RecruitmentPositionFollow> entityList = new ArrayList<>();
			// get category id from string.
			String[] posIds = dto.getPositionsSelectedStr().split(",");
			for (String id : posIds) {
				RecruitmentPositionFollow entity = new RecruitmentPositionFollow();
				RecruitmentPositionLevel rectPosition = recruitmentPositionLevelRepository.findOne(Long.parseLong(id.trim()));
				if (rectPosition != null) {
					entity.setRecruitmentPosition(rectPosition);
					entity.setUserId(userId);
					entityList.add(entity);
				}
			}
			// Insert new.
			recruitmentPositionFollowRepository.save(entityList);
			String provinceDistrictSelectedStr = dto.getProvinceDistrictSelectedStr();
			recruitmentDistrictFollowService.save(userId, provinceDistrictSelectedStr);
		}
	}

	/**
	 * Get DTO Position Follow By User Id.
	 * 
	 * @param userId
	 * @return DTO
	 */
	public RecruitmentPositionFollowDTO getPositionFollowDTOByUserId(String userId) {
		String provinceDistrictSelectedStr = recruitmentDistrictFollowService.getProvinceDistrictSelectedStr(userId);
		String posIdString = "";
		List<RecruitmentPositionFollow> positionFollows = recruitmentPositionFollowRepository.findByUserId(userId);
		for (RecruitmentPositionFollow entity : positionFollows) {
			posIdString += "," + entity.getRecruitmentPosition().getId();
		}
		posIdString = posIdString.replaceFirst(",", "");
		
		RecruitmentPositionFollowDTO dto = new RecruitmentPositionFollowDTO();
		dto.setPositionsSelectedStr(posIdString);
		dto.setProvinceDistrictSelectedStr(provinceDistrictSelectedStr);
		return dto;
	}
}
