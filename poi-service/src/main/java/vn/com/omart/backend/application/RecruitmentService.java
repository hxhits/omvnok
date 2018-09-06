package vn.com.omart.backend.application;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.com.omart.backend.application.response.PoiNotificationDTO;
import vn.com.omart.backend.application.response.RecruitmentDTO;
import vn.com.omart.backend.application.util.CommonUtils;
import vn.com.omart.backend.constants.ConstantUtils;
import vn.com.omart.backend.constants.OmartType;
import vn.com.omart.backend.domain.model.PoiNotification;
import vn.com.omart.backend.domain.model.PoiNotificationRepository;
import vn.com.omart.backend.domain.model.PointOfInterest;
import vn.com.omart.backend.domain.model.PointOfInterestRepository;
import vn.com.omart.backend.domain.model.Recruitment;
import vn.com.omart.backend.domain.model.RecruitmentPosition;
import vn.com.omart.backend.domain.model.RecruitmentPositionLevel;
import vn.com.omart.backend.domain.model.RecruitmentPositionLevelRepository;
import vn.com.omart.backend.domain.model.RecruitmentPositionRepository;
import vn.com.omart.backend.domain.model.RecruitmentRepository;
import vn.com.omart.backend.domain.model.Timeline;
import vn.com.omart.sharedkernel.application.model.error.NotFoundException;

@Service
public class RecruitmentService {

	private final Logger logger = getLogger(RecruitmentService.class);

	@Autowired
	private RecruitmentRepository recruitmentRepository;

	@Autowired
	private RecruitmentPositionRepository recruitmentPosRepository;

	@Autowired
	private RecruitmentPositionLevelRepository recruitmentPosLvlRepository;

	@Autowired
	private PointOfInterestRepository pointOfInterestRepository;

	@Autowired
	private PoiNotificationService poiNotificationService;

	@Autowired
	private PoiNotificationRepository poiNotificationRepository;
	
	@Value("${share.facebook.path.recruit}")
	private String Html_Share_Recruit_Content;

	@Autowired
	private ResourceLoaderService resourceLoaderService;

	/**
	 * Get Salary.
	 * 
	 * @param recruitment
	 * @return salary message
	 */
	private String getSalary(Recruitment recruitment) {
		String salary = "";
		if (StringUtils.isNumeric(recruitment.getSalary())) {
			salary = CommonUtils.getSalaryLable(Integer.parseInt(recruitment.getSalary()));
		}
		return salary;
	}

	/**
	 * Get Salary V1.
	 * 
	 * @param recruitment
	 * @return salary message
	 */
	private String getSalaryV1(Recruitment recruitment) {
		String salary = "";
		if (StringUtils.isNumeric(recruitment.getSalary())) {
			salary = CommonUtils.getSalaryLableV1(Integer.parseInt(recruitment.getSalary()));
		}
		return salary;
	}

	/**
	 * Get Recruit Title.
	 * 
	 * @param recruitment
	 * @return
	 */
	private String getRecruitTitle(Recruitment recruitment) {
		String recruitTitle = "";
		if (!StringUtils.isBlank(recruitment.getTitle())) {
			recruitTitle = CommonUtils.getRecruitTitle(Integer.parseInt(recruitment.getTitle()));
		}
		return recruitTitle;
	}

	/**
	 * Handle title before push notification.
	 * 
	 * @param recruitment
	 * @param recPos
	 * @param recPosLvl
	 * @return title
	 */
	private String getRecruitPushTitle(Recruitment recruitment, RecruitmentPosition recPos,
			RecruitmentPositionLevel recPosLvl) {
		String subBody = "";
		if (CommonUtils.isRecruitPositionIdExisting(recPos.getId())) {
			if (recruitment.getQuantity() > 0) {
				subBody = recruitment.getQuantity() + " " + recPos.getName() + " " + recPosLvl.getName();
			} else {
				subBody = recPos.getName() + " " + recPosLvl.getName();
			}
		} else {
			if (recruitment.getQuantity() > 0) {
				subBody = recruitment.getQuantity() + " " + recPosLvl.getName() + " " + recPos.getName();
			} else {
				subBody = recPosLvl.getName() + " " + recPos.getName();
			}
		}
		return subBody;
	}

