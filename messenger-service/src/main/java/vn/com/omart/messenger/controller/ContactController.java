package vn.com.omart.messenger.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.com.omart.messenger.application.request.ContactRequest;
import vn.com.omart.messenger.application.response.ChatDTO;
import vn.com.omart.messenger.application.response.ContactDTO;
import vn.com.omart.messenger.service.ContactService;

/**
 * Contact Controller.
 * 
 * @author Win10
 *
 */
@RestController
@RequestMapping("v1/messenger/contact")
public class ContactController {

	@Autowired
	private ContactService contactService;

	/**
	 * Save contact.
	 * 
	 * @param contact
	 * @param userId
	 * @return EmptyJsonResponse
	 */
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<List<ContactDTO>> save(@RequestBody(required = true) ContactRequest contacts,
			@RequestHeader(value = "X-User-Id", required = true) String userId) {
		List<ContactDTO> response = contactService.save(userId, contacts.getContacts());
		return new ResponseEntity<List<ContactDTO>>(response, HttpStatus.CREATED);
	}

	/**
	 * Get contact.
	 * 
	 * @param userId
	 * @return List of ContactDTO
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<List<ContactDTO>> getContact(
			@PageableDefault(size = Integer.MAX_VALUE, page = 0) Pageable pageable,
			@RequestHeader(value = "X-User-Id", required = true) String userId) {
		List<ContactDTO> response = contactService.getContacts(userId, pageable);
		return new ResponseEntity<List<ContactDTO>>(response, HttpStatus.OK);
	}

	/**
	 * Create channel.
	 * 
	 * @param sender
	 * @param recipient
	 * @return
	 */
	@RequestMapping(value = "/chat/{recipient}", method = RequestMethod.GET)
	public ResponseEntity<ChatDTO> createChannel(@RequestHeader(value = "X-User-Id", required = true) String sender,
			@PathVariable(value = "recipient", required = true) String recipient) {
		String channelName = contactService.createChannel(sender, recipient);
		ChatDTO chat = new ChatDTO();
		chat.setChannelName(channelName);
		return new ResponseEntity<ChatDTO>(chat, HttpStatus.OK);
	}
}
