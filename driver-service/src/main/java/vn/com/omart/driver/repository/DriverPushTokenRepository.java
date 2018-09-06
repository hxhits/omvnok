package vn.com.omart.driver.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.omart.driver.entity.DriverPushToken;

@Repository
public interface DriverPushTokenRepository extends JpaRepository<DriverPushToken, String> {

	DriverPushToken findByUserId(String userId);

	@Query(value = "SELECT distinct p.user_id,p.token,p.client,p.is_disabled,p.ring_index FROM omart_db.omart_driver_push_token p "
			+ "inner join omart_db.omart_driver_dist_follow d on p.user_id = d.user_id "
			+ "inner join omart_db.omart_driver_follow c on c.user_id = d.user_id "
			+ "where c.car_type_id = :carTypeId and FIND_IN_SET(d.province,:provinces) and FIND_IN_SET(d.district,:districts) and p.is_disabled = 0 and p.is_blocked = 0",nativeQuery = true)
	public List<Object[]> getDriverTokens(@Param("carTypeId") Long carTypeId, @Param("provinces") String provinces,
			@Param("districts") String districts);
	
	@Query("SELECT d FROM DriverPushToken d WHERE d.userId IN (:userIds) AND d.isDisabled = false AND d.isBlocked = false")
	public List<DriverPushToken> getTokenInUserIds(@Param("userIds") List<String> userIds);
}
