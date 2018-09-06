package vn.com.omart.backend.domain.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
	
	UserProfile findByUserId(String userId);
	
	UserProfile findByPhone(String phone);
	
	@Query("SELECT u FROM UserProfile u WHERE u.userId IN (:userId)")
	List<UserProfile> getUserInUserIds(@Param("userId") List<String> userId);
}
