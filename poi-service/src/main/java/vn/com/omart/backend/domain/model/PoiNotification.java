package vn.com.omart.backend.domain.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedStoredProcedureQueries;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.OneToOne;
import javax.persistence.ParameterMode;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Table;

import com.vdurmont.emoji.EmojiParser;

import vn.com.omart.backend.port.adapter.support.mysql.JpaImageConverter;

@Entity
@Table(name = "omart_poi_notification")
@SqlResultSetMappings({
		@SqlResultSetMapping(name = "PoiNoti", columns = {
				@ColumnResult(name = "id", type = Long.class), @ColumnResult(name = "poiId", type = Long.class),
				@ColumnResult(name = "catId", type = Long.class), @ColumnResult(name = "userId", type = String.class),
				@ColumnResult(name = "description", type = String.class), @ColumnResult(name = "images", type = String.class),
				@ColumnResult(name = "notificationType", type = Integer.class),
				@ColumnResult(name = "createdAt", type = Date.class), @ColumnResult(name = "name", type = String.class),
				@ColumnResult(name = "avatarImages", type = String.class), @ColumnResult(name = "address", type = String.class),
				@ColumnResult(name = "distance", type = Double.class), @ColumnResult(name = "phone", type = String.class),
				@ColumnResult(name = "latitude", type = Double.class), @ColumnResult(name = "longitude", type = Double.class),
				@ColumnResult(name = "recruitId", type = Long.class), @ColumnResult(name = "color", type = String.class),
				@ColumnResult(name = "fontStyle", type = String.class), @ColumnResult(name = "fontSize", type = Integer.class),
				@ColumnResult(name = "updatedAt", type = Date.class), @ColumnResult(name = "bookcarId", type = Long.class) }),

		@SqlResultSetMapping(name = "PoiNoti2", columns = {
				@ColumnResult(name = "id", type = Long.class),
				@ColumnResult(name = "poiId", type = Long.class),
				@ColumnResult(name = "catId", type = Long.class),
				@ColumnResult(name = "userId", type = String.class),
				@ColumnResult(name = "description", type = String.class),
				@ColumnResult(name = "images", type = String.class),
				@ColumnResult(name = "notificationType", type = Integer.class),
				@ColumnResult(name = "createdAt", type = Date.class),
				@ColumnResult(name = "name", type = String.class),
				@ColumnResult(name = "avatarImages", type = String.class),
				@ColumnResult(name = "address", type = String.class),
				@ColumnResult(name = "distance", type = Double.class),
				@ColumnResult(name = "phone", type = String.class),
				@ColumnResult(name = "latitude", type = Double.class),
				@ColumnResult(name = "longitude", type = Double.class),
				@ColumnResult(name = "recruitId", type = Long.class),
				@ColumnResult(name = "color", type = String.class),
				@ColumnResult(name = "fontStyle", type = String.class),
				@ColumnResult(name = "fontSize", type = Integer.class),
				@ColumnResult(name = "updatedAt", type = Date.class),
				@ColumnResult(name = "bookcarId", type = Long.class),
				@ColumnResult(name = "poiOwnerId", type = String.class) }) })

@NamedStoredProcedureQueries({
		@NamedStoredProcedureQuery(name = "geodist_poi_notification_v1", resultSetMappings = "PoiNoti", procedureName = "geodistance_poi_notification_v1", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "orig_latitude", type = Double.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "orig_longitude", type = Double.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "radius", type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "paging", type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "size", type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "categoryIds", type = String.class) }),

		@NamedStoredProcedureQuery(name = "geodist_poi_notification_v2", resultSetMappings = "PoiNoti", procedureName = "geodistance_poi_notification_v2", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "orig_latitude", type = Double.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "orig_longitude", type = Double.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "radius", type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "paging", type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "size", type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "categoryIds", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "provinceId", type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "districtIds", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "userId", type = String.class) }),

		@NamedStoredProcedureQuery(name = "geodist_poi_notification_v4", resultSetMappings = "PoiNoti2", procedureName = "geodistance_poi_notification_v4", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "orig_latitude", type = Double.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "orig_longitude", type = Double.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "radius", type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "paging", type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "size", type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "categoryIds", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "positionLevelIds", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "provinceId", type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "districtIds", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "notificationType", type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "userId", type = String.class) })

})

