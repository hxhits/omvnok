package vn.com.omart.driver.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.com.omart.driver.entity.BookCar;

@Repository
public interface BookCarRepository extends JpaRepository<BookCar, Long> {
	
	@Query("SELECT b FROM BookCar b WHERE b.poiId IN (:poiIds) AND b.serviceType = 1 AND b.state = 'WAITING' Order By b.updatedAt DESC")
	public List<BookCar> getOrder(@Param("poiIds") List<Long> poiIds, Pageable pageable);

	@Query("SELECT bc FROM BookCar bc WHERE bc.id = :id")
	public BookCar getById(@Param("id") Long id);
	
	public BookCar findStateById(Long id);
	
	@Query("SELECT b FROM BookCar b WHERE b.serviceType = 0 AND b.carType.id IN (:carTypes) AND (:provinceId = 0L OR b.province = :provinceId) AND (:districtId = 0L OR b.district = :districtId) AND b.state = 'WAITING' AND CONVERT(b.updatedAt,DATE) BETWEEN :inDay AND :inDay Order By b.updatedAt DESC")
	public List<BookCar> getBookCarByCarTypeAndProvinceAndDistrict(@Param("carTypes") Long[] carTypes,
			@Param("provinceId") Long provinceId, @Param("districtId") Long districtId, @Param("inDay") String inDay, Pageable pageable);
	
}
