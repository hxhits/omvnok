package vn.com.omart.backend.application;

import java.util.ArrayList;
import java.util.Calendar;
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

import vn.com.omart.backend.application.response.BookCarDTO;
import vn.com.omart.backend.application.response.BookOrderDTO;
import vn.com.omart.backend.application.response.DelivererDTO;
import vn.com.omart.backend.application.response.OrderDTO;
import vn.com.omart.backend.application.response.OrderPageDTO;
import vn.com.omart.backend.application.response.OrderReportDTO;
import vn.com.omart.backend.application.response.ProvinceDTO;
import vn.com.omart.backend.application.response.UserProfileDTO;
import vn.com.omart.backend.application.util.CommonUtils;
import vn.com.omart.backend.application.util.DateUtils;
import vn.com.omart.backend.constants.OmartType;
import vn.com.omart.backend.constants.OmartType.OrderSellerState;
import vn.com.omart.backend.domain.model.DriverInfo;
import vn.com.omart.backend.domain.model.DriverInfoRepository;
import vn.com.omart.backend.domain.model.Order;
import vn.com.omart.backend.domain.model.OrderDetail;
import vn.com.omart.backend.domain.model.OrderRepository;
import vn.com.omart.backend.domain.model.PointOfInterest;
import vn.com.omart.backend.domain.model.PointOfInterestRepository;
import vn.com.omart.backend.domain.model.UserProfile;
import vn.com.omart.backend.domain.model.UserProfileRepository;
import vn.com.omart.sharedkernel.application.model.error.NotFoundException;

@Service
public class OrderService {

	private Logger logger = LoggerFactory.getLogger(OrderService.class);

	@Autowired
	private DriverInfoRepository driverInfoRepository;

	@Autowired
	private RestTemplate restTemplate;

	@Value("${driver.order.booking}")
	private String Book_Order;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private OrderDetailService orderDetailService;

	@Autowired
	private PointOfInterestRepository pointOfInterestRepository;

	@Autowired
	private UserProfileRepository userProfileRepository;

	@Autowired
	private PoiNotificationService poiNotificationService;

	@Autowired
	private DelivererService delivererService;

	@Autowired
	private ReportAbuseService reportAbuseService;

	/**
	 * Create order
	 * 
	 * @param userId
	 * @param location
	 * @param dto
	 */
	@Transactional(readOnly = false)
	public void save(String userId, String location, OrderDTO dto) {
		PointOfInterest poi = pointOfInterestRepository.findOne(dto.getPoiId());
		UserProfile userProfile = userProfileRepository.findByUserId(userId);
		if (poi != null && userProfile != null) {
			Order order = OrderDTO.toEntity(dto);
			if (StringUtils.isNotBlank(location)) {
				String[] locations = location.split(",");
				order.setUserLatitude(Double.valueOf(locations[0].trim()));
				order.setUserLongitude(Double.valueOf(locations[1].trim()));
			}
			order.setUserProfile(userProfile);
			order.setUserId(userId);
			order.setPoi(poi);
			order.setPurchaseOrderId(CommonUtils.getPurchaseOrderID());
			order.setCreatedAt(DateUtils.getCurrentDate());
			order.setUpdatedAt(DateUtils.getCurrentDate());
			List<OrderDetail> orderDetails = order.getOrderDetails();
			order = orderRepository.save(order);
			if (order != null) {
				orderDetailService.save(orderDetails, order);
				poiNotificationService.sendNotiOrderToSeller(order);
			}
		} else {
			logger.error("\n[CREATE ORDER] cannot save because PointOfInterest/UserProfile not found");
		}
	}

	/**
	 * Get order by userId.
	 * 
	 * @param userId
	 * @param pageable
	 * @return List of OrderDTO
	 */
	public List<OrderDTO> getOrderByUserId(String userId, Pageable pageable) {
		UserProfile userProfile = userProfileRepository.findByUserId(userId);
		if (userProfile != null) {
			List<Order> entities = orderRepository.findByUserProfileAndIsDeleted(userProfile, false, pageable);
			if (!entities.isEmpty()) {
				List<OrderDTO> orderDTOs = entities.stream().map(OrderDTO::toBasicDTO).collect(Collectors.toList());
				return orderDTOs;
			}
		}
		return new ArrayList<>();
	}

	/**
	 * Get order by userId and poiId and isDeleted
	 * 
	 * @param userId
	 * @param poiId
	 * @param pageable
	 * @return List of OrderDTO
	 */
	public List<OrderDTO> getOrderByUserIdAndPoiId(String userId, Long poiId, Pageable pageable) {
		UserProfile userProfile = userProfileRepository.findByUserId(userId);
		if (userProfile != null) {
			PointOfInterest poi = pointOfInterestRepository.findOne(poiId);
			if (poi != null) {
				List<Order> entities = orderRepository.findByUserProfileAndPoiAndIsDeleted(userProfile, poi, false,
						pageable);
				if (!entities.isEmpty()) {
					List<OrderDTO> orderDTOs = entities.stream().map(OrderDTO::toBasicDTO).collect(Collectors.toList());
					return orderDTOs;
				}
			}
		}
		return new ArrayList<>();
	}