public class PoiNotification implements Serializable {

	private static final long serialVersionUID = -3540788604296025524L;

	@Id
	@GeneratedValue
	@Column(name = "id", columnDefinition = "int")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "poi_id", referencedColumnName = "id", columnDefinition = "int")
	private PointOfInterest poi;

	@ManyToOne
	@JoinColumn(name = "cat_id", referencedColumnName = "id", columnDefinition = "int")
	private Category category;

	@ManyToOne
	@JoinColumn(name = "pos_id", referencedColumnName = "id", columnDefinition = "int")
	private RecruitmentPositionLevel recruitmentPositionLevel;

	@ManyToOne
	@JoinColumn(name = "recruit_id", referencedColumnName = "id", columnDefinition = "int")
	private Recruitment recruit;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bookcar_id")
	private BookCar bookcar;

	@Column(name = "user_id", columnDefinition = "varchar")
	private String userId;

	@Column(name = "images", columnDefinition = "json")
	@Convert(converter = JpaImageConverter.class)
	private List<Image> images;

	@Column(name = "description", columnDefinition = "text")
	private String description;

	@Column(name = "notification_type", columnDefinition = "int")
	private int notificationType;

	@Column(name = "latitude", columnDefinition = "double")
	private Double latitude;

	@Column(name = "longitude", columnDefinition = "double")
	private Double longitude;

	@Column(name = "created_at", columnDefinition = "timestamp")
	private Date createdAt;

	@Column(name = "updated_at", columnDefinition = "timestamp")
	private Date updatedAt;

	@Column(name = "avatar_image", columnDefinition = "json")
	@Convert(converter = JpaImageConverter.class)
	private List<Image> avatarImages;

	@Column(name = "name", columnDefinition = "varchar")
	private String name;

	@Column(name = "address", columnDefinition = "varchar")
	private String address;

	@Column(name = "phone", columnDefinition = "varchar")
	private String phone;

	@Column(name = "is_deleted", columnDefinition = "bit")
	private boolean isDeleted;

	@Column(name = "font_size", columnDefinition = "int")
	private int fontSize;

	@Column(name = "font_style", columnDefinition = "varchar")
	private String fontStyle;

	@Column(name = "color", columnDefinition = "varchar")
	private String color;

	@Column(name = "province", columnDefinition = "int")
	private Long province;

	@Column(name = "district", columnDefinition = "int")
	private Long district;

	@Column(name = "is_active", columnDefinition = "bit")
	private boolean isActive;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PointOfInterest getPoi() {
		return poi;
	}

	public void setPoi(PointOfInterest poi) {
		this.poi = poi;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public void setRecruitmentPositionLevel(RecruitmentPositionLevel recruitmentPositionLevel) {
		this.recruitmentPositionLevel = recruitmentPositionLevel;
	}

	public RecruitmentPositionLevel getRecruitmentPositionLevel() {
		return recruitmentPositionLevel;
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

	public int getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(int notificationType) {
		this.notificationType = notificationType;
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

	public List<Image> getAvatarImages() {
		return avatarImages;
	}

	public void setAvatarImages(List<Image> avatarImages) {
		this.avatarImages = avatarImages;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Recruitment getRecruit() {
		return recruit;
	}

	public void setRecruit(Recruitment recruit) {
		this.recruit = recruit;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
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

	public BookCar getBookcar() {
		return bookcar;
	}

	public void setBookcar(BookCar bookcar) {
		this.bookcar = bookcar;
	}

	public Long getProvince() {
		return province;
	}

	public void setProvince(Long province) {
		this.province = province;
	}

	public Long getDistrict() {
		return district;
	}

	public void setDistrict(Long district) {
		this.district = district;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

}