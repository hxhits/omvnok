package vn.com.omart.backend.application.response;

public class CategoryFollowDTO {

	private Long id;
	private String categoriesSelectedStr;
	private String provinceDistrictSelectedStr="";

	public CategoryFollowDTO() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCategoriesSelectedStr() {
		return categoriesSelectedStr;
	}

	public void setCategoriesSelectedStr(String categoriesSelectedStr) {
		this.categoriesSelectedStr = categoriesSelectedStr;
	}

	public String getProvinceDistrictSelectedStr() {
		return provinceDistrictSelectedStr;
	}

	public void setProvinceDistrictSelectedStr(String provinceDistrictSelectedStr) {
		this.provinceDistrictSelectedStr = provinceDistrictSelectedStr;
	}
	
}
