package vn.com.omart.backend.application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.com.omart.backend.application.response.BookCarDTO;
import vn.com.omart.backend.application.response.PoiNotificationDTO;
import vn.com.omart.backend.application.response.PushNotificationTokenDTO;
import vn.com.omart.backend.application.response.RecruitmentDTO;
import vn.com.omart.backend.application.response.StoreProcedureParams;
import vn.com.omart.backend.application.util.CommonUtils;
import vn.com.omart.backend.constants.ConstantUtils;
import vn.com.omart.backend.constants.Device;
import vn.com.omart.backend.constants.OmartType;
import vn.com.omart.backend.constants.OmartType.NotificationType;
import vn.com.omart.backend.constants.OmartType.OrderSellerState;
import vn.com.omart.backend.domain.model.BookCar;
import vn.com.omart.backend.domain.model.BookCarRepository;
import vn.com.omart.backend.domain.model.Category;
import vn.com.omart.backend.domain.model.CategoryFollowRepository;
import vn.com.omart.backend.domain.model.Image;
import vn.com.omart.backend.domain.model.Order;
import vn.com.omart.backend.domain.model.PoiAction;
import vn.com.omart.backend.domain.model.PoiActionRepository;
import vn.com.omart.backend.domain.model.PoiNotification;
import vn.com.omart.backend.domain.model.PoiNotificationRepository;
import vn.com.omart.backend.domain.model.PointOfInterest;
import vn.com.omart.backend.domain.model.PointOfInterestRepository;
import vn.com.omart.backend.domain.model.PushNotificationToken;
import vn.com.omart.backend.domain.model.PushNotificationTokenRepository;
import vn.com.omart.backend.domain.model.Recruitment;
import vn.com.omart.backend.domain.model.RecruitmentPositionLevel;
import vn.com.omart.backend.domain.model.RecruitmentRepository;
import vn.com.omart.backend.domain.model.UserCvNotification;
import vn.com.omart.backend.domain.model.UserCvNotificationRepository;
import vn.com.omart.backend.domain.model.UserFriendRequest;
import vn.com.omart.backend.port.adapter.support.DeviceGroupToken;

@Service
public class PoiNotificationService {

	private static final Logger logger = LoggerFactory.getLogger(PoiNotificationService.class);

	@Autowired
	private FcmClientService fcmClientService;

	@Autowired
	private PoiNotificationRepository poiNotificationRepository;

	@Autowired
	private PointOfInterestRepository pointOfInterestRepository;

	@Autowired
	private StoredProcedureService storedProcedureService;

	@Autowired
	private AppCommonService commonService;

	@Autowired
	private CategoryFollowRepository categoryFollowRepository;

	@Autowired
	private RecruitmentRepository recruitmentRepository;

	@Autowired
	private BookCarRepository bookCarRepository;

	@Autowired
	private PushNotificationTokenRepository pushNotificationTokenRepository;
	
	@Autowired
	private PushNotificationTokenNewService pushNotificationTokenNewService;

	@Value("${omart.vn.domain-url}")
	private String Omart_Domain_Url;
	
	@Autowired
	private PoiActionRepository poiActionRepository;

	/**
	 * Save
	 *
	 * @param userId
	 * @param dto
	 * @return
	 */
	@Transactional(readOnly = false)
	public PoiNotification save(String userId, PoiNotificationDTO dto) {
		PoiNotification entity = PoiNotificationDTO.toEntity(dto);
		PointOfInterest poi = pointOfInterestRepository.findOne(dto.getPoiId());
		if (poi != null) {
			entity.setPoi(poi);
			entity.setLatitude(poi.latitude());
			entity.setLongitude(poi.longitude());
			entity.setCategory(poi.category());
			entity.setUserId(userId);
			entity.setName(poi.getName());
			entity.setAvatarImages(poi.getAvatarImage());
			entity.setAddress(commonService.getPoiFullAddress(poi));
			entity.setPhone(poi.getPhone());
			entity.setActive(true);
			entity = poiNotificationRepository.save(entity);
		}
		return entity;
	}

