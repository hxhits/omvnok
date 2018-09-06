package vn.com.omart.messenger.service.implement;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import vn.com.omart.messenger.application.request.DeviceGroupToken;
import vn.com.omart.messenger.application.response.ChatDTO;
import vn.com.omart.messenger.application.response.MessageChannelDTO;
import vn.com.omart.messenger.application.response.MessageDTO;
import vn.com.omart.messenger.application.response.PoiDTO;
import vn.com.omart.messenger.application.response.UserDTO;
import vn.com.omart.messenger.application.response.WebHookDTO;
import vn.com.omart.messenger.common.constant.Device;
import vn.com.omart.messenger.common.constant.Messenger;
import vn.com.omart.messenger.common.constant.MessengerResponse;
import vn.com.omart.messenger.domain.model.MessageChannel;
import vn.com.omart.messenger.domain.model.MessageChannelRepository;
import vn.com.omart.messenger.domain.model.MessageType;
import vn.com.omart.messenger.domain.model.MessengerHistoryRepository;
import vn.com.omart.messenger.domain.model.PointOfInterest;
import vn.com.omart.messenger.domain.model.PointOfInterestRepository;
import vn.com.omart.messenger.domain.model.PushNotificationToken;
import vn.com.omart.messenger.domain.model.PushNotificationTokenNew;
import vn.com.omart.messenger.domain.model.PushNotificationTokenRepository;
import vn.com.omart.messenger.exception.MessengerException;
import vn.com.omart.messenger.service.MessageChannelService;
import vn.com.omart.messenger.service.PushNotificationTokenNewService;

/**
 * Implement Message Channel Service Interface.
 * 
 * @author Win10
 *
 */
@Service
public class MessageChannelServiceImpl implements MessageChannelService {

	private static final Logger logger = LoggerFactory.getLogger(MessageChannelServiceImpl.class);

	@Autowired
	private MessageChannelRepository messageChannelRepository;

	@Autowired
	private TwilioService twilioService;

	@Autowired
	private MessengerHistoryRepository messengerHistoryRepository;

	@Autowired
	private PointOfInterestRepository pointOfInterestRepository;

	@Value("${messenger.notification.chat.title}")
	private String Chat_Title;

	@Autowired
	private PushNotificationTokenRepository pushNotificationTokenRepository;

	@Autowired
	private FcmClientService fcmClientService;

	/**
	 * Generate token.
	 */
	@Override
	public ChatDTO chatTokenGenerator(String identity, Device device) {
		// TODO Auto-generated method stub
		String token = twilioService.chatTokenGenerator(identity, null, device);
		ChatDTO chat = new ChatDTO();
		chat.setToken(token);
		return chat;
	}

	/**
	 * Get user info.
	 * 
	 * @param userId
	 * @return UserDTO
	 */
	public UserDTO getUserById(String userId) {
		Object[] userObj = (Object[]) messengerHistoryRepository.getUserById(userId);
		if (userObj == null) {
			logger.error("User ID not found: " + userId);
			// throw new MessengerException(MessengerResponse.USER_NOT_FOUND, userId);
		}
		UserDTO user = UserDTO.toDTO(userObj);
		return user;
	}

	/**
	 * Checking user is a sender column or recipient column in databasse.
	 * 
	 * @param channel
	 * @param userId
	 * @return true if user is a sender user.
	 */
	public boolean isSenderUser(MessageChannel channel, String userId) {
		if (channel.getSenderId().equals(userId)) {
			return true;
		}
		return false;
	}

	/**
	 * Save Message Channel.
	 */
	@Transactional
	@Override
	public MessageChannelDTO save(MessageChannelDTO messageChannelDTO) {
		// TODO Auto-generated method stub
		MessageChannel channel = messageChannelRepository.findByChannelName(messageChannelDTO.getChannelName());
		if (channel == null) {
			MessageChannel messageChannel = MessageChannelDTO.toEntity(messageChannelDTO);
			channel = messageChannelRepository.saveAndFlush(messageChannel);
		} else {
			/*
			 * if sender(recipient) was deleted message then recipient(sender) send message
			 * to, the action is reset deleted to 0 to start chat
			 */
			channel.setSenderDeleted(0);
			channel.setRecipientDeleted(0);
			channel = messageChannelRepository.saveAndFlush(channel);
		}
		MessageChannelDTO entity = new MessageChannelDTO();
		if (isSenderUser(channel, messageChannelDTO.getSenderId())) {
			entity.setLastIndex(channel.getSenderLastIndex());
		} else {
			entity.setLastIndex(channel.getRecipientLastIndex());
		}
		return entity;
	}