	/**
	 * Get Order By PoiId.
	 * 
	 * @param poiId
	 * @param pageable
	 * @return List of OrderDTO
	 */
	public List<OrderDTO> getOrderByPoiId(Long poiId, Pageable pageable) {
		PointOfInterest poi = pointOfInterestRepository.findOne(poiId);
		if (poi != null) {
			List<Order> entities = orderRepository.findAllByPoiAndIsDeletedOrderByCreatedAtDesc(poi, false, pageable);
			if (!entities.isEmpty()) {
				List<OrderDTO> orderDTOs = entities.stream().map(OrderDTO::toFullDTO).collect(Collectors.toList());
				return orderDTOs;
			}
		}
		return new ArrayList<>();
	}

	/**
	 * Get Orders By PoiId.
	 * 
	 * @param poiId
	 * @param pageable
	 * @return OrderPageDTO
	 */
	public OrderPageDTO getOrdersByPoiId(Long poiId, Pageable pageable) {
		OrderPageDTO result = new OrderPageDTO();
		PointOfInterest poi = pointOfInterestRepository.findOne(poiId);
		if (poi != null) {
			Page<Order> entities = orderRepository.getAllByPoiAndIsDeletedOrderByCreatedAtDesc(poi, false, pageable);
			List<OrderDTO> orderDTOs = entities.getContent().stream().map(OrderDTO::toFullDTO)
					.collect(Collectors.toList());
			result.setOrders(orderDTOs);
			result.setSize(entities.getTotalElements());
		}

		return result;
	}

	/**
	 * Get Order Report By PoiId.
	 * 
	 * @param poiId
	 * @return OrderReportDTO
	 */
	public OrderReportDTO getOrderReportByPoiId(Long poiId) {
		OrderReportDTO result = new OrderReportDTO();

		// ALL REPORT
		List<Object[]> allRptData = orderRepository.getAllReportByPoi(poiId);
		Long allOrder = 0L;
		Long newOrder = 0L;
		Long doneOrder = 0L;
		for (Object[] item : allRptData) {
			allOrder += Long.parseLong(String.valueOf(item[1]));
			if (OrderSellerState.WAITING.getId() == Integer.parseInt(String.valueOf(item[0]))) {
				newOrder = Long.parseLong(String.valueOf(item[1]));
			} else if (OrderSellerState.COMPLETED.getId() == Integer.parseInt(String.valueOf(item[0]))) {
				doneOrder = Long.parseLong(String.valueOf(item[1]));
			}
		}
		result.setAllOrderAll(allOrder);
		result.setNewOrderAll(newOrder);
		result.setDoneOrderAll(doneOrder);

		// MONTH REPORT
		Calendar now = Calendar.getInstance();
		List<Object[]> monthRptData = orderRepository.getMonthReportByPoi(poiId, now.get(Calendar.YEAR),
				now.get(Calendar.MONTH) + 1);
		allOrder = 0L;
		newOrder = 0L;
		doneOrder = 0L;
		for (Object[] item : monthRptData) {
			allOrder += Long.parseLong(String.valueOf(item[1]));
			if (OrderSellerState.WAITING.getId() == Integer.parseInt(String.valueOf(item[0]))) {
				newOrder = Long.parseLong(String.valueOf(item[1]));
			} else if (OrderSellerState.COMPLETED.getId() == Integer.parseInt(String.valueOf(item[0]))) {
				doneOrder = Long.parseLong(String.valueOf(item[1]));
			}
		}
		result.setAllOrderMonth(allOrder);
		result.setNewOrderMonth(newOrder);
		result.setDoneOrderMonth(doneOrder);

		// DAY REPORT
		List<Object[]> dayRptData = orderRepository.getDateReportByPoi(poiId, now.get(Calendar.YEAR),
				now.get(Calendar.MONTH) + 1, now.get(Calendar.DATE));
		allOrder = 0L;
		newOrder = 0L;
		doneOrder = 0L;
		for (Object[] item : dayRptData) {
			allOrder += Long.parseLong(String.valueOf(item[1]));
			if (OrderSellerState.WAITING.getId() == Integer.parseInt(String.valueOf(item[0]))) {
				newOrder = Long.parseLong(String.valueOf(item[1]));
			} else if (OrderSellerState.COMPLETED.getId() == Integer.parseInt(String.valueOf(item[0]))) {
				doneOrder = Long.parseLong(String.valueOf(item[1]));
			}
		}
		result.setAllOrderDate(allOrder);
		result.setNewOrderDate(newOrder);
		result.setDoneOrderDate(doneOrder);

		return result;
	}

