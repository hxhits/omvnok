package vn.com.omart.backend.domain.model;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecruitmentPositionRepository extends JpaRepository<RecruitmentPosition, Long> {

	List<RecruitmentPosition> findAllByIsDisabledOrderByOrder(boolean isDisabled);

}
