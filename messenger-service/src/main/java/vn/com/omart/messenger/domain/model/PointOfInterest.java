package vn.com.omart.messenger.domain.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;
import vn.com.omart.messenger.port.support.mysql.JpaImageConverter;

@Entity
@Table(name = "omart_point_of_interest")
@ToString
@Builder
@AllArgsConstructor
public class PointOfInterest {

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

	// @Column(name = "avatar_image", columnDefinition = "json")
	// @Convert(converter = JpaImageConverter.class)
	// private List<Image> avatarImage;

	// @Column(name = "cover_image", columnDefinition = "json")
	// @Convert(converter = JpaImageConverter.class)
	// private List<Image> coverImage;
	//
	// @Column(name = "featured_image", columnDefinition = "json")
	// @Convert(converter = JpaImageConverter.class)
	// private List<Image> featuredImage;
	//
	// @Column(name = "banner_images", columnDefinition = "json")
	// @Convert(converter = JpaImageConverter.class)
	// private List<Image> bannerImages;

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

	public PointOfInterest() {
	}

	// GETTERS

	public Long id() {
		return id;
	}

	public String name() {
		return name;
	}

	public String description() {
		return description;
	}

	public String address() {
		return address;
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

	public List<Image> getAvatarImage() {
		return avatarImage;
	}

	public void setAvatarImage(List<Image> avatarImage) {
		this.avatarImage = avatarImage;
	}

}
