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
@Table(name = "omart_notification")
public class Notification implements Serializable {

	private static final long serialVersionUID = 4743334310835558082L;

	@Id
	@Column(name = "id", columnDefinition = "int")
	@GeneratedValue
	private Long id;

	@Column(name = "notification_type", columnDefinition = "int")
	private int notificationType;
	
	@Column(name = "view_type", columnDefinition = "int")
	private int viewType;

	@Column(name = "title", columnDefinition = "varchar")
	private String title;

	@Column(name = "content", columnDefinition = "text")
	private String content;

	@Column(name = "recipient_id", columnDefinition = "varchar")
	private String recipientId;

	@Column(name = "created_at", columnDefinition = "timestamp")
	private Date createdAt;

	@Column(name = "created_by", columnDefinition = "varchar")
	private String createdBy;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(int notificationType) {
		this.notificationType = notificationType;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getRecipientId() {
		return recipientId;
	}

	public void setRecipientId(String recipientId) {
		this.recipientId = recipientId;
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

	public String getContent() {
		if (this.content != null) {
			return EmojiParser.parseToUnicode(content);
		}
		return null;
	}

	public void setContent(String content) {
		if (content != null) {
			this.content = EmojiParser.parseToAliases(content);
		}
	}

	public int getViewType() {
		return viewType;
	}

	public void setViewType(int viewType) {
		this.viewType = viewType;
	}

}