package vn.com.omart.messenger.domain.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CallStatusRepository extends JpaRepository<CallStatus, Long> {

	public CallStatus findByCallSid(String callSid);

	@Query(value = "SELECT cs FROM CallStatus cs WHERE cs.callSid = :callSid OR cs.parentCallSid = :callSid")
	public CallStatus findByCallSidOrParentCallSid(@Param("callSid") String callSid);
}
