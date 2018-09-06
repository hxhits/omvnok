package vn.com.omart.backend.domain.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="omart_coin")
public class OmartCoin implements Serializable {
	
	@Id
	@GeneratedValue
	@Column(name = "id", columnDefinition = "int")
	private Long id;

//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "user_profile_id", referencedColumnName = "id", columnDefinition = "int")
//	private UserProfile UserProfile;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_profile_id")
	private UserProfile userProfile;
	
	@Column(name = "user_id", columnDefinition = "varchar", unique = true, nullable = false)
	private String userId;
	
	@Column(name = "mart_coin", columnDefinition = "int")
	private int martCoin;
	
	@Column(name = "created_at", columnDefinition = "timestamp")
	private Date createdAt;
	
	@Column(name = "updated_at", columnDefinition = "timestamp")
	private Date updatedAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UserProfile getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getMartCoin() {
		return martCoin;
	}

	public void setMartCoin(int martCoin) {
		this.martCoin = martCoin;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

}
