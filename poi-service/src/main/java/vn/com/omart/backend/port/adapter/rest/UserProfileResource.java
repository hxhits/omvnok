package vn.com.omart.backend.port.adapter.rest;

import javax.validation.Valid;

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

import vn.com.omart.backend.application.UserProfileService;
import vn.com.omart.backend.application.response.UserDTO;
import vn.com.omart.backend.application.response.UserProfileDTO;

@RestController
@RequestMapping("/v1/user-profile")
public class UserProfileResource {

	@Autowired
	private UserProfileService userProfileService;

	/**
	 * Get user profile.
	 * 
	 * @param userId
	 * @param id
	 * @return UserProfileDTO
	 */
	@GetMapping(value = "/{userId}")
	public ResponseEntity<UserProfileDTO> getUserProfile(
			@RequestHeader(value = "X-User-Id", required = false, defaultValue = "") String userId,
			@PathVariable(value = "userId", required = true) String id) {
		UserProfileDTO dto = userProfileService.getProfile(userId, id);
		return new ResponseEntity<UserProfileDTO>(dto, HttpStatus.OK);
	}

	/**
	 * Save.
	 * 
	 * @param dto
	 * @return HttpStatus.
	 */
	@PostMapping(value = "")
	public ResponseEntity<Void> save(@Valid @RequestBody(required = true) UserProfileDTO dto) {
		userProfileService.save(dto);
		return new ResponseEntity<Void>(HttpStatus.CREATED);
	}

	/**
	 * Update
	 * 
	 * @param userId
	 * @param id
	 * @param dto
	 * @return
	 */
	@PutMapping(value = "/{id}")
	public ResponseEntity<Void> update(@RequestHeader(value = "X-User-Id", required = true) String userId,
			@PathVariable(value = "id", required = true) Long id, @RequestBody(required = true) UserProfileDTO dto) {
		userProfileService.update(id, dto);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	/**
	 * Create user profile. Be used from auth service.
	 * 
	 * @param userDTO
	 * @return HttpStatus
	 */
	@PostMapping(value = "/remote-create")
	public ResponseEntity<Void> remoteCreate(@RequestBody(required = true) UserDTO userDTO) {
		userProfileService.remoteCreate(userDTO);
		return new ResponseEntity<Void>(HttpStatus.CREATED);
	}
	
	/**
	 * searching by phone
	 * @param phone
	 * @return UserProfileDTO
	 */
	@GetMapping(value = "/phone/{phone}")
	public ResponseEntity<UserProfileDTO> searchByPhone(@PathVariable(value = "phone", required = true) String phone) {
		UserProfileDTO dto = userProfileService.searchByPhone(phone);
		return new ResponseEntity<UserProfileDTO>(dto, HttpStatus.OK);
	}

	/*
	 * THIS FUNCTION ONLY FOR DEVELOPER. COPY DATA FROM AUTH-USER INTO USER-PROFILE.
	 */
	@GetMapping(value = "/initial-data")
	public ResponseEntity<Void> get() {
		userProfileService.copyUserToUserProfile();
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
}
