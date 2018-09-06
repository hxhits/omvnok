package vn.com.omart.backend.port.adapter.rest;

import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.com.omart.backend.application.RecruitmentPositionFollowService;
import vn.com.omart.backend.application.RecruitmentPositionService;
import vn.com.omart.backend.application.response.RecruitmentApplyDTO;
import vn.com.omart.backend.application.response.RecruitmentPositionDTO;
import vn.com.omart.backend.application.response.RecruitmentPositionFollowDTO;
import vn.com.omart.backend.domain.model.EmptyJsonResponse;

@RestController
@RequestMapping("/v1")
public class RecruitmentPositionResource {

	@Autowired
	private RecruitmentPositionService recPosService;
	
	@Autowired
	private RecruitmentPositionFollowService recruitmentPositionFollowService;

	/**
	 * Get All Recruit Position.
	 * 
	 * @param userId
	 * @return List of RecruitmentPositionDTO
	 */
	@RequestMapping(value = "/recruit/position", method = RequestMethod.GET)
	public ResponseEntity<List<RecruitmentPositionDTO>> getAllRecruitPosition(
			@RequestHeader(value = "X-User-Id", required = false) String userId) {
		List<RecruitmentPositionDTO> dtos = recPosService.getAllRecruitPosition(userId);
		return new ResponseEntity<List<RecruitmentPositionDTO>>(dtos, HttpStatus.OK);
	}

	/**
	 * Save
	 * 
	 * @param userId
	 * @param RecruitmentApplyDTO
	 * @return
	 */
	@RequestMapping(value = "/recruit/apply", method = RequestMethod.POST)
	public ResponseEntity<EmptyJsonResponse> save(@RequestHeader(value = "X-User-Id", required = true) String userId,
			@RequestBody(required = true) RecruitmentApplyDTO recruitmentDTO) {
		recPosService.save(recruitmentDTO, userId);
		return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.CREATED);
	}

	/**
	 * Get All Recruit Apply.
	 * 
	 * @param userId
	 * @return List of RecruitmentApplyDTO
	 */
	@RequestMapping(value = "/recruit/apply", method = RequestMethod.GET)
	public ResponseEntity<List<RecruitmentApplyDTO>> getAllRecruitApply(
			@RequestHeader(value = "X-User-Id", required = true) String userId) {
		List<RecruitmentApplyDTO> dtos = recPosService.getAllRecruitApply(userId);
		return new ResponseEntity<List<RecruitmentApplyDTO>>(dtos, HttpStatus.OK);
	}

	/**
	 * Get RecruitApply by Poi.
	 * 
	 * @param userId
	 * @param poiId
	 * @return List of RecruitmentApplyDTO
	 */
	@RequestMapping(value = "/recruit/apply/{poi-id}", method = RequestMethod.GET)
	public ResponseEntity<List<RecruitmentApplyDTO>> getRecruitApplyByPoi(
			@RequestHeader(value = "X-User-Id", required = true) String userId,
			@PathVariable(value = "poi-id", required = true) Long poiId) {
		List<RecruitmentApplyDTO> dtos = recPosService.getRecruitApplyByPoi(userId, poiId);
		return new ResponseEntity<List<RecruitmentApplyDTO>>(dtos, HttpStatus.OK);
	}

	/**
	 * Get RecruitApply of User.
	 * 
	 * @param userId
	 * @return List of RecruitmentApplyDTO
	 */
	@RequestMapping(value = "/recruit/apply/user", method = RequestMethod.GET)
	public ResponseEntity<List<RecruitmentApplyDTO>> getRecruitApplyOfUser(
			@RequestHeader(value = "X-User-Id", required = true) String userId) {
		List<RecruitmentApplyDTO> dtos = recPosService.getRecruitApplyOfUser(userId);
		return new ResponseEntity<List<RecruitmentApplyDTO>>(dtos, HttpStatus.OK);
	}

	/**
	 * Delete
	 * 
	 * @param userId
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/recruit/apply/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<EmptyJsonResponse> delete(@RequestHeader(value = "X-User-Id") String userId,
			@PathVariable(value = "id", required = true) Long id) {
		recPosService.deleteApply(id);
		return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.OK);
	}

	/**
	 * Update Status
	 * 
	 * @param userId
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/recruit/apply/{id}/status", method = RequestMethod.PUT)
	public ResponseEntity<EmptyJsonResponse> updateStatus(
			@RequestHeader(value = "X-User-Id") String userId,
			@PathVariable(value = "id", required = true) Long id,
			@RequestBody(required = true) RecruitmentApplyDTO dto) {
		recPosService.updateStatus(id, dto);
		return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.OK);
	}

	/**
	 * Update Status
	 * 
	 * @param userId
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/recruit/apply/status", method = RequestMethod.PUT)
	public ResponseEntity<EmptyJsonResponse> updateBatchStatus(
			@RequestHeader(value = "X-User-Id") String userId,
			@RequestBody(required = true) List<RecruitmentApplyDTO> dtos) {
		recPosService.updateStatus(dtos);
		return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.OK);
	}

	/**
	 * Save Positions Follow.
	 * 
	 * @param dto
	 * @param userId
	 * @return EmptyJsonResponse
	 */
	@RequestMapping(value = "/recruit/follow", method = RequestMethod.POST)
	public ResponseEntity<EmptyJsonResponse> postPositionsFollow(
			@RequestBody(required = true) RecruitmentPositionFollowDTO dto,
			@RequestHeader(value = "X-User-Id", required = true) String userId) {
		recruitmentPositionFollowService.save(userId, dto);
		return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.CREATED);
	}

	/**
	 * Get Positions Follow.
	 * 
	 * @param dto
	 * @param userId
	 * @return EmptyJsonResponse
	 */
	@RequestMapping(value = "/recruit/follow", method = RequestMethod.GET)
	public ResponseEntity<RecruitmentPositionFollowDTO> getPositionsFollow(
			@RequestHeader(value = "X-User-Id", required = true) String userId) {
		RecruitmentPositionFollowDTO dto = recruitmentPositionFollowService.getPositionFollowDTOByUserId(userId);
		return new ResponseEntity<RecruitmentPositionFollowDTO>(dto, HttpStatus.CREATED);
	}

}
