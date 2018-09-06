package vn.com.omart.messenger.domain.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PushNotificationTokenNewRepository extends JpaRepository<PushNotificationTokenNew, PushNotificationTokenId>{
	
	public List<PushNotificationTokenNew> findByIdUserId(String userId);
	
}
