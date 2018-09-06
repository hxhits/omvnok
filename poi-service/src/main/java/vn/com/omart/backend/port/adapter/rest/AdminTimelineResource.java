package vn.com.omart.backend.port.adapter.rest;

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

import vn.com.omart.backend.application.MobileAdminService;
import vn.com.omart.backend.application.TimelineActionService;
import vn.com.omart.backend.application.TimelineCommentService;
import vn.com.omart.backend.application.TimelineService;
import vn.com.omart.backend.application.response.TimelineDTO;
import vn.com.omart.backend.domain.model.EmptyJsonResponse;
import vn.com.omart.sharedkernel.application.model.dto.PageDTO;

@RestController
@RequestMapping("/v1/admin")
public class AdminTimelineResource {
	@Autowired
	private MobileAdminService mobileAdminService;

	@Autowired
	private TimelineActionService timelineActionService;

	@Autowired
	private TimelineService timelineService;

	@Autowired
	private TimelineCommentService timelineCommentService;

	@RequestMapping(value = "/timeline", method = RequestMethod.GET)
	public ResponseEntity<PageDTO<TimelineDTO>> getAll(@PageableDefault(size = 50, page = 0) Pageable pageable,
			@RequestHeader(value = "X-User-Id", required = true) String userId) {
		PageDTO<TimelineDTO> dtos = timelineService.getAll(pageable);
		return new ResponseEntity<PageDTO<TimelineDTO>>(dtos, HttpStatus.OK);
	}

	/**
	 * Update Is Deleted timeline.
	 * 
	 * @param timelineId
	 * @param dto
	 * @return EmptyJsonResponse
	 */
	@RequestMapping(value = "/timeline/{id}", method = RequestMethod.PUT)
	public ResponseEntity<EmptyJsonResponse> updateTimeline(@RequestHeader(value = "X-User-Id", required = true) String userId,
			@PathVariable(value = "id", required = true) Long timelineId,
			@RequestBody(required = true) TimelineDTO dto) {
		timelineService.updatIsDeletedTimeline(timelineId, dto);
		return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.OK);
	}

}
