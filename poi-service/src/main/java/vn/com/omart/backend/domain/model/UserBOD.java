package vn.com.omart.backend.domain.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "omart_user_bod")
public class UserBOD implements Serializable {

	private static final long serialVersionUID = 1613310474760066375L;

	@Id
	@Column(name = "id", columnDefinition = "varchar")
	private String id;

	@Column(name = "bod_name", columnDefinition = "varchar")
	private String bodName;

	@Column(name = "user_name", columnDefinition = "varchar")
	private String userName;

	@Column(name = "password", columnDefinition = "text")
	private String password;

	@Column(name = "created_at", columnDefinition = "timestamp")
	private Date createdAt;

	@Column(name = "updated_at", columnDefinition = "timestamp")
	private Date updatedAt;
	
	@OneToMany(mappedBy = "userBOD", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
	private List<PoiBOD> poiBODs;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBodName() {
		return bodName;
	}

	public void setBodName(String bodName) {
		this.bodName = bodName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public List<PoiBOD> getPoiBODs() {
		return poiBODs;
	}

	public void setPoiBODs(List<PoiBOD> poiBODs) {
		this.poiBODs = poiBODs;
	}
}
