package vn.com.omart.messenger.application.response;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;

import vn.com.omart.messenger.domain.model.MessageChannel;

/**
 * Message Channel Data Transfer Object.
 * 
 * @author Win10
 *
 */
public class MessageChannelDTO implements Serializable {
	private static final long serialVersionUID = -5442523663473690639L;
	private Long id;
	private String channelName;
	private String channelSid;
	private String senderId;
	private String recipientId;
	@NotNull
	private Long poiId;
	private PoiDTO poi;
	private int lastIndex;
	private int isDeleted;
	// see from side user login send message to other user.
	private UserDTO recipient;

	private Date createdAt;

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

	public Long getPoiId() {
		return poiId;
	}

	public void setPoiId(Long poiId) {
		this.poiId = poiId;
	}

	public static MessageChannelDTO toDTO() {
		return null;
	}

	public PoiDTO getPoi() {
		return poi;
	}

	public void setPoi(PoiDTO poi) {
		this.poi = poi;
	}

	public UserDTO getRecipient() {
		return recipient;
	}

	public void setRecipient(UserDTO recipient) {
		this.recipient = recipient;
	}

	public int getLastIndex() {
		return lastIndex;
	}

	public void setLastIndex(int lastIndex) {
		this.lastIndex = lastIndex;
	}

	public int getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public static MessageChannel toEntity(MessageChannelDTO dto) {
		MessageChannel messageChannel = new MessageChannel();
		messageChannel.setChannelName(dto.getChannelName());
		messageChannel.setChannelSid(dto.getChannelSid());
		messageChannel.setPoiId(dto.getPoiId());
		messageChannel.setRecipientId(dto.getRecipientId());
		messageChannel.setSenderId(dto.getSenderId());
		messageChannel.setCreatedAt(new Date());

		return messageChannel;
	}

	public static MessageChannelDTO toDTO(MessageChannel entity) {
		MessageChannelDTO channelDTO = new MessageChannelDTO();
		channelDTO.setIsDeleted(entity.getSenderDeleted());
		channelDTO.setLastIndex(entity.getSenderLastIndex());
		channelDTO.setChannelName(entity.getChannelName());
		channelDTO.setChannelSid(entity.getChannelSid());
		channelDTO.setCreatedAt(entity.getCreatedAt());
		return channelDTO;
	}

}
