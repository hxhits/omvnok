package vn.com.omart.messenger.domain.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "omart_message_history")
public class MessageHistory implements Serializable {

	private static final long serialVersionUID = -8374071570714697475L;

	@Id
	@Column(name = "id", columnDefinition = "int")
	@GeneratedValue
	private Long id;

	@Column(name = "channel_id", columnDefinition = "varchar", nullable = true)
	private String channelName;

	@Column(name = "channel_sid", columnDefinition = "varchar", nullable = false)
	private String channelSid;

	@Column(name = "sender_id", columnDefinition = "varchar")
	private String senderId;

	@Column(name = "recipient_id", columnDefinition = "varchar")
	private String recipientId;

	@Column(name = "message_index", columnDefinition = "int")
	private int messageIndex;

	@Column(name = "content", columnDefinition = "varchar")
	private String content;

	@Column(name = "created_at", columnDefinition = "timestamp")
	private Date createdAt;
	
	@Column(name = "raw_content", columnDefinition = "varchar")
	private String rawContent;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getChannelSid() {
		return channelSid;
	}

	public void setChannelSid(String channelSid) {
		this.channelSid = channelSid;
	}

	public String getSenderId() {
		return senderId;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}

	public String getRecipientId() {
		return recipientId;
	}

	public void setRecipientId(String recipientId) {
		this.recipientId = recipientId;
	}

	public int getMessageIndex() {
		return messageIndex;
	}

	public void setMessageIndex(int messageIndex) {
		this.messageIndex = messageIndex;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getRawContent() {
		return rawContent;
	}

	public void setRawContent(String rawContent) {
		this.rawContent = rawContent;
	}

}
