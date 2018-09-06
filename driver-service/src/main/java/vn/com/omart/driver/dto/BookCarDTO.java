package vn.com.omart.driver.dto;

import java.util.Date;

import vn.com.omart.driver.common.constant.DriverType;
import vn.com.omart.driver.common.constant.DriverType.BookCarState;

public class BookCarDTO {

	private Long id;
	private Long carType;
	private String userId;
	private Double originLatitude;
	private Double originLongitude;
	private Double destinationLatitude;
	private Double destinationLongitude;
	private String originAddress;
	private String destinationAddress;
	private Double distance;
	private int offerPrice;
	private int userPrice;
	private int timeOfArrival;
	private Date createdAt;
	private Date updatedAt;
	private String placeNameOrigin;
	private String placeNameDestination;
	private String phone;
	private String userName;
	private String avatar;
	private Long province;
	private Long district;
	private BookCarState state;
	private String receiverName;
	private String receiverPhone;
	private Long orderId;
	private Long poiId;

	public DriverType.BookCarState getState() {
		return state;
	}

	public void setState(DriverType.BookCarState state) {
		this.state = state;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Long getCarType() {
		return carType;
	}

	public void setCarType(Long carType) {
		this.carType = carType;
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
