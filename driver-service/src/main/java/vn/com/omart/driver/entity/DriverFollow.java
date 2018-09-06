package vn.com.omart.driver.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "omart_driver_follow")
public class DriverFollow implements Serializable {

	private static final long serialVersionUID = -6385663868490458662L;

	@Id
	@GeneratedValue
	@Column(name = "id", columnDefinition = "int")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "car_type_id", referencedColumnName = "id", columnDefinition = "int")
	private CarType carType;

	@Column(name = "user_id", columnDefinition = "varchar")
	private String userId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CarType getCarType() {
		return carType;
	}

	public void setCarType(CarType carType) {
		this.carType = carType;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}