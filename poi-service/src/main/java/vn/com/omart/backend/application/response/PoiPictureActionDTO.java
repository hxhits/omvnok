package vn.com.omart.backend.application.response;

import java.util.Date;
import lombok.Data;
import vn.com.omart.backend.application.util.DateUtils;
import vn.com.omart.backend.domain.model.PoiPicture;
import vn.com.omart.backend.domain.model.PoiPictureAction;

@Data
public class PoiPictureActionDTO {

  private Long id;
  private String userId;
  private PoiPicture picture;
  private int actionType;
  private Date createdAt;
  private boolean isliked;

  public static PoiPictureActionDTO from(PoiPictureAction entity) {
    PoiPictureActionDTO action = new PoiPictureActionDTO();
    action.setCreatedAt(DateUtils.getCurrentDate());
    action.setId(entity.getId());
    action.setIsliked(likeMapping(entity.getActionType()));
    return action;
  }

  public static boolean likeMapping(int actionType) {
    boolean isLike = false;
    switch (actionType) {
      case 1:
        isLike = true;
        break;
      case 2:
        isLike = false;
        break;
    }
    return isLike;
  }

}
