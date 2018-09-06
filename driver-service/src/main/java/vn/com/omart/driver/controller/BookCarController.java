package vn.com.omart.driver.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import vn.com.omart.driver.dto.BookCarDTO;
import vn.com.omart.driver.dto.CallLogDTO;
import vn.com.omart.driver.dto.DriverFollowDTO;
import vn.com.omart.driver.entity.BookCar;
import vn.com.omart.driver.jsonview.BookCarView;
import vn.com.omart.driver.service.BookCarService;
import vn.com.omart.driver.service.CallLogService;
import vn.com.omart.driver.service.DriverLocationService;

@RestController
@RequestMapping("/v1/driver/bookcar")
public class BookCarController {

	@Autowired
	private BookCarService bookCarService;

	@Autowired
	private CallLogService callLogService;

	@Autowired
	private DriverLocationService driverLocationService;

	/**
	 * View detail.
	 * 
	 * @param id
	 * @param userId
	 * @return BookCar
	 */
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	@GetMapping(value = "/{id}")
	@JsonView({ BookCarView.DetailView.class })
	public BookCar getBookCarById(@PathVariable(required = true, value = "id") Long id,
			@RequestHeader(value = "X-User-Id", required = true) String userId) {
		BookCar bookcar = bookCarService.getBookCarById(id);
		return bookcar;
	}

	/**
	 * Get book car by driver following.
	 * 
	 * @param dto
	 * @param userId
	 * @param pageable
	 * @return
	 */
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/following")
	@JsonView({ BookCarView.OveralView.class })
	public List<BookCar> getBookCarByCarTypeAndProvinceAndDistrict(@RequestBody(required = true) DriverFollowDTO dto,
			@RequestHeader(value = "X-User-Id", required = true) String userId,
			@PageableDefault(size = Integer.MAX_VALUE, page = 0) Pageable pageable) {
		List<BookCar> bookcars = bookCarService.getBookCarByCarTypeAndProvinceAndDistrict(userId, dto, pageable);
		return bookcars;
	}

	/**
	 * Get book car state by id
	 * 
	 * @param id
	 * @return state
	 */
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	@GetMapping(value = "/{id}/state")
	public BookCarDTO getState(@RequestHeader(value = "X-User-Id", required = true) String userId,
			@PathVariable(required = true, value = "id") Long id) {
		BookCarDTO dto = bookCarService.getBookCarStateById(id);
		return dto;
	}

	/**
	 * Save Call Log.
	 * 
	 * @param userId
	 * @param location
	 * @param dto
	 * @return CallLogDTO
	 */
	@PostMapping(value = "/{id}/call-log")
	public ResponseEntity<CallLogDTO> driverCall(@RequestHeader(value = "X-User-Id", required = true) String userId,
			@RequestHeader(value = "X-Location-Geo", required = true) String location,
			@PathVariable(value = "id", required = true) Long id) {
		CallLogDTO dto = callLogService.save(userId, location, id);
		return new ResponseEntity<CallLogDTO>(dto, HttpStatus.CREATED);
	}

	/**
	 * Accept delivery.
	 * 
	 * @param userId
	 * @param dto
	 * @return HttpStatus
	 */
	@PutMapping(value = "/accept-delivery")
	public ResponseEntity<Void> acceptDelivery(@RequestHeader(value = "X-User-Id", required = true) String userId,
			@RequestBody(required = true) BookCarDTO dto) {
		bookCarService.acceptDelivery(userId, dto);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

}
