package vn.com.omart.driver.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import vn.com.omart.driver.dto.DriverLocationDTO;
import vn.com.omart.driver.dto.response.EmptyJsonResponse;
import vn.com.omart.driver.service.DriverLocationService;

@RestController
@RequestMapping("/v1/driver")
public class DriverLocationController {

	@Autowired
	private DriverLocationService driverLocationService;

	/**
	 * Update location.
	 * 
	 * @param location
	 * @param userId
	 * @param dto
	 * @return EmptyJsonResponse
	 */
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping(value = "/current-location")
	public EmptyJsonResponse locationUpdate(@RequestHeader(value = "X-Location-Geo", required = true) String location,
			@RequestHeader(value = "X-User-Id", required = false) String userId,
			@RequestBody(required = true) DriverLocationDTO dto) {
		dto.setLocation(location);
		dto.setUserId(userId);
		driverLocationService.locationUpdate(dto);
		return new EmptyJsonResponse();
	}

	/**
	 * Get Nearest driver.
	 * 
	 * @param userId
	 * @param location
	 * @param driverLocationDTO
	 * @return List of DriverLocationDTO
	 */
	@RequestMapping(value = "/nearest", method = RequestMethod.POST)
	public ResponseEntity<List<DriverLocationDTO>> getNearestDriver(
			@RequestHeader(value = "X-User-id", required = true) String userId,
			@RequestHeader(value = "X-Location-Geo", required = true) String location,
			@RequestBody(required = false) DriverLocationDTO driverLocationDTO) {
		List<DriverLocationDTO> driverLocations = driverLocationService.getNearestDriver(userId, location,
				driverLocationDTO);
		if (!driverLocations.isEmpty()) {
			return new ResponseEntity<List<DriverLocationDTO>>(driverLocations, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	/**
	 * Get location by driver id.
	 * 
	 * @param userId
	 * @return DriverLocationDTO
	 */
	@GetMapping(value = "/{driverId}/location")
	public ResponseEntity<DriverLocationDTO> getLocationByDriverId(
			@RequestHeader(value = "X-User-id", required = true) String userId,
			@PathVariable(value = "driverId", required = true) String driverId) {
		DriverLocationDTO driverLocations = driverLocationService.getLocationByDriverId(driverId);
		if (driverLocations != null) {
			return new ResponseEntity<DriverLocationDTO>(driverLocations, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

}
