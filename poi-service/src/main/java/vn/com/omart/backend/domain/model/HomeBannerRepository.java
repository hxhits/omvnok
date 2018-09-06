package vn.com.omart.backend.domain.model;


import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HomeBannerRepository extends JpaRepository<HomeBanner, Long> {
	
	public List<HomeBanner> findByBannerTypeAndIsApprovedOrderByUpdatedAtDesc(int bannerType,boolean isApproved,Pageable pageable);
	
	public HomeBanner findByIdAndPoiAndBannerType(Long id,PointOfInterest poi,int bannerType);
	
	public List<HomeBanner> findByPoi(PointOfInterest poi);
	
}
