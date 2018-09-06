package vn.com.omart.messenger.domain.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoRoomRepository extends JpaRepository<VideoRoom, Long> {

	VideoRoom findById(Long id);

	@Query(value = "SELECT * FROM auth_db.oauth_user u where u.id = :userId", nativeQuery = true)
	public Object[] getUserById(@Param("userId") String userId);

	@Query(value = "SELECT * FROM omart_db.omart_video_room v where v.sender_id = :userId OR v.recipient_id = :userId "
			+ "ORDER BY created_at DESC LIMIT 1", nativeQuery = true)
	public VideoRoom findByUserOrRec(@Param("userId") String userId);

}
