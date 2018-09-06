package vn.com.omart.backend.application.response;

import java.util.Date;

import org.json.simple.JSONObject;

import lombok.Data;
import vn.com.omart.backend.domain.model.News;

@Data
public class NewsDTO {

	private Long id;

	private String title;

	private String desc;

	private String thumbnailUrl;

	private String bannerUrl;

	private int type;

	private boolean read;

	private String createdBy;

	private Date createdAt;

	private String updatedBy;

	private Date updatedAt;

	private String userId;

	private double longitude;

	private double latitude;

	private int approval;

	private Long poiId;
	
	private int discount;

	private int distance;

	private JSONObject userAction;
	
	private boolean isActionLike;

	private Date lastNotification;
	
	public static NewsDTO from(News dto) {

		NewsDTO entity = new NewsDTO();

		entity.setId(dto.getId());

		entity.setTitle(dto.getTitle());
		entity.setDesc(dto.getDesc());

		entity.setThumbnailUrl(dto.getThumbnailUrl());
		entity.setBannerUrl(dto.getBannerUrl());

		entity.setRead(dto.isRead());
		entity.setType(dto.getNewsType());

		entity.setCreatedAt(dto.getCreatedAt());
		entity.setCreatedBy(dto.getCreatedBy());

		entity.setUpdatedBy(dto.getUpdatedBy());
		entity.setUpdatedAt(dto.getUpdatedAt());

		entity.setApproval(dto.getApproval());
		if (dto.getLatitude() != null) {
			entity.setLatitude(dto.getLatitude());
		}
		if (dto.getLongitude() != null) {
			entity.setLongitude(dto.getLongitude());
		}
		if (dto.getPoi() != null) {
			entity.setPoiId(dto.getPoi().id());
		}
		entity.setDistance(dto.getDistance());
		return entity;
	}

	public NewsDTO(Long id) {
		super();
		this.id = id;
	}

	public NewsDTO() {
		super();
		this.id = id;
	}

	public NewsDTO(Long id, String title, String desc, String thumbnailUrl, String bannerUrl, int type, boolean read) {
		super();
		this.id = id;
		this.title = title;
		this.desc = desc;
		this.thumbnailUrl = thumbnailUrl;
		this.bannerUrl = bannerUrl;
		this.type = type;
		this.read = read;
	}

	public NewsDTO(Long poiId,String title, String desc, String thumbnailUrl, String bannerUrl) {
		super();
		this.poiId = poiId;
		this.title = title;
		this.desc = desc;
		this.thumbnailUrl = thumbnailUrl;
		this.bannerUrl = bannerUrl;
	}
	

}
