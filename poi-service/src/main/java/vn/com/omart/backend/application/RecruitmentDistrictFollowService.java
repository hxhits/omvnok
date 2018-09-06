package vn.com.omart.backend.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.com.omart.backend.domain.model.RecruitmentDistrictFollow;
import vn.com.omart.backend.domain.model.RecruitmentDistrictFollowRepository;

@Service
public class RecruitmentDistrictFollowService {

	@Autowired
	private RecruitmentDistrictFollowRepository recruitmentDistrictFollowRepository;

	/**
	 * Save
	 * 
	 * @param userId
	 * @param provinceDistrictSelectedStr
	 *            format: {provinceId,districtId 1,...districtId n}
	 */
	@Transactional(readOnly = false)
	public void save(String userId, String provinceDistrictSelectedStr) {
		// reset.
		List<RecruitmentDistrictFollow> districtFollows = recruitmentDistrictFollowRepository.findByUserId(userId);
		if (!districtFollows.isEmpty()) {
			recruitmentDistrictFollowRepository.delete(districtFollows);
		}
		// insert.
		Long provinceId = 0L;
		Long districtId = 0L;
		if (!provinceDistrictSelectedStr.isEmpty()) {
			String[] strArray = provinceDistrictSelectedStr.replace(" ", "").split(",");
			if (strArray.length == 1) {
				RecruitmentDistrictFollow entity = new RecruitmentDistrictFollow();
				provinceId = Long.parseLong(strArray[0]);
				entity.setProvinceId(provinceId);
				entity.setDistrictId(districtId);
				entity.setUserId(userId);
				recruitmentDistrictFollowRepository.save(entity);
			} else {
				List<RecruitmentDistrictFollow> entities = new ArrayList<>();
				provinceId = Long.parseLong(strArray[0]);
				for (int i = 1; i < strArray.length; i++) {
					RecruitmentDistrictFollow entity = new RecruitmentDistrictFollow();
					districtId = Long.parseLong(strArray[i]);
					entity.setUserId(userId);
					entity.setProvinceId(provinceId);
					entity.setDistrictId(districtId);
					entities.add(entity);
				}
				recruitmentDistrictFollowRepository.save(entities);
			}
		} else {
			RecruitmentDistrictFollow entity = new RecruitmentDistrictFollow();
			entity.setUserId(userId);
			entity.setProvinceId(provinceId);
			entity.setDistrictId(districtId);
			recruitmentDistrictFollowRepository.save(entity);
		}
	}

	/**
	 * Get Province District Selected Str.
	 * 
	 * @param userId
	 * @return
	 */
	public String getProvinceDistrictSelectedStr(String userId) {
		String provinceDistrictSelectedStr = "";
		List<RecruitmentDistrictFollow> districtFollows = recruitmentDistrictFollowRepository.findByUserId(userId);
		boolean isIgnored = false;
		for (RecruitmentDistrictFollow entity : districtFollows) {
			if (entity.getProvinceId() != null && !isIgnored) {
				provinceDistrictSelectedStr += entity.getProvinceId();
				isIgnored = true;
			}
			if (entity.getDistrictId() > 0L) {
				provinceDistrictSelectedStr += "," + entity.getDistrictId();
			}
		}
		return provinceDistrictSelectedStr;
	}

}
