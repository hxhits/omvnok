package vn.com.omart.backend.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.com.omart.backend.constants.ConstantUtils;
import vn.com.omart.backend.constants.Device;
import vn.com.omart.backend.domain.model.PoiAction;
import vn.com.omart.backend.domain.model.PushNotificationToken;
import vn.com.omart.backend.domain.model.PushNotificationTokenId;
import vn.com.omart.backend.domain.model.PushNotificationTokenNew;
import vn.com.omart.backend.domain.model.PushNotificationTokenNewRepository;
import vn.com.omart.backend.domain.model.PushNotificationTokenRepository;
import vn.com.omart.backend.port.adapter.support.DeviceGroupToken;

@Service
public class PushNotificationTokenNewService {

	private static final Logger logger = LoggerFactory.getLogger(PushNotificationTokenNewService.class);

	@Autowired
	private FcmClientDriverService fcmClientDriverService;
	
	@Autowired
	private FcmClientService fcmClientService;

	@Autowired
	private PushNotificationTokenNewRepository pushNotificationTokenNewRepository;

	@Autowired
	private PushNotificationTokenRepository pushNotificationTokenRepository;

	/**
	 * Migration.
	 */
	public void dataMigration() {
		List<PushNotificationToken> tokens = (List<PushNotificationToken>) pushNotificationTokenRepository.findAll();
		List<PushNotificationTokenNew> newTokens = new ArrayList<>();
		if (tokens != null) {
			newTokens = tokens.stream().map(this::toTokenNew).collect(Collectors.toList());
			if (newTokens != null) {
				pushNotificationTokenNewRepository.save(newTokens);
			}
		}
	}

	/**
	 * Transfer old to new push token.
	 * 
	 * @param token
	 * @return PushNotificationTokenNew
	 */
	public PushNotificationTokenNew toTokenNew(PushNotificationToken token) {
		PushNotificationTokenNew tNew = new PushNotificationTokenNew();
		PushNotificationTokenId id = new PushNotificationTokenId(token.getUserId(), token.getToken());
		tNew.setClient(token.getClient());
		tNew.setId(id);
		return tNew;
	}

	/**
	 * Get Token By UserId.
	 * 
	 * @param userIgnore
	 * @param userId
	 * @param client
	 * @return DeviceGroupToken
	 */
	public DeviceGroupToken getTokenByUserId(String userIgnore, String userId) {
		List<PushNotificationTokenNew> entities = pushNotificationTokenNewRepository.findByIdUserId(userId);
		DeviceGroupToken tokens = this.getDeviceGroupTokens(userIgnore, entities);
		return tokens;
	}

	/**
	 * Get Token By UserId.
	 * 
	 * @param userId
	 * @return DeviceGroupToken
	 */
//	public DeviceGroupToken getTokenByUserId(String userId) {
//		List<PushNotificationTokenNew> entities = pushNotificationTokenNewRepository.findByIdUserId(userId);
//		DeviceGroupToken tokens = this.getDeviceGroupTokens("", entities);
//		return tokens;
//	}

	/**
	 * Get By In UserIds.
	 * 
	 * @param userIgnore
	 * @param userIds
	 * @return DeviceGroupToken
	 */
	public DeviceGroupToken getByInUserIds(String userIgnore, List<String> userIds) {
		List<PushNotificationTokenNew> entities = pushNotificationTokenNewRepository.getByInUserIds(userIds);
		DeviceGroupToken tokens = this.getDeviceGroupTokens(userIgnore, entities);
		return tokens;
	}

	/**
	 * Get Token By Position.
	 * 
	 * @param userIgnore
	 * @param posId
	 * @param provinces
	 * @param districts
	 * @return DeviceGroupToken
	 */
	public DeviceGroupToken getTokenByPosition(String userIgnore, Long posId, String provinces, String districts) {
		List<Object[]> objList = pushNotificationTokenNewRepository.getByPosition(posId, provinces, districts);
		DeviceGroupToken tokens = this.getDeviceGroupTokensByObject(userIgnore, objList);
		return tokens;
	}
	
