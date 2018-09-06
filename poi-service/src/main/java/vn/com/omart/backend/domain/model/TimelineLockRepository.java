package vn.com.omart.backend.domain.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimelineLockRepository extends JpaRepository<TimelineLock, Long>{
	
	public TimelineLock findByUserFromAndUserTo(String userFrom,String userTo);
}
