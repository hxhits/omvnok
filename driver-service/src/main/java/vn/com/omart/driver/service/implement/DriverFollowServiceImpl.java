package vn.com.omart.driver.service.implement;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.com.omart.driver.dto.DriverFollowDTO;
import vn.com.omart.driver.entity.CarType;
import vn.com.omart.driver.entity.DriverFollow;
import vn.com.omart.driver.repository.CarTypeRepository;
import vn.com.omart.driver.repository.DriverFollowRepository;
import vn.com.omart.driver.service.DriverDistFollowService;
import vn.com.omart.driver.service.DriverFollowService;

@Service
public class DriverFollowServiceImpl implements DriverFollowService {

	@Autowired
	private DriverFollowRepository driverFollowRepository;

	@Autowired
	private DriverDistFollowService driverDistFollowService;

	@Autowired
	private CarTypeRepository carTypeRepository;

	/**
	 * Save
	 */
	@Transactional(readOnly = false)
	@Override
	public void save(String userId, DriverFollowDTO dto) {
		// TODO Auto-generated method stub

		List<DriverFollow> driverFollows = driverFollowRepository.findByUserId(userId);
		// reset.
		if (!driverFollows.isEmpty()) {
			driverFollowRepository.delete(driverFollows);
		}

		if (!dto.getCarTypeSelectedStr().isEmpty()) {
			List<DriverFollow> entities = new ArrayList<DriverFollow>();
			String[] carTypeIds = dto.getCarTypeSelectedStr().replaceAll(" ", "").split(",");
			for (String value : carTypeIds) {
				Long id = Long.parseLong(value);
				DriverFollow entity = new DriverFollow();
				CarType carType = carTypeRepository.findOne(id);
				entity.setCarType(carType);
				entity.setUserId(userId);
				entities.add(entity);
			}
			driverFollowRepository.save(entities);
			driverDistFollowService.save(userId, dto);
		}
	}

	/**
	 * Get car type that user is following.
	 */
	@Override
	public String getCarTypeFollowingByUserId(String userId) {
		// TODO Auto-generated method stub
		String carTypeStr = "";
		List<DriverFollow> driverFollows = driverFollowRepository.findByUserId(userId);
		if (!driverFollows.isEmpty()) {
			for (DriverFollow item : driverFollows) {
				carTypeStr += "," + item.getCarType().getId();
			}
			carTypeStr = carTypeStr.replaceFirst(",", "");
		}
		return carTypeStr;
	}

	/**
	 * Save
	 */
	@Override
	@Transactional(readOnly = false)
	public void save(String userId, CarType carType) {
		List<DriverFollow> driverFollows = driverFollowRepository.findByUserId(userId);
		// reset.
		if (!driverFollows.isEmpty()) {
			driverFollowRepository.delete(driverFollows);
		}
		// add new.
		DriverFollow entity = new DriverFollow();
		entity.setCarType(carType);
		entity.setUserId(userId);
		driverFollowRepository.save(entity);
	}

}
