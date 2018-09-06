package vn.com.omart.driver.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.com.omart.driver.entity.Deliverer;

@Repository
public interface DelivererRepository extends JpaRepository<Deliverer, Long> {

	@Query("SELECT d.poiId FROM Deliverer d WHERE d.driver.userId = :userId")
	public List<Long> getByDriver(@Param("userId") String userId);

	@Query("SELECT COUNT(1) > 0 FROM Deliverer d WHERE d.poiId = :poiId")
	public boolean isMemberOf(@Param("poiId") Long poiId);

	@Query("SELECT d.driver.userId FROM Deliverer d WHERE d.poiId= :poiId")
	public List<String> getUserIdByPoiId(@Param("poiId") Long poiId);
}
