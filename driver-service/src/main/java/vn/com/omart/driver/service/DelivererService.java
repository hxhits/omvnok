package vn.com.omart.driver.service;

import java.util.List;

public interface DelivererService {
	
	public List<String> getUserIds(Long poiId);
	
	public List<Long> getPoiIds(String userId);
	
}
