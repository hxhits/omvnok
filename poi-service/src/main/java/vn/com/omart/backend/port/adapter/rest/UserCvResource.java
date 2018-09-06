package vn.com.omart.backend.port.adapter.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.com.omart.backend.application.UserCvService;
import vn.com.omart.backend.application.response.UserCvDTO;
import vn.com.omart.backend.domain.model.EmptyJsonResponse;

@RestController
@RequestMapping("/v1")
public class UserCvResource {

	@Autowired
	private UserCvService userCvService;

	/**
	 * Save CV.
	 * 
	 * @param userId
	 * @param UserCvDTO
	 * @return
	 */
	@RequestMapping(value = "/user/cv", method = RequestMethod.POST)
	public ResponseEntity<EmptyJsonResponse> save(@RequestHeader(value = "X-User-Id", required = true) String userId,
			@RequestBody(required = true) UserCvDTO userCvDTO) {
		userCvService.save(userCvDTO, userId);
		return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.CREATED);
	}

	/**
	 * Get CV.
	 * 
	 * @param userId
	 * @return UserCvDTO
	 */
	@RequestMapping(value = "/user/cv", method = RequestMethod.GET)
	public ResponseEntity<Object> getUserCV(@RequestHeader(value = "X-User-Id", required = true) String userId) {
		UserCvDTO dto = userCvService.getUserCV(userId);
		if (dto == null) {
			return new ResponseEntity<Object>(new EmptyJsonResponse(), HttpStatus.OK);
		}
		return new ResponseEntity<Object>(dto, HttpStatus.OK);
	}

}
