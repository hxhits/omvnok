package vn.com.omart.backend.domain.model;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PoiItemGroupRepository extends JpaRepository<PoiItemGroup, Long> {
	
	public List<PoiItemGroup> findByPoiId(Long poiId,Pageable pageable);
}
