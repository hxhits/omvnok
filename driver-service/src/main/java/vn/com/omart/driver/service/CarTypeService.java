package vn.com.omart.driver.service;

import java.util.List;

import vn.com.omart.driver.dto.CarTypeDTO;
import vn.com.omart.driver.dto.InitialCarDTO;

public interface CarTypeService {

	/**
	 * Initial
	 * 
	 * @param lang
	 * @param userId
	 * @return InitialCarDTO
	 */
	public InitialCarDTO initial(String lang, String userId);

	/**
	 * Get car types.
	 * 
	 * @param lang
	 * @return List of CarTypeDTO
	 */
	public List<CarTypeDTO> getCarTypes(String lang);
}
