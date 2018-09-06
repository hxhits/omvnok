package vn.com.omart.backend.application.response;

import java.util.List;

public class CategoryParentDTO {
	
	private String categoriesSelectedStr;
	private String provinceDistrictSelectedStr;
	
	private List<CategoryDTO> categories;

	public String getProvinceDistrictSelectedStr() {
		return provinceDistrictSelectedStr;
	}

	public void setProvinceDistrictSelectedStr(String provinceDistrictSelectedStr) {
		this.provinceDistrictSelectedStr = provinceDistrictSelectedStr;
	}

	public String getCategoriesSelectedStr() {
		return categoriesSelectedStr;
	}

	public void setCategoriesSelectedStr(String categoriesSelectedStr) {
		this.categoriesSelectedStr = categoriesSelectedStr;
	}

	public List<CategoryDTO> getCategories() {
		return categories;
	}

	public void setCategories(List<CategoryDTO> categories) {
		this.categories = categories;
	}

}
