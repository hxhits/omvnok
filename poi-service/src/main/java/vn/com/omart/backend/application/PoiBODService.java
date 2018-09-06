package vn.com.omart.backend.application;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import vn.com.omart.backend.application.response.PoiBODOrderDTO;
import vn.com.omart.backend.application.response.PointOfInterestDTO;
import vn.com.omart.backend.application.util.DateUtils;
import vn.com.omart.backend.constants.ConstantUtils;
import vn.com.omart.backend.domain.model.Order;
import vn.com.omart.backend.domain.model.OrderRepository;
import vn.com.omart.backend.domain.model.PoiBOD;
import vn.com.omart.backend.domain.model.PoiBODRepository;
import vn.com.omart.backend.domain.model.PointOfInterest;
import vn.com.omart.backend.domain.model.PointOfInterestRepository;
import vn.com.omart.backend.domain.model.UserBOD;
import vn.com.omart.backend.domain.model.UserBODRepository;

@Service
public class PoiBODService {

	@Autowired
	private PoiBODRepository poiBODRepository;

	@Autowired
	private PointOfInterestRepository pointOfInterestRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private UserBODRepository userBODRepository;

	/**
	 * Count order between date
	 * 
	 * @param poi
	 * @param date
	 * @return amount
	 */
	public int countOrderByDate(PointOfInterest poi, Long date) {
		String strFrom = DateUtils.getDateByFormat(new Date(date), ConstantUtils.YYYYMMDD);
		String strCurrent = DateUtils.getDateByFormat(new Date(), ConstantUtils.YYYYMMDD);
		List<Order> orders = orderRepository.getOrdersByPoiAndBetweenDate(poi, strFrom, strCurrent);
		if (orders != null) {
			return orders.size();
		}
		return 0;
	}

	/**
	 * BOD get order basic report.
	 * 
	 * @param user
	 * @return List of PoiBODOrderDTO
	 */
	public List<PoiBODOrderDTO> getPoiBODOrders(UserBOD user, Pageable pageable) {
		List<PoiBOD> entities = poiBODRepository.findByUserBOD(user, pageable);
		if (entities != null) {
			List<PoiBODOrderDTO> results = new ArrayList<>();
			Long firstDayOfQuater = DateUtils.getFirstDayOfQuarter(new Date());
			Long firstDayOfMonth = DateUtils.getFirstDayOfMonth();
			Long firstDayOfWeek = DateUtils.getFirstDayOfWeek();
			Long currentDay = DateUtils.getCurrentDate().getTime();
			for (PoiBOD entity : entities) {
				PointOfInterest poi = entity.getPoi();
				int orderAmountCurrWeek = this.countOrderByDate(poi, firstDayOfWeek);
				int orderAmountCurrDate = this.countOrderByDate(poi, currentDay);
				int orderAmountCurrQuater = this.countOrderByDate(poi, firstDayOfQuater);
				int orderAmountCurrMonth = this.countOrderByDate(poi, firstDayOfMonth);
				PoiBODOrderDTO poiBODOrderDTO = new PoiBODOrderDTO(orderAmountCurrDate, orderAmountCurrWeek,
						orderAmountCurrMonth, orderAmountCurrQuater);
				poiBODOrderDTO.setPoiName(poi.name());
				poiBODOrderDTO.setPoiId(poi.id());
				results.add(poiBODOrderDTO);
			}
			return results;
		}
		return null;
	}

	/**
	 * BOD get order basic report.
	 * 
	 * @param userId
	 * @return List of PoiBODOrderDTO
	 */
	public List<PoiBODOrderDTO> getPoiBODOrders(String userId, Pageable pageable) {
		UserBOD userBOD = userBODRepository.findOne(userId);
		if (userBOD != null) {
			List<PoiBODOrderDTO> results = this.getPoiBODOrders(userBOD, pageable);
			if (results != null) {
				return results;
			}
		}
		return new ArrayList<>();
	}

	/**
	 * Save
	 * 
	 * @param user
	 * @param poiIdStr
	 */
	public void save(UserBOD user, String poiIdStr) {
		String[] ids = poiIdStr.split(",");
		if (ids.length > 0) {
			List<PoiBOD> entities = new ArrayList<>();
			for (String poiId : ids) {
				PointOfInterest poi = pointOfInterestRepository.findOne(Long.valueOf(poiId));
				if (poi != null) {
					PoiBOD entity = poiBODRepository.findByPoi(poi);
					if (entity == null) {
						entity = new PoiBOD();
						entity.setPoi(poi);
						entity.setUserBOD(user);
						entity.setCreatedAt(DateUtils.getCurrentDate());
						entity.setUpdatedAt(DateUtils.getCurrentDate());
						entities.add(entity);
					}
				}
			}
			if (!entities.isEmpty()) {
				poiBODRepository.save(entities);
			}
		}
	}

	/**
	 * get poi by bod user
	 * 
	 * @param userId
	 * @param pageable
	 * @return List of PointOfInterestDTO
	 */
	public List<PointOfInterestDTO> getPoiByBODUser(String userId, Pageable pageable) {
		UserBOD userBOD = userBODRepository.findOne(userId);
		if (userBOD != null) {
			List<PoiBOD> entities = poiBODRepository.findByUserBOD(userBOD, pageable);
			if (entities != null) {
				List<PointOfInterestDTO> dtos = entities.stream()
						.map(entity -> PointOfInterestDTO.toBasicDTO(entity.getPoi())).collect(Collectors.toList());
				return dtos;
			}
		}
		return new ArrayList<>();
	}

}
