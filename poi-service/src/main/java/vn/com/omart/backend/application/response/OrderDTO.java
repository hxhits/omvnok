package vn.com.omart.backend.application.response;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;

import vn.com.omart.backend.constants.OmartType.OrderSellerState;
import vn.com.omart.backend.domain.model.DriverInfo;
import vn.com.omart.backend.domain.model.Order;
import vn.com.omart.backend.domain.model.OrderDetail;

public class OrderDTO {
	private Long id;
	private String userId;
	private String purchaseOrderId;
	private double userLatitude = 0d;
	private double userLongitude = 0d;
	private PointOfInterestDTO poi;
	@NotNull
	private Long poiId;
	private UserProfileDTO userProfile;
	private DriverInfoDTO driverProfile;
	private String shippingAddress;
	private int quantity;
	private int shipFee;
	private double subTotal;
	private double total;
	private int paymentType;
	private OrderSellerState sellerState;
	private Long createdAt;
	private Long updatedAt;
	private Long deliveryDate;
	private int deliveryPeriod;
	List<OrderDetailDTO> orderDetails;
	private String note;
	private String userCurrentAddress;
	private String receiverName;
	private String receiverPhone;
	private long totalQuantity;
	private String sellerStateReason;
	private long visitDate;
	private int coinExtra;
	//private int discount;

	public static Order toEntity(OrderDTO dto) {
		Order entity = new Order();
		entity.setShippingAddress(dto.getShippingAddress());
		entity.setQuantity(dto.getQuantity());
		entity.setShipFee(dto.getShipFee());
		entity.setSubTotal(dto.getSubTotal());
		entity.setTotal(dto.getTotal());
		entity.setPaymentType(dto.getPaymentType());
		List<OrderDetail> orderDetails = dto.getOrderDetails().stream().map(OrderDetailDTO::toEntity)
				.collect(Collectors.toList());
		entity.setOrderDetails(orderDetails);
		if (StringUtils.isNotBlank(dto.getNote())) {
			entity.setNote(dto.getNote());
		}

		if (StringUtils.isNotBlank(dto.getUserCurrentAddress())) {
			entity.setUserCurrentAddress(dto.getUserCurrentAddress());
		}
		// checking is blank for previous version can action.
		if (StringUtils.isNotBlank(dto.getReceiverName())) {
			entity.setReceiverName(dto.getReceiverName());
		}

		if (StringUtils.isNotBlank(dto.getReceiverPhone())) {
			entity.setReceiverPhone(dto.getReceiverPhone());
		}

		entity.setDeliveryPeriod(dto.getDeliveryPeriod());
		//entity.setDiscount(dto.getDiscount());
		return entity;
	}

	public static OrderDTO toBasicDTO(Order entity) {
		OrderDTO dto = new OrderDTO();
		dto.setCreatedAt(entity.getCreatedAt().getTime());
		dto.setId(entity.getId());
		if (StringUtils.isNotBlank(entity.getNote())) {
			dto.setNote(entity.getNote());
		}
		if (entity.getPoi() != null) {
			dto.setPoi(PointOfInterestDTO.toBasicDTO(entity.getPoi()));
		}
		dto.setShippingAddress(entity.getShippingAddress());
		dto.setQuantity(entity.getQuantity());
		dto.setTotal(entity.getTotal());
		dto.setPurchaseOrderId(entity.getPurchaseOrderId());
		dto.setCreatedAt(entity.getCreatedAt().getTime());
		dto.setUpdatedAt(entity.getUpdatedAt().getTime());
		if (entity.getDeliveryDate() != null) {
			dto.setDeliveryDate(entity.getDeliveryDate().getTime());
		}
		dto.setDeliveryPeriod(entity.getDeliveryPeriod());
		dto.setPaymentType(entity.getPaymentType());
		dto.setSellerState(OrderSellerState.getById(entity.getSellerState()));
		dto.setSellerStateReason(entity.getSellerStateReason());
		//dto.setDiscount(entity.getDiscount());
		return dto;
	}

	public static OrderDTO toFullDTO(Order entity) {
		OrderDTO dto = toBasicDTO(entity);
		dto.setPaymentType(entity.getPaymentType());
		dto.setShipFee(entity.getShipFee());
		dto.setSellerState(OrderSellerState.getById(entity.getSellerState()));
		dto.setSubTotal(entity.getSubTotal());
		dto.setUserLatitude(entity.getUserLatitude());
		dto.setUserLongitude(entity.getUserLongitude());
		dto.setReceiverName(entity.getReceiverName());
		dto.setReceiverPhone(entity.getReceiverPhone());
		dto.setCoinExtra(entity.getCoinExtra());
		if (entity.getUserProfile() != null) {
			dto.setUserProfile(UserProfileDTO.toBasicDTO(entity.getUserProfile()));
		}
		if (entity.getOrderDetails() != null) {
			dto.setOrderDetails(
					entity.getOrderDetails().stream().map(OrderDetailDTO::toFullDTO).collect(Collectors.toList()));
		}
		if(entity.getDriverInfo() != null) {
			DriverInfoDTO driverProfileDTO = new DriverInfoDTO();
			driverProfileDTO.setUserId(entity.getDriverInfo().getUserId());
			driverProfileDTO.setFullName(entity.getDriverInfo().getFullName());
			driverProfileDTO.setPhoneNumber(entity.getDriverInfo().getPhoneNumber());
			dto.setDriverProfile(driverProfileDTO);
		}
		return dto;
	}

