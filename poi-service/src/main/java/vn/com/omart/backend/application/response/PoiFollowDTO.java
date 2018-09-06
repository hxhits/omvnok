package vn.com.omart.backend.application.response;

import java.util.Date;

import vn.com.omart.backend.domain.model.PoiFollow;

public class PoiFollowDTO {

	private Long id;
	private UserProfileDTO user;
	private PointOfInterestDTO poi;
	private Date createdAt;
	private Long poiId;

	public static PoiFollowDTO toBasicDTO(PoiFollow entity) {
		PoiFollowDTO dto = new PoiFollowDTO();
		dto.setId(entity.getId());
		dto.setPoi(PointOfInterestDTO.toBasicDTO(entity.getPoi()));
		dto.setUser(UserProfileDTO.toBasicDTO(entity.getUser()));
		return dto;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UserProfileDTO getUser() {
		return user;
	}

	public void setUser(UserProfileDTO user) {
		this.user = user;
	}

	public PointOfInterestDTO getPoi() {
		return poi;
	}

	public void setPoi(PointOfInterestDTO poi) {
		this.poi = poi;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Long getPoiId() {
		return poiId;
	}

	public void setPoiId(Long poiId) {
		this.poiId = poiId;
	}

}
