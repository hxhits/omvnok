package vn.com.omart.backend.domain.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "omart_order_detail")
public class OrderDetail implements Serializable {

	private static final long serialVersionUID = 6170517889545661960L;

	@Id
	@Column(name = "id", columnDefinition = "int")
	@GeneratedValue
	private Long id;

	@ManyToOne
	@JoinColumn(name = "order_id", referencedColumnName = "id", columnDefinition = "int" )
	private Order order;

	@Column(name = "item_id", columnDefinition = "int")
	private Long itemId;

	@Column(name = "item_name", columnDefinition = "varchar")
	private String itemName;

	@Column(name = "item_avatar", columnDefinition = "varchar")
	private String itemAvatar;

	@Column(name = "unit_price", columnDefinition = "double")
	private double unitPrice;

	@Column(name = "quantity", columnDefinition = "int")
	private int quantity;

	@Column(name = "total_price", columnDefinition = "double")
	private double totalPrice;

	@Column(name = "created_at", columnDefinition = "timestamp")
	private Date createdAt;
	
	@Column(name = "updated_at", columnDefinition = "timestamp")
	private Date updatedAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemAvatar() {
		return itemAvatar;
	}

	public void setItemAvatar(String itemAvatar) {
		this.itemAvatar = itemAvatar;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
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

}
