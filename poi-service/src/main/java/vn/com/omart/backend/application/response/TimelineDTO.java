package vn.com.omart.backend.application.response;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import vn.com.omart.backend.application.util.PoiActionStatus;
import vn.com.omart.backend.constants.ConstantUtils;
import vn.com.omart.backend.domain.model.Image;
import vn.com.omart.backend.domain.model.Timeline;
import vn.com.omart.backend.domain.model.TimelineComment;
import vn.com.omart.backend.domain.model.Video;

public class TimelineDTO {
	private Long id;
	private String userId;
	private List<Image> images;
	private String description;
	private int timelineType = 1;
	private Double latitude;
	private Double longitude;
	private Date createdAt;
	private int likes;
	private int comments;
	private String color;
	private String fontStyle;
	private int fontSize;
	private String href;
	private String hrefTitle;
	private boolean isDeleted;
	private boolean isPrivated;
	private boolean isMoment;
	private int momentRadius;
	private List<TimelineCommentDTO> timelineComments;
	private UserDTO user;
	private boolean isYourLike = false;
	private String customUserName;
	private String customUserAvatar;
	private Long videoDuration;
	private List<Video> videos;
	@JsonIgnore
	public static Set<String> userIds = new HashSet<String>();
	private String placeName;
	private PointOfInterestDTO poi;
	private Long poiId = 0L;
	
	public TimelineDTO() {
	}

	public TimelineDTO(Long id) {
		this.id = id;
	}

	public static void objToDto(Object[] objs) {
		TimelineDTO dto = new TimelineDTO();
		dto.setId(Long.parseLong(String.valueOf(objs[0])));
		dto.setUserId(String.valueOf(objs[1]));
	}

	private static TimelineDTO mappingToDTO(Timeline entity) {
		TimelineDTO dto = new TimelineDTO();
		dto.setUserId(entity.getUserId());
		dto.setId(entity.getId());
		dto.setDescription(entity.getDescription());
		dto.setImages(entity.getImages());
		dto.setTimelineType(entity.getTimelineType());
		dto.setCreatedAt(entity.getCreatedAt());
		dto.setLatitude(entity.getLatitude());
		dto.setLongitude(entity.getLongitude());
		dto.setColor(entity.getColor());
		dto.setFontSize(entity.getFontSize());
		dto.setFontStyle(entity.getFontStyle());
		dto.setHref(entity.getHref());
		dto.setHrefTitle(entity.getHrefTitle());
		dto.setLikes(entity.getLikes());
		dto.setComments(entity.getComments());
		dto.setPrivated(entity.isPrivated());
		dto.setMoment(entity.isMoment());
		dto.setMomentRadius(entity.getMomentRadius());
		dto.setVideos(entity.getVideos());
		return dto;
	}

	public static Timeline toEntity(TimelineDTO dto) {
		Timeline entity = new Timeline();
		if (dto.getId() != null) {
			entity.setId(dto.getId());
		}
		entity.setUserId(dto.getUserId());
		if (dto.getDescription() != null) {
			entity.setDescription(dto.getDescription());
		}
		if (dto.getImages() != null) {
			entity.setImages(dto.getImages());
		} else {
			entity.setImages(new ArrayList<>());
		}
		entity.setTimelineType(dto.getTimelineType());// type = 3 is shop post
		entity.setLatitude(dto.getLatitude());
		entity.setLongitude(dto.getLongitude());
		entity.setColor(dto.getColor());
		entity.setFontSize(dto.getFontSize());
		entity.setFontStyle(dto.getFontStyle());
		entity.setHref(dto.getHref());
		entity.setHrefTitle(dto.getHrefTitle());
		entity.setCustomUserAvatar(StringUtils.isBlank(dto.getCustomUserAvatar()) ? "" : dto.getCustomUserAvatar());
		entity.setCustomUserName(StringUtils.isBlank(dto.getCustomUserName()) ? "" : dto.getCustomUserName());
		entity.setPrivated(dto.isPrivated());
		entity.setMoment(dto.isMoment());
		entity.setMomentRadius(dto.getMomentRadius() <= 0 ? ConstantUtils.MYTL_RADIUS : dto.getMomentRadius());
		if (dto.getVideos() != null) {
			entity.setVideos(dto.getVideos());
		} else {
			entity.setVideos(new ArrayList<>());
		}
		return entity;
	}

	private static List<TimelineComment> getCommentLitmited(List<TimelineComment> comments, int limit) {
		int n = comments.size();
		if (n >= limit) {
			comments = comments.stream().skip(n - limit).limit(limit).collect(Collectors.toList());
		} else {
			comments = comments.stream().skip(0).limit(limit).collect(Collectors.toList());
		}
		return comments;
	}

	public static TimelineDTO toDTO(String userId, Timeline entity) {
		TimelineDTO dto = new TimelineDTO();
		dto = mappingToDTO(entity);
		userIds.add(entity.getUserId());
		// only get 5 newest comments.
		List<TimelineComment> comments = entity.getTimelineComments();
		comments = getCommentLitmited(comments, 5);
		dto.setTimelineComments(comments.stream().map(TimelineCommentDTO::toDTO).collect(Collectors.toList()));

		// double check is your like.
		boolean isYourLike = entity.getTimelineActions().stream().anyMatch(
				tlact -> tlact.getUserId().equals(userId) & tlact.getActionType() == PoiActionStatus.LIKE.getId());
		dto.setYourLike(isYourLike);
		// user of omart system
		if (!entity.getCustomUserName().isEmpty() && !entity.getCustomUserAvatar().isEmpty()) {
			UserDTO userOmart = new UserDTO();
			userOmart.setAvatar(entity.getCustomUserAvatar());
			userOmart.setUserName(entity.getCustomUserName());
			userOmart.setUserId(entity.getUserId());
			dto.setUser(userOmart);
		}
		return dto;
	}

