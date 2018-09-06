package vn.com.omart.backend.application.response;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import vn.com.omart.backend.constants.OmartType.ApplicantAction;
import vn.com.omart.backend.constants.OmartType.InterviewResult;
import vn.com.omart.backend.constants.OmartType.UserCVNotificatinType;
import vn.com.omart.backend.domain.model.PointOfInterest;
import vn.com.omart.backend.domain.model.RecruitmentApply;
import vn.com.omart.backend.domain.model.UserCvNotification;

@Data
public class UserCvNotificationDTO {

	private Long id;
	private String userId;
	private UserDTO user;
	private Long poiId;
	private Long applyId;
	private PointOfInterestDTO poi;
	private RecruitmentApplyDTO apply;
	private RecruitmentPositionDTO recruitmentPosition;
	private String title;
	private String content;
	private Long type;
	private String createdBy;
	private Date createdAt;
	private String updatedBy;
	private Date updatedAt;
	@NotNull
	private ApplicantAction status;
	@NotNull
	private UserCVNotificatinType notificationType;

	@JsonIgnore
	public static Set<String> userIds = new HashSet<String>();
	
	private InterviewResult result;

	public UserCvNotificationDTO() {
		super();
	}

	public static UserCvNotificationDTO toDTO(UserCvNotification entity) {
		UserCvNotificationDTO dto = new UserCvNotificationDTO();
		dto.setId(entity.getId());
		dto.setUserId(entity.getUserId());
		dto.setPoiId(entity.getPoi() != null ? entity.getPoi().id() : 0);
		dto.setApplyId(entity.getApply() != null ? entity.getApply().getId() : 0);
		dto.setPoi(entity.getPoi() != null ? PointOfInterestDTO.from(entity.getPoi()) : null);
		dto.setApply(entity.getApply() != null ? RecruitmentApplyDTO.toDTO(entity.getApply()) : null);
		dto.setTitle(entity.getTitle());
		dto.setContent(entity.getContent());
		dto.setType(entity.getType());
		dto.setCreatedBy(entity.getCreatedBy());
		dto.setCreatedAt(entity.getCreatedAt());
		dto.setUpdatedBy(entity.getUpdatedBy());
		dto.setUpdatedAt(entity.getUpdatedAt());
		dto.setResult(InterviewResult.getById(entity.getResult()));
		dto.setStatus(ApplicantAction.getById(entity.getStatus()));
		userIds.add(entity.getUserId());
		return dto;
	}

	public static UserCvNotification fromDTO(UserCvNotificationDTO entity) {
		UserCvNotification dto = new UserCvNotification();
		dto.setId(entity.getId());
		dto.setUserId(entity.getUserId());
		dto.setTitle(entity.getTitle());
		dto.setContent(entity.getContent());
		dto.setType(entity.getType());
		dto.setResult(entity.getResult().getId());
		dto.setNotificationType(entity.getNotificationType().getId());
		dto.setCreatedBy(entity.getCreatedBy());
		dto.setCreatedAt(entity.getCreatedAt());
		dto.setUpdatedBy(entity.getUpdatedBy());
		dto.setUpdatedAt(entity.getUpdatedAt());
		return dto;
	}

	public static UserCvNotification updateEntity(UserCvNotification dto, UserCvNotificationDTO entity) {
		dto.setUserId(entity.getUserId());
		dto.setTitle(entity.getTitle());
		dto.setContent(entity.getContent());
		dto.setType(entity.getType());
		dto.setNotificationType(entity.getNotificationType().getId());
		dto.setUpdatedBy(entity.getUpdatedBy());
		dto.setUpdatedAt(new Date());
		return dto;
	}

	public static UserCvNotificationDTO toBasicDTO(UserCvNotification entity) {
		UserCvNotificationDTO dto = new UserCvNotificationDTO();
		dto.setId(entity.getId());
		dto.setStatus(ApplicantAction.getById(entity.getStatus()));
		dto.setNotificationType(UserCVNotificatinType.getById(entity.getNotificationType()));
		dto.setCreatedAt(entity.getCreatedAt());
		// set position, level.
		RecruitmentApply apply = entity.getApply();
		if (apply != null) {
			if (apply.getRecruitment() != null) {
				if (apply.getRecruitment().getRecruitmentPositionLevel() != null) {
					RecruitmentPositionDTO position = RecruitmentPositionDTO
							.toDTO(apply.getRecruitment().getRecruitmentPositionLevel());
					dto.setRecruitmentPosition(position);
				}
			}
		}
		// set apply id.
		if (entity.getApply() != null) {
			dto.setApplyId(entity.getApply().getId());
		}
		// set poi.
		PointOfInterest poiEntity = entity.getPoi();
		if (poiEntity != null) {
			PointOfInterestDTO poi = PointOfInterestDTO.toBasicDTO(poiEntity);
			dto.setPoi(poi);
		}
		return dto;
	}
}
