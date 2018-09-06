package vn.com.omart.driver.service;

import vn.com.omart.driver.dto.UserDTO;

public interface ServiceUtil {

	/**
	 * Get User profile.
	 * 
	 * @param userId
	 * @return UserDTO
	 */
	public UserDTO getUserProfile(String userId);

}
