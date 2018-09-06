package vn.com.omart.backend.application.response;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;

import vn.com.omart.backend.constants.UserTitleEnum;
import vn.com.omart.backend.domain.model.ReportAbuse;

public class ReportAbuseDTO {
		
	private Long id;
	@NotNull
	private Long poiId;
	private String ownerId;
	private String userId;
	private String reason;
	@NotNull
	private UserTitleEnum side;
	private Long orderId;
	private PointOfInterestDTO poi;
	private UserProfileDTO owner;
	private UserProfileDTO user;
	private DriverInfoDTO driverInfo;
	private Long createdAt;
	
	public static ReportAbuseDTO toFullDTO(ReportAbuse entity) {
		ReportAbuseDTO dto = new ReportAbuseDTO();
		dto.setCreatedAt(entity.getCreatedAt().getTime());
		dto.setId(entity.getId());
		dto.setPoiId(entity.getPoi().id());
		PointOfInterestDTO poiItem = new PointOfInterestDTO();
		poiItem.setName(entity.getPoi().name());
		poiItem.setId(entity.getPoi().id());
		poiItem.setPhone(Arrays.asList(entity.getPoi().phone()));
		dto.setPoi(poiItem);
		if(entity.getOrder()!=null) {
			dto.setOrderId(entity.getOrder().getId());
		}
		dto.setOwner(UserProfileDTO.toBasicDTO(entity.getOwnerProfile()));
		if(entity.getUserProfile()!=null) {
			dto.setUser(UserProfileDTO.toBasicDTO(entity.getUserProfile()));
		} 
		if(entity.getDriver()!=null) {
			dto.setDriverInfo(DriverInfoDTO.toBasicDTO(entity.getDriver()));
		}
		if(StringUtils.isNotBlank(entity.getReason())) {
			dto.setReason(entity.getReason());
		}
		if(entity.getSide()==0) {
			dto.setSide(UserTitleEnum.USER);
		} else 	if(entity.getSide()==1) {
			dto.setSide(UserTitleEnum.OWNER);
		} else 	if(entity.getSide()==2) {
			dto.setSide(UserTitleEnum.SHIPPER);
		}
		return dto;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getPoiId() {
		return poiId;
	}
	public void setPoiId(Long poiId) {
		this.poiId = poiId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	public UserTitleEnum getSide() {
		return side;
	}
	public void setSide(UserTitleEnum side) {
		this.side = side;
	}
	public String getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public PointOfInterestDTO getPoi() {
		return poi;
	}
	public void setPoi(PointOfInterestDTO poi) {
		this.poi = poi;
	}
	public UserProfileDTO getOwner() {
		return owner;
	}
	public void setOwner(UserProfileDTO owner) {
		this.owner = owner;
	}
	public UserProfileDTO getUser() {
		return user;
	}
	public void setUser(UserProfileDTO user) {
		this.user = user;
	}

	public DriverInfoDTO getDriverInfo() {
		return driverInfo;
	}

	public void setDriverInfo(DriverInfoDTO driverInfo) {
		this.driverInfo = driverInfo;
	}

	public Long getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Long createdAt) {
		this.createdAt = createdAt;
	}
	
}
