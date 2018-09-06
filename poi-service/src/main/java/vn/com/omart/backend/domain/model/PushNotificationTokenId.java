package vn.com.omart.backend.domain.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class PushNotificationTokenId implements Serializable {

	private static final long serialVersionUID = -4694335527775836426L;

	@Column(name = "user_id", columnDefinition = "varchar")
	private String userId;

	@Column(name = "token", columnDefinition = "varchar")
	private String token;
	
	public PushNotificationTokenId() {
		
	}

	public PushNotificationTokenId(String userId, String token) {
		super();
		this.userId = userId;
		this.token = token;
	}

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
	
}
