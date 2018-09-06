package vn.com.omart.driver.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import vn.com.omart.driver.dto.BookCarDTO;
import vn.com.omart.driver.dto.DriverFollowDTO;
import vn.com.omart.driver.entity.BookCar;

public interface BookCarService {

	/**
	 * Get book car.
	 * 
	 * @param id
	 * @return BookCar
	 */
	public BookCar getBookCarById(Long id);

	/**
	 * Get book car by driver following.
	 * 
	 * @param dto
	 * @param pageable
	 * @return List of BookCar
	 */
	public List<BookCar> getBookCarByCarTypeAndProvinceAndDistrict(String userId, DriverFollowDTO dto,
			Pageable pageable);

	/**
	 * Get state.
	 * 
	 * @param id
	 * @return state
	 */
	public BookCarDTO getBookCarStateById(Long id);

	/**
	 * Accept Delivery.
	 * 
	 * @param userId
	 * @param dto
	 */
	public void acceptDelivery(String userId, BookCarDTO dto);
}
