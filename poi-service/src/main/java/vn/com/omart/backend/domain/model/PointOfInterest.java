package vn.com.omart.backend.domain.model;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedStoredProcedureQueries;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.ParameterMode;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.vdurmont.emoji.EmojiParser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;
import vn.com.omart.backend.port.adapter.support.mysql.JpaImageConverter;

@Entity
@Table(name = "omart_point_of_interest")
@ToString
@Builder
@AllArgsConstructor
@SqlResultSetMappings({
		@SqlResultSetMapping(name = "POIAndDistance", entities = @EntityResult(entityClass = PointOfInterest.class), columns = @ColumnResult(name = "distance", type = Double.class)),
		@SqlResultSetMapping(name = "PoiNearest", columns = { @ColumnResult(name = "id", type = Long.class),
				@ColumnResult(name = "name", type = String.class),
				@ColumnResult(name = "description", type = String.class),
				@ColumnResult(name = "avatarImage", type = String.class),
				@ColumnResult(name = "coverImage", type = String.class),
				@ColumnResult(name = "ownerId", type = String.class),
				@ColumnResult(name = "openHour", type = Double.class),
				@ColumnResult(name = "closeHour", type = Double.class),
				@ColumnResult(name = "isOpening", type = Boolean.class),
				@ColumnResult(name = "poiState", type = Integer.class),
				@ColumnResult(name = "career", type = String.class),
				@ColumnResult(name = "distance", type = Double.class),
				@ColumnResult(name = "lastNotification", type = Date.class),
				@ColumnResult(name = "displayState", type = Integer.class) }),

		@SqlResultSetMapping(name = "PoiWithNoDistance", columns = { @ColumnResult(name = "id", type = Long.class),
				@ColumnResult(name = "name", type = String.class),
				@ColumnResult(name = "description", type = String.class),
				@ColumnResult(name = "avatarImage", type = String.class),
				@ColumnResult(name = "coverImage", type = String.class),
				@ColumnResult(name = "ownerId", type = String.class),
				@ColumnResult(name = "openHour", type = Double.class),
				@ColumnResult(name = "closeHour", type = Double.class),
				@ColumnResult(name = "isOpening", type = Boolean.class),
				@ColumnResult(name = "poiState", type = Integer.class),
				@ColumnResult(name = "career", type = String.class),
				@ColumnResult(name = "lastNotification", type = Date.class),
				@ColumnResult(name = "displayState", type = Integer.class) }),
		
		//  p.id,p.name,p.description,p.phone,p.own_id,p.avatar_image,p.address, p.province,p.district,p.ward,p.close_hour,p.open_hour,p.opening_state,p.poi_state,
		@SqlResultSetMapping(name = "PoiOfCategory", columns = { 
				@ColumnResult(name = "id", type = Long.class),
				@ColumnResult(name = "name", type = String.class),
				@ColumnResult(name = "description", type = String.class),
				@ColumnResult(name = "phone", type = String.class),
				@ColumnResult(name = "ownId", type = String.class),
				@ColumnResult(name = "avatarImage", type = String.class),
				
				@ColumnResult(name = "address", type = String.class),
				@ColumnResult(name = "province", type = Long.class),
				@ColumnResult(name = "district", type = Long.class),
				@ColumnResult(name = "ward", type = Long.class),
				@ColumnResult(name = "distance", type = Double.class),
				
				@ColumnResult(name = "closeHour", type = Double.class),
				@ColumnResult(name = "openHour", type = Double.class),
				@ColumnResult(name = "isOpening", type = Boolean.class),
				@ColumnResult(name = "poiState", type = Integer.class),
				
				@ColumnResult(name = "bannerImages", type = String.class),
				@ColumnResult(name = "bannerIsApprove", type = Boolean.class),
				@ColumnResult(name = "categoryId", type = Long.class)
				}),
		//p.banner_images as bannerImages ,p.banner_is_approve as bannerIsApprove,p.cat_id as categoryId
		})
