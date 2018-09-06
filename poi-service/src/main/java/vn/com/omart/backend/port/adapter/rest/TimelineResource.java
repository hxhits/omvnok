package vn.com.omart.backend.port.adapter.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import vn.com.omart.backend.application.MobileAdminService;
import vn.com.omart.backend.application.TimelineActionService;
import vn.com.omart.backend.application.TimelineCommentService;
import vn.com.omart.backend.application.TimelineReportAbuseService;
import vn.com.omart.backend.application.TimelineService;
import vn.com.omart.backend.application.response.ProvinceDTO;
import vn.com.omart.backend.application.response.TimelineActionDTO;
import vn.com.omart.backend.application.response.TimelineCommentDTO;
import vn.com.omart.backend.application.response.TimelineDTO;
import vn.com.omart.backend.application.response.TimelineHistoryDTO;
import vn.com.omart.backend.application.response.TimelineReportAbuseDTO;
import vn.com.omart.backend.constants.Device;
import vn.com.omart.backend.domain.model.EmptyJsonResponse;

@RestController
@RequestMapping("/v1")
public class TimelineResource {

	@Autowired
	private MobileAdminService mobileAdminService;

	@Autowired
	private TimelineActionService timelineActionService;

	@Autowired
	private TimelineService timelineService;

	@Autowired
	private TimelineCommentService timelineCommentService;

	@Autowired
	private TimelineReportAbuseService timelineReportAbuseService;

	/**
	 * Create a timeline.
	 * 
	 * @param userId
	 * @param timelineDTO
	 * @return
	 */
	@RequestMapping(value = "/timeline", method = RequestMethod.POST)
	public ResponseEntity<TimelineDTO> createTimeline(@RequestHeader(value = "X-User-Id") String userId,
			@RequestHeader(value = "client", required = false, defaultValue = "mobile") Device device,
			@RequestBody(required = true) TimelineDTO timelineDTO) {
		TimelineDTO entity = timelineService.createTimeline(userId, timelineDTO, device);
		return new ResponseEntity<TimelineDTO>(entity, HttpStatus.CREATED);
	}

