package vn.com.omart.backend.domain.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PushNotificationTokenNewRepository extends JpaRepository<PushNotificationTokenNew, PushNotificationTokenId>{
	
	List<PushNotificationTokenNew> findByIdUserIdAndClient(String userId,int client);
	
	List<PushNotificationTokenNew> findByIdUserId(String userId);

	@Query(value = "SELECT distinct p.user_id, p.token, p.client FROM push_notification_token_new p inner join omart_recruitment_district_follow d on p.user_id = d.user_id inner join omart_recruitment_position_follow c on c.user_id = d.user_id where c.position_id = :posId and FIND_IN_SET(d.province,:provinces) and FIND_IN_SET(d.district,:districts)", nativeQuery = true)
	List<Object[]> getByPosition(@Param("posId") Long posId, @Param("provinces") String provinces,
			@Param("districts") String districts);
	
	@Query("SELECT DISTINCT p FROM PushNotificationTokenNew p WHERE p.id.userId IN (:userIds)")
	List<PushNotificationTokenNew> getByInUserIds(@Param("userIds") List<String> userIds);
	
	@Query(value = "SELECT distinct p.user_id, p.token, p.client FROM omart_db.omart_poi_action a inner join omart_db.push_notification_token_new p on a.user_id = p.user_id where a.action_type = 1 and a.poi_id = :poiId", nativeQuery = true)
	List<Object[]> getBasePoiAction(@Param("poiId") Long poiId);
}