	/**
	 * Save.
	 * 
	 * @param dto
	 */
	@Transactional(readOnly = false)
	public void save(RecruitmentDTO dto, String userId, int apiVersion) {
		// save recruit.
		if (Optional.ofNullable(dto.getPoiId()).orElse(0L).intValue() != 0) {
			Recruitment recruitment = RecruitmentDTO.toNewEntity(dto);
			RecruitmentPosition recPos = recruitmentPosRepository.findOne(dto.getPositionId());
			if (recPos == null || dto.getPositionLevelId() == null) {
				return;
			}
			recruitment.setRecruitmentPosition(recPos);
			RecruitmentPositionLevel recPosLvl = recruitmentPosLvlRepository.findOne(dto.getPositionLevelId());
			if (recPosLvl == null) {
				return;
			}
			recruitment.setRecruitmentPositionLevel(recPosLvl);
			PointOfInterest poi = pointOfInterestRepository.findOne(dto.getPoiId());
			recruitment.setPoi(poi);
			recruitment = recruitmentRepository.save(recruitment);

			String subBody = "";
			if (StringUtils.isBlank(recPosLvl.getTitle())) {
				subBody = this.getRecruitPushTitle(recruitment, recPos, recPosLvl);
			} else {
				subBody = recPosLvl.getTitle();
			}

			String body = "";
			if (apiVersion == 1) {
				body = String.format(ConstantUtils.RECRUIT_PUSH_BODY, subBody, recruitment.getPoi().province().name(),
						this.getSalaryV1(recruitment));
			} else {
				body = String.format(ConstantUtils.RECRUIT_PUSH_BODY, subBody, recruitment.getPoi().province().name(),
						this.getSalary(recruitment));
			}

			String title = String.format(ConstantUtils.POI_NOTIFICATION_PUSH_TITLE, recruitment.getPoi().name());

			// save poi notification.
			PoiNotificationDTO poiNotificationDTO = new PoiNotificationDTO();
			poiNotificationDTO.setNotificationType(OmartType.NotificationType.RECRUIT.getId());
			poiNotificationDTO.setPoiId(poi.id());
			poiNotificationDTO.setDescription(body);
			poiNotificationDTO.setRecruit(new RecruitmentDTO(recruitment.getId()));
			PoiNotification poiNoti = poiNotificationService.save(userId, poiNotificationDTO, recPosLvl);

			// set data.
			Map<String, String> data = new HashMap<String, String>();
			data.put("type", "2");
			data.put("id", poiNoti.getId().toString());
			data.put("userId", userId);
			data.put("payload", body);
			data.put("poiId", poi.id().toString());
			data.put("poiName", poi.name());
			data.put("recruitmentId", recruitment.getId().toString());
			data.put("positionName", recPosLvl.getName() + " " + recPos.getName());
			data.put("title", title);
			data.put("description", body);

			// Send notification
			poiNotificationService.send(recruitment, userId, title, body, data, recPosLvl.getId());
		}
	}

	/**
	 * Update.
	 * 
	 * @param id
	 * @param dto
	 */
	public void update(Long id, RecruitmentDTO dto,int apiVersion) {
		Recruitment recruitment = recruitmentRepository.findOne(id);
		if (recruitment != null) {
			dto.setId(id);
			Recruitment entity = RecruitmentDTO.toUpdateEntity(dto);
			RecruitmentPosition recPos = recruitmentPosRepository.findOne(dto.getPositionId());
			entity.setRecruitmentPosition(recPos);
			RecruitmentPositionLevel recPosLvl = recruitmentPosLvlRepository.findOne(dto.getPositionLevelId());
			entity.setRecruitmentPositionLevel(recPosLvl);
			PointOfInterest poi = pointOfInterestRepository.findOne(dto.getPoiId());
			entity.setPoi(poi);
			entity.setViewCount(recruitment.getViewCount());
			entity.setCreatedAt(recruitment.getCreatedAt());
			entity = recruitmentRepository.save(entity);
			
			// update poinotification.
			if (dto.getPoiNotificationId() != null) {
				PoiNotification poiNotification = poiNotificationRepository.findOne(dto.getPoiNotificationId());
				if (poiNotification != null) {
					
					String subBody = "";
					if (StringUtils.isBlank(recPosLvl.getTitle())) {
						if (CommonUtils.isRecruitPositionIdExisting(recPos.getId())) {
							subBody = recPos.getName() + " " + recPosLvl.getName();
						} else {
							subBody = recPosLvl.getName() + " " + recPos.getName();
						}
					} else {
						subBody = recPosLvl.getTitle();
					}
					String body = "";
					if(apiVersion == 1) {
						 body = String.format(ConstantUtils.RECRUIT_PUSH_BODY, subBody,
								recruitment.getPoi().province().name(), this.getSalaryV1(recruitment));
					} else {
						 body = String.format(ConstantUtils.RECRUIT_PUSH_BODY, subBody,
								recruitment.getPoi().province().name(), this.getSalary(recruitment));
					}
					
					poiNotification.setDescription(body);
					poiNotification.setRecruitmentPositionLevel(entity.getRecruitmentPositionLevel());
					if (entity.isDeleted() && entity.getState() == 1) {
						poiNotification.setActive(false);
					}
					poiNotificationRepository.save(poiNotification);
				} else {
					logger.error("Recruit was null at update function");
				}
			}
		}
	}

	/**
	 * Get Recruit By Poi.
	 * 
	 * @param poiId
	 * @return List of RecruitmentDTO
	 */
	public List<RecruitmentDTO> getRecruitByPoi(String userId, Long poiId) {
		boolean isOwner = false;
		PointOfInterest poi = pointOfInterestRepository.findOne(poiId);
		if (userId != null && userId.equals(poi.ownerId())) {
			isOwner = true;
		}
		List<Recruitment> entities = null;
		if (isOwner) {
			entities = recruitmentRepository.findByPoiAndIsDeletedOrderByCreatedAtDesc(poi, false);
		} else {
			entities = recruitmentRepository.findByPoiAndIsDeletedAndStateOrderByCreatedAtDesc(poi, false, 0);
		}
		return entities.stream().map(RecruitmentDTO::toDTO).collect(Collectors.toList());
	}

