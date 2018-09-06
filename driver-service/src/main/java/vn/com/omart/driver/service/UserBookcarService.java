package vn.com.omart.driver.service;

import vn.com.omart.driver.dto.BookCarDTO;

public interface UserBookcarService {
	/**
	 * Create. 
	 * @param userId
	 * @param dto
	 * @return
	 */
	public BookCarDTO create(String userId, BookCarDTO dto);
	
	public void receiveOrdering(String userId,BookCarDTO dto);
}
