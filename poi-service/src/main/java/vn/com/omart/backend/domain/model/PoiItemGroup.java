package vn.com.omart.backend.domain.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="omart_poi_item_group")
public class PoiItemGroup implements Serializable {
	
	private static final long serialVersionUID = -5905951140660946600L;
	
	@Id
	@Column(name = "id", columnDefinition = "int")
	@GeneratedValue
	private Long id;
	
	@Column(name = "name", columnDefinition = "varchar")
	private String name;
	
	@ManyToOne
	@JoinColumn(name = "poi_id", referencedColumnName = "id", columnDefinition = "int")
	private PointOfInterest poi;
	
	@Column(name = "avatar", columnDefinition = "varchar")
	private String avatar;
	
//	@ManyToMany(mappedBy = "itemGroups")
//    private List<Item> items;
	
	@OneToMany(mappedBy = "group", fetch = FetchType.LAZY, cascade = CascadeType.DETACH, orphanRemoval = false)
	private List<ItemGroup> itemGroups;
	
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

	public PointOfInterest getPoi() {
		return poi;
	}

	public void setPoi(PointOfInterest poi) {
		this.poi = poi;
	}

	public List<ItemGroup> getItemGroups() {
		return itemGroups;
	}

	public void setItemGroups(List<ItemGroup> itemGroups) {
		this.itemGroups = itemGroups;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

}
