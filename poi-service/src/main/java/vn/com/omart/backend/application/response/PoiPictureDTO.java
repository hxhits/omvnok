package vn.com.omart.backend.application.response;

import java.util.Date;
import com.vdurmont.emoji.EmojiParser;
import vn.com.omart.backend.application.util.DateUtils;
import vn.com.omart.backend.domain.model.PoiPicture;


public class PoiPictureDTO {

  private Long id;
  private String userId;
  private String url;
  private String title;
  private Date createdAt;
  private int likeNumber;
  private int commentNumber;
  private boolean isLike;

  public static PoiPictureDTO from(PoiPicture picture) {
    PoiPictureDTO entity = new PoiPictureDTO();
    entity.setCreatedAt(DateUtils.getCurrentDate());
    entity.setId(picture.getId());
    if(picture.getUrls()!=null) {
    	 if(!picture.getUrls().isEmpty()) {
    	    	entity.setUrl(picture.getUrls().get(0).getUrl());
    	    }
    }
    entity.setTitle(picture.getTitle());
    entity.setLikeNumber(picture.getLikeNumber());
    entity.setCommentNumber(picture.getCommentNumber());
    return entity;
  }

  public PoiPictureDTO() {}

  public PoiPictureDTO(Long id, String userId, String url, String title, Date createdAt, int likeNumber, int commentNumber) {
    super();
    this.id = id;
    this.userId = userId;
    this.url = url;
    this.title = title;
    this.createdAt = createdAt;
    this.likeNumber = likeNumber;
    this.commentNumber = commentNumber;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getTitle() {
    if (title != null) {
      return EmojiParser.parseToUnicode(title);
    }
    return null;
  }

  public void setTitle(String title) {
    if (title != null) {
      this.title = EmojiParser.parseToAliases(title);
    }
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public int getLikeNumber() {
    return likeNumber;
  }

  public void setLikeNumber(int likeNumber) {
    this.likeNumber = likeNumber;
  }

  public int getCommentNumber() {
    return commentNumber;
  }

  public void setCommentNumber(int commentNumber) {
    this.commentNumber = commentNumber;
  }

  public boolean isLike() {
    return isLike;
  }

  public void setLike(boolean isLike) {
    this.isLike = isLike;
  }

}
