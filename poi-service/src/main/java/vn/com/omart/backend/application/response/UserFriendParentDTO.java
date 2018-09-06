package vn.com.omart.backend.application.response;

import java.util.List;

public class UserFriendParentDTO {
	
	private int friendRequestCount;
	
	private List<UserFriendRequestDTO> friendRequests;
	
	private List<UserProfileDTO> friendSuggestions;
	
	public UserFriendParentDTO() {
		super();
	}

	public UserFriendParentDTO(int friendRequestCount, List<UserFriendRequestDTO> friendRequests) {
		super();
		this.friendRequestCount = friendRequestCount;
		this.friendRequests = friendRequests;
	}

	public int getFriendRequestCount() {
		return friendRequestCount;
	}

	public void setFriendRequestCount(int friendRequestCount) {
		this.friendRequestCount = friendRequestCount;
	}

	public List<UserFriendRequestDTO> getFriendRequests() {
		return friendRequests;
	}

	public void setFriendRequests(List<UserFriendRequestDTO> friendRequests) {
		this.friendRequests = friendRequests;
	}

	public List<UserProfileDTO> getFriendSuggestions() {
		return friendSuggestions;
	}

	public void setFriendSuggestions(List<UserProfileDTO> friendSuggestions) {
		this.friendSuggestions = friendSuggestions;
	}

}
