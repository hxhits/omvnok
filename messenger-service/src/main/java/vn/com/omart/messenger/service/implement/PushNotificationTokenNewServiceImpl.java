package vn.com.omart.messenger.service.implement;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.com.omart.messenger.application.request.DeviceGroupToken;
import vn.com.omart.messenger.common.constant.Device;
import vn.com.omart.messenger.domain.model.PushNotificationTokenId;
import vn.com.omart.messenger.domain.model.PushNotificationTokenNew;
import vn.com.omart.messenger.domain.model.PushNotificationTokenNewRepository;
import vn.com.omart.messenger.domain.model.PushNotificationTokenRepository;
import vn.com.omart.messenger.service.PushNotificationTokenNewService;

@Service
public class PushNotificationTokenNewServiceImpl implements PushNotificationTokenNewService {

	@Autowired
	private PushNotificationTokenNewRepository pushNotificationTokenNewRepository;

	@Autowired
	private PushNotificationTokenRepository pushNotificationTokenRepository;

	@Override
	public PushNotificationTokenNew findByUserIdAndToken(String userId, String token) {
		// TODO Auto-generated method stub
		PushNotificationTokenId id = new PushNotificationTokenId(userId, token);
		PushNotificationTokenNew entity = pushNotificationTokenNewRepository.findOne(id);
		return entity;
	}

	@Override
	public void save(String userId, Device client, String token) {
		// TODO Auto-generated method stub
		PushNotificationTokenId id = new PushNotificationTokenId(userId, token);
		PushNotificationTokenNew entity = pushNotificationTokenNewRepository.findOne(id);
		if (entity == null) {
			entity = new PushNotificationTokenNew();
			entity.setId(id);
		}
		entity.setClient(client.getId());
		pushNotificationTokenNewRepository.save(entity);
	}

	@Override
	public void deleteByUserIdAndToken(String userId, String token) {
		PushNotificationTokenNew entity = this.findByUserIdAndToken(userId, token);
		if (entity != null) {
			pushNotificationTokenNewRepository.delete(entity);
		}
	}

	@Override
	public void deleteByUserId(String userId) {
		List<PushNotificationTokenNew> entities = pushNotificationTokenNewRepository.findByIdUserId(userId);
		if (entities != null) {
			pushNotificationTokenNewRepository.delete(entities);
		}
	}

	@Override
	public DeviceGroupToken getDeviceGroupToken(String userId) {
		List<PushNotificationTokenNew> entities = pushNotificationTokenNewRepository.findByIdUserId(userId);
		DeviceGroupToken tokenGroup = new DeviceGroupToken();
		List<String> iosTokens = new ArrayList<>();
		List<String> androidTokens = new ArrayList<>();
		List<String> webTokens = new ArrayList<>();
		List<String> desktopTokens = new ArrayList<>();
		if (entities != null) {
			for (PushNotificationTokenNew entity : entities) {
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
		tokenGroup.setAndroidTokens(androidTokens);
		tokenGroup.setDesktopTokens(desktopTokens);
		tokenGroup.setIosTokens(iosTokens);
		tokenGroup.setWebTokens(webTokens);
		return tokenGroup;
	}

}
