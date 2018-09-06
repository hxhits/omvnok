package vn.com.omart.driver.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "omart_driver_setting")
public class Setting {

	@Id
	@GeneratedValue
	@Column(name = "id", columnDefinition = "int")
	private Long id;

	@Column(name = "ring_index", columnDefinition = "int", nullable = false)
	private int ringIndex;

	@Column(name = "is_disabled", columnDefinition = "bit", nullable = false)
	private boolean isDisabled;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private DriverInfo driverInfo;

	public int getRingIndex() {
		return ringIndex;
	}

	public void setRingIndex(int ringIndex) {
		this.ringIndex = ringIndex;
	}

	public boolean isDisabled() {
		return isDisabled;
	}

	public void setDisabled(boolean isDisabled) {
		this.isDisabled = isDisabled;
	}

	public DriverInfo getDriverInfo() {
		return driverInfo;
	}

	public void setDriverInfo(DriverInfo driverInfo) {
		this.driverInfo = driverInfo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
