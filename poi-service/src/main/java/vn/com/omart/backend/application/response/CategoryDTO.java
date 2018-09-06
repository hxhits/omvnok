/**
 * 
 */
package vn.com.omart.backend.application.response;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import lombok.Data;
import vn.com.omart.backend.constants.ConstantUtils;
import vn.com.omart.backend.domain.model.Category;

@Data
public class CategoryDTO {

	private long id;

	private String name;
	private String keywords;
	private String imageUrl;

	private String itemTabName;

	private String titleColor;

	private String backgroundColor;

	private String description;

	private String subDescription;

	private long order;
	
	private long unitPrice;

	private List<CategoryDTO> children;

	public static CategoryDTO from(Category model) {

		CategoryDTO dto = new CategoryDTO();

		dto.id = model.id();
		dto.name = model.name();
		dto.keywords = model.keywords();

		if (!StringUtils.isBlank(model.image())) {
			dto.imageUrl = model.image();
		}

		dto.titleColor = model.titleColor();
		dto.backgroundColor = model.backgroundColor();
		dto.description = model.description();
		dto.subDescription = model.subDescription();
		dto.order = model.order();
		dto.unitPrice = model.getUnitPrice();
		return dto;
	}

	public static CategoryDTO from(Category model, List<CategoryDTO> children) {
		CategoryDTO dto = from(model);
		dto.children = children;
		return dto;
	}
	
	public static CategoryDTO from(Category model, List<CategoryDTO> children,String language) {
		CategoryDTO dto = from(model,language);
		dto.children = children;
		return dto;
	}

	public static CategoryDTO from(Category model,String language) {
		CategoryDTO dto = new CategoryDTO();
		dto.id = model.id();
		switch(language.toLowerCase()){
			case ConstantUtils.LANGUAGE_EN: 
				dto.name = model.getNameEn();
				dto.description = model.getDescriptionEn();
				dto.subDescription = model.getSubDescriptionEn();
				break;
			default: 
				dto.name = model.name();
				dto.description = model.description();
				dto.subDescription = model.subDescription();
				break;
		}
		dto.keywords = model.keywords();
		if (!StringUtils.isBlank(model.image())) {
			dto.imageUrl = model.image();
		}
		dto.titleColor = model.titleColor();
		dto.backgroundColor = model.backgroundColor();
		dto.order = model.order();
		dto.unitPrice = model.getUnitPrice();
		return dto;
	}
}
