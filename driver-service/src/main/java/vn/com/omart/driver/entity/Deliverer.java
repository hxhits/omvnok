package vn.com.omart.driver.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "omart_poi_deliverer")
public class Deliverer implements Serializable {

	private static final long serialVersionUID = -2767668992499040051L;

	@Id
	@Column(name = "id", columnDefinition = "int")
	@GeneratedValue
	private Long id;

	@ManyToOne
	@JoinColumn(name = "driver_id", referencedColumnName = "user_id", columnDefinition = "varchar")
	private DriverInfo driver;

	@Column(name = "poi_id", columnDefinition = "int")
	private Long poiId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public DriverInfo getDriver() {
		return driver;
	}

	public void setDriver(DriverInfo driver) {
		this.driver = driver;
	}

	public Long getPoiId() {
		return poiId;
	}

	public void setPoiId(Long poiId) {
		this.poiId = poiId;
	}

}
