package vn.com.omart.messenger.domain.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "omart_message_channel")
public class MessageChannel implements Serializable {

	@Id
	@Column(name = "id", columnDefinition = "int")
	@GeneratedValue
	private Long id;

	@Column(name = "name", columnDefinition = "varchar")
	private String name;

	@Column(name = "channel_id", columnDefinition = "varchar", unique = true, nullable = false)
	private String channelName;

	@Column(name = "channel_sid", columnDefinition = "varchar", unique = true, nullable = false)
	private String channelSid;

	@Column(name = "sender_id", columnDefinition = "varchar")
	private String senderId;

	@Column(name = "recipient_id", columnDefinition = "varchar")
	private String recipientId;

	@Column(name = "poi_id", columnDefinition = "int", nullable = false)
	private Long poiId;

	@Column(name = "sender_deleted", columnDefinition = "int")
	private int senderDeleted;

	@Column(name = "recipient_deleted", columnDefinition = "int")
	private int recipientDeleted;

	@Column(name = "created_at", columnDefinition = "TIMESTAMP")
	private Date createdAt;

	@Column(name = "sender_last_index", columnDefinition = "int")
	private int senderLastIndex;

	@Column(name = "recipient_last_index", columnDefinition = "int")
	private int recipientLastIndex;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Long getPoiId() {
		return poiId;
	}

	public void setPoiId(Long poiId) {
		this.poiId = poiId;
	}

	public int getSenderDeleted() {
		return senderDeleted;
	}

	public void setSenderDeleted(int senderDeleted) {
		this.senderDeleted = senderDeleted;
	}

	public int getRecipientDeleted() {
		return recipientDeleted;
	}

	public void setRecipientDeleted(int recipientDeleted) {
		this.recipientDeleted = recipientDeleted;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public int getSenderLastIndex() {
		return senderLastIndex;
	}

	public void setSenderLastIndex(int senderLastIndex) {
		this.senderLastIndex = senderLastIndex;
	}

	public int getRecipientLastIndex() {
		return recipientLastIndex;
	}

	public void setRecipientLastIndex(int recipientLastIndex) {
		this.recipientLastIndex = recipientLastIndex;
	}

}