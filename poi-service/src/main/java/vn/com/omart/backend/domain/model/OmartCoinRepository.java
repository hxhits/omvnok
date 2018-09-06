package vn.com.omart.backend.domain.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OmartCoinRepository extends JpaRepository<OmartCoin, Long>{
	
	public OmartCoin findByUserProfile(UserProfile userProfile);
	
}