@NamedNativeQueries({
		@NamedNativeQuery(name = "PointOfInterest.findNearest", query = " SELECT *, round( distance_in_km * 1000 ) distance "
				+ " FROM " + "     (SELECT  "
				+ "         z.*, CALC_DISTANCE(p.latpoint, p.longpoint, z.latitude, z.longitude, 'km') AS distance_in_km,"
				+ "         p.radius " + "     FROM " + "         `omart_db`.`omart_point_of_interest` AS z "
				+ "     JOIN (SELECT  :latpoint AS latpoint, :longpoint AS longpoint,    "
				+ " 					  :radius AS radius,      111.045 AS distance_unit,"
				+ "                   :province AS province , :district AS district, :ward AS ward "
				+ "     ) AS p ON 1 = 1 " + "     WHERE " + "         1 = 1 AND z.cat_id = :categoryId "
				+ "             AND (p.province = 'all' OR z.province = p.province) "
				+ "             AND (p.district = 'all' OR z.district = p.district) "
				+ "             AND (p.ward     = 'all' OR z.ward     = p.ward) "
				+ "             AND z.latitude  BETWEEN p.latpoint -  (p.radius / p.distance_unit)  "
				+ " 								  AND p.latpoint +  (p.radius / p.distance_unit)  "
				+ "             AND z.longitude BETWEEN p.longpoint - (p.radius / (p.distance_unit * COS(RADIANS(p.latpoint))))  "
				+ " 								  AND p.longpoint + (p.radius / (p.distance_unit * COS(RADIANS(p.latpoint))))  "
				+ "     ORDER BY distance_in_km) AS t "
				+ " WHERE 1 = 1 and distance_in_km <= radius and round( distance_in_km * 1000 ) > :distance AND t.is_deleted = 0 AND t.is_approved = 1 ", resultSetMapping = "POIAndDistance"),

		@NamedNativeQuery(name = "PointOfInterest.findNearestByPoiId", query = " SELECT *, round( distance_in_km * 1000 ) distance "
				+ " FROM " + "     (SELECT  "
				+ "         z.*, CALC_DISTANCE(p.latpoint, p.longpoint, z.latitude, z.longitude, 'km') AS distance_in_km,"
				+ "         p.radius " + "     FROM " + "         `omart_db`.`omart_point_of_interest` AS z "
				+ "     JOIN (SELECT  :latpoint AS latpoint, :longpoint AS longpoint,    "
				+ " 					  :radius AS radius,      111.045 AS distance_unit,"
				+ "                   :province AS province , :district AS district, :ward AS ward "
				+ "     ) AS p ON 1 = 1 " + "     WHERE " + "         1 = 1 AND FIND_IN_SET(z.id, (:ids)) "
				+ "             AND (p.province = 'all' OR z.province = p.province) "
				+ "             AND (p.district = 'all' OR z.district = p.district) "
				+ "             AND (p.ward     = 'all' OR z.ward     = p.ward) "
				+ "             AND z.latitude  BETWEEN p.latpoint -  (p.radius / p.distance_unit)  "
				+ " 								  AND p.latpoint +  (p.radius / p.distance_unit)  "
				+ "             AND z.longitude BETWEEN p.longpoint - (p.radius / (p.distance_unit * COS(RADIANS(p.latpoint))))  "
				+ " 								  AND p.longpoint + (p.radius / (p.distance_unit * COS(RADIANS(p.latpoint))))  "
				+ "     ORDER BY distance_in_km) AS t "
				+ " WHERE 1 = 1 and distance_in_km <= radius and round( distance_in_km * 1000 ) > :distance AND t.is_deleted = 0 AND t.is_approved = 1 ", resultSetMapping = "POIAndDistance") })
