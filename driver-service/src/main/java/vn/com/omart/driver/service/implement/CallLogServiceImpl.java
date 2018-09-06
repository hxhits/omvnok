package vn.com.omart.driver.service.implement;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.com.omart.driver.common.constant.DriverType;
import vn.com.omart.driver.common.constant.DriverType.CallLogState;
import vn.com.omart.driver.common.util.DateUtils;
import vn.com.omart.driver.dto.BookCarDTO;
import vn.com.omart.driver.dto.CallLogDTO;
import vn.com.omart.driver.dto.DriverInfoDTO;
import vn.com.omart.driver.entity.BookCar;
import vn.com.omart.driver.entity.CallLog;
import vn.com.omart.driver.entity.CallLogId;
import vn.com.omart.driver.entity.DriverInfo;
import vn.com.omart.driver.entity.DriverLocation;
import vn.com.omart.driver.repository.BookCarRepository;
import vn.com.omart.driver.repository.CallLogRepository;
import vn.com.omart.driver.repository.DriverInfoRepository;
import vn.com.omart.driver.repository.DriverLocationRepository;
import vn.com.omart.driver.service.CallLogService;

@Service
public class CallLogServiceImpl implements CallLogService {

	private static final Logger logger = LoggerFactory.getLogger(CallLogServiceImpl.class);

	@Autowired
	private CallLogRepository callLogRepository;

	@Autowired
	private BookCarRepository bookCarRepository;

	@Autowired
	private DriverLocationRepository driverLocationRepository;

	@Autowired
	private DriverInfoRepository driverInfoRepository;

	/**
	 * Save call log.
	 */
	@Override
	public CallLogDTO save(String userId, String location, Long bookcarId) {
		// TODO Auto-generated method stub
		//DriverLocation driver = driverLocationRepository.findOne(userId);
		BookCar bookcar = bookCarRepository.findOne(bookcarId);
		CallLogDTO dto = new CallLogDTO();
		if (bookcar != null) {
			double latitude = 0;
			double longitude = 0;
			CallLogId id = new CallLogId(userId, bookcar.getId());

			CallLog entity = callLogRepository.findOne(id);
			if (entity != null) {
				entity.setState(entity.getState());
			} else {
				entity = new CallLog();
				entity.setState(DriverType.CallLogState.CALLING.getId());
			}
			entity.setId(id);
			entity.setBookerId(bookcar.getUserId());
			if (!StringUtils.isBlank(location)) {
				String[] locations = location.split(",");
				latitude = Double.valueOf(locations[0]);
				longitude = Double.valueOf(locations[1]);
			}
			entity.setLatitude(latitude);
			entity.setLongitude(longitude);

			entity.setCreateaAt(DateUtils.getCurrentDate());
			entity = callLogRepository.save(entity);
			//if booker is approved you then sys return APPROVED_ME
			if(entity.getId().getDriverId().equals(userId) && entity.getState() == DriverType.CallLogState.APPROVED.getId()) {
				dto.setState(DriverType.CallLogState.APPROVED_ME);
			} else {
				dto.setState(DriverType.CallLogState.getById(entity.getState()));
			}
		} else {
			logger.error("Function save: bookcar was null");
		}
		return dto;
	}

	/**
	 * Get call log.
	 */
	@Override
	public CallLogDTO getByBookcarOrderByCreatedAtDesc(Long bookcarId) {
		CallLog entity = callLogRepository.findTopByIdBookcarIdAndStateOrderByCreateaAtDesc(bookcarId,
				DriverType.CallLogState.CALLING.getId());
		if (entity != null) {
			String driverId = entity.getId().getDriverId();
			DriverInfo driverInfo = driverInfoRepository.findOne(driverId);
			if (driverInfo != null) {
				DriverInfoDTO profile = new DriverInfoDTO();
				profile.setAvatar(driverInfo.getAvatar());
				profile.setFullName(driverInfo.getFullName());
				profile.setNumberPlate(driverInfo.getNumberPlate());
				profile.setCarTypeId(driverInfo.getCarType().getId());
				profile.setUserId(driverInfo.getUserId());
				profile.setPhoneNumber(driverInfo.getPhoneNumber());
				if (driverInfo.getModel() != null) {
					profile.setModel(driverInfo.getModel());
				}
				CallLogDTO dto = new CallLogDTO();
				dto.setProfile(profile);
				dto.setBookcarId(entity.getId().getBookcarId());
				dto.setDriverId(entity.getId().getDriverId());
				dto.setLatitude(entity.getLatitude());
				dto.setLongitude(entity.getLongitude());
				return dto;
			} else {
				logger.error("Function getByBookcarOrderByCreatedAtDesc: Driver Info was null");
			}
		}
		return null;
	}

	/**
	 * Update state
	 */
	@Override
	public void reject(CallLogDTO dto) {
		// TODO Auto-generated method stub
		if (!StringUtils.isBlank(dto.getDriverId()) && dto.getBookcarId() != null) {
			CallLogId id = new CallLogId(dto.getDriverId(), dto.getBookcarId());
			CallLog entity = callLogRepository.findOne(id);
			if (entity != null) {
				entity.setState(DriverType.CallLogState.REJECTED.getId());
				callLogRepository.save(entity);
			}
		}
	}

	/**
	 * Update state
	 */
	@Transactional(readOnly = false)
	@Override
	public void approve(CallLogDTO dto) {
		BookCar bookCar = bookCarRepository.findOne(dto.getBookcarId());
		CallLogId callLogId = new CallLogId(dto.getDriverId(), dto.getBookcarId());
		CallLog callLog = callLogRepository.findOne(callLogId);
		if (bookCar != null && callLog != null) {
			// Book-car.
			bookCar.setState(dto.getBookCarState());
			bookCar.setUpdatedAt(DateUtils.getCurrentDate());
			bookCar = bookCarRepository.save(bookCar);
			// Call-Log.
			callLog.setState(DriverType.CallLogState.APPROVED.getId());
			callLogRepository.save(callLog);
		} else {
			logger.error("Function approve: Bookcar or call-log was null");
		}
	}

}
