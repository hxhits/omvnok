package vn.com.omart.driver.service.implement;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import vn.com.omart.driver.common.constant.CommonConstant;
import vn.com.omart.driver.common.constant.DriverType.LockType;
import vn.com.omart.driver.common.util.CommonUtils;
import vn.com.omart.driver.common.util.DateUtils;
import vn.com.omart.driver.dto.DriverInfoDTO;
import vn.com.omart.driver.dto.SettingDTO;
import vn.com.omart.driver.dto.UserDTO;
import vn.com.omart.driver.entity.CarType;
import vn.com.omart.driver.entity.DriverInfo;
import vn.com.omart.driver.entity.DriverPushToken;
import vn.com.omart.driver.entity.Province;
import vn.com.omart.driver.repository.CarTypeRepository;
import vn.com.omart.driver.repository.DriverInfoRepository;
import vn.com.omart.driver.repository.DriverPushTokenRepository;
import vn.com.omart.driver.repository.ProvinceRepository;
import vn.com.omart.driver.service.DriverDistFollowService;
import vn.com.omart.driver.service.DriverFollowService;
import vn.com.omart.driver.service.DriverInfoService;
import vn.com.omart.driver.service.DriverPushTokenService;
import vn.com.omart.driver.service.SettingService;

@Service
public class DriverInfoServiceImpl implements DriverInfoService {

	private static final Logger logger = LoggerFactory.getLogger(DriverInfoServiceImpl.class);

	@Value("${auth.user.set-password}")
	private String Set_Password_API;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private DriverInfoRepository driverInfoRepository;

	@Autowired
	private ProvinceRepository provinceRepository;

	@Autowired
	private CarTypeRepository carTypeRepository;

	@Autowired
	private ServiceUtilImpl serviceUtilImpl;

	@Autowired
	private DriverDistFollowService driverDistFollowService;

	@Autowired
	private DriverFollowService driverFollowService;

	@Autowired
	private DriverPushTokenRepository driverPushTokenRepository;

	@Autowired
	private SettingService settingService;

	@Autowired
	private DriverPushTokenService driverPushTokenService;

	/**
	 * Save
	 */
	@Override
	@Transactional(readOnly = false)
	public void save(DriverInfoDTO dto) {
		// TODO Auto-generated method stub
		// Save driver info.
		DriverInfo entity = DriverInfoDTO.toEntity(dto);
		entity.setUserId(dto.getUserId());
		entity.setDateOfRegistration(DateUtils.getCurrentDate());
		CarType carType = carTypeRepository.findOne(dto.getCarTypeId());
		Province province = provinceRepository.findOne(dto.getProvinceId());
		if (carType != null) {
			entity.setCarType(carType);
			entity.setContractId(this.getContractId(carType.getId()));
		} else {
			logger.error("Car type was null");
		}
		if (province != null) {
			entity.setProvince(province);
		} else {
			logger.error("Province was null");
		}
		UserDTO user = serviceUtilImpl.getUserProfile(dto.getUserId());
		entity.setPhoneNumber(user.getPhoneNumber());
		entity = driverInfoRepository.save(entity);

		// save default value setting.
		settingService.saveDefault(entity);

		// set follow.
		driverDistFollowService.save(dto.getUserId(), province.getId(), 0L);
		driverFollowService.save(dto.getUserId(), carType);

		// connect to authentication service to set password.
		Map<String, String> params = new HashMap<String, String>();
		params.put("account", user.getUsername());
		params.put("password", dto.getPassword());
		ResponseEntity<Void> response = restTemplate.postForEntity(Set_Password_API, null, Void.class, params);
		HttpStatus status = response.getStatusCode();
		if (status == HttpStatus.OK) {
			logger.info("Driver account register success");
		} else {
			logger.info("Driver account can not set password");
		}
	}

	/**
	 * Generate contract id.
	 * 
	 * @param carTypeId
	 * @return
	 */
	private String getContractId(Long carTypeId) {
		// generate contract id.
		DriverInfo entity = driverInfoRepository.findTop1ByOrderByDateOfRegistrationDesc();
		String contactIdStr = CommonUtils.contractIdGenerator(carTypeId, entity.getContractId());
		return contactIdStr;
	}

	@Override
	public void updateContractId() {
		List<DriverInfo> entities = driverInfoRepository.getAllByContractIdIsEmpty();
		List<DriverInfo> entitiesUpdate = new ArrayList<>();
		if (entities != null) {
			for (int i = 0; i < entities.size(); i++) {
				DriverInfo dr = entities.get(i);
				Long carTypeId = dr.getCarType().getId();
				dr.setContractId(
						CommonUtils.contractIdGenerator(carTypeId, entities.get(i).getDateOfRegistration(), i));
				entitiesUpdate.add(dr);
			}
			driverInfoRepository.save(entitiesUpdate);
		}
	}

	/**
	 * Checking existing by id.
	 */
	@Override
	public boolean isDriverExisting(String userId) {
		// TODO Auto-generated method stub
		DriverInfo entity = driverInfoRepository.findOne(userId);
		if (entity != null) {
			return true;
		}
		return false;
	}

	/**
	 * Checking existing by phone number.
	 */
	@Override
	public boolean isDriverExistingByPhoneNumber(String phone) {
		// TODO Auto-generated method stub
		DriverInfo entity = driverInfoRepository.findByPhoneNumber(phone);
		if (entity != null) {
			return true;
		}
		return false;
	}

