package vn.com.omart.backend.domain.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="omart_poi_deliverer")
public class Deliverer implements Serializable {
	
	private static final long serialVersionUID = -2767668992499040051L;

	@Id
	@Column(name = "id", columnDefinition = "int")
	@GeneratedValue
	private Long id;

	@ManyToOne
	@JoinColumn(name = "driver_id", referencedColumnName = "user_id", columnDefinition = "varchar")
	private DriverInfo driver;
	
	@ManyToOne
	@JoinColumn(name = "poi_id", referencedColumnName = "id", columnDefinition = "int")
	private PointOfInterest poi;

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

	public PointOfInterest getPoi() {
		return poi;
	}

	public void setPoi(PointOfInterest poi) {
		this.poi = poi;
	}
	
}
