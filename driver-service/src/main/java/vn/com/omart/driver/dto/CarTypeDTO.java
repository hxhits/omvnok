package vn.com.omart.driver.dto;

import vn.com.omart.driver.common.constant.CommonConstant;
import vn.com.omart.driver.entity.CarType;

public class CarTypeDTO {

	private Long id;
	private String name;
	private String nameEn;
	private String image;
	private Long order;
	private Long unitPrice;
	private Long unitPrice2Km;
	private String keywords;

	public static CarTypeDTO toDTO(CarType entity, String language) {
		CarTypeDTO dto = new CarTypeDTO();
		dto.setId(entity.getId());
		dto.setUnitPrice(entity.getUnitPrice());
		dto.setUnitPrice2Km(entity.getUnitPrice2Km());
		if (CommonConstant.LANGUAGE_EN.equals(language)) {
			dto.setName(entity.getNameEn());
		} else {
			dto.setName(entity.getName());
		}
		return dto;
	}

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

	public Long getUnitPrice2Km() {
		return unitPrice2Km;
	}

	public void setUnitPrice2Km(Long unitPrice2Km) {
		this.unitPrice2Km = unitPrice2Km;
	}

}
