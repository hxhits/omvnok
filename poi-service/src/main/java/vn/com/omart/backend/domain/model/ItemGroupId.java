package vn.com.omart.backend.domain.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ItemGroupId implements Serializable {

	private static final long serialVersionUID = -1280768623412560612L;

	@Column(name = "item_id", columnDefinition = "int")
	private Long itemId;

	@Column(name = "group_id", columnDefinition = "int")
	private Long groupId;

	public ItemGroupId() {
	}

	public ItemGroupId(Long itemId, Long groupId) {
		super();
		this.itemId = itemId;
		this.groupId = groupId;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
}