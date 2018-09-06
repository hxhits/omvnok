package vn.com.omart.driver.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "district")
public class District {
	@Id
	@Column(name = "id", columnDefinition = "int")
	private Long id;

	@Column(name = "name", columnDefinition = "varchar")
	private String name;

	@Column(name = "province_id", columnDefinition = "int")
	private Long provinceId;

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

	public Long getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Long provinceId) {
		this.provinceId = provinceId;
	}

}
