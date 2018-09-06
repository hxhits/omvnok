package vn.com.omart.driver.service.implement;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.com.omart.driver.dto.DriverFollowDTO;
import vn.com.omart.driver.entity.DriverDistFollow;
import vn.com.omart.driver.repository.DriverDistFollowRepository;
import vn.com.omart.driver.service.DriverDistFollowService;

@Service
public class DriverDistFollowServiceImpl implements DriverDistFollowService {

	@Autowired
	private DriverDistFollowRepository driverDistFollowRepository;

	/**
	 * Save
	 */
	@Transactional(readOnly = false)
	@Override
	public void save(String userId, DriverFollowDTO dto) {
		// TODO Auto-generated method stub

		List<DriverDistFollow> driverDistFollows = driverDistFollowRepository.findByUserId(userId);
		// reset.
		if (!driverDistFollows.isEmpty()) {
			driverDistFollowRepository.delete(driverDistFollows);
		}
		// save.
		Long provinceId = 0L;
		Long districtId = 0L;
		if (!dto.getProvinceDistrictSelectedStr().isEmpty()) {
			String[] pdArray = dto.getProvinceDistrictSelectedStr().replaceAll(" ", "").split(",");
			if (pdArray.length == 1) {
				provinceId = Long.parseLong(pdArray[0]);
				DriverDistFollow entity = new DriverDistFollow();
				entity.setUserId(userId);
				entity.setDistrictId(districtId);
				entity.setProvinceId(provinceId);
				driverDistFollowRepository.save(entity);
			} else {
				List<DriverDistFollow> entities = new ArrayList<DriverDistFollow>();
				provinceId = Long.parseLong(pdArray[0]);
				for (int i = 1; i < pdArray.length; i++) {
					districtId = Long.parseLong(pdArray[i]);
					DriverDistFollow entity = new DriverDistFollow();
					entity.setUserId(userId);
					entity.setDistrictId(districtId);
					entity.setProvinceId(provinceId);
					entities.add(entity);
				}
				driverDistFollowRepository.save(entities);
			}
		} else {
			DriverDistFollow entity = new DriverDistFollow();
			entity.setUserId(userId);
			entity.setDistrictId(districtId);
			entity.setProvinceId(provinceId);
			driverDistFollowRepository.save(entity);
		}
	}

	/**
	 * Get car province and districts that user is following.
	 */
	@Override
	public String getDistFollowingByUserId(String userId) {
		// TODO Auto-generated method stub
		String distStr = "";
		List<DriverDistFollow> driverDistFollows = driverDistFollowRepository.findByUserId(userId);
		if (!driverDistFollows.isEmpty()) {
			if (!driverDistFollows.get(0).getProvinceId().equals(0L)) {
				distStr += driverDistFollows.get(0).getProvinceId();
				for (int i = 0; i < driverDistFollows.size(); i++) {
					if (!driverDistFollows.get(i).getDistrictId().equals(0L)) {
						distStr += "," + driverDistFollows.get(i).getDistrictId();
					}
				}
			}
		}
		return distStr;
	}

	/**
	 * Save.
	 */
	@Override
	@Transactional(readOnly = false)
	public void save(String userId, Long provinceId, Long districtId) {
		List<DriverDistFollow> driverDistFollows = driverDistFollowRepository.findByUserId(userId);
		// reset.
		if (!driverDistFollows.isEmpty()) {
			driverDistFollowRepository.delete(driverDistFollows);
		}
		// add new.
		DriverDistFollow entity = new DriverDistFollow();
		entity.setUserId(userId);
		entity.setProvinceId(provinceId);
		entity.setDistrictId(districtId);
		driverDistFollowRepository.save(entity);
	}

}
