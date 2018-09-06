package vn.com.omart.backend.domain.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecruitmentPositionFollowRepository extends JpaRepository<RecruitmentPositionFollow, Long> {

	List<RecruitmentPositionFollow> findByUserId(String userId);

	List<RecruitmentPositionFollow> findByRecruitmentPosition(RecruitmentPositionLevel position);

}
