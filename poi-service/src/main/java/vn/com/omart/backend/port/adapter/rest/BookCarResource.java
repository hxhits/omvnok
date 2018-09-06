package vn.com.omart.backend.port.adapter.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.com.omart.backend.application.BookCarService;
import vn.com.omart.backend.application.DriverLocationService;
import vn.com.omart.backend.application.response.BookCarDTO;
import vn.com.omart.backend.application.response.DriverLocationDTO;
import vn.com.omart.backend.domain.model.EmptyJsonResponse;

@RestController
@RequestMapping("/v1")
public class BookCarResource {

	@Autowired
	private BookCarService bookCarService;

	@Autowired
	private DriverLocationService driverLocationService;

	/**
	 * Get distance.
	 * 
	 * @param userId
	 * @param bookCarDTO
	 * @return BookCarDTO
	 */
	@RequestMapping(value = "/bookcar/distance", method = RequestMethod.POST)
	public ResponseEntity<BookCarDTO> getDistance(@RequestHeader(value = "X-User-id", required = true) String userId,
			@RequestBody(required = true) BookCarDTO bookCarDTO) {
		BookCarDTO dto = bookCarService.getDistance(bookCarDTO);
		return new ResponseEntity<BookCarDTO>(dto, HttpStatus.OK);
	}

	/**
	 * Insert a new record.
	 * 
	 * @param userId
	 * @param bookCarDTO
	 * @return EmptyJsonResponse
	 */
	@RequestMapping(value = "/bookcar", method = RequestMethod.POST)
	public ResponseEntity<BookCarDTO> insert(@RequestHeader(value = "X-User-id", required = true) String userId,
			@RequestBody(required = true) BookCarDTO bookCarDTO) {
		BookCarDTO dto = bookCarService.insert(userId, bookCarDTO);
		return new ResponseEntity<BookCarDTO>(dto, HttpStatus.CREATED);
	}

	/**
	 * Insert a new record.
	 * 
	 * @param userId
	 * @param bookCarDTO
	 * @return EmptyJsonResponse
	 */
	@RequestMapping(value = "/bookcar", method = RequestMethod.POST, headers = { "X-API-Version=1.1" })
	public ResponseEntity<BookCarDTO> insertV1(@RequestHeader(value = "X-User-id", required = true) String userId,
			@RequestBody(required = true) BookCarDTO bookCarDTO) {
		BookCarDTO dto = bookCarService.insertV1(userId, bookCarDTO);
		return new ResponseEntity<BookCarDTO>(dto, HttpStatus.CREATED);
	}

	/**
	 * Submit state.
	 * 
	 * @param userId
	 * @param id
	 * @param state
	 * @return EmptyJsonResponse
	 */
	@RequestMapping(value = "/bookcar/{id}", method = RequestMethod.PUT)
	public ResponseEntity<EmptyJsonResponse> bookCarSate(
			@RequestHeader(value = "X-User-id", required = true) String userId,
			@PathVariable(value = "id", required = true) Long id, @RequestBody(required = true) BookCarDTO dto) {
		bookCarService.submitState(id, dto);
		return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.OK);
	}

	/**
	 * Get detail by id.
	 * 
	 * @param userId
	 * @param id
	 * @return BookCarDTO
	 */
	@RequestMapping(value = "/bookcar/{id}/detail", method = RequestMethod.GET)
	public ResponseEntity<BookCarDTO> getById(@RequestHeader(value = "X-User-id", required = true) String userId,
			@PathVariable(value = "id", required = true) Long id) {
		BookCarDTO dto = bookCarService.getById(id);
		return new ResponseEntity<BookCarDTO>(dto, HttpStatus.OK);
	}

	/**
	 * Get state by id.
	 * 
	 * @param userId
	 * @param id
	 * @return state
	 */
	@RequestMapping(value = "/bookcar/{id}/state", method = RequestMethod.GET)
	public ResponseEntity<BookCarDTO> getStateById(@RequestHeader(value = "X-User-id", required = true) String userId,
			@PathVariable(value = "id", required = true) Long id) {
		BookCarDTO state = bookCarService.getStateById(id);
		return new ResponseEntity<BookCarDTO>(state, HttpStatus.OK);
	}

}
