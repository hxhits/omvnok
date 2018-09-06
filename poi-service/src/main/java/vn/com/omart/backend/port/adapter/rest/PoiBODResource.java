package vn.com.omart.backend.port.adapter.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.com.omart.backend.application.PoiBODService;
import vn.com.omart.backend.application.response.PoiBODOrderDTO;
import vn.com.omart.backend.application.response.PointOfInterestDTO;

@RestController
@RequestMapping("/v1/bod")
public class PoiBODResource {

	@Autowired
	private PoiBODService poiBODService;

	/**
	 * Get poi by BOD User.
	 * 
	 * @param userId
	 * @param pageable
	 * @return List of PointOfInterestDTO
	 */
	@GetMapping(value = "/pois")
	public ResponseEntity<List<PointOfInterestDTO>> getPoiByBODUser(
			@RequestHeader(value = "X-User-Id", required = true) String userId,
			@PageableDefault(size = Integer.MAX_VALUE, page = 0) Pageable pageable) {
		List<PointOfInterestDTO> dtos = poiBODService.getPoiByBODUser(userId, pageable);
		return new ResponseEntity<List<PointOfInterestDTO>>(dtos, HttpStatus.OK);
	}

	/**
	 * Get basic report.
	 * @param userId
	 * @param pageable
	 * @return List of PoiBODOrderDTO
	 */
	@GetMapping(value = "/pois/basic-report")
	public ResponseEntity<List<PoiBODOrderDTO>> getPoiReportByBODUser(
			@RequestHeader(value = "X-User-Id", required = true) String userId,
			@PageableDefault(size = Integer.MAX_VALUE, page = 0) Pageable pageable) {
		List<PoiBODOrderDTO> poiBODOrders = poiBODService.getPoiBODOrders(userId, pageable);
		return new ResponseEntity<List<PoiBODOrderDTO>>(poiBODOrders, HttpStatus.OK);
	}

}
