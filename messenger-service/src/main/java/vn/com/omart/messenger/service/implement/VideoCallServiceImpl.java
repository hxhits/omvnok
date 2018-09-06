package vn.com.omart.messenger.service.implement;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.twilio.rest.video.v1.Room;

import vn.com.omart.messenger.application.request.DeviceGroupToken;
import vn.com.omart.messenger.application.response.UserDTO;
import vn.com.omart.messenger.application.response.VideoRoomDTO;
import vn.com.omart.messenger.common.constant.Device;
import vn.com.omart.messenger.common.constant.Messenger;
import vn.com.omart.messenger.common.constant.MessengerResponse;
import vn.com.omart.messenger.common.constant.VideoCallStatus;
import vn.com.omart.messenger.domain.model.CallStatusRepository;
import vn.com.omart.messenger.domain.model.MessengerHistoryRepository;
import vn.com.omart.messenger.domain.model.PushNotificationToken;
import vn.com.omart.messenger.domain.model.PushNotificationTokenRepository;
import vn.com.omart.messenger.domain.model.VideoRoom;
import vn.com.omart.messenger.domain.model.VideoRoomRepository;
import vn.com.omart.messenger.exception.MessengerException;
import vn.com.omart.messenger.service.PushNotificationTokenNewService;
import vn.com.omart.messenger.service.VideoCallService;

@Service
public class VideoCallServiceImpl implements VideoCallService {

	@Autowired
	private FcmClientService fcmClientService;

	@Autowired
	private TwilioService twilioService;

	@Autowired
	private VideoRoomRepository videoRoomRepository;

	@Autowired
	private PushNotificationTokenRepository pushNotificationTokenRepository;

	@Autowired
	private MessengerHistoryRepository messengerHistoryRepository;

	@Value("${messenger.notification.videocall.title}")
	private String Notification_Title;

	@Autowired
	private CallStatusRepository callStatusRepository;
	
	@Autowired
	private PushNotificationTokenNewService pushNotificationTokenNewService;
	
	/**
	 * Get user info.
	 * 
	 * @param userId
	 * @return UserDTO
	 */
	public UserDTO getUserById(String userId) {
		Object[] userObj = (Object[]) messengerHistoryRepository.getUserById(userId);
		if (userObj == null) {
			throw new MessengerException(MessengerResponse.USER_NOT_FOUND, userId);
		}
		UserDTO user = UserDTO.toDTO(userObj);
		return user;
	}

	/**
	 * Setup a video call.
	 */
	@Override
	public VideoRoomDTO setupVideoCall(String userIdSender, String userIdRecipient, Long poiId, Device device) {
		// TODO Auto-generated method stub
		VideoRoom videoRoom = saveRoom(userIdSender, userIdRecipient, poiId, device);
		// retrieve user sender info to get username send to recipient notification.
		UserDTO userSender = getUserById(userIdSender);
		String body = "Video Call";
		if (userSender != null) {
			body = (userSender.getFirstname().isEmpty() ? body : userSender.getFirstname());
		}
		VideoRoomDTO room = new VideoRoomDTO();
		
//		PushNotificationToken pushNotif = pushNotificationTokenRepository.findByUserId(userIdRecipient);
//		if (pushNotif != null) {
//			// put notification
//			Map<String, String> data = new HashMap<String, String>();
//			data.put("type", "1");
//			data.put("roomId", String.valueOf(videoRoom.getId()));
//			data.put("senderName", userSender.getFirstname());
//			// send
//			int client = pushNotif.getClient();
//			if (client == Device.android.getId()) {
//				fcmClientService.pushNotificationWithoutPayload(Notification_Title, StringUtils.abbreviate(body, 200),
//						pushNotif.token(), data);
//			} else if (client == Device.ios.getId()) {
//				fcmClientService.pushNotification(Notification_Title, StringUtils.abbreviate(body, 200),
//						pushNotif.token(), data, Messenger.VIDEO_CALL_SOUND);
//			} else if (client == Device.desktop.getId()) {
//				fcmClientService.pushNotificationWithoutPayload(Notification_Title, StringUtils.abbreviate(body, 200),
//						pushNotif.token(), data);
//			}
//		}
		
		Map<String, String> data = new HashMap<String, String>();
		data.put("type", "1");
		data.put("roomId", String.valueOf(videoRoom.getId()));
		data.put("senderName", userSender.getFirstname());
		DeviceGroupToken deviceGroupToken = pushNotificationTokenNewService.getDeviceGroupToken(userIdRecipient);
		
		if(!deviceGroupToken.getAndroidTokens().isEmpty()) {
			fcmClientService.pushNotificationWithPayload(Notification_Title,body,deviceGroupToken.getAndroidTokens(),data);
		}
		if(!deviceGroupToken.getIosTokens().isEmpty()) {
			fcmClientService.pushNotification(Notification_Title,body,deviceGroupToken.getIosTokens(),Messenger.VIDEO_CALL_SOUND,data);
		}
		if(!deviceGroupToken.getDesktopTokens().isEmpty()) {
			fcmClientService.pushNotificationWithPayload(Notification_Title,body,deviceGroupToken.getDesktopTokens(),data);
		}
		if(!deviceGroupToken.getWebTokens().isEmpty()) {
			fcmClientService.pushNotificationWithoutPayload(Notification_Title,body,"",deviceGroupToken.getWebTokens(),data);
		}
		
		// retrieve user recipient info to response to sender.
		UserDTO userRecipient = getUserById(userIdRecipient);

		room.setName(videoRoom.getName());
		room.setSenderToken(videoRoom.getSenderToken());
		room.setId(videoRoom.getId());
		room.setAvatar(userRecipient.getAvatar());
		room.setUserName(userRecipient.getFirstname());
		return room;
	}

