package vn.com.omart.messenger.service;

import vn.com.omart.messenger.application.request.DeviceGroupToken;
import vn.com.omart.messenger.common.constant.Device;
import vn.com.omart.messenger.domain.model.PushNotificationTokenNew;

public interface PushNotificationTokenNewService {
	
	public PushNotificationTokenNew findByUserIdAndToken(String userId, String token);
	
	public void save(String userId, Device client, String token);
	
	public void deleteByUserIdAndToken(String userId, String token);
	
	public void deleteByUserId(String userId);
	
	public DeviceGroupToken getDeviceGroupToken(String userId);
}
