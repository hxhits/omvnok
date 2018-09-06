package vn.com.omart.backend.domain.model;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserFriendRequestRepository extends JpaRepository<UserFriendRequest, Long> {

	@Query("SELECT u FROM UserFriendRequest u WHERE (u.recipient = :recipient AND u.sender = :sender) OR (u.recipient = :sender AND u.sender = :recipient)")
	public UserFriendRequest getByRecipientAndSenderOrElse(@Param("recipient") UserProfile recipient,@Param("sender") UserProfile sender);

	public UserFriendRequest findByRecipientAndSender(UserProfile recipient, UserProfile sender);

	public List<UserFriendRequest> findByRecipientAndStateOrderByUpdatedAtDesc(UserProfile recipient, int state, Pageable pageable);
	
	public int countByRecipientAndState(UserProfile recipient, int state);

}
