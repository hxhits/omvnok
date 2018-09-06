package vn.com.omart.backend.application.response;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import vn.com.omart.backend.constants.OmartType;
import vn.com.omart.backend.domain.model.BookCar;

public class BookCarDTO {

	private Long id;
	private Long carType;
	private String userId;
	private CategoryDTO carTypeDTO;
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
	private OmartType.BookCarState state;
	private String receiverName;
	private String receiverPhone;
	private Long orderId;
	private Long poiId;

	public BookCarDTO() {
	}

	public BookCarDTO(Long id) {
		this.id = id;
	}

	public static BookCarDTO toDTO(BookCar entity) {
		BookCarDTO dto = new BookCarDTO();
		dto.setId(entity.getId());
		// dto.setCarType(entity.getCarType().id());
		dto.setCreatedAt(entity.getCreatedAt());
		dto.setUpdatedAt(entity.getUpdatedAt());
		dto.setDestinationAddress(entity.getDestinationAddress());
		dto.setOriginAddress(entity.getOriginAddress());
		dto.setDistance(entity.getDistance());
		dto.setOfferPrice(entity.getOfferPrice());
		dto.setUserPrice(entity.getUserPrice());
		dto.setUserId(entity.getUserId());
		dto.setTimeOfArrival(entity.getTimeOfArrival());
		dto.setState(entity.getState());
		dto.setPlaceNameDestination(entity.getPlaceNameDestination());
		dto.setPlaceNameOrigin(entity.getPlaceNameOrigin());
		dto.setPhone(entity.getPhone());
		dto.setOriginLatitude(entity.getOriginLatitude());
		dto.setOriginLongitude(entity.getOriginLongitude());
		dto.setDestinationLongitude(entity.getDestinationLongitude());
		dto.setDestinationLatitude(entity.getDestinationLatitude());
		if (StringUtils.isBlank(entity.getAvatar())) {
			dto.setAvatar(entity.getAvatar());
		}
		return dto;
	}

	public Long getCarType() {
		return carType;
	}

	public void setCarType(Long carType) {
		this.carType = carType;
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

	public CategoryDTO getCarTypeDTO() {
		return carTypeDTO;
	}

	public void setCarTypeDTO(CategoryDTO carTypeDTO) {
		this.carTypeDTO = carTypeDTO;
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
