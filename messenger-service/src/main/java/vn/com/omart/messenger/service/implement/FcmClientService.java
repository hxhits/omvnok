package vn.com.omart.messenger.service.implement;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.bytefish.fcmjava.client.FcmClient;
import de.bytefish.fcmjava.model.enums.PriorityEnum;
import de.bytefish.fcmjava.model.options.FcmMessageOptions;
import de.bytefish.fcmjava.requests.data.DataMulticastMessage;
import de.bytefish.fcmjava.requests.data.DataUnicastMessage;
import de.bytefish.fcmjava.requests.notification.NotificationPayload;

/**
 * Push Notification using FcmClient.
 * 
 * @author Win10
 *
 */
@Service
public class FcmClientService {

	@Autowired
	private FcmClient fcmClient;

	/**
	 * Push a notification.
	 * 
	 * @param title
	 * @param roomName
	 * @param token
	 * @param data
	 */
	public void pushNotification(String title, String body, String token, Map<String, String> data, String sound) {
		FcmMessageOptions options = FcmMessageOptions.builder().setTimeToLive(Duration.ofHours(1))
				.setPriorityEnum(PriorityEnum.High).setContentAvailable(true).build();
		DataUnicastMessage unicastMessage = new DataUnicastMessage(options, token, data,
				NotificationPayload.builder().setTitle(title).setBody(body).setSound(sound).build());
		fcmClient.send(unicastMessage);
	}

	/**
	 * Push a notification without payload.
	 * 
	 * @param title
	 * @param roomName
	 * @param token
	 * @param data
	 */
	public void pushNotificationWithoutPayload(String title, String body, String token, Map<String, String> data) {
		FcmMessageOptions options = FcmMessageOptions.builder().setTimeToLive(Duration.ofHours(1))
				.setPriorityEnum(PriorityEnum.High).setContentAvailable(true).build();
		DataUnicastMessage unicastMessage = new DataUnicastMessage(options, token, data);
		fcmClient.send(unicastMessage);
	}
	
	//=================enhance as below==================
	/**
	 * Push notification to ios.
	 *
	 * @param title
	 * @param body
	 * @param registrationIds
	 * @param sound
	 * @param data
	 */
	public void pushNotification(String title, String body, List<String> registrationIds, String sound,
			Map<String, String> data) {
		FcmMessageOptions options = FcmMessageOptions.builder().setTimeToLive(Duration.ofHours(1))
				.setPriorityEnum(PriorityEnum.High).setContentAvailable(true).build();
		fcmClient.send(new DataMulticastMessage(options, registrationIds, data,
				NotificationPayload.builder().setTitle(title).setBody(body).setSound(sound).build()));
	}

	/**
	 * Push notification to web.
	 *
	 * @param title
	 * @param body
	 * @param registrationIds
	 * @param data
	 */
	public void pushNotificationWithoutPayload(String title, String body, String clickAction,
			List<String> registrationIds, Map<String, String> data) {
		FcmMessageOptions options = FcmMessageOptions.builder().setTimeToLive(Duration.ofHours(1))
				.setPriorityEnum(PriorityEnum.High).setContentAvailable(true).build();
		fcmClient.send(new DataMulticastMessage(options, registrationIds, data,
				NotificationPayload.builder().setTitle(title).setBody(body).setClickAction(clickAction).build()));
	}

	/**
	 * Push notification to android.
	 *
	 * @param title
	 * @param body
	 * @param registrationIds
	 * @param data
	 */
	public void pushNotificationWithPayload(String title, String body, List<String> registrationIds,
			Map<String, String> data) {
		FcmMessageOptions options = FcmMessageOptions.builder().setTimeToLive(Duration.ofHours(1))
				.setPriorityEnum(PriorityEnum.High).setContentAvailable(true).build();
		fcmClient.send(new DataMulticastMessage(options, registrationIds, data));
	}

	
}