	/**
	 * Complete room.
	 */
	@Override
	public void videoCompleted(Long roomId) {
		// TODO Auto-generated method stub
		VideoRoom videoRoom = videoRoomRepository.findById(roomId);
		if (videoRoom == null) {
			throw new MessengerException(MessengerResponse.ROOM_NOT_FOUND, roomId);
		}
		twilioService.videoCompleted(videoRoom.getRoomSid());
	}

	/**
	 * Save room info.
	 * 
	 * @param caller
	 * @param listener
	 * @param poiId
	 * @return VideoRoom
	 */
	@Transactional
	public VideoRoom saveRoom(String caller, String listener, Long poiId, Device device) {
		// Init new Room
		VideoRoom videoRoom = new VideoRoom();
		// connect to twilio service.
		Room room = twilioService.roomGenerator();
		String roomName = room.getUniqueName();
		String senderToken = twilioService.videoTokenGenerator(roomName, caller);
		String recipientToken = twilioService.videoTokenGenerator(roomName, listener);
		// store room video call.
		videoRoom.setName(roomName);
		videoRoom.setCreatedAt(new Date());
		videoRoom.setSenderId(caller);
		videoRoom.setRecipientId(listener);
		videoRoom.setSenderToken(senderToken);
		videoRoom.setRecipientToken(recipientToken);
		videoRoom.setRoomSid(room.getSid());
		videoRoom.setPoiId(poiId);
		return videoRoomRepository.save(videoRoom);
	}

	/**
	 * Get room info from database.
	 */
	@Override
	public VideoRoomDTO getVideoRoom(Long roomId) {
		// TODO Auto-generated method stub
		VideoRoom room = videoRoomRepository.findById(roomId);
		if (room == null) {
			throw new MessengerException(MessengerResponse.ROOM_NOT_FOUND, roomId);
		}
		UserDTO user = getUserById(room.getSenderId());

		VideoRoomDTO roomDTO = new VideoRoomDTO();
		roomDTO.setId(room.getId());
		roomDTO.setName(room.getName());
		roomDTO.setRecipientToken(room.getRecipientToken());
		roomDTO.setUserName(user.getFirstname());
		roomDTO.setAvatar(user.getAvatar());
		return roomDTO;
	}

	/**
	 * Update status.
	 */
	@Override
	public void updateVideoCallStatus(VideoRoomDTO dto, Long roomId) {
		// TODO Auto-generated method stub
		VideoRoom entity = videoRoomRepository.findOne(roomId);
		if(entity!=null) {
			entity.setStatus(dto.getStatus().getId());
			videoRoomRepository.save(entity);
		}
	}

	/**
	 * Get status.
	 */
	@Override
	public VideoRoomDTO getVideoCallStatus(Long roomId) {
		// TODO Auto-generated method stub
		VideoRoom entity  = videoRoomRepository.findOne(roomId);
		if(entity!=null) {
			VideoRoomDTO dto = new VideoRoomDTO();
			dto.setStatus(VideoCallStatus.getById(entity.getStatus()));
			return dto;
		}
		return null;
	}

}
