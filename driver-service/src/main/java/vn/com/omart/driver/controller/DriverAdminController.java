package vn.com.omart.driver.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import vn.com.omart.driver.dto.DriverInfoDTO;
import vn.com.omart.driver.dto.NotificationContentDTO;
import vn.com.omart.driver.service.DriverAdminService;
import vn.com.omart.driver.service.DriverInfoService;

@RestController
@RequestMapping("/v1/driver/admin")
public class DriverAdminController {

	@Autowired
	private DriverInfoService driverInfoService;

	@Autowired
	private DriverAdminService driverAdminService;

	/**
	 * Lock/Unlock driver.
	 * 
	 * @param userId
	 * @param driver
	 * @param dto
	 * @return HttpStatus
	 */
	@PutMapping(value = "/{userId}")
	public ResponseEntity<Void> updateBlockDriver(@RequestHeader(value = "X-User-Id", required = true) String userId,
			@PathVariable(value = "userId", required = true) String driver,
			@RequestBody(required = true) DriverInfoDTO dto) {
		driverInfoService.updateBlockDriver(driver, dto);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	/**
	 * Get profile.
	 * 
	 * @param pageable
	 * @param userId
	 * @return List of DriverInfoDTO
	 */
	@GetMapping(value = "profile/all")
	public ResponseEntity<List<DriverInfoDTO>> getAllDriverInfo(
			@PageableDefault(size = Integer.MAX_VALUE, page = 0, direction = Direction.DESC, sort = "dateOfRegistration") Pageable pageable,
			@RequestHeader(value = "X-User-Id", required = true) String userId) {
		List<DriverInfoDTO> driverInfoDTOs = driverInfoService.getAllDriverInfo(pageable);
		return new ResponseEntity<List<DriverInfoDTO>>(driverInfoDTOs, HttpStatus.OK);
	}

	/**
	 * Omart admin send notification to driver.
	 * 
	 * @param dto
	 * @return HttpStatus
	 */
	@PostMapping(value = "/notification/send")
	public ResponseEntity<Void> notification(@RequestHeader(value = "X-User-Id", required = true) String userId,
			@RequestBody(required = true) NotificationContentDTO dto) {
		driverAdminService.notificationToDriver(dto);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	/**
	 * Search driver info base date of registration.
	 * 
	 * @param userId
	 * @param from
	 * @param to
	 * @param pageable
	 * @return List of DriverInfoDTO
	 */
	@RequestMapping(value = "search/info/dateOfRegistration", method = RequestMethod.GET)
	public ResponseEntity<List<DriverInfoDTO>> getShopBetweenDate(
			@RequestHeader(value = "X-User-Id", required = true) String userId,
			@RequestParam(value = "from", required = true) Long from,
			@RequestParam(value = "to", required = true) Long to,
			@PageableDefault(page = 0, size = Integer.MAX_VALUE) Pageable pageable) {
		List<DriverInfoDTO> dtos = driverInfoService.getByDateOfRegistrationAtBetween(from, to, pageable);
		return new ResponseEntity<List<DriverInfoDTO>>(dtos, HttpStatus.OK);
	}

	/**
	 * Search driver info by full name or phone.
	 * 
	 * @param userId
	 * @param text
	 * @param pageable
	 * @return List of DriverInfoDTO
	 */
	@RequestMapping(value = "search/info/fullname-phone", method = RequestMethod.GET)
	public ResponseEntity<List<DriverInfoDTO>> getByFullNameOrPhone(
			@RequestHeader(value = "X-User-Id", required = true) String userId,
			@RequestParam(value = "text", required = true) String text,
			@PageableDefault(page = 0, size = Integer.MAX_VALUE) Pageable pageable) {
		List<DriverInfoDTO> dtos = driverInfoService.getByFullNameOrPhone(text, pageable);
		return new ResponseEntity<List<DriverInfoDTO>>(dtos, HttpStatus.OK);
	}

	/**
	 * Update basic driver info
	 * 
	 * @param userId
	 * @param dto
	 * @return HttpStatus
	 */
	@PutMapping(value = "/update-info")
	public ResponseEntity<Void> updateDriverInfo(@RequestHeader(value = "X-User-Id", required = true) String userId,
			@RequestBody(required = true) DriverInfoDTO dto) {
		driverInfoService.basicUpdate(userId, dto);
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