@NamedStoredProcedureQueries({

		@NamedStoredProcedureQuery(name = "geo_poi_dist_enhance_v4", procedureName = "geodistance_poi_enhance_v4", resultSetMappings = "PoiNearest", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "orig_latitude", type = Double.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "orig_longitude", type = Double.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "radius", type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "paging", type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "size", type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "categoryIds", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "provinceId", type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "districtIds", type = String.class) }),

		@NamedStoredProcedureQuery(name = "geo_poi_dist_enhance_v2", procedureName = "geodistance_poi_enhance_v2", resultSetMappings = "PoiNearest", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "orig_latitude", type = Double.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "orig_longitude", type = Double.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "radius", type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "paging", type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "size", type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "poiIds", type = String.class) }),

		@NamedStoredProcedureQuery(name = "geo_poi_dist_enhance_v1", procedureName = "geodistance_poi_enhance_v1", resultSetMappings = "PoiNearest", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "orig_latitude", type = Double.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "orig_longitude", type = Double.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "radius", type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "paging", type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "size", type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "categoryIds", type = String.class) }),

		@NamedStoredProcedureQuery(name = "geo_poi_dist_enhance", procedureName = "geodistance_poi_enhance", resultSetMappings = "PoiNearest", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "orig_latitude", type = Double.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "orig_longitude", type = Double.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "radius", type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "paging", type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "size", type = Integer.class) }),

		@NamedStoredProcedureQuery(name = "geo_poi_dist", procedureName = "geodistance_poi", resultSetMappings = "POIAndDistance", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "orig_latitude", type = Double.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "orig_longitude", type = Double.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "radius", type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "paging", type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "size", type = Integer.class) }),
		
		@NamedStoredProcedureQuery(name = "filter_poi_by_category", procedureName = "filter_poi_by_category", resultSetMappings = "PoiOfCategory", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "orig_latitude", type = Double.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "orig_longitude", type = Double.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "catId", type = Long.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "provinceId", type = Long.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "districtId", type = Long.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "wardId", type = Long.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "radius", type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "paging", type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "size", type = Integer.class) })

})

public class PointOfInterest {