	/**
	 * Get Recruit By Id.
	 * 
	 * @param id
	 * @return RecruitmentDTO
	 */
	public RecruitmentDTO getRecruitById(Long id) {
		Recruitment entity = recruitmentRepository.findOne(id);
		if (entity == null) {
			throw new NotFoundException("Recruitment was not found");
		}
		return RecruitmentDTO.toDTO(entity);
	}

	/**
	 * Delete.
	 * 
	 * @param id
	 */
	public void delete(Long id) {
		Recruitment entity = recruitmentRepository.findOne(id);
		if (entity != null) {
			entity.setDeleted(true);
			if (!entity.getPoiNotifications().isEmpty()) {
				Long poiNotiId = entity.getPoiNotifications().get(0).getId();
				PoiNotification poiNoti = poiNotificationRepository.findOne(poiNotiId);
				poiNoti.setActive(false);
				poiNotificationRepository.save(poiNoti);
			}
			recruitmentRepository.save(entity);
		}
	}

	/**
	 * View Count.
	 * 
	 * @param id
	 */
	public void viewCount(Long id) {
		Recruitment entity = recruitmentRepository.findOne(id);
		if (entity != null) {
			int viewCount = entity.getViewCount() + 1;
			entity.setViewCount(viewCount);
			recruitmentRepository.save(entity);
		}
	}

	/**
	 * Repush
	 * 
	 * @param userId
	 * @param id
	 */
	public void rePushRecruitment(String userId, Long id) {
		Recruitment recruitment = recruitmentRepository.findOne(id);
		if (recruitment != null) {
			// save data.
			String body = String.format(ConstantUtils.RECRUIT_PUSH_BODY, recruitment.getPoi().province().name(),
					this.getRecruitTitle(recruitment), recruitment.getRecruitmentPosition().getName(),
					this.getSalary(recruitment));
			// save poi notification.
			PoiNotificationDTO poiNotificationDTO = new PoiNotificationDTO();
			poiNotificationDTO.setNotificationType(OmartType.NotificationType.RECRUIT.getId());
			poiNotificationDTO.setPoiId(recruitment.getPoi().id());
			poiNotificationDTO.setDescription(body);
			poiNotificationDTO.setRecruit(new RecruitmentDTO(recruitment.getId()));
			PoiNotification poiNoti = poiNotificationService.save(userId, poiNotificationDTO,
					recruitment.getRecruitmentPositionLevel());
			// repush
			poiNotificationService.rePushNotification(poiNoti.getId());
		} else {
			logger.error("Recruitment was null at repush function");
		}
	}
	
	/**
	 * Get all.
	 * @param pageable
	 * @return
	 */
	public ResponseEntity<Page<RecruitmentDTO>> getAllByFilterIsDeleted(Pageable pageable){
		 Page<Recruitment> entities = recruitmentRepository.getAllByFilterIsDeletedOrderByUpdatedAtDesc(pageable);
		 // Fix empty response: 27/Aug/18
		 //if(!entities.getContent().isEmpty()) {
		 Page<RecruitmentDTO> dtos = entities.map(new RecruitmentDTO.QueryMapper()::map);
		 return new ResponseEntity<Page<RecruitmentDTO>>(dtos,HttpStatus.OK);
		 //}
		 //return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	public String shareFacebook(Long recruitId) {
		Recruitment entity = recruitmentRepository.findOne(recruitId);
		String htmlContent = "";
		if (entity != null) {
			htmlContent = resourceLoaderService.getResource(Html_Share_Recruit_Content);
			if (htmlContent != null) {
				String imageUrl = "https://img001.omartvietnam.com/JmJPwW75DQEamX9MxOZpNNCrJ9Q=/572fd8659ee042fbb55d5555b6e9dcf7/1111.jpg.jpg";
				String name = "";
				String desc = "";

				name = ConstantUtils.RECRUIT_SHARE_TITLE + entity.getRecruitmentPositionLevel().getTitle() + " tại "+entity.getPoi().name();

				if (entity.getDescription() != null) {
					desc = entity.getDescription();
				}

				htmlContent = htmlContent.replace("__RECRUIT_ID__", "" + entity.getId());
				htmlContent = htmlContent.replace("__PAGE_TITLE__", name);
				htmlContent = htmlContent.replace("__PAGE_DESC__", desc);
				htmlContent = htmlContent.replace("__PAGE_IMAGE__", imageUrl);

				htmlContent = htmlContent.replace("__IOS_URL__", "omart://recruit/" + entity.getId());
				htmlContent = htmlContent.replace("__ANDROID_URL__", "omart://recruit/" + entity.getId());
			}
		}
		return htmlContent;
	}
}
