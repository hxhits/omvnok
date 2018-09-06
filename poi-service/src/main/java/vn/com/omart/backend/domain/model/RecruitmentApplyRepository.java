package vn.com.omart.backend.domain.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecruitmentApplyRepository extends JpaRepository<RecruitmentApply, Long> {

	List<RecruitmentApply> findAllByIsDeleted(boolean isDeleted);

	List<RecruitmentApply> findAllByIsDeletedAndUserIdOrderByUpdatedAtDesc(boolean isDeleted, String userId);

}
