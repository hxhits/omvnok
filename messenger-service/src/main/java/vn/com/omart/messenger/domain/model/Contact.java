package vn.com.omart.messenger.domain.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.vdurmont.emoji.EmojiParser;

/**
 * Contact.
 * 
 * @author Win10
 *
 */
@Entity
@Table(name = "omart_contact")
public class Contact implements Serializable {

	private static final long serialVersionUID = 1524151671943478508L;

	@Id
	@Column(name = "id", columnDefinition = "int")
	@GeneratedValue
	private Long id;

	@Column(name = "user_id", columnDefinition = "varchar")
	private String userId;

	@Column(name = "contact_name", columnDefinition = "varchar")
	private String name;

	@Column(name = "omart_id", columnDefinition = "varchar")
	private String omartId;

	@Column(name = "phone_number", columnDefinition = "varchar")
	private String phoneNumber;

	@Column(name = "type", columnDefinition = "int")
	private int type;

	@Column(name = "created_at", columnDefinition = "timestamp")
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

	public String getName() {
		if (this.name != null) {
			return EmojiParser.parseToUnicode(name);
		}
		return null;
	}

	public void setName(String name) {
		if (name != null) {
			this.name = EmojiParser.parseToAliases(name);
		}
	}

	public String getOmartId() {
		return omartId;
	}

	public void setOmartId(String omartId) {
		this.omartId = omartId;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

}
