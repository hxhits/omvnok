package vn.com.omart.backend.domain.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.FetchType;
import javax.persistence.FieldResult;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQueries;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.OneToMany;
import javax.persistence.ParameterMode;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Table;

import com.vdurmont.emoji.EmojiParser;

import vn.com.omart.backend.port.adapter.support.mysql.JpaImageConverter;
import vn.com.omart.backend.port.adapter.support.mysql.JpaVideoConverter;

@Entity
@Table(name = "omart_timeline")
@SqlResultSetMappings({
		@SqlResultSetMapping(name = "timeline_resultset", entities = @EntityResult(entityClass = Timeline.class, fields = {
				@FieldResult(name = "id", column = "id"), @FieldResult(name = "userId", column = "userId"),
				@FieldResult(name = "images", column = "images"),
				@FieldResult(name = "description", column = "description"),
				@FieldResult(name = "timelineType", column = "timelineType"),
				@FieldResult(name = "latitude", column = "latitude"),
				@FieldResult(name = "longitude", column = "longitude"),
				@FieldResult(name = "createdAt", column = "createdAt"),
				@FieldResult(name = "likes", column = "likeCount"),
				@FieldResult(name = "comments", column = "commentCount"),
				@FieldResult(name = "fontSize", column = "fontSize"),
				@FieldResult(name = "fontStyle", column = "fontStyle"), @FieldResult(name = "color", column = "color"),
				@FieldResult(name = "href", column = "href"), @FieldResult(name = "hrefTitle", column = "hrefTitle"),
				@FieldResult(name = "isDeleted", column = "isDeleted"),
				@FieldResult(name = "customUserAvatar", column = "customUserAvatar"),
				@FieldResult(name = "customUserName", column = "customUserName"),
				@FieldResult(name = "updatedAt", column = "updatedAt"),
				@FieldResult(name = "isPrivated", column = "isPrivated"),
				@FieldResult(name = "isMoment", column = "isMoment"),
				@FieldResult(name = "momentRadius", column = "momentRadius"),
				@FieldResult(name = "videos", column = "videos"),
				@FieldResult(name = "placeName", column = "placeName"),
				@FieldResult(name = "isSaveHistory", column = "isSaveHistory"),
				@FieldResult(name = "poiId", column = "poiId"),
				@FieldResult(name = "isReportAbuse", column = "isReportAbuse") })),
		// @SqlResultSetMapping(name = "timeline_resultset2", entities =
		// @EntityResult(entityClass = Timeline.class)),
		/*
		 * THIS IS A WAY TO MAPPING DTO CLASS TO resultSetMappings OF
		 * StoredProcedureQuery
		 * 
		 * @SqlResultSetMapping(name="timeline_resultset1",classes=@ConstructorResult(
		 * targetClass=Timeline.class,columns = {
		 * 
		 * @ColumnResult(name = "id",type=Long.class),
		 * 
		 * @ColumnResult(name = "userId",type=String.class) }))
		 */
})

