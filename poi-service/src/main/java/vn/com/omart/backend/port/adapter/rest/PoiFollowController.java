package vn.com.omart.backend.port.adapter.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import vn.com.omart.backend.application.PoiFollowService;
import vn.com.omart.backend.application.response.PoiFollowDTO;
import vn.com.omart.backend.domain.model.EmptyJsonResponse;

@RestController
@RequestMapping("/v1/poi-follow")
public class PoiFollowController {

	@Autowired
	private PoiFollowService poiFollowService;

	@GetMapping(value="")
	public ResponseEntity<List<PoiFollowDTO>> getFollowsByUserId (
			@RequestHeader(value = "X-User-Id",required=true) String userId,
			@PageableDefault(page = 0, size = Integer.MAX_VALUE) Pageable pageable) {
		List<PoiFollowDTO> dto = poiFollowService.getByUserId(userId, pageable);
		if (!dto.isEmpty()) {
			return new ResponseEntity<List<PoiFollowDTO>>(dto, HttpStatus.OK);
		}
		return new ResponseEntity<List<PoiFollowDTO>>(HttpStatus.NO_CONTENT);
	}
	
	@PostMapping(value="")
	public ResponseEntity<EmptyJsonResponse> save(@RequestHeader(value = "X-User-Id",required=true) String userId,
			@RequestBody(required=true) PoiFollowDTO dto) {
		poiFollowService.save(userId, dto.getPoiId());
		return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(),HttpStatus.CREATED);
	}
}
