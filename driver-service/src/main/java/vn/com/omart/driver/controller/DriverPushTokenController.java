package vn.com.omart.driver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import vn.com.omart.driver.common.constant.DriverType;
import vn.com.omart.driver.dto.DriverPushTokenDTO;
import vn.com.omart.driver.dto.response.EmptyJsonResponse;
import vn.com.omart.driver.service.DriverPushTokenService;

@RestController
@RequestMapping("/v1/driver/push-token")
public class DriverPushTokenController {

	@Autowired
	private DriverPushTokenService driverPushTokenService;

	/**
	 * Update token.
	 * 
	 * @param userId
	 * @param client
	 * @param dto
	 * @return EmptyJsonResponse
	 */
	@PutMapping(value = "")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public EmptyJsonResponse save(@RequestHeader(value = "X-User-Id", required = true) String userId,
			@RequestHeader(value = "client", required = true) DriverType.Device client,
			@RequestBody(required = true) DriverPushTokenDTO dto) {
		driverPushTokenService.save(userId, client, dto);
		return new EmptyJsonResponse();
	}

	/**
	 * Delete token.
	 * 
	 * @param userId
	 * @return EmptyJsonResponse
	 */
	@DeleteMapping(value = "")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public EmptyJsonResponse delete(@RequestHeader(value = "X-User-Id", required = true) String userId) {
		driverPushTokenService.delete(userId);
		return new EmptyJsonResponse();
	}

	/**
	 * Update disable. User cannot receive notification.
	 * 
	 * @param userId
	 * @return HttpStatus
	 */
	@PutMapping(value = "/update/disable")
	public ResponseEntity<Void> updateIsDisable(@RequestHeader(value = "X-User-Id", required = true) String userId) {
		driverPushTokenService.updateIsDisable(userId);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	/**
	 * Update disable. User continue receive notification.
	 * 
	 * @param userId
	 * @return HttpStatus
	 */
	@PutMapping(value = "/update/enable")
	public ResponseEntity<Void> updateIsEnable(@RequestHeader(value = "X-User-Id", required = true) String userId) {
		driverPushTokenService.updateIsEnable(userId);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

}
