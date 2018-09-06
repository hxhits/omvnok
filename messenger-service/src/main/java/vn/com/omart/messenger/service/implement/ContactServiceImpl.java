package vn.com.omart.messenger.service.implement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import vn.com.omart.messenger.application.response.ContactDTO;
import vn.com.omart.messenger.application.response.UserDTO;
import vn.com.omart.messenger.common.constant.Messenger;
import vn.com.omart.messenger.domain.model.Contact;
import vn.com.omart.messenger.domain.model.ContactRepository;
import vn.com.omart.messenger.domain.model.MessageChannel;
import vn.com.omart.messenger.domain.model.MessageChannelRepository;
import vn.com.omart.messenger.domain.model.MessengerHistoryRepository;
import vn.com.omart.messenger.service.ContactService;

@Service
public class ContactServiceImpl implements ContactService {

	@Autowired
	private MessageChannelRepository messageChannelRepository;

	@Autowired
	private ContactRepository contactRepository;

	@Autowired
	private MessengerHistoryRepository messengerHistoryRepository;

	/**
	 * Save Contact.
	 */
	@Override
	public List<ContactDTO> save(String userId, List<ContactDTO> contactDTO) {
		// delete data before create new.
		delete(userId);
		// create user name (phone number) list.
		List<String> userNameRequests = new ArrayList<String>();
		contactDTO.stream().forEach(contact -> {
			userNameRequests.add(contact.getPhoneNumber());
		});

		List<Contact> contacts = new ArrayList<Contact>();
		List<Object[]> userObjs = messengerHistoryRepository.getUserByUserNames(userNameRequests);
		List<String> userOmartMembers = new ArrayList<>();
		List<UserDTO> userDTOs = new ArrayList<>();

		userObjs.stream().forEach(userObj -> {
			UserDTO user = UserDTO.toDTO(userObj);
			// create a contact.
			Contact contact = setUserContact(userId, user);
			contacts.add(contact);
			// add user omart member.
			userOmartMembers.add(user.getUsername());
			userDTOs.add(user);
		});

		// filter user not omart member.
		userNameRequests.removeAll(userOmartMembers);

		// save omart not member.
		userNameRequests.stream().forEach(userName -> {
			ContactDTO contact = contactDTO.stream()
					.filter(predicate -> predicate.getPhoneNumber().equalsIgnoreCase(userName)).findAny().orElse(null);
			Contact contactEntity = new Contact();
			contactEntity.setUserId(userId);
			contactEntity.setName(vn.com.omart.sharedkernel.StringUtils.emoijNormalize(contact.getName()));
			contactEntity.setPhoneNumber(contact.getPhoneNumber());
			contactEntity.setType(Messenger.OMART_NOT_MEMBER);
			contactEntity.setCreatedAt(new Date());
			contacts.add(contactEntity);
		});
		try {
			contactRepository.save(contacts);
		} catch(Exception e) {
			
		}
		// get contact list.
		// List<ContactDTO> contactResponse = getContacts(userId, userDTOs);
		List<ContactDTO> contactResponse = getContacts(userId, new PageRequest(0, 2000));
		return contactResponse;
	}
	
	/**
	 * Set User Contact.
	 * 
	 * @param userId
	 * @param user
	 * @return Contact
	 */
	public Contact setUserContact(String userId, UserDTO user) {
		Contact contact = new Contact();
		contact.setUserId(userId);
		contact.setOmartId(user.getId());
		contact.setName(user.getFirstname());
		contact.setPhoneNumber(user.getPhoneNumber());
		contact.setType(Messenger.OMART_MEMBER);
		contact.setCreatedAt(new Date());
		return contact;
	}

	/**
	 * Delete.
	 * 
	 * @param userId
	 */
	public void delete(String userId) {
		List<Contact> contacts = contactRepository.findByUserIdOrderByNameAsc(userId);
		if (!contacts.isEmpty()) {
			contactRepository.delete(contacts);
		}
	}

	/**
	 * Get contacts.
	 */
	@Override
	public List<ContactDTO> getContacts(String userId, Pageable pageable) {
		//init 
		List<ContactDTO> result = new ArrayList<>();
		// get omart member.
		List<Object[]> contactObj = contactRepository.getContactWithUserIdAndType(userId, Messenger.OMART_MEMBER);
		List<ContactDTO> members = contactObj.stream().map(ContactDTO::objectToDTO).collect(Collectors.toList());
		// get omart not member.
		List<Contact> contacts = contactRepository.findByUserIdAndType(userId, Messenger.OMART_NOT_MEMBER);
		List<ContactDTO> noMembers = contacts.stream().map(ContactDTO::toDTO).collect(Collectors.toList());
		// merge member and not member.
		List<ContactDTO> contactResponse = Stream.concat(members.stream(), noMembers.stream())
				.collect(Collectors.toList());
		// sorting.
		List<ContactDTO> contactResponseSorted = contactResponse.stream().collect(Collectors.toList());
		// pagination
		int pageNumber = pageable.getPageNumber();
		int pageSize = pageable.getPageSize();
		int index = (pageNumber * pageSize);
		ContactDTO.sortByName(contactResponseSorted);
		// begin if becomed friend do not return in contact. 
		try {
			if(!contactResponseSorted.isEmpty()) {
				Long id = Long.valueOf(contactRepository.getIdByUserId(userId));
				List<Object[]> friends = contactRepository.getFriendById(id);
				if(!friends.isEmpty()) {
					String str = friends.get(0)[0] + "," +friends.get(0)[1];
					String [] strs = str.split(",");
					Set<String> mySet = new HashSet<String>(Arrays.asList(strs));
					List<String> userIds = new ArrayList<>();
					userIds.addAll(mySet);
					List<Object[]> userIdObjs = contactRepository.getUserIdInIds(userIds);
					
					for(ContactDTO e : contactResponseSorted) {
						if(e.getOmartId()!=null) {
							Object o =(Object) e.getOmartId();
							if(!userIdObjs.contains(o)) {
								result.add(e);
							}
						}else {
							result.add(e);
						}
					}
					return result.stream().skip(index).limit(pageSize).collect(Collectors.toList());
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		// end
		return contactResponseSorted.stream().skip(index).limit(pageSize).collect(Collectors.toList());
	}

	/**
	 * Get contacts.
	 * 
	 * @param userId
	 * @param userDTOs
	 * @return List of ContactDTO.
	 */
	public List<ContactDTO> getContacts(String userId, List<UserDTO> userDTOs) {
		List<Contact> contactList = contactRepository.findByUserIdOrderByNameAsc(userId);
		List<ContactDTO> contactResponse = contactList.stream().map(ContactDTO::toDTO).collect(Collectors.toList());
		userDTOs.stream().forEach(member -> {
			contactResponse.stream().forEach(contact -> {
				if (contact.getPhoneNumber().equalsIgnoreCase(member.getUsername())) {
					contact.setAvatar(member.getAvatar());
				}
			});
		});
		return contactResponse;
	}

	/**
	 * Create channel.
	 */
	@Override
	public String createChannel(String sender, String recipient) {
		// 0L is user and user
		MessageChannel channel = messageChannelRepository.findBySenderIdAndRecipientIdAndPoiId(sender, recipient, 0L);
		channel = (channel == null)
				? messageChannelRepository.findBySenderIdAndRecipientIdAndPoiId(recipient, sender, 0L)
				: channel;
		if (channel == null) {
			return (sender + "-" + recipient + "-" + 0);
		} else {
			return channel.getChannelName();
		}
	}

}
