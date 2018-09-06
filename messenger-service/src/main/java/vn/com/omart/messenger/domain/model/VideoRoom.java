package vn.com.omart.messenger.domain.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "omart_video_room")
public class VideoRoom {

	@Id
	@Column(name = "id", columnDefinition = "int")
	@GeneratedValue
	private Long id;

	@Column(name = "name", columnDefinition = "varchar")
	private String name;

	@Column(name = "sender_id", columnDefinition = "varchar")
	private String senderId;

	@Column(name = "recipient_id", columnDefinition = "varchar")
	private String recipientId;

	@Column(name = "sender_token", columnDefinition = "varchar")
	private String senderToken;

	@Column(name = "recipient_token", columnDefinition = "varchar")
	private String recipientToken;

	@Column(name = "room_sid", columnDefinition = "varchar")
	private String roomSid;

	@Column(name = "status", columnDefinition = "int")
	private int status;

	@Column(name = "created_at", columnDefinition = "TIMESTAMP")
	private Date createdAt;

	@Column(name = "poi_id", columnDefinition = "int")
	private Long poiId;

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

	public String getSenderToken() {
		return senderToken;
	}

	public void setSenderToken(String senderToken) {
		this.senderToken = senderToken;
	}

	public String getRecipientToken() {
		return recipientToken;
	}

	public void setRecipientToken(String recipientToken) {
		this.recipientToken = recipientToken;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getRoomSid() {
		return roomSid;
	}

	public void setRoomSid(String roomSid) {
		this.roomSid = roomSid;
	}

	public Long getPoiId() {
		return poiId;
	}

	public void setPoiId(Long poiId) {
		this.poiId = poiId;
	}

}