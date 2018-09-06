package vn.com.omart.auth.domain;


import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, String> {

  @Query("SELECT u FROM User u WHERE LOWER(u.username) = LOWER(:username)")
  User findByUsernameCaseInsensitive(@Param("username") String username);

  @Query
  User findByEmail(String email);

  List<User> findAllByCreatedBy(String userid);

  List<User> findAllByManageBy(String managerId);

  List<User> findAllByManageByAndTitle(String managerId, String title);

  User findByIdAndTitle(String id, String title);

  User findByusernameIgnoreCase(String username);
  
  List<User> findAllByActivated(boolean activated);

}
