package vn.com.omart.driver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.com.omart.driver.dto.SettingDTO;
import vn.com.omart.driver.service.SettingService;

@RestController
@RequestMapping("/v1/driver/setting")
public class SettingController {

	@Autowired
	private SettingService settingService;

	/**
	 * Update or insert.
	 * 
	 * @param userId
	 * @param id
	 * @param dto
	 * @return HttpStatus
	 */
	@PutMapping(value = "")
	public ResponseEntity<Void> updateOrInsert(@RequestHeader(value = "X-User-Id", required = true) String userId,
			@RequestBody(required = true) SettingDTO dto) {
		settingService.updateOrInsert(userId, dto);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/*
	 * THIS FUNCTION ONLY FOR DEVELOPER. COPY DATA FROM DRIVER INFO INTO SETTING.
	 */
	@GetMapping(value = "/initial-data")
	public ResponseEntity<Void> copyDriverInfoIntoSetting() {
		settingService.copyDriverInfoIntoSetting();
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
