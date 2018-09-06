package vn.com.omart.backend.domain.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="omart_item_group")
public class ItemGroup implements Serializable {
	
	private static final long serialVersionUID = 3284336668106125664L;
	
//	@EmbeddedId 
//	private ItemGroupId id;
//
//	public ItemGroupId getId() {
//		return id;
//	}
//
//	public void setId(ItemGroupId id) {
//		this.id = id;
//	}
	
	@Id
	@Column(name = "id", columnDefinition = "int")
	@GeneratedValue
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "group_id", referencedColumnName = "id", columnDefinition = "int")
	private PoiItemGroup group;
	
	@ManyToOne
	@JoinColumn(name = "item_id", referencedColumnName = "id", columnDefinition = "int")
	private Item item;
	
	@Column(name = "poi_id", columnDefinition = "int")
	private Long poiId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PoiItemGroup getGroup() {
		return group;
	}

	public void setGroup(PoiItemGroup group) {
		this.group = group;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Long getPoiId() {
		return poiId;
	}

	public void setPoiId(Long poiId) {
		this.poiId = poiId;
	}
	
}
