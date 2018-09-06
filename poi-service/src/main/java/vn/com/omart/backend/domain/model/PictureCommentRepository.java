package vn.com.omart.backend.domain.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PictureCommentRepository extends JpaRepository<PictureComment, Long> {

  @Query(value = "SELECT comment.picture_id,comment.id,comment.user_id,user.firstname,user.avatar,comment.comment,comment.created_at "
      + "FROM omart_db.omart_poi_picture_comment as comment " + "inner join auth_db.oauth_user as user "
      + "on user.id = comment.user_id where comment.picture_id = :pictureId ORDER BY comment.created_at ASC \n#pageable\n", nativeQuery = true)
  public Page<Object[]> findCommentByPictureId(@Param("pictureId") Long pictureId, Pageable pageable);

  @Query(value = "SELECT comment.picture_id,comment.id,comment.user_id,user.firstname,user.avatar,comment.comment,comment.created_at "
      + "FROM omart_db.omart_poi_picture_comment as comment " + "inner join auth_db.oauth_user as user "
      + "on user.id = comment.user_id where comment.picture_id = :pictureId ORDER BY comment.created_at DESC \n#pageable\n", nativeQuery = true)
  public Page<Object[]> findCommentByPictureIdDesc(@Param("pictureId") Long pictureId, Pageable pageable);

  public int countByPicture(PoiPicture picture);
}
