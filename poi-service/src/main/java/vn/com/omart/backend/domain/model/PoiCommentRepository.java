package vn.com.omart.backend.domain.model;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PoiCommentRepository extends JpaRepository<PoiComment, Long> {

  public int countByPoi(PointOfInterest poi);

  @Query(value = "SELECT comment.poi_id,comment.id,comment.user_id,user.firstname,user.avatar,comment.comment,comment.created_at "
      + "FROM omart_db.omart_poi_comment as comment " + "inner join auth_db.oauth_user as user on user.id = comment.user_id " + "where comment.poi_id = :poiId "
      + "ORDER BY comment.created_at ASC \n#pageable\n", nativeQuery = true)
  public Page<Object[]> findCommentByPoiId(@Param("poiId") Long poiId, Pageable pageable);
  
  @Query(value="SELECT c.poi_id, Count(c.poi_id) as amount FROM omart_db.omart_poi_comment c GROUP BY c.poi_id ORDER BY c.poi_id ASC",nativeQuery=true)
  List<Object[]> getCountComment();

}
