package vn.com.omart.backend.domain.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PoiPictureCPRepository extends JpaRepository<PoiPictureCP, Long> {
	
//	@Query("SELECT p FROM PoiPictureCP p WHERE p.url not like '[%' ")
//	public List<PoiPictureCP> getAllByUrlNotLike();
	
	public List<PoiPictureCP> findAllByCreatedAtIsNull();
}
