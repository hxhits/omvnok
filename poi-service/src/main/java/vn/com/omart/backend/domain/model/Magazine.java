package vn.com.omart.backend.domain.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.vdurmont.emoji.EmojiParser;

@Entity
@Table(name = "omart_magazine")
@SqlResultSetMapping(name = "magazineresultset", entities = @EntityResult(entityClass = Magazine.class))

public class Magazine {

	@Id
	@Column(name = "id", columnDefinition = "int")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "cat_id", columnDefinition = "int")
	private Long catId;

	@Column(name = "title", columnDefinition = "varchar")
	private String title;

	@Column(name = "description", columnDefinition = "text")
	private String desc;

	@Column(name = "thumb_url", columnDefinition = "varchar")
	private String thumbUrl;

	@Column(name = "link", columnDefinition = "varchar")
	private String link;

	@Column(name = "source", columnDefinition = "varchar")
	private String source;

	@Column(name = "created_at", columnDefinition = "TIMESTAMP")
	private Date createdAt;

	@Column(name = "created_by", columnDefinition = "varchar")
	private String createdBy;

	@Column(name = "time_stamp", columnDefinition = "TIMESTAMP")
	private Date timestamp;

	@Column(name = "hash", columnDefinition = "varchar")
	private String hash;

	@Transient
	private String userId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCatId() {
		return catId;
	}

	public void setCatId(Long catId) {
		this.catId = catId;
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

	public String getThumbUrl() {
		return thumbUrl;
	}

	public void setThumbUrl(String thumbnailUrl) {
		this.thumbUrl = thumbnailUrl;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
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

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
