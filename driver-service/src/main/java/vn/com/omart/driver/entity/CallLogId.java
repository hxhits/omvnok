package vn.com.omart.driver.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class CallLogId implements Serializable {

	private static final long serialVersionUID = -2305231267690379121L;

	@Column(name = "driver_id", columnDefinition = "varchar")
	private String driverId;

	@Column(name = "bookcar_id", columnDefinition = "int")
	private Long bookcarId;

	public CallLogId() {
	}

	public CallLogId(String driverId, Long bookcarId) {
		super();
		this.driverId = driverId;
		this.bookcarId = bookcarId;
	}

	public String getDriverId() {
		return driverId;
	}

	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}

	public Long getBookcarId() {
		return bookcarId;
	}

	public void setBookcarId(Long bookcarId) {
		this.bookcarId = bookcarId;
	}

}
