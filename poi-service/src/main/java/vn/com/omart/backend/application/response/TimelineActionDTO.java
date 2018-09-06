package vn.com.omart.backend.application.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import vn.com.omart.backend.application.util.PoiActionStatus;
import vn.com.omart.backend.domain.model.TimelineAction;

public class TimelineActionDTO {
	@JsonIgnore
	private String userId;
	private Long timelineId;
	private PoiActionStatus action;

	public static TimelineAction toEntity(TimelineActionDTO dto) {
		TimelineAction entity = new TimelineAction();
		entity.setUserId(dto.getUserId());
		entity.setCreatedAt(new Date());
		entity.setActionType(dto.getAction().getId());
		return entity;
	}

	public static TimelineActionDTO toDTO(TimelineAction entity) {
		TimelineActionDTO dto = new TimelineActionDTO();
		dto.setUserId(entity.getUserId());
		return dto;
	}

	public Long getTimelineId() {
		return timelineId;
	}

	public void setTimelineId(Long timelineId) {
		this.timelineId = timelineId;
	}

	public PoiActionStatus getAction() {
		return action;
	}

	public void setAction(PoiActionStatus action) {
		this.action = action;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
