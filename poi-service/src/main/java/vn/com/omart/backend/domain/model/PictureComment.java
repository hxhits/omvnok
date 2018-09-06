package vn.com.omart.backend.domain.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.vdurmont.emoji.EmojiParser;

@Entity
@Table(name = "omart_poi_picture_comment")
public class PictureComment {

  @Id
  @GeneratedValue
  @Column(name = "id", columnDefinition = "int")
  private Long id;

  @Column(name = "user_id", length = 255, nullable = false, columnDefinition = "varchar")
  private String userId;

  @ManyToOne
  @JoinColumn(name = "picture_id", referencedColumnName = "id", columnDefinition = "int")
  private PoiPicture picture;

  @Column(name = "comment", columnDefinition = "varchar")
  private String comment;

  @Column(name = "created_at", columnDefinition = "TIMESTAMP")
  private Date createdAt;

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

  public PoiPicture getPicture() {
    return picture;
  }

  public void setPicture(PoiPicture picture) {
    this.picture = picture;
  }

  public String getComment() {
    if (comment != null) {
      return EmojiParser.parseToUnicode(comment);
    }
    return null;
  }

  public void setComment(String comment) {
    if (comment != null) {
      this.comment = EmojiParser.parseToAliases(comment);
    }
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

}
