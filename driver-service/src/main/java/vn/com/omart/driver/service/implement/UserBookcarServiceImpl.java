package vn.com.omart.driver.service.implement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.com.omart.driver.common.constant.DriverType.BookCarState;
import vn.com.omart.driver.common.util.CommonUtils;
import vn.com.omart.driver.common.util.DateUtils;
import vn.com.omart.driver.dto.BookCarDTO;
import vn.com.omart.driver.entity.BookCar;
import vn.com.omart.driver.entity.CarType;
import vn.com.omart.driver.entity.District;
import vn.com.omart.driver.entity.DriverPushToken;
import vn.com.omart.driver.entity.Province;
import vn.com.omart.driver.repository.BookCarRepository;
import vn.com.omart.driver.repository.CarTypeRepository;
import vn.com.omart.driver.repository.DistrictRepository;
import vn.com.omart.driver.repository.DriverPushTokenRepository;
import vn.com.omart.driver.repository.ProvinceRepository;
import vn.com.omart.driver.service.DelivererService;
import vn.com.omart.driver.service.FcmClientService;
import vn.com.omart.driver.service.UserBookcarService;

@Service
public class UserBookcarServiceImpl implements UserBookcarService {

	private static final Logger logger = LoggerFactory.getLogger(UserBookcarServiceImpl.class);

	@Autowired
	private FcmClientService fcmClientService;

	@Autowired
	private DriverPushTokenRepository driverPushTokenRepository;

	@Autowired
	private CarTypeRepository carTypeRepository;

	@Autowired
	private ProvinceRepository provinceRepository;

	@Autowired
	private DistrictRepository districtRepository;

	@Autowired
	private BookCarRepository bookCarRepository;

	@Autowired
	private DelivererService delivererService;

	/**
	 * 
	 * @param bookCar
	 */
	public void sendPushNotification(BookCar bookCar) {
		// send notification.
		Map<String, String> data = new HashMap<String, String>();
		String price = CommonUtils.getShortPrice(new Long(bookCar.getUserPrice()));
		String title = bookCar.getUserName() + " cần đặt " + bookCar.getCarType().getName().toLowerCase() + " - "
				+ price;
		String description = "Đi từ " + bookCar.getPlaceNameOrigin() + " đến " + bookCar.getPlaceNameDestination();

		data.put("type", "3");
		data.put("origin_address", bookCar.getOriginAddress());
		data.put("destination_address", bookCar.getDestinationAddress());
		data.put("distance", bookCar.getDistance().toString());
		data.put("user_price", String.valueOf(bookCar.getUserPrice()));
		data.put("bookcar_id", bookCar.getId().toString());
		data.put("car_type_id", bookCar.getCarType().getId().toString());
		data.put("title", title);
		data.put("description", description);
		data.put("origin_latitude", bookCar.getOriginLatitude().toString());
		data.put("origin_longitude", bookCar.getOriginLongitude().toString());
		data.put("destination_latitude", bookCar.getDestinationLatitude().toString());
		data.put("destination_longitude", bookCar.getDestinationLongitude().toString());

		// 0 is get ALL
		String provinces = "0," + bookCar.getProvince();
		String districts = "0," + bookCar.getDistrict();
		List<Object[]> cartypes = driverPushTokenRepository.getDriverTokens(bookCar.getCarType().getId(), provinces,
				districts);
		fcmClientService.send(bookCar.getUserId(), title, description, data, cartypes);
	}

