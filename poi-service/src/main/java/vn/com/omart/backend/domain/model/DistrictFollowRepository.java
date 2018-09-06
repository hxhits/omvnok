package vn.com.omart.backend.domain.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DistrictFollowRepository extends JpaRepository<DistrictFollow, Long> {

	List<DistrictFollow> findByUserId(String userId);

}
