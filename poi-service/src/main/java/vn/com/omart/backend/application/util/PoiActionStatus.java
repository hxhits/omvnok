package vn.com.omart.backend.application.util;

public enum PoiActionStatus {
  LIKE(1, "LIKE"), DISLIKE(2, "DISLIKE"), FAVORITE(3, "FAVORITE");

  private int poiActionStatusId;
  private String lable;

  private PoiActionStatus(int poiActionStatusId, String lable) {
    this.poiActionStatusId = poiActionStatusId;
    this.lable = lable;
  }

  public int getId() {
    return poiActionStatusId;
  }

  public String getLable() {
    return lable;
  }

  public void setLable(String lable) {
    this.lable = lable;
  }

  /**
   * Check status is exit in Poi Action enum.
   * 
   * @param status
   * @return boolean
   */
  public static boolean contains(int status) {
    for (PoiActionStatus notificationStatus : PoiActionStatus.values()) {
      if (notificationStatus.getId() == status) {
        return true;
      }
    }
    return false;
  }
}
