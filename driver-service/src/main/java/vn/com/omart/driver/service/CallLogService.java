package vn.com.omart.driver.service;

import vn.com.omart.driver.dto.CallLogDTO;

public interface CallLogService {

	/**
	 * Save
	 * 
	 * @param userId
	 * @param location
	 * @param bookcarId
	 * @return
	 */
	public CallLogDTO save(String userId, String location, Long bookcarId);

	/**
	 * Get Call log.
	 * 
	 * @param bookcarId
	 * @return CallLogDTO
	 */
	public CallLogDTO getByBookcarOrderByCreatedAtDesc(Long bookcarId);

	/**
	 * Update state.
	 * 
	 * @param dto
	 */
	public void reject(CallLogDTO dto);

	
	/**
	 * Approve
	 * @param dto
	 */
	public void approve(CallLogDTO dto);
}