	/**
	 * Get user profile.
	 */
	@Override
	public DriverInfoDTO getDriverProfile(String userId) {
		DriverInfo entity = driverInfoRepository.findOne(userId);
		DriverInfoDTO dto = null;
		if (entity != null) {
			dto = DriverInfoDTO.toDTO(entity);
			if (entity.getSetting() != null) {
				SettingDTO setting = new SettingDTO();
				setting.setDisable(entity.getSetting().isDisabled());
				setting.setRingIndex(entity.getSetting().getRingIndex());
				dto.setSetting(setting);
			} else {
				dto.setSetting(new SettingDTO());
			}
		} else {
			logger.error("User profile not found");
		}
		return dto;
	}

	@Override
	public List<DriverInfoDTO> getAllDriverInfo(Pageable pageable) {
		Page<DriverInfo> pages = driverInfoRepository.findAll(pageable);
		List<DriverInfo> entities = pages.getContent();
		List<DriverInfoDTO> dtos = new ArrayList<>();
		if (dtos != null) {
			dtos = entities.stream().map(DriverInfoDTO::toDTO).collect(Collectors.toList());
		}
		return dtos;
	}

	@Transactional(readOnly = false)
	@Override
	public void updateBlockDriver(String userId, DriverInfoDTO dto) {
		// TODO Auto-generated method stub
		DriverInfo entity = driverInfoRepository.findOne(userId);
		if (entity != null) {

			if (dto.getAction() == LockType.LOCK) {
				entity.setBlocked(true);
			} else if (dto.getAction() == LockType.UNLOCK) {
				entity.setBlocked(false);
			}

			if (dto.getReason() != null) {
				entity.setReason(dto.getReason());
			}

			driverInfoRepository.save(entity);
			driverPushTokenService.updateIsBlocked(userId, dto.getAction());
		} else {
			logger.info("\n [updateBlockDriver] has DriverInfo/DriverPushToken was null");
		}
	}

	@Override
	public boolean isDriverBlocked(String userId) {
		// TODO Auto-generated method stub
		DriverInfo entity = driverInfoRepository.findOne(userId);
		if (entity != null) {
			return entity.isBlocked();
		} else {
			logger.info("isDriverBlocked has DriverInfo was null");
		}
		return false;
	}

	@Override
	public void rating(String driverId, int star) {
		DriverInfo entity = driverInfoRepository.findOne(driverId);
		if (entity != null) {
			int totalStar = entity.getTotalStar();
			float overallRating = entity.getRateNumber();
			float rateNumber = CommonUtils.calculateStarRating(overallRating, totalStar, star);
			entity.setRateNumber(CommonUtils.makeRound2Decimal(rateNumber));
			entity.setTotalStar(totalStar + 1);
			driverInfoRepository.save(entity);
		} else {
			logger.info("rating has DriverInfo was null");
		}
	}

	@Override
	public void update(String userId, DriverInfoDTO dto) {
		// TODO Auto-generated method stub
		// Update driver info.
		DriverInfo entity = driverInfoRepository.findOne(userId);
		if (entity != null) {
			entity = DriverInfoDTO.toEntity(entity, dto);
			entity = driverInfoRepository.save(entity);
		} else {
			logger.info("update driver info was null");
		}
	}
	
	@Override
	public void basicUpdate(String userId, DriverInfoDTO dto) {
		DriverInfo entity = driverInfoRepository.findOne(dto.getUserId());
		if (entity != null) {
			entity = DriverInfoDTO.toBasicEntity(entity, dto);
			entity = driverInfoRepository.save(entity);
		} else {
			logger.info("basic update driver info was null");
		}
	}

	@Override
	public List<DriverInfoDTO> getByDateOfRegistrationAtBetween(Long from, Long to, Pageable pageable) {
		Date fromDate = new Date(from);
		Date toDate = new Date(to);
		String strTo = DateUtils.getDateByFormat(toDate, CommonConstant.YYYY_MM_DD);
		String strFrom = DateUtils.getDateByFormat(fromDate, CommonConstant.YYYY_MM_DD);
		List<DriverInfoDTO> dtos = new ArrayList<>();
		List<DriverInfo> entities = driverInfoRepository.getByDateOfRegistrationAtBetween(strFrom, strTo, pageable);
		if (entities != null) {
			dtos = entities.stream().map(DriverInfoDTO::toDTO).collect(Collectors.toList());
		}
		return dtos;
	}

	@Override
	public List<DriverInfoDTO> getByFullNameOrPhone(String text, Pageable pageable) {
		List<DriverInfoDTO> dtos = new ArrayList<>();
		if(StringUtils.isNotBlank(text)) {
			List<DriverInfo> entities = new ArrayList<>();
			if (StringUtils.isNumericSpace(text)) {
				text = CommonUtils.removeSpace(text);
				entities = driverInfoRepository.findByPhoneNumberContainingOrderByDateOfRegistrationDesc(text, pageable);
			} else {
				entities = driverInfoRepository.findByFullNameContainingOrderByDateOfRegistrationDesc(text.trim(), pageable);
			}
			if (!entities.isEmpty()) {
				dtos = entities.stream().map(DriverInfoDTO::toDTO).collect(Collectors.toList());
			}
		}
		return dtos;
	}

}