	@OneToMany(mappedBy = "poi", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
	private List<PoiAction> poiActions;

	public List<PoiAction> poiActions() {
		return poiActions;
	}

	@OneToMany(mappedBy = "poi", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
	private List<PoiComment> poiComments;

	public List<PoiComment> poiComments() {
		return poiComments;
	}

	@OneToMany(mappedBy = "poi", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
	private List<PoiPicture> poiPictures;

	@OneToMany(mappedBy = "poi", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
	private List<News> omartNews;

	@OneToMany(mappedBy = "poi", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
	private List<PoiNotification> poiNotifications;

	@OneToMany(mappedBy = "poi", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
	private List<Recruitment> recruitments;
	
	@OneToMany(mappedBy = "poi", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
	private List<Order> orders;
	
	@OneToMany(mappedBy = "poi", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
	private List<Deliverer> deliverers;
	
	public List<Deliverer> Deliverer() {
		return deliverers;
	}
	
	public List<Order> Order() {
		return orders;
	}

	public List<PoiNotification> PoiNotification() {
		return poiNotifications;
	}

	public List<Recruitment> Recruitment() {
		return recruitments;
	}

	public List<News> omartNews() {
		return omartNews;
	}

	public List<PoiPicture> poiPictures() {
		return poiPictures;
	}

	@ManyToOne
	@JoinColumn(name = "cat_id", columnDefinition = "int")
	private Category category;

	@Column(name = "own_id", columnDefinition = "varchar")
	private String ownerId;

	@Id
	@Column(name = "id", columnDefinition = "int")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name", columnDefinition = "varchar")
	private String name;

	@Column(name = "description", columnDefinition = "text")
	private String description;

	@Column(name = "address", columnDefinition = "text")
	private String address;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "province", columnDefinition = "int")
	private Province province;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "district", columnDefinition = "int")
	private District district;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ward", columnDefinition = "int")
	private Ward ward;

	@Column(name = "snapshot_map", columnDefinition = "text")
	private String snapshotMap;

	@Column(name = "phone", columnDefinition = "text")
	private String phone; // list split by comma

	@Column(name = "latitude", columnDefinition = "DOUBLE")
	private Double latitude;

	@Column(name = "longitude", columnDefinition = "DOUBLE")
	private Double longitude;

	@Column(name = "open_hour", columnDefinition = "DOUBLE")
	private Double openHour;

	@Column(name = "close_hour", columnDefinition = "DOUBLE")
	private Double closeHour;

	@Column(name = "opening_state", columnDefinition = "bit")
	private int openingState;

	@Column(name = "avatar_image", columnDefinition = "json")
	@Convert(converter = JpaImageConverter.class)
	private List<Image> avatarImage;

	@Column(name = "cover_image", columnDefinition = "json")
	@Convert(converter = JpaImageConverter.class)
	private List<Image> coverImage;

	@Column(name = "featured_image", columnDefinition = "json")
	@Convert(converter = JpaImageConverter.class)
	private List<Image> featuredImage;

	@Column(name = "banner_images", columnDefinition = "json")
	@Convert(converter = JpaImageConverter.class)
	private List<Image> bannerImages;

	@Column(name = "banner_is_approve", columnDefinition = "bit")
	private int bannerIsApprove;

	@Column(name = "created_by", columnDefinition = "varchar")
	private String createdBy;

	@Column(name = "lat_of_creator", columnDefinition = "DOUBLE")
	private Double latOfCreator;

	@Column(name = "lon_of_creator", columnDefinition = "DOUBLE")
	private Double lonOfCreator;

	@Column(name = "created_at", columnDefinition = "TIMESTAMP")
	private Date createdAt;

	@Column(name = "updated_by", columnDefinition = "varchar")
	private String updatedBy;

	@Column(name = "updated_at", columnDefinition = "TIMESTAMP")
	private Date updatedAt;

	@Transient
	private Double distance;

	@Column(name = "view_count", columnDefinition = "int")
	private int viewCount;

	@Column(name = "rate", columnDefinition = "float")
	private float rate;

	@Column(name = "share_count", columnDefinition = "int")
	private int shareCount;

	@Column(name = "career", columnDefinition = "varchar")
	private String career;

	@Column(name = "poi_state", columnDefinition = "bit")
	private int poiState;

	@Column(name = "last_notification", columnDefinition = "TIMESTAMP")
	private Date lastNotification;

	@Column(name = "is_deleted", columnDefinition = "bit")
	private boolean isDeleted;

	@Column(name = "display_state", columnDefinition = "int")
	private int displayState;

	@Column(name = "email", columnDefinition = "varchar")
	private String email;

	@Column(name = "web_address", columnDefinition = "varchar")
	private String webAddress;

	@Column(name = "poi_type", columnDefinition = "int")
	private int poiType;

	@Column(name = "fax", columnDefinition = "varchar")
	private String fax;

	@Column(name = "tel", columnDefinition = "varchar")
	private String tel;

	@Column(name = "tax", columnDefinition = "varchar")
	private String tax;

	@Column(name = "facebook", columnDefinition = "varchar")
	private String facebook;

	@Column(name = "twitter", columnDefinition = "varchar")
	private String twitter;
	
	@Column(name = "is_approved", columnDefinition = "bit")
	private boolean isApproved;
	
	@Column(name = "reason", columnDefinition = "text")
	private String reason;

	@Column(name = "disable_sale_feature", columnDefinition = "bit")
	private boolean disableSaleFeature;
	
	@Column(name = "ring_index", columnDefinition = "int")
	private int ringIndex;
	
	@Column(name = "currency", columnDefinition = "int")
	private int currency;
	
	@Column(name = "delivery_radius", columnDefinition = "int")
	private int deliveryRadius;
	
	@Column(name = "discount", columnDefinition = "int")
	private int discount;
	
	public PointOfInterest() {
	}

	public PointOfInterest updateBanner(String bannerImage) {
		this.bannerImages = Collections.singletonList(new Image(bannerImage));
		return this;
	}

	public PointOfInterest approveBanner(boolean isApprove) {
		this.bannerIsApprove = isApprove ? 1 : 0;
		return this;
	}

	// GETTERS
	public Category category() {
		return category;
	}

	public Long id() {
		return id;
	}

	public String name() {
		return name;
	}

	public String description() {
		if (description != null) {
			return EmojiParser.parseToUnicode(description);
		}
		return null;
	}

	public String address() {
		return address;
	}

	public Ward ward() {
		return ward;
	}

	public District district() {
		return district;
	}

	public Province province() {
		return province;
	}

	public String phone() {
		return phone;
	}

	public Double latitude() {
		return latitude;
	}

	public Double longitude() {
		return longitude;
	}

	public Double openHour() {
		return openHour;
	}

	public Double closeHour() {
		return closeHour;
	}

	public int openingState() {
		return openingState;
	}

	public boolean isOpening() {
		return openingState == 1;
	}

	public boolean isBannerApprored() {
		return bannerIsApprove == 1;
	}

	public List<Image> avatarImage() {
		return avatarImage;
	}

	public List<Image> coverImage() {
		return coverImage;
	}

	public List<Image> featuredImage() {
		return featuredImage;
	}

	public List<Image> bannerImages() {
		return bannerImages;
	}

	public String createdBy() {
		return createdBy;
	}

	public Date createdAt() {
		return createdAt;
	}

	public String updatedBy() {
		return updatedBy;
	}

	public Date updatedAt() {
		return updatedAt;
	}

	public String snapshotMap() {
		return snapshotMap;
	}

	public String ownerId() {
		return ownerId;
	}

	public Double distance() {
		return distance;
	}

	public Double latOfCreator() {
		return latOfCreator;
	}

	public Double lonOfCreator() {
		return lonOfCreator;
	}

	public int getViewCount() {
		return viewCount;
	}

	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}

	public float getRate() {
		return rate;
	}

	public void setRate(float rate) {
		this.rate = rate;
	}

	public int getShareCount() {
		return shareCount;
	}

	public void setShareCount(int shareCount) {
		this.shareCount = shareCount;
	}

	public List<Image> getBannerImages() {
		return bannerImages;
	}

	public void setBannerImages(List<Image> bannerImages) {
		this.bannerImages = bannerImages;
	}

	public List<Image> getAvatarImage() {
		return avatarImage;
	}

	public void setAvatarImage(List<Image> avatarImage) {
		this.avatarImage = avatarImage;
	}

	public List<Image> getCoverImage() {
		return coverImage;
	}

	public void setCoverImage(List<Image> coverImage) {
		this.coverImage = coverImage;
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

	public String getDescription() {
		if (description != null) {
			return EmojiParser.parseToUnicode(description);
		}
		return description;
	}

	public void setDescription(String description) {
		if (description != null) {
			this.description = EmojiParser.parseToAliases(description);
		}
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Double getOpenHour() {
		return openHour;
	}

	public void setOpenHour(Double openHour) {
		this.openHour = openHour;
	}

	public Double getCloseHour() {
		return closeHour;
	}

	public void setCloseHour(Double closeHour) {
		this.closeHour = closeHour;
	}

	public int getPoiState() {
		return poiState;
	}

	public void setPoiState(int poiState) {
		this.poiState = poiState;
	}

	public String getCareer() {
		return career;
	}

	public void setCareer(String career) {
		this.career = career;
	}

	public Date getLastNotification() {
		return lastNotification;
	}

	public void setLastNotification(Date lastNotification) {
		this.lastNotification = lastNotification;
	}

	public boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public int getDisplayState() {
		return displayState;
	}

	public void setDisplayState(int displayState) {
		this.displayState = displayState;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getWebAddress() {
		return webAddress;
	}

	public void setWebAddress(String webAddress) {
		this.webAddress = webAddress;
	}

	public void setPoiType(int poiType) {
		this.poiType = poiType;
	}

	public int getPoiType() {
		return this.poiType;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getFax() {
		return this.fax;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getTel() {
		return this.tel;
	}

	public void setTax(String tax) {
		this.tax = tax;
	}

	public String getTax() {
		return this.tax;
	}

	public void setFacebook(String facebook) {
		this.facebook = facebook;
	}

	public String getFacebook() {
		return this.facebook;
	}

	public void setTwitter(String twitter) {
		this.twitter = twitter;
	}

	public String getTwitter() {
		return this.twitter;
	}

	public boolean isApproved() {
		return isApproved;
	}

	public void setApproved(boolean isApproved) {
		this.isApproved = isApproved;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public boolean isDisableSaleFeature() {
		return disableSaleFeature;
	}

	public void setDisableSaleFeature(boolean disableSaleFeature) {
		this.disableSaleFeature = disableSaleFeature;
	}

	public int getRingIndex() {
		return ringIndex;
	}

	public void setRingIndex(int ringIndex) {
		this.ringIndex = ringIndex;
	}

	public int getCurrency() {
		return currency;
	}

	public void setCurrency(int currency) {
		this.currency = currency;
	}

	public int getDeliveryRadius() {
		return deliveryRadius;
	}

	public void setDeliveryRadius(int deliveryRadius) {
		this.deliveryRadius = deliveryRadius;
	}

	public int getDiscount() {
		return discount;
	}

	public void setDiscount(int discount) {
		this.discount = discount;
	}
}
