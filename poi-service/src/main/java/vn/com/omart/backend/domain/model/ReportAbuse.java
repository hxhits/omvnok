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

import com.vdurmont.emoji.EmojiParser;

@Entity
@Table(name="omart_report_abuse")
public class ReportAbuse implements Serializable {
	
	private static final long serialVersionUID = 3248749016476342986L;

	@Id
	@Column(name = "id", columnDefinition = "int")
	@GeneratedValue
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "poi_id", referencedColumnName = "id", columnDefinition = "int")
	private PointOfInterest poi;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "owner_profile_id", referencedColumnName = "id", columnDefinition = "int")
	private UserProfile ownerProfile;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_profile_id", referencedColumnName = "id", columnDefinition = "int")
	private UserProfile userProfile;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id", referencedColumnName = "id", columnDefinition = "int")
	private Order order;
	
	@Column(name = "reason", columnDefinition = "varchar")
	private String reason;

	@Column(name = "side", columnDefinition = "int")
	private int side;
	
	@Column(name = "created_at", columnDefinition = "timestamp")
	private Date createdAt;
	
	@ManyToOne
	@JoinColumn(name = "driver_id", referencedColumnName = "user_id", columnDefinition = "varchar")
	private DriverInfo driver;

	public int getSide() {
		return side;
	}

	public void setSide(int side) {
		this.side = side;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PointOfInterest getPoi() {
		return poi;
	}

	public void setPoi(PointOfInterest poi) {
		this.poi = poi;
	}

	public UserProfile getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}

	public String getReason() {
		if (this.reason != null) {
			return EmojiParser.parseToUnicode(reason);
		}
		return null;
	}

	public void setReason(String reason) {
		if (reason != null) {
			this.reason = EmojiParser.parseToAliases(reason);
		}
	}

	public UserProfile getOwnerProfile() {
		return ownerProfile;
	}

	public void setOwnerProfile(UserProfile ownerProfile) {
		this.ownerProfile = ownerProfile;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public DriverInfo getDriver() {
		return driver;
	}

	public void setDriver(DriverInfo driver) {
		this.driver = driver;
	}
	
}
