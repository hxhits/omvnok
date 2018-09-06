package vn.com.omart.messenger.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import vn.com.omart.messenger.application.response.MessageHistoryDTO;
import vn.com.omart.messenger.application.response.WebHookDTO;

/**
 * Message History Service Interface.
 * 
 * @author Win10
 *
 */
public interface MessageHistoryService {

	/**
	 * Save Message History.
	 * 
	 * @param dto
	 */
	public void save(MessageHistoryDTO dto);

	/**
	 * Save Message History.
	 * 
	 * @param webhook
	 */
	public void save(WebHookDTO webhook);

	/**
	 * Get messages.
	 * 
	 * @param id
	 * @param userId
	 * @param pageable
	 * @return List of MessageHistoryDTO
	 */
	public List<MessageHistoryDTO> getMessages(Long id, String userId, Pageable pageable);

	/**
	 * Convert existing message content to raw content.
	 * 
	 * @return
	 */
	public void toRawContent();
}
