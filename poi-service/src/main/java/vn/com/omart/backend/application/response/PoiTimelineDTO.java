package vn.com.omart.backend.application.response;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Data;
import vn.com.omart.backend.application.util.DateUtils;
import vn.com.omart.backend.application.util.PoiActionStatus;
import vn.com.omart.backend.domain.model.Image;
import vn.com.omart.backend.domain.model.PoiPicture;
import vn.com.omart.backend.port.adapter.support.mysql.JpaImageConverter;

@Data
public class PoiTimelineDTO {

	private Long id;
	private String userId;
	private Long poiId;
	private List<Image> images;
	private String title;
	private Long createdAt;
	private int likeNumber;
	private int commentNumber;
	private String description;
	private Double latitude;
	private Double longitude;
	private int fontSize;
	private String fontStyle;
	private String color;
	private String href;
	private String hrefTitle;
	private boolean isDeleted;
	private boolean isYourLike = false;
	private List<PictureCommentDTO> Comments;

	// Get last number of Comments
	private static int GET_COMMENTS = 5;
	
	// Convert Images
	private static JpaImageConverter JIC = new JpaImageConverter();

	/**
	 * Convert Entity to DTO
	 * 
	 * @param entity
	 * @return
	 */
	public static PoiTimelineDTO from(PoiPicture entity, String userId) {
		PoiTimelineDTO dto = new PoiTimelineDTO();
		// Comments
		if (entity.getPictureComments() != null) {
			if (entity.getPictureComments().size() <= GET_COMMENTS) {
				dto.setComments(entity.getPictureComments().stream().map(PictureCommentDTO::from)
						.collect(Collectors.toList()));
			} else {
				int n = entity.getPictureComments().size();
				dto.setComments(entity.getPictureComments().stream().skip(n - GET_COMMENTS)
						.map(PictureCommentDTO::from).collect(Collectors.toList()));
			}
		}

		// Is Your Like?
		if (userId != null && entity.getPictureActions() != null) {
			boolean isYourLike = entity.getPictureActions().stream().anyMatch(
					item -> item.getUserId().equals(userId) & item.getActionType() == PoiActionStatus.LIKE.getId());
			dto.setYourLike(isYourLike);
		}

		dto.setId(entity.getId());
		dto.setUserId(entity.getUserId());
		dto.setPoiId(entity.getPoi() != null ? entity.getPoi().id() : 0);
		dto.setImages(entity.getUrls());
		dto.setTitle(entity.getTitle());
		dto.setCreatedAt(entity.getCreatedAt() != null ? entity.getCreatedAt().getTime() : 0);
		dto.setLikeNumber(entity.getLikeNumber());
		dto.setCommentNumber(entity.getCommentNumber());
		dto.setDescription(entity.getDescription());
		dto.setLatitude(entity.getLatitude());
		dto.setLongitude(entity.getLongitude());
		dto.setFontSize(entity.getFontSize());
		dto.setFontStyle(entity.getFontStyle());
		dto.setColor(entity.getColor());
		dto.setHref(entity.getHref());
		dto.setHrefTitle(entity.getHrefTitle());
		dto.setDeleted(entity.isDeleted());

		return dto;
	}
	
//	public static PoiTimelineDTO from(PoiPicture entity, String userId) {
//		PoiTimelineDTO dto = new PoiTimelineDTO();
//
//		List<Image> tmp = new ArrayList<Image>();
//		// Old Data
//		if (null != entity.getUrl() && !entity.getUrl().isEmpty()
//				&& !entity.getUrl().equals("[]") && !entity.getUrl().startsWith("[")) {
//			tmp.add(new Image(entity.getUrl()));
//		} else {
//			tmp = JIC.convertToEntityAttribute(entity.getUrl());
//		}
//
//		// Comments
//		if (entity.getPictureComments() != null) {
//			if (entity.getPictureComments().size() <= GET_COMMENTS) {
//				dto.setComments(entity.getPictureComments().stream().map(PictureCommentDTO::from)
//						.collect(Collectors.toList()));
//			} else {
//				int n = entity.getPictureComments().size();
//				dto.setComments(entity.getPictureComments().stream().skip(n - GET_COMMENTS)
//						.map(PictureCommentDTO::from).collect(Collectors.toList()));
//			}
//		}
//
//		// Is Your Like?
//		if (userId != null && entity.getPictureActions() != null) {
//			boolean isYourLike = entity.getPictureActions().stream().anyMatch(
//					item -> item.getUserId().equals(userId) & item.getActionType() == PoiActionStatus.LIKE.getId());
//			dto.setYourLike(isYourLike);
//		}
//
//		dto.setId(entity.getId());
//		dto.setUserId(entity.getUserId());
//		dto.setPoiId(entity.getPoi() != null ? entity.getPoi().id() : 0);
//		dto.setImages(tmp);
//		dto.setTitle(entity.getTitle());
//		dto.setCreatedAt(entity.getCreatedAt() != null ? entity.getCreatedAt().getTime() : 0);
//		dto.setLikeNumber(entity.getLikeNumber());
//		dto.setCommentNumber(entity.getCommentNumber());
//		dto.setDescription(entity.getDescription());
//		dto.setLatitude(entity.getLatitude());
//		dto.setLongitude(entity.getLongitude());
//		dto.setFontSize(entity.getFontSize());
//		dto.setFontStyle(entity.getFontStyle());
//		dto.setColor(entity.getColor());
//		dto.setHref(entity.getHref());
//		dto.setHrefTitle(entity.getHrefTitle());
//		dto.setDeleted(entity.isDeleted());
//
//		return dto;
//	}

