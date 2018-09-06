package vn.com.omart.backend.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.com.omart.backend.application.response.UserFriendParentDTO;
import vn.com.omart.backend.application.response.UserFriendRequestDTO;
import vn.com.omart.backend.application.response.UserProfileDTO;
import vn.com.omart.backend.application.util.DateUtils;
import vn.com.omart.backend.constants.OmartType.Commons;
import vn.com.omart.backend.constants.OmartType.UserFriendRequestState;
import vn.com.omart.backend.domain.model.UserFriend;
import vn.com.omart.backend.domain.model.UserFriendRepository;
import vn.com.omart.backend.domain.model.UserFriendRequest;
import vn.com.omart.backend.domain.model.UserFriendRequestRepository;
import vn.com.omart.backend.domain.model.UserProfile;
import vn.com.omart.backend.domain.model.UserProfileRepository;

@Service
public class UserFriendRequestService {

	@Autowired
	private UserFriendRequestRepository userFriendRequestRepository;

	@Autowired
	private UserProfileRepository userProfileRepository;

	@Autowired
	private UserFriendRepository userFriendRepository;

	@Autowired
	private PoiNotificationService poiNotificationService;

	/**
	 * Request friend.
	 * 
	 * @param from
	 * @param to
	 */
	public void sendRequest(String from, String to) {
		UserProfile recipient = userProfileRepository.findByUserId(to);
		UserProfile sender = userProfileRepository.findByUserId(from);
		if (recipient != null && sender != null) {
			UserFriendRequest entity = userFriendRequestRepository.getByRecipientAndSenderOrElse(recipient, sender);
			if (entity == null) {
				entity = new UserFriendRequest();
			}
			entity.setSender(sender);
			entity.setRecipient(recipient);
			entity.setState(UserFriendRequestState.WAIT.getId());
			entity.setCreatedAt(DateUtils.getCurrentDate());
			entity.setUpdatedAt(DateUtils.getCurrentDate());
			UserFriendRequest result = userFriendRequestRepository.save(entity);
			if (result != null) {
				poiNotificationService.sendFriendRequest(result);
			}
		}
	}

	/**
	 * Action user {ACCEPT,REJECT,UNFRIEND}
	 * 
	 * @param from
	 * @param to
	 * @param state
	 */
	@Transactional(readOnly = false)
	public void actionUser(String from, String to, UserFriendRequestState state) {
		UserProfile recipient = userProfileRepository.findByUserId(to);
		UserProfile sender = userProfileRepository.findByUserId(from);
		if (recipient != null && sender != null) {
			UserFriendRequest entity = userFriendRequestRepository.getByRecipientAndSenderOrElse(recipient, sender);
			if (entity != null) {
				entity.setState(state.getId());
				entity.setUpdatedAt(DateUtils.getCurrentDate());
				userFriendRequestRepository.save(entity);
				UserFriend userFriend = null;
				switch (state) {
				case ACCEPT:
					userFriend = userFriendRepository.getByUserAndFriendOrElse(sender, recipient);
					if (userFriend == null) {
						userFriend = new UserFriend(sender, recipient, DateUtils.getCurrentDate());
					} else {
						userFriend.setUser(sender);
						userFriend.setFriend(recipient);
						userFriend.setCreatedAt(DateUtils.getCurrentDate());
					}
					UserFriend resultAcpt = userFriendRepository.save(userFriend);
					if (resultAcpt != null) {
						poiNotificationService.sendFriendAccept(entity);
					}
					break;
				case UNFRIEND:
					userFriend = userFriendRepository.getByUserAndFriendOrElse(sender, recipient);
					if (userFriend != null) {
						userFriendRepository.delete(userFriend);
					}
					break;
				default:
					break;
				}
			}
		}
	}

	/**
	 * Get request friends.
	 * 
	 * @param userId
	 * @param pageable
	 * @return List of UserFriendRequestDTO
	 */
	public List<UserFriendRequestDTO> getRequestFriends(String userId, Pageable pageable) {
		UserProfile recipient = userProfileRepository.findByUserId(userId);
		if (recipient != null) {
			List<UserFriendRequest> entities = userFriendRequestRepository.findByRecipientAndStateOrderByUpdatedAtDesc(
					recipient, UserFriendRequestState.WAIT.getId(), pageable);
			List<UserFriendRequestDTO> result = entities.stream().map(UserFriendRequestDTO::toBasicDTO)
					.collect(Collectors.toList());
			return result;
		}
		return new ArrayList<>();
	}

