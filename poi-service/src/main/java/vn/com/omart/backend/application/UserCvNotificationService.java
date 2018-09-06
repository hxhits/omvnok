package vn.com.omart.backend.application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.com.omart.backend.application.response.UserCvInterviewResultDTO;
import vn.com.omart.backend.application.response.UserCvNotificationDTO;
import vn.com.omart.backend.application.response.UserDTO;
import vn.com.omart.backend.constants.OmartType.ApplicantAction;
import vn.com.omart.backend.constants.OmartType.InterviewResult;
import vn.com.omart.backend.constants.OmartType.RecruitmentApplyStatus;
import vn.com.omart.backend.constants.OmartType.UserCVNotificatinType;
import vn.com.omart.backend.domain.model.PointOfInterest;
import vn.com.omart.backend.domain.model.PointOfInterestRepository;
import vn.com.omart.backend.domain.model.RecruitmentApply;
import vn.com.omart.backend.domain.model.RecruitmentApplyRepository;
import vn.com.omart.backend.domain.model.UserCvNotification;
import vn.com.omart.backend.domain.model.UserCvNotificationRepository;
import vn.com.omart.backend.port.adapter.userprofile.UserService;
import vn.com.omart.sharedkernel.application.model.error.NotFoundException;
import vn.com.omart.sharedkernel.application.model.error.UnauthorizedAccessException;

@Service
public class UserCvNotificationService {

	private static final Logger logger = LoggerFactory.getLogger(UserCvNotificationService.class);

	@Autowired
	private UserCvNotificationRepository userCvNotificationRepository;

	@Autowired
	private PointOfInterestRepository poiRepository;

	@Autowired
	private RecruitmentApplyRepository applyRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private PoiNotificationService poiNotificationService;

	private final Long USER_TYPE = 0L;
	private final Long SHOP_TYPE = 1L;

	/**
	 * Save Notification.
	 * 
	 * @param dto
	 * @param userId
	 */
	@Transactional(readOnly = false)
	public void save(UserCvNotificationDTO dto, String userId) {
		if (null == userService.getOwner(userId)) {
			throw new UnauthorizedAccessException("User is Unauthorized");
		}

		List<UserCvNotification> entities = new ArrayList<UserCvNotification>();
		UserCvNotification entity = UserCvNotificationDTO.fromDTO(dto);

		PointOfInterest poi = null;
		if (dto.getPoiId() != null) {
			poi = poiRepository.findOne(dto.getPoiId());
		}
		if (null == poi) {
			throw new NotFoundException("POI not found.");
		}
		entity.setPoi(poi);

		RecruitmentApply apply = null;
		if (dto.getApplyId() != null) {
			apply = applyRepository.findOne(dto.getApplyId());
		}
		if (null == apply) {
			throw new NotFoundException("APPLY not found.");
		}
		entity.setApply(apply);
		entities.add(entity);

		// Update Interview Result
		if (dto.getNotificationType() == UserCVNotificatinType.INVITE_ONBOARD
				|| dto.getNotificationType() == UserCVNotificatinType.INTERVIEW_FAIL) {
			List<UserCvNotification> interview = userCvNotificationRepository.findByApplyAndNotificationType(apply,
					UserCVNotificatinType.INVITE_INTERVIEW.getId());
			if (interview != null && interview.size() > 0) {
				interview.forEach(item -> {
					item.setResult(dto.getResult().getId());
					item.setStatus(ApplicantAction.ACCEPT.getId());
				});

				entities.addAll(interview);
				// Reset new Entity
				entity.setResult(InterviewResult.NO_RESULT.getId());
			}
		}

		// Update Onboard Result
		if (dto.getNotificationType() == UserCVNotificatinType.ONBOARD_STATUS) {
			List<UserCvNotification> onboard = userCvNotificationRepository.findByApplyAndNotificationType(apply,
					UserCVNotificatinType.INVITE_ONBOARD.getId());
			if (onboard != null && onboard.size() > 0) {
				onboard.forEach(item -> {
					item.setStatus(dto.getResult() == InterviewResult.PASS ? ApplicantAction.ACCEPT.getId()
							: ApplicantAction.REJECT.getId());
				});

				entities.addAll(onboard);
				// Reset new Entity
				entity.setResult(InterviewResult.NO_RESULT.getId());
			}
		}

		userCvNotificationRepository.save(entities);
		poiNotificationService.send(Arrays.asList(entity));
	}

