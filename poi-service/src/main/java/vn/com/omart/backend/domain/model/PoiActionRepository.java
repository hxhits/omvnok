package vn.com.omart.backend.domain.model;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.com.omart.backend.application.response.PoiActionDTO;

@Repository
public interface PoiActionRepository extends JpaRepository<PoiAction, Long> {
	
  List<PoiAction> findByUserIdAndActionType(String userId,int actionType);

  PoiAction findByUserIdAndPoi(String userId, PointOfInterest poi);

  int countByActionTypeAndPoi(int actionType, PointOfInterest poi);

  @Query("SELECT COUNT(pa.actionType) AS resultCount FROM PoiAction pa WHERE pa.poi = :poi AND pa.actionType = :actionType GROUP BY pa.actionType")
  int actionCount(@Param("poi") PointOfInterest poi, @Param("actionType") int actionType);

  @Query("SELECT new vn.com.omart.backend.application.response.PoiActionDTO(COUNT(pa.actionType),pa.actionType) FROM PoiAction pa WHERE pa.poi = :poi GROUP BY pa.actionType")
  List<PoiActionDTO> countPoiActionByActionType(@Param("poi") PointOfInterest poi);

  @Query("SELECT new vn.com.omart.backend.application.response.PoiActionDTO(pa.actionType) FROM PoiAction pa WHERE pa.poi = :poi AND pa.userId = :userId")
  PoiActionDTO queryByUserIdAndPoi(@Param("userId") String userId, @Param("poi") PointOfInterest poi);

  @Query("SELECT new vn.com.omart.backend.application.response.PoiActionDTO(COUNT(pa.actionType),pa.actionType) FROM PoiAction pa WHERE pa.poi = :poi AND pa.actionType = 1 GROUP BY pa.actionType")
  PoiActionDTO countPoiActionLIKEByPoi(@Param("poi") PointOfInterest poi);
  
  @Query(value="SELECT a.poi_id, COUNT(a.action_type) as amount FROM omart_db.omart_poi_action a WHERE a.action_type = 1 GROUP BY a.poi_id ORDER BY a.poi_id ASC",nativeQuery=true)
  List<Object[]> getCountLike();
  
  @Query("SELECT new vn.com.omart.backend.application.response.PoiActionDTO(0L,pa.poi.id,pa.actionType) FROM PoiAction pa WHERE pa.poi.id IN (:id) AND pa.actionType = 1 AND pa.userId = :userId")
  List<PoiActionDTO> getPoiAction(@Param("id") Long [] idList,@Param("userId") String userId);
  
  @Query("SELECT a FROM PoiAction a WHERE a.userId= :userId AND a.actionType = 1 AND a.poi.isDeleted = false AND a.poi.isApproved = true Order By a.createdAt DESC")
  List<PoiAction> getPoiByUserLiked(@Param("userId") String userId,Pageable pageable);
 

}
