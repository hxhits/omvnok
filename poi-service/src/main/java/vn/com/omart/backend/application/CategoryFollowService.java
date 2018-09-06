package vn.com.omart.backend.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.com.omart.backend.application.response.CategoryFollowDTO;
import vn.com.omart.backend.domain.model.Category;
import vn.com.omart.backend.domain.model.CategoryFollow;
import vn.com.omart.backend.domain.model.CategoryFollowRepository;
import vn.com.omart.backend.domain.model.CategoryRepository;
import vn.com.omart.backend.domain.model.DistrictFollowRepository;

@Service
public class CategoryFollowService {

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private CategoryFollowRepository categoryFollowRepository;
	
	@Autowired
	private DistrictFollowService districtFollowService;

	/**
	 * Save.
	 * 
	 * @param userId
	 * @param dto
	 */
	@Transactional(readOnly = false)
	public void save(String userId, CategoryFollowDTO dto) {
		// Delete.
		List<CategoryFollow> categoryFollows = categoryFollowRepository.findByUserId(userId);
		if (!categoryFollows.isEmpty()) {
			categoryFollowRepository.delete(categoryFollows);
		}
		if (!dto.getCategoriesSelectedStr().isEmpty()) {
			List<CategoryFollow> entityList = new ArrayList<>();
			// get category id from string.
			String[] catIds = dto.getCategoriesSelectedStr().split(",");
			for (String id : catIds) {
				CategoryFollow entity = new CategoryFollow();
				Category category = categoryRepository.findOne(Long.parseLong(id.trim()));
				entity.setCategory(category);
				entity.setUserId(userId);
				entityList.add(entity);
			}
			// Insert new.
			categoryFollowRepository.save(entityList);
			String provinceDistrictSelectedStr = dto.getProvinceDistrictSelectedStr();
			districtFollowService.save(userId, provinceDistrictSelectedStr);
		}
	}

	/**
	 * Get String Category Follow By User Id.
	 * 
	 * @param userId
	 * @return String
	 */
	public String getCategoryFollowByUserId(String userId) {
		String catIdString = "";
		List<CategoryFollow> categoryFollows = categoryFollowRepository.findByUserId(userId);
		for (CategoryFollow entity : categoryFollows) {
			catIdString += "," + entity.getCategory().id();
		}
		catIdString = catIdString.replaceFirst(",", "");
		return catIdString;
	}
}
