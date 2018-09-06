package vn.com.omart.driver.entity;

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
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonView;

import vn.com.omart.driver.common.constant.DriverType;
import vn.com.omart.driver.jsonview.BookCarView;

@Entity
@Table(name = "omart_book_car")
public class BookCar implements Serializable {

	private static final long serialVersionUID = 1453079944212754821L;

	@JsonView({ BookCarView.OveralView.class })
	@Id
	@GeneratedValue
	@Column(name = "id", columnDefinition = "int")
	private Long id;

	@Column(name = "user_id", columnDefinition = "varchar")
	@JsonView({ BookCarView.FullView.class })
	private String userId;

	@ManyToOne
	@JoinColumn(name = "car_type", referencedColumnName = "id", columnDefinition = "int")
	@JsonView({ BookCarView.OveralView.class })
	private CarType carType;

	@Column(name = "origin_latitude", columnDefinition = "double")
	@JsonView({ BookCarView.OveralView.class })
	private Double originLatitude;

	@Column(name = "origin_longitude", columnDefinition = "double")
	@JsonView({ BookCarView.OveralView.class })
	private Double originLongitude;

	@Column(name = "destination_latitude", columnDefinition = "double")
	@JsonView({ BookCarView.OveralView.class })
	private Double destinationLatitude;

	@Column(name = "destination_longitude", columnDefinition = "double")
	@JsonView({ BookCarView.OveralView.class })
	private Double destinationLongitude;

	@Column(name = "origin_address", columnDefinition = "varchar")
	@JsonView({ BookCarView.OveralView.class })
	private String originAddress;

	@Column(name = "destination_address", columnDefinition = "varchar")
	@JsonView({ BookCarView.OveralView.class })
	private String destinationAddress;

	@Column(name = "distance", columnDefinition = "double")
	@JsonView({ BookCarView.OveralView.class })
	private Double distance;

	@Column(name = "offer_price", columnDefinition = "int")
	@JsonView({ BookCarView.FullView.class })
	private int offerPrice;

	@Column(name = "user_price", columnDefinition = "int")
	@JsonView({ BookCarView.OveralView.class })
	private int userPrice;

	@Column(name = "time_of_arrival", columnDefinition = "int")
	@JsonView({ BookCarView.DetailView.class })
	private int timeOfArrival;

	@Column(name = "created_at", columnDefinition = "timestamp")
	@JsonView({ BookCarView.FullView.class })
	private Date createdAt;

	@Column(name = "updated_at", columnDefinition = "timestamp")
	@JsonView({ BookCarView.OveralView.class })
	private Date updatedAt;

	@Column(name = "state", columnDefinition = "int")
	@JsonView({ BookCarView.DetailView.class })
	private DriverType.BookCarState state;

	@Column(name = "place_name_origin", columnDefinition = "varchar")
	@JsonView({ BookCarView.DetailView.class })
	private String placeNameOrigin;

	@Column(name = "place_name_destination", columnDefinition = "varchar")
	@JsonView({ BookCarView.DetailView.class })
	private String placeNameDestination;

	@Column(name = "phone", columnDefinition = "varchar")
	@JsonView({ BookCarView.DetailView.class })
	private String phone;

	@Column(name = "user_name", columnDefinition = "varchar")
	@JsonView({ BookCarView.DetailView.class })
	private String userName;

	@Column(name = "avatar", columnDefinition = "text")
	@JsonView({ BookCarView.DetailView.class })
	private String avatar;

	@Column(name = "province", columnDefinition = "int")
	@JsonView({ BookCarView.FullView.class })
	private Long province;

	@Column(name = "district", columnDefinition = "int")
	@JsonView({ BookCarView.FullView.class })
	private Long district;

	@Column(name = "receiver_name", columnDefinition = "varchar")
	@JsonView({ BookCarView.OveralView.class })
	private String receiverName;
	
	@Column(name = "receiver_phone", columnDefinition = "varchar")
	@JsonView({ BookCarView.OveralView.class })
	private String receiverPhone;
	
	@Column(name = "service_type", columnDefinition = "int")
	@JsonView({ BookCarView.OveralView.class })
	private int serviceType;
	
	@Column(name = "order_id", columnDefinition = "int")
	@JsonView({ BookCarView.OveralView.class })
	private Long orderId;
	
	@Column(name = "poi_id", columnDefinition = "int")
	@JsonView({ BookCarView.FullView.class })
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

	public Long getProvince() {
		return province;
	}

	public void setProvince(Long province) {
		this.province = province;
	}

	public Long getDistrict() {
		return district;
	}

	public void setDistrict(Long district) {
		this.district = district;
	}

	public CarType getCarType() {
		return carType;
	}

	public void setCarType(CarType carType) {
		this.carType = carType;
	}

	public DriverType.BookCarState getState() {
		return state;
	}

	public void setState(DriverType.BookCarState state) {
		this.state = state;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getReceiverPhone() {
		return receiverPhone;
	}

	public void setReceiverPhone(String receiverPhone) {
		this.receiverPhone = receiverPhone;
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