	/**
	 * Get tokens base poi action.
	 * @param tokens
	 * @param userIgnore
	 * @param poiId
	 * @return DeviceGroupToken
	 */
	public DeviceGroupToken getTokenBasePoiAction(DeviceGroupToken tokens,String userIgnore,Long poiId) {
		List<Object[]> objList = pushNotificationTokenNewRepository.getBasePoiAction(poiId);
		DeviceGroupToken result = this.getDeviceGroupTokens(tokens, userIgnore, objList);
		return result;
	}

	/**
	 * Get Device Group Tokens.
	 * 
	 * @param userIgnore
	 * @param entities
	 * @return DeviceGroupToken
	 */
	public DeviceGroupToken getDeviceGroupTokens(String userIgnore, List<PushNotificationTokenNew> entities) {
		DeviceGroupToken tokens = new DeviceGroupToken();
		List<String> iosTokens = new ArrayList<>();
		List<String> androidTokens = new ArrayList<>();
		List<String> webTokens = new ArrayList<>();
		List<String> desktopTokens = new ArrayList<>();
		if (entities != null) {
			for (PushNotificationTokenNew entity : entities) {
				if (!entity.getId().getUserId().equals(userIgnore)) {
					if (entity.getClient() == Device.android.getId()) {
						androidTokens.add(entity.getId().getToken());
					} else if (entity.getClient() == Device.ios.getId()) {
						iosTokens.add(entity.getId().getToken());
					} else if (entity.getClient() == Device.web.getId()) {
						webTokens.add(entity.getId().getToken());
					} else if (entity.getClient() == Device.desktop.getId()) {
						desktopTokens.add(entity.getId().getToken());
					}
				}
			}
		}
		tokens.setAndroidTokens(androidTokens);
		tokens.setDesktopTokens(desktopTokens);
		tokens.setIosTokens(iosTokens);
		tokens.setWebTokens(webTokens);
		return tokens;
	}

	/**
	 * Get Device Group Tokens By Object.
	 * 
	 * @param userIgnore
	 * @param objList
	 * @return DeviceGroupToken
	 */
	public DeviceGroupToken getDeviceGroupTokensByObject(String userIgnore, List<Object[]> objList) {
		DeviceGroupToken tokens = new DeviceGroupToken();
		List<String> iosTokens = new ArrayList<>();
		List<String> androidTokens = new ArrayList<>();
		List<String> webTokens = new ArrayList<>();
		List<String> desktopTokens = new ArrayList<>();
		// p.user_id, p.token, p.client
		if (objList != null) {
			for (Object[] objs : objList) {
				if (!userIgnore.equals(String.valueOf(objs[0]))) {
					int client = Integer.valueOf(String.valueOf(objs[2]));
					if (client == Device.android.getId()) {
						androidTokens.add(String.valueOf(objs[1]));
					} else if (client == Device.ios.getId()) {
						iosTokens.add(String.valueOf(objs[1]));
					} else if (client == Device.web.getId()) {
						webTokens.add(String.valueOf(objs[1]));
					} else if (client == Device.desktop.getId()) {
						desktopTokens.add(String.valueOf(objs[1]));
					}
				}
			}
		}
		tokens.setAndroidTokens(androidTokens);
		tokens.setDesktopTokens(desktopTokens);
		tokens.setIosTokens(iosTokens);
		tokens.setWebTokens(webTokens);
		return tokens;
	}
	
	public DeviceGroupToken getDeviceGroupTokens(DeviceGroupToken tokens,String userIgnore, List<Object[]> objList) {
		// p.user_id, p.token, p.client
		if (objList != null) {
			for (Object[] objs : objList) {
				if (!userIgnore.equals(String.valueOf(objs[0]))) {
					String tk = String.valueOf(objs[1]);
					int client = Integer.valueOf(String.valueOf(objs[2]));
					if (client == Device.android.getId() && !tokens.getAndroidTokens().contains(tk)) {
						tokens.getAndroidTokens().add(tk);
					} else if (client == Device.ios.getId() && !tokens.getIosTokens().contains(tk)) {
						tokens.getIosTokens().add(tk);
					} else if (client == Device.web.getId() && !tokens.getWebTokens().contains(tk)) {
						tokens.getWebTokens().add(tk);
					} else if (client == Device.desktop.getId() && !tokens.getDesktopTokens().contains(tk)) {
						tokens.getDesktopTokens().add(tk);
					}
				}
			}
		}
		return tokens;
	}

