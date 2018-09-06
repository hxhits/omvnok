package vn.com.omart.messenger.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import vn.com.omart.messenger.common.constant.Device;
import vn.com.omart.messenger.application.response.VideoRoomDTO;
import vn.com.omart.messenger.domain.model.EmptyJsonResponse;
import vn.com.omart.messenger.service.VideoCallService;

/**
 * Video Call Controller.
 * 
 * @author Win10
 *
 */
@RestController
@RequestMapping("v1/messenger")
public class VideoCallController {

	@Autowired
	private VideoCallService videoCallService;

	/**
	 * Setup a video call.
	 * 
	 * @param poiId
	 * @param userIdSender
	 * @param userIdRecipient
	 * @return VideoRoomDTO
	 */
	@RequestMapping(value = "/video/{recipient}/call", method = RequestMethod.GET)
	public ResponseEntity<VideoRoomDTO> getRoom(@RequestParam(value = "poiId", required = true) Long poiId,
			@RequestHeader(value = "X-User-Id", required = true) String userIdSender,
			@RequestHeader(value = "client", required = false, defaultValue = "mobile") Device device,
			@PathVariable(value = "recipient") String userIdRecipient) {
		VideoRoomDTO room = videoCallService.setupVideoCall(userIdSender, userIdRecipient, poiId, device);
		return new ResponseEntity<VideoRoomDTO>(room, HttpStatus.OK);
	}

	/**
	 * Complete a room.
	 * 
	 * @param roomId
	 * @return EmptyJsonResponse
	 */
	@Deprecated
	@RequestMapping(value = "/video/{room-id}/completed", method = RequestMethod.POST)
	public ResponseEntity<EmptyJsonResponse> endVideCall(
			@PathVariable(value = "room-id", required = true) Long roomId) {
		videoCallService.videoCompleted(roomId);
		return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.OK);
	}

	/**
	 * Accept a video call.
	 * 
	 * @param userId
	 * @param roomId
	 * @return VideoRoomDTO
	 */
	@RequestMapping(value = "/video/{room-id}", method = RequestMethod.GET)
	public ResponseEntity<VideoRoomDTO> getVideoRoom(@RequestHeader(value = "X-User-Id", required = true) String userId,
			@PathVariable(value = "room-id", required = true) Long roomId) {
		return new ResponseEntity<VideoRoomDTO>(videoCallService.getVideoRoom(roomId), HttpStatus.OK);
	}
	
	/**
	 * Update status.
	 * @param roomId
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/video/{room-id}/status", method = RequestMethod.PUT)
	public ResponseEntity<EmptyJsonResponse> updateStatus(@RequestHeader(value = "X-User-Id", required = true) String userId,
			@PathVariable(value = "room-id", required = true) Long roomId,
			@RequestBody(required=true) VideoRoomDTO dto) {
		videoCallService.updateVideoCallStatus(dto, roomId);
		return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.OK);
	}
	
	/**
	 * Get status.
	 * @param roomId
	 * @return
	 */
	@RequestMapping(value = "/video/{room-id}/status", method = RequestMethod.GET)
	public ResponseEntity<VideoRoomDTO> updateStatus(@RequestHeader(value = "X-User-Id", required = true) String userId,
			@PathVariable(value = "room-id", required = true) Long roomId) {
		VideoRoomDTO dto = videoCallService.getVideoCallStatus(roomId);
		return new ResponseEntity<VideoRoomDTO>(dto, HttpStatus.OK);
	}
	

}
