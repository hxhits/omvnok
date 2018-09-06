package vn.com.omart.backend.domain.model;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PushNotificationTokenRepository extends PagingAndSortingRepository<PushNotificationToken, String> {

	
//	PushNotificationToken findByUserIdAndClient(String userId,int client);
	
//	PushNotificationToken findByUserId(String userId);

//	@Query(value = "SELECT pnt.user_id,pnt.token,pnt.client FROM push_notification_token pnt inner join omart_recruitment_position_follow ct on ct.user_id = pnt.user_id where ct.position_id = :posId", nativeQuery = true)
//	List<PushNotificationToken> getByPosition(@Param("posId") Long posId);

//	@Query(value = "SELECT distinct p.user_id, p.token, p.client FROM push_notification_token p inner join omart_recruitment_district_follow d on p.user_id = d.user_id inner join omart_recruitment_position_follow c on c.user_id = d.user_id where c.position_id = :posId and FIND_IN_SET(d.province,:provinces) and FIND_IN_SET(d.district,:districts)", nativeQuery = true)
//	List<PushNotificationToken> getByPosition(@Param("posId") Long posId, @Param("provinces") String provinces,
//			@Param("districts") String districts);
	
//	@Query("SELECT DISTINCT p FROM PushNotificationToken p WHERE p.userId IN (:userIds)")
//	List<PushNotificationToken> getByUserIds(@Param("userIds") List<String> userIds);
}
