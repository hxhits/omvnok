package vn.com.omart.backend.domain.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.FieldResult;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedStoredProcedureQueries;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.vdurmont.emoji.EmojiParser;

@Entity
@Table(name = "omart_news")
@SqlResultSetMapping(name = "newsresultset", entities = @EntityResult(entityClass = News.class, fields = {
		@FieldResult(name = "id", column = "id"), 
		@FieldResult(name = "title", column = "title"),
		@FieldResult(name = "desc", column = "description"),
		@FieldResult(name = "thumbnailUrl", column = "thumbnail_url"),
		@FieldResult(name = "bannerUrl", column = "banner_url"), 
		@FieldResult(name = "newsType", column = "news_type"),
		@FieldResult(name = "read", column = "is_read"), 
		@FieldResult(name = "createdBy", column = "created_by"),
		@FieldResult(name = "createdAt", column = "created_at"),
		@FieldResult(name = "updatedBy", column = "updated_by"),
		@FieldResult(name = "updatedAt", column = "updated_at"),
		@FieldResult(name = "latitude", column = "latitude"),
		@FieldResult(name = "longitude", column = "longitude"),
		@FieldResult(name = "approval", column = "approval"),
		@FieldResult(name = "poi", column = "poi_id"),
		@FieldResult(name = "lastNotification", column = "last_notification") 
		}), 
		columns = @ColumnResult(name = "distance", type = Double.class))

@NamedStoredProcedureQueries({
		@NamedStoredProcedureQuery(name = "geo_dist", procedureName = "geodistance_news", resultSetMappings = "newsresultset", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "orig_latitude", type = Double.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "orig_longitude", type = Double.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "radius", type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "paging", type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "size", type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "approval", type = Integer.class) }),
		@NamedStoredProcedureQuery(name = "geo_dist_new_enhance", procedureName = "geodistance_news_enhance", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "orig_latitude", type = Double.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "orig_longitude", type = Double.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "radius", type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "paging", type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "size", type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "approval", type = Integer.class) }),
		@NamedStoredProcedureQuery(name = "geo_dist_new_enhance_v1", procedureName = "geodistance_news_enhance_v1", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "orig_latitude", type = Double.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "orig_longitude", type = Double.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "radius", type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "paging", type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "size", type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "approval", type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "categoryIds", type = String.class) }) })
public class News {

	@Id
	@Column(name = "id", columnDefinition = "int")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "title", columnDefinition = "varchar")
	private String title;

	@Column(name = "description", columnDefinition = "text")
	private String desc;

	@Column(name = "thumbnail_url", columnDefinition = "varchar")
	private String thumbnailUrl;

	@Column(name = "banner_url", columnDefinition = "varchar")
	private String bannerUrl;

	@Column(name = "news_type", columnDefinition = "tinyint")
	private int newsType;

	@Column(name = "is_read", columnDefinition = "bit")
	private boolean read;

	@Column(name = "created_by", columnDefinition = "varchar")
	private String createdBy;

	@Column(name = "created_at", columnDefinition = "TIMESTAMP")
	private Date createdAt;

	@Column(name = "updated_by", columnDefinition = "varchar")
	private String updatedBy;

	@Column(name = "updated_at", columnDefinition = "TIMESTAMP")
	private Date updatedAt;

	@Column(name = "latitude", columnDefinition = "DOUBLE")
	private Double latitude;

	@Column(name = "longitude", columnDefinition = "DOUBLE")
	private Double longitude;

	@Column(name = "approval", columnDefinition = "bit")
	private int approval;

	@ManyToOne
	@JoinColumn(name = "poi_id", referencedColumnName = "id", columnDefinition = "int")
	private PointOfInterest poi;

	@Transient
	private int distance;

	@Transient
	private String userId;

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
		if (this.desc != null) {
			return EmojiParser.parseToUnicode(this.desc);
		}
		return null;
	}

	public void setDesc(String desc) {
		if (desc != null) {
			this.desc = EmojiParser.parseToAliases(desc);
		}
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	public String getBannerUrl() {
		return bannerUrl;
	}

	public void setBannerUrl(String bannerUrl) {
		this.bannerUrl = bannerUrl;
	}

	public int getNewsType() {
		return newsType;
	}

	public void setNewsType(int newsType) {
		this.newsType = newsType;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
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

	public int getApproval() {
		return approval;
	}

	public void setApproval(int approval) {
		this.approval = approval;
	}

	public PointOfInterest getPoi() {
		return poi;
	}

	public void setPoi(PointOfInterest poi) {
		this.poi = poi;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