	/**
	 * Save
	 *
	 * @param userId
	 * @param dto
	 * @param category
	 * @return
	 */
	@Transactional(readOnly = false)
	public PoiNotification save(String userId, PoiNotificationDTO dto, Category category) {
		PoiNotification entity = PoiNotificationDTO.toEntity(dto);
		PointOfInterest poi = pointOfInterestRepository.findOne(dto.getPoiId());
		if (poi != null) {
			entity.setPoi(poi);
			entity.setLatitude(poi.latitude());
			entity.setLongitude(poi.longitude());
			entity.setCategory(category);
			entity.setUserId(userId);
			entity.setName(poi.getName());
			entity.setAvatarImages(poi.getAvatarImage());
			entity.setAddress(commonService.getPoiFullAddress(poi));
			entity.setPhone(poi.getPhone());
			entity.setActive(true);
			Recruitment recruit = recruitmentRepository.findOne(dto.getRecruit().getId());
			if (recruit != null) {
				entity.setRecruit(recruit);
			}

			entity = poiNotificationRepository.save(entity);
		}
		return entity;
	}

	/**
	 * Save for Position
	 *
	 * @param userId
	 * @param dto
	 * @param position
	 * @return
	 */
	@Transactional(readOnly = false)
	public PoiNotification save(String userId, PoiNotificationDTO dto, RecruitmentPositionLevel positionLevel) {
		PoiNotification entity = PoiNotificationDTO.toEntity(dto);
		PointOfInterest poi = pointOfInterestRepository.findOne(dto.getPoiId());
		if (poi != null) {
			entity.setPoi(poi);
			entity.setLatitude(poi.latitude());
			entity.setLongitude(poi.longitude());
			entity.setCategory(poi.category());
			entity.setRecruitmentPositionLevel(positionLevel);
			entity.setNotificationType(NotificationType.RECRUIT.getId());
			entity.setUserId(userId);
			entity.setName(poi.getName());
			entity.setAvatarImages(poi.getAvatarImage());
			entity.setAddress(commonService.getPoiFullAddress(poi));
			entity.setPhone(poi.getPhone());
			entity.setActive(true);
			Recruitment recruit = recruitmentRepository.findOne(dto.getRecruit().getId());
			if (recruit != null) {
				entity.setRecruit(recruit);
			}

			entity = poiNotificationRepository.save(entity);
		}
		return entity;
	}

	/**
	 * Poi Push Notification
	 *
	 * @param userId
	 * @param dto
	 */
	public void poiPushNotification(String userId, PoiNotificationDTO dto) {
		// step 1 : save PoiNotification.
		PoiNotification entity = this.save(userId, dto);

		// step 2: send notification.
		this.poiPushNotification(entity);
	}

	/**
	 * Poi Push Notification
	 *
	 * @param entity
	 */
	public void poiPushNotification(PoiNotification entity) {
		// step 1: retrieve user are following on the category.
		// 0 is get ALL
		String provinces = "0," + entity.getPoi().province().id();
		String districts = "0," + entity.getPoi().district().id();
		
		DeviceGroupToken tokens = null;
		if (entity.getRecruit() != null) {
			 tokens = pushNotificationTokenNewService.getTokenByPosition(entity.getUserId(), entity.getRecruitmentPositionLevel().getId(), provinces, districts);
		} else {
			List<Object[]> objTokens = categoryFollowRepository.getByCategory(entity.getCategory().id(), provinces,
					districts);
			tokens = pushNotificationTokenNewService.getDeviceGroupTokensByObject(entity.getUserId(), objTokens);
		}
		// merge user following.
		DeviceGroupToken allTokens = pushNotificationTokenNewService.getTokenBasePoiAction(tokens, entity.getUserId(), entity.getPoi().id());
		// end
		this.poiPushNotification(entity, allTokens);
	}