	/**
	 * Save Notifications.
	 * 
	 * @param dtos
	 * @param userId
	 */
	@Transactional(readOnly = false)
	public void save(List<UserCvNotificationDTO> dtos, String userId) {
		if (null == userService.getOwner(userId)) {
			throw new UnauthorizedAccessException("User is Unauthorized");
		}

		if (dtos == null || dtos.size() < 1) {
			throw new NotFoundException("NOTIs not found.");
		}

		Map<Long, PointOfInterest> dicPoi = StreamSupport.stream(poiRepository
				.findAll(dtos.stream().map(item -> item.getPoiId()).collect(Collectors.toList())).spliterator(), false)
				.collect(Collectors.toMap(PointOfInterest::id, Function.identity()));
		Map<Long, RecruitmentApply> dicApply = applyRepository
				.findAll(dtos.stream().map(item -> item.getApplyId()).collect(Collectors.toList())).stream()
				.collect(Collectors.toMap(RecruitmentApply::getId, Function.identity()));

		List<UserCvNotification> entities = new ArrayList<UserCvNotification>();

		for (UserCvNotificationDTO dto : dtos) {
			if (dto.getPoiId() != null && dto.getApplyId() != null && dicPoi.containsKey(dto.getPoiId())
					&& dicApply.containsKey(dto.getApplyId())) {
				UserCvNotification entity = UserCvNotificationDTO.fromDTO(dto);

				entity.setPoi(dicPoi.get(dto.getPoiId()));
				entity.setApply(dicApply.get(dto.getApplyId()));
				entities.add(entity);
			}
		}

		entities = userCvNotificationRepository.save(entities);
		poiNotificationService.send(entities);
	}

	/**
	 * Delete Notification.
	 * 
	 * @param dto
	 * @param notiId
	 */
	@Transactional(readOnly = false)
	public void delete(String userId, Long notiId) {
		if (null == userService.getOwner(userId)) {
			throw new UnauthorizedAccessException("User is Unauthorized");
		}

		UserCvNotification entity = userCvNotificationRepository.findOne(notiId);
		if (null == entity) {
			throw new NotFoundException("NOTI not found.");
		}

		entity.setDeleted(true);

		userCvNotificationRepository.save(entity);
	}

	/**
	 * Get User Notifications.
	 * 
	 * @param userId
	 * @param pageable
	 * @return List<UserCvNotificationDTO>
	 */
	public List<UserCvNotificationDTO> getUserCVNotification(String userId, Pageable pageable) {
		if (null == userService.getOwner(userId)) {
			throw new UnauthorizedAccessException("User is Unauthorized");
		}

		List<UserCvNotification> entities = userCvNotificationRepository.findAllByUserIdAndTypeAndIsDeleted(userId,
				USER_TYPE, false, pageable);
		List<UserCvNotificationDTO> dtos = entities.stream().map(UserCvNotificationDTO::toDTO)
				.collect(Collectors.toList());

		// Get User info
		Set<String> cmUserIds = UserCvNotificationDTO.userIds;
		List<Object[]> userCMs = userCvNotificationRepository
				.getUserInUserIds(cmUserIds.toArray(new String[cmUserIds.size()]));
		Map<String, UserDTO> dicUsers = userCMs.stream().map(UserDTO::toDTO)
				.collect(Collectors.toMap(UserDTO::getUserId, Function.identity()));

		// Set User info
		dtos.forEach(dto -> {
			if (dto.getUserId() != null && dicUsers.containsKey(dto.getUserId())) {
				dto.setUser(dicUsers.get(dto.getUserId()));
			}
		});
		return dtos;
	}

	/**
	 * Get Shop Notifications.
	 * 
	 * @param userId
	 * @param poiId
	 * @param pageable
	 * @return List<UserCvNotificationDTO>
	 */
	public List<UserCvNotificationDTO> getShopCVNotification(String userId, Long poiId, Pageable pageable) {
		if (null == userService.getOwner(userId)) {
			throw new UnauthorizedAccessException("User is Unauthorized");
		}

		PointOfInterest poi = poiRepository.findOne(poiId);
		if (null == poi) {
			throw new NotFoundException("POI not found.");
		}

		if (!userId.equals(poi.ownerId())) {
			throw new UnauthorizedAccessException("User is Unauthorized");
		}

		List<UserCvNotification> entities = userCvNotificationRepository.findAllByPoiAndTypeAndIsDeleted(poi, SHOP_TYPE,
				false, pageable);
		List<UserCvNotificationDTO> dtos = entities.stream().map(UserCvNotificationDTO::toDTO)
				.collect(Collectors.toList());

		// Get User info
		Set<String> cmUserIds = UserCvNotificationDTO.userIds;
		List<Object[]> userCMs = userCvNotificationRepository
				.getUserInUserIds(cmUserIds.toArray(new String[cmUserIds.size()]));
		Map<String, UserDTO> dicUsers = userCMs.stream().map(UserDTO::toDTO)
				.collect(Collectors.toMap(UserDTO::getUserId, Function.identity()));

		// Set User info
		dtos.forEach(dto -> {
			if (dto.getUserId() != null && dicUsers.containsKey(dto.getUserId())) {
				dto.setUser(dicUsers.get(dto.getUserId()));
			}
		});
		return dtos;
	}

