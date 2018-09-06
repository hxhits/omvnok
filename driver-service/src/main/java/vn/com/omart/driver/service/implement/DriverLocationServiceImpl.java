package vn.com.omart.driver.service.implement;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.omart.driver.common.constant.DriverResponse;
import vn.com.omart.driver.common.exception.DriverException;
import vn.com.omart.driver.dto.DriverLocationDTO;
import vn.com.omart.driver.dto.request.StoreProcedureParams;
import vn.com.omart.driver.entity.CarType;
import vn.com.omart.driver.entity.DriverLocation;
import vn.com.omart.driver.repository.CarTypeRepository;
import vn.com.omart.driver.repository.DriverLocationRepository;
import vn.com.omart.driver.service.DriverLocationService;
import vn.com.omart.driver.service.StoredProcedureService;

@Service
public class DriverLocationServiceImpl implements DriverLocationService {

	private Logger logger = LoggerFactory.getLogger(DriverLocationServiceImpl.class);

	@Autowired
	private DriverLocationRepository driverLocationRepository;

	@Autowired
	private CarTypeRepository carTypeRepository;

	@Autowired
	private StoredProcedureService storedProcedureService;

	/**
	 * Update location
	 */
	@Override
	public void locationUpdate(DriverLocationDTO dto) {
		// TODO Auto-generated method stub
		DriverLocation entity = DriverLocationDTO.toEntity(dto);
		CarType carType = carTypeRepository.findOne(dto.getCarTypeId());
		if (carType == null) {
			throw new DriverException(DriverResponse.CAR_TYPE_EMPTY);
		}
		entity.setCarType(carType);
		driverLocationRepository.save(entity);
	}

	/**
	 * Get nearest driver.
	 */
	@Override
	public List<DriverLocationDTO> getNearestDriver(String userId, String location,
			DriverLocationDTO driverLocationDTO) {
		// TODO Auto-generated method stub
		String[] locations = location.split(",");
		Double latitude = Double.valueOf(String.valueOf(locations[0]));
		Double longitude = Double.valueOf(String.valueOf(locations[1]));

		StoreProcedureParams params = new StoreProcedureParams();
		params.setLatitude(latitude);
		params.setLongitude(longitude);
		params.setRadius(5000); // is 5km
		params.setPage(0);
		params.setSize(Integer.MAX_VALUE);

		if (driverLocationDTO != null && driverLocationDTO.getCarTypeId() != null) {
			params.setCarTypeId(driverLocationDTO.getCarTypeId().intValue());
		}

		List<Object[]> drLocats = storedProcedureService.getStoredProcedureQueryWithLatLngRad("get_nearest_driver_v1",
				params);
		List<DriverLocationDTO> dto = drLocats.stream().map(DriverLocationDTO::toDTO).collect(Collectors.toList());
		return dto;

	}

	@Override
	public DriverLocationDTO getLocationByDriverId(String userId) {
		// TODO Auto-generated method stub
		DriverLocation entity = driverLocationRepository.findOne(userId);
		if (entity != null) {
			DriverLocationDTO dto = new DriverLocationDTO();
			dto.setLatitude(entity.getLatitude());
			dto.setLongitude(entity.getLongitude());
			return dto;
		} else {
			logger.error("\n [GET LOCATION] driver id not found with userId" + userId);
		}
		return null;
	}
}
