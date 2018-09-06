package vn.com.omart.backend.domain.model;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.springframework.data.repository.query.Param;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
	
	public List<Order> findByUserProfileAndPoiAndIsDeleted(UserProfile userProfile,PointOfInterest poi,boolean isDeleted,Pageable pageable);

	public List<Order> findByUserProfileAndIsDeleted(UserProfile userProfile,boolean isDeleted,Pageable pageable);

	public List<Order> findAllByPoiAndIsDeletedOrderByCreatedAtDesc(PointOfInterest poi, boolean isDeleted, Pageable pageable);

	public Page<Order> getAllByPoiAndIsDeletedOrderByCreatedAtDesc(PointOfInterest poi, boolean isDeleted, Pageable pageable);

	public List<Order> findAllByPoiAndIsDeletedAndSellerStateOrderByCreatedAtDesc(PointOfInterest poi, boolean isDeleted, int sellerState, Pageable pageable);

	@Query(nativeQuery = true, value = "select * from omart_db.omart_order where is_deleted = 0 AND poi_id IN :ids AND seller_state = :sellerState ORDER BY created_at DESC \n-- #pageable\n",
			countQuery = "select COUNT(1) from omart_db.omart_order where is_deleted = 0 AND poi_id IN :ids AND seller_state = :sellerState")
	public Page<Order> getAllByPoiAndIsDeletedAndSellerStateOrderByCreatedAtDesc(@Param("ids") long[] ids,
			@Param("sellerState") int sellerState, Pageable pageable);

    @Query(nativeQuery = true, value = "select count(1) as `count` from omart_db.omart_order where is_deleted = 0 AND poi_id = :poiId")
    Long getTotalOrderByPoi(@Param("poiId") Long poiId);
 
    @Query("SELECT new vn.com.omart.backend.domain.model.Order(O.userProfile, COUNT(O.userId), MAX(O.createdAt)) FROM Order O WHERE O.isDeleted = false And O.poi = :poi GROUP BY O.userProfile")
    public List<Order> getOrderByPoiAndSumQuantity(@Param("poi") PointOfInterest poi, Pageable pageable);

    @Query(nativeQuery = true, value = "select seller_state as `state`, count(1) as `allOrder` from omart_db.omart_order "
    		+ " where is_deleted = 0 AND poi_id = :poiId group by seller_state")
    List<Object[]> getAllReportByPoi(@Param("poiId") Long poiId);

    @Query(nativeQuery = true, value = "select seller_state as `state`, count(1) as `allOrder` from omart_db.omart_order "
    		+ " where is_deleted = 0 AND poi_id = :poiId AND YEAR(created_at) = :year AND MONTH(created_at) = :month group by seller_state")
    List<Object[]> getMonthReportByPoi(@Param("poiId") Long poiId, @Param("year") int year, @Param("month") int month);

    @Query(nativeQuery = true, value = "select seller_state as `state`, count(1) as `allOrder` from omart_db.omart_order "
    		+ " where is_deleted = 0 AND poi_id = :poiId AND YEAR(created_at) = :year AND MONTH(created_at) = :month AND DAY(created_at) = :day group by seller_state")
    List<Object[]> getDateReportByPoi(@Param("poiId") Long poiId, @Param("year") int year, @Param("month") int month, @Param("day") int day);
    
    @Query("SELECT o FROM Order o WHERE o.poi = :poi AND CONVERT(o.createdAt,DATE) BETWEEN :from AND :to AND o.isDeleted = false")
    public List<Order> getOrdersByPoiAndBetweenDate(@Param("poi") PointOfInterest poi,@Param("from") String from, @Param("to") String to);

}