	/**
	 * Get friends.
	 * 
	 * @param userId
	 * @param pageable
	 * @return List of UserFriendRequestDTO
	 */
	public UserFriendParentDTO getFriends(String userId, Pageable pageable) {
		UserProfile user = userProfileRepository.findByUserId(userId);
		if (user != null) {
			List<UserFriend> entities = userFriendRepository.findByUserOrFriendOrderByCreatedAtDesc(user, user,
					pageable);
			List<UserFriendRequestDTO> friendReqs = entities.stream()
					.map(item -> UserFriendRequestDTO.toBasicDTO(user, item)).collect(Collectors.toList());
			int friendReqCount = userFriendRequestRepository.countByRecipientAndState(user,
					UserFriendRequestState.WAIT.getId());
			UserFriendParentDTO result = new UserFriendParentDTO(friendReqCount, friendReqs);
			// friend suggestion
//			Set<String> userIdSet = UserFriendRequestDTO.getUserIds();
//			result.setFriendSuggestions(this.getFriendSuggestions(userId, userIdSet));
//			UserFriendRequestDTO.getUserIds().clear();
			return result;
		}
		return new UserFriendParentDTO();
	}

	/**
	 * Friend suggestions.
	 * 
	 * @param userId
	 * @param userIds
	 * @return List of UserProfileDTO
	 */
	private List<UserProfileDTO> getFriendSuggestions(String userId, Set<String> userIds) {
		// init.
		List<String> userIdStr = userIds.stream().collect(Collectors.toList());

		// messenger.
		List<Object[]> userObjects = userFriendRepository.getUserCommunicateOften(userId);
		List<String> userMessagers = this.getUserId(userObjects);

		// contact.
		List<String> userOmartContacts = userFriendRepository.getUserInContact(userId);

		// concat.
		List<String> result = Stream.concat(userMessagers.stream(), userOmartContacts.stream())
				.collect(Collectors.toList());
		if (!result.isEmpty()) {
			// unique user.
			boolean isResultChanged = result.removeAll(userIdStr);
			if(!result.isEmpty()) {
				// query.
				List<UserProfile> userProfiles = userProfileRepository.getUserInUserIds(result);

				// DTO.
				List<UserProfileDTO> friendSuggestions = userProfiles.stream().map(item -> {
					UserProfileDTO dto = new UserProfileDTO();
					dto.setAvatar(item.getAvatar());
					dto.setName(item.getName());
					dto.setUserId(item.getUserId());
					return dto;
				}).collect(Collectors.toList());
				return friendSuggestions;
			}
		}
		return new ArrayList<>();
	}

	/**
	 * Get friend state.
	 * 
	 * @param userId
	 * @param friendId
	 * @return UserFriendRequestState
	 */
	public Commons getState(String userId, String friendId) {
		UserProfile sender = userProfileRepository.findByUserId(userId);
		UserProfile recipient = userProfileRepository.findByUserId(friendId);
		Commons result = Commons.NONE;
		if (recipient != null && sender != null) {
			UserFriendRequest entity = userFriendRequestRepository.getByRecipientAndSenderOrElse(recipient, sender);
			if (entity != null) {
				if (entity.getState() == UserFriendRequestState.ACCEPT.getId()) {
					result = Commons.FRIEND;
				} else if (entity.getState() == UserFriendRequestState.WAIT.getId()) {
					result = Commons.WAIT;
				}
			}
		}
		return result;
	}

	/**
	 * Get List of user id.
	 * 
	 * @param commuReport
	 * @return List of String
	 */
	public List<String> getUserId(List<Object[]> commuReport) {
		List<String> userIds = new ArrayList<>();
		for (Object[] objs : commuReport) {
			String userId = String.valueOf(objs[0]);
			userIds.add(userId);
		}
		return userIds;
	}

}
