package vn.com.omart.backend.domain.model;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HomeFeatureRepository extends JpaRepository<HomeFeature, Long>{
	
	public List<HomeFeature> findByIsApproved(boolean isApproved,Pageable pageable);
	
}
