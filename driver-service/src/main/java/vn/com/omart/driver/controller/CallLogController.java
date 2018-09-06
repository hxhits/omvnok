package vn.com.omart.driver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.com.omart.driver.dto.CallLogDTO;
import vn.com.omart.driver.service.CallLogService;

@RestController
@RequestMapping("/v1/driver/call-log")
public class CallLogController {

	@Autowired
	private CallLogService callLogService;

	/**
	 * Get call log.
	 * 
	 * @param userId
	 * @param bookcarId
	 * @return CallLogDTO
	 */
	@GetMapping(value = "/{id}")
	public ResponseEntity<CallLogDTO> getByBookcarOrderByCreatedAtDesc(
			@RequestHeader(value = "X-User-Id", required = true) String userId,
			@PathVariable(required = true, value = "id") Long bookcarId) {
		CallLogDTO entity = callLogService.getByBookcarOrderByCreatedAtDesc(bookcarId);
		if (entity != null) {
			return new ResponseEntity<CallLogDTO>(entity, HttpStatus.OK);
		}
		return new ResponseEntity<CallLogDTO>(HttpStatus.NOT_FOUND);
	}

	/**
	 * reject
	 * 
	 * @param dto
	 * @return Void
	 */
	@PutMapping(value = "/reject")
	public ResponseEntity<Void> reject(@RequestHeader(value = "X-User-Id", required = true) String userId,
			@RequestBody(required = true) CallLogDTO dto) {
		callLogService.reject(dto);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * approve
	 * 
	 * @param userId
	 * @param dto
	 * @return
	 */
	@PutMapping(value = "/approve")
	public ResponseEntity<Void> approve(@RequestHeader(value = "X-User-Id", required = true) String userId,
			@RequestBody(required = true) CallLogDTO dto) {
		callLogService.approve(dto);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
