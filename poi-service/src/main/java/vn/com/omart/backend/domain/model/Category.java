package vn.com.omart.backend.domain.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import lombok.ToString;

@Entity
@Table(name = "omart_category")
@ToString
public class Category {

	@Id
	@Column(name = "`id`", columnDefinition = "int")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Transient
	@Column(name = "`parent_id`", columnDefinition = "int")
	private Long parentId;

	@Column(name = "`name`", columnDefinition = "varchar")
	private String name;
	
	@Column(name = "`name_en`", columnDefinition = "varchar")
	private String nameEn;

	@Column(name = "`image`", columnDefinition = "text")
	private String image;

	@Column(name = "`title_color`", columnDefinition = "varchar")
	private String titleColor;

	@Column(name = "`background_color`", columnDefinition = "varchar")
	private String backgroundColor;

	@Column(name = "`description`", columnDefinition = "text")
	private String description;
	
	@Column(name = "`description_en`", columnDefinition = "text")
	private String descriptionEn;

	@Column(name = "`sub_description`", columnDefinition = "text")
	private String subDescription;
	
	@Column(name = "`sub_description_en`", columnDefinition = "text")
	private String subDescriptionEn;

	@Column(name = "`order`", columnDefinition = "int")
	private Long order;
	
	@Column(name = "`unit_price`", columnDefinition = "int")
	private Long unitPrice;

	@Column(name = "`keywords`", columnDefinition = "varchar")
	private String keywords;

	@Column(name = "is_disable", columnDefinition = "int")
	private String isDisable;

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "parent_id")
	private Category parent;

	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Category> children = new HashSet<>();
	
	//@OneToMany(mappedBy = "position", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
	//@Transient
	//List<Recruitment> recruitments;
	
	@OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
	List<CategoryFollow> categoryFollows;
	
	@OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
	List<PoiNotification> poiNotifications;
	
//	@OneToMany(mappedBy = "carType", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
//	List<BookCar> bookCars;
	
	public Category() {

	}

	// Create Group
	public Category(String name, String keywords, String description, Long order) {

		this.name = name;

		if (StringUtils.isNotBlank(keywords)) {
			this.keywords = keywords;
		}

		this.description = description;
	}

	// Create Element
	public Category(Category parent, String name, String keywords, String image, String titleColor,
			String backgroundColor, Long order) {
		this.parent = parent;
		this.name = name;

		if (StringUtils.isNotBlank(keywords)) {
			this.keywords = keywords;
		}

		this.image = image;
		this.titleColor = titleColor;
		this.backgroundColor = backgroundColor;
		this.order = order;
	}

	public Category(String name, String keywords, String image, String description) {
		this(name, keywords, image, null, null, description, null, null);
	}

	public Category(String name, String keywords, String image, String titleColor, String backgroundColor,
			String description, Long order, Category parent) {
		this.name = name;

		if (StringUtils.isNotBlank(keywords)) {
			this.keywords = keywords;
		}

		this.image = image;
		this.titleColor = titleColor;
		this.backgroundColor = backgroundColor;
		this.description = description;
		this.order = order;
		this.parent = parent;
	}

	public void update(String name, String keywords, String image, String titleColor, String backgroundColor,
			String description, Long order, Category parent) {
		this.name = name;

		if (StringUtils.isNotBlank(keywords)) {
			this.keywords = keywords;
		}

		this.image = image;
		this.titleColor = titleColor;
		this.backgroundColor = backgroundColor;
		this.description = description;
		this.order = order;
		this.parent = parent;
	}

	public Category update(String name, String keywords, String imageUrl) {
		this.name = name;

		if (StringUtils.isNotBlank(keywords)) {
			this.keywords = keywords;
		}

		this.image = imageUrl;
		return this;
	}

	// GETTERS
	public long id() {
		return id;
	}

	public String name() {
		return name;
	}

	public String keywords() {
		return keywords;
	}

	public String image() {
		return image;
	}

	public Long order() {
		return order;
	}

	public Set<Category> children() {
		return children;
	}

	public String titleColor() {
		return titleColor;
	}

	public String backgroundColor() {
		return backgroundColor;
	}

	public String description() {
		return description;
	}

	public String subDescription() {
		return subDescription;
	}

	public String getNameEn() {
		return nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	public String getDescriptionEn() {
		return descriptionEn;
	}

	public void setDescriptionEn(String descriptionEn) {
		this.descriptionEn = descriptionEn;
	}

	public String getSubDescriptionEn() {
		return subDescriptionEn;
	}

	public void setSubDescriptionEn(String subDescriptionEn) {
		this.subDescriptionEn = subDescriptionEn;
	}

	//public List<Recruitment> getRecruitments() {
	//	return recruitments;
	//}

	//public void setRecruitments(List<Recruitment> recruitments) {
	//	this.recruitments = recruitments;
	//}

	public List<CategoryFollow> getCategoryFollows() {
		return categoryFollows;
	}

	public void setCategoryFollows(List<CategoryFollow> categoryFollows) {
		this.categoryFollows = categoryFollows;
	}

	public List<PoiNotification> getPoiNotifications() {
		return poiNotifications;
	}

	public void setPoiNotifications(List<PoiNotification> poiNotifications) {
		this.poiNotifications = poiNotifications;
	}

//	public List<BookCar> getBookCars() {
//		return bookCars;
//	}
//
//	public void setBookCars(List<BookCar> bookCars) {
//		this.bookCars = bookCars;
//	}

	public Long getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Long unitPrice) {
		this.unitPrice = unitPrice;
	}
	
	public String getIsDisable() {
		return this.isDisable;
	}
}
