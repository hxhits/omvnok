package vn.com.omart.messenger.port.adapter.rest;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.vdurmont.emoji.EmojiParser;

import de.bytefish.fcmjava.client.FcmClient;
import de.bytefish.fcmjava.model.options.FcmMessageOptions;
import de.bytefish.fcmjava.requests.data.DataUnicastMessage;
import de.bytefish.fcmjava.requests.notification.NotificationPayload;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import vn.com.omart.messenger.application.request.MessageCMD;
import vn.com.omart.messenger.application.response.MessageDTO;
import vn.com.omart.messenger.domain.model.MessageType;
import vn.com.omart.messenger.domain.model.MessengerHistory;
import vn.com.omart.messenger.domain.model.MessengerHistoryRepository;
import vn.com.omart.messenger.domain.model.PushNotificationToken;
import vn.com.omart.messenger.domain.model.PushNotificationTokenRepository;
import vn.com.omart.sharedkernel.application.model.dto.EntityMapper;

@RestController
@RequestMapping("/v1/messenger")
@Slf4j
public class MessengerResource implements MessageListener {

	@Value("${messenger.auto.message.text}")
	private String Auto_Message_Text;

	private final Map<String, DeferredResult<Object>> REQUESTS_WAITING = new ConcurrentHashMap<>();
	private final Map<String, SseEmitter> SSE_REQUESTS_WAITING = new ConcurrentHashMap<>();

	private final RedisTemplate<String, Object> redisTemplate;
	private final MessengerHistoryRepository messengerHistoryRepository;
	private final PushNotificationTokenRepository pushNotificationTokenRepository;

	private final FcmClient fcmClient;

	@Autowired
	public MessengerResource(RedisTemplate<String, Object> redisTemplate,
			MessengerHistoryRepository messengerHistoryRepository,
			PushNotificationTokenRepository pushNotificationTokenRepository, FcmClient fcmClient) {
		this.redisTemplate = redisTemplate;
		this.messengerHistoryRepository = messengerHistoryRepository;
		this.pushNotificationTokenRepository = pushNotificationTokenRepository;
		this.fcmClient = fcmClient;
	}

