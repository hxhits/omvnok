package vn.com.omart.backend.domain.model;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserFriendRepository extends JpaRepository<UserFriend, Long> {
	
	@Query("SELECT uf FROM UserFriend uf WHERE (UPPER(uf.user.name) like UPPER(:name)) OR (UPPER(uf.friend.name) like UPPER(:name))")
	public List<UserFriend> searchFriendByName(@Param("name") String name,Pageable pageable);
	
	public List<UserFriend> findByUserOrFriendOrderByCreatedAtDesc(UserProfile user,UserProfile friend,Pageable pageable);
	
	@Query("SELECT u FROM UserFriend u WHERE (u.user = :user AND u.friend = :friend) OR (u.user = :friend AND u.friend = :user)")
	public UserFriend getByUserAndFriendOrElse(@Param("user") UserProfile user,@Param("friend") UserProfile friend);
	
//	@Query(value="SELECT h.recipient_id, count(h.recipient_id) AS amount FROM omart_db.omart_message_history h where h.sender_id = :userId AND h.recipient_id NOT IN (:userIgnores) group by h.recipient_id having amount > 3 order by count(h.recipient_id) DESC",nativeQuery=true)
//	public List<Object[]> getUserCommunicateOften(@Param("userId") String userId,@Param("userIgnores") String []userIgnores);
	
	@Query(value="SELECT h.recipient_id, count(h.recipient_id) AS amount FROM omart_db.omart_message_history h where h.sender_id = :userId group by h.recipient_id having amount > 3 order by count(h.recipient_id) DESC",nativeQuery=true)
	public List<Object[]> getUserCommunicateOften(@Param("userId") String userId);
	
	@Query(value="SELECT c.omart_id FROM omart_db.omart_contact c where c.user_id = :userId and c.type = 1",nativeQuery=true)
	public List<String> getUserInContact(@Param("userId") String userId);
	
	@Query("SELECT uf FROM UserFriend uf WHERE uf.friend.userId = :userId or uf.user.userId = :userId")
	public List<UserFriend> getByUserId(@Param("userId") String userId);
	
}
