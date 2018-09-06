package vn.com.omart.messenger.domain.model;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PushNotificationTokenRepository extends PagingAndSortingRepository<PushNotificationToken, String> {

    PushNotificationToken findByUserId(String userId);

}