	/**
	 * Send Notification.
	 * 
	 * @param title
	 * @param clickAction
	 * @param body
	 * @param data
	 * @param tokens
	 */
	public void sendNotification(String title, String clickAction, String body, Map<String, String> data,
			DeviceGroupToken tokens) {
		List<String> androidRegistrationIds = tokens.getAndroidTokens();
		List<String> iosRegistrationIds = tokens.getIosTokens();
		List<String> webRegistrationIds = tokens.getWebTokens();
		List<String> desktopRegistrationIds = tokens.getDesktopTokens();

		// push to android devices.
		if (!androidRegistrationIds.isEmpty()) {
			fcmClientService.pushNotificationWithPayload(ConstantUtils.PUSH_TITLE, body, androidRegistrationIds, data);
		}
		
		// push to ios devices.
		if (!iosRegistrationIds.isEmpty()) {
			if (title.isEmpty()) {
				fcmClientService.pushNotification(ConstantUtils.PUSH_TITLE, body, iosRegistrationIds,
						ConstantUtils.SOUND_DEFAULT, data);
			} else {
				fcmClientService.pushNotification(title, body, iosRegistrationIds, ConstantUtils.SOUND_DEFAULT, data);
			}
		}
		
		// push to web browser.
		if (!webRegistrationIds.isEmpty()) {
			// fcmClientService.pushNotificationWithoutPayload(title, body, clickAction,
			// webRegistrationIds, data);
			fcmClientService.pushNotificationWithPayload(title, body, webRegistrationIds, data);
		}
		
		// push to desktop app.
		if (!desktopRegistrationIds.isEmpty()) {
			fcmClientService.pushNotificationWithPayload(ConstantUtils.PUSH_TITLE, body, desktopRegistrationIds, data);
		}
	}
	
	/**
	 * Send Notification.
	 * 
	 * @param title
	 * @param clickAction
	 * @param body
	 * @param data
	 * @param tokens
	 */
	public void sendNotificationToDriver(String title, String clickAction, String body, Map<String, String> data,
			DeviceGroupToken tokens) {
		List<String> androidRegistrationIds = tokens.getAndroidTokens();
		List<String> iosRegistrationIds = tokens.getIosTokens();
		List<String> webRegistrationIds = tokens.getWebTokens();
		List<String> desktopRegistrationIds = tokens.getDesktopTokens();

		// push to android devices.
		if (!androidRegistrationIds.isEmpty()) {
			fcmClientDriverService.pushNotificationWithPayload(ConstantUtils.PUSH_TITLE, body, androidRegistrationIds, data);
		}
		
		// push to ios devices.
		if (!iosRegistrationIds.isEmpty()) {
			if (title.isEmpty()) {
				fcmClientDriverService.pushNotification(ConstantUtils.PUSH_TITLE, body, iosRegistrationIds,
						ConstantUtils.SOUND_DEFAULT, data);
			} else {
				fcmClientDriverService.pushNotification(title, body, iosRegistrationIds, ConstantUtils.SOUND_DEFAULT, data);
			}
		}
		
		// push to web browser.
		if (!webRegistrationIds.isEmpty()) {
			fcmClientDriverService.pushNotificationWithPayload(title, body, webRegistrationIds, data);
		}
		
		// push to desktop app.
		if (!desktopRegistrationIds.isEmpty()) {
			fcmClientDriverService.pushNotificationWithPayload(ConstantUtils.PUSH_TITLE, body, desktopRegistrationIds, data);
		}
	}

}
