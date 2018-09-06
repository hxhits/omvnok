package vn.com.omart.backend.application.response;

public class UserDTO {
	private String userId;
	private String userName;
	private String avatar;
//------------------
	private String id;
    private String firstname;
    private String name;
    private String phoneNumber;
    private String username;
    private String password;
    
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	//------------
	public static UserDTO toDTO(Object[] obj) {
		UserDTO dto = new UserDTO();
		dto.setUserId(String.valueOf((obj[0])));
		dto.setUserName(String.valueOf(obj[1]));
		if (obj[3] != null) {
			dto.setAvatar(String.valueOf(obj[3]));
		}
		return dto;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
