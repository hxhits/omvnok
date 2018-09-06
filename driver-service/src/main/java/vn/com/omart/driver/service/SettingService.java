package vn.com.omart.driver.service;

import vn.com.omart.driver.dto.SettingDTO;
import vn.com.omart.driver.entity.DriverInfo;

public interface SettingService {

	/**
	 * Save default value.
	 * 
	 * @param diverInfo
	 */
	public void saveDefault(DriverInfo diverInfo);

	/**
	 * Update.
	 * 
	 * @param id
	 * @param dto
	 * @param userId
	 */
	public void updateOrInsert(String userId, SettingDTO dto);

	/**
	 * Copy driver info into setting.
	 */
	public void copyDriverInfoIntoSetting();
}