	/**
	 * Create.
	 */
	@Transactional(readOnly = false)
	@Override
	public BookCarDTO create(String userId, BookCarDTO dto) {
		BookCarDTO response = new BookCarDTO();
		ModelMapper mapper = new ModelMapper();
		BookCar entity = mapper.map(dto, BookCar.class);
		if (entity != null) {
			CarType carType = carTypeRepository.findOne(dto.getCarType());
			// location cannot found.
			if (dto.getProvince() == 0L || dto.getDistrict() == 0L) {
				logger.error("create function province or district cannot found");
				entity.setProvince(0L);
				entity.setDistrict(0L);
			} else {
				Province province = provinceRepository.findOne(dto.getProvince());
				District district = districtRepository.findOne(dto.getDistrict());
				entity.setProvince(province.getId());
				entity.setDistrict(district.getId());
			}
			entity.setUserId(userId);
			entity.setCarType(carType);
			entity.setState(BookCarState.WAITING);
			entity.setCreatedAt(new DateUtils().getCurrentDate());
			entity.setUpdatedAt(new DateUtils().getCurrentDate());
			entity = bookCarRepository.save(entity);
			this.sendPushNotification(entity);
			response.setId(entity.getId());
		}
		return response;
	}

	/**
	 * Receive Ordering.
	 */
	@Override
	public void receiveOrdering(String userId, BookCarDTO dto) {
		ModelMapper mapper = new ModelMapper();
		BookCar entity = mapper.map(dto, BookCar.class);
		if (entity != null) {
			CarType carType = carTypeRepository.findOne(12L);// 12 = value of car type delivery
			entity.setCarType(carType);
			entity.setState(BookCarState.WAITING);
			entity.setCreatedAt(new DateUtils().getCurrentDate());
			entity.setUpdatedAt(new DateUtils().getCurrentDate());
			entity.setUserId(userId);
			entity.setDistance(0d);
			entity.setPlaceNameDestination("");
			entity.setPlaceNameOrigin("");
			entity.setServiceType(1);
			entity.setPoiId(dto.getPoiId());
			entity = bookCarRepository.save(entity);
			// send push
			this.sendOrderPushNotification(entity, dto.getPoiId());
		}
	}

	/**
	 * Send notification to driver.
	 * 
	 * @param bookCar
	 * @param poiId
	 */
	private void sendOrderPushNotification(BookCar bookCar, Long poiId) {
		// send notification.
		Map<String, String> data = new HashMap<String, String>();
		String price = CommonUtils.getShortPrice(new Long(bookCar.getUserPrice()));
		String title = bookCar.getUserName() + " cần giao hàng";
		String shipFee = "Phí giao: "
				+ CommonUtils.getShortPrice(Long.parseLong(String.valueOf(bookCar.getUserPrice()))) + "\n";
		String description = shipFee + "Địa chỉ giao: " + bookCar.getDestinationAddress();
		// bookcar info.
		data.put("type", "3");
		data.put("origin_address", bookCar.getOriginAddress());
		data.put("destination_address", bookCar.getDestinationAddress());
		data.put("user_price", String.valueOf(bookCar.getUserPrice()));
		data.put("bookcar_id", bookCar.getId().toString());
		// data.put("car_type_id", bookCar.getCarType().getId().toString());
		data.put("title", title);
		data.put("description", description);
		data.put("origin_latitude", bookCar.getOriginLatitude().toString());
		data.put("origin_longitude", bookCar.getOriginLongitude().toString());
		// data.put("destination_latitude",
		// bookCar.getDestinationLatitude().toString());
		// data.put("destination_longitude",
		// bookCar.getDestinationLongitude().toString());
		// ordering info.
		data.put("service_type", String.valueOf(bookCar.getServiceType()));
		data.put("receiver_name", bookCar.getReceiverName());
		data.put("receiver_phone", bookCar.getReceiverPhone());
		data.put("orderId", bookCar.getOrderId().toString());
		data.put("shop_name", bookCar.getUserName());

		// 0 is get ALL
		String provinces = "0," + bookCar.getProvince();
		String districts = "0," + bookCar.getDistrict();
		List<String> userIds = delivererService.getUserIds(poiId);
		if (!userIds.isEmpty()) {
			List<DriverPushToken> tokens = driverPushTokenRepository.getTokenInUserIds(userIds);
			if (tokens != null && !tokens.isEmpty()) {
				fcmClientService.sendWithEntity(bookCar.getUserId(), title, description, data, tokens);
			}
		}
	}
}
