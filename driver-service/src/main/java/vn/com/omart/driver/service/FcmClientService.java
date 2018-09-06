package vn.com.omart.driver.service;

import java.util.List;
import java.util.Map;

import vn.com.omart.driver.entity.DriverPushToken;

public interface FcmClientService {

	@Deprecated
	public void send_pending(String userId, String title, String body, Map<String, String> data,
			List<Object[]> categories);

	/**
	 * Notification with objects
	 * 
	 * @param userId
	 * @param title
	 * @param body
	 * @param data
	 * @param categories
	 */
	public void send(String userId, String title, String body, Map<String, String> data, List<Object[]> categories);

	/**
	 * Notification with entities
	 * 
	 * @param userId
	 * @param title
	 * @param body
	 * @param data
	 * @param tokens
	 */
	public void sendWithEntity(String userId, String title, String body, Map<String, String> data,
			List<DriverPushToken> tokens);

	/**
	 * Notification to an user.
	 * 
	 * @param token
	 * @param title
	 * @param body
	 * @param data
	 */
	public void send(DriverPushToken token, String title, String body, Map<String, String> data);

}
