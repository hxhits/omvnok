package vn.com.omart.backend.domain.model;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.com.omart.backend.application.response.PointOfInterestDTO;

@Repository
public interface PointOfInterestRepository extends PagingAndSortingRepository<PointOfInterest, Long> {

	List<PointOfInterest> findByCategoryId(Long categoryId);

	PointOfInterest findByIdAndCategoryId(Long id, Long categoryId);

	PointOfInterest findByIdAndCategoryIdAndIsDeleted(Long id, Long categoryId, boolean isDeleted);

	@Query(nativeQuery = true)
	List<Object[]> findNearest(@Param("categoryId") long categoryId, @Param("latpoint") double latpoint,
			@Param("longpoint") double longpoint, @Param("radius") double radius, @Param("distance") double distance,
			@Param("province") String province, @Param("district") String district, @Param("ward") String ward);

	@Query(nativeQuery = true)
	List<Object[]> findNearestByPoiId(@Param("ids") String ids, @Param("latpoint") double latpoint,
			@Param("longpoint") double longpoint, @Param("radius") double radius, @Param("distance") double distance,
			@Param("province") String province, @Param("district") String district, @Param("ward") String ward);

	@Query(nativeQuery = true, value = "SELECT * FROM `omart_db`.`omart_point_of_interest` p " + " WHERE 1 = 1 "
			+ "   AND p.cat_id = :categoryId " + "   AND (:province = 'all' OR p.province = :province) "
			+ "   AND (:district = 'all' OR p.district = :district) "
			+ "   AND (:ward     = 'all' OR p.ward     = :ward) " + "   AND id > :lastId AND p.is_deleted = 0 AND p.is_approved = 1 " + " ORDER BY id ASC "
			+ " LIMIT :limit  ")
	List<PointOfInterest> findAllByCategoryId(@Param("categoryId") Long categoryId, @Param("limit") Long limit,
			@Param("lastId") Long lastId, @Param("province") String province, @Param("district") String district,
			@Param("ward") String ward);

	@Query(nativeQuery = true, value = " SELECT z.*, "
			+ "        CALC_DISTANCE(10.808061, 106.645882, z.latitude, z.longitude, 'km') AS distance_in_km "
			+ "     FROM omart_point_of_interest AS z " + "     WHERE z.is_deleted = false and z.is_approved = true and cat_id IN ( " + "          SELECT id "
			+ "          FROM omart_category " + "          WHERE keywords LIKE :categoryKeywords " + "        )   "
			+ " ORDER BY distance_in_km LIMIT :maxResults ")
	List<PointOfInterest> findShopByCategory(@Param("categoryKeywords") String categoryKeywords,
			@Param("maxResults") int maxResults);

	List<PointOfInterest> findByCreatedBy(String creator);

	PointOfInterest findByIdAndCreatedBy(Long id, String creator);

	PointOfInterest findByIdAndIsDeleted(Long id, boolean isDeleted);
	
	PointOfInterest findByIdAndIsDeletedAndIsApproved(Long id, boolean isDeleted,boolean isApproved);
	
	List<PointOfInterest> findTop10ByCategoryAndBannerIsApproveAndIsDeletedAndIsApproved(Category category,int bannerIsApprove, boolean isDeleted,boolean isApproved);

	List<PointOfInterest> findAllByOwnerId(String ownerId);

	List<PointOfInterest> findAllByOwnerIdAndIsDeleted(String ownerId, boolean isDeleted);
	
	List<PointOfInterest> findAllByOwnerIdAndIsDeletedAndIsApproved(String ownerId, boolean isDeleted,boolean isApproved);

	PointOfInterest findTop1ByOwnerId(String ownerId);

	List<PointOfInterest> findAllByNameLike(String text);

	List<PointOfInterest> findAllByBannerImagesIsNotNullOrderByUpdatedAtDesc();

	Page<PointOfInterest> findAllByBannerImagesIsNotNullOrderByUpdatedAtDesc(Pageable pageRequest);

