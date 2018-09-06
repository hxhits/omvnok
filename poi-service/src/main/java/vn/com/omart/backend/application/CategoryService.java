package vn.com.omart.backend.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.omart.backend.application.request.CategoryCmd;
import vn.com.omart.backend.application.response.CategoryDTO;
import vn.com.omart.backend.domain.model.Category;
import vn.com.omart.backend.domain.model.CategoryRepository;
import vn.com.omart.sharedkernel.application.model.error.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

	private final CategoryRepository categoryRepository;

	@Autowired
	public CategoryService(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	public List<CategoryDTO> getSubCategories() {
		return categoryRepository.findAllByParentIsNotNull().stream().map(CategoryDTO::from)
				.collect(Collectors.toList());
	}

	public List<CategoryDTO> getSubCategories_v1() {
		return categoryRepository.findAllByParentIsNotNullAndIsDisableIsNull().stream().map(CategoryDTO::from)
				.collect(Collectors.toList());
	}

	public List<CategoryDTO> getGroupCategories() {
		return this.categoryRepository.findAllByParentIsNullAndIsDisableIsNull().stream().map(CategoryDTO::from)
				.collect(Collectors.toList());
	}

	public CategoryDTO getCategory(Long categoryId) {
		Category one = categoryRepository.findOne(categoryId);
		return CategoryDTO.from(one);
	}

	public CategoryDTO createCategoryGroup(CategoryCmd.GroupCreateOrUpdate payload) {
		Category entity = new Category(payload.getName(), payload.getKeywords(), payload.getDescription(),
				payload.getOrder());
		entity = this.categoryRepository.save(entity);
		return CategoryDTO.from(entity);
	}

	public CategoryDTO createCategory(CategoryCmd.CreateOrUpdateNew payload) {

		Category parent = this.categoryRepository.findOne(payload.getParentId());

		if (null == parent) {
			throw new NotFoundException("Category Group not found");
		}

		Category entity = new Category(parent, payload.getName(), payload.getKeywords(), payload.getImageUrl(),
				payload.getTitleColor(), payload.getBackgroundColor(), payload.getOrder());
		entity = this.categoryRepository.save(entity);
		return CategoryDTO.from(entity);
	}

}