	/**
	 * Get channel history.
	 */
//	@Transactional
//	@Override
//	public List<MessageChannelDTO> getChannelHistoryByUserId(String userId, String channelSid) {
//		List<MessageChannelDTO> result = new ArrayList<>();
//		// reset is deleted to 0.
//		if (!StringUtils.isEmpty(channelSid)) {
//			MessageChannel channel = messageChannelRepository.findByChannelSid(channelSid);
//			if (channel == null) {
//				throw new MessengerException(MessengerResponse.MESSAGE_CHANNEL_SID_NOT_FOUND, channelSid);
//			}
//			/*
//			 * if sender(recipient) was deleted message then recipient(sender) send message
//			 * to, the action is reset deleted to 0 to start chat
//			 */
//			channel.setSenderDeleted(0);
//			channel.setRecipientDeleted(0);
//			channel = messageChannelRepository.saveAndFlush(channel);
//		}
//		// filter channel list.
//		List<MessageChannel> messageChannels = messageChannelRepository
//				.findBySenderIdOrRecipientIdOrderByCreatedAtDesc(userId, userId, new PageRequest(0, 2000));
//
//		// filter only get poi Channel.
//		List<MessageChannel> messageOfPoiChannels = messageChannels.stream()
//				.filter(predicate -> predicate.getPoiId() != 0L).collect(Collectors.toList());
//
//		// filter only get Contact Channel.
//		List<MessageChannel> messageOfContactChannels = messageChannels.stream()
//				.filter(predicate -> predicate.getPoiId() == 0L).collect(Collectors.toList());
//
//		messageOfPoiChannels.stream().forEach(channel -> {
//			MessageChannelDTO channelDTO = new MessageChannelDTO();
//			if (channel.getRecipientId().equals(userId)) {
//				// side from shop owner.
//				int isDeleted = channel.getRecipientDeleted();
//				if (isDeleted == 0) {
//					String id = channel.getSenderId();
//					UserDTO userItem = getUserById(id);
//					PoiDTO poiDTO = this.getPointOfInterestById(channel.getPoiId());
//					channelDTO = MessageChannelDTO.toDTO(channel);
//					channelDTO.setRecipient(userItem);
//					channelDTO.setPoi(poiDTO);
//					result.add(channelDTO);
//				}
//			} else {
//				int isDeleted = channel.getSenderDeleted();
//				if (isDeleted == 0) {
//					PoiDTO poiDTO = this.getPointOfInterestById(channel.getPoiId());
//					channelDTO = MessageChannelDTO.toDTO(channel);
//					channelDTO.setPoi(poiDTO);
//					result.add(channelDTO);
//				}
//			}
//		});
//
//		// add user history of contact channel.
//		messageOfContactChannels.stream().forEach(channel -> {
//			MessageChannelDTO channelDTO = new MessageChannelDTO();
//			if (channel.getRecipientId().equals(userId)) {
//				int isDeleted = channel.getRecipientDeleted();
//				if (isDeleted == 0) {
//					String id = channel.getSenderId();
//					UserDTO userItem = getUserById(id);
//					channelDTO = MessageChannelDTO.toDTO(channel);
//					channelDTO.setRecipient(userItem);
//					result.add(channelDTO);
//				}
//			} else {
//				int isDeleted = channel.getSenderDeleted();
//				if (isDeleted == 0) {
//					String id = channel.getRecipientId();
//					UserDTO userItem = getUserById(id);
//					channelDTO = MessageChannelDTO.toDTO(channel);
//					channelDTO.setRecipient(userItem);
//					result.add(channelDTO);
//				}
//			}
//		});
//
//		List<MessageChannelDTO> resultSorted = result.stream()
//				.sorted(Comparator.comparing(MessageChannelDTO::getCreatedAt).reversed()).collect(Collectors.toList());
//		return resultSorted;
//	}

