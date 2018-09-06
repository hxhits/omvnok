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
@Table(name = "omart_user_friend_request")
public class UserFriendRequest implements Serializable {

	private static final long serialVersionUID = 4995849922213904143L;

	@Id
	@GeneratedValue
	@Column(name = "id", columnDefinition = "int")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sender", referencedColumnName = "id", columnDefinition = "int")
	private UserProfile sender;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "recipient", referencedColumnName = "id", columnDefinition = "int")
	private UserProfile recipient;

	@Column(name = "state", columnDefinition = "int")
	private int state;

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

	public UserProfile getSender() {
		return sender;
	}

	public void setSender(UserProfile sender) {
		this.sender = sender;
	}

	public UserProfile getRecipient() {
		return recipient;
	}

	public void setRecipient(UserProfile recipient) {
		this.recipient = recipient;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
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