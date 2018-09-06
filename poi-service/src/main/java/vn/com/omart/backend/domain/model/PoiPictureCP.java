package vn.com.omart.backend.domain.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.vdurmont.emoji.EmojiParser;

import vn.com.omart.backend.port.adapter.support.mysql.JpaImageConverter;

@Entity
@Table(name = "omart_poi_picture")
public class PoiPictureCP {

	@Id
	@GeneratedValue
	@Column(name = "id", columnDefinition = "int")
	private Long id;

	@Column(name = "user_id", length = 255, nullable = false, columnDefinition = "varchar")
	private String userId;

	@Column(name = "poi_id", columnDefinition = "int")
	private Long poiId;

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

	public Long getPoiId() {
		return poiId;
	}

	public void setPoiId(Long poiId) {
		this.poiId = poiId;
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

	public List<Image> getUrls() {
		return urls;
	}

	public void setUrls(List<Image> urls) {
		this.urls = urls;
	}

	public String getTitle() {
		if (title != null) {
			return EmojiParser.parseToUnicode(title);
		}
		return null;
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

	public int getLikeNumber() {
		return likeNumber;
	}

	public void setLikeNumber(int likeNumber) {
		this.likeNumber = likeNumber;
	}

	public int getCommentNumber() {
		return commentNumber;
	}

	public void setCommentNumber(int commentNumber) {
		this.commentNumber = commentNumber;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return this.description;
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
