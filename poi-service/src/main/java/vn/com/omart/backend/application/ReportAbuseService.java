package vn.com.omart.backend.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import vn.com.omart.backend.application.response.ReportAbuseDTO;
import vn.com.omart.backend.application.util.CommonUtils;
import vn.com.omart.backend.constants.UserTitleEnum;
import vn.com.omart.backend.domain.model.DriverInfo;
import vn.com.omart.backend.domain.model.DriverInfoRepository;
import vn.com.omart.backend.domain.model.Order;
import vn.com.omart.backend.domain.model.OrderRepository;
import vn.com.omart.backend.domain.model.PointOfInterest;
import vn.com.omart.backend.domain.model.PointOfInterestRepository;
import vn.com.omart.backend.domain.model.ReportAbuse;
import vn.com.omart.backend.domain.model.ReportAbuseRepository;
import vn.com.omart.backend.domain.model.UserProfile;
import vn.com.omart.backend.domain.model.UserProfileRepository;
import vn.com.omart.sharedkernel.application.model.error.NotFoundException;

@Service
public class ReportAbuseService {

	@Autowired
	private ReportAbuseRepository reportAbuseRepository;

	@Autowired
	private PointOfInterestRepository pointOfInterestRepository;

	@Autowired
	private UserProfileRepository userProfileRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private DriverInfoRepository driverInfoRepository;

	public  List<ReportAbuseDTO> getAll(Pageable pageable) {
		 List<ReportAbuse> entities = reportAbuseRepository.findAll(pageable).getContent();
		 if(!entities.isEmpty()) {
			 List<ReportAbuseDTO> results  = entities.stream().map(ReportAbuseDTO::toFullDTO).collect(Collectors.toList());
			 return results;
		 }
		 return new ArrayList<>();
	}

	public void save(String userId, ReportAbuseDTO dto) {
		PointOfInterest poi = pointOfInterestRepository.findOne(dto.getPoiId());
		if (poi != null) {
			ReportAbuse entity = new ReportAbuse();
			UserProfile userProfile = null;
			UserProfile ownerProfile = null;
			DriverInfo driverInfo = null;
			if (dto.getSide() == UserTitleEnum.OWNER) {
				userProfile = userProfileRepository.findByUserId(dto.getUserId());
				if (userProfile == null) {
					throw new NotFoundException("Report Abuse exception user profile not found in user profile table ");
				}
				ownerProfile = userProfileRepository.findByUserId(userId);
				entity.setSide(1);// from shop owner->user
				entity.setUserProfile(userProfile);
			} else if (dto.getSide() == UserTitleEnum.USER){
				userProfile = userProfileRepository.findByUserId(userId);
				if (userProfile == null) {
					throw new NotFoundException("Report Abuse exception user profile not found in user profile table ");
				}
				ownerProfile = userProfileRepository.findByUserId(poi.getOwnerId());
				entity.setSide(0); // from user->shop
				entity.setUserProfile(userProfile);
			} else if (dto.getSide() == UserTitleEnum.SHIPPER){
				driverInfo = driverInfoRepository.findOne(dto.getUserId());
				if (driverInfo == null) {
					throw new NotFoundException("Report Abuse exception driver profile not found in driver profile table ");
				}
				ownerProfile = userProfileRepository.findByUserId(userId);
				entity.setSide(2);// from shop owner->shipper
				entity.setDriver(driverInfo);
			}

			if (ownerProfile == null) {
				throw new NotFoundException("Report Abuse exception poi owner not found in user profile table ");
			}
			
			if (StringUtils.isNotBlank(dto.getReason())) {
				entity.setReason(dto.getReason());
			}
			
			entity.setPoi(poi);
			entity.setOwnerProfile(ownerProfile);
			if(!CommonUtils.isIdNullOrZero(dto.getOrderId())) {
				Order order = orderRepository.findOne(dto.getOrderId());
				if(order!=null) {
					entity.setOrder(order);
				}
			}
			reportAbuseRepository.save(entity);
		}else {
			throw new NotFoundException("Report Abuse exception poi not found with id: "+poi.id());
		}
	}
}
