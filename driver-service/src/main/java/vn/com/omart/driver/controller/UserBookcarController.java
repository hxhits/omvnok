package vn.com.omart.driver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.com.omart.driver.dto.BookCarDTO;
import vn.com.omart.driver.service.DriverInfoService;
import vn.com.omart.driver.service.UserBookcarService;

@RestController
@RequestMapping("/v1/driver/user")
public class UserBookcarController {

	@Autowired
	private UserBookcarService userBookcarService;

	@Autowired
	private DriverInfoService driverInfoService;

	/**
	 * Create.
	 * 
	 * @param userId
	 * @param bookCarDTO
	 * @return BookCarDTO
	 */
	@PostMapping(value = "/bookcar")
	public ResponseEntity<BookCarDTO> create(@RequestHeader(value = "X-User-id", required = true) String userId,
			@RequestBody(required = true) BookCarDTO bookCarDTO) {
		BookCarDTO dto = userBookcarService.create(userId, bookCarDTO);
		return new ResponseEntity<BookCarDTO>(dto, HttpStatus.CREATED);
	}

	/**
	 * Rating.
	 * 
	 * @param userId
	 * @param driverId
	 * @param star
	 * @return HttpStatus
	 */
	@PutMapping(value = "/{driverId}/{star}/rating")
	public ResponseEntity<Void> rating(@RequestHeader(value = "X-User-id", required = true) String userId,
			@PathVariable(value = "driverId", required = true) String driverId,
			@PathVariable(value = "star", required = true) int star) {
		driverInfoService.rating(driverId, star);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	/**
	 * Ordering
	 * @param userId
	 * @param bookCarDTO
	 * @return HttpStatus
	 */
	@PostMapping(value = "/order/{user-id}")
	public ResponseEntity<Void> receiveOrdering(@PathVariable(value = "user-id", required = true) String userId,
			@RequestBody(required = true) BookCarDTO bookCarDTO) {
		userBookcarService.receiveOrdering(userId, bookCarDTO);
		return new ResponseEntity<Void>(HttpStatus.CREATED);
	}
	 
}
