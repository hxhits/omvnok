package vn.com.omart.backend.application;

import static org.slf4j.LoggerFactory.getLogger;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import vn.com.omart.backend.application.response.BookCarDTO;
import vn.com.omart.backend.application.util.CommonUtils;
import vn.com.omart.backend.application.util.DateUtils;
import vn.com.omart.backend.application.util.XMLUtils;
import vn.com.omart.backend.constants.ConstantUtils;
import vn.com.omart.backend.constants.OmartType;
import vn.com.omart.backend.constants.OmartType.BookCarState;
import vn.com.omart.backend.domain.model.BookCar;
import vn.com.omart.backend.domain.model.BookCarRepository;
import vn.com.omart.backend.domain.model.CarType;
import vn.com.omart.backend.domain.model.CarTypeRepository;
import vn.com.omart.backend.domain.model.District;
import vn.com.omart.backend.domain.model.DistrictRepository;
import vn.com.omart.backend.domain.model.DriverPushTokenRepository;
import vn.com.omart.backend.domain.model.PoiNotification;
import vn.com.omart.backend.domain.model.PoiNotificationRepository;
import vn.com.omart.backend.domain.model.Province;
import vn.com.omart.backend.domain.model.ProvinceRepository;

@Service
public class BookCarService {

	private final Logger logger = getLogger(BookCarService.class);

	@Value("${google.geocode-api-distancematrix}")
	private String Google_DistanceMatrix_API;

	@Value("${google.api-key}")
	private String Google_API_Key;

	@Autowired
	private BookCarRepository bookCarRepository;

	@Autowired
	private PoiNotificationRepository poiNotificationRepository;

	@Autowired
	private CarTypeRepository carTypeRepository;

	@Autowired
	private DriverPushTokenRepository driverPushTokenRepository;

	@Autowired
	private FcmClientDriverService fcmClientDriverService;

	@Autowired
	private ProvinceRepository provinceRepository;

	@Autowired
	private DistrictRepository districtRepository;

	private final String DISTANCE_MATRIX_RESPONSE_STATUS_QUERY = "/DistanceMatrixResponse/status/text()";
	private final String DISTANCE_MATRIX_RESPONSE_ELEMENT_DISTANCE_QUERY = "/DistanceMatrixResponse/row[1]/element/distance/value/text()";
	private final String DISTANCE_MATRIX_RESPONSE_ELEMENT_STATUS_QUERY = "/DistanceMatrixResponse/row[1]/element/status/text()";

	/**
	 * Insert a new record.
	 *
	 * @param userId
	 * @param dto
	 */
	@Transactional(readOnly = false)
	public BookCarDTO insert(String userId, BookCarDTO dto) {
		BookCarDTO response = new BookCarDTO();
		return response;
	}

	/**
	 * Insert a new record. V1
	 *
	 * @param userId
	 * @param dto
	 * @return
	 */
	@Transactional(readOnly = false)
	public BookCarDTO insertV1(String userId, BookCarDTO dto) {
		BookCarDTO response = new BookCarDTO();
		ModelMapper mapper = new ModelMapper();
		BookCar entity = mapper.map(dto, BookCar.class);
		if (entity != null) {
			CarType carType = carTypeRepository.findOne(dto.getCarType());
			Province province = provinceRepository.findOne(dto.getProvince());
			District district = districtRepository.findOne(dto.getDistrict());
			entity.setUserId(userId);
			entity.setCarType(carType);
			entity.setProvince(province);
			entity.setDistrict(district);
			entity.setState(OmartType.BookCarState.WAITING);
			entity.setCreatedAt(new DateUtils().getCurrentDate());
			entity.setUpdatedAt(new DateUtils().getCurrentDate());
			entity = bookCarRepository.save(entity);
			this.sendToDriver(entity);
			response.setId(entity.getId());
		}
		return response;
	}

	/**
	 * Send push to driver.
	 *
	 * @param bookCar
	 */
	public void sendToDriver(BookCar bookCar) {
		// send notification.
		Map<String, String> data = new HashMap<String, String>();
		// int distance = (int) Math.round(bookCar.getDistance()/1000);
		String title = bookCar.getProvince().name() + ", đặt " + bookCar.getCarType().getName().toLowerCase() + " - "
				+ bookCar.getUserPrice() / 1000 + "K";
		String description = "Từ " + bookCar.getPlaceNameOrigin() + " đến " + bookCar.getPlaceNameDestination();

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
		String provinces = "0," + bookCar.getProvince().id();
		String districts = "0," + bookCar.getDistrict().id();
		List<Object[]> cartypes = driverPushTokenRepository.getDriverTokens(bookCar.getCarType().getId(), provinces,
				districts);
		fcmClientDriverService.send(bookCar.getUserId(), title, description, data, cartypes);
	}

