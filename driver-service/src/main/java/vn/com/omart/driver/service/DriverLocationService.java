package vn.com.omart.driver.service;

import java.util.List;

import vn.com.omart.driver.dto.DriverLocationDTO;

public interface DriverLocationService {

	/**
	 * Update location.
	 * 
	 * @param dto
	 */
	public void locationUpdate(DriverLocationDTO dto);

	/**
	 * Get driver nearest.
	 * 
	 * @param userId
	 * @param location
	 * @param driverLocationDTO
	 * @return List of DriverLocationDTO
	 */
	public List<DriverLocationDTO> getNearestDriver(String userId, String location,
			DriverLocationDTO driverLocationDTO);

	/**
	 * Get location by driver id.
	 * 
	 * @param userId
	 * @return DriverLocationDTO
	 */
	public DriverLocationDTO getLocationByDriverId(String userId);

}