	/**
	 * Store love hide history.
	 * 
	 * @param userId
	 * @param device
	 * @param timelineDTO
	 * @return EmptyJsonResponse
	 */
	@RequestMapping(value = "/timeline/lovehide/history", method = RequestMethod.PUT)
	public ResponseEntity<EmptyJsonResponse> saveLoveHideHistory(@RequestHeader(value = "X-User-Id") String userId,
			@RequestHeader(value = "client", required = false, defaultValue = "mobile") Device device,
			@RequestBody(required = true) TimelineDTO timelineDTO) {
		timelineService.storeLoveHideHistory(timelineDTO);
		return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.OK);
	}

	/**
	 * Get love hide histories.
	 * 
	 * @param userId
	 * @param pageable
	 * @return List of TimelineHistoryDTO
	 */
	@RequestMapping(value = "/timeline/lovehide/history", method = RequestMethod.GET)
	public ResponseEntity<List<TimelineHistoryDTO>> getLovehideHistories(
			@RequestHeader(value = "X-User-Id", required = true) String userId,
			@PageableDefault(size = Integer.MAX_VALUE, page = 0) Pageable pageable) {
		return new ResponseEntity<List<TimelineHistoryDTO>>(timelineService.getLoveHideHistories(userId, pageable),
				HttpStatus.OK);
	}

	/**
	 * Get timeline.
	 * 
	 * @param userId
	 * @param latitude
	 * @param longitude
	 * @param radius
	 * 
	 * @param pageable
	 * @return List of TimelineDTO
	 */
	@RequestMapping(value = "/timeline", method = RequestMethod.GET)
	public ResponseEntity<List<TimelineDTO>> getTimelines(
			@RequestHeader(value = "X-User-Id", required = false, defaultValue = "") String userId,
			@RequestParam(value = "latitude", required = false, defaultValue = "0") Double latitude,
			@RequestParam(value = "longitude", required = false, defaultValue = "0") Double longitude,
			@RequestParam(value = "radius", required = false, defaultValue = "5000") int radius,
			@RequestHeader(value = "client", required = false, defaultValue = "android") Device device,
			@PageableDefault(size = 10, page = 0) Pageable pageable) {
		if(userId.isEmpty() && device == Device.ios) {
			return new ResponseEntity<List<TimelineDTO>>(new ArrayList<>(),HttpStatus.OK);
		}
		return new ResponseEntity<List<TimelineDTO>>(
				// timelineService.getTimelines(userId, latitude, longitude, radius, pageable)
				timelineService.getTimelines(userId, pageable), HttpStatus.OK);
	}

	/**
	 * Get timelines by userId.
	 * 
	 * @param userId
	 * @param clientId
	 * @param pageable
	 * @return List of TimelineDTO
	 */
	@RequestMapping(value = "/timeline/{user-id}", method = RequestMethod.GET)
	public ResponseEntity<List<TimelineDTO>> getTimelinesByUserId(
			@RequestHeader(value = "X-User-Id", required = true) String userId,
			@PathVariable(value = "user-id", required = true) String clientId,
			@PageableDefault(size = 10, page = 0) Pageable pageable) {
		return new ResponseEntity<List<TimelineDTO>>(timelineService.getTimelinesByUserId(clientId, pageable),
				HttpStatus.OK);
	}

	/**
	 * Get my timeline.
	 * 
	 * @param userId
	 * @param latitude
	 * @param longitude
	 * @param radius
	 * 
	 * @param pageable
	 * @return List of TimelineDTO
	 */
	@RequestMapping(value = "/timeline/me", method = RequestMethod.GET)
	public ResponseEntity<List<TimelineDTO>> getMyTimelines(
			@RequestHeader(value = "X-User-Id", required = true) String userId,
			@RequestParam(value = "latitude", required = false, defaultValue = "0") Double latitude,
			@RequestParam(value = "longitude", required = false, defaultValue = "0") Double longitude,
			@PageableDefault(size = 10, page = 0) Pageable pageable) {
		return new ResponseEntity<List<TimelineDTO>>(
				// timelineService.getMyTimelines(userId, latitude, longitude, pageable),
				// HttpStatus.OK);
				timelineService.getMyTimelinesFIXTemporary(userId, latitude, longitude, pageable), HttpStatus.OK);
	}

	/**
	 * Post a comment.
	 * 
	 * @param userId
	 * @param timelineCommentDTO
	 * @return HttpStatus
	 */
	@RequestMapping(value = "/timeline/comment", method = RequestMethod.POST)
	public ResponseEntity<EmptyJsonResponse> postComment(@RequestHeader(value = "X-User-Id") String userId,
			@RequestBody(required = true) TimelineCommentDTO timelineCommentDTO) {
		timelineCommentService.postComment(userId, timelineCommentDTO);
		return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.CREATED);
	}

	/**
	 * Post user action LIKE,DISLIKE.
	 * 
	 * @param userId
	 * @param timelineId
	 * @param timelineActionDTO
	 * @return HttpStatus
	 */
	@RequestMapping(value = "/timeline/action", method = RequestMethod.POST)
	public ResponseEntity<EmptyJsonResponse> postAction(@RequestHeader(value = "X-User-Id") String userId,
			@RequestBody(required = true) TimelineActionDTO timelineActionDTO) {
		timelineActionService.postAction(userId, timelineActionDTO);
		return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.CREATED);
	}

	/**
	 * Get Timeline Comments.
	 * 
	 * @param timelineId
	 * @param pageable
	 * @return List of TimelineCommentDTO
	 */
	@RequestMapping(value = "/timeline/{timeline-id}/comments", method = RequestMethod.GET)
	public ResponseEntity<List<TimelineCommentDTO>> getTimelineComments(
			@RequestHeader(value = "X-User-Id", required = false) String userId,
			@PathVariable(value = "timeline-id") Long timelineId,
			@PageableDefault(size = 20, page = 0, sort = "createdAt", direction = Direction.ASC) Pageable pageable) {
		return new ResponseEntity<List<TimelineCommentDTO>>(
				timelineCommentService.getTimelineComments(timelineId, pageable), HttpStatus.OK);
	}

	/**
	 * Populate short address.
	 *
	 * @param latitude
	 * @param longitude
	 * @return FullAddressDTO
	 */
	@RequestMapping(value = "/timeline/address", method = RequestMethod.GET)
	public ResponseEntity<ProvinceDTO> getFullAddressFromGoogle(@RequestHeader(value = "X-User-Id") String userId,
			@RequestParam(value = "latitude", required = true) Double latitude,
			@RequestParam(value = "longitude", required = true) Double longitude) {
		String latlng = String.valueOf(latitude) + "," + String.valueOf(longitude);
		ProvinceDTO province = mobileAdminService.getProvince(latlng);
		return new ResponseEntity<ProvinceDTO>(province, HttpStatus.OK);
	}

	/**
	 * Update
	 * 
	 * @param userId
	 * @param timelineId
	 * @param dto
	 * @return EmptyJsonResponse
	 */
	@RequestMapping(value = "/timeline/{id}", method = RequestMethod.PUT)
	public ResponseEntity<EmptyJsonResponse> updateTimeline(
			@RequestHeader(value = "X-User-Id", required = true) String userId,
			@PathVariable(value = "id", required = true) Long timelineId,
			@RequestBody(required = true) TimelineDTO dto) {
		timelineService.update(timelineId, dto);
		return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.OK);
	}

	/**
	 * Update Moment Radius
	 * 
	 * @param timelineId
	 * @param dto
	 * @return EmptyJsonResponse
	 */
	@RequestMapping(value = "/timeline/{id}/lovehide-radius", method = RequestMethod.PUT)
	public ResponseEntity<EmptyJsonResponse> updateRadius(
			@RequestHeader(value = "X-User-Id", required = true) String userId,
			@PathVariable(value = "id", required = true) Long timelineId,
			@RequestBody(required = true) TimelineDTO dto) {
		timelineService.updateLoveHideRadius(timelineId, dto);
		return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.OK);
	}

	/**
	 * Hide/Show timeline.
	 * 
	 * @param timelineId
	 * @param dto
	 * @return EmptyJsonResponse
	 */
	@RequestMapping(value = "/timeline/{id}/show-hide", method = RequestMethod.PUT)
	public ResponseEntity<EmptyJsonResponse> hideTimeline(
			@RequestHeader(value = "X-User-Id", required = true) String userId,
			@PathVariable(value = "id", required = true) Long timelineId,
			@RequestBody(required = true) TimelineDTO dto) {
		timelineService.updateIsPrivatedTimeline(timelineId, dto);
		return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.OK);
	}

	/**
	 * Share to facebook.
	 *
	 * @param timelineId
	 * @return HTML
	 */
	@RequestMapping(value = "/timeline/{timeline-id}/facebook", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public ResponseEntity<String> facebookShare(@PathVariable(value = "timeline-id", required = true) Long timelineId) {
		String html = timelineService.getShareTimeline(timelineId);
		return new ResponseEntity<String>(html, HttpStatus.OK);
	}

	/**
	 * Report abuse.
	 * 
	 * @param userId
	 * @param dto
	 * @return EmptyJsonResponse
	 */
	@PutMapping(value = "/timeline/report-abuse")
	public ResponseEntity<EmptyJsonResponse> reportAbuse(
			@RequestHeader(value = "X-User-Id", required = true) String userId,
			@RequestBody(required = true) TimelineReportAbuseDTO dto) {
		timelineReportAbuseService.reportAbuse(userId, dto);
		return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.OK);
	}

	/**
	 * Lock timeline user.
	 * 
	 * @param userId
	 * @param dto
	 * @return EmptyJsonResponse
	 */
	@PostMapping(value = "/timeline/hide")
	public ResponseEntity<EmptyJsonResponse> lockTimeline(
			@RequestHeader(value = "X-User-Id", required = true) String userId,
			@RequestBody(required = true) TimelineReportAbuseDTO dto) {
		// timelineReportAbuseService.hideTimeline(userId, id);
		timelineReportAbuseService.lockTimeline(userId, dto);
		return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.CREATED);
	}

}
