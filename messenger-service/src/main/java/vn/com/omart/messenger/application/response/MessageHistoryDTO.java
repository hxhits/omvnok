package vn.com.omart.messenger.application.response;

import java.util.Date;

import org.json.JSONObject;

import com.google.gson.Gson;

import vn.com.omart.messenger.domain.model.MessageHistory;

public class MessageHistoryDTO {

	private Long id;
	private String channelName;
	private String channelSid;
	private String senderId;
	private String recipientId;
	private int messageIndex;
	private String content;
	private JSONObject rawContent;
	private Date createdAt;
	private MessageDTO body;

	public MessageHistoryDTO(String senderId, String recipientId, int messageIndex, String rawContent, Date createdAt) {
		super();
		Gson gson = new Gson();
		MessageDTO mDto = gson.fromJson(rawContent, MessageDTO.class);
		this.setBody(mDto);
		this.senderId = senderId;
		this.recipientId = recipientId;
		this.messageIndex = messageIndex;
		this.createdAt = createdAt;
	}
	
	public MessageHistoryDTO(int messageIndex, String rawContent) {
		super();
		Gson gson = new Gson();
		MessageDTO mDto = gson.fromJson(rawContent, MessageDTO.class);
		this.setBody(mDto);
		this.messageIndex = messageIndex;

	}

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

	public JSONObject getRawContent() {
		return rawContent;
	}

	public void setRawContent(JSONObject rawContent) {
		this.rawContent = rawContent;
	}

	public static MessageHistory toEntity(MessageHistoryDTO dto) {
		MessageHistory entity = new MessageHistory();
		entity.setChannelName(dto.getChannelName());
		entity.setChannelSid(dto.getChannelSid());
		entity.setContent(dto.getContent());
		entity.setMessageIndex(dto.getMessageIndex());
		entity.setRecipientId(dto.getRecipientId());
		entity.setSenderId(dto.getSenderId());
		entity.setCreatedAt(new Date());
		return entity;
	}

	public MessageDTO getBody() {
		return body;
	}

	public void setBody(MessageDTO body) {
		this.body = body;
	}
	
}
