package vn.com.omart.driver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.com.omart.driver.entity.DriverInfo;
import vn.com.omart.driver.entity.Setting;

@Repository
public interface SettingRepository extends JpaRepository<Setting, Long> {

	public Setting findByDriverInfo(DriverInfo userId);
	
	public Setting findByDriverInfoUserId(String userId);

}
