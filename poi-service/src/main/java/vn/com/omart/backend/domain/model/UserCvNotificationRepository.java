package vn.com.omart.backend.domain.model;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCvNotificationRepository extends JpaRepository<UserCvNotification, Long> {

	UserCvNotification findByApply(RecruitmentApply apply);

	List<UserCvNotification> findByApplyAndNotificationType(RecruitmentApply apply, int notificationType);

	List<UserCvNotification> findAllByUserId(String userId);

	List<UserCvNotification> findAllByUserId(String userId, Pageable pageable);

	List<UserCvNotification> findAllByUserIdAndTypeAndIsDeleted(String userId, Long type, boolean isDeleted,
			Pageable pageable);

	List<UserCvNotification> findAllByPoiAndTypeAndIsDeleted(PointOfInterest poi, Long type, boolean isDeleted,
			Pageable pageable);

	List<UserCvNotification> findAllByApplyAndIsDeletedOrderByCreatedAtDesc(RecruitmentApply apply, boolean isDeleted);

	@Query(nativeQuery = true, value = "SELECT * FROM omart_db.omart_user_cv_notification WHERE apply_id = :applyId")
	List<UserCvNotification> getListByApply(@Param("applyId") Long applyId);

	@Query(nativeQuery = true, value = "SELECT * FROM omart_db.omart_user_cv_notification WHERE apply_id IN (:applyIds)")
	List<UserCvNotification> getAllByApplyIds(@Param("applyIds") String[] applyIds);

	@Query(nativeQuery = true, value = "SELECT u.id,u.firstname,u.lastname,u.avatar FROM auth_db.oauth_user u WHERE u.id IN (:userIds)")
	public List<Object[]> getUserInUserIds(@Param("userIds") String[] userIds);

	List<UserCvNotification> findAllByPoiAndIsDeleted(PointOfInterest poi, boolean isDeleted, Pageable pageable);

//	List<UserCvNotification> findAllByPoiAndNotificationTypeAndIsDeleted(PointOfInterest poi, int notificationType,
//			boolean isDeleted, Pageable pageable);
	
	List<UserCvNotification> findAllByPoiAndNotificationTypeAndIsDeletedOrderByUpdatedAtDesc(PointOfInterest poi, int notificationType,
			boolean isDeleted, Pageable pageable);

}
