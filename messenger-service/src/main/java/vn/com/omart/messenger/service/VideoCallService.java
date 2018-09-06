package vn.com.omart.messenger.service;

import vn.com.omart.messenger.application.response.VideoRoomDTO;
import vn.com.omart.messenger.common.constant.Device;

/**
 * Video Call Service Interface.
 * 
 * @author Win10
 *
 */

public interface VideoCallService {

	/**
	 * Setup a video call.
	 * 
	 * @param userIdSender
	 * @param userIdRecipient
	 * @param poiId
	 * @return VideoRoomDTO
	 */
	public VideoRoomDTO setupVideoCall(String userIdSender, String userIdRecipient, Long poiId, Device device);

	/**
	 * Complete room.
	 * 
	 * @param roomId
	 */
	public void videoCompleted(Long roomId);

	/**
	 * Get room info from database.
	 * 
	 * @param roomId
	 * @return VideoRoomDTO
	 */
	public VideoRoomDTO getVideoRoom(Long roomId);
	
	/**
	 * Update status.
	 * @param dto
	 * @param roomId
	 */
	public void updateVideoCallStatus(VideoRoomDTO dto, Long roomId) ;
	
	/**
	 * Get status.
	 * @param roomId
	 * @return Status
	 */
	public VideoRoomDTO getVideoCallStatus(Long roomId);

}
