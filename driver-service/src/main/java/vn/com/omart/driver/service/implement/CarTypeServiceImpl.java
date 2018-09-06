package vn.com.omart.driver.service.implement;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.com.omart.driver.common.constant.CommonConstant;
import vn.com.omart.driver.common.constant.DriverResponse;
import vn.com.omart.driver.common.exception.DriverException;
import vn.com.omart.driver.dto.CarTypeDTO;
import vn.com.omart.driver.dto.InitialCarDTO;
import vn.com.omart.driver.entity.CarType;
import vn.com.omart.driver.repository.CarTypeRepository;
import vn.com.omart.driver.service.CarTypeService;
import vn.com.omart.driver.service.DriverDistFollowService;
import vn.com.omart.driver.service.DriverFollowService;

@Service
public class CarTypeServiceImpl implements CarTypeService {

	@Autowired
	private CarTypeRepository carTypeRepository;

	@Autowired
	private DriverDistFollowService driverDistFollowService;

	@Autowired
	private DriverFollowService driverFollowService;

	/**
	 * Initial
	 */
	@Override
	public InitialCarDTO initial(String lang, String userId) {
		// TODO Auto-generated method stub
		InitialCarDTO dto = new InitialCarDTO();
//		List<CarType> entities = carTypeRepository.findAll();
//		if (entities.isEmpty()) {
//			throw new DriverException(DriverResponse.CAR_TYPE_EMPTY);
//		}

		if (lang.equals(CommonConstant.LANGUAGE_EN)) {
			dto.setCarDurations(CommonConstant.CAR_DURATION_EN);
		} else {
			dto.setCarDurations(CommonConstant.CAR_DURATION_VI);
		}
		//List<CarTypeDTO> dtos = entities.stream().map(entity -> CarTypeDTO.toDTO(entity, lang))
				//.collect(Collectors.toList());
		dto.setCarTypes(this.getCarTypes(lang));
		if (!userId.isEmpty()) {
			dto.setCarTypeSelectedStr(driverFollowService.getCarTypeFollowingByUserId(userId));
			dto.setProvinceDistrictSelectedStr(driverDistFollowService.getDistFollowingByUserId(userId));
		}
		return dto;
	}
	
	@Override
	public List<CarTypeDTO> getCarTypes(String lang) {
		List<CarType> entities = carTypeRepository.findAll();
		if (entities.isEmpty()) {
			throw new DriverException(DriverResponse.CAR_TYPE_EMPTY);
		}
		List<CarTypeDTO> dtos = entities.stream().map(entity -> CarTypeDTO.toDTO(entity, lang))
				.collect(Collectors.toList());
		return dtos;
	}

}
