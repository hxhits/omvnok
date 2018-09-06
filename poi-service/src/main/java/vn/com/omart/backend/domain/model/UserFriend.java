package vn.com.omart.backend.domain.model;

import java.io.Serializable;
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
@Table(name = "omart_user_friend")
public class UserFriend implements Serializable {

	@Id
	@GeneratedValue
	@Column(name = "id", columnDefinition = "int")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", referencedColumnName = "id", columnDefinition = "int")
	private UserProfile user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "friend_id", referencedColumnName = "id", columnDefinition = "int")
	private UserProfile friend;

	@Column(name = "created_at", columnDefinition = "timestamp")
	private Date createdAt;

	public UserFriend() {

	}

	public UserFriend(UserProfile user, UserProfile friend, Date createdAt) {
		super();
		this.user = user;
		this.friend = friend;
		this.createdAt = createdAt;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UserProfile getUser() {
		return user;
	}

	public void setUser(UserProfile user) {
		this.user = user;
	}

	public UserProfile getFriend() {
		return friend;
	}

	public void setFriend(UserProfile friend) {
		this.friend = friend;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
}
