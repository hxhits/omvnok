package vn.com.omart.driver.service.implement;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.com.omart.driver.entity.DriverInfo;
import vn.com.omart.driver.repository.DelivererRepository;
import vn.com.omart.driver.repository.DriverInfoRepository;
import vn.com.omart.driver.service.DelivererService;

@Service
public class DelivererServiceImpl implements DelivererService {

	@Autowired
	private DelivererRepository delivererRepository;

	@Autowired
	private DriverInfoRepository driverInfoRepository;

	@Override
	public List<String> getUserIds(Long poiId) {
		// TODO Auto-generated method stub
		List<String> userIds = delivererRepository.getUserIdByPoiId(poiId);
		return userIds;
	}

	@Override
	public List<Long> getPoiIds(String userId) {
		// TODO Auto-generated method stub
		List<Long> ids = ids = delivererRepository.getByDriver(userId);
		return ids;
	}
}
