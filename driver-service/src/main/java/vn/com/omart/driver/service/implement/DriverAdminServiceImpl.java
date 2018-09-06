package vn.com.omart.driver.service.implement;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.com.omart.driver.common.constant.CommonConstant;
import vn.com.omart.driver.dto.NotificationContentDTO;
import vn.com.omart.driver.entity.DriverPushToken;
import vn.com.omart.driver.repository.DriverPushTokenRepository;
import vn.com.omart.driver.service.DriverAdminService;
import vn.com.omart.driver.service.FcmClientService;

@Service
public class DriverAdminServiceImpl implements DriverAdminService {

	@Autowired
	private FcmClientService fcmClientService;

	@Autowired
	private DriverPushTokenRepository driverPushTokenRepository;

	@Override
	public void notificationToDriver(NotificationContentDTO dto) {
		// TODO Auto-generated method stub
		DriverPushToken token = driverPushTokenRepository.findOne(dto.getUserId());
		if (token != null) {
			String title = CommonConstant.PUSH_TITLE, body = dto.getContent();
			Map<String, String> data = new HashMap<String, String>();
			data.put("type", "31");
			data.put("title", title);
			data.put("description", body);
			fcmClientService.send(token, title, body, data);
		}
	}

}
