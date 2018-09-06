package vn.com.omart.driver.service.implement;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import vn.com.omart.driver.common.constant.DriverResponse;
import vn.com.omart.driver.common.constant.DriverType.BookCarState;
import vn.com.omart.driver.common.exception.DriverException;
import vn.com.omart.driver.common.util.DateUtils;
import vn.com.omart.driver.dto.BookCarDTO;
import vn.com.omart.driver.dto.DriverFollowDTO;
import vn.com.omart.driver.entity.BookCar;
import vn.com.omart.driver.repository.BookCarRepository;
import vn.com.omart.driver.repository.CarTypeRepository;
import vn.com.omart.driver.repository.DistrictRepository;
import vn.com.omart.driver.repository.DriverPushTokenRepository;
import vn.com.omart.driver.repository.ProvinceRepository;
import vn.com.omart.driver.service.BookCarService;
import vn.com.omart.driver.service.DelivererService;
import vn.com.omart.driver.service.DriverInfoService;
import vn.com.omart.driver.service.FcmClientService;
import vn.com.omart.driver.service.RemotePoiService;

@Service
public class BookCarServiceImpl implements BookCarService {

	private static final Logger logger = LoggerFactory.getLogger(BookCarServiceImpl.class);

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
	private DriverInfoService driverInfoService;

	@Autowired
	private RemotePoiService remotePoiService;

	/**
	 * Get By Id.
	 */
	public BookCar getBookCarById(Long id) {
		BookCar entity = bookCarRepository.getById(id);
		if (entity == null) {
			throw new DriverException(DriverResponse.BOOK_CAR_NOT_FOUND, id);
		}
		return entity;
	}

	@Autowired
	private DelivererService delivererService;

	/**
	 * Get book car by driver following.
	 */
	@Override
	public List<BookCar> getBookCarByCarTypeAndProvinceAndDistrict(String userId, DriverFollowDTO dto,
			Pageable pageable) {
		// TODO Auto-generated method stub
		List<BookCar> results = new ArrayList<>();
		List<BookCar> bookcars = new ArrayList<>();
		List<BookCar> orders = new ArrayList<>();
		// if user unblock.
		if (!driverInfoService.isDriverBlocked(userId)) {
			List<Long> poiIds = delivererService.getPoiIds(userId);
			if (!poiIds.isEmpty()) {
				// ordering
				List<BookCar> orderes = new ArrayList<>();
				PageRequest pageRequest = new PageRequest(0, Integer.MAX_VALUE);
				orders = bookCarRepository.getOrder(poiIds, pageRequest);
			}
			// bookcar
			bookcars = this.getBookCars(dto, pageable);
			// merge
			results = Stream.concat(orders.stream(), bookcars.stream()).collect(Collectors.toList());
			// sort created at desc.
			// Collections.sort(results, (new Comparator<BookCar>() {
			// @Override
			// public int compare(BookCar o1, BookCar o2) {
			// // TODO Auto-generated method stub
			// return o1.getUpdatedAt().compareTo(o2.getUpdatedAt());
			// }
			// }).reversed());
		}
		return results;
	}

	private List<BookCar> getBookCars(DriverFollowDTO dto, Pageable pageable) {
		List<BookCar> entities = new ArrayList<>();
		if (!dto.getCarTypeSelectedStr().trim().isEmpty()) {
			String[] carTypeStr = dto.getCarTypeSelectedStr().replace(" ", "").split(",");
			Long[] carTypes = new Long[carTypeStr.length];
			for (int i = 0; i < carTypeStr.length; i++) {
				carTypes[i] = Long.parseLong(carTypeStr[i]);
			}

			Long provinceId = 0L;
			Long districtId = 0L;
			if (!dto.getProvinceDistrictSelectedStr().trim().isEmpty()) {
				String[] pdStr = dto.getProvinceDistrictSelectedStr().replace(" ", "").split(",");
				if (pdStr.length == 1) {
					provinceId = Long.parseLong(pdStr[0]);
				} else {
					provinceId = Long.parseLong(pdStr[0]);
					districtId = Long.parseLong(pdStr[1]);
				}
			}
			entities = bookCarRepository.getBookCarByCarTypeAndProvinceAndDistrict(carTypes, provinceId, districtId,
					DateUtils.getCurrentDateWithString(), pageable);
		}
		return entities;
	}

	// public List<BookCar> getBookCarByCarTypeAndProvinceAndDistrict(String userId,
	// DriverFollowDTO dto,
	// Pageable pageable) {
	// // TODO Auto-generated method stub
	// List<BookCar> entities = new ArrayList<>();
	// //if user unblock.
	// if(!driverInfoService.isDriverBlocked(userId)) {
	// if (!dto.getCarTypeSelectedStr().trim().isEmpty()) {
	// String[] carTypeStr = dto.getCarTypeSelectedStr().replace(" ",
	// "").split(",");
	// Long[] carTypes = new Long[carTypeStr.length];
	// for (int i = 0; i < carTypeStr.length; i++) {
	// carTypes[i] = Long.parseLong(carTypeStr[i]);
	// }
	//
	// Long provinceId = 0L;
	// Long districtId = 0L;
	// if (!dto.getProvinceDistrictSelectedStr().trim().isEmpty()) {
	// String[] pdStr = dto.getProvinceDistrictSelectedStr().replace(" ",
	// "").split(",");
	// if (pdStr.length == 1) {
	// provinceId = Long.parseLong(pdStr[0]);
	// } else {
	// provinceId = Long.parseLong(pdStr[0]);
	// districtId = Long.parseLong(pdStr[1]);
	// }
	// }
	// entities =
	// bookCarRepository.getBookCarByCarTypeAndProvinceAndDistrict(carTypes,
	// provinceId, districtId,
	// DateUtils.getCurrentDateWithString(), pageable);
	// }
	// }
	// return entities;
	// }

	/**
	 * Get state.
	 */
	@Override
	public BookCarDTO getBookCarStateById(Long id) {
		// TODO Auto-generated method stub
		BookCarDTO dto = new BookCarDTO();
		BookCar entity = bookCarRepository.findStateById(id);
		if (entity != null) {
			dto.setState(entity.getState());
		}
		return dto;
	}

	/**
	 * Accept Delivery.
	 */
	@Override
	public void acceptDelivery(String userId, BookCarDTO dto) {
		BookCar entity = bookCarRepository.findOne(dto.getId());
		if (entity != null) {
			entity.setState(BookCarState.BOOKED);
			entity.setUpdatedAt(DateUtils.getCurrentDate());
			bookCarRepository.save(entity);
			dto.setUserId(userId);// this is a driver_id
			dto.setOrderId(entity.getOrderId());
			remotePoiService.acceptDelivery(dto);
		}
	}

}
