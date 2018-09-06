package vn.com.omart.backend.port.adapter.rest;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.com.omart.backend.application.UserCvNotificationService;
import vn.com.omart.backend.application.response.UserCvInterviewResultDTO;
import vn.com.omart.backend.application.response.UserCvNotificationDTO;
import vn.com.omart.backend.constants.OmartType.InterviewResult;
import vn.com.omart.backend.domain.model.EmptyJsonResponse;

@RestController
@RequestMapping("/v1")
public class UserCvNotificationResource {

	@Autowired
	private UserCvNotificationService userCvNotificationService;

	/**
	 * Save Notification.
	 * 
	 * @param userId
	 * @param UserCvNotificationDTO
	 * @return
	 */
	@RequestMapping(value = "/user/cv/notification", method = RequestMethod.POST)
	public ResponseEntity<EmptyJsonResponse> save(@RequestHeader(value = "X-User-Id", required = true) String userId,
			@RequestBody(required = true) UserCvNotificationDTO userCvDTO) {
		userCvNotificationService.save(userCvDTO, userId);
		return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.CREATED);
	}

	/**
	 * Save Notification.
	 * 
	 * @param userId
	 * @param UserCvNotificationDTO
	 * @return
	 */
	@RequestMapping(value = "/user/cv/notifications", method = RequestMethod.POST)
	public ResponseEntity<EmptyJsonResponse> saveAll(@RequestHeader(value = "X-User-Id", required = true) String userId,
			@RequestBody(required = true) List<UserCvNotificationDTO> userCvDTOs) {
		userCvNotificationService.save(userCvDTOs, userId);
		return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.CREATED);
	}

	/**
	 * Delete Notification.
	 * 
	 * @param userId
	 * @param notiId
	 * @return
	 */
	@RequestMapping(value = "/user/cv/notification/{noti-id}", method = RequestMethod.DELETE)
	public ResponseEntity<EmptyJsonResponse> delete(@RequestHeader(value = "X-User-Id", required = true) String userId,
			@PathVariable(value = "noti-id", required = true) Long notiId) {
		userCvNotificationService.delete(userId, notiId);
		return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.CREATED);
	}

	/**
	 * Get Notifications.
	 * 
	 * @param userId
	 * @param pageable
	 * @return List<UserCvNotificationDTO>
	 */
	@RequestMapping(value = "/user/cv/notification", method = RequestMethod.GET)
	public ResponseEntity<List<UserCvNotificationDTO>> getUserCV(
			@RequestHeader(value = "X-User-Id", required = true) String userId,
			@PageableDefault(size = 20, page = 0, sort = "createdAt", direction = Direction.DESC) Pageable pageable) {
		List<UserCvNotificationDTO> dtos = userCvNotificationService.getAllRecruitmentPush(userId, pageable);// getUserCVNotification(userId,
																												// pageable);
		return new ResponseEntity<List<UserCvNotificationDTO>>(dtos, HttpStatus.OK);
	}

	/**
	 * Get Notifications.
	 * 
	 * @param userId
	 * @param pageable
	 * @return List<UserCvNotificationDTO>
	 */
//	@RequestMapping(value = "/poi/cv/notification/{poi-id}", method = RequestMethod.GET)
//	public ResponseEntity<List<UserCvNotificationDTO>> getShopCV(
//			@RequestHeader(value = "X-User-Id", required = true) String userId,
//			@PathVariable(value = "poi-id", required = true) Long poiId,
//			@PageableDefault(size = 20, page = 0, sort = "createdAt", direction = Direction.ASC) Pageable pageable) {
//		List<UserCvNotificationDTO> dtos = userCvNotificationService.getShopCVNotification(userId, poiId, pageable);
//		return new ResponseEntity<List<UserCvNotificationDTO>>(dtos, HttpStatus.OK);
//	}
	
	@RequestMapping(value = "/poi/cv/notification/{poi-id}/interviews", method = RequestMethod.GET)
	public ResponseEntity<List<UserCvNotificationDTO>> getShopCV(
			@RequestHeader(value = "X-User-Id", required = true) String userId,
			@PathVariable(value = "poi-id", required = true) Long poiId,
			@PageableDefault(size = 20, page = 0, sort = "createdAt", direction = Direction.ASC) Pageable pageable) {
		List<UserCvNotificationDTO> dtos = userCvNotificationService.getShopCVNotificationV1(userId, poiId, pageable);
		return new ResponseEntity<List<UserCvNotificationDTO>>(dtos, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/poi/cv/notification/{poi-id}/invite-onboards", method = RequestMethod.GET)
	public ResponseEntity<List<UserCvNotificationDTO>> getListInviteOnboard(
			@RequestHeader(value = "X-User-Id", required = true) String userId,
			@PathVariable(value = "poi-id", required = true) Long poiId,
			@PageableDefault(size = 20, page = 0, sort = "createdAt", direction = Direction.ASC) Pageable pageable) {
		List<UserCvNotificationDTO> dtos = userCvNotificationService.getListInviteOnboard(poiId, pageable);
		return new ResponseEntity<List<UserCvNotificationDTO>>(dtos, HttpStatus.OK);
	}

	/**
	 * View detail notification by id.
	 * 
	 * @param userId
	 * @param id
	 * @return UserCvNotificationDTO
	 */
	@RequestMapping(value = "/applicant/cv/notification/{id}", method = RequestMethod.GET)
	public ResponseEntity<UserCvNotificationDTO> getCVNotificationDetail(
			@RequestHeader(value = "X-User-Id", required = true) String userId,
			@PathVariable(value = "id", required = true) Long id) {
		UserCvNotificationDTO dtos = userCvNotificationService.getCVNotificationDetail(id);
		return new ResponseEntity<UserCvNotificationDTO>(dtos, HttpStatus.OK);
	}

	/**
	 * Applicant action reject/accept
	 * 
	 * @param userId
	 * @param id
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/applicant/cv/notification/{id}/status", method = RequestMethod.PUT)
	public ResponseEntity<EmptyJsonResponse> applicantAction(@RequestHeader(value = "X-User-Id", required = true) String userId,
			@PathVariable(value = "id", required = true) Long id,
			@Valid @RequestBody(required = true) UserCvNotificationDTO dto) {
		userCvNotificationService.applicantAction(id, dto);
		return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.OK);
	}

	/**
	 * Interview Result
	 * 
	 * @param userId
	 * @param id
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/applicant/cv/notification/{id}/result", method = RequestMethod.PUT)
	public ResponseEntity<EmptyJsonResponse> applicantResult(@RequestHeader(value = "X-User-Id", required = true) String userId,
			@PathVariable(value = "id", required = true) Long id,
			@Valid @RequestBody(required = true) UserCvInterviewResultDTO dto) {
		userCvNotificationService.applicantResult(id, dto);
		return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.OK);
	}

}
