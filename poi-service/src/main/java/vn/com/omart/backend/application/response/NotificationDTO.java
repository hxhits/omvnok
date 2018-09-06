package vn.com.omart.backend.application.response;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import vn.com.omart.backend.constants.OmartType.AppType;
import vn.com.omart.backend.domain.model.Notification;

public class NotificationDTO {

	private Long id;
	private int notificationType;
	private int viewType;
	private String title;
	private String content;
	private String recipientId;
	private Long createdAt;
	private String createdBy;
	private AppType appType;

	public static Notification toEntity(NotificationDTO dto) {
		Notification entity = new Notification();
		entity.setTitle(dto.getTitle());
		if (StringUtils.isNotBlank(dto.getContent())) {
			entity.setContent(dto.getContent());
		}
		if (StringUtils.isNotBlank(dto.getRecipientId())) {
			entity.setRecipientId(dto.getRecipientId());
		}
		entity.setNotificationType(0);// default 0
		return entity;
	}

	public static NotificationDTO toBasicDTO(Notification entity) {
		NotificationDTO dto = new NotificationDTO();
		dto.setId(entity.getId());
		dto.setTitle(entity.getTitle());
		dto.setContent(entity.getContent());
		dto.setViewType(entity.getViewType());
		dto.setCreatedAt(entity.getCreatedAt().getTime());
		return dto;
	}

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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getRecipientId() {
		return recipientId;
	}

	public void setRecipientId(String recipientId) {
		this.recipientId = recipientId;
	}

	public Long getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Long createdAt) {
		this.createdAt = createdAt;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public int getViewType() {
		return viewType;
	}

	public void setViewType(int viewType) {
		this.viewType = viewType;
	}

	public AppType getAppType() {
		return appType;
	}

	public void setAppType(AppType appType) {
		this.appType = appType;
	}

}
