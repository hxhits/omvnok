package vn.com.omart.backend.domain.model;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.omart.backend.application.response.PoiActionDTO;

@Repository
public interface PoiPictureActionRepository extends JpaRepository<PoiPictureAction, Long> {

  public List<PoiPictureAction> findByPicture(PoiPicture picture);

  public PoiPictureAction findByUserIdAndPicture(String userId, PoiPicture picture);

  @Query("SELECT new vn.com.omart.backend.application.response.PoiActionDTO(COUNT(ppa.actionType),ppa.actionType) FROM PoiPictureAction ppa WHERE ppa.picture = :picture AND ppa.actionType = 1 GROUP BY ppa.actionType")
  PoiActionDTO countPictureActionLIKEByPicture(@Param("picture") PoiPicture picture);

  @Query("SELECT new vn.com.omart.backend.application.response.PoiActionDTO(ppa.actionType) FROM PoiPictureAction ppa WHERE ppa.picture = :picture AND ppa.userId = :userId")
  PoiActionDTO queryByUserIdAndPoi(@Param("userId") String userId, @Param("picture") PoiPicture picture);

  @Query("SELECT ppa FROM PoiPictureAction ppa WHERE ppa.userId = :userId AND ppa.picture IN (:picture)")
  List<PoiPictureAction> getPictureInList(@Param("userId") String userId, @Param("picture") List<PoiPicture> pictures);

}
