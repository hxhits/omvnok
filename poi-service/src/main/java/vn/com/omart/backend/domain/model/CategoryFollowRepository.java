package vn.com.omart.backend.domain.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryFollowRepository extends JpaRepository<CategoryFollow, Long> {
	
	List<CategoryFollow> findByUserId(String userId);
	
	List<CategoryFollow> findByCategory(Category category);
	
	@Query(value = "SELECT pnt.user_id,pnt.token,pnt.client FROM omart_db.push_notification_token_new pnt inner join omart_db.omart_category_follow ct on ct.user_id = pnt.user_id where ct.cat_id = :catId",nativeQuery=true)
	List<PushNotificationToken> getByCategory(@Param("catId") Long catId);
		
	@Query(value="SELECT distinct p.user_id,p.token,p.client FROM omart_db.push_notification_token_new p inner join omart_db.omart_district_follow d on p.user_id = d.user_id inner join omart_db.omart_category_follow c on c.user_id = d.user_id where c.cat_id = :catId and FIND_IN_SET(d.province,:provinces) and FIND_IN_SET(d.district,:districts)",nativeQuery=true)
	List<Object[]> getByCategory(@Param("catId") Long catId,@Param("provinces") String provinces, @Param("districts") String districts);
}
