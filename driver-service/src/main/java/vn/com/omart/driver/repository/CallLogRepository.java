package vn.com.omart.driver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.com.omart.driver.entity.CallLog;
import vn.com.omart.driver.entity.CallLogId;

@Repository
public interface CallLogRepository extends JpaRepository<CallLog, CallLogId> {

	CallLog findTopByIdBookcarIdAndStateOrderByCreateaAtDesc(Long bookcarId, int state);
}
