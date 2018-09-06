package vn.com.omart.messenger.application.response;

public class UserDTO {

	private String id;

	private String firstname;

	private String lastname;

	private String avatar;

	private String phoneNumber;

	private String username;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public static UserDTO toDTO(Object[] obj) {
		UserDTO user = new UserDTO();
		if (obj != null) {
			if (obj[0] != null) {
				user.setId(String.valueOf(obj[0]));
			}
			if (obj[2] != null) {
				user.setFirstname(String.valueOf(obj[2]));
			}
			if (obj[3] != null) {
				user.setLastname(String.valueOf(obj[3]));
			}
			if (obj[4] != null) {
				user.setAvatar(String.valueOf(obj[4]));
			}
			if (obj[5] != null) {
				user.setPhoneNumber(String.valueOf(obj[5]));
			}
			user.setUsername(String.valueOf(obj[6]));
		}
		return user;
	}

}
