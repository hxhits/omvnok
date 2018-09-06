package vn.com.omart.backend.domain.model;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PoiFollowRepository extends JpaRepository<PoiFollow, Long> {
	
	public List<PoiFollow> findByUserUserId(String userId,Pageable pageable);
	
	public PoiFollow findByUserUserIdAndPoiId(String userId,Long poiId);
}
