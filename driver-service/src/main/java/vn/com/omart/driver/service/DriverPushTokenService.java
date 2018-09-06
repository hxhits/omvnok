package vn.com.omart.driver.service;

import org.springframework.web.bind.annotation.RequestBody;

import vn.com.omart.driver.common.constant.DriverType;
import vn.com.omart.driver.common.constant.DriverType.LockType;
import vn.com.omart.driver.dto.DriverPushTokenDTO;

public interface DriverPushTokenService {

	/**
	 * 
	 * @param Save.
	 * @param client
	 * @param dto
	 */
	public void save(String userId, DriverType.Device client, @RequestBody(required = true) DriverPushTokenDTO dto);

	/**
	 * Update delete.
	 * 
	 * @param userId
	 */
	public void delete(String userId);

	/**
	 * Update disable.
	 * 
	 * @param userId
	 */
	public void updateIsDisable(String userId);

	/**
	 * Update enable.
	 * 
	 * @param userId
	 */
	public void updateIsEnable(String userId);

	/**
	 * Update is blocked.
	 * 
	 * @param userId
	 * @param action
	 */
	public void updateIsBlocked(String userId, LockType action);
}
