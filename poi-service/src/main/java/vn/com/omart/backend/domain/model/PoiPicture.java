package vn.com.omart.backend.domain.model;

import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.vdurmont.emoji.EmojiParser;

import vn.com.omart.backend.port.adapter.support.mysql.JpaImageConverter;

@Entity
@Table(name = "omart_poi_picture")
public class PoiPicture {

	@Id
	@GeneratedValue
	@Column(name = "id", columnDefinition = "int")
	private Long id;

	@Column(name = "user_id", length = 255, nullable = false, columnDefinition = "varchar")
	private String userId;

	@ManyToOne
	@JoinColumn(name = "poi_id", referencedColumnName = "id", columnDefinition = "int", nullable = true)
	private PointOfInterest poi;

	@Transient
	private String url;

	@Column(name = "image_url", columnDefinition = "json")
	@Convert(converter = JpaImageConverter.class)
	private List<Image> urls;

	@Column(name = "title", columnDefinition = "varchar")
	private String title;

	@Column(name = "created_at", columnDefinition = "TIMESTAMP")
	private Date createdAt;

	@Column(name = "like_count", columnDefinition = "int")
	private int likeNumber;

	@Column(name = "comment_count", columnDefinition = "int")
	private int commentNumber;

	@Column(name = "description", columnDefinition = "text")
	private String description;

	@Column(name = "latitude", columnDefinition = "DOUBLE")
	private Double latitude;

	@Column(name = "longitude", columnDefinition = "DOUBLE")
	private Double longitude;

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

	@Column(name = "is_deleted", columnDefinition = "int")
	private boolean isDeleted;

	@OneToMany(mappedBy = "picture", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
	private List<PoiPictureAction> pictureActions;

	@OneToMany(mappedBy = "picture", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
	private List<PictureComment> pictureComments;

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

	public PointOfInterest getPoi() {
		return poi;
	}

	public void setPoi(PointOfInterest poi) {
		this.poi = poi;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		if (title != null) {
			return EmojiParser.parseToUnicode(title);
		}
		return null;
	}

	public List<Image> getUrls() {
		return urls;
	}

	public void setUrls(List<Image> urls) {
		this.urls = urls;
	}

	public void setTitle(String title) {
		if (title != null) {
			this.title = EmojiParser.parseToAliases(title);
		}
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public List<PoiPictureAction> getPictureActions() {
		return pictureActions;
	}

	public void setPictureActions(List<PoiPictureAction> pictureActions) {
		this.pictureActions = pictureActions;
	}

	public int getLikeNumber() {
		return likeNumber;
	}

	public void setLikeNumber(int likeNumber) {
		this.likeNumber = likeNumber;
	}

	public List<PictureComment> getPictureComments() {
		return pictureComments;
	}

	public void setPictureComments(List<PictureComment> pictureComments) {
		this.pictureComments = pictureComments;
	}

	public int getCommentNumber() {
		return commentNumber;
	}

	public void setCommentNumber(int commentNumber) {
		this.commentNumber = commentNumber;
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

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLatitude() {
		return this.latitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLongitude() {
		return this.longitude;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}

	public int getFontSize() {
		return this.fontSize;
	}

	public void setFontStyle(String fontStyle) {
		this.fontStyle = fontStyle;
	}

	public String getFontStyle() {
		return this.fontStyle;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getColor() {
		return this.color;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getHref() {
		return this.href;
	}

	public void setHrefTitle(String hrefTitle) {
		this.hrefTitle = hrefTitle;
	}

	public String getHrefTitle() {
		return this.hrefTitle;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public boolean isDeleted() {
		return this.isDeleted;
	}

}
