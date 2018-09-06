package vn.com.omart.driver.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import vn.com.omart.driver.dto.DriverInfoDTO;
import vn.com.omart.driver.dto.UserDTO;
import vn.com.omart.driver.entity.DriverInfo;

public interface DriverInfoService {

	/**
	 * Save driver info.
	 * 
	 * @param dto
	 */
	public void save(DriverInfoDTO dto);

	/**
	 * Is Driver Existing.
	 * 
	 * @param userId
	 * @return boolean
	 */
	public boolean isDriverExisting(String userId);

	/**
	 * Is Driver Existing by phone number.
	 * 
	 * @param phone
	 * @return
	 */
	public boolean isDriverExistingByPhoneNumber(String phone);

	/**
	 * Get user profile.
	 * 
	 * @param userId
	 * @return DriverInfoDTO
	 */
	public DriverInfoDTO getDriverProfile(String userId);

	/**
	 * Action block driver.
	 * 
	 * @param userId
	 * @param dto
	 * 
	 */
	public void updateBlockDriver(String userId, DriverInfoDTO dto);

	/**
	 * Checking is driver blocked.
	 * 
	 * @param userId
	 * @return boolean
	 */
	public boolean isDriverBlocked(String userId);

	/**
	 * Rating
	 * 
	 * @param driverId
	 * @param star
	 */
	public void rating(String driverId, int star);

	/**
	 * Update
	 * 
	 * @param dto
	 */
	public void update(String userId, DriverInfoDTO dto);

	/**
	 * Get all driver info.
	 * 
	 * @return List of DriverInfoDTO
	 */
	public List<DriverInfoDTO> getAllDriverInfo(Pageable pageable);

	/**
	 * Update contract Id.
	 */
	public void updateContractId();

	/**
	 * Search between date of registration.
	 * 
	 * @param from
	 * @param to
	 * @param pageable
	 * @return List of DriverInfoDTO
	 */
	List<DriverInfoDTO> getByDateOfRegistrationAtBetween(Long from, Long to, Pageable pageable);

	/**
	 * Get by full name or phone
	 * 
	 * @param text
	 * @param pageable
	 * @return List of DriverInfoDTO
	 */
	public List<DriverInfoDTO> getByFullNameOrPhone(String text, Pageable pageable);

	/**
	 * Basic update.
	 * 
	 * @param userId
	 * @param dto
	 */
	public void basicUpdate(String userId, DriverInfoDTO dto);
}
