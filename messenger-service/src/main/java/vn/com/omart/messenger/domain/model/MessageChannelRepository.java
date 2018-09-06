package vn.com.omart.messenger.domain.model;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageChannelRepository extends JpaRepository<MessageChannel, Long> {

	public List<MessageChannel> findBySenderIdOrRecipientIdOrderByCreatedAtDesc(String sender, String recipient);

	public MessageChannel findBySenderIdAndRecipientIdAndPoiId(String sender, String recipient, Long poiId);

	public MessageChannel findByChannelName(String channelName);

	public MessageChannel findByChannelSid(String channelSid);

	@Query(value = "SELECT mc FROM MessageChannel mc WHERE mc.senderId != mc.recipientId AND (mc.senderId= :sender OR mc.recipientId= :recipient) ORDER BY mc.createdAt DESC")
	public List<MessageChannel> findBySenderIdOrRecipientIdOrderByCreatedAtDesc(@Param("sender") String sender,
			@Param("recipient") String recipient, Pageable pageable);
	
	@Query(value = "SELECT mc FROM MessageChannel mc WHERE (mc.poiId= :poiId OR :poiId = 0L) AND mc.senderId != mc.recipientId AND (mc.senderId= :sender OR mc.recipientId= :recipient) ORDER BY mc.createdAt DESC")
	public List<MessageChannel> findByPoiIdAndSenderIdOrRecipientIdOrderByCreatedAtDesc(@Param("poiId") Long poiId ,@Param("sender") String sender,
			@Param("recipient") String recipient, Pageable pageable);
}