	/**
	 * Get PointOfInterest By Id.
	 * 
	 * @param poiId
	 * @return PoiDTO
	 */
	public PoiDTO getPointOfInterestById(Long poiId) {
		PointOfInterest poiEnity = pointOfInterestRepository.findOne(poiId);
		if (poiEnity == null) {
			throw new MessengerException(MessengerResponse.POI_NOT_FOUND, poiId);
		}
		PoiDTO poiDTO = PoiDTO.toDTO(poiEnity);
		return poiDTO;
	}
	
	@Autowired
	private PushNotificationTokenNewService pushNotificationTokenNewService;
	
	/**
	 * Send Notification.
	 */
	@Override
	public void sendNotification(WebHookDTO webhook) {
		try {
			Gson gson = new Gson();
			MessageDTO dto = gson.fromJson(webhook.getBody(), MessageDTO.class);
			// body
			String body = "";
			LinkedTreeMap payload = (LinkedTreeMap) dto.getPayload();
			if (payload.containsKey(MessageType.TEXT.value().toLowerCase())) {
				body = (String) payload.get(MessageType.TEXT.value().toLowerCase());
			} else if (payload.containsKey(MessageType.IMAGE.value().toLowerCase())) {
				body = "Hình ảnh";// TODO
			}
			// sender name.
			String senderName = dto.getSenderName() == null ? "" : dto.getSenderName();
			// recipient
			String recipientId = dto.getRecipientId();
			
			// update message channel createdAt to sort.
			MessageChannel messageChannel = messageChannelRepository.findByChannelSid(webhook.getChannelSid());
			if(messageChannel!=null) {
				messageChannel.setCreatedAt(new Date());
				messageChannelRepository.save(messageChannel);
			}
			
//			PushNotificationToken pushNotificationToken = pushNotificationTokenRepository.findByUserId(recipientId);
//			if (pushNotificationToken != null) {
//				// put notification
//				Map<String, String> data = new HashMap<String, String>();
//				data.put("type", "0");
//				data.put("payload", webhook.getBody());
//				// push notification for android.
//				int client = pushNotificationToken.getClient();
//				if (client == Device.android.getId()) {
//					fcmClientService.pushNotificationWithoutPayload(Chat_Title + " " + senderName, body,
//							pushNotificationToken.token(), data);
//				} else if (client == Device.ios.getId()) {
//					fcmClientService.pushNotification(Chat_Title + " " + senderName, body,
//							pushNotificationToken.token(), data, Messenger.SOUND_DEFAULT);
//				} else if (client == Device.desktop.getId()) {
//					fcmClientService.pushNotificationWithoutPayload(Chat_Title + " " + senderName, body,
//							pushNotificationToken.token(), data);
//				}
//			}
			
			Map<String, String> data = new HashMap<String, String>();
			data.put("type", "0");
			data.put("payload", webhook.getBody());
			String title = Chat_Title+" "+senderName;
			
			DeviceGroupToken deviceGroupToken = pushNotificationTokenNewService.getDeviceGroupToken(recipientId);
			
			if(!deviceGroupToken.getAndroidTokens().isEmpty()) {
				fcmClientService.pushNotificationWithPayload(title,body,deviceGroupToken.getAndroidTokens(),data);
			}
			if(!deviceGroupToken.getIosTokens().isEmpty()) {
				fcmClientService.pushNotification(title,body,deviceGroupToken.getIosTokens(),Messenger.SOUND_DEFAULT,data);
			}
			if(!deviceGroupToken.getDesktopTokens().isEmpty()) {
				fcmClientService.pushNotificationWithPayload(title,body,deviceGroupToken.getDesktopTokens(),data);
			}
			if(!deviceGroupToken.getWebTokens().isEmpty()) {
				fcmClientService.pushNotificationWithoutPayload(title,body,"",deviceGroupToken.getWebTokens(),data);
			}
			
		} catch (Exception ex) {
			logger.error("push-notification: " + ex.getMessage());
		}
	}

	/**
	 * Delete a message.
	 */
	@Transactional
	@Override
	public void deleteMessage(MessageChannelDTO messageChannelDTO, String userId) {
		MessageChannel messageChannel = messageChannelRepository.findByChannelName(messageChannelDTO.getChannelName());
		if (messageChannel == null) {
			throw new MessengerException(MessengerResponse.MESSAGE_CHANNEL_NAME_NOT_FOUND,
					messageChannel.getChannelName());
		}
		if (isSenderUser(messageChannel, userId)) {
			messageChannel.setSenderLastIndex(messageChannelDTO.getLastIndex());
			messageChannel.setSenderDeleted(1);
		} else {
			messageChannel.setRecipientLastIndex(messageChannelDTO.getLastIndex());
			messageChannel.setRecipientDeleted(1);
		}
		messageChannelRepository.save(messageChannel);
	}