	public static OrderDTO toBasicDTOWithoutUserProfile(Order entity) {
		OrderDTO dto = toBasicDTO(entity);
		if (entity.getOrderDetails() != null) {
			dto.setOrderDetails(
					entity.getOrderDetails().stream().map(OrderDetailDTO::toFullDTO).collect(Collectors.toList()));
			dto.setReceiverName(entity.getReceiverName());
			dto.setReceiverPhone(entity.getReceiverPhone());
			dto.setCoinExtra(entity.getCoinExtra());
			if(entity.getDriverInfo() != null) {
				DriverInfoDTO driverProfileDTO = new DriverInfoDTO();
				driverProfileDTO.setUserId(entity.getDriverInfo().getUserId());
				driverProfileDTO.setFullName(entity.getDriverInfo().getFullName());
				driverProfileDTO.setPhoneNumber(entity.getDriverInfo().getPhoneNumber());
				dto.setDriverProfile(driverProfileDTO);
			}
		}
		return dto;
	}

	public long getTotalQuantity() {
		return totalQuantity;
	}

	public void setTotalQuantity(long totalQuantity) {
		this.totalQuantity = totalQuantity;
	}

	public long getVisitDate() {
		return visitDate;
	}

	public void setVisitDate(long visitDate) {
		this.visitDate = visitDate;
	}

	public Long getPoiId() {
		return poiId;
	}

	public void setPoiId(Long poiId) {
		this.poiId = poiId;
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

	public double getUserLatitude() {
		return userLatitude;
	}

	public void setUserLatitude(double userLatitude) {
		this.userLatitude = userLatitude;
	}

	public double getUserLongitude() {
		return userLongitude;
	}

	public void setUserLongitude(double userLongitude) {
		this.userLongitude = userLongitude;
	}

	public PointOfInterestDTO getPoi() {
		return poi;
	}

	public void setPoi(PointOfInterestDTO poi) {
		this.poi = poi;
	}

	public String getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(String shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getShipFee() {
		return shipFee;
	}

	public void setShipFee(int shipFee) {
		this.shipFee = shipFee;
	}

	public double getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(double subTotal) {
		this.subTotal = subTotal;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public int getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(int paymentType) {
		this.paymentType = paymentType;
	}

	public Long getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Long createdAt) {
		this.createdAt = createdAt;
	}

	public Long getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Long updatedAt) {
		this.updatedAt = updatedAt;
	}

	public List<OrderDetailDTO> getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(List<OrderDetailDTO> orderDetails) {
		this.orderDetails = orderDetails;
	}

	public UserProfileDTO getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(UserProfileDTO userProfile) {
		this.userProfile = userProfile;
	}

	public String getPurchaseOrderId() {
		return purchaseOrderId;
	}

	public void setPurchaseOrderId(String purchaseOrderId) {
		this.purchaseOrderId = purchaseOrderId;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public OrderSellerState getSellerState() {
		return sellerState;
	}

	public void setSellerState(OrderSellerState sellerState) {
		this.sellerState = sellerState;
	}

	public Long getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Long deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public int getDeliveryPeriod() {
		return deliveryPeriod;
	}

	public void setDeliveryPeriod(int deliveryPeriod) {
		this.deliveryPeriod = deliveryPeriod;
	}

	public String getUserCurrentAddress() {
		return userCurrentAddress;
	}

	public void setUserCurrentAddress(String userCurrentAddress) {
		this.userCurrentAddress = userCurrentAddress;
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

	public String getSellerStateReason() {
		return sellerStateReason;
	}

	public void setSellerStateReason(String sellerStateReason) {
		this.sellerStateReason = sellerStateReason;
	}

	public DriverInfoDTO getDriverProfile() {
		return driverProfile;
	}

	public void setDriverProfile(DriverInfoDTO driverProfile) {
		this.driverProfile = driverProfile;
	}

	public int getCoinExtra() {
		return coinExtra;
	}

	public void setCoinExtra(int coinExtra) {
		this.coinExtra = coinExtra;
	}
}
