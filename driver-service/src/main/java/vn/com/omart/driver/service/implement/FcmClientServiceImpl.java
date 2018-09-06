package vn.com.omart.driver.service.implement;

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
import vn.com.omart.driver.common.constant.CommonConstant;
import vn.com.omart.driver.common.constant.DriverType.Device;
import vn.com.omart.driver.entity.DriverPushToken;
import vn.com.omart.driver.repository.SettingRepository;
import vn.com.omart.driver.service.FcmClientService;

@Service
public class FcmClientServiceImpl implements FcmClientService {

	@Qualifier("driver")
	@Autowired
	private FcmClient fcmClient;

	private SettingRepository settingRepository;

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

	/**
	 * PENDING
	 * 
	 * @param userId
	 * @param title
	 * @param body
	 * @param data
	 * @param categories
	 */
	@Override
	public void send_pending(String userId, String title, String body, Map<String, String> data,
			List<Object[]> categories) {
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
			data.put("sound", "1");
			this.pushNotificationWithPayload(CommonConstant.PUSH_TITLE, body, androidRegistrationIds, data);
		}
		// push to ios devices.
		if (!iosRegistrationIds.isEmpty()) {
			if (title.isEmpty()) {
				this.pushNotification(CommonConstant.PUSH_TITLE, body, iosRegistrationIds, CommonConstant.SOUND_DEFAULT,
						data);
			} else {
				this.pushNotification(title, body, iosRegistrationIds, CommonConstant.SOUND_DEFAULT, data);
			}
		}
		// push to web browser.
		if (!webRegistrationIds.isEmpty()) {
			this.pushNotificationWithPayload(CommonConstant.PUSH_TITLE, body, webRegistrationIds, data);
		}
		// push to desktop app.
		if (!desktopRegistrationIds.isEmpty()) {
			this.pushNotificationWithPayload(CommonConstant.PUSH_TITLE, body, desktopRegistrationIds, data);
		}
	}

	/**
	 * send notification.
	 */
	@Override
	public void send(String userId, String title, String body, Map<String, String> data, List<Object[]> categories) {
		try {
			List<String> androidRegistrationIds = new ArrayList<>();

			List<String> iosRegistrationIds0 = new ArrayList<>();
			List<String> iosRegistrationIds1 = new ArrayList<>();
			List<String> iosRegistrationIds2 = new ArrayList<>();
			List<String> iosRegistrationIds3 = new ArrayList<>();
			List<String> iosRegistrationIds4 = new ArrayList<>();
			List<String> iosRegistrationIds5 = new ArrayList<>();
			List<String> iosRegistrationIds6 = new ArrayList<>();
			List<String> iosRegistrationIds7 = new ArrayList<>();
			List<String> iosRegistrationIds8 = new ArrayList<>();
			List<String> iosRegistrationIds9 = new ArrayList<>();
			List<String> iosRegistrationIds10 = new ArrayList<>();
			List<String> iosRegistrationIds11 = new ArrayList<>();
			List<String> iosRegistrationIds12 = new ArrayList<>();
			List<String> iosRegistrationIds13 = new ArrayList<>();
			List<String> iosRegistrationIds14 = new ArrayList<>();
			List<String> iosRegistrationIds15 = new ArrayList<>();
			List<String> iosRegistrationIds16 = new ArrayList<>();
			List<String> iosRegistrationIds17 = new ArrayList<>();
			List<String> iosRegistrationIds18 = new ArrayList<>();
			List<String> iosRegistrationIds19 = new ArrayList<>();

			List<String> webRegistrationIds = new ArrayList<>();
			List<String> desktopRegistrationIds = new ArrayList<>();
			for (Object[] objs : categories) {
				if (!userId.equals(String.valueOf(objs[0]))) {
					int client = Integer.parseInt(String.valueOf(objs[2]));
					if (client == Device.android.getId()) {
						androidRegistrationIds.add(String.valueOf(objs[1]));
					} else if (client == Device.ios.getId()) {
						// begin: filter by sound.
						switch (Integer.parseInt(String.valueOf(objs[4]))) {
						case 0:
							iosRegistrationIds0.add(String.valueOf(objs[1]));
							break;
						case 1:
							iosRegistrationIds1.add(String.valueOf(objs[1]));
							break;
						case 2:
							iosRegistrationIds2.add(String.valueOf(objs[1]));
							break;
						case 3:
							iosRegistrationIds3.add(String.valueOf(objs[1]));
							break;
						case 4:
							iosRegistrationIds4.add(String.valueOf(objs[1]));
							break;
						case 5:
							iosRegistrationIds5.add(String.valueOf(objs[1]));
							break;
						case 6:
							iosRegistrationIds6.add(String.valueOf(objs[1]));
							break;
						case 7:
							iosRegistrationIds7.add(String.valueOf(objs[1]));
							break;
						case 8:
							iosRegistrationIds8.add(String.valueOf(objs[1]));
							break;
						case 9:
							iosRegistrationIds9.add(String.valueOf(objs[1]));
							break;
						case 10:
							iosRegistrationIds10.add(String.valueOf(objs[1]));
							break;
						case 11:
							iosRegistrationIds11.add(String.valueOf(objs[1]));
							break;
						case 12:
							iosRegistrationIds12.add(String.valueOf(objs[1]));
							break;
						case 13:
							iosRegistrationIds13.add(String.valueOf(objs[1]));
							break;
						case 14:
							iosRegistrationIds14.add(String.valueOf(objs[1]));
							break;
						case 15:
							iosRegistrationIds15.add(String.valueOf(objs[1]));
							break;
						case 16:
							iosRegistrationIds16.add(String.valueOf(objs[1]));
							break;
						case 17:
							iosRegistrationIds17.add(String.valueOf(objs[1]));
							break;
						case 18:
							iosRegistrationIds18.add(String.valueOf(objs[1]));
							break;
						case 19:
							iosRegistrationIds19.add(String.valueOf(objs[1]));
							break;
						default:
							break;
						}
						// end.
					} else if (client == Device.web.getId()) {
						webRegistrationIds.add(String.valueOf(objs[1]));
					} else if (client == Device.desktop.getId()) {
						desktopRegistrationIds.add(String.valueOf(objs[1]));
					}
				}
			}
			// push to android devices.
			if (!androidRegistrationIds.isEmpty()) {
				this.pushNotificationWithPayload(CommonConstant.PUSH_TITLE, body, androidRegistrationIds, data);
			}
			// push to ios devices.
			pushNotificationWithSound(title, body, iosRegistrationIds0, data, 0);
			pushNotificationWithSound(title, body, iosRegistrationIds1, data, 1);
			pushNotificationWithSound(title, body, iosRegistrationIds2, data, 2);
			pushNotificationWithSound(title, body, iosRegistrationIds3, data, 3);
			pushNotificationWithSound(title, body, iosRegistrationIds4, data, 4);
			pushNotificationWithSound(title, body, iosRegistrationIds5, data, 5);
			pushNotificationWithSound(title, body, iosRegistrationIds6, data, 6);
			pushNotificationWithSound(title, body, iosRegistrationIds7, data, 7);
			pushNotificationWithSound(title, body, iosRegistrationIds8, data, 8);
			pushNotificationWithSound(title, body, iosRegistrationIds9, data, 9);
			pushNotificationWithSound(title, body, iosRegistrationIds10, data, 10);
			pushNotificationWithSound(title, body, iosRegistrationIds11, data, 11);
			pushNotificationWithSound(title, body, iosRegistrationIds12, data, 12);
			pushNotificationWithSound(title, body, iosRegistrationIds13, data, 13);
			pushNotificationWithSound(title, body, iosRegistrationIds14, data, 14);
			pushNotificationWithSound(title, body, iosRegistrationIds15, data, 15);
			pushNotificationWithSound(title, body, iosRegistrationIds16, data, 16);
			pushNotificationWithSound(title, body, iosRegistrationIds17, data, 17);
			pushNotificationWithSound(title, body, iosRegistrationIds18, data, 18);
			pushNotificationWithSound(title, body, iosRegistrationIds19, data, 19);
			// push to web browser.
			if (!webRegistrationIds.isEmpty()) {
				this.pushNotificationWithPayload(CommonConstant.PUSH_TITLE, body, webRegistrationIds, data);
			}
			// push to desktop app.
			if (!desktopRegistrationIds.isEmpty()) {
				this.pushNotificationWithPayload(CommonConstant.PUSH_TITLE, body, desktopRegistrationIds, data);
			}
		} catch (Exception e) {
			System.out.println("\n Error: " + e.getMessage());
		}
	}
	
	@Override
	public void sendWithEntity(String userId, String title, String body, Map<String, String> data, List<DriverPushToken> tokens) {
		try {
			List<String> androidRegistrationIds = new ArrayList<>();

			List<String> iosRegistrationIds0 = new ArrayList<>();
			List<String> iosRegistrationIds1 = new ArrayList<>();
			List<String> iosRegistrationIds2 = new ArrayList<>();
			List<String> iosRegistrationIds3 = new ArrayList<>();
			List<String> iosRegistrationIds4 = new ArrayList<>();
			List<String> iosRegistrationIds5 = new ArrayList<>();
			List<String> iosRegistrationIds6 = new ArrayList<>();
			List<String> iosRegistrationIds7 = new ArrayList<>();
			List<String> iosRegistrationIds8 = new ArrayList<>();
			List<String> iosRegistrationIds9 = new ArrayList<>();
			List<String> iosRegistrationIds10 = new ArrayList<>();
			List<String> iosRegistrationIds11 = new ArrayList<>();
			List<String> iosRegistrationIds12 = new ArrayList<>();
			List<String> iosRegistrationIds13 = new ArrayList<>();
			List<String> iosRegistrationIds14 = new ArrayList<>();
			List<String> iosRegistrationIds15 = new ArrayList<>();
			List<String> iosRegistrationIds16 = new ArrayList<>();
			List<String> iosRegistrationIds17 = new ArrayList<>();
			List<String> iosRegistrationIds18 = new ArrayList<>();
			List<String> iosRegistrationIds19 = new ArrayList<>();

			List<String> webRegistrationIds = new ArrayList<>();
			List<String> desktopRegistrationIds = new ArrayList<>();
			//p.user_id,p.token,p.client,p.is_disabled,p.ring_index
			for (DriverPushToken entity : tokens) {
				if (!userId.equals(entity.getUserId())) {
					int client = entity.getClient();
					if (client == Device.android.getId()) {
						androidRegistrationIds.add(entity.getToken());
					} else if (client == Device.ios.getId()) {
						// begin: filter by sound.
						switch (entity.getRingIndex()) {
						case 0:
							iosRegistrationIds0.add(entity.getToken());
							break;
						case 1:
							iosRegistrationIds1.add(entity.getToken());
							break;
						case 2:
							iosRegistrationIds2.add(entity.getToken());
							break;
						case 3:
							iosRegistrationIds3.add(entity.getToken());
							break;
						case 4:
							iosRegistrationIds4.add(entity.getToken());
							break;
						case 5:
							iosRegistrationIds5.add(entity.getToken());
							break;
						case 6:
							iosRegistrationIds6.add(entity.getToken());
							break;
						case 7:
							iosRegistrationIds7.add(entity.getToken());
							break;
						case 8:
							iosRegistrationIds8.add(entity.getToken());
							break;
						case 9:
							iosRegistrationIds9.add(entity.getToken());
							break;
						case 10:
							iosRegistrationIds10.add(entity.getToken());
							break;
						case 11:
							iosRegistrationIds11.add(entity.getToken());
							break;
						case 12:
							iosRegistrationIds12.add(entity.getToken());
							break;
						case 13:
							iosRegistrationIds13.add(entity.getToken());
							break;
						case 14:
							iosRegistrationIds14.add(entity.getToken());
							break;
						case 15:
							iosRegistrationIds15.add(entity.getToken());
							break;
						case 16:
							iosRegistrationIds16.add(entity.getToken());
							break;
						case 17:
							iosRegistrationIds17.add(entity.getToken());
							break;
						case 18:
							iosRegistrationIds18.add(entity.getToken());
							break;
						case 19:
							iosRegistrationIds19.add(entity.getToken());
							break;
						default:
							break;
						}
						// end.
					} else if (client == Device.web.getId()) {
						webRegistrationIds.add(entity.getToken());
					} else if (client == Device.desktop.getId()) {
						desktopRegistrationIds.add(entity.getToken());
					}
				}
			}
			// push to android devices.
			if (!androidRegistrationIds.isEmpty()) {
				this.pushNotificationWithPayload(CommonConstant.PUSH_TITLE, body, androidRegistrationIds, data);
			}
			// push to ios devices.
			pushNotificationWithSound(title, body, iosRegistrationIds0, data, 0);
			pushNotificationWithSound(title, body, iosRegistrationIds1, data, 1);
			pushNotificationWithSound(title, body, iosRegistrationIds2, data, 2);
			pushNotificationWithSound(title, body, iosRegistrationIds3, data, 3);
			pushNotificationWithSound(title, body, iosRegistrationIds4, data, 4);
			pushNotificationWithSound(title, body, iosRegistrationIds5, data, 5);
			pushNotificationWithSound(title, body, iosRegistrationIds6, data, 6);
			pushNotificationWithSound(title, body, iosRegistrationIds7, data, 7);
			pushNotificationWithSound(title, body, iosRegistrationIds8, data, 8);
			pushNotificationWithSound(title, body, iosRegistrationIds9, data, 9);
			pushNotificationWithSound(title, body, iosRegistrationIds10, data, 10);
			pushNotificationWithSound(title, body, iosRegistrationIds11, data, 11);
			pushNotificationWithSound(title, body, iosRegistrationIds12, data, 12);
			pushNotificationWithSound(title, body, iosRegistrationIds13, data, 13);
			pushNotificationWithSound(title, body, iosRegistrationIds14, data, 14);
			pushNotificationWithSound(title, body, iosRegistrationIds15, data, 15);
			pushNotificationWithSound(title, body, iosRegistrationIds16, data, 16);
			pushNotificationWithSound(title, body, iosRegistrationIds17, data, 17);
			pushNotificationWithSound(title, body, iosRegistrationIds18, data, 18);
			pushNotificationWithSound(title, body, iosRegistrationIds19, data, 19);
			// push to web browser.
			if (!webRegistrationIds.isEmpty()) {
				this.pushNotificationWithPayload(CommonConstant.PUSH_TITLE, body, webRegistrationIds, data);
			}
			// push to desktop app.
			if (!desktopRegistrationIds.isEmpty()) {
				this.pushNotificationWithPayload(CommonConstant.PUSH_TITLE, body, desktopRegistrationIds, data);
			}
		} catch (Exception e) {
			System.out.println("\n Error: " + e.getMessage());
		}
	}
	

	/**
	 * Push notification by sound.
	 * 
	 * @param title
	 * @param body
	 * @param iosRegistrationIds
	 * @param data
	 * @param index
	 */
	private void pushNotificationWithSound(String title, String body, List<String> iosRegistrationIds,
			Map<String, String> data, int index) {
		if (!iosRegistrationIds.isEmpty()) {
			if (title.isEmpty()) {
				this.pushNotification(CommonConstant.PUSH_TITLE, body, iosRegistrationIds,
						CommonConstant.RINGINGS[index], data);
			} else {
				this.pushNotification(title, body, iosRegistrationIds, CommonConstant.RINGINGS[index], data);
			}
		}
	}

	/**
	 * Notification to an user.
	 */
	@Override
	public void send(DriverPushToken token, String title, String body, Map<String, String> data) {
		int client = token.getClient();
		if (client == Device.android.getId()) {
			this.pushNotificationWithPayload(CommonConstant.PUSH_TITLE, body, Arrays.asList(token.getToken()), data);
		} else if (client == Device.ios.getId()) {
			if (title.isEmpty()) {
				this.pushNotification(CommonConstant.PUSH_TITLE, body, Arrays.asList(token.getToken()),
						CommonConstant.SOUND_DEFAULT, data);
			} else {
				this.pushNotification(title, body, Arrays.asList(token.getToken()), CommonConstant.SOUND_DEFAULT, data);
			}
		}
	}
}
