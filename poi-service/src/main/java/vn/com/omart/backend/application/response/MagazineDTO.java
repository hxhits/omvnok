package vn.com.omart.backend.application.response;

import java.util.Date;

import lombok.Data;
import vn.com.omart.backend.domain.model.Magazine;

@Data
public class MagazineDTO {

	private Long id;

	private Long catId;

	private String title;

	private String desc;

	private String thumbUrl;

	private String link;

	private String source;

	private Date createdAt;

	private String createdBy;

	private Date timestamp;

	private String userId;

	private String hash;

	public static MagazineDTO from(Magazine dto) {

		MagazineDTO entity = new MagazineDTO();

		entity.setId(dto.getId());
		entity.setCatId(dto.getCatId());
		entity.setTitle(dto.getTitle());
		entity.setDesc(dto.getDesc());
		entity.setThumbUrl(dto.getThumbUrl());
		entity.setLink(dto.getLink());
		entity.setSource(dto.getSource());
		entity.setCreatedBy(dto.getCreatedBy());
		entity.setCreatedAt(dto.getCreatedAt());
		entity.setTimestamp(dto.getTimestamp());
		entity.setHash(dto.getHash());

		return entity;
	}

	public MagazineDTO(Long id) {
		super();
		this.id = id;
	}

	public MagazineDTO() {
		super();
	}

	public static Magazine toEntity(MagazineDTO dto) {
		Magazine entity = new Magazine();

		entity.setId(dto.getId());
		entity.setCatId(dto.getCatId());
		entity.setTitle(dto.getTitle());
		entity.setDesc(dto.getDesc());
		entity.setThumbUrl(dto.getThumbUrl());
		entity.setLink(dto.getLink());
		entity.setSource(dto.getSource());
		entity.setCreatedBy(dto.getCreatedBy());
		entity.setCreatedAt(dto.getCreatedAt());
		entity.setTimestamp(dto.getTimestamp());
		entity.setHash(dto.getHash());

		return entity;
	}

}
