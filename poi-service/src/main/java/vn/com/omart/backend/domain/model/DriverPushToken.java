package vn.com.omart.backend.domain.model;

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
	
}