	public static TimelineDTO toDTO(Timeline entity) {
		TimelineDTO dto = new TimelineDTO();
		dto = mappingToDTO(entity);
		dto.setDeleted(entity.isDeleted());
		userIds.add(entity.getUserId());
		// user of omart system
		if (!entity.getCustomUserName().isEmpty() && !entity.getCustomUserAvatar().isEmpty()) {
			UserDTO userOmart = new UserDTO();
			userOmart.setAvatar(entity.getCustomUserAvatar());
			userOmart.setUserName(entity.getCustomUserName());
			userOmart.setUserId(entity.getUserId());
			dto.setUser(userOmart);
		}
		return dto;
	}

	private static String getStringValue(Object obj) {
		return (obj == null ? "" : String.valueOf(obj));
	}

	private static int getIntValue(Object obj) {
		return (obj == null ? 0 : Integer.valueOf(String.valueOf(obj)));
	}

	private static Date getDateValue(Object obj) {
		return (obj == null ? null : (Date) obj);
	}

	private static Double getDoubleValue(Object obj) {
		return (obj == null ? null : (Double) obj);
	}

	public static TimelineDTO toDTO(Object[] objs) {
		TimelineDTO dto = new TimelineDTO();
		dto.setColor(getStringValue(objs[0]));
		dto.setComments(getIntValue(objs[1]));
		dto.setCreatedAt(getDateValue(objs[2]));
		dto.setCustomUserAvatar(getStringValue(objs[3]));
		dto.setCustomUserName(getStringValue(objs[4]));
		dto.setDescription(getStringValue(objs[5]));
		dto.setFontSize(getIntValue(objs[6]));
		dto.setFontStyle(getStringValue(objs[7]));
		dto.setHref(getStringValue(objs[8]));
		dto.setHrefTitle(getStringValue(objs[9]));
		dto.setId(Long.valueOf(String.valueOf(objs[10])));
		Object b1 = objs[11];
		Object b2 = objs[12];
		// dto.setImages(entity.getImages());
		// is deleted
		dto.setLatitude(getDoubleValue(objs[13]));
		dto.setLikes(getIntValue(objs[14]));
		dto.setLongitude(getDoubleValue(objs[15]));
		Object b3 = objs[16];
		Object b4 = objs[17];
		// action
		// comment
		dto.setTimelineType(getIntValue(objs[18]));
		dto.setUserId(String.valueOf(objs[19]));
		return dto;
	}

	public boolean isYourLike() {
		return isYourLike;
	}

	public void setYourLike(boolean isYourLike) {
		this.isYourLike = isYourLike;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public List<Image> getImages() {
		return images;
	}

	public void setImages(List<Image> images) {
		this.images = images;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getTimelineType() {
		return timelineType;
	}

	public void setTimelineType(int timelineType) {
		this.timelineType = timelineType;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public List<TimelineCommentDTO> getTimelineComments() {
		return timelineComments;
	}

	public void setTimelineComments(List<TimelineCommentDTO> timelineComments) {
		this.timelineComments = timelineComments;
	}

	public static Set<String> getUserIds() {
		return userIds;
	}

	public static void setUserIds(Set<String> userIds) {
		TimelineDTO.userIds = userIds;
	}

	public UserDTO getUser() {
		return user;
	}

	public void setUser(UserDTO user) {
		this.user = user;
	}

	public int getLikes() {
		return likes;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}

	public int getComments() {
		return comments;
	}

	public void setComments(int comments) {
		this.comments = comments;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getFontStyle() {
		return fontStyle;
	}

	public void setFontStyle(String fontStyle) {
		this.fontStyle = fontStyle;
	}

	public int getFontSize() {
		return fontSize;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getHrefTitle() {
		return hrefTitle;
	}

	public void setHrefTitle(String hrefTitle) {
		this.hrefTitle = hrefTitle;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getCustomUserName() {
		return customUserName;
	}

	public void setCustomUserName(String customUserName) {
		this.customUserName = customUserName;
	}

	public String getCustomUserAvatar() {
		return customUserAvatar;
	}

	public void setCustomUserAvatar(String customUserAvatar) {
		this.customUserAvatar = customUserAvatar;
	}

	public boolean isPrivated() {
		return isPrivated;
	}

	public void setPrivated(boolean isPrivated) {
		this.isPrivated = isPrivated;
	}

	public boolean isMoment() {
		return isMoment;
	}

	public void setMoment(boolean isMoment) {
		this.isMoment = isMoment;
	}

	public int getMomentRadius() {
		return momentRadius;
	}

	public void setMomentRadius(int momentRadius) {
		this.momentRadius = momentRadius;
	}

	public Long getVideoDuration() {
		return videoDuration;
	}

	public void setVideoDuration(Long videoDuration) {
		this.videoDuration = videoDuration;
	}

	public List<Video> getVideos() {
		return videos;
	}

	public void setVideos(List<Video> videos) {
		this.videos = videos;
	}

	public String getPlaceName() {
		return placeName;
	}

	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}

	public PointOfInterestDTO getPoi() {
		return poi;
	}

	public void setPoi(PointOfInterestDTO poi) {
		this.poi = poi;
	}

	public Long getPoiId() {
		return poiId;
	}

	public void setPoiId(Long poiId) {
		this.poiId = poiId;
	}
	
}
