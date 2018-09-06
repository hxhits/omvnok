package vn.com.omart.backend.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.com.omart.backend.domain.model.DistrictFollow;
import vn.com.omart.backend.domain.model.DistrictFollowRepository;

@Service
public class DistrictFollowService {

	@Autowired
	private DistrictFollowRepository districtFollowRepository;

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
		List<DistrictFollow> districtFollows = districtFollowRepository.findByUserId(userId);
		if (!districtFollows.isEmpty()) {
			districtFollowRepository.delete(districtFollows);
		}
		// insert.
		Long provinceId = 0L;
		Long districtId = 0L;
		if (!provinceDistrictSelectedStr.isEmpty()) {
			String[] strArray = provinceDistrictSelectedStr.replace(" ", "").split(",");
			if (strArray.length == 1) {
				DistrictFollow entity = new DistrictFollow();
				provinceId = Long.parseLong(strArray[0]);
				entity.setProvinceId(provinceId);
				entity.setDistrictId(districtId);
				entity.setUserId(userId);
				districtFollowRepository.save(entity);
			} else {
				List<DistrictFollow> entities = new ArrayList<>();
				provinceId = Long.parseLong(strArray[0]);
				for (int i = 1; i < strArray.length; i++) {
					DistrictFollow entity = new DistrictFollow();
					districtId = Long.parseLong(strArray[i]);
					entity.setUserId(userId);
					entity.setProvinceId(provinceId);
					entity.setDistrictId(districtId);
					entities.add(entity);
				}
				districtFollowRepository.save(entities);
			}
		} else {
			DistrictFollow entity = new DistrictFollow();
			entity.setUserId(userId);
			entity.setProvinceId(provinceId);
			entity.setDistrictId(districtId);
			districtFollowRepository.save(entity);
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
		List<DistrictFollow> districtFollows = districtFollowRepository.findByUserId(userId);
		boolean isIgnored = false;
		for (DistrictFollow entity : districtFollows) {
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
