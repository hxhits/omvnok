package vn.com.omart.backend.domain.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "omart_driver_location")
public class DriverLocation implements Serializable {

	private static final long serialVersionUID = -2047903102201906543L;

	@Id
	@Column(name = "user_id", columnDefinition = "varchar")
	private String userId;

	@Column(name = "latitude", columnDefinition = "double")
	private double latitude;

	@Column(name = "longitude", columnDefinition = "double")
	private double longitude;

	@Column(name = "name", columnDefinition = "varchar")
	private String name;

	@Column(name = "updated_at", columnDefinition = "timestamp")
	private Date updatedAt;

	@ManyToOne
	@JoinColumn(name = "car_type_id", referencedColumnName = "id", columnDefinition = "int")
	private CarType carType;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public CarType getCarType() {
		return carType;
	}

	public void setCarType(CarType carType) {
		this.carType = carType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}