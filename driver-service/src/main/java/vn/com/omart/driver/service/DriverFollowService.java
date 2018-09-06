package vn.com.omart.driver.service;

import vn.com.omart.driver.dto.DriverFollowDTO;
import vn.com.omart.driver.entity.CarType;

public interface DriverFollowService {

	/**
	 * Save
	 * 
	 * @param userId
	 * @param dto
	 */
	public void save(String userId, DriverFollowDTO dto);

	/**
	 * Get car type that user is following.
	 * 
	 * @param userId
	 * @return String
	 */
	public String getCarTypeFollowingByUserId(String userId);

	/**
	 * Save
	 * 
	 * @param userId
	 * @param carType
	 */
	public void save(String userId, CarType carType);
}
