package vn.com.omart.driver.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.com.omart.driver.entity.DriverFollow;

@Repository
public interface DriverFollowRepository extends JpaRepository<DriverFollow, Long> {

	public List<DriverFollow> findByUserId(String userId);

}
