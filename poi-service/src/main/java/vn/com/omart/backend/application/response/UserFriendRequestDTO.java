package vn.com.omart.backend.application.response;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import vn.com.omart.backend.constants.OmartType.UserFriendRequestState;
import vn.com.omart.backend.domain.model.UserFriend;
import vn.com.omart.backend.domain.model.UserFriendRequest;
import vn.com.omart.backend.domain.model.UserProfile;

public class UserFriendRequestDTO {
	
	private Long id;
	private UserFriendRequestState state;
	private UserProfileDTO user;
	private Long createdAt;
	private Long updatedAt;
	
	@JsonIgnore
	public static Set<String> userIds = new  HashSet<String>();
	
	public static UserFriendRequestDTO toBasicDTO(UserFriendRequest entity) {
		UserFriendRequestDTO dto = new UserFriendRequestDTO();
		dto.setId(entity.getId());
		dto.setUpdatedAt(entity.getUpdatedAt().getTime());
		UserProfileDTO userProfile = new UserProfileDTO();
		userProfile.setUserId(entity.getSender().getUserId());
		userProfile.setName(entity.getSender().getName());
		userProfile.setAvatar(entity.getSender().getAvatar());
		dto.setUser(userProfile);
		return dto;
	}
	
	public static UserFriendRequestDTO toBasicDTO(UserProfile user ,UserFriend entity) {
		UserFriendRequestDTO dto = new UserFriendRequestDTO();
		dto.setId(entity.getId());
		dto.setUpdatedAt(entity.getCreatedAt().getTime());
		UserProfileDTO userProfile = new UserProfileDTO();
		if(user.getId()!=entity.getUser().getId()) {
			userProfile.setUserId(entity.getUser().getUserId());
			userProfile.setName(entity.getUser().getName());
			userProfile.setAvatar(entity.getUser().getAvatar());
		}else {
			userProfile.setUserId(entity.getFriend().getUserId());
			userProfile.setName(entity.getFriend().getName());
			userProfile.setAvatar(entity.getFriend().getAvatar());
		}
		dto.setUser(userProfile);
		userIds.add(userProfile.getUserId());
		return dto;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UserFriendRequestState getState() {
		return state;
	}

	public void setState(UserFriendRequestState state) {
		this.state = state;
	}

	public Long getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Long createdAt) {
		this.createdAt = createdAt;
	}

	public Long getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Long updatedAt) {
		this.updatedAt = updatedAt;
	}

	public UserProfileDTO getUser() {
		return user;
	}

	public void setUser(UserProfileDTO user) {
		this.user = user;
	}

	public static Set<String> getUserIds() {
		return userIds;
	}

	public static void setUserIds(Set<String> userIds) {
		UserFriendRequestDTO.userIds = userIds;
	}

}