	/**
	 * Submit state.
	 *
	 * @param id
	 * @param state
	 */
	public void submitState(Long id, BookCarDTO dto) {
		BookCar entity = bookCarRepository.findOne(id);
		if (entity != null) {
			entity.setState(dto.getState());
			entity.setUpdatedAt(DateUtils.getCurrentDate());
			entity = bookCarRepository.save(entity);
			if (entity.getState() == BookCarState.CANCELLED || entity.getState() == BookCarState.BOOKED) {
				if (entity.getPoiNotification() != null) {
					PoiNotification poiNotification = poiNotificationRepository
							.findOne(entity.getPoiNotification().getId());
					poiNotification.setActive(false);
					poiNotificationRepository.save(poiNotification);
				}
			}
		} else {
			logger.error("Bookcar cancel is null");
		}
	}

	/**
	 * Get distance base on latitude , longitude.
	 *
	 * @param bookCarDTO
	 * @return BookCarDTO
	 */
	public BookCarDTO getDistance(BookCarDTO bookCarDTO) {
		String origins = bookCarDTO.getOriginLatitude() + "," + bookCarDTO.getOriginLongitude();
		String destinations = bookCarDTO.getDestinationLatitude() + "," + bookCarDTO.getDestinationLongitude();
		// set DTO.
		BookCarDTO dto = new BookCarDTO();
		String xml = this.getGoogleDistanceMatrix(origins, destinations);
		String dStatus = XMLUtils.getValueByQuery(xml, DISTANCE_MATRIX_RESPONSE_STATUS_QUERY);
		if (dStatus.equalsIgnoreCase("OK")) {
			String eStatus = XMLUtils.getValueByQuery(xml, DISTANCE_MATRIX_RESPONSE_ELEMENT_STATUS_QUERY);
			if (eStatus.equalsIgnoreCase("OK")) {
				String distanceStr = XMLUtils.getValueByQuery(xml, DISTANCE_MATRIX_RESPONSE_ELEMENT_DISTANCE_QUERY);
				if (!StringUtils.isBlank(distanceStr)) {
					double distance = Double.valueOf(distanceStr);
					dto.setDistance(distance);
				}
			}
		}
		return dto;
	}

	/**
	 * Access to google service get distance matrix.
	 *
	 * @param origins
	 * @param destinations
	 * @return XML
	 */
	private String getGoogleDistanceMatrix(String origins, String destinations) {
		String transactionUrl = Google_DistanceMatrix_API + ConstantUtils.XML
				+ "?origins={origins}&destinations={destinations}&mode=driving&key="
				+ CommonUtils.getGoogleAPIKey(Google_API_Key);
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(transactionUrl);
		RestTemplate restTemplate = new RestTemplate();
		// set params.
		Map<String, String> uriParams = new HashMap<String, String>();
		uriParams.put("origins", origins);
		uriParams.put("destinations", destinations);
		// invoke the google api.
		URI uri = builder.buildAndExpand(uriParams).toUri();
		String xml = restTemplate.getForObject(uri, String.class);
		return xml;
	}

	/**
	 * Get detail.
	 *
	 * @param id
	 * @return BookCarDTO
	 */
	public BookCarDTO getById(Long id) {
		BookCar entity = bookCarRepository.findOne(id);
		if (entity == null) {
			logger.error("BookCar getById was null");
		} else {
			BookCarDTO dto = BookCarDTO.toDTO(entity);
			return dto;
		}
		return new BookCarDTO();
	}

	/**
	 * Get state by id.
	 *
	 * @param id
	 * @return state of BookCarDTO
	 */
	public BookCarDTO getStateById(Long id) {
		BookCar entity = bookCarRepository.findOne(id);
		if (entity == null) {
			logger.error("BookCar getStateById was null");
		} else {
			BookCarDTO dto = new BookCarDTO();
			dto.setState(entity.getState());
			return dto;
		}
		return new BookCarDTO();
	}

	public List<BookCarDTO> getByState(OmartType.BookCarState state, Pageable pageable) {
		List<BookCar> entities = bookCarRepository.findBystate(state, pageable);
		List<BookCarDTO> FS = entities.stream().map(BookCarDTO::toDTO).collect(Collectors.toList());
		return FS;
	}

}
