package vn.com.omart.driver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.com.omart.driver.entity.DriverLocation;

@Repository
public interface DriverLocationRepository extends JpaRepository<DriverLocation, String>{

}
