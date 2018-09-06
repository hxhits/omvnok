package vn.com.omart.backend.domain.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonView;

import vn.com.omart.backend.constants.OmartType;

@Entity
@Table(name = "omart_book_car")
public class BookCar implements Serializable {

	private static final long serialVersionUID = 1453079944212754821L;

	@Id
	@GeneratedValue
	@Column(name = "id", columnDefinition = "int")
	private Long id;

	@Column(name = "user_id", columnDefinition = "varchar")
	private String userId;

	@ManyToOne
	@JoinColumn(name = "car_type", referencedColumnName = "id", columnDefinition = "int")
	private CarType carType;

	@Column(name = "origin_latitude", columnDefinition = "double")
	private Double originLatitude;

	@Column(name = "origin_longitude", columnDefinition = "double")
	private Double originLongitude;

	@Column(name = "destination_latitude", columnDefinition = "double")
	private Double destinationLatitude;

	@Column(name = "destination_longitude", columnDefinition = "double")
	private Double destinationLongitude;

	@Column(name = "origin_address", columnDefinition = "varchar")
	private String originAddress;

	@Column(name = "destination_address", columnDefinition = "varchar")
	private String destinationAddress;

	@Column(name = "distance", columnDefinition = "double")
	private Double distance;

	@Column(name = "offer_price", columnDefinition = "int")
	private int offerPrice;

	@Column(name = "user_price", columnDefinition = "int")
	private int userPrice;

	@Column(name = "time_of_arrival", columnDefinition = "int")
	private int timeOfArrival;

	@Column(name = "created_at", columnDefinition = "timestamp")
	private Date createdAt;

	@Column(name = "updated_at", columnDefinition = "timestamp")
	private Date updatedAt;

	@Column(name = "state", columnDefinition = "int")
	private OmartType.BookCarState state;

	@Column(name = "place_name_origin", columnDefinition = "varchar")
	private String placeNameOrigin;

	@Column(name = "place_name_destination", columnDefinition = "varchar")
	private String placeNameDestination;

	@Column(name = "phone", columnDefinition = "varchar")
	private String phone;

	@Column(name = "user_name", columnDefinition = "varchar")
	private String userName;

	@Column(name = "avatar", columnDefinition = "text")
	private String avatar;
	
	@ManyToOne
	@JoinColumn(name = "province", referencedColumnName = "id", columnDefinition = "int")
	private Province province;
	
	@ManyToOne
	@JoinColumn(name = "district", referencedColumnName = "id", columnDefinition = "int")
	private District district;

	@OneToOne(mappedBy = "bookcar", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
	PoiNotification poiNotification;
	
	// migration
	@Column(name = "service_type", columnDefinition = "int")
	private int serviceType;
	
	@Column(name = "order_id", columnDefinition = "int")
	private Long orderId;
	
	@Column(name = "poi_id", columnDefinition = "int")
	private Long poiId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Double getOriginLatitude() {
		return originLatitude;
	}

	public void setOriginLatitude(Double originLatitude) {
		this.originLatitude = originLatitude;
	}

	public Double getOriginLongitude() {
		return originLongitude;
	}

	public void setOriginLongitude(Double originLongitude) {
		this.originLongitude = originLongitude;
	}

	public Double getDestinationLatitude() {
		return destinationLatitude;
	}

	public void setDestinationLatitude(Double destinationLatitude) {
		this.destinationLatitude = destinationLatitude;
	}

	public Double getDestinationLongitude() {
		return destinationLongitude;
	}

	public void setDestinationLongitude(Double destinationLongitude) {
		this.destinationLongitude = destinationLongitude;
	}

	public String getOriginAddress() {
		return originAddress;
	}

	public void setOriginAddress(String originAddress) {
		this.originAddress = originAddress;
	}

	public String getDestinationAddress() {
		return destinationAddress;
	}

	public void setDestinationAddress(String destinationAddress) {
		this.destinationAddress = destinationAddress;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	public int getOfferPrice() {
		return offerPrice;
	}

	public void setOfferPrice(int offerPrice) {
		this.offerPrice = offerPrice;
	}

	public int getUserPrice() {
		return userPrice;
	}

	public void setUserPrice(int userPrice) {
		this.userPrice = userPrice;
	}

	public int getTimeOfArrival() {
		return timeOfArrival;
	}

	public void setTimeOfArrival(int timeOfArrival) {
		this.timeOfArrival = timeOfArrival;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public OmartType.BookCarState getState() {
		return state;
	}

	public void setState(OmartType.BookCarState state) {
		this.state = state;
	}

	public String getPlaceNameOrigin() {
		return placeNameOrigin;
	}

	public void setPlaceNameOrigin(String placeNameOrigin) {
		this.placeNameOrigin = placeNameOrigin;
	}

	public String getPlaceNameDestination() {
		return placeNameDestination;
	}

	public void setPlaceNameDestination(String placeNameDestination) {
		this.placeNameDestination = placeNameDestination;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public PoiNotification getPoiNotification() {
		return poiNotification;
	}

	public void setPoiNotification(PoiNotification poiNotification) {
		this.poiNotification = poiNotification;
	}

	public Province getProvince() {
		return province;
	}

	public void setProvince(Province province) {
		this.province = province;
	}

	public District getDistrict() {
		return district;
	}

	public void setDistrict(District district) {
		this.district = district;
	}

	public CarType getCarType() {
		return carType;
	}

	public void setCarType(CarType carType) {
		this.carType = carType;
	}

	public int getServiceType() {
		return serviceType;
	}

	public void setServiceType(int serviceType) {
		this.serviceType = serviceType;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getPoiId() {
		return poiId;
	}

	public void setPoiId(Long poiId) {
		this.poiId = poiId;
	}

}