	/**
	 * Push notification (new).
	 * @param entity
	 * @param tokens
	 */
	public void poiPushNotification(PoiNotification entity, DeviceGroupToken tokens) {
		// set data.
		String desc = "", body = "", userId = entity.getUserId();
		if (entity.getDescription() != null) {
			desc = CommonUtils.getTextByWordLimit(entity.getDescription(), 200);
		}
		// checking if re-push from recruit.
		if (entity.getRecruit() == null) {
			body = String.format(ConstantUtils.POI_NOTIFICATION_PUSH_BODY, entity.getPoi().province().name(), desc);
		} else {
			body = desc;
		}
		// set infor.
		String title = String.format(ConstantUtils.POI_NOTIFICATION_PUSH_TITLE, entity.getPoi().name(),
				entity.getCategory().name());
		Map<String, String> data = new HashMap<String, String>();
		data.put("type", "2");
		data.put("id", entity.getId().toString());
		data.put("userId", userId);
		data.put("payload", body);
		data.put("poiId", entity.getPoi().id().toString());
		data.put("poiName", entity.getPoi().name());
		data.put("title", title);
		data.put("description", body);
		if (entity.getRecruit() != null) {
			data.put("recruitmentId", entity.getRecruit().getId().toString());
			data.put("positionName", entity.getRecruit().getRecruitmentPositionLevel().getName() + " "
					+ entity.getRecruit().getRecruitmentPosition().getName());
		} else {
			data.put("catName", entity.getCategory().name());
		}
		// send data.
		pushNotificationTokenNewService.sendNotification(title, "", body, data, tokens);
	}
	
	/**
	 * Send for Recruitment
	 *
	 * @param userId
	 * @param body
	 * @param data
	 * @param postId
	 */
	public void send(Recruitment recruitment, String userId, String title, String body, Map<String, String> data,
			Long posId) {
		// 0 is get ALL
		String provinces = "0," + recruitment.getPoi().province().id();
		String districts = "0," + recruitment.getPoi().district().id();
	 	DeviceGroupToken tokens = pushNotificationTokenNewService.getTokenByPosition(userId, posId, provinces, districts);
	 	pushNotificationTokenNewService.sendNotification(title, "", body, data, tokens);
	}

	/**
	 * Send for User Cv Notification.
	 *
	 * @param userCvNotification
	 */
	public void send(List<UserCvNotification> userCvNotifications) {
		List<String> userIdList = userCvNotifications.stream().map(item -> item.getUserId())
				.collect(Collectors.toList());
		DeviceGroupToken tokens = pushNotificationTokenNewService.getByInUserIds(userCvNotifications.get(0).getPoi().ownerId(), userIdList);
		if(tokens!=null) {
			String title = userCvNotifications.get(0).getTitle();
			String body = "Từ " + userCvNotifications.get(0).getPoi().getName();
			Map<String, String> data = new HashMap<String, String>();
			data.put("type", "3");
			data.put("id", userCvNotifications.get(0).getId().toString());
			data.put("title", title);
			data.put("description", body);
			pushNotificationTokenNewService.sendNotification(title, "", body, data, tokens);
		}
	}

	/**
	 * Get poi notification latest 10 items.
	 *
	 * @param notiTypes
	 * @param pageable
	 * @return
	 */
	public List<PoiNotificationDTO> getPoiPushNotification(int[] notiTypes, Pageable pageable) {
		List<PoiNotification> entities = poiNotificationRepository
				.getAllByIsDeletedAndNotificationTypeOrderByUpdatedAtDesc(false, notiTypes, pageable);
		List<PoiNotificationDTO> poiPushNotifications = entities.stream().map(PoiNotificationDTO::toDTO)
				.collect(Collectors.toList());
		return poiPushNotifications;
	}

	/**
	 * Get Poi Push Notification.
	 *
	 * @param userId
	 * @return List of PoiNotificationDTO
	 */
	public List<PoiNotificationDTO> getPoiPushNotification(StoreProcedureParams params, Pageable pageable) {
		if (params.getLatitude() > 0 && params.getLongitude() > 0) {
			List<Object[]> entityList = storedProcedureService
					.getPoiNotificationStoredProcedureQuery("geodist_poi_notification_v1", params);
			List<PoiNotificationDTO> dtoList = entityList.stream().map(this::toDTO).collect(Collectors.toList());
			return dtoList;
		}
		PageRequest pageRequest = new PageRequest(params.getPage(), params.getSize());
		// no location.
		int[] notiTypes = { OmartType.NotificationType.SYS.getId(), OmartType.NotificationType.POI.getId(),
				OmartType.NotificationType.RECRUIT.getId() };
		return this.getPoiPushNotification(notiTypes, pageRequest);
	}

