package vn.com.omart.messenger.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import vn.com.omart.messenger.application.response.ContactDTO;

/**
 * Contact Service Interface.
 * 
 * @author Win10
 *
 */
public interface ContactService {

	/**
	 * Save contact.
	 * 
	 * @param userId
	 * @param contact
	 * @return List ContactDTO
	 */
	public List<ContactDTO> save(String userId, List<ContactDTO> contact);

	/**
	 * Get contacts
	 * 
	 * @param userId
	 * @param pageable
	 * @return List ContactDTO
	 */
	public List<ContactDTO> getContacts(String userId, Pageable pageable);

	/**
	 * Create channel.
	 * 
	 * @param sender
	 * @param recipient
	 * @return Channel name
	 */
	public String createChannel(String sender, String recipient);
}
