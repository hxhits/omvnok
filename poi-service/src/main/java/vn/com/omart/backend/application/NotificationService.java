package vn.com.omart.backend.application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ser.ArraySerializers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import vn.com.omart.backend.application.response.NotificationDTO;
import vn.com.omart.backend.application.util.DateUtils;
import vn.com.omart.backend.constants.ConstantUtils;
import vn.com.omart.backend.constants.OmartType.AppType;
import vn.com.omart.backend.domain.model.DriverPushToken;
import vn.com.omart.backend.domain.model.DriverPushTokenRepository;
import vn.com.omart.backend.domain.model.Notification;
import vn.com.omart.backend.domain.model.NotificationRepository;
import vn.com.omart.backend.domain.model.PushNotificationToken;
import vn.com.omart.backend.domain.model.PushNotificationTokenNew;
import vn.com.omart.backend.domain.model.PushNotificationTokenNewRepository;
import vn.com.omart.backend.domain.model.PushNotificationTokenRepository;
import vn.com.omart.backend.port.adapter.support.DeviceGroupToken;

@Service
public class NotificationService {

	@Autowired
	private NotificationRepository notificationRepository;

	@Autowired
	private FcmClientService fcmClientService;

	@Autowired
	private FcmClientDriverService fcmClientDriverService;

	@Autowired
	private PushNotificationTokenNewService pushNotificationTokenNewService;

	@Autowired
	private DriverPushTokenRepository driverPushTokenRepository;

	/**
	 * Create notification.
	 * 
	 * @param userId
	 * @param dto
	 */
	public void save(String userId, NotificationDTO dto) {
		Notification entity = NotificationDTO.toEntity(dto);
		entity.setCreatedAt(DateUtils.getCurrentDate());
		entity.setCreatedBy(userId);
		int viewType = 0; // TODO
		entity.setViewType(viewType);
		entity = notificationRepository.save(entity);

		if (dto.getAppType() == AppType.DRIVER) {
			this.driverSendNotification(entity.getRecipientId(), entity);
		} else if (dto.getAppType() == AppType.OMART) {
			this.omartSendNotification(entity.getRecipientId(), entity);
		}

	}

	/**
	 * Omart send notification
	 * 
	 * @param userId
	 * @param notification
	 * @param viewType
	 */
	public void omartSendNotification(String userId, Notification notification) {
		DeviceGroupToken tokens = pushNotificationTokenNewService.getTokenByUserId("", userId);
		if (tokens != null) {
			String title = ConstantUtils.PUSH_TITLE, body = notification.getTitle();
			Map<String, String> data = new HashMap<String, String>();
			data.put("type", "4");
			data.put("title", title);
			data.put("description", body);
			data.put("notificationId", notification.getId().toString());
			pushNotificationTokenNewService.sendNotification(title, "", body, data, tokens);
		}
	}

	// public void omartSendNotification(String userId, Notification notification) {
	// PushNotificationToken token =
	// pushNotificationTokenRepository.findOne(userId);
	// if (token != null) {
	// String title = ConstantUtils.PUSH_TITLE, body = notification.getTitle();
	// Map<String, String> data = new HashMap<String, String>();
	// data.put("type", "4");
	// data.put("title", title);
	// data.put("description", body);
	// data.put("notificationId", notification.getId().toString());
	// fcmClientService.send(token, title, body, "", data);
	// }
	// }

	/**
	 * Driver send notification
	 * 
	 * @param userId
	 * @param notification
	 * @param viewType
	 */
	public void driverSendNotification(String userId, Notification notification) {
		DeviceGroupToken tokens = pushNotificationTokenNewService.getTokenByUserId("", userId);
		String title = ConstantUtils.PUSH_TITLE, body = notification.getTitle();
		Map<String, String> data = new HashMap<String, String>();
		data.put("type", "4");
		data.put("title", title);
		data.put("description", body);
		data.put("notificationId", notification.getId().toString());
		pushNotificationTokenNewService.sendNotificationToDriver(title, "", body, data, tokens);
	}

	// public void driverSendNotification(String userId, Notification notification)
	// {
	// DriverPushToken token = driverPushTokenRepository.findOne(userId);
	// if (token != null) {
	// String title = ConstantUtils.PUSH_TITLE, body = notification.getTitle();
	// Map<String, String> data = new HashMap<String, String>();
	// data.put("type", "4");
	// data.put("title", title);
	// data.put("description", body);
	// data.put("notificationId", notification.getId().toString());
	// fcmClientDriverService.send(token, title, body, "", data);
	// }
	// }

	/**
	 * Get notification.
	 * 
	 * @param id
	 * @return NotificationDTO
	 */
	public NotificationDTO getNotification(Long id) {
		Notification entity = notificationRepository.findOne(id);
		NotificationDTO dto = new NotificationDTO();
		if (entity != null) {
			dto = NotificationDTO.toBasicDTO(entity);
		}
		return dto;
	}

	public List<NotificationDTO> getAllNotification(String userId, Pageable pageable) {
		if(StringUtils.isNotBlank(userId)) {
			//Page<Notification> pages = notificationRepository.findAll(pageable);
			List<Notification> entities = notificationRepository.findByRecipientId(userId, pageable);
			List<NotificationDTO> dtos = new ArrayList<>();
			if (!entities.isEmpty()) {
				dtos = entities.stream().map(NotificationDTO::toBasicDTO).collect(Collectors.toList());
			}
			return dtos;
		}
		return new ArrayList<>();
		
	}
}
