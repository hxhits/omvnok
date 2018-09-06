package vn.com.omart.backend.domain.model;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecruitmentRepository extends JpaRepository<Recruitment, Long> {
	
	@Query("SELECT r FROM Recruitment r WHERE r.expiredAt >= CURRENT_DATE And r.isDeleted = false And r.state = 0 And r.poi.isDeleted = false And r.poi.isApproved = true Order By r.updatedAt DESC")
	Page<Recruitment> getAllByFilterIsDeletedOrderByUpdatedAtDesc(Pageable pageable);

	List<Recruitment> findByPoiOrderByCreatedAtDesc(PointOfInterest poi);

	List<Recruitment> findByPoiAndIsDeletedOrderByCreatedAtDesc(PointOfInterest poi, boolean isDeleted);

	List<Recruitment> findByPoiAndIsDeletedAndStateOrderByCreatedAtDesc(PointOfInterest poi, boolean isDeleted, int state);

	List<Recruitment> findByRecruitmentPositionAndIsDeleted(RecruitmentPosition poi, boolean isDeleted);

	@Query("SELECT DISTINCT r.poi.id FROM Recruitment r WHERE r.recruitmentPosition = :recruitmentPosition AND r.isDeleted = :isDeleted AND r.state = :state")
	List<Long> findDistinctPoiByPositionAndIsDeletedAndState(
			@Param("recruitmentPosition") RecruitmentPosition recruitmentPosition,
			@Param("isDeleted") boolean isDeleted, @Param("state") int state);

	@Query(value = "SELECT distinct r.poi_id FROM omart_db.omart_recruitment r where FIND_IN_SET(r.position_id, (:catIds)) AND r.state = :state AND r.is_deleted = false", nativeQuery = true)
	List<Long> getRecruitmentInPositionIdAndState(@Param("catIds") String catIds, @Param("state") int state);
	
//	@Query("SELECT p FROM Recruitment p WHERE p.expiredAt >= CURRENT_DATE Order By p.createdAt DESC")
//	List<Recruitment> huongtest(Pageable pageable);

}