@NamedStoredProcedureQueries({
		@NamedStoredProcedureQuery(name = "geo_dist_timeline_v1", procedureName = "geodistance_timeline_v1", resultSetMappings = {
				"timeline_resultset" }, parameters = {
						@StoredProcedureParameter(mode = ParameterMode.IN, name = "orig_latitude", type = Double.class),
						@StoredProcedureParameter(mode = ParameterMode.IN, name = "orig_longitude", type = Double.class),
						@StoredProcedureParameter(mode = ParameterMode.IN, name = "radius", type = Integer.class),
						@StoredProcedureParameter(mode = ParameterMode.IN, name = "paging", type = Integer.class),
						@StoredProcedureParameter(mode = ParameterMode.IN, name = "size", type = Integer.class) }),
		@NamedStoredProcedureQuery(name = "geo_dist_timeline_v2", procedureName = "geodistance_timeline_v2", resultSetMappings = {
				"timeline_resultset" }, parameters = {
						@StoredProcedureParameter(mode = ParameterMode.IN, name = "orig_type", type = Integer.class),
						@StoredProcedureParameter(mode = ParameterMode.IN, name = "orig_latitude", type = Double.class),
						@StoredProcedureParameter(mode = ParameterMode.IN, name = "orig_longitude", type = Double.class),
						@StoredProcedureParameter(mode = ParameterMode.IN, name = "radius", type = Integer.class),
						@StoredProcedureParameter(mode = ParameterMode.IN, name = "paging", type = Integer.class),
						@StoredProcedureParameter(mode = ParameterMode.IN, name = "size", type = Integer.class) }),
		@NamedStoredProcedureQuery(name = "geo_dist_timeline_v4", procedureName = "geodistance_timeline_v4", resultSetMappings = {
				"timeline_resultset" }, parameters = {
						@StoredProcedureParameter(mode = ParameterMode.IN, name = "orig_user_id", type = String.class),
						@StoredProcedureParameter(mode = ParameterMode.IN, name = "orig_types", type = String.class),
						@StoredProcedureParameter(mode = ParameterMode.IN, name = "orig_latitude", type = Double.class),
						@StoredProcedureParameter(mode = ParameterMode.IN, name = "orig_longitude", type = Double.class),
						@StoredProcedureParameter(mode = ParameterMode.IN, name = "radius", type = Integer.class),
						@StoredProcedureParameter(mode = ParameterMode.IN, name = "paging", type = Integer.class),
						@StoredProcedureParameter(mode = ParameterMode.IN, name = "size", type = Integer.class) }),

		@NamedStoredProcedureQuery(name = "geo_dist_timeline_v5", procedureName = "geodistance_timeline_v5", resultSetMappings = {
				"timeline_resultset" }, parameters = {
						@StoredProcedureParameter(mode = ParameterMode.IN, name = "orig_user_id", type = String.class),
						@StoredProcedureParameter(mode = ParameterMode.IN, name = "friend_str", type = String.class),
						@StoredProcedureParameter(mode = ParameterMode.IN, name = "paging", type = Integer.class),
						@StoredProcedureParameter(mode = ParameterMode.IN, name = "size", type = Integer.class) }),

		@NamedStoredProcedureQuery(name = "geo_dist_my_timeline_v2", procedureName = "geodistance_my_timeline_v2", resultSetMappings = "timeline_resultset", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "orig_user_id", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "orig_types", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "orig_latitude", type = Double.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "orig_longitude", type = Double.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "radius", type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "paging", type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "size", type = Integer.class) }),

		@NamedStoredProcedureQuery(name = "geo_dist_my_timeline_v3", procedureName = "geodistance_my_timeline_v3", resultSetMappings = "timeline_resultset", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "orig_user_id", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "orig_types", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "orig_latitude", type = Double.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "orig_longitude", type = Double.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "paging", type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "size", type = Integer.class) })

})
public class Timeline implements Serializable {

	private static final long serialVersionUID = -8750862553892000566L;

	@Id
	@GeneratedValue
	@Column(name = "id", columnDefinition = "int")
	private Long id;

	@Column(name = "user_id", columnDefinition = "varchar")
	private String userId;

	@Column(name = "images", columnDefinition = "json")
	@Convert(converter = JpaImageConverter.class)
	private List<Image> images;

	@Column(name = "description", columnDefinition = "text")
	private String description;

	@Column(name = "timeline_type", columnDefinition = "int")
	private int timelineType;

	@Column(name = "latitude", columnDefinition = "double")
	private Double latitude;

	@Column(name = "longitude", columnDefinition = "double")
	private Double longitude;

	@Column(name = "created_at", columnDefinition = "timestamp")
	private Date createdAt;

	@Column(name = "updated_at", columnDefinition = "timestamp")
	private Date updatedAt;

	@Column(name = "like_count", columnDefinition = "int")
	private int likes;

	@Column(name = "comment_count", columnDefinition = "int")
	private int comments;

	@Column(name = "font_size", columnDefinition = "int")
	private int fontSize;

