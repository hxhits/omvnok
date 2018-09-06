package vn.com.omart.backend.port.adapter.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import vn.com.omart.backend.application.UserFriendRequestService;
import vn.com.omart.backend.application.response.UserFriendParentDTO;
import vn.com.omart.backend.application.response.UserFriendRequestDTO;
import vn.com.omart.backend.constants.OmartType.UserFriendRequestState;
import vn.com.omart.backend.domain.model.EmptyJsonResponse;
import vn.com.omart.backend.domain.model.UserFriend;
import vn.com.omart.backend.domain.model.UserFriendRepository;

@RestController
@RequestMapping("/v1/user/friend")
public class UserFriendRequestResource {

	@Autowired
	private UserFriendRequestService userFriendRequestService;

	/**
	 * Request friend.
	 * 
	 * @param userId
	 * @param recipient
	 * @return EmptyJsonResponse
	 */
	@PostMapping(value = "/request/{recipient}")
	public ResponseEntity<EmptyJsonResponse> sendRequest(
			@RequestHeader(value = "X-User-Id", required = true) String userId,
			@PathVariable(value = "recipient", required = true) String recipient) {
		userFriendRequestService.sendRequest(userId, recipient);
		return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.CREATED);
	}

	/**
	 * Action user {ACCEPT,REJECT,UNFRIEND}
	 * 
	 * @param to
	 * @param userId
	 * @param action
	 * @return EmptyJsonResponse
	 */
	@PutMapping(value = "/{to}")
	public ResponseEntity<EmptyJsonResponse> actionUser(@PathVariable(value = "to", required = true) String to,
			@RequestHeader(value = "X-User-Id", required = true) String userId,
			@RequestParam(value = "action", required = true) UserFriendRequestState action) {
		userFriendRequestService.actionUser(userId, to, action);
		return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.OK);
	}

	/**
	 * Get request friends.
	 * 
	 * @param userId
	 * @param pageable
	 * @return List of UserFriendRequestDTO
	 */
	@GetMapping(value = "/requests")
	public ResponseEntity<List<UserFriendRequestDTO>> getRequestFriends(
			@RequestHeader(value = "X-User-Id", required = true) String userId,
			@PageableDefault(page = 0, size = Integer.MAX_VALUE) Pageable pageable) {
		return new ResponseEntity<List<UserFriendRequestDTO>>(
				userFriendRequestService.getRequestFriends(userId, pageable), HttpStatus.OK);
	}

	/**
	 * Get friends.
	 * 
	 * @param userId
	 * @param pageable
	 * @return UserFriendParentDTO
	 */
	@GetMapping(value = "")
	public ResponseEntity<UserFriendParentDTO> getFriends(
			@RequestHeader(value = "X-User-Id", required = true) String userId,
			@PageableDefault(page = 0, size = Integer.MAX_VALUE) Pageable pageable) {
		return new ResponseEntity<UserFriendParentDTO>(userFriendRequestService.getFriends(userId, pageable),
				HttpStatus.OK);
	}
	
//	@Autowired
//	private UserFriendRepository userFriendRepository;
//	
//	@GetMapping(value = "/name/{name}")
//	public ResponseEntity<List<Object[]>> searchs(
//			@PathVariable(value = "name", required = true) String name,
//			@RequestHeader(value = "X-User-Id", required = true) String userId,
//			@PageableDefault(page = 0, size = Integer.MAX_VALUE) Pageable pageable) {
//		String []ids = {"d00cc474a9d3460883eb4b35f4cb654c","9dd0cdc4ca6042dfb3f4e8841505b0e2"};
//		
//		
//		if(!rs.isEmpty()) {
//			return new ResponseEntity<List<Object[]>>(rs,HttpStatus.OK);
//		}
//		return new ResponseEntity<List<Object[]>>(new ArrayList<>(),HttpStatus.OK);
//	}
	
	
}
