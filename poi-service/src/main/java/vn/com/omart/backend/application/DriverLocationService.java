package vn.com.omart.backend.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.com.omart.backend.domain.model.DriverLocationRepository;

@Service
public class DriverLocationService {

	@Autowired
	private DriverLocationRepository driverLocationRepository;

	@Autowired
	private StoredProcedureService storedProcedureService;

}
