package vn.com.omart.messenger.service.implement;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.vdurmont.emoji.EmojiParser;

import vn.com.omart.messenger.application.response.MessageDTO;
import vn.com.omart.messenger.application.response.MessageHistoryDTO;
import vn.com.omart.messenger.application.response.WebHookDTO;
import vn.com.omart.messenger.domain.model.MessageChannel;
import vn.com.omart.messenger.domain.model.MessageChannelRepository;
import vn.com.omart.messenger.domain.model.MessageHistory;
import vn.com.omart.messenger.domain.model.MessageHistoryRepository;
import vn.com.omart.messenger.domain.model.MessageType;
import vn.com.omart.messenger.service.MessageHistoryService;

/**
 * Message History Service Implement.
 * 
 * @author Win10
 *
 */
@Service
@Transactional
public class MessageHistoryServiceImpl implements MessageHistoryService {

	private static final Logger logger = LoggerFactory.getLogger(MessageHistoryServiceImpl.class);

	@Autowired
	private MessageHistoryRepository messageHistoryRepository;

	@Autowired
	private MessageChannelRepository messageChannelRepository;

	@Autowired
	private MessageChannelServiceImpl messageChannelServiceImpl;

	/**
	 * Save Message History.
	 */
	@Transactional
	@Override
	public void save(MessageHistoryDTO dto) {
		// TODO Auto-generated method stub
		MessageHistory messageHistory = MessageHistoryDTO.toEntity(dto);
		messageHistoryRepository.save(messageHistory);
	}

	/**
	 * Save Message History.
	 */
	@Transactional
	@Override
	public void save(WebHookDTO webhook) {
		try {
			// TODO Auto-generated method stub
			Gson gson = new Gson();
			// convert string to object.
			MessageDTO messageDto = gson.fromJson(webhook.getBody(), MessageDTO.class);
			LinkedTreeMap payload = (LinkedTreeMap) messageDto.getPayload();
			String content = "undefine";
			if (payload.containsKey(MessageType.TEXT.value().toLowerCase())) {
				content = (String) payload.get(MessageType.TEXT.value().toLowerCase());
				if (!StringUtils.isEmpty(content)) {
					content = EmojiParser.parseToAliases(content);
				}
			} else if (payload.containsKey(MessageType.IMAGE.value().toLowerCase())) {
				content = (String) payload.get(MessageType.IMAGE.value().toLowerCase());
			}
			MessageHistory message = new MessageHistory();
			message.setChannelSid(webhook.getChannelSid());
			message.setContent(content);
			message.setMessageIndex(webhook.getMessageIndex());
			message.setRecipientId(messageDto.getRecipientId());
			message.setSenderId(messageDto.getSenderId());
			message.setCreatedAt(new Date());
			message.setRawContent(webhook.getBody());
			messageHistoryRepository.save(message);
		} catch (Exception ex) {
			logger.error("webhook-save-data: " + ex.getMessage());
		}
	}

	/**
	 * Get Messages.
	 */
	@Override
	public List<MessageHistoryDTO> getMessages(Long id, String userId, Pageable pageable) {
		// TODO Auto-generated method stub
		MessageChannel messageChannel = messageChannelRepository.findOne(id);
		int messageLastIndex;
		if (messageChannelServiceImpl.isSenderUser(messageChannel, userId)) {
			messageLastIndex = messageChannel.getSenderLastIndex();
		} else {
			messageLastIndex = messageChannel.getRecipientLastIndex();
		}
		String channelSid = messageChannel.getChannelSid();
		List<MessageHistoryDTO> dto = messageHistoryRepository.getMessages(channelSid,
				(messageLastIndex == 0 ? -1 : messageLastIndex), pageable);
		return dto;
	}

	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Set raw content. Convert content to raw content.
	 */
	@Override
	public void toRawContent() {
		List<MessageHistory> mhs = messageHistoryRepository.findAll();
		int batchSize = 20;
		int i = 0;
		Gson gson = new Gson();
		for (MessageHistory item : mhs) {
			if (StringUtils.isBlank(item.getRawContent())) {
				i++;
				MessageDTO rawData = new MessageDTO();
				rawData.setChannelSID(item.getChannelSid());
				rawData.setRecipientId(item.getRecipientId());
				rawData.setSenderId(item.getSenderId());
				rawData.setTimestamp(item.getCreatedAt().getTime());
				LinkedHashMap<String, String> text = new LinkedHashMap<>();
				String content = item.getContent().trim();
				if (!isImageUrl(content)) {
					text.put("text", content);
				} else {
					text.put("image", content);
				}
				rawData.setPayload(text);
				String rawContent = gson.toJson(rawData).toString();
				item.setRawContent(rawContent);
				entityManager.merge(item);
				if (i % batchSize == 0) {
					entityManager.flush();
					entityManager.clear();
				}
			}
		}
	}

	/**
	 * Checking url is an url image.
	 * 
	 * @param url
	 * @return boolean
	 */
	private boolean isImageUrl(String url) {
		String[] extensions = { ".png", ".jpg", ".jpeg", ".PNG", ".JPG", ".JPEG" };
		if (url.startsWith("https://img001.omartvietnam.com/")) {
			for (String exts : extensions) {
				if (url.endsWith(exts)) {
					return true;
				}
			}
		}
		return false;
	}

}
