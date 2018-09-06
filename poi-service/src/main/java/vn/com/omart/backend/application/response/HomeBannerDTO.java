package vn.com.omart.backend.application.response;

import java.util.Date;

import javax.validation.constraints.NotNull;

import vn.com.omart.backend.application.util.DateUtils;
import vn.com.omart.backend.domain.model.HomeBanner;

public class HomeBannerDTO {
	
	private Long id;
	@NotNull
	private Long poiId;
	@NotNull
	private String image;
	@NotNull
	private int bannerType;
	private Long updatedAt;
	private String updatedBy;
	private Long createdAt;
	private String createdBy;
	private boolean isApproved;

	public static HomeBanner toEntity(HomeBannerDTO dto) {
		HomeBanner entity = new HomeBanner();
		entity.setApproved(true);
		entity.setBannerType(dto.getBannerType());
		entity.setImage(dto.getImage());
		entity.setUpdatedAt(DateUtils.getCurrentDate());
		entity.setCreatedAt(DateUtils.getCurrentDate());
		return entity;
	}
	
	public static HomeBanner toEntity(HomeBanner entity,HomeBannerDTO dto) {
		entity.setApproved(true);
		entity.setBannerType(dto.getBannerType());
		entity.setImage(dto.getImage());
		entity.setUpdatedAt(DateUtils.getCurrentDate());
		return entity;
	}
	
	public static HomeBannerDTO toBasicDTO(HomeBanner entity) {
		HomeBannerDTO dto = new HomeBannerDTO();
		dto.setImage(entity.getImage());
		dto.setPoiId(entity.getPoi().id());
		return dto;
	}
	
	public static HomeBannerDTO toFullDTO(HomeBanner entity) {
		HomeBannerDTO dto = toBasicDTO(entity);
		dto.setBannerType(entity.getBannerType());
		dto.setId(entity.getId());
		dto.setUpdatedAt(entity.getUpdatedAt().getTime());
		dto.setCreatedAt(entity.getCreatedAt().getTime());
		dto.setUpdatedBy(entity.getUpdatedBy());
		dto.setCreatedBy(entity.getCreatedBy());
		dto.setApproved(entity.isApproved());
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

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public int getBannerType() {
		return bannerType;
	}

	public void setBannerType(int bannerType) {
		this.bannerType = bannerType;
	}

	public Long getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Long updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Long getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Long createdAt) {
		this.createdAt = createdAt;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public boolean isApproved() {
		return isApproved;
	}

	public void setApproved(boolean isApproved) {
		this.isApproved = isApproved;
	}

}
