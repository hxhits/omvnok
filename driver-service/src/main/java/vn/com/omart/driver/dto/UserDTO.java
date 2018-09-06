package vn.com.omart.driver.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserDTO {

	private String id;
	@JsonIgnore
	private String firstname;
	private String avatar;
	private String phoneNumber;
	private String name;
	private String username;
	
	//private boolean activated;
	// private String lastname;
	// private String email;
	// private String type;
	// private String title;

	public UserDTO() {
		super();
		this.name = this.firstname;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
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

}
