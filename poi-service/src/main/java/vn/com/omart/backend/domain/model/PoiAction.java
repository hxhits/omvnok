package vn.com.omart.backend.domain.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "omart_poi_action")
public class PoiAction {

  @Id
  @GeneratedValue
  @Column(name = "id", columnDefinition = "int")
  private Long id;

  @Column(name = "user_id", length = 255, nullable = false, columnDefinition = "varchar")
  private String userId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "poi_id", referencedColumnName = "id", columnDefinition = "int")
  private PointOfInterest poi;

  // 1 LIKE, 2 DISLIKE, 3 FAVORITE
  @Column(name = "action_type", columnDefinition = "int")
  private int actionType;

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

  public PointOfInterest getPoi() {
    return poi;
  }

  public void setPoi(PointOfInterest poi) {
    this.poi = poi;
  }

  public int getActionType() {
    return actionType;
  }

  public void setActionType(int actionType) {
    this.actionType = actionType;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

}
