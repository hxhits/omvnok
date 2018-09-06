package vn.com.omart.driver.entity;

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

import org.springframework.util.comparator.BooleanComparator;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonView;

import vn.com.omart.driver.jsonview.BookCarView;
import vn.com.omart.driver.jsonview.InitialView;

@Entity
@Table(name = "omart_driver_car_type")
public class CarType implements Serializable {

	private static final long serialVersionUID = -8949831185350955932L;

	@Id
	@Column(name = "id", columnDefinition = "int")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonView({ InitialView.OveralView.class,BookCarView.OveralView.class })
	private Long id;

	@Column(name = "name", columnDefinition = "varchar")
	@JsonView({ InitialView.OveralView.class, })
	private String name;

	@Column(name = "name_en", columnDefinition = "varchar")
	@JsonView({ InitialView.OveralView.class })
	private String nameEn;

	@Column(name = "image", columnDefinition = "text")
	@JsonView({ InitialView.DetailView.class })
	private String image;

	@Column(name = "order", columnDefinition = "int")
	@JsonView({ InitialView.DetailView.class })
	private Long order;

	@Column(name = "unit_price", columnDefinition = "int")
	@JsonView({ InitialView.OveralView.class })
	private Long unitPrice;
	
	@Column(name = "unit_price_2km", columnDefinition = "int")
	@JsonView({ InitialView.OveralView.class })
	private Long unitPrice2Km;

	@Column(name = "keywords", columnDefinition = "varchar")
	@JsonView({ InitialView.DetailView.class })
	private String keywords;

	@OneToMany(mappedBy = "carType", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
	List<DriverFollow> driverFollows;
	
	@OneToMany(mappedBy = "carType", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
	List<DriverLocation> driverLocations;
	
	@OneToMany(mappedBy = "carType", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
	List<BookCar> bookCars;
	
	@OneToMany(mappedBy = "carType", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
	private List<DriverInfo> driverInfos;
	
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
	
	public Long getUnitPrice2Km() {
		return unitPrice2Km;
	}

	public void setUnitPrice2Km(Long unitPrice2Km) {
		this.unitPrice2Km = unitPrice2Km;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public List<DriverFollow> getDriverFollows() {
		return driverFollows;
	}

	public void setDriverFollows(List<DriverFollow> driverFollows) {
		this.driverFollows = driverFollows;
	}

	public List<DriverLocation> getDriverLocations() {
		return driverLocations;
	}

	public void setDriverLocations(List<DriverLocation> driverLocations) {
		this.driverLocations = driverLocations;
	}

	public List<BookCar> getBookCars() {
		return bookCars;
	}

	public void setBookCars(List<BookCar> bookCars) {
		this.bookCars = bookCars;
	}

	public List<DriverInfo> getDriverInfos() {
		return driverInfos;
	}

	public void setDriverInfos(List<DriverInfo> driverInfos) {
		this.driverInfos = driverInfos;
	}
	
}