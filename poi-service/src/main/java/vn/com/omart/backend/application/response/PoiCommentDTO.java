package vn.com.omart.backend.application.response;

import java.util.Date;
import com.vdurmont.emoji.EmojiParser;
import vn.com.omart.sharedkernel.application.model.dto.EntityMapper;

public class PoiCommentDTO {
  private Long id;
  private Long commentId;
  private String userId;
  private String firstName;
  private String avatar;
  private String comment;
  private Date createdAt;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getCommentId() {
    return commentId;
  }

  public void setCommentId(Long commentId) {
    this.commentId = commentId;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getComment() {
    return EmojiParser.parseToUnicode(comment);
  }

  public void setComment(String comment) {
    this.comment = EmojiParser.parseToAliases(comment);
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public String getAvatar() {
    return avatar;
  }

  public void setAvatar(String avatar) {
    this.avatar = avatar;
  }

  public static class QueryMapper implements EntityMapper<PoiCommentDTO, Object[]> {

    @Override
    public PoiCommentDTO map(Object[] entity) {
      PoiCommentDTO dto = new PoiCommentDTO();
      dto.setId(Long.valueOf(String.valueOf(entity[0])));
      dto.setCommentId(Long.valueOf(String.valueOf(entity[1])));
      dto.setUserId((String) entity[2]);
      dto.setFirstName((String) entity[3]);
      dto.setAvatar((String) entity[4]);
      dto.setComment((String) entity[5]);
      dto.setCreatedAt((Date) entity[6]);
      return dto;
    }

    @Override
    public void map(Object[] entity, PoiCommentDTO dto) {
      // TODO Auto-generated method stub
    }

    /**
     * Converter Object To PoiCommentDTO.
     * 
     * @param obj
     * @return PoiCommentDTO
     */
    public PoiCommentDTO converter(Object[] obj) {
      PoiCommentDTO comment = this.map(obj);
      return comment;
    }

  }

}