	@Column(name = "font_style", columnDefinition = "varchar")
	private String fontStyle;

	@Column(name = "color", columnDefinition = "varchar")
	private String color;

	@Column(name = "href", columnDefinition = "varchar")
	private String href;

	@Column(name = "href_title", columnDefinition = "varchar")
	private String hrefTitle;

	@Column(name = "is_deleted", columnDefinition = "bit")
	private boolean isDeleted;

	@Column(name = "is_privated", columnDefinition = "bit")
	private boolean isPrivated;

	@Column(name = "is_moment", columnDefinition = "bit")
	private boolean isMoment;

	@Column(name = "moment_radius", columnDefinition = "int")
	private int momentRadius;

	@Column(name = "custom_user_avatar", columnDefinition = "varchar")
	private String customUserAvatar;

	@Column(name = "custom_user_name", columnDefinition = "varchar")
	private String customUserName;

	@Column(name = "place_name", columnDefinition = "varchar")
	private String placeName;

	@Column(name = "is_save_history", columnDefinition = "bit")
	private boolean isSaveHistory;

	@Column(name = "poi_id", columnDefinition = "int")
	private Long poiId;

	@Column(name = "videos", columnDefinition = "json")
	@Convert(converter = JpaVideoConverter.class)
	private List<Video> videos;

	@Column(name = "is_report_abuse", columnDefinition = "bit")
	private boolean isReportAbuse;

	@OneToMany(mappedBy = "timeline", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
	private List<TimelineComment> timelineComments;

	@OneToMany(mappedBy = "timeline", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
	private List<TimelineAction> timelineActions;

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
		if (this.description != null) {
			return EmojiParser.parseToUnicode(description);
		}
		return null;
	}

	public void setDescription(String description) {
		if (description != null) {
			this.description = EmojiParser.parseToAliases(description);
		}
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

	public List<TimelineComment> getTimelineComments() {
		return timelineComments;
	}

	public void setTimelineComments(List<TimelineComment> timelineComments) {
		this.timelineComments = timelineComments;
	}

	public List<TimelineAction> getTimelineActions() {
		return timelineActions;
	}

	public void setTimelineActions(List<TimelineAction> timelineActions) {
		this.timelineActions = timelineActions;
	}

	public int getLikes() {
		return likes;
	}

	public void setLikes(int likes) {
		if (likes >= 0) {
			this.likes = likes;
		}
	}

	public int getComments() {
		return comments;
	}

	public void setComments(int comments) {
		this.comments = comments;
	}

	public int getFontSize() {
		return fontSize;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}

	public String getFontStyle() {
		return fontStyle;
	}

	public void setFontStyle(String fontStyle) {
		this.fontStyle = fontStyle;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
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

	public String getCustomUserAvatar() {
		return customUserAvatar;
	}

	public void setCustomUserAvatar(String customUserAvatar) {
		this.customUserAvatar = customUserAvatar;
	}

	public String getCustomUserName() {
		return customUserName;
	}

	public void setCustomUserName(String customUserName) {
		this.customUserName = customUserName;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
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

	public List<Video> getVideos() {
		return videos;
	}

	public void setVideos(List<Video> videos) {
		this.videos = videos;
	}

	public String getPlaceName() {
		if (this.placeName != null) {
			return EmojiParser.parseToUnicode(placeName);
		}
		return null;
	}

	public void setPlaceName(String placeName) {
		if (placeName != null) {
			this.placeName = EmojiParser.parseToAliases(placeName);
		}
	}

	public boolean isSaveHistory() {
		return isSaveHistory;
	}

	public void setSaveHistory(boolean isSaveHistory) {
		this.isSaveHistory = isSaveHistory;
	}

	public Long getPoiId() {
		return poiId;
	}

	public void setPoiId(Long poiId) {
		this.poiId = poiId;
	}

	public boolean isReportAbuse() {
		return isReportAbuse;
	}

	public void setReportAbuse(boolean isReportAbuse) {
		this.isReportAbuse = isReportAbuse;
	}

}
