package vn.com.omart.messenger.application.request;

import java.util.List;

import vn.com.omart.messenger.application.response.ContactDTO;

/**
 * DTO Request.
 * 
 * @author Win10
 *
 */
public class ContactRequest {
	public List<ContactDTO> contacts;

	public List<ContactDTO> getContacts() {
		return contacts;
	}

	public void setContacts(List<ContactDTO> contacts) {
		this.contacts = contacts;
	}
}
