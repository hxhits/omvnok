package vn.com.omart.backend.application.response;

import vn.com.omart.backend.domain.model.ItemGroup;

public class ItemGroupDTO {

	private ItemDTO item;

	public static ItemDTO toImageDTO(ItemGroup entity) {
		ItemDTO dto = new ItemDTO();
		dto = ItemDTO.from(entity.getItem());
		return dto;
	}

	public ItemDTO getItem() {
		return item;
	}

	public void setItem(ItemDTO item) {
		this.item = item;
	}

}
