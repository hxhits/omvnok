package vn.com.omart.backend.domain.model;

import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "omart_poi_own")
@ToString
public class Owner {

    @Id
    @Column(name = "id", columnDefinition = "int")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", columnDefinition = "varchar")
    private String userId;

    @Column(name = "name", columnDefinition = "varchar")
    private String name;

    @Column(name = "avatar", columnDefinition = "text")
    private String avatar;

    @Column(name = "phone_number", columnDefinition = "varchar")
    private String phoneNumber;

    @Column(name = "password", columnDefinition = "text")
    private String password;

    @Column(name = "is_paid", columnDefinition = "bit")
    private boolean isPaid;
    
    @Column(name = "trial_at", columnDefinition = "TIMESTAMP")
    private Date trialAt;
    
    @Column(name = "created_by", columnDefinition = "varchar")
    private String createdBy;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP")
    private Date createdAt;

    @Column(name = "updated_by", columnDefinition = "varchar")
    private String updatedBy;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP")
    private Date updatedAt;

    public Owner() {
    }

    public Owner(String name, String avatar, String phoneNumber) {
        this.name = name;
        this.avatar = avatar;
        this.phoneNumber = phoneNumber;
    }

    public Owner(String name, String avatar, String phoneNumber, String password, String username, String userId) {
        this.userId = userId;
        this.name = name;
        this.avatar = avatar;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.createdBy = username;
        this.createdAt = new Date();
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long id() {
        return id;
    }

    public String name() {
        return name;
    }

    public String avatar() {
        return avatar;
    }

    public String phoneNumber() {
        return phoneNumber;
    }

    public String password() {
        return password;
    }

    public Date createdAt() {
        return createdAt;
    }

    public String userId() {
        return userId;
    }
    
    public String createdBy() {
        return createdBy;
    }
    
    public String updatedBy() {
        return updatedBy;
    }

	public boolean isPaid() {
		return isPaid;
	}

	public void setPaid(boolean isPaid) {
		this.isPaid = isPaid;
	}

	public Date getTrialAt() {
		return trialAt;
	}

	public void setTrialAt(Date trialAt) {
		this.trialAt = trialAt;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
}