	@RequestMapping(method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
	public DeferredResult<Object> returner(@RequestHeader("X-User-Id") String userId) {
		// final UserSocialProfile userSocialProfile =
		// userProfileService.getSocialProfile(userId);

		if (StringUtils.isBlank(userId)) {
			throw new AccessDeniedException("You are missing User-ID");
		}

		log.debug("Handling request of user={}", userId);

		final DeferredResult<Object> deferredResult = new DeferredResult<>(15000L, Collections.emptyList());

		this.REQUESTS_WAITING.put(userId, deferredResult);
		deferredResult.onCompletion(() -> REQUESTS_WAITING.remove(userId));

		Object outComing = this.redisTemplate.boundListOps(userId).leftPop();
		if (null != outComing) {
			deferredResult.setResult(outComing);
		}

		return deferredResult;
	}

	@RequestMapping(method = RequestMethod.GET, value = { "/sse" })
	public SseEmitter returnerSSE(@RequestHeader("X-User-Id") String userId) {

		if (StringUtils.isBlank(userId)) {
			throw new AccessDeniedException("You are missing User-ID");
		}

		// final UserSocialProfile userSocialProfile =
		// userProfileService.getSocialProfile(userMtoId);
		log.debug("Handling request of user={}", userId);

		SseEmitter emitter = new SseEmitter(50000L);

		this.SSE_REQUESTS_WAITING.put(userId, emitter);

		emitter.onTimeout(() -> {
			log.debug("Request of user={} is timeout", userId);
			emitter.complete();
		});
		emitter.onCompletion(() -> {
			log.debug("Request of user={} is completed", userId);
			this.SSE_REQUESTS_WAITING.remove(userId);
		});

		while (this.redisTemplate.boundListOps(userId).size() > 0) {
			Object obj = this.redisTemplate.boundListOps(userId).leftPop();
			try {
				emitter.send(String.valueOf(obj), MediaType.APPLICATION_JSON);
			} catch (IOException e) {
				log.error("Can not emit because of {}", e.getMessage());
				emitter.complete();
				this.redisTemplate.boundListOps(userId).leftPush(obj);
				break;
			}
		}

		return emitter;
	}

	@RequestMapping(method = POST, produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
	public ResponseEntity receiver(@RequestHeader("X-User-Id") String userId, @RequestBody MessageCMD payload) {
		// JsonObject payloadJsonObject = new
		// JsonParser().parse(payload).getAsJsonObject();
		// final String recipientId = getPropertyAsString(payloadJsonObject,
		// PROP_RECIPIENT, PROP_ID);
		log.debug("Receive Message={}", payload);

		String conversationId = userId + payload.getRecipientId();

		String ownerId = null;
		String shopId = null;
		if (payload.getRecipientId().contains("|")) {
			ownerId = payload.getRecipientId().split(Pattern.quote("|"))[0];
			shopId = payload.getRecipientId().split(Pattern.quote("|"))[1];
		} else {
			ownerId = payload.getRecipientId();
		}

		String finalOwnerId = ownerId;
		String finalShopId = shopId;

		MessageDTO dto = new MessageDTO();
		dto.setTimestamp(new Date().getTime());
		dto.setPayload(payload.getPayload());
		dto.setSenderId(userId);
		dto.setShopId(payload.getShopId());
		// dto.setSenderName("Shop Abc");

		// Thông tin chat -> conversation_id, sender, recipient, timestamp, content

		Long shopIdAsLong = null;

		if (StringUtils.isNotEmpty(payload.getShopId())) {
			shopIdAsLong = Long.parseLong(payload.getShopId());
		}

		MessengerHistory.MessengerHistoryBuilder builder = MessengerHistory.builder().sender(userId)
				.recipient(payload.getRecipientId()).timestamp(new Date()).shopId(shopIdAsLong);

		LinkedHashMap a = (LinkedHashMap) payload.getPayload();
		if (a.containsKey(MessageType.TEXT.value().toLowerCase())) {

			String content = EmojiParser.parseToAliases((String) a.get(MessageType.TEXT.value().toLowerCase()));

			builder.type(MessageType.TEXT.name()).content(content);
		}
		if (a.containsKey(MessageType.IMAGE.value().toLowerCase())) {
			builder.type(MessageType.IMAGE.name()).content((String) a.get(MessageType.IMAGE.value().toLowerCase()));
		}
		messengerHistoryRepository.save(builder.build());

		this.redisTemplate.boundListOps(payload.getRecipientId()).rightPush(new Gson().toJson(dto));
		this.redisTemplate.convertAndSend("chat", payload.getRecipientId());

		log.debug("Message is sent");

		return new ResponseEntity(HttpStatus.OK);
	}

	@GetMapping(value = { "/conversations" })
	public List conversations(@RequestHeader("X-User-Id") String userId) {
		return messengerHistoryRepository.findListConversation(userId).stream().map(new Conversation.QueryMapper()::map)
				.collect(Collectors.toList());
	}

	/*
	 * @GetMapping(value = {"/histories/{recipient}"}) public List histories(
	 * 
	 * @RequestHeader("X-User-Id") String userId,
	 * 
	 * @PathVariable(value = "recipient") String recipient,
	 * 
	 * @RequestParam(value = "time", required = false) Long timestamp,
	 * 
	 * @RequestParam(value = "limit", required = false, defaultValue = "100")
	 * Integer limit ) { Date time = new Date(); if (null != timestamp) { time = new
	 * Date(timestamp); } return messengerHistoryRepository.getHistory(userId,
	 * recipient, time, limit) .stream().map(new History.QueryMapper()::map)
	 * .sorted(Comparator.comparingLong(History::getTimestamp)) // .peek(history ->
	 * { // if (history.getSender().equalsIgnoreCase(userId)) { //
	 * history.setSender(null); // } // if
	 * (history.getRecipient().equalsIgnoreCase(userId)) { //
	 * history.setRecipient(null); // } // }) .map(new
	 * MessageDTO.HistoryMapper()::map) .collect(Collectors.toList()); }
	 */

	@GetMapping(value = { "/histories/{recipient}" })
	public List histories(@RequestHeader("X-User-Id") String userId,
			@PathVariable(value = "recipient") String recipient,
			@RequestParam(value = "time", required = false) Long timestamp,
			@RequestParam(value = "shop_id", required = false, defaultValue = "0") String shopId,
			@RequestParam(value = "limit", required = false, defaultValue = "500") Integer limit) {
		/*
		 * Date time = new Date(); if (null != timestamp) { time = new Date(timestamp);
		 * }
		 */
		return messengerHistoryRepository.getHistory(userId, recipient, shopId, limit).stream()
				.map(new History.QueryMapper()::map)
				// .sorted(Comparator.comparingLong(History::getTimestamp))
				.map(new MessageDTO.HistoryMapper()::map).collect(Collectors.toList());
	}

	@DeleteMapping(value = { "/conversations/{friend_id}" })
	public void delete(@RequestHeader("X-User-Id") String userId, @PathVariable(value = "friend_id") String friendId,
			@RequestParam(value = "shop_id", required = false, defaultValue = "0") String shopId) {
		messengerHistoryRepository.deleteConversationBySender(userId, friendId, shopId);
		messengerHistoryRepository.deleteConversationByRecipient(userId, friendId, shopId);
	}

	private void pushNotify(String title, String body, int badge, String token, Map<String, String> data) {
		// Message Options:
		FcmMessageOptions options = FcmMessageOptions.builder().setTimeToLive(Duration.ofHours(1)).build();
		DataUnicastMessage unicastMessage = new DataUnicastMessage(options, token, data, NotificationPayload.builder()
				.setTitle(title).setBody(body).setSound("Default").setBadge(String.valueOf(badge)).build());
		fcmClient.send(unicastMessage);
	}

	@Override
	public void onMessage(Message message, byte[] pattern) {

		String recipientId = message.toString().replaceAll("\"", "");
		log.debug("Awake a message to {}", recipientId);

		DeferredResult<Object> deferredResult = this.REQUESTS_WAITING.get(recipientId);
		SseEmitter emitter = this.SSE_REQUESTS_WAITING.get(recipientId);

		// Using for Long Polling
		if (deferredResult != null) {

			log.debug("Long-Polling return message");
			Object outComing = this.redisTemplate.boundListOps(recipientId).leftPop();
			deferredResult.setResult(outComing);
			return;
		}

		// Using for SSE
		if (null != emitter) {
			log.debug("SSE return message");

			while (this.redisTemplate.boundListOps(recipientId).size() > 0) {
				Object obj = this.redisTemplate.boundListOps(recipientId).leftPop();
				try {
					emitter.send(String.valueOf(obj), MediaType.APPLICATION_JSON);
					return;
				} catch (IOException e) {
					log.error("Can not emit because of {}", e.getMessage());
					emitter.complete();
					this.redisTemplate.boundListOps(recipientId).leftPush(obj);
					break;
				}
			}
		}

		// Else no Long polling and no SSE -> Push notification
		log.debug("Push notification: No waiting request");
		PushNotificationToken pushNotif = pushNotificationTokenRepository.findByUserId(recipientId);

		// pushNotif = new PushNotificationToken("43b46937befd4f39b93483cf1e21c7ef",
		// "e3mFjZtdI1M:APA91bErbV82ULzJIFkw3z8SkwZrNr650f_3rNo3wH3iBIWxQ4T42QEs45kFiZBGc-9W8zx44XtaSgLCDt_3qySZQnWThdn02yvCjOKSlrTCNeievoo6j0BoHtOU6swJsu3bajHJEfSF");

		BoundListOperations<String, Object> listOps = this.redisTemplate.boundListOps(recipientId);
		Object obj = listOps.leftPop();
		Gson gson = new Gson();
		MessageDTO dto = gson.fromJson(String.valueOf(obj), MessageDTO.class);

		try {
			if (null != pushNotif) {
				String body = "";
				LinkedTreeMap a = (LinkedTreeMap) dto.getPayload();
				if (a.containsKey(MessageType.TEXT.value().toLowerCase())) {
					body = (String) a.get(MessageType.TEXT.value().toLowerCase());
				}
				// return message
				listOps.leftPush(obj);
				Long size = listOps.size();
				Map<String, String> data = Collections.unmodifiableMap(new HashMap<String, String>() {
					{
						put("event", "messenger");
						put("sender", dto.getSenderId());
						put("type", "0");
					}
				});
				// Push to FCM

				pushNotify("Bạn có tin nhắn mới!", StringUtils.abbreviate(body, 200), size.intValue(),
						pushNotif.token(), data);
			}

			if (dto.getSenderId() != null && recipientId != null) {
				// checking is the first time to communicate with shop owner.
				if (isTheFirstTimeChatting(dto.getSenderId(), recipientId)) {
					LinkedHashMap<String, String> text = new LinkedHashMap<>();
					text.put("text", Auto_Message_Text);
					MessageCMD sendText = new MessageCMD();
					sendText.setRecipientId(dto.getSenderId());
					sendText.setPayload(text);
					sendText.setShopId(dto.getShopId());
					this.receiver(recipientId, sendText);
				}
				// end
			}
		} catch (Exception ex) {
			log.error("Message error in notification: " + ex);
		}

	}

	/**
	 * Is the first time chatting.
	 * 
	 * @param sender
	 * @param recipient
	 * @return boolean
	 */
	public boolean isTheFirstTimeChatting(String sender, String recipient) {
		List<MessengerHistory> client = messengerHistoryRepository.findBySenderAndRecipient(sender, recipient);
		List<MessengerHistory> shopowner = messengerHistoryRepository.findBySenderAndRecipient(recipient, sender);
		if (client.size() == 1 && shopowner.size() == 0) {
			return true;
		}
		return false;
	}

	public static String encodeStringUrl(String url) {
		String encodedUrl = null;
		try {
			encodedUrl = URLEncoder.encode(url, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return encodedUrl;
		}
		return encodedUrl;
	}

	public static String decodeStringUrl(String encodedUrl) {
		String decodedUrl = null;
		try {
			decodedUrl = URLDecoder.decode(encodedUrl, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return decodedUrl;
		}
		return decodedUrl;
	}

	@Data
	public static class Shop {

		private String id;
		private String ownerId;
		private String name;
		private String avatar;

		public static class QueryMapper implements EntityMapper<Shop, Object[]> {

			@Override
			public Shop map(Object[] entity) {
				Shop c = new Shop();
				/*
				 * c.setRecipient((String) entity[0]); c.setAvatar((String) entity[1]);
				 * c.setName((String) entity[2]);
				 * 
				 * Object strShopId = entity[3]; if(strShopId != null) {
				 * c.setShopId(String.valueOf(strShopId));
				 * 
				 * } else { c.setShopId(""); }
				 */

				return c;
			}

			@Override
			public void map(Object[] entity, Shop dto) {

			}
		}
	}

	@Data
	public static class Conversation {

		private String recipient;
		private String avatar;
		private String name;
		private String shopId;

		private Shop shop;

		public static class QueryMapper implements EntityMapper<Conversation, Object[]> {

			@Override
			public Conversation map(Object[] entity) {
				Conversation c = new Conversation();
				c.setRecipient((String) entity[0]);
				c.setAvatar((String) entity[1]);
				c.setName((String) entity[2]);

				Object strShopId = entity[3];
				if (strShopId != null) {
					c.setShopId(String.valueOf(strShopId));

					Shop sh = null;
					Object sOwnerId = entity[4];
					if (sOwnerId != null) {
						sh = new Shop();
						sh.setId(c.getShopId());
						sh.setName((String) entity[4]);
						sh.setOwnerId((String) entity[5]);
						sh.setAvatar(c.getAvatar());
					}
					c.setShop(sh);
				} else {
					c.setShopId("");
					c.setShop(null);
				}

				return c;
			}

			@Override
			public void map(Object[] entity, Conversation dto) {

			}
		}

	}

	@Data
	public static class History {

		private Integer id;
		private String sender;
		private String recipient;
		private Long timestamp;
		private MessageType type;
		private String content;
		private Integer shopId;

		public static class QueryMapper implements EntityMapper<History, Object[]> {

			@Override
			public History map(Object[] entity) {
				History h = new History();
				h.setId((Integer) entity[0]);
				h.setSender((String) entity[1]);
				h.setRecipient((String) entity[2]);
				h.setTimestamp(((Date) entity[3]).getTime());
				h.setType(MessageType.from((String) entity[4]));
				h.setContent(EmojiParser.parseToUnicode((String) entity[5]));
				h.setShopId((Integer) entity[6]);

				return h;
			}

			@Override
			public void map(Object[] entity, History dto) {

			}
		}
	}
}