	/**
	 * Registor a user on twilio server.
	 */
	@Override
	public void createUserOnTwilio(String userId) {
		// TODO Auto-generated method stub
		UserDTO user = this.getUserById(userId);
		if (user != null) {
			throw new MessengerException(MessengerResponse.USER_NOT_FOUND, userId);
		}
		twilioService.createUser(userId, user.getFirstname());
	}

	@Override
	@Transactional
	public List<MessageChannelDTO> getChannelHistoryByUserIdAndPoiId(String userId, Long poiId, String channelSid) {
		// TODO Auto-generated method stub

		List<MessageChannelDTO> result = new ArrayList<>();
		// reset is deleted to 0.
		if (!StringUtils.isEmpty(channelSid)) {
			MessageChannel channel = messageChannelRepository.findByChannelSid(channelSid);
			if (channel == null) {
				throw new MessengerException(MessengerResponse.MESSAGE_CHANNEL_SID_NOT_FOUND, channelSid);
			}
			/*
			 * if sender(recipient) was deleted message then recipient(sender) send message
			 * to, the action is reset deleted to 0 to start chat
			 */
			channel.setSenderDeleted(0);
			channel.setRecipientDeleted(0);
			channel = messageChannelRepository.saveAndFlush(channel);
		}
		// filter channel list.
		if(poiId == null) { // poiId optional
			poiId = 0L;
		}
		List<MessageChannel> messageChannels = messageChannelRepository
				.findByPoiIdAndSenderIdOrRecipientIdOrderByCreatedAtDesc(poiId,userId, userId, new PageRequest(0, 2000));

		// filter only get poi Channel.
		List<MessageChannel> messageOfPoiChannels = messageChannels.stream()
				.filter(predicate -> predicate.getPoiId() != 0L).collect(Collectors.toList());

		// filter only get Contact Channel.
		List<MessageChannel> messageOfContactChannels = messageChannels.stream()
				.filter(predicate -> predicate.getPoiId() == 0L).collect(Collectors.toList());

		messageOfPoiChannels.stream().forEach(channel -> {
			MessageChannelDTO channelDTO = new MessageChannelDTO();
			if (channel.getRecipientId().equals(userId)) {
				// side from shop owner.
				int isDeleted = channel.getRecipientDeleted();
				if (isDeleted == 0) {
					String id = channel.getSenderId();
					UserDTO userItem = getUserById(id);
					PoiDTO poiDTO = this.getPointOfInterestById(channel.getPoiId());
					channelDTO = MessageChannelDTO.toDTO(channel);
					channelDTO.setRecipient(userItem);
					channelDTO.setPoi(poiDTO);
					result.add(channelDTO);
				}
			} else {
				int isDeleted = channel.getSenderDeleted();
				if (isDeleted == 0) {
					PoiDTO poiDTO = this.getPointOfInterestById(channel.getPoiId());
					channelDTO = MessageChannelDTO.toDTO(channel);
					channelDTO.setPoi(poiDTO);
					result.add(channelDTO);
				}
			}
		});

		// add user history of contact channel.
		messageOfContactChannels.stream().forEach(channel -> {
			MessageChannelDTO channelDTO = new MessageChannelDTO();
			if (channel.getRecipientId().equals(userId)) {
				int isDeleted = channel.getRecipientDeleted();
				if (isDeleted == 0) {
					String id = channel.getSenderId();
					UserDTO userItem = getUserById(id);
					channelDTO = MessageChannelDTO.toDTO(channel);
					channelDTO.setRecipient(userItem);
					result.add(channelDTO);
				}
			} else {
				int isDeleted = channel.getSenderDeleted();
				if (isDeleted == 0) {
					String id = channel.getRecipientId();
					UserDTO userItem = getUserById(id);
					channelDTO = MessageChannelDTO.toDTO(channel);
					channelDTO.setRecipient(userItem);
					result.add(channelDTO);
				}
			}
		});

		List<MessageChannelDTO> resultSorted = result.stream()
				.sorted(Comparator.comparing(MessageChannelDTO::getCreatedAt).reversed()).collect(Collectors.toList());
		return resultSorted;
	}

}