	/**
	 * Get Order By orderId
	 * 
	 * @param orderId
	 * @param pageable
	 * @return
	 */
	public OrderDTO getOrderByOderId(Long orderId, Pageable pageable) {
		Order order = orderRepository.findOne(orderId);
		OrderDTO orderDTO = null;
		if (order != null) {
			orderDTO = OrderDTO.toBasicDTOWithoutUserProfile(order);
			orderDTO.setUserCurrentAddress(order.getUserCurrentAddress());
			orderDTO.setReceiverName(order.getReceiverName());
			orderDTO.setReceiverPhone(order.getReceiverPhone());
			orderDTO.setCoinExtra(order.getCoinExtra());
			// user-id of orderer.
			UserProfileDTO userProfile = new UserProfileDTO();
			userProfile.setUserId(order.getUserId());
			orderDTO.setUserProfile(userProfile);
			return orderDTO;
		}
		return new OrderDTO();
	}

	/**
	 * Update seller state.
	 * 
	 * @param orderId
	 * @param sellerState
	 */
	public void updateState(Long orderId, OrderSellerState sellerState) {
		Order order = orderRepository.findOne(orderId);
		if (order != null) {
			order.setSellerState(sellerState.getId());
			orderRepository.save(order);
		}
	}

	@Autowired
	private CoinService coinService;
	
	/**
	 * Update seller state V1.
	 * 
	 * @param orderId
	 * @param sellerState
	 */
	public void updateStateV1(Long orderId, OrderDTO dto) {
		Order order = orderRepository.findOne(orderId);
		if (order != null) {
			order.setSellerState(dto.getSellerState().getId());
			if (StringUtils.isNotBlank(dto.getSellerStateReason())) {
				order.setSellerStateReason(dto.getSellerStateReason());
			}
			// set discount.
			if (dto.getSellerState() == OrderSellerState.COMPLETED && !order.isCalcDiscount()) {
				order.setCalcDiscount(true);
				UserProfile userProfile = order.getUserProfile();
				if (userProfile != null) {
					double discount = order.getPoi().getDiscount()/100d; // calculate by %. 
					double coinExtra = (order.getTotal() * discount / 200);
					int coin = (int) Math.round(coinExtra) + userProfile.getCoin();
					coinService.calculateMartCoin(userProfile,coin);
					order.setCoinExtra((int) Math.round(coinExtra));
				}
			}
			orderRepository.save(order);
			// send notification
			poiNotificationService.sendNotiOrderToBuyer(order);
		}
	}

	/**
	 * Delete.
	 * 
	 * @param orderId
	 */
	public void delete(Long orderId) {
		Order order = orderRepository.findOne(orderId);
		if (order != null) {
			order.setDeleted(true);
			orderRepository.save(order);
		}
	}

	/**
	 * Get order by order state.
	 * 
	 * @param poiId
	 * @param sellerState
	 * @param pageable
	 * @return List of OrderDTO
	 */
	public List<OrderDTO> getOrderByPoiIdAndIsDeletedAndSellerState(Long poiId, OrderSellerState sellerState,
			Pageable pageable) {
		PointOfInterest poi = pointOfInterestRepository.findOne(poiId);
		if (poi != null) {
			List<Order> entities = orderRepository.findAllByPoiAndIsDeletedAndSellerStateOrderByCreatedAtDesc(poi,
					false, sellerState.getId(), pageable);
			if (!entities.isEmpty()) {
				List<OrderDTO> orderDTOs = entities.stream().map(OrderDTO::toFullDTO).collect(Collectors.toList());
				return orderDTOs;
			}
		}
		return new ArrayList<>();
	}

	/**
	 * Get order by order state.
	 * 
	 * @param poiId
	 * @param sellerState
	 * @param pageable
	 * @return OrderPageDTO
	 */
	public OrderPageDTO getOrderByPoiIdAndIsDeletedAndSellerStateV1(long[] ids, OrderSellerState sellerState,
			Pageable pageable) {
		OrderPageDTO result = new OrderPageDTO();
		Page<Order> entities = orderRepository.getAllByPoiAndIsDeletedAndSellerStateOrderByCreatedAtDesc(ids,
				sellerState.getId(), pageable);
		List<OrderDTO> orderDTOs = entities.getContent().stream().map(OrderDTO::toFullDTO).collect(Collectors.toList());
		result.setOrders(orderDTOs);
		result.setSize(entities.getTotalElements());
		return result;
	}