	/**
	 * Get Poi Push Notification V2.
	 *
	 * @param userId
	 * @return List of PoiNotificationDTO
	 */
	public List<PoiNotificationDTO> getPoiPushNotificationV2(String userId, StoreProcedureParams params, Pageable pageable) {
		
		String provinceDistrictSelectedStr = params.getProvinceDistrictSelectedStr(), districtIds = "ALL";
		int provinceId = 0;
		provinceDistrictSelectedStr = provinceDistrictSelectedStr.replace(" ", "");
		if (!provinceDistrictSelectedStr.isEmpty()) {
			int index = provinceDistrictSelectedStr.indexOf(",");
			if (index != -1) {
				provinceId = Integer.parseInt(provinceDistrictSelectedStr.substring(0, index));
				districtIds = provinceDistrictSelectedStr.substring(index + 1, provinceDistrictSelectedStr.length());
			} else {
				provinceId = Integer.parseInt(provinceDistrictSelectedStr);
			}
		}
//		String poiIds = "";
//		if(!userId.isEmpty()) {
//			List<PoiAction> poiActions = poiActionRepository.findByUserIdAndActionType(userId, 1);
//			if(!poiActions.isEmpty()) {
//				for(PoiAction item: poiActions) {
//					poiIds+=","+item.getPoi().id();
//				}
//				poiIds = poiIds.replaceFirst(",", "");
//			}
//		}
		params.setUserId(userId);
		List<Object[]> entityList = storedProcedureService.getPoiNotificationV2("geodist_poi_notification_v2",
				provinceId, districtIds, params);
		List<PoiNotificationDTO> dtoList = entityList.stream().map(this::toDTO).collect(Collectors.toList());
		return dtoList;
	}

	/**
	 * Get Poi Push Notification V4.
	 *
	 * @param userId
	 * @return List of PoiNotificationDTO
	 */
	public List<PoiNotificationDTO> getPoiPushNotificationV4(StoreProcedureParams params, String userId,
			Pageable pageable) {
		String provinceDistrictSelectedStr = params.getProvinceDistrictSelectedStr(), districtIds = "ALL";
		int provinceId = 0;
		provinceDistrictSelectedStr = provinceDistrictSelectedStr.replace(" ", "");
		if (!provinceDistrictSelectedStr.isEmpty()) {
			int index = provinceDistrictSelectedStr.indexOf(",");
			if (index != -1) {
				provinceId = Integer.parseInt(provinceDistrictSelectedStr.substring(0, index));
				districtIds = provinceDistrictSelectedStr.substring(index + 1, provinceDistrictSelectedStr.length());
			} else {
				provinceId = Integer.parseInt(provinceDistrictSelectedStr);
			}
		}
		List<Object[]> entityList = storedProcedureService.getPoiNotificationV4("geodist_poi_notification_v4",
				provinceId, districtIds, params, userId);
		List<PoiNotificationDTO> dtoList = entityList.stream().map(this::toDTO).collect(Collectors.toList());
		return dtoList;
	}

	/**
	 * To DTO.
	 *
	 * @param objs
	 * @return PoiNotificationDTO
	 */
	private PoiNotificationDTO toDTO(Object[] objs) {
		PoiNotificationDTO dto = PoiNotificationDTO.toDTO(objs);
		if (dto.getRecruit() != null) {
			Recruitment recruite = recruitmentRepository.findOne(dto.getRecruit().getId());
			if (recruite != null) {
				RecruitmentDTO recruitmentDTO = RecruitmentDTO.toDTO(recruite);
				dto.setRecruit(recruitmentDTO);
			}
		}

		if (dto.getBookcar() != null) {
			BookCar bookcar = bookCarRepository.findOne(dto.getBookcar().getId());
			if (bookcar != null) {
				BookCarDTO boocarDTO = BookCarDTO.toDTO(bookcar);
				dto.setBookcar(boocarDTO);
			}
		}
		return dto;
	}

