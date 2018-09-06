package vn.com.omart.driver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import vn.com.omart.driver.dto.DriverFollowDTO;
import vn.com.omart.driver.dto.response.EmptyJsonResponse;
import vn.com.omart.driver.service.DriverFollowService;

@RestController
@RequestMapping("/v1/driver/follow")
public class DriverFollowController {

	@Autowired
	private DriverFollowService driverFollowService;

	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping(value = "")
	public EmptyJsonResponse follow(@RequestHeader(value = "X-User-Id", required = false) String userId,
			@RequestBody(required = true) DriverFollowDTO dto) {
		driverFollowService.save(userId, dto);
		return new EmptyJsonResponse();
	}
}
