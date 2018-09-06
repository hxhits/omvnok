package vn.com.omart.backend.domain.model;


import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import vn.com.omart.backend.constants.OmartType;

@Repository
public interface BookCarRepository extends JpaRepository<BookCar, Long> {
	
	public List<BookCar> findBystate(OmartType.BookCarState state,Pageable pageable);
	
	@Query("SELECT b FROM BookCar b WHERE b.serviceType =1")
	public List<BookCar> getDataForMigration();//(Pageable pageable);
}
