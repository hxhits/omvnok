package vn.com.omart.backend.port.adapter.rest;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.com.omart.backend.application.UserBODService;
import vn.com.omart.backend.application.response.UserBODDTO;
import vn.com.omart.backend.domain.model.EmptyJsonResponse;

@RestController
@RequestMapping("/v1/user/bod")
public class UserBODResource {

	@Autowired
	private UserBODService userBODService;

	/**
	 * signin
	 * 
	 * @param dto
	 * @return EmptyJsonResponse
	 */
	@PostMapping(value = "/signin")
	public ResponseEntity<EmptyJsonResponse> signIn(@Valid @RequestBody(required = true) UserBODDTO dto) {
		userBODService.save(dto);
		return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.CREATED);
	}

	/**
	 * login
	 * 
	 * @param userId
	 * @param dto
	 * @return UserBODDTO
	 */
	@PostMapping(value = "/login")
	public ResponseEntity<UserBODDTO> login(@RequestHeader(value = "X-User-Id", required = false) String userId,
			@RequestBody(required = true) UserBODDTO dto) {
		UserBODDTO user = userBODService.login(dto);
		if (user != null) {
			return new ResponseEntity<UserBODDTO>(user, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}

	/**
	 * Get all
	 * @param pageable
	 * @return List of UserBODDTO
	 */
	@GetMapping(value="/all")
	public ResponseEntity<List<UserBODDTO>> getAll(
			@PageableDefault(page = 0, size = Integer.MAX_VALUE, direction = Direction.DESC, sort = "createdAt") Pageable pageable) {
		return new ResponseEntity<List<UserBODDTO>>(userBODService.getAll(pageable), HttpStatus.OK);
	}
}
