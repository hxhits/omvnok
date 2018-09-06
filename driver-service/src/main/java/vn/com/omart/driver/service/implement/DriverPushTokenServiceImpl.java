package vn.com.omart.driver.service.implement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.com.omart.driver.common.constant.CommonConstant;
import vn.com.omart.driver.common.constant.DriverType.Device;
import vn.com.omart.driver.common.constant.DriverType.LockType;
import vn.com.omart.driver.dto.DriverPushTokenDTO;
import vn.com.omart.driver.entity.DriverInfo;
import vn.com.omart.driver.entity.DriverPushToken;
import vn.com.omart.driver.entity.Setting;
import vn.com.omart.driver.repository.DriverInfoRepository;
import vn.com.omart.driver.repository.DriverPushTokenRepository;
import vn.com.omart.driver.repository.SettingRepository;
import vn.com.omart.driver.service.DriverPushTokenService;

@Service
public class DriverPushTokenServiceImpl implements DriverPushTokenService {

	private final static Logger logger = LoggerFactory.getLogger(DriverPushTokenServiceImpl.class);

	@Autowired
	private DriverPushTokenRepository driverPushTokenRepository;

	@Autowired
	private SettingRepository settingRepository;

	@Autowired
	private DriverInfoRepository driverInfoRepository;

	/**
	 * Update token.
	 */
	@Override
	public void save(String userId, Device client, DriverPushTokenDTO dto) {
		// TODO Auto-generated method stub
		DriverInfo driverInfo = driverInfoRepository.findOne(userId);
		if (driverInfo != null) {
			Setting setting = settingRepository.findByDriverInfoUserId(userId);
			DriverPushToken entity = new DriverPushToken();
			entity.setUserId(userId);
			entity.setClient(client.getId());
			entity.setToken(dto.getToken());
			entity.setBlocked(driverInfo.isBlocked());
			if (setting != null) {
				entity.setDisabled(setting.isDisabled());
				entity.setRingIndex(setting.getRingIndex());
			} else {
				entity.setDisabled(false);
				entity.setRingIndex(0);
			}
			driverPushTokenRepository.save(entity);
		}
	}

	/**
	 * Delete token.
	 */
	@Override
	public void delete(String userId) {
		// TODO Auto-generated method stub
		DriverPushToken entity = driverPushTokenRepository.findByUserId(userId);
		if (entity != null) {
			driverPushTokenRepository.delete(entity);
		}
	}

	/**
	 * Update disable. Mean user cannot receive notification.
	 */
	@Transactional(readOnly = false)
	@Override
	public void updateIsDisable(String userId) {
		// TODO Auto-generated method stub
		DriverPushToken entity = driverPushTokenRepository.findOne(userId);
		Setting setting = settingRepository.findByDriverInfoUserId(userId);
		if (entity != null && setting != null) {
			entity.setDisabled(true);
			driverPushTokenRepository.save(entity);
			setting.setDisabled(true);
			settingRepository.save(setting);
		} else {
			logger.error("function update driver token: userId = " + userId + " not found");
		}
	}

	/**
	 * Update enable. Mean user continue receive notification.
	 */
	@Transactional(readOnly = false)
	@Override
	public void updateIsEnable(String userId) {
		// TODO Auto-generated method stub
		DriverPushToken entity = driverPushTokenRepository.findOne(userId);
		Setting setting = settingRepository.findByDriverInfoUserId(userId);
		if (entity != null && setting != null) {
			entity.setDisabled(false);
			driverPushTokenRepository.save(entity);
			setting.setDisabled(false);
			settingRepository.save(setting);
		} else {
			logger.error("function update driver token: userId = " + userId + " not found");
		}
	}

	/**
	 * Update is blocked.
	 */
	@Override
	public void updateIsBlocked(String userId, LockType action) {
		DriverPushToken driverPushToken = driverPushTokenRepository.findOne(userId);
		if (driverPushToken != null) {
			if (action == LockType.LOCK) {
				driverPushToken.setBlocked(true);
			} else if (action == LockType.UNLOCK) {
				driverPushToken.setBlocked(false);
			}
			driverPushTokenRepository.save(driverPushToken);
		}
	}

}
