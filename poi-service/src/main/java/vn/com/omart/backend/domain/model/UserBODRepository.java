package vn.com.omart.backend.domain.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBODRepository extends JpaRepository<UserBOD, String> {
	
	public UserBOD findByIdAndPassword(String id, String password);
	
	public UserBOD findByUserNameAndPassword(String userName, String password);
	
	public UserBOD findByUserName(String userName);
}
