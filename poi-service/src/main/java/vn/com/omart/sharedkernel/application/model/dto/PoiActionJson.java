package vn.com.omart.sharedkernel.application.model.dto;

import java.io.Serializable;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public class PoiActionJson implements Serializable {
  private int likeNumber;
  private int dislikeNumber;
  private int viewNumber;

  public int getLikeNumber() {
    return likeNumber;
  }

  public void setLikeNumber(int likeNumber) {
    this.likeNumber = likeNumber;
  }

  public int getDislikeNumber() {
    return dislikeNumber;
  }

  public void setDislikeNumber(int dislikeNumber) {
    this.dislikeNumber = dislikeNumber;
  }

  public int getViewNumber() {
    return viewNumber;
  }

  public void setViewNumber(int viewNumber) {
    this.viewNumber = viewNumber;
  }
}
