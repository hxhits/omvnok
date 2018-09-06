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
import javax.persistence.Transient;

import com.vdurmont.emoji.EmojiParser;

@Entity
@Table(name = "omart_order")
public class Order implements Serializable {

	private static final long serialVersionUID = 9168205601268524460L;

	@Id
	@Column(name = "id", columnDefinition = "int")
	@GeneratedValue
	private Long id;

	@Column(name = "user_id", columnDefinition = "varchar")
	private String userId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "profile_id", referencedColumnName = "id", columnDefinition = "int")
	private UserProfile userProfile;

	@Column(name = "user_latitude", columnDefinition = "double")
	private double userLatitude;

	@Column(name = "user_longitude", columnDefinition = "double")
	private double userLongitude;

	@ManyToOne
	@JoinColumn(name = "poi_id", referencedColumnName = "id", columnDefinition = "int")
	private PointOfInterest poi;

	@Column(name = "shipping_address", columnDefinition = "varchar")
	private String shippingAddress;

	@Column(name = "quantity", columnDefinition = "int")
	private int quantity;

	@Column(name = "ship_fee", columnDefinition = "int")
	private int shipFee;

	@Column(name = "sub_total", columnDefinition = "double")
	private double subTotal;

	@Column(name = "total", columnDefinition = "double")
	private double total;

	@Column(name = "payment_type", columnDefinition = "int")
	private int paymentType;

	@Column(name = "buyer_state", columnDefinition = "int")
	private int buyerState;

	@Column(name = "seller_state", columnDefinition = "int")
	private int sellerState;

	@Column(name = "created_at", columnDefinition = "timestamp")
	private Date createdAt;

	@Column(name = "updated_at", columnDefinition = "timestamp")
	private Date updatedAt;

	@Column(name = "note", columnDefinition = "text")
	private String note;

	@Column(name = "is_deleted", columnDefinition = "bit")
	private boolean isDeleted;

	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = { CascadeType.DETACH })
	List<OrderDetail> orderDetails;

	@Column(name = "purchase_order_id", columnDefinition = "varchar")
	private String purchaseOrderId;

	@Deprecated
	@Column(name = "delivery_date", columnDefinition = "timestamp")
	private Date deliveryDate;

	@Column(name = "delivery_period", columnDefinition = "int")
	private int deliveryPeriod;

	@Column(name = "user_current_address", columnDefinition = "varchar")
	private String userCurrentAddress;

	@Column(name = "receiver_name", columnDefinition = "varchar")
	private String receiverName;

	@Column(name = "receiver_phone", columnDefinition = "varchar")
	private String receiverPhone;

	@Column(name = "seller_state_reason", columnDefinition = "varchar")
	private String sellerStateReason;

	@Column(name = "bookcar_id", columnDefinition = "int")
	private Long bookcarId;

	@Column(name = "is_calc_discount", columnDefinition = "bit")
	private boolean isCalcDiscount;

	@Column(name = "coin_extra", columnDefinition = "int")
	private int coinExtra;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "driver_id")
	private DriverInfo driverInfo;

	@Transient
	private long totalQuantity;

	@Transient
	private Date visitDate;

	public Order() {
	}

	public Order(UserProfile userProfile, long totalQuantity, Date visitDate) {
		super();
		this.userProfile = userProfile;
		this.totalQuantity = totalQuantity;
		this.visitDate = visitDate;
	}

	public Date getVisitDate() {
		return visitDate;
	}

	public void setVisitDate(Date visitDate) {
		this.visitDate = visitDate;
	}

	public long getTotalQuantity() {
		return totalQuantity;
	}

	public void setTotalQuantity(long totalQuantity) {
		this.totalQuantity = totalQuantity;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public PointOfInterest getPoi() {
		return poi;
	}

	public void setPoi(PointOfInterest poi) {
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

	public int getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(int paymentType) {
		this.paymentType = paymentType;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public List<OrderDetail> getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(List<OrderDetail> orderDetails) {
		this.orderDetails = orderDetails;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public UserProfile getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}

	public String getPurchaseOrderId() {
		return purchaseOrderId;
	}

	public void setPurchaseOrderId(String purchaseOrderId) {
		this.purchaseOrderId = purchaseOrderId;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getNote() {
		if (this.note != null) {
			return EmojiParser.parseToUnicode(note);
		}
		return null;
	}

	public void setNote(String note) {
		if (note != null) {
			this.note = EmojiParser.parseToAliases(note);
		}
	}

	public int getBuyerState() {
		return buyerState;
	}

	public void setBuyerState(int buyerState) {
		this.buyerState = buyerState;
	}

	public int getSellerState() {
		return sellerState;
	}

	public void setSellerState(int sellerState) {
		this.sellerState = sellerState;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
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
		if (this.sellerStateReason != null) {
			return EmojiParser.parseToUnicode(sellerStateReason);
		}
		return null;
	}

	public void setSellerStateReason(String sellerStateReason) {
		if (sellerStateReason != null) {
			this.sellerStateReason = EmojiParser.parseToAliases(sellerStateReason);
		}
	}

	public Long getBookcarId() {
		return bookcarId;
	}

	public void setBookcarId(Long bookcarId) {
		this.bookcarId = bookcarId;
	}

	public DriverInfo getDriverInfo() {
		return driverInfo;
	}

	public void setDriverInfo(DriverInfo driverInfo) {
		this.driverInfo = driverInfo;
	}

	public boolean isCalcDiscount() {
		return isCalcDiscount;
	}

	public void setCalcDiscount(boolean isCalcDiscount) {
		this.isCalcDiscount = isCalcDiscount;
	}

	public int getCoinExtra() {
		return coinExtra;
	}

	public void setCoinExtra(int coinExtra) {
		this.coinExtra = coinExtra;
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

}
