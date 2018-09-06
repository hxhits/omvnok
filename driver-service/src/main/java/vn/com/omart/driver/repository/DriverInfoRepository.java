package vn.com.omart.driver.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.com.omart.driver.entity.DriverInfo;

@Repository
public interface DriverInfoRepository extends JpaRepository<DriverInfo, String> {
	
	public DriverInfo findByPhoneNumber(String phoneNumber);
	
	public Page<DriverInfo> findAll(Pageable pageable);
	
	@Query("SELECT f FROM DriverInfo f WHERE f.contractId = '' Order By f.dateOfRegistration asc")
	public List<DriverInfo> getAllByContractIdIsEmpty();
	
	@Query("SELECT f FROM DriverInfo f Order By f.dateOfRegistration asc")
	public List<DriverInfo> getAllOrderByDateOfRegistrationAsc();
	
	public DriverInfo findTop1ByOrderByDateOfRegistrationDesc();
	
	@Query("SELECT p FROM DriverInfo p WHERE CONVERT(p.dateOfRegistration,DATE) BETWEEN :from AND :to Order By p.dateOfRegistration DESC")
	List<DriverInfo> getByDateOfRegistrationAtBetween(@Param("from") String from, @Param("to") String to ,Pageable pageable);
	
	List<DriverInfo> findByFullNameContainingOrderByDateOfRegistrationDesc(String fullName,Pageable pageable);
	
	List<DriverInfo> findByPhoneNumberContainingOrderByDateOfRegistrationDesc(String phoneNumber,Pageable pageable);
}
