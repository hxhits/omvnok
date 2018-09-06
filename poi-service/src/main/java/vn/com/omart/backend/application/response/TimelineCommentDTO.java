package vn.com.omart.backend.application.response;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import vn.com.omart.backend.domain.model.TimelineComment;

public class TimelineCommentDTO {

	private Long id;
	private String userId;
	private Long timelineId;
	private String comment;
	private Date createdAt;
	private UserDTO user;
	
	@JsonIgnore
	public static Set<String> userIds = new HashSet<String>();

	public static TimelineComment toEntity(TimelineCommentDTO dto) {
		TimelineComment entity = new TimelineComment();
		entity.setUserId(dto.getUserId());
		entity.setCreatedAt(new Date());
		entity.setComment(dto.getComment());
		return entity;
	}

	public static TimelineCommentDTO toDTO(TimelineComment entity) {
		TimelineCommentDTO dto = new TimelineCommentDTO();
		dto.setUserId(entity.getUserId());
		dto.setCreatedAt(entity.getCreatedAt());
		dto.setComment(entity.getComment());
		userIds.add(entity.getUserId());
		return dto;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Long getTimelineId() {
		return timelineId;
	}

	public void setTimelineId(Long timelineId) {
		this.timelineId = timelineId;
	}

	public static Set<String> getUserIds() {
		return userIds;
	}

	public static void setUserIds(Set<String> userIds) {
		TimelineCommentDTO.userIds = userIds;
	}

	public UserDTO getUser() {
		return user;
	}

	public void setUser(UserDTO user) {
		this.user = user;
	}

}
