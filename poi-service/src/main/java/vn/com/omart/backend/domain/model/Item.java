package vn.com.omart.backend.domain.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.vdurmont.emoji.EmojiParser;

import lombok.Builder;
import lombok.ToString;
import vn.com.omart.backend.port.adapter.support.mysql.JpaImageConverter;

@Entity
@Table(name = "omart_poi_item")
@ToString
@Builder
public class Item {

	@ManyToOne
	@JoinColumn(name = "poi_id", referencedColumnName = "id", columnDefinition = "int")
	private PointOfInterest poi;

	@Id
	@Column(name = "id", columnDefinition = "int")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name", columnDefinition = "varchar")
	private String name;

	@Column(name = "description", columnDefinition = "text")
	private String description;

	@Column(name = "unit_price", columnDefinition = "DOUBLE")
	private double unitPrice;

	@Column(name = "type", columnDefinition = "text")
	private String type;

	@Column(name = "avatar_image", columnDefinition = "json")
	@Convert(converter = JpaImageConverter.class)
	private List<Image> avatarImage;

	@Column(name = "cover_image", columnDefinition = "json")
	@Convert(converter = JpaImageConverter.class)
	private List<Image> coverImage;

	@Column(name = "featured_image", columnDefinition = "json")
	@Convert(converter = JpaImageConverter.class)
	private List<Image> featuredImage;

	@Column(name = "out_of_stock", columnDefinition = "int")
	private boolean isOutOfStock;

	@Column(name = "created_by", columnDefinition = "varchar")
	private String createdBy;

	@Column(name = "created_at", columnDefinition = "TIMESTAMP")
	private Date createdAt;

	@Column(name = "updated_by", columnDefinition = "varchar")
	private String updatedBy;

	@Column(name = "updated_at", columnDefinition = "TIMESTAMP")
	private Date updatedAt;

	@OneToMany(mappedBy = "item", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
	private List<ItemGroup> itemGroups;
	
	@Column(name = "posted_at", columnDefinition = "TIMESTAMP")
	private Date postedAt;

	@Column(name = "cat_id", columnDefinition = "int")
	private Long catId;

	@Column(name = "is_posted", columnDefinition = "bit")
	private boolean isPosted;


	public Item() {
	}

	public Item(PointOfInterest poi, Long id, String name, String description, double unitPrice, String type,
			List<Image> avatarImage, List<Image> coverImage, List<Image> featuredImage, boolean isOutOfStock,
			String createdBy, Date createdAt, String updatedBy, Date updatedAt, List<ItemGroup> itemGroups,
			Date postedAt, Long catId, boolean isPosted) {
		this.poi = poi;
		this.id = id;
		this.name = name;
		this.description = description;
		this.unitPrice = unitPrice;
		this.type = type;
		this.avatarImage = avatarImage;
		this.coverImage = coverImage;
		this.featuredImage = featuredImage;
		this.isOutOfStock = isOutOfStock;
		this.createdBy = createdBy;
		this.createdAt = createdAt;
		this.updatedBy = updatedBy;
		this.updatedAt = updatedAt;
		this.itemGroups = itemGroups;
		this.postedAt = postedAt;
		this.catId = catId;
		this.isPosted = isPosted;
	}

	public Long id() {
		return id;
	}

	public String name() {
		return name;
	}

	public String description() {
		return description;
	}

	public double unitPrice() {
		return unitPrice;
	}

	public String type() {
		return type;
	}

	public List<Image> avatarImage() {
		return avatarImage;
	}

	public List<Image> coverImage() {
		return coverImage;
	}

	public List<Image> featuredImage() {
		return featuredImage;
	}

	public String createdBy() {
		return createdBy;
	}

	public Date createdAt() {
		return createdAt;
	}

	public String updatedBy() {
		return updatedBy;
	}

	public Date updatedAt() {
		return updatedAt;
	}

	public void setDescription(String description) {
		if (description != null) {
			this.description = EmojiParser.parseToAliases(description);
		}
	}

	public String getDescription() {
		if (description != null) {
			return EmojiParser.parseToUnicode(description);
		}
		return null;
	}

	public boolean isOutOfStock() {
		return isOutOfStock;
	}

	public void setOutOfStock(boolean isOutOfStock) {
		this.isOutOfStock = isOutOfStock;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public List<ItemGroup> getItemGroups() {
		return itemGroups;
	}

	public void setItemGroups(List<ItemGroup> itemGroups) {
		this.itemGroups = itemGroups;
	}

	public Date getPostedAt() {
		return postedAt;
	}

	public void setPostedAt(Date postedAt) {
		this.postedAt = postedAt;
	}

	public Long getCatId() {
		return catId;
	}

	public void setCatId(Long catId) {
		this.catId = catId;
	}

	public boolean isPosted() {
		return isPosted;
	}

	public void setPosted(boolean isPosted) {
		this.isPosted = isPosted;
	}

	public PointOfInterest getPoi() {
		return poi;
	}

	public void setPoi(PointOfInterest poi) {
		this.poi = poi;
	}
}