	/**
	 * Get interviews.
	 * 
	 * @param userId
	 * @param poiId
	 * @param pageable
	 * @return List<UserCvNotificationDTO>
	 */
	public List<UserCvNotificationDTO> getShopCVNotificationV1(String userId, Long poiId, Pageable pageable) {
		PointOfInterest poi = poiRepository.findOne(poiId);
		if (poi != null) {
			List<UserCvNotification> cvNotifications = userCvNotificationRepository
					.findAllByPoiAndNotificationTypeAndIsDeletedOrderByUpdatedAtDesc(poi, UserCVNotificatinType.INVITE_INTERVIEW.getId(),
							false, pageable)
					.stream().filter(i -> i.getApply().isDeleted() == false).collect(Collectors.toList());
			List<UserCvNotificationDTO> dtos = cvNotifications.stream().map(UserCvNotificationDTO::toDTO)
					.collect(Collectors.toList());
			return dtos;
		}
		return new ArrayList<>();
	}

	/**
	 * Get invite onboards.
	 * 
	 * @param userId
	 * @param poiId
	 * @param pageable
	 * @return List<UserCvNotificationDTO>
	 */
	public List<UserCvNotificationDTO> getListInviteOnboard(Long poiId, Pageable pageable) {
		PointOfInterest poi = poiRepository.findOne(poiId);
		if (poi != null) {
			List<UserCvNotification> cvNotifications = userCvNotificationRepository
					.findAllByPoiAndNotificationTypeAndIsDeletedOrderByUpdatedAtDesc(poi, UserCVNotificatinType.INVITE_ONBOARD.getId(),
							false, pageable)
					.stream().filter(i -> i.getApply().isDeleted() == false).collect(Collectors.toList());
			List<UserCvNotificationDTO> dtos = cvNotifications.stream().map(UserCvNotificationDTO::toDTO)
					.collect(Collectors.toList());
			return dtos;
		}
		return new ArrayList<>();
	}

	/**
	 * View Detail.
	 * 
	 * @param id
	 * @return UserCvNotificationDTO
	 */
	public UserCvNotificationDTO getCVNotificationDetail(Long id) {
		UserCvNotificationDTO dto = new UserCvNotificationDTO();
		UserCvNotification entity = userCvNotificationRepository.findOne(id);
		if (entity != null) {
			dto = UserCvNotificationDTO.toBasicDTO(entity);
			dto.setContent(entity.getContent());
		}
		return dto;
	}

	/**
	 * Get All Recruit push notification.
	 * 
	 * @return List of UserCvNotificationDTO.
	 */
	public List<UserCvNotificationDTO> getAllRecruitmentPush(String userId, Pageable pageable) {
		List<UserCvNotification> entities = userCvNotificationRepository.findAllByUserId(userId, pageable);
		List<UserCvNotificationDTO> dtos = new ArrayList<>();
		if (entities != null) {
			dtos = entities.stream().map(UserCvNotificationDTO::toBasicDTO).collect(Collectors.toList());
		}
		return dtos;
	}

	/**
	 * Applicant action ACCEPT/REJECT
	 * 
	 * @param id
	 * @param action
	 */
	public void applicantAction(Long id, UserCvNotificationDTO dto) {
		UserCvNotification entity = userCvNotificationRepository.findOne(id);
		if (entity != null) {
			RecruitmentApply recruitmentApply = applyRepository.findOne(entity.getApply().getId());
			if (recruitmentApply != null) {
				switch (dto.getNotificationType()) {
				case INVITE_INTERVIEW:
					if (dto.getStatus() == ApplicantAction.ACCEPT) {
						entity.setStatus(ApplicantAction.ACCEPT.getId());
						recruitmentApply.setStatus(RecruitmentApplyStatus.ACCEPT.getId());
					} else if (dto.getStatus() == ApplicantAction.REJECT) {
						entity.setStatus(ApplicantAction.REJECT.getId());
						recruitmentApply.setStatus(RecruitmentApplyStatus.REJECT.getId());
					}
					break;

				case INVITE_ONBOARD:
					if (dto.getStatus() == ApplicantAction.ACCEPT) {
						entity.setStatus(ApplicantAction.ACCEPT.getId());
						recruitmentApply.setStatus(RecruitmentApplyStatus.ACCEPT_ONBOARD.getId());
					} else if (dto.getStatus() == ApplicantAction.REJECT) {
						entity.setStatus(ApplicantAction.REJECT.getId());
						recruitmentApply.setStatus(RecruitmentApplyStatus.REJECT_ONBOARD.getId());
					}
					break;

				default:
					break;
				}
				userCvNotificationRepository.save(entity);
				applyRepository.save(recruitmentApply);
			} else {
				logger.error("Applicant Action Recruitment Apply with id " + entity.getApply().getId() + " not found");
			}
		} else {
			logger.error("Applicant Action with id " + id + " not found");
		}
	}

	/**
	 * Interview Result
	 * 
	 * @param id
	 * @param action
	 */
	public void applicantResult(Long id, UserCvInterviewResultDTO dto) {
		UserCvNotification entity = userCvNotificationRepository.findOne(id);
		if (entity != null) {
			entity.setResult(dto.getResult().getId());
			userCvNotificationRepository.save(entity);
		} else {
			logger.error("Applicant Action with id " + id + " not found");
		}
	}
}
