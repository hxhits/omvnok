package vn.com.omart.backend.application.response;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.id.PostInsertIdentityPersister;

import lombok.Data;
import vn.com.omart.backend.domain.model.PoiNotification;
import vn.com.omart.backend.domain.model.PointOfInterest;
import vn.com.omart.backend.domain.model.Recruitment;
import vn.com.omart.sharedkernel.application.model.dto.EntityMapper;

@Data
public class RecruitmentDTO {

	private Long id;
	private Long positionId;
	private Long positionLevelId;
	private RecruitmentPositionDTO recruitmentPosition;
	@NotNull
	private Long poiId;
	private String salary;
	private String workLocation;
	private String educationLevel;
	private String experienceLevel;
	private int quantity;
	private Date createdAt;
	private Date updatedAt;
	private Long expiredAt;
	private String description;
	private String requirement;
	private String benefit;
	private String contactInfo;
	private int viewCount;
	private String positionType;
	private int state = 0;
	private String title = "";
	private PointOfInterestDTO poi;
	private Long poiNotificationId;
	private boolean isDeleted;

	public RecruitmentDTO(Long id) {
		super();
		this.id = id;
	}

	public RecruitmentDTO() {
		super();
	}

	public static RecruitmentDTO toDTO(Recruitment entity) {
		RecruitmentDTO dto = new RecruitmentDTO();
		dto.setId(entity.getId());
		dto.setBenefit(entity.getBenefit());
		dto.setContactInfo(entity.getContactInfo());
		dto.setDescription(entity.getDescription());
		dto.setEducationLevel(entity.getEducationLevel());
		dto.setExperienceLevel(entity.getExperienceLevel());
		dto.setExpiredAt(entity.getExpiredAt() != null ? entity.getExpiredAt().getTime() : 0L);
		dto.setPositionType(entity.getPositionType());
		dto.setQuantity(entity.getQuantity());
		dto.setRequirement(entity.getRequirement());
		dto.setSalary(entity.getSalary());
		dto.setState(entity.getState());
		dto.setWorkLocation(entity.getWorkLocation());
		dto.setCreatedAt(entity.getCreatedAt());
		dto.setPoiId(entity.getPoi() != null ? entity.getPoi().id() : 0);
		dto.setPositionId(entity.getRecruitmentPosition() != null ? entity.getRecruitmentPosition().getId() : 0);
		dto.setPositionLevelId(
				entity.getRecruitmentPositionLevel() != null ? entity.getRecruitmentPositionLevel().getId() : 0);
		dto.setRecruitmentPosition(RecruitmentPositionDTO.toDTO(entity.getRecruitmentPositionLevel()));
		dto.setViewCount(entity.getViewCount());
		dto.setTitle(entity.getTitle());
		dto.setDeleted(entity.isDeleted());
		if (entity.getPoiNotifications() != null && !entity.getPoiNotifications().isEmpty()) {
			PoiNotification poi = entity.getPoiNotifications().get(0);
			dto.setPoiNotificationId(poi != null ? poi.getId() : 0);
		}
		// Add POI Info
		PointOfInterestDTO poi = new PointOfInterestDTO();
		if (entity.getPoi() != null) {
			PointOfInterest poiEntity = entity.getPoi();
			if (poiEntity != null) {
				poi.setId(poiEntity.id());
				poi.setName(poiEntity.getName());
				poi.setAvatarImage(ImageDTO.from(poiEntity.avatarImage()));
				poi.setAddress(poiEntity.getAddress());
				poi.setProvince(ProvinceDTO.from(poiEntity.province()));
				poi.setWard(WardDTO.from(poiEntity.ward()));
				poi.setDistrict(DistrictDTO.from(poiEntity.district()));
				poi.setOwnerId(poiEntity.ownerId());
				poi.setLatitude(poiEntity.latitude());
				poi.setLongitude(poiEntity.longitude());
			}
		}
		dto.setPoi(poi);

		return dto;
	}

	public static RecruitmentDTO toDTOHasPoi(Recruitment entity) {
		RecruitmentDTO dto = toDTO(entity);
		PointOfInterestDTO poi = new PointOfInterestDTO();
		if (entity.getPoi() != null) {
			PointOfInterest poiEntity = entity.getPoi();
			poi.setName(poiEntity.getName());
			poi.setAvatarImage(ImageDTO.from(poiEntity.avatarImage()));
			poi.setAddress(poiEntity.getAddress());
			poi.setProvince(ProvinceDTO.from(poiEntity.province()));
			poi.setWard(WardDTO.from(poiEntity.ward()));
			poi.setDistrict(DistrictDTO.from(poiEntity.district()));
			poi.setOwnerId(poiEntity.ownerId());
			dto.setPoi(poi);
		}
		return dto;
	}

	private static Recruitment setStaticsEntity(RecruitmentDTO dto) {
		Recruitment entity = new Recruitment();
		entity.setId(dto.getId());
		entity.setBenefit(dto.getBenefit());
		entity.setContactInfo(dto.getContactInfo());
		entity.setDescription(dto.getDescription());
		entity.setEducationLevel(dto.getEducationLevel());
		entity.setExperienceLevel(dto.getExperienceLevel());
		entity.setExpiredAt(new Date(dto.getExpiredAt()));
		entity.setPositionType(dto.getPositionType());
		entity.setQuantity(dto.getQuantity());
		entity.setRequirement(dto.getRequirement());
		entity.setSalary(dto.getSalary());
		entity.setState(dto.getState());
		entity.setWorkLocation(dto.getWorkLocation());
		entity.setTitle(dto.getTitle());
		return entity;
	}

	public static Recruitment toNewEntity(RecruitmentDTO dto) {
		Recruitment entity = setStaticsEntity(dto);
		entity.setCreatedAt(new Date());
		entity.setUpdatedAt(new Date());
		return entity;
	}

	public static Recruitment toUpdateEntity(RecruitmentDTO dto) {
		Recruitment entity = setStaticsEntity(dto);
		entity.setUpdatedAt(new Date());
		return entity;
	}

	public static Recruitment toUpdateEntity1(Recruitment entity) {

		return entity;
	}
	public static class QueryMapper implements EntityMapper<RecruitmentDTO, Recruitment>{

		@Override
		public RecruitmentDTO map(Recruitment entity) {
			// TODO Auto-generated method stub
			return toDTO(entity);
		}

		@Override
		public void map(Recruitment entity, RecruitmentDTO dto) {
			// TODO Auto-generated method stub
			
		}
		
	}

}
