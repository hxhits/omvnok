package vn.com.omart.backend.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.com.omart.backend.application.response.DelivererDTO;
import vn.com.omart.backend.domain.model.Deliverer;
import vn.com.omart.backend.domain.model.DelivererRepository;
import vn.com.omart.backend.domain.model.DriverInfo;
import vn.com.omart.backend.domain.model.DriverInfoRepository;
import vn.com.omart.backend.domain.model.PointOfInterest;
import vn.com.omart.backend.domain.model.PointOfInterestRepository;
import vn.com.omart.sharedkernel.application.model.error.AlreadyExistingException;
import vn.com.omart.sharedkernel.application.model.error.NotFoundException;

@Service
public class DelivererService {

	@Autowired
	private DelivererRepository delivererRepository;

	@Autowired
	private PointOfInterestRepository pointOfInterestRepository;

	@Autowired
	private DriverInfoRepository driverInfoRepository;

	public DelivererDTO save(DelivererDTO dto) {
		DriverInfo driver = driverInfoRepository.findByPhoneNumber(dto.getPhoneNumber());
		if (driver != null) {
			PointOfInterest poi = pointOfInterestRepository.findOne(dto.getPoiId());
			if (poi != null) {
				Deliverer entity = delivererRepository.findByDriverAndPoi(driver, poi);
				if(entity == null) {
					entity = new Deliverer();
					entity.setPoi(poi);
					entity.setDriver(driver);
					entity = delivererRepository.save(entity);
					return DelivererDTO.toDTO(entity);
				} else {
					throw new AlreadyExistingException("This " + dto.getPhoneNumber() + " is existing in deliverer of shop");
				}
			}
		} else {
			throw new NotFoundException("This " + dto.getPhoneNumber() + " is not a driver");
		}
		return new DelivererDTO();
	}
	
	public List<DelivererDTO> get(long poiId) {
		PointOfInterest poi = pointOfInterestRepository.findOne(poiId);
		if (poi != null) {
			List<Deliverer> entities = delivererRepository.findAllByPoi(poi);
			List<DelivererDTO> dtos = entities.stream().map(DelivererDTO::toDTO).collect(Collectors.toList());
			return dtos;
		}
		return new ArrayList<>();
	}

	public void delete(long id) {
		Deliverer entity = delivererRepository.findOne(id);
		if (entity != null) {
			delivererRepository.delete(entity);
		}
	}

}
