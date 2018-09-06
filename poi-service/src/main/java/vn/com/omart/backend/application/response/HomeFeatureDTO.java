package vn.com.omart.backend.application.response;

import java.util.Date;

import vn.com.omart.backend.application.util.DateUtils;
import vn.com.omart.backend.constants.OmartType.AppFeature;
import vn.com.omart.backend.domain.model.HomeFeature;

public class HomeFeatureDTO {
	
	private Long id;
	private String title;
	private String desc;
	private String image;
	private String titleColor;
	private String descColor;
	private AppFeature appFeature;
	private String featureName;
	private String featureNameEn;
	private Long updatedAt;
	private String updatedBy;
	private Long createdAt;
	private String createdBy;
	private boolean isApproved;
	
	public static HomeFeature toEntity(HomeFeatureDTO dto) {
		HomeFeature entity = new HomeFeature();
		entity.setApproved(true);
		entity.setDescription(dto.getDesc());
		entity.setDescColor(dto.getDescColor());
		entity.setTitle(dto.getTitle());
		entity.setTitleColor(dto.getTitleColor());
		entity.setFeatureId(dto.getAppFeature().getId());
		entity.setFeatureName(dto.getFeatureName());
		entity.setFeatureNameEn(dto.getFeatureNameEn());
		entity.setImage(dto.getImage());
		entity.setCreatedAt(DateUtils.getCurrentDate());
		entity.setUpdatedAt(DateUtils.getCurrentDate());
		return entity;
	}
	
	public static HomeFeature toEntity(HomeFeature entity,HomeFeatureDTO dto) {
		entity.setApproved(true);
		entity.setDescription(dto.getDesc());
		entity.setDescColor(dto.getDescColor());
		entity.setTitle(dto.getTitle());
		entity.setTitleColor(dto.getTitleColor());
		entity.setFeatureId(dto.getAppFeature().getId());
		entity.setFeatureName(dto.getFeatureName());
		entity.setFeatureNameEn(dto.getFeatureNameEn());
		entity.setImage(dto.getImage());
		entity.setUpdatedAt(DateUtils.getCurrentDate());
		return entity;
	}
	
	public static HomeFeatureDTO toBasicDTO(HomeFeature entity) {
		HomeFeatureDTO dto = new HomeFeatureDTO();
		dto.setDesc(entity.getDescription());
		dto.setTitle(entity.getTitle());
		dto.setTitleColor(entity.getTitleColor());
		dto.setDescColor(entity.getDescColor());
		dto.setAppFeature(AppFeature.getById(entity.getFeatureId()));
		dto.setImage(entity.getImage());
		dto.setFeatureName(entity.getFeatureName());
		dto.setFeatureNameEn(entity.getFeatureNameEn());
		return dto;
	}

	public static HomeFeatureDTO toFullDTO(HomeFeature entity) {
		HomeFeatureDTO dto = toBasicDTO(entity);
		dto.setId(entity.getId());
		dto.setApproved(entity.isApproved());
		dto.setCreatedAt(entity.getCreatedAt().getTime());
		dto.setUpdatedAt(entity.getUpdatedAt().getTime());
		dto.setCreatedBy(entity.getCreatedBy());
		dto.setUpdatedBy(entity.getUpdatedBy());
		return dto;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getTitleColor() {
		return titleColor;
	}

	public void setTitleColor(String titleColor) {
		this.titleColor = titleColor;
	}

	public String getDescColor() {
		return descColor;
	}

	public void setDescColor(String descColor) {
		this.descColor = descColor;
	}

	public AppFeature getAppFeature() {
		return appFeature;
	}

	public void setAppFeature(AppFeature appFeature) {
		this.appFeature = appFeature;
	}

	public String getFeatureName() {
		return featureName;
	}

	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}

	public String getFeatureNameEn() {
		return featureNameEn;
	}

	public void setFeatureNameEn(String featureNameEn) {
		this.featureNameEn = featureNameEn;
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
