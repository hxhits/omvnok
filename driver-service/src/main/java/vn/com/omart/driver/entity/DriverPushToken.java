package vn.com.omart.driver.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "omart_driver_push_token")
public class DriverPushToken implements Serializable {

	private static final long serialVersionUID = 5801213705816223865L;

	@Id
	@Column(name = "user_id", columnDefinition = "varchar")
	private String userId;

	@Column(name = "token", columnDefinition = "varchar")
	private String token;

	@Column(name = "client", columnDefinition = "int")
	private int client;

	@Column(name="is_disabled",columnDefinition="bit")
	private boolean isDisabled;
	
	@Column(name = "ring_index", columnDefinition = "int", nullable = false)
	private int ringIndex;
	
	@Column(name = "is_blocked", columnDefinition = "bit")
	private boolean isBlocked;
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public int getClient() {
		return client;
	}

	public void setClient(int client) {
		this.client = client;
	}

	public boolean isDisabled() {
		return isDisabled;
	}

	public void setDisabled(boolean isDisabled) {
		this.isDisabled = isDisabled;
	}

	public int getRingIndex() {
		return ringIndex;
	}

	public void setRingIndex(int ringIndex) {
		this.ringIndex = ringIndex;
	}

	public boolean isBlocked() {
		return isBlocked;
	}

	public void setBlocked(boolean isBlocked) {
		this.isBlocked = isBlocked;
	}
	
}
