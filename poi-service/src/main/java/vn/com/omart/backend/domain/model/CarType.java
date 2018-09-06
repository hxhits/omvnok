package vn.com.omart.backend.domain.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "omart_driver_car_type")
public class CarType implements Serializable {

	private static final long serialVersionUID = -8949831185350955932L;

	@Id
	@Column(name = "id", columnDefinition = "int")
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	private Long id;

	@Column(name = "name", columnDefinition = "varchar")

	private String name;

	@Column(name = "name_en", columnDefinition = "varchar")

	private String nameEn;

	@Column(name = "image", columnDefinition = "text")

	private String image;

	@Column(name = "order", columnDefinition = "int")

	private Long order;

	@Column(name = "unit_price", columnDefinition = "int")

	private Long unitPrice;

	@Column(name = "keywords", columnDefinition = "varchar")

	private String keywords;

	@OneToMany(mappedBy = "carType", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
	List<BookCar> bookCars;
	
	@OneToMany(mappedBy = "carType", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
	List<DriverFollow> driverFollows;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNameEn() {
		return nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Long getOrder() {
		return order;
	}

	public void setOrder(Long order) {
		this.order = order;
	}

	public Long getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Long unitPrice) {
		this.unitPrice = unitPrice;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public List<BookCar> getBookCars() {
		return bookCars;
	}

	public void setBookCars(List<BookCar> bookCars) {
		this.bookCars = bookCars;
	}

	public List<DriverFollow> getDriverFollows() {
		return driverFollows;
	}

	public void setDriverFollows(List<DriverFollow> driverFollows) {
		this.driverFollows = driverFollows;
	}

}