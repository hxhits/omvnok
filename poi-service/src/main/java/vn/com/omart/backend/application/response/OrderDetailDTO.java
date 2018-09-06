package vn.com.omart.backend.application.response;

import vn.com.omart.backend.domain.model.OrderDetail;

public class OrderDetailDTO {
	private Long id;
	private Long itemId;
	private String itemName;
	private String itemAvatar;
	private double unitPrice;
	private int quantity;
	private double totalPrice;
	private Long createdAt;
	private Long updatedAt;

	public static OrderDetail toEntity(OrderDetailDTO dto) {
		OrderDetail entity = new OrderDetail();
		entity.setItemId(dto.getItemId());
		entity.setItemName(dto.getItemName());
		entity.setItemAvatar(dto.getItemAvatar());
		entity.setUnitPrice(dto.getUnitPrice());
		entity.setQuantity(dto.getQuantity());
		entity.setTotalPrice(dto.getTotalPrice());
		return entity;
	}

	public static OrderDetailDTO toFullDTO(OrderDetail entity) {
		OrderDetailDTO dto = new OrderDetailDTO();
		dto.setId(entity.getId());
		dto.setItemId(entity.getItemId());
		dto.setItemName(entity.getItemName());
		dto.setItemAvatar(entity.getItemAvatar());
		dto.setUnitPrice(entity.getUnitPrice());
		dto.setQuantity(entity.getQuantity());
		dto.setTotalPrice(entity.getTotalPrice());
		dto.setUpdatedAt(entity.getUpdatedAt().getTime());
		dto.setCreatedAt(entity.getCreatedAt().getTime());
		return dto;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

}
