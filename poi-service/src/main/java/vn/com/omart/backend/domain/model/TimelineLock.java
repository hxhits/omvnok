package vn.com.omart.backend.domain.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "omart_timeline_lock")
public class TimelineLock implements Serializable {

	private static final long serialVersionUID = -2066491274368617447L;

	@Id
	@Column(name = "id", columnDefinition = "int")
	@GeneratedValue
	private Long id;

	@Column(name = "user_from", columnDefinition = "varchar")
	private String userFrom;
	
	@Column(name = "user_to", columnDefinition = "varchar")
	private String userTo;

	@Column(name = "created_at", columnDefinition = "timestamp")
	private Date createdAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserFrom() {
		return userFrom;
	}

	public void setUserFrom(String userFrom) {
		this.userFrom = userFrom;
	}

	public String getUserTo() {
		return userTo;
	}

	public void setUserTo(String userTo) {
		this.userTo = userTo;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

}
