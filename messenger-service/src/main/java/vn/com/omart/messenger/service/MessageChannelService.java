package vn.com.omart.messenger.service;

import java.util.List;

import vn.com.omart.messenger.application.response.ChatDTO;
import vn.com.omart.messenger.application.response.MessageChannelDTO;
import vn.com.omart.messenger.application.response.WebHookDTO;
import vn.com.omart.messenger.common.constant.Device;

/**
 * Message Channel Service Interface.
 * 
 * @author Win10
 *
 */
public interface MessageChannelService {

	/**
	 * Save Message Channel.
	 * 
	 * @param messageChannelDTO
	 */
	public MessageChannelDTO save(MessageChannelDTO messageChannelDTO);

	/**
	 * Get channel history with UserId.
	 * 
	 * @param userid
	 * @param channelName
	 *            optional
	 * @return List of MessageChannelDTO
	 */
//	public List<MessageChannelDTO> getChannelHistoryByUserId(String userid, String channelSid);
	
	/**
	 * Get channel history with UserId and PoiId
	 * 
	 * @param userid
	 * @param channelName
	 *            optional
	 * @return List of MessageChannelDTO
	 */
	public List<MessageChannelDTO> getChannelHistoryByUserIdAndPoiId(String userid,Long poiId,String channelSid);

	/**
	 * Generate token.
	 * 
	 * @param identity
	 * @param device
	 * @return ChatDTO
	 */
	public ChatDTO chatTokenGenerator(String identity, Device device);

	/**
	 * Push Notification.
	 * 
	 * @param webhook
	 */
	public void sendNotification(WebHookDTO webhook);

	/**
	 * Delete message. only set data without deleting.
	 * 
	 * @param messageChannelDTO
	 * @param userId
	 */
	public void deleteMessage(MessageChannelDTO messageChannelDTO, String userId);

	/**
	 * Registor a user on twilio server.
	 * 
	 * @param userId
	 */
	public void createUserOnTwilio(String userId);

}
