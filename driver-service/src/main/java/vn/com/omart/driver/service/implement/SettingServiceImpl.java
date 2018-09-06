package vn.com.omart.driver.service.implement;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.com.omart.driver.dto.SettingDTO;
import vn.com.omart.driver.entity.DriverInfo;
import vn.com.omart.driver.entity.DriverPushToken;
import vn.com.omart.driver.entity.Setting;
import vn.com.omart.driver.repository.DriverInfoRepository;
import vn.com.omart.driver.repository.DriverPushTokenRepository;
import vn.com.omart.driver.repository.SettingRepository;
import vn.com.omart.driver.service.SettingService;

@Service
public class SettingServiceImpl implements SettingService {

	@Autowired
	private SettingRepository settingRepository;

	@Autowired
	private DriverInfoRepository driverInfoRepository;

	@Autowired
	private DriverPushTokenRepository driverPushTokenRepository;

	@Transactional(readOnly = true)
	@Override
	public void saveDefault(DriverInfo driverInfo) {
		Setting entity = settingRepository.findByDriverInfo(driverInfo);
		if (entity == null) {
			entity = new Setting();
			entity.setDriverInfo(driverInfo);
			entity = settingRepository.save(entity);
		}
	}

	@Transactional(readOnly = false)
	@Override
	public void updateOrInsert(String userId, SettingDTO dto) {
		Setting entity = settingRepository.findByDriverInfoUserId(userId);
		DriverPushToken driverPushToken = driverPushTokenRepository.findOne(userId);
		if (entity == null) {
			DriverInfo driverInfo = driverInfoRepository.findOne(userId);
			if (driverInfo != null) {
				entity = new Setting();
				entity.setDriverInfo(driverInfo);
			}
		} else {
			entity.setDriverInfo(entity.getDriverInfo());
		}
		entity.setRingIndex(dto.getRingIndex());
		entity.setDisabled(dto.isDisable());
		settingRepository.save(entity);

		if (driverPushToken != null) {
			driverPushToken.setDisabled(dto.isDisable());
			driverPushToken.setRingIndex(dto.getRingIndex());
			driverPushTokenRepository.save(driverPushToken);
		}
	}

	public void copyDriverInfoIntoSetting() {
		List<DriverInfo> entities = driverInfoRepository.findAll();
		List<Setting> settings = new ArrayList<>();
		for (DriverInfo entity : entities) {
			Setting settingItem = settingRepository.findByDriverInfo(entity);
			if (settingItem == null) {
				Setting setting = new Setting();
				setting.setDriverInfo(entity);
				settings.add(setting);
			}
		}
		settingRepository.save(settings);
	}
}
