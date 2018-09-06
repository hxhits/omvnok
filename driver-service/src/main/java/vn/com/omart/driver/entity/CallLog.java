package vn.com.omart.driver.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "omart_driver_call_log")
public class CallLog implements Serializable {

	private static final long serialVersionUID = 1352259663332277070L;
	
	@EmbeddedId
	private CallLogId id;

	@Column(name = "booker_id", columnDefinition = "varchar")
	private String bookerId;

	@Column(name = "latitude", columnDefinition = "double")
	private double latitude;

	@Column(name = "longitude", columnDefinition = "double")
	private double longitude;

	@Column(name = "state", columnDefinition = "int")
	private int state;

	@Column(name = "createa_at", columnDefinition = "timestamp")
	private Date createaAt;

	public CallLogId getId() {
		return id;
	}

	public void setId(CallLogId id) {
		this.id = id;
	}

	public String getBookerId() {
		return bookerId;
	}

	public void setBookerId(String bookerId) {
		this.bookerId = bookerId;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public Date getCreateaAt() {
		return createaAt;
	}

	public void setCreateaAt(Date createaAt) {
		this.createaAt = createaAt;
	}
}
