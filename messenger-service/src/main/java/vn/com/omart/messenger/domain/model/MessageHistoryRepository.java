package vn.com.omart.messenger.domain.model;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.com.omart.messenger.application.response.MessageHistoryDTO;

/**
 * Message History Repository.
 * 
 * @author Win10
 *
 */
@Repository
public interface MessageHistoryRepository extends JpaRepository<MessageHistory, Long> {
	
	@Query("SELECT new vn.com.omart.messenger.application.response.MessageHistoryDTO(mh.messageIndex,mh.rawContent) "
			+ "FROM MessageHistory mh WHERE mh.channelSid = :channelSid AND mh.messageIndex > :lastIndex "
			+ "ORDER BY mh.messageIndex ASC")
	public List<MessageHistoryDTO> getMessages(@Param("channelSid") String channelSid,@Param("lastIndex") int lastIndex, Pageable pageable);
}
