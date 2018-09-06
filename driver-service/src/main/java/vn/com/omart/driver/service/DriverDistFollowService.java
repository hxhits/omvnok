package vn.com.omart.driver.service;

import vn.com.omart.driver.dto.DriverFollowDTO;

public interface DriverDistFollowService {

	/**
	 * Save
	 * 
	 * @param userId
	 * @param dto
	 */
	public void save(String userId, DriverFollowDTO dto);

	/**
	 * Get car province and districts that user is following.
	 * 
	 * @param userId
	 * @return String
	 */
	public String getDistFollowingByUserId(String userId);

	/**
	 * Save.
	 * 
	 * @param userId
	 * @param provinceId
	 * @param districtId
	 */
	public void save(String userId, Long provinceId, Long districtId);

}
