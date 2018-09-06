package vn.com.omart.messenger.domain.model;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Contact Repository.
 * 
 * @author Win10
 *
 */
@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

	public List<Contact> findByUserIdOrderByTypeDesc(String userId, Pageable pageable);

	public List<Contact> findByUserIdOrderByNameAsc(String userId);

	public List<Contact> findByUserIdAndType(String userId, int type);

	@Query(nativeQuery = true, value = "SELECT ct.*,u.avatar FROM omart_db.omart_contact ct inner join auth_db.oauth_user u on u.id = ct.omart_id where ct.user_id = :userId and ct.type = :type")
	public List<Object[]> getContactWithUserIdAndType(@Param("userId") String userId, @Param("type") int type);
	
	@Query(value="SELECT u.id FROM omart_db.omart_user_profile u WHERE u.user_id = :userId", nativeQuery=true)
	public String getIdByUserId(@Param("userId") String userId);
	
	@Query(value="SELECT group_concat(f.user_id) AS str1 , group_concat(f.friend_id) AS str2 FROM omart_db.omart_user_friend f where f.user_id = :id or f.friend_id = :id",nativeQuery=true)
	public List<Object[]> getFriendById(@Param("id") Long id);
	
	@Query(value="SELECT u.user_id FROM omart_db.omart_user_profile u WHERE u.id in (:ids)", nativeQuery=true)
	public List<Object[]> getUserIdInIds(@Param("ids") List<String> ids);
	
	
	
}
