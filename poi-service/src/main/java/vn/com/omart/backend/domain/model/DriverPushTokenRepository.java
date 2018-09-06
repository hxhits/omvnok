package vn.com.omart.backend.domain.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverPushTokenRepository extends JpaRepository<DriverPushToken, String> {

	@Query(value = "SELECT distinct p.* FROM omart_db.omart_driver_push_token p "
			+ "inner join omart_db.omart_driver_dist_follow d on p.user_id = d.user_id "
			+ "inner join omart_db.omart_driver_follow c on c.user_id = d.user_id "
			+ "where c.car_type_id = :carTypeId and FIND_IN_SET(d.province,:provinces) and FIND_IN_SET(d.district,:districts)",nativeQuery = true)
	public List<Object[]> getDriverTokens(@Param("carTypeId") Long carTypeId, @Param("provinces") String provinces,
			@Param("districts") String districts);
}
