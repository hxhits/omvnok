package vn.com.omart.messenger.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.com.omart.messenger.domain.model.EmptyJsonResponse;
import vn.com.omart.messenger.service.MessageChannelService;

/**
 * 
 * @author Win10
 *
 */
@RestController
@RequestMapping("v1/messenger/admin")
public class MessengerAdminController {

	@Autowired
	private MessageChannelService messageChannelService;

	/**
	 * Register a user on twilio server.
	 * 
	 * @param userId
	 * @return EmptyJsonResponse
	 */
	@RequestMapping(value = "/chat/{user-id}/register", method = RequestMethod.POST)
	public ResponseEntity<EmptyJsonResponse> createUserOnTwilio(
			@PathVariable(value = "user-id", required = true) String userId) {
		messageChannelService.createUserOnTwilio(userId);
		return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.CREATED);
	}
}
