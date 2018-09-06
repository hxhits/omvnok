package vn.com.omart.backend.application.response;

import vn.com.omart.backend.domain.model.PushNotificationToken;

public class PushNotificationTokenDTO {

	private String userId;
	private String token;
	private int client;

	public static PushNotificationToken toEntity(Object[] objs) {
		PushNotificationToken entity = new PushNotificationToken();
		entity.setUserId(String.valueOf(objs[0]));
		entity.setToken(String.valueOf(objs[1]));
		entity.setClient(Integer.parseInt(String.valueOf(objs[2])));
		return entity;
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

	public int getClient() {
		return client;
	}

	public void setClient(int client) {
		this.client = client;
	}

}
