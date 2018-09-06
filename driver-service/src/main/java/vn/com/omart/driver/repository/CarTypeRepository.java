package vn.com.omart.driver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.com.omart.driver.entity.CarType;

@Repository
public interface CarTypeRepository extends JpaRepository<CarType, Long> {

}
