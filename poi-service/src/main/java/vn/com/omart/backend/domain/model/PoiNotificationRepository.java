package vn.com.omart.backend.domain.model;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PoiNotificationRepository extends JpaRepository<PoiNotification, Long> {

	List<PoiNotification> findByUserIdOrderByCreatedAtDesc(String userId);

	@Query("SELECT pn FROM PoiNotification pn WHERE pn.category.id IN (:catIdSelected) ORDER BY pn.createdAt Desc")
	List<PoiNotification> getByCatIdOrderByCreatedAtDesc(@Param("catIdSelected") Long[] catIdSelected);

	List<PoiNotification> findAllByOrderByCreatedAtDesc(Pageable pageable);
	
	@Query("SELECT n FROM PoiNotification n WHERE n.notificationType IN (:notificationTypes) And n.isDeleted = :isDeleted ORDER BY n.updatedAt Desc")
	List<PoiNotification> getAllByIsDeletedAndNotificationTypeOrderByUpdatedAtDesc(@Param("isDeleted") boolean isDeleted,@Param("notificationTypes") int[] notificationTypes, Pageable pageable);

//	List<PoiNotification> findAllByPoiAndIsDeletedOrderByCreatedAtDesc(PointOfInterest poi, boolean isDeleted,
//			Pageable pageable);
	
	List<PoiNotification> findAllByPoiAndIsDeletedAndNotificationTypeOrderByCreatedAtDesc(PointOfInterest poi, boolean isDeleted,int notificationType,Pageable pageable);
	
	List<PoiNotification> findAllByUpdatedAtIsNull();

}