	List<PointOfInterest> findByOwnerId(String ownerId);

	List<PointOfInterest> findAllByOrderByCreatedAtDesc(Pageable pageable);

	List<PointOfInterest> findAllByIsDeletedOrderByCreatedAtDesc(boolean isDeleted, Pageable pageable);

	@Query("SELECT new vn.com.omart.backend.application.response.PointOfInterestDTO(p.ownerId,p.id) FROM PointOfInterest p WHERE p.id IN (:id)")
	List<PointOfInterestDTO> getPointOfInterest(@Param("id") Long[] id);

	@Query("SELECT p FROM PointOfInterest p WHERE (:isIgnore = true OR p.category.id IN (:categoryIds)) AND is_deleted = false ORDER BY p.createdAt DESC")
	List<PointOfInterest> getAllByOrderByCreatedAtDesc(@Param("isIgnore") boolean isIgnore,
			@Param("categoryIds") Long[] categoryIds, Pageable pageable);

	@Query("SELECT p FROM PointOfInterest p WHERE p.id IN (:id) AND p.isDeleted = false AND p.isApproved = true")
	List<PointOfInterest> getByInIds(@Param("id") Long[] id, Pageable pageable);

	@Query(value = "SELECT distinct p.id as id ,p.name as name,p.description as description,p.avatar_image ,p.cover_image as coverImage,p.own_id as ownerId,p.open_hour as openHour, p.close_hour as closeHour,p.opening_state as isOpening,p.poi_state as poiState, p.career as career,0 AS distance, p.last_notification as lastNotification	,p.display_state as displayState FROM     omart_db.omart_point_of_interest p LEFT JOIN omart_db.omart_recruitment r ON p.id = r.poi_id     WHERE (:categoryIds = 'ALL' OR (FIND_IN_SET(p.cat_id,:categoryIds) OR FIND_IN_SET(r.position_id,:categoryIds))) 	AND (:districtIds = 'ALL' OR FIND_IN_SET(p.district,:districtIds))     AND (:provinceId = 0 OR p.province = :provinceId) 	AND p.is_deleted = false 	AND (r.is_deleted = 0 OR r.is_deleted IS NULL) 	AND (r.state = 0 OR r.state IS NULL \n#pageable\n) ", nativeQuery = true)
	List<Object[]> getAllByCategoryAndProvinceAndDistrict(@Param("categoryIds") String categoryIds, @Param("provinceId") int provinceId,
			@Param("districtIds") String districtIds,Pageable pageable);
	
	List<PointOfInterest> findAllByIsApprovedAndIsDeleted(boolean isApproved,boolean isDeleted);
	
	@Query("SELECT p FROM PointOfInterest p WHERE CONVERT(p.createdAt,DATE) BETWEEN :from AND :to Order By p.createdAt DESC")
	List<PointOfInterest> getByCreatedAtBetween(@Param("from") String from, @Param("to") String to ,Pageable pageable);
	
	Page<PointOfInterest> findAllByIsDeleted(boolean isDeleted,Pageable pageable);
	
	//List<PointOfInterest> findByPhoneContaining(String phone,Pageable pageable);
	
	Page<PointOfInterest> findByPhoneContaining(String phone,Pageable pageable);
	
	List<PointOfInterest> findByProvince(Province province,Pageable pageable);
	
	@Query("SELECT p FROM PointOfInterest p WHERE (:isIgnoreDate = true OR (CONVERT(p.createdAt,DATE) BETWEEN :from AND :to)) AND (:isIgnoreProvince = true OR (p.province = :province)) Order By p.createdAt DESC")
	Page<PointOfInterest> getByCreatedAtBetweenAndProvince(@Param("isIgnoreDate") boolean  isIgnoreDate ,@Param("from") String from, @Param("to") String to ,@Param("isIgnoreProvince") boolean  isIgnoreProvince ,@Param("province") Province province ,Pageable pageable);

}
