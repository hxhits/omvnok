package vn.com.omart.backend.domain.model;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecruitmentPositionLevelRepository extends JpaRepository<RecruitmentPositionLevel, Long> {

	List<RecruitmentPositionLevel> findAllById(Long id);
	List<RecruitmentPositionLevel> findAllByRecruitmentPosition(RecruitmentPosition recruitmentPosition);

}
