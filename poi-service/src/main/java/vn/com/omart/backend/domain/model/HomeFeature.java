package vn.com.omart.backend.domain.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.vdurmont.emoji.EmojiParser;

@Entity
@Table(name = "omart_home_feature")
public class HomeFeature implements Serializable {

	private static final long serialVersionUID = -5877731830116698408L;

	@Id
	@Column(name = "id", columnDefinition = "int")
	@GeneratedValue
	private Long id;

	@Column(name = "title", columnDefinition = "varchar")
	private String title;

	@Column(name = "description", columnDefinition = "text")
	private String description;

	@Column(name = "image", columnDefinition = "text")
	private String image;

	@Column(name = "title_color", columnDefinition = "varchar")
	private String titleColor;

	@Column(name = "desc_color", columnDefinition = "varchar")
	private String descColor;

	@Column(name = "feature_id", columnDefinition = "int")
	private int featureId;

	@Column(name = "feature_name", columnDefinition = "varchar")
	private String featureName;

	@Column(name = "feature_name_en", columnDefinition = "varchar")
	private String featureNameEn;

	@Column(name = "created_at", columnDefinition = "timestamp")
	private Date createdAt;

	@Column(name = "created_by", columnDefinition = "varchar")
	private String createdBy;

	@Column(name = "updated_at", columnDefinition = "timestamp")
	private Date updatedAt;

	@Column(name = "updated_by", columnDefinition = "varchar")
	private String updatedBy;

	@Column(name = "is_approved", columnDefinition = "bit")
	private boolean isApproved;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		if (this.title != null) {
			return EmojiParser.parseToUnicode(title);
		}
		return null;
	}

	public void setTitle(String title) {
		if (title != null) {
			this.title = EmojiParser.parseToAliases(title);
		}
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

	public String getImage() {
		return image;
	}
	
	public void setImage(String image) {
		this.image = image;
	}

	public String getTitleColor() {
		return titleColor;
	}

	public void setTitleColor(String titleColor) {
		this.titleColor = titleColor;
	}

	public String getDescColor() {
		return descColor;
	}

	public void setDescColor(String descColor) {
		this.descColor = descColor;
	}

	public int getFeatureId() {
		return featureId;
	}

	public void setFeatureId(int featureId) {
		this.featureId = featureId;
	}

	public String getFeatureName() {
		return featureName;
	}

	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}

	public String getFeatureNameEn() {
		return featureNameEn;
	}

	public void setFeatureNameEn(String featureNameEn) {
		this.featureNameEn = featureNameEn;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public boolean isApproved() {
		return isApproved;
	}

	public void setApproved(boolean isApproved) {
		this.isApproved = isApproved;
	}

}