	/**
	 * Convert DTO to Entity
	 * 
	 * @param entity
	 * @return
	 */
	public static PoiPicture to(PoiTimelineDTO dto) {
		PoiPicture entity = new PoiPicture();
		entity = to(dto, entity);
		entity.setCreatedAt(DateUtils.getCurrentDate());
		return entity;
	}

	/**
	 * Update DTO to Entity
	 * 
	 * @param dto
	 * @param entity
	 * @return
	 */
	public static PoiPicture to(PoiTimelineDTO dto, PoiPicture entity) {
		
		if (dto.getImages() != null) {
			entity.setUrls(dto.getImages());
		} else {
			entity.setUrls(new ArrayList<>());
		}
		entity.setTitle(dto.getTitle());
		entity.setDescription(dto.getDescription());
		entity.setLatitude(dto.getLatitude());
		entity.setLongitude(dto.getLongitude());
		entity.setFontSize(dto.getFontSize());
		entity.setFontStyle(dto.getFontStyle());
		entity.setColor(dto.getColor());
		entity.setHref(dto.getHref());
		entity.setHrefTitle(dto.getHrefTitle());
		entity.setDeleted(dto.isDeleted());

		return entity;
	}
	
//	public static PoiPicture to(PoiTimelineDTO dto, PoiPicture entity) {
//		if (dto.getImages() != null) {
//			entity.setUrl(JIC.convertToDatabaseColumn(dto.getImages()));
//		} else {
//			entity.setUrl(JIC.convertToDatabaseColumn(new ArrayList<>()));
//		}
//		entity.setTitle(dto.getTitle());
//		entity.setDescription(dto.getDescription());
//		entity.setLatitude(dto.getLatitude());
//		entity.setLongitude(dto.getLongitude());
//		entity.setFontSize(dto.getFontSize());
//		entity.setFontStyle(dto.getFontStyle());
//		entity.setColor(dto.getColor());
//		entity.setHref(dto.getHref());
//		entity.setHrefTitle(dto.getHrefTitle());
//		entity.setDeleted(dto.isDeleted());
//
//		return entity;
//	}

}