	/**
	 * Get Poi Notification.
	 *
	 * @param id
	 * @return
	 */
	public PoiNotificationDTO getPoiNotificationById(Long id) {
		PoiNotification entity = poiNotificationRepository.findOne(id);
		if (entity != null) {
			PoiNotificationDTO dto = PoiNotificationDTO.toDTO(entity);
			return dto;
		}
		return null;
	}

	/**
	 * Get Poi Notification By Poi.
	 *
	 * @param poiId
	 * @param pageable
	 * @return List of PoiNotificationDTO
	 */
	public List<PoiNotificationDTO> getPoiNotificationByPoiId(Long poiId, Pageable pageable) {
		PointOfInterest poi = pointOfInterestRepository.findOne(poiId);
		if (poi != null) {
			List<PoiNotification> entities = poiNotificationRepository
					.findAllByPoiAndIsDeletedAndNotificationTypeOrderByCreatedAtDesc(poi, false,
							NotificationType.POI.getId(), pageable);
			if (!entities.isEmpty()) {
				List<PoiNotificationDTO> dtoList = entities.stream().map(PoiNotificationDTO::toDTO)
						.collect(Collectors.toList());
				return dtoList;
			}
		}
		return new ArrayList<>();
	}

	/**
	 * Delete.
	 *
	 * @param id
	 */
	public void delete(Long id) {
		PoiNotification entity = poiNotificationRepository.findOne(id);
		if (entity != null) {
			entity.setDeleted(true);
			entity.setActive(false);
			poiNotificationRepository.save(entity);
		}
	}

	/**
	 * Update
	 *
	 * @param id
	 * @param dto
	 */
	public void update(Long id, PoiNotificationDTO dto) {
		PoiNotification entity = poiNotificationRepository.findOne(id);
		if (entity != null) {
			entity = PoiNotificationDTO.mergeToEntity(entity, dto);
			poiNotificationRepository.save(entity);
		}
	}

	/**
	 * Re-Push Poi Notification.
	 *
	 * @param id
	 */
	public void rePushNotification(Long id) {
		PoiNotification entity = poiNotificationRepository.findOne(id);
		if (entity != null) {
			entity.setUpdatedAt(new Date());
			entity = poiNotificationRepository.save(entity);
			this.poiPushNotification(entity);
		}
	}
	
	/**
	 * Send Noti Order To Seller
	 * @param order
	 */
	public void sendNotiOrderToSeller(Order order) {
		String title = "", desc = "", body = "", ownerId = order.getPoi().ownerId();
		title = "Bạn nhận được đơn hàng mới ";
		long total = (long) order.getTotal();
		body = "Số lượng: " + order.getQuantity() + " - Tổng tiền: " + CommonUtils.getShortPrice(total);
		List<Image> imgs = order.getPoi().getAvatarImage();
		Map<String, String> data = new HashMap<String, String>();
		data.put("orderId", order.getId().toString());
		data.put("poiId", String.valueOf(order.getPoi().id()));
		data.put("username", order.getUserProfile().getName());
		data.put("avatar", order.getUserProfile().getAvatar());
		data.put("quantity", String.valueOf(order.getQuantity()));
		data.put("cash", String.valueOf(order.getTotal()));
		data.put("domain", Omart_Domain_Url);
		data.put("shopAvatar", imgs.size() > 0 ? imgs.get(0).getUrl() : "");
		data.put("sound", String.valueOf(order.getPoi().getRingIndex()));
		data.put("type", "5");
		data.put("title", title);
		data.put("description", body);
		String clickAction = Omart_Domain_Url + "/myshop/" + order.getPoi().id() + "?page=1&tab=1";
		
//		PushNotificationToken token = pushNotificationTokenRepository.findByUserIdAndClient(ownerId,Device.web.getId());
//		if (token != null) {
//			this.send(order.getUserId(), title, clickAction, body, data, Arrays.asList(token));
//		}
		DeviceGroupToken tokens = pushNotificationTokenNewService.getTokenByUserId(order.getUserId(),ownerId);
		pushNotificationTokenNewService.sendNotification(title, clickAction, body, data, tokens);
	}
	
