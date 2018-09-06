package vn.com.omart.backend.application.response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.com.omart.backend.application.ReportAbuseService;
import vn.com.omart.backend.domain.model.EmptyJsonResponse;

@RestController
@RequestMapping("/v1/report-abuse")
public class ReportAbuseResource {

	@Autowired
	private ReportAbuseService reportAbuseService;

	@PostMapping(value = "")
	public ResponseEntity<EmptyJsonResponse> save(@RequestHeader(value = "X-User-Id", required = true) String userId,
			@RequestBody(required = true) ReportAbuseDTO dto) {
		reportAbuseService.save(userId, dto);
		return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.CREATED);
	}
	
}
