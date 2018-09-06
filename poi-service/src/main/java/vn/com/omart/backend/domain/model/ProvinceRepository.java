package vn.com.omart.backend.domain.model;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProvinceRepository extends JpaRepository<Province, Long> {

  @Query(nativeQuery = true,
      value = "SELECT DISTINCT a.* " + "FROM " + "    `province` a, " + "    (SELECT  " + "        target.* " + "    FROM "
          + "        auth_db.oauth_user u, omart_db.omart_sales_target target " + "    WHERE " + "        (u.manage_by = target.user_id OR target.user_id = :userId) "
          + "            AND `month` = DATE_FORMAT(NOW(), '%Y-%m-01') " + "            AND u.id = :userId) b "
          + "WHERE a.id = b.province_id and (0 = :provinceId OR b.province_id = :provinceId)")
  List<Province> findAllByUser(@Param("userId") String userId, @Param("provinceId") Long provinceId);

  Province findByNameContaining(String name);
  
  @Query("SELECT p FROM  Province p WHERE p.id != -1")
  List<Province> getAllWithout();

}
