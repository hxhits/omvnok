package vn.com.omart.backend.domain.model;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TimelineRepository extends JpaRepository<Timeline, Long> {

	public List<Timeline> findByVideosIsNull();
	
	@Query("SELECT t FROM Timeline t WHERE t.isDeleted = :isDeleted ORDER BY t.createdAt DESC")
	public List<Timeline> getAllByIsDeleted(@Param("isDeleted") boolean isDeleted, Pageable pageable);

	public List<Timeline> findByIsDeletedAndTimelineTypeOrderByCreatedAtDesc(@Param("isDeleted") boolean isDeleted,
			@Param("timelineType") int timelineType, Pageable pageable);

	public int countByTimelineType(@Param("timelineType") int timelineType);
	
	@Query("SELECT COUNT(1) FROM Timeline t WHERE t.userId = :userId AND t.isMoment = true AND t.isDeleted = false")
	public int countByIsMoment(@Param("userId") String userId);

	public Page<Timeline> findByOrderByCreatedAtDesc(Pageable pageable);

	@Query(nativeQuery = true, value = "SELECT u.id,u.firstname,u.lastname,u.avatar FROM auth_db.oauth_user u WHERE u.id IN (:userIds)")
	public List<Object[]> getUserInUserIds(@Param("userIds") String[] userIds);

	public List<Timeline> findByTimelineType(@Param("timelineType") int timelineType);

	@Query(nativeQuery = true, value = "SELECT u.id,u.firstname,u.lastname,u.avatar FROM auth_db.oauth_user u WHERE u.id= :userId")
	public List<Object[]> getUserByUserId(@Param("userId") String userId);

	List<Timeline> findByIsMomentAndIsDeletedAndUserIdOrderByCreatedAtDesc(boolean isMoment,boolean isDeleted, String userId, Pageable pageable);
	
	@Query("SELECT t FROM Timeline t WHERE (t.isPrivated = :isPrivated OR t.userId = :userId) AND t.isMoment = :isMoment AND t.isDeleted = :isDeleted Order By t.createdAt Desc")
	List<Timeline> getTimelineOrderByCreatedAtDesc(@Param ("isPrivated") boolean isPrivated,@Param("userId") String userId,@Param("isMoment") boolean isMoment,@Param("isDeleted") boolean isDeleted,Pageable pageable);
	
	List<Timeline> findByUserIdAndIsDeletedAndIsMomentOrderByCreatedAtDesc(String userId, boolean isDeleted,boolean isMoment, Pageable pageable);

	@Query(value="SELECT u.activated FROM auth_db.oauth_user u WHERE u.id = :userId And u.activated = 1",nativeQuery = true)
	public Object getUserActive(@Param("userId") String userId);
	
	List<Timeline> findByUserIdAndIsDeletedAndIsMomentAndIsSaveHistoryOrderByCreatedAtDesc(String userId, boolean isDeleted,boolean isMoment,boolean isSaveHistory, Pageable pageable);

	@Query("SELECT t FROM Timeline t WHERE t.isPrivated = false AND t.userId = :userId AND t.isMoment = false AND t.isDeleted = false Order By t.createdAt Desc")
	List<Timeline> getTimelinesByUserId(@Param("userId") String userId,Pageable pageable);
	
}
