package vn.com.omart.backend.domain.model;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MagazineRepository extends JpaRepository<Magazine, Long> {

	Magazine findById(Long id);

	List<Magazine> findAllByOrderByTimestampDesc(Pageable pageable);

	List<Magazine> findAllByCatIdOrderByTimestampDesc(Long catId, Pageable pageable);
}
