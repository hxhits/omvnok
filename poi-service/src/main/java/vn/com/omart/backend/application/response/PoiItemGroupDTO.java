package vn.com.omart.backend.application.response;

import javax.validation.constraints.NotNull;

import vn.com.omart.backend.domain.model.PoiItemGroup;

public class PoiItemGroupDTO {
	
	private Long id;
	@NotNull
	private String name;
	@NotNull
	private Long poiId;
	@NotNull
	private String avatar;
	
	public static PoiItemGroup toEntity(PoiItemGroupDTO dto) {
		PoiItemGroup entity = new PoiItemGroup();
		entity.setName(dto.getName());
		entity.setAvatar(dto.getAvatar());
		return entity;
	}
	
	public static PoiItemGroup toEntity(PoiItemGroup entity,PoiItemGroupDTO dto) {
		entity.setName(dto.getName());
		entity.setAvatar(dto.getAvatar());
		return entity;
	}
	
	public static PoiItemGroupDTO toBasicDTO(PoiItemGroup entity) {
		PoiItemGroupDTO dto = new PoiItemGroupDTO();
		dto.setId(entity.getId());
		dto.setName(entity.getName());
		dto.setAvatar(entity.getAvatar());
		return dto;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public Long getPoiId() {
		return poiId;
	}

	public void setPoiId(Long poiId) {
		this.poiId = poiId;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	
}
