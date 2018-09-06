package vn.com.omart.backend.port.adapter.rest;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.com.omart.backend.application.RecruitmentService;
import vn.com.omart.backend.application.response.RecruitmentDTO;
import vn.com.omart.backend.domain.model.EmptyJsonResponse;

@RestController
@RequestMapping("/v1")
public class RecruitmentResource {

	@Autowired
	private RecruitmentService recruitmentService;

	/**
	 * Save.
	 * 
	 * @param userId
	 * @param recruitmentDTO
	 * @return
	 */
	@RequestMapping(value = "/recruit", method = RequestMethod.POST)
	public ResponseEntity<EmptyJsonResponse> save(@RequestHeader(value = "X-User-Id") String userId,
			@Valid @RequestBody(required = true) RecruitmentDTO recruitmentDTO) {
		recruitmentService.save(recruitmentDTO, userId, 0);
		return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.CREATED);
	}

	/**
	 * Save V1.
	 * 
	 * @param userId
	 * @param recruitmentDTO
	 * @return
	 */
	@RequestMapping(value = "/recruit", method = RequestMethod.POST, headers = { "X-API-Version=1.1" })
	public ResponseEntity<EmptyJsonResponse> saveV11(@RequestHeader(value = "X-User-Id") String userId,
			@Valid @RequestBody(required = true) RecruitmentDTO recruitmentDTO) {
		recruitmentService.save(recruitmentDTO, userId, 1);
		return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.CREATED);
	}

	/**
	 * Update
	 * 
	 * @param userId
	 * @param id
	 * @param recruitmentDTO
	 * @return
	 */
	@RequestMapping(value = "/recruit/{id}", method = RequestMethod.PUT)
	public ResponseEntity<EmptyJsonResponse> update(@RequestHeader(value = "X-User-Id") String userId,
			@PathVariable(value = "id", required = true) Long id,
			@RequestBody(required = true) RecruitmentDTO recruitmentDTO) {
		recruitmentService.update(id, recruitmentDTO, 0);
		return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.OK);
	}

	/**
	 * Update V1.
	 * 
	 * @param userId
	 * @param id
	 * @param recruitmentDTO
	 * @return
	 */
	@RequestMapping(value = "/recruit/{id}", method = RequestMethod.PUT, headers = { "X-API-Version=1.1" })
	public ResponseEntity<EmptyJsonResponse> updateV1(@RequestHeader(value = "X-User-Id") String userId,
			@PathVariable(value = "id", required = true) Long id,
			@RequestBody(required = true) RecruitmentDTO recruitmentDTO) {
		recruitmentService.update(id, recruitmentDTO, 1);
		return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.OK);
	}

	/**
	 * Get recruit by poi.
	 * 
	 * @param poiId
	 * @return
	 */
	@RequestMapping(value = "/recruit/{poi-id}/poiId", method = RequestMethod.GET)
	public ResponseEntity<List<RecruitmentDTO>> getRecruitByPoi(
			@RequestHeader(value = "X-User-Id", required = false) String userId,
			@PathVariable(value = "poi-id", required = true) Long poiId) {
		List<RecruitmentDTO> dtos = recruitmentService.getRecruitByPoi(userId, poiId);
		return new ResponseEntity<List<RecruitmentDTO>>(dtos, HttpStatus.OK);
	}

	/**
	 * Get recruit by Id.
	 * 
	 * @param id
	 * @return RecruitmentDTO
	 */
	@RequestMapping(value = "/recruit/{id}", method = RequestMethod.GET)
	public ResponseEntity<RecruitmentDTO> getRecruitById(@PathVariable(value = "id", required = true) Long id) {
		RecruitmentDTO dto = recruitmentService.getRecruitById(id);
		return new ResponseEntity<RecruitmentDTO>(dto, HttpStatus.OK);
	}

	/**
	 * Delete
	 * 
	 * @param userId
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/recruit/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<EmptyJsonResponse> delete(@RequestHeader(value = "X-User-Id") String userId,
			@PathVariable(value = "id", required = true) Long id) {
		recruitmentService.delete(id);
		return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.OK);
	}

	/**
	 * View Count.
	 * 
	 * @param userId
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/recruit/{id}/view", method = RequestMethod.PUT)
	public ResponseEntity<EmptyJsonResponse> viewCount(@RequestHeader(value = "X-User-Id") String userId,
			@PathVariable(value = "id", required = true) Long id) {
		recruitmentService.viewCount(id);
		return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.OK);
	}

	/**
	 * Re-Push Recruitment.
	 * 
	 * @param userId
	 * @param id
	 * @return EmptyJsonResponse
	 */
	@RequestMapping(value = "/recruit/{recruit-id}/repush", method = RequestMethod.GET)
	public ResponseEntity<EmptyJsonResponse> rePushNotification(
			@RequestHeader(value = "X-User-Id", required = true) String userId,
			@PathVariable(value = "recruit-id", required = true) Long id) {
		recruitmentService.rePushRecruitment(userId, id);
		return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.OK);
	}

	/**
	 * Share facebook.
	 * 
	 * @param recruitId
	 * @return html
	 */
	@RequestMapping(value = "/recruit/{recruit-id}/facebook", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public ResponseEntity<String> facebookShare(@PathVariable(value = "recruit-id", required = true) Long recruitId) {
		String html = recruitmentService.shareFacebook(recruitId);
		return new ResponseEntity<String>(html, HttpStatus.OK);
	}

	/**
	 * Get all filter is deleted.
	 * 
	 * @param pageable
	 * @return List of RecruitmentDTO
	 */
	@GetMapping(value = "/recruit")
	public ResponseEntity<Page<RecruitmentDTO>> getAll(@PageableDefault(page = 0, size = 20) Pageable pageable) {
		return recruitmentService.getAllByFilterIsDeleted(pageable);
	}

}
