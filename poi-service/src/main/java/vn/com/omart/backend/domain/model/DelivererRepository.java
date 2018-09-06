package vn.com.omart.backend.domain.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DelivererRepository extends JpaRepository<Deliverer, Long>{
	
	public List<Deliverer> findAllByPoi(PointOfInterest poi);
	public Deliverer findByDriverAndPoi(DriverInfo driver, PointOfInterest poi);
}
