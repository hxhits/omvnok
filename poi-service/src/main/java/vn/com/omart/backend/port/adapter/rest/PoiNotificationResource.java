package vn.com.omart.backend.port.adapter.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.com.omart.backend.application.CategoryFollowService;
import vn.com.omart.backend.application.PoiNotificationService;
import vn.com.omart.backend.application.response.CategoryFollowDTO;
import vn.com.omart.backend.application.response.PoiNotificationDTO;
import vn.com.omart.backend.application.response.StoreProcedureParams;
import vn.com.omart.backend.constants.OmartType;
import vn.com.omart.backend.domain.model.EmptyJsonResponse;

@RestController
@RequestMapping("/v1")
public class PoiNotificationResource {

	@Autowired
	private CategoryFollowService categoryFollowService;

	@Autowired
	private PoiNotificationService poiNotificationService;

	/**
	 * Save Categories Follow.
	 * 
	 * @param dto
	 * @param userId
	 * @return EmptyJsonResponse
	 */
	@RequestMapping(value = "/pois/categories", method = RequestMethod.POST)
	public ResponseEntity<EmptyJsonResponse> postCategoriesFollow(@RequestBody(required = true) CategoryFollowDTO dto,
			@RequestHeader(value = "X-User-Id", required = false) String userId) {
		categoryFollowService.save(userId, dto);
		return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.CREATED);
	}

	/**
	 * Poi Push Notification to user.
	 * 
	 * @param dto
	 * @param userId
	 * @return EmptyJsonResponse
	 */
	@RequestMapping(value = "/pois/push-notfication", method = RequestMethod.POST)
	public ResponseEntity<EmptyJsonResponse> poiPushNotification(@RequestBody(required = true) PoiNotificationDTO dto,
			@RequestHeader(value = "X-User-Id", required = true) String userId) {
		poiNotificationService.poiPushNotification(userId, dto);
		return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.CREATED);
	}

	/**
	 * Get Poi Push Notification.
	 * 
	 * @param userId
	 * @return List of PoiNotificationDTO
	 */
	@RequestMapping(value = "/pois/push-notfications", method = RequestMethod.POST)
	public ResponseEntity<List<PoiNotificationDTO>> getPoiPushNotification(
			@PageableDefault(size = Integer.MAX_VALUE, page = 0) Pageable pageable,
			@RequestBody(required = true) StoreProcedureParams params,
			@RequestHeader(value = "X-User-Id", required = false) String userId) {
		List<PoiNotificationDTO> dtoList = poiNotificationService.getPoiPushNotification(params, pageable);
		return new ResponseEntity<List<PoiNotificationDTO>>(dtoList, HttpStatus.OK);
	}

	/**
	 * Get Poi Push Notification. V2
	 * 
	 * @param userId
	 * @return List of PoiNotificationDTO
	 */
	@RequestMapping(value = "/pois/push-notfications", method = RequestMethod.POST, headers = { "X-API-Version=1.2" })
	public ResponseEntity<List<PoiNotificationDTO>> getPoiPushNotificationV2(
			@PageableDefault(size = Integer.MAX_VALUE, page = 0) Pageable pageable,
			@RequestBody(required = true) StoreProcedureParams params,
			@RequestHeader(value = "X-User-Id", required = false,defaultValue="") String userId) {
		List<PoiNotificationDTO> dtoList = poiNotificationService.getPoiPushNotificationV2(userId,params, pageable);
		return new ResponseEntity<List<PoiNotificationDTO>>(dtoList, HttpStatus.OK);
	}

	/**
	 * Get Poi Push Notification. V4
	 * 
	 * @param userId
	 * @return List of PoiNotificationDTO
	 */
	@RequestMapping(value = "/pois/push-notfications", method = RequestMethod.POST, headers = { "X-API-Version=1.4" })
	public ResponseEntity<List<PoiNotificationDTO>> getPoiPushNotificationV4(
			@PageableDefault(size = Integer.MAX_VALUE, page = 0) Pageable pageable,
			@RequestBody(required = true) StoreProcedureParams params,
			@RequestHeader(value = "X-User-Id", required = true) String userId) {
		List<PoiNotificationDTO> dtoList = poiNotificationService.getPoiPushNotificationV4(params, userId, pageable);
		return new ResponseEntity<List<PoiNotificationDTO>>(dtoList, HttpStatus.OK);
	}

	/**
	 * Get poi notification latest 10 items.
	 * 
	 * @param pageable
	 * @return List of PoiNotificationDTO
	 */
	@RequestMapping(value = "/pois/push-notfications", method = RequestMethod.GET)
	public ResponseEntity<List<PoiNotificationDTO>> getPoiPushNotification(
			@PageableDefault(size = 10, page = 0) Pageable pageable) {
		int[] notiTypes = { OmartType.NotificationType.SYS.getId(), OmartType.NotificationType.POI.getId() };
		List<PoiNotificationDTO> pois = poiNotificationService.getPoiPushNotification(notiTypes, pageable);
		return new ResponseEntity<List<PoiNotificationDTO>>(pois, HttpStatus.OK);
	}

	/**
	 * Get push notification by id.
	 * 
	 * @param id
	 * @return PoiNotificationDTO
	 */
	@RequestMapping(value = "/pois/push-notification/{push-notfication-id}", method = RequestMethod.GET)
	public ResponseEntity<PoiNotificationDTO> getPoiNotificationById(
			@PathVariable(required = true, value = "push-notfication-id") Long id) {
		PoiNotificationDTO dto = poiNotificationService.getPoiNotificationById(id);
		return new ResponseEntity<PoiNotificationDTO>(dto, HttpStatus.OK);
	}

	/**
	 * Get Poi Notification By PoiId.
	 * 
	 * @param id
	 * @param pageable
	 * @return List of PoiNotificationDTO
	 */
	@RequestMapping(value = "/pois/push-notification/{poi-id}/poiId", method = RequestMethod.GET)
	public ResponseEntity<List<PoiNotificationDTO>> getPoiNotificationByPoiId(
			@RequestHeader(value = "X-User-Id", required = true) String userId,
			@PathVariable(required = true, value = "poi-id") Long id,
			@PageableDefault(size = Integer.MAX_VALUE, page = 0) Pageable pageable) {
		List<PoiNotificationDTO> dtoList = poiNotificationService.getPoiNotificationByPoiId(id, pageable);
		return new ResponseEntity<List<PoiNotificationDTO>>(dtoList, HttpStatus.OK);
	}

	/**
	 * update
	 * 
	 * @param userId
	 * @param id
	 * @param dto
	 * @return EmptyJsonResponse
	 */
	@RequestMapping(value = "/pois/push-notification/{id}", method = RequestMethod.PUT)
	public ResponseEntity<EmptyJsonResponse> update(@RequestHeader(value = "X-User-Id", required = true) String userId,
			@PathVariable(required = true, value = "id") Long id,
			@RequestBody(required = true) PoiNotificationDTO dto) {
		poiNotificationService.update(id, dto);
		return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.OK);
	}

	/**
	 * Delete
	 * 
	 * @param userId
	 * @param id
	 * @return EmptyJsonResponse
	 */
	@RequestMapping(value = "/pois/push-notification/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<EmptyJsonResponse> delete(@RequestHeader(value = "X-User-Id", required = true) String userId,
			@PathVariable(required = true, value = "id") Long id) {
		poiNotificationService.delete(id);
		return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.OK);
	}

	/**
	 * Re-Push Poi Notification.
	 * 
	 * @param userId
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/pois/push-notification/{push-id}/repush", method = RequestMethod.GET)
	public ResponseEntity<EmptyJsonResponse> rePushNotification(
			@RequestHeader(value = "X-User-Id", required = true) String userId,
			@PathVariable(value = "push-id", required = true) Long id) {
		poiNotificationService.rePushNotification(id);
		return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.OK);
	}

	/**
	 * UPDATE COPY CREATED_AT TO UPDATED_AT IN DB. THIS FUNCTION ONLY FOR DEVELOPER
	 * 
	 * @return EmptyJsonResponse
	 */
	@RequestMapping(value = "/pois/push-notification/SUA", method = RequestMethod.GET)
	public ResponseEntity<EmptyJsonResponse> updateUpdatedAt() {
		poiNotificationService.setUpdatedAt();
		return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.OK);
	}

}