	/**
	 * Send Noti Order To Buyer.
	 * @param order
	 */
	public void sendNotiOrderToBuyer(Order order) {
		String title = "", desc = "", body = "", ownerId = order.getPoi().ownerId();
		if(order.getSellerState() == OrderSellerState.COMPLETED.getId()) {
			title = "Bạn đã hoàn thành đơn hàng "+order.getPurchaseOrderId();
			body = "Bạn được thưởng "+order.getCoinExtra() + " coin";
		}else {
			title = "Đơn hàng " + order.getPurchaseOrderId();
			String state = OrderSellerState.getById(order.getSellerState()).getLabelVI();
			body = state; 
		}
		List<Image> imgs = order.getPoi() != null ? order.getPoi().getAvatarImage() : null;
		Map<String, String> data = new HashMap<String, String>();
		data.put("orderId", order.getId().toString());
		data.put("title", title);
		data.put("description", body);
		data.put("type", "6");
		data.put("shopName", order.getPoi().name());
		data.put("shopAvatar", imgs != null && imgs.size() > 0 ? imgs.get(0).getUrl() : "");
		data.put("orderState", String.valueOf(OrderSellerState.getById(order.getSellerState())));
		data.put("orderReason", order.getSellerStateReason() !=  null ? order.getSellerStateReason() : "");
		data.put("coinExtra",String.valueOf(order.getCoinExtra()));
		DeviceGroupToken tokens = pushNotificationTokenNewService.getTokenByUserId(ownerId,order.getUserId());
		pushNotificationTokenNewService.sendNotification(title, "", body, data, tokens);
	}
	
	/**
	 * Send requesting make friend.
	 * @param friendRequest
	 */
	public void sendFriendRequest( UserFriendRequest friendRequest) {
		String ownerId = friendRequest.getSender().getUserId();
		String title = "Lời mời kết bạn";
		String body = friendRequest.getSender().getName() + " muốn kết bạn với bạn";
		
		Map<String, String> data = new HashMap<String, String>();
		data.put("title", title);
		data.put("description",body);
		data.put("type","7");
		data.put("userId", ownerId);
		DeviceGroupToken tokens = pushNotificationTokenNewService.getTokenByUserId(ownerId,friendRequest.getRecipient().getUserId());
		pushNotificationTokenNewService.sendNotification(title, "", body, data, tokens);
	}
	
	/**
	 * Send accept make friend.
	 * @param friendRequest
	 */
	public void sendFriendAccept(UserFriendRequest friendRequest) {
		String ownerId = friendRequest.getRecipient().getUserId();
		String body = friendRequest.getRecipient().getName() + " đã đồng ý kết bạn";
		String title = "Đồng ý kết bạn";
		
		Map<String, String> data = new HashMap<String, String>();
		data.put("title", title);
		data.put("description",body);
		data.put("type","8");
		data.put("userId", ownerId);
		DeviceGroupToken tokens = pushNotificationTokenNewService.getTokenByUserId(ownerId,friendRequest.getSender().getUserId());
		pushNotificationTokenNewService.sendNotification(title, "", body, data, tokens);
	}

	/**
	 * UPDATE COPY CREATED_AT TO UPDATED_AT IN DB. THIS FUNCTION ONLY FOR DEVELOPER
	 */
	public void setUpdatedAt() {
		List<PoiNotification> entities = poiNotificationRepository.findAllByUpdatedAtIsNull();
		if (!entities.isEmpty()) {
			entities.forEach(poi -> {
				if (poi.getUpdatedAt() == null) {
					poi.setUpdatedAt(poi.getCreatedAt());
				}
			});
			poiNotificationRepository.save(entities);
		}
	}

}
