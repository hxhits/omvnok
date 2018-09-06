package vn.com.omart.backend.domain.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverInfoRepository extends JpaRepository<DriverInfo, String> {
	
	public DriverInfo findByPhoneNumber(String phoneNumber);
}
