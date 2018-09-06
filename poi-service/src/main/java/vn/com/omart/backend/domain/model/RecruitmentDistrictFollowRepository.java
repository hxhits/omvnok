package vn.com.omart.backend.domain.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecruitmentDistrictFollowRepository extends JpaRepository<RecruitmentDistrictFollow, Long> {

	List<RecruitmentDistrictFollow> findByUserId(String userId);

}