	/**
	 * Get order by userId.
	 * 
	 * @param userId
	 * @param pageable
	 * @return List if OrderDTO
	 */
	public List<OrderDTO> getOrderByUserIdForWeb(String userId, Pageable pageable) {
		UserProfile userProfile = userProfileRepository.findByUserId(userId);
		if (userProfile != null) {
			List<Order> entities = orderRepository.findByUserProfileAndIsDeleted(userProfile, false, pageable);
			if (!entities.isEmpty()) {
				List<OrderDTO> orderDTOs = entities.stream().map(OrderDTO::toBasicDTOWithoutUserProfile)
						.collect(Collectors.toList());
				return orderDTOs;
			}
		}
		return new ArrayList<>();
	}

	/**
	 * Get order with users and total quantity.
	 * 
	 * @param userId
	 * @param pageable
	 * @param poiId
	 * @return List of OrderDTO
	 */
	public List<OrderDTO> getOrderByPoiAndSumQuantity(String userId, Pageable pageable, Long poiId) {
		reportAbuseService.getAll(pageable);
		PointOfInterest poi = pointOfInterestRepository.findOne(poiId);
		if (poi != null) {
			List<Order> entities = orderRepository.getOrderByPoiAndSumQuantity(poi, pageable);
			if (entities != null) {
				List<OrderDTO> dtos = entities.stream().map(entity -> {
					OrderDTO dto = new OrderDTO();
					UserProfileDTO userProfileDTO = UserProfileDTO.toBasicDTO(entity.getUserProfile());
					userProfileDTO.setProvince(ProvinceDTO.from(entity.getUserProfile().getProvince()));
					dto.setUserProfile(userProfileDTO);
					dto.setTotalQuantity(entity.getTotalQuantity());
					dto.setVisitDate(entity.getVisitDate() != null ? entity.getVisitDate().getTime() : 0);
					return dto;
				}).collect(Collectors.toList());
				return dtos;
			}
		}
		return new ArrayList<>();
	}

	/**
	 * Booking order deliverer.
	 * 
	 * @param userId
	 * @param dto
	 */
	public void lookingForDeliverer(String userId, BookOrderDTO dto) {
		Order entity = orderRepository.findOne(dto.getOrderId());
		if (entity != null) {
			List<DelivererDTO> deliverers = delivererService.get(entity.getPoi().id());
			if (deliverers.isEmpty()) {
				throw new NotFoundException("Your shop don't have any deliverer");
			}
			// set param.
			Map<String, String> params = new HashMap<String, String>();
			params.put("user-id", userId);
			// set booking info.
			BookCarDTO deliverer = this.setBookCar(dto, entity);
			ResponseEntity<Void> response = restTemplate.postForEntity(Book_Order, deliverer, Void.class, params);
			HttpStatus status = response.getStatusCode();
			if (status == HttpStatus.CREATED) {

			}
		}
	}

	/**
	 * Set bookcar id.
	 * 
	 * @param bookOrder
	 * @param order
	 * @return BookCarDTO
	 */
	private BookCarDTO setBookCar(BookOrderDTO bookOrder, Order order) {
		BookCarDTO deliverer = new BookCarDTO();
		deliverer.setOriginLatitude(order.getPoi().latitude());
		deliverer.setOriginLongitude(order.getPoi().longitude());
		deliverer.setOriginAddress(bookOrder.getPoiAddress());
		deliverer.setDestinationLatitude(0d);
		deliverer.setDestinationLongitude(0d);
		deliverer.setDestinationAddress(order.getShippingAddress());
		if (!order.getPoi().getAvatarImage().isEmpty()) {
			deliverer.setAvatar(order.getPoi().getAvatarImage().get(0).getUrl());
		}
		deliverer.setPhone(order.getPoi().phone());
		deliverer.setUserName(order.getPoi().name());
		deliverer.setOfferPrice(bookOrder.getShipFee());
		deliverer.setUserPrice(bookOrder.getShipFee());
		deliverer.setProvince(order.getPoi().province().id());
		deliverer.setDistrict(order.getPoi().district().id());
		deliverer.setTimeOfArrival(order.getDeliveryPeriod());
		deliverer.setState(OmartType.BookCarState.WAITING);
		deliverer.setReceiverName(order.getReceiverName());
		deliverer.setReceiverPhone(order.getReceiverPhone());
		deliverer.setOrderId(order.getId());
		deliverer.setPoiId(order.getPoi().id());
		return deliverer;
	}

	/**
	 * Accept delivery
	 * 
	 * @param orderId
	 * @param dto
	 */
	public void acceptDelivery(Long orderId, BookCarDTO dto) {
		Order entity = orderRepository.findOne(orderId);
		DriverInfo driver = driverInfoRepository.findOne(dto.getUserId());
		if (entity != null && driver != null) {
			entity.setBookcarId(dto.getId());
			entity.setDriverInfo(driver);
			entity.setSellerState(OrderSellerState.DELIVERY.getId());
			orderRepository.save(entity);
		}
	}

}
