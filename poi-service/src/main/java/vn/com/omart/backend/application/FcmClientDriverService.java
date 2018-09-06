package vn.com.omart.backend.application;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import de.bytefish.fcmjava.client.FcmClient;
import de.bytefish.fcmjava.model.enums.PriorityEnum;
import de.bytefish.fcmjava.model.options.FcmMessageOptions;
import de.bytefish.fcmjava.requests.data.DataMulticastMessage;
import de.bytefish.fcmjava.requests.notification.NotificationPayload;
import vn.com.omart.backend.constants.ConstantUtils;
import vn.com.omart.backend.constants.Device;
import vn.com.omart.backend.domain.model.DriverPushToken;
import vn.com.omart.backend.domain.model.PushNotificationToken;

@Service
public class FcmClientDriverService {

	@Qualifier("driver")
	@Autowired
	private FcmClient fcmClient;

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

	public void send(String userId, String title, String body, Map<String, String> data, List<Object[]> categories) {
		List<String> androidRegistrationIds = new ArrayList<>();
		List<String> iosRegistrationIds = new ArrayList<>();
		List<String> webRegistrationIds = new ArrayList<>();
		List<String> desktopRegistrationIds = new ArrayList<>();
		for (Object[] objs : categories) {
			if (!userId.equals(String.valueOf(objs[0]))) {
				int client = Integer.parseInt(String.valueOf(objs[2]));
				if (client == Device.android.getId()) {
					androidRegistrationIds.add(String.valueOf(objs[1]));
				} else if (client == Device.ios.getId()) {
					iosRegistrationIds.add(String.valueOf(objs[1]));
				} else if (client == Device.web.getId()) {
					webRegistrationIds.add(String.valueOf(objs[1]));
				} else if (client == Device.desktop.getId()) {
					desktopRegistrationIds.add(String.valueOf(objs[1]));
				}
			}
		}
		// push to android devices.
		if (!androidRegistrationIds.isEmpty()) {
			this.pushNotificationWithPayload(ConstantUtils.PUSH_TITLE, body, androidRegistrationIds, data);
		}
		// push to ios devices.
		if (!iosRegistrationIds.isEmpty()) {

			if (title.isEmpty()) {
				this.pushNotification(ConstantUtils.PUSH_TITLE, body, iosRegistrationIds, ConstantUtils.SOUND_DEFAULT,
						data);
			} else {
				this.pushNotification(title, body, iosRegistrationIds, ConstantUtils.SOUND_DEFAULT, data);
			}

		}
		// push to web browser.
		if (!webRegistrationIds.isEmpty()) {
			this.pushNotificationWithPayload(ConstantUtils.PUSH_TITLE, body, webRegistrationIds, data);
		}
		// push to desktop app.
		if (!desktopRegistrationIds.isEmpty()) {
			this.pushNotificationWithPayload(ConstantUtils.PUSH_TITLE, body, desktopRegistrationIds, data);
		}
	}

	/**
	 * Notification to an user.
	 * 
	 * @param token
	 * @param title
	 * @param body
	 * @param clickAction
	 * @param data
	 */
	public void send(DriverPushToken token, String title, String body, String clickAction, Map<String, String> data) {
		int client = token.getClient();
		if (client == Device.android.getId()) {
			this.pushNotificationWithPayload(title, body, Arrays.asList(token.getToken()), data);
		} else if (client == Device.ios.getId()) {
			if (title.isEmpty()) {
				this.pushNotification(ConstantUtils.PUSH_TITLE, body, Arrays.asList(token.getToken()),
						ConstantUtils.SOUND_DEFAULT, data);
			} else {
				this.pushNotification(title, body, Arrays.asList(token.getToken()), ConstantUtils.SOUND_DEFAULT, data);
			}
		} else if (client == Device.web.getId()) {
			this.pushNotificationWithoutPayload(title, body, clickAction, Arrays.asList(token.getToken()), data);
		}
	}
}
