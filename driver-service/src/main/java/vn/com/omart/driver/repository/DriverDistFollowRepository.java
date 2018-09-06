package vn.com.omart.driver.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.com.omart.driver.entity.DriverDistFollow;

@Repository
public interface DriverDistFollowRepository extends JpaRepository<DriverDistFollow, Long> {

	public List<DriverDistFollow> findByUserId(String userId);

}
