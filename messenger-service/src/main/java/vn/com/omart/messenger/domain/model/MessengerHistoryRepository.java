package vn.com.omart.messenger.domain.model;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface MessengerHistoryRepository extends PagingAndSortingRepository<MessengerHistory, Long> {
	// @Query("SELECT DISTINCT name FROM people p (nolock) WHERE p.name NOT IN
	// (:myparam)")
	// List<String> findNonReferencedNames(@Param("myparam")List<String> names);

	// @Query(value = "SELECT DISTINCT " +
	// " recipient, b.avatar, b.firstname " +
	// "FROM " +
	// " omart_db.messenger_history a, " +
	// " auth_db.oauth_user b " +
	// "WHERE " +
	// " sender = :userId " +
	// " AND a.recipient = b.id", nativeQuery = true)
	// List<Object[]> findListConversation(@Param("userId") String userId);

	@Query(value = "SELECT  "
			+ "    a.id, a.avatar, a.firstname, b.shop_id, c.name as shop_name, c.own_id, c.avatar_image " + "FROM "
			+ "    auth_db.oauth_user a, " + "    (                     " + "    SELECT DISTINCT "
			+ "        recipient AS user_id, shop_id AS shop_id " + "    FROM " + "        omart_db.messenger_history "
			+ "    WHERE " + "        sender = :userId AND sender_deleted=0" +

			"    UNION " +

			"    SELECT DISTINCT " + "        sender AS user_id, shop_id AS shop_id " + "    FROM "
			+ "        omart_db.messenger_history " + "    WHERE "
			+ "        recipient = :userId AND recipient_deleted=0) b "
			+ "LEFT JOIN omart_db.omart_point_of_interest c ON b.shop_id = c.id " + "WHERE "
			+ "    a.id = b.user_id", nativeQuery = true)
	List<Object[]> findListConversation(@Param("userId") String userId);

	@Query(nativeQuery = true, value = "SELECT * " + " FROM omart_db.messenger_history " + " WHERE 1 = 1 "
			+ "     AND shop_id = :shopId "
			+ "     AND (((sender = :sender AND sender_deleted=0) OR (recipient = :sender AND recipient_deleted=0))"
			+ "     OR ((sender = :recipient AND recipient_deleted=0) OR (recipient = :recipient AND sender_deleted=0)))"
			+ "ORDER BY `timestamp` " + "LIMIT :limit")
	List<Object[]> getHistoryOld(@Param("sender") String sender, @Param("recipient") String recipient,
			@Param("shopId") String shopId, @Param("limit") Integer limit);

	@Query(nativeQuery = true, value = "SELECT * " + " FROM omart_db.messenger_history " + " WHERE 1 = 1 "
			+ "     AND shop_id = :shopId "
			+ "     AND ((sender = :sender AND sender_deleted=0) OR (recipient = :sender AND recipient_deleted=0))"
			+ "     AND ((sender = :recipient AND recipient_deleted=0) OR (recipient = :recipient AND sender_deleted=0))"
			+ "ORDER BY `timestamp` " + "LIMIT :limit")
	List<Object[]> getHistory(@Param("sender") String sender, @Param("recipient") String recipient,
			@Param("shopId") String shopId, @Param("limit") Integer limit);

	// Delete conversation
	@Modifying
	@Transactional
	@Query(nativeQuery = true, value = "UPDATE omart_db.messenger_history " + " SET sender_deleted = 1 "
			+ " WHERE sender = :userId " + "   AND recipient = :recipient " + "   AND shop_id = :shopId")
	void deleteConversationBySender(@Param("userId") String userId, @Param("recipient") String recipient,
			@Param("shopId") String shopId);

	@Modifying
	@Transactional
	@Query(nativeQuery = true, value = "UPDATE omart_db.messenger_history " + " SET recipient_deleted = 1 "
			+ " WHERE sender = :sender " + "   AND recipient = :userId " + "   AND shop_id = :shopId")
	void deleteConversationByRecipient(@Param("userId") String userId, @Param("sender") String sender,
			@Param("shopId") String shopId);

	@Transactional
	public List<MessengerHistory> findBySenderAndRecipient(String sender, String recipient);

	@Query(value = "SELECT * FROM auth_db.oauth_user u where u.id = :userId", nativeQuery = true)
	public Object getUserById(@Param("userId") String userId);

	@Query(value = "SELECT * FROM auth_db.oauth_user u where u.username IN (:usernames)", nativeQuery = true)
	public List<Object[]> getUserByUserNames(@Param("usernames") List<String> usernames);

}
