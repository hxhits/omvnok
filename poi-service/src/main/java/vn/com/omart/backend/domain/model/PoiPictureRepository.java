package vn.com.omart.backend.domain.model;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PoiPictureRepository extends JpaRepository<PoiPicture, Long> {

	public List<PoiPicture> findPoiPictureByPoi(PointOfInterest poi);

	public List<PoiPicture> findPoiPictureByPoi(PointOfInterest poi, Pageable pageable);

	public List<PoiPicture> findPoiPictureByPoiAndIsDeleted(PointOfInterest poi, boolean isDeleted,
			Pageable pageable);

	public List<PoiPicture> findPoiPictureByPoiAndIsDeletedOrderByCreatedAtDesc(PointOfInterest poi,
			boolean isDeleted, Pageable pageable);

	@Query(nativeQuery = true, value = "SELECT u.id,u.firstname,u.lastname,u.avatar FROM auth_db.oauth_user u WHERE u.id IN (:userIds)")
	public List<Object[]> getUserInUserIds(@Param("userIds") String[] userIds);

}
