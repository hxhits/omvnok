package vn.com.omart.messenger.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import vn.com.omart.messenger.application.response.ChatDTO;
import vn.com.omart.messenger.application.response.MessageChannelDTO;
import vn.com.omart.messenger.application.response.MessageHistoryDTO;
import vn.com.omart.messenger.application.response.WebHookDTO;
import vn.com.omart.messenger.common.constant.Device;
import vn.com.omart.messenger.common.constant.Messenger;
import vn.com.omart.messenger.domain.model.EmptyJsonResponse;
import vn.com.omart.messenger.service.MessageChannelService;
import vn.com.omart.messenger.service.MessageHistoryService;

/**
 * Chat Controller.
 * 
 * @author Win10
 *
 */
@RestController
@RequestMapping("v1/messenger")
public class ChatController {

	@Autowired
	private MessageChannelService messageChannelService;

	@Autowired
	private MessageHistoryService messageHistoryService;

	/**
	 * Generate Token.
	 * 
	 * @param userId
	 * @param device
	 * @return ChatDTO
	 */
	@RequestMapping(value = "/chat/token", method = RequestMethod.GET)
	public ResponseEntity<ChatDTO> generateToken(@RequestHeader(value = "X-User-Id", required = true) String userId,
			@RequestHeader(value = "client", required = true) Device device) {
		ChatDTO chat = messageChannelService.chatTokenGenerator(userId, device);
		return new ResponseEntity<ChatDTO>(chat, HttpStatus.OK);
	}

	/**
	 * Save Message Channel.
	 * 
	 * @param userId
	 * @param messageChannel
	 * @return A HttpStatus and EmptyJsonResponse.
	 */
	@RequestMapping(value = "/chat/channel", method = RequestMethod.POST)
	public ResponseEntity<MessageChannelDTO> save(@RequestHeader(value = "X-User-Id", required = true) String userId,
			@RequestBody(required = true) MessageChannelDTO messageChannel) {
		messageChannel.setSenderId(userId);
		MessageChannelDTO entity = messageChannelService.save(messageChannel);
		return new ResponseEntity<MessageChannelDTO>(entity, HttpStatus.CREATED);
	}

	/**
	 * Channel History By UserId.
	 * 
	 * @param userId
	 * @return List of MessageChannelDTO
	 */
//	@RequestMapping(value = "/chat/channel", method = RequestMethod.GET)
//	public ResponseEntity<List<MessageChannelDTO>> getChannelHistoryByUserId(
//			@RequestHeader(value = "X-User-Id", required = true) String userId,
//			@RequestParam(value = "channelsid", required = false) String channelSid) {
//		List<MessageChannelDTO> users = messageChannelService.getChannelHistoryByUserId(userId, channelSid);
//		return new ResponseEntity<List<MessageChannelDTO>>(users, HttpStatus.OK);
//	}
	
	/**
	 *  Get channel history with UserId and PoiId
	 * @param userId
	 * @param channelSid
	 * @param poiId
	 * @return
	 */
	@RequestMapping(value = "/chat/channel", method = RequestMethod.GET)
	public ResponseEntity<List<MessageChannelDTO>> getChannelHistoryByUserIdAndPoiId(
			@RequestHeader(value = "X-User-Id", required = true) String userId,
			@RequestParam(value = "channelsid", required = false) String channelSid,
			@RequestParam(value = "poiId", required = false) Long poiId) {
		List<MessageChannelDTO> users = messageChannelService.getChannelHistoryByUserIdAndPoiId(userId, poiId, channelSid);
		return new ResponseEntity<List<MessageChannelDTO>>(users, HttpStatus.OK);
	}

	/**
	 * Webhook Twilio auto call this api to send notification.
	 * 
	 * @param eventType
	 * @param channelSid
	 * @param body
	 * @param attribute
	 * @param from
	 * @return EmptyJsonResponse
	 */
	@RequestMapping(value = "/chat/webhook", method = RequestMethod.GET)
	public ResponseEntity<EmptyJsonResponse> getDataOfWebhook(
			@RequestParam(value = "EventType", required = false) String eventType,
			@RequestParam(value = "MessageSid", required = false) String messageSid,
			@RequestParam(value = "Index", required = false) int index,
			@RequestParam(value = "ChannelSid", required = false) String channelSid,
			@RequestParam(value = "Body", required = false) String body,
			@RequestParam(value = "Attributes", required = false) String attribute,
			@RequestParam(value = "From", required = false) String from,
			@RequestParam(value = "DateCreated", required = false) String dateCreated) {

		if (Messenger.EVENT_TYPE_MESSAGE_SENT.equalsIgnoreCase(eventType)) {
			WebHookDTO webHook = new WebHookDTO();
			webHook.setBody(body);
			System.out.println("\n============chat body============" + body);
			webHook.setAttribute(attribute);
			webHook.setChannelSid(channelSid);
			webHook.setEventType(eventType);
			webHook.setFrom(from);
			webHook.setMessageIndex(index);
			messageChannelService.sendNotification(webHook);
			// auto save message into db.
			messageHistoryService.save(webHook);
		}
		return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.OK);
	}

	/**
	 * Delete message.
	 * 
	 * @param userId
	 * @param messageChannelDTO
	 * @return EmptyJsonResponse
	 */
	@RequestMapping(value = "/chat/channel", method = RequestMethod.DELETE)
	public ResponseEntity<EmptyJsonResponse> deleteMessage(
			@RequestHeader(value = "X-User-Id", required = true) String userId,
			@RequestBody(required = true) MessageChannelDTO messageChannelDTO) {
		messageChannelService.deleteMessage(messageChannelDTO, userId);
		return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.OK);
	}

	/**
	 * Get messages.
	 * @param userId
	 * @param id
	 * @param pageable
	 * @return List of MessageHistoryDTO
	 */
	@RequestMapping(value = "/chat/channel/{id}/messages", method = RequestMethod.GET)
	public ResponseEntity<List<MessageHistoryDTO>> getMessages(
			@RequestHeader(value = "X-User-Id", required = true) String userId,
			@PathVariable(value = "id", required = true) Long id, Pageable pageable) {
		List<MessageHistoryDTO> dto = messageHistoryService.getMessages(id, userId, pageable);
		return new ResponseEntity<List<MessageHistoryDTO>>(dto,HttpStatus.OK);
	}
	
	/**
	 * Convert old content to new content (raw data has payload data content the message)
	 * @param userId
	 * @return EmptyJsonResponse
	 */
	@RequestMapping(value = "/chat/convert-message-content", method = RequestMethod.GET)
	public ResponseEntity<EmptyJsonResponse> convertRawData(@RequestHeader(value = "X-User-Id", required = false) String userId) {
		messageHistoryService.toRawContent();
		return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(),HttpStatus.OK);
	}
	
	/**
	 * Only for testing
	 * 
	 * @param userId
	 * @param webHookDTO
	 * @return EmptyJsonResponse
	 */
	@RequestMapping(value = "/chat/test", method = RequestMethod.GET)
	public ResponseEntity<EmptyJsonResponse> autosave(@RequestHeader(value = "X-User-Id", required = false) String userId) {
		//messageHistoryService.toRawContent();
		return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(),HttpStatus.OK);
	}

}
