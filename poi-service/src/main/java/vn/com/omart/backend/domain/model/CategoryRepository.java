package vn.com.omart.backend.domain.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

	List<Category> findAllByParentIsNull();

	List<Category> findAllByParentIsNullAndIsDisableIsNull();

	List<Category> findAllByParentIsNotNull();

	List<Category> findAllByParentIsNotNullAndIsDisableIsNull();

	List<Category> findByKeywordsIgnoreCaseContainingAndIsDisableIsNull(String keywords);

	List<Category> findAllByIsDisableIsNullOrderById();

	@Query(value = "SELECT *FROM omart_db.omart_category ct where ct.parent_id = :parentId and ct.is_disable is NULL", nativeQuery = true)
	List<Category> getByParentId(@Param("parentId") Long parentId);
	
}
