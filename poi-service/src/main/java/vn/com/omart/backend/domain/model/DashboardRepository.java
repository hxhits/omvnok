package vn.com.omart.backend.domain.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DashboardRepository extends JpaRepository<PointOfInterest, Long> {

    @Query(value = "SELECT  " +
        "    poi.created_by, u.firstname, u.username, COUNT(1) " +
        "FROM " +
        "    omart_db.omart_point_of_interest poi, " +
        "    auth_db.oauth_user u " +
        "WHERE " +
        "    1 = 1 AND poi.created_by = u.id " +
        "        AND poi.created_at BETWEEN DATE_FORMAT(NOW(), '%Y-%m-01') AND NOW() " +
        "        AND u.manage_by = :manageBy " +
        "GROUP BY poi.created_by , u.firstname , u.username", nativeQuery = true)
    List<Object[]> listQuota(@Param("manageBy") String manageBy); // cf8652e7963611e7b2cc0242ac110002

    @Query(nativeQuery = true, value = "SELECT  " +
        "    poi.created_by AS user_id, " +
        "    u.firstname AS user_name, " +
        "    u.username AS username, " +
        "    poi.province AS province_id, " +
        "    prov.`name` AS province_name, " +
        "    COUNT(1) AS count " +
        "FROM " +
        "    omart_db.omart_point_of_interest poi, " +
        "    auth_db.oauth_user u, " +
        "    omart_db.province prov " +
        "WHERE " +
        "    1 = 1 AND poi.created_by = u.id " +
        "        AND prov.id = poi.province " +
        "        AND DATE(poi.created_at) BETWEEN DATE(NOW()) AND DATE(NOW()) " +
        "GROUP BY poi.created_by , u.firstname , u.username , poi.province , prov.`name`;")
    List<Object[]> getQuotaByDayAndProvince();

    @Query(nativeQuery = true, value = "SELECT COUNT(1) as `count`" +
        "FROM " +
        "    omart_db.omart_point_of_interest")
    Long totalShop();

    @Query(nativeQuery = true, value = "select count(1) as `count` from auth_db.oauth_user " +
        "where title = 'SALESMAN'")
    Long totalSalesman();

    @Query(nativeQuery = true, value = "select count(1) as `count` from auth_db.oauth_user " +
        "where title = 'SALESMAN' AND activated=1")
    Long totalActivatedSalesman();

    @Query(nativeQuery = true, value = "select count(1) as `count` from auth_db.oauth_user " +
        "where title = 'SUPERVISOR'")
    Long totalSupervior();

    @Query(nativeQuery = true, value = "select count(1) as `count` from auth_db.oauth_user " +
        "where title = 'SUPERVISOR' AND activated=1")
    Long totalActivatedSupervior();

    @Query(nativeQuery = true, value = "select count(1) as `count` from auth_db.oauth_user " +
        "where title = 'USER'")
    Long totalUser();

    @Query(nativeQuery = true, value =
    	" SELECT  " +
        "    tbl.province_id, " +
        "    tbl.province_name, " +
        "    SUM(tbl.total) totalAll, " +
        "    SUM(tbl.inDay) totalInDay " +

        " FROM (" +

        "       SELECT  " +
        "            prov.id AS province_id, " +
        "            prov.name AS province_name, " +
        "            COUNT(1) AS total, " +
        "            0 AS inDay " +
        "       FROM " +
        "            omart_db.province prov " +
        "        LEFT OUTER JOIN omart_db.omart_point_of_interest poi " +
        "            ON prov.id = poi.province " +
        "       GROUP BY prov.id, prov.name " +

        "       UNION ALL  " +

        "       SELECT  " +
        "            prov1.id AS province_id, " +
        "            prov1.name AS province_name, " +
        "            0 AS total, " +
        "            COUNT(1) AS inDay " +
        "       FROM " +
        "            omart_db.province prov1 " +
        "              LEFT OUTER JOIN omart_db.omart_point_of_interest poi1 " +
        "                ON prov1.id = poi1.province " +
        "       WHERE " +
        "            DATE(poi1.created_at) BETWEEN DATE(NOW()) AND DATE(NOW()) " +
        "       GROUP BY prov1.id, prov1.name " +

        " ) AS tbl " +
        " GROUP BY tbl.province_id , tbl.province_name")
    List<Object[]> getTotalByProvince();

    @Query(nativeQuery = true, value =
        " SELECT " +
        "   title, " +
        "   count(id) as count " +
        " FROM auth_db.oauth_user z " +
        " WHERE activated=1 " +
        " GROUP BY z.title ")
    List<Object[]> getTotalActivatedUserByTitle();

    @Query(nativeQuery = true, value =
        " SELECT " +
        "   title, " +
        "   count(id) as count " +
        " FROM auth_db.oauth_user z " +
        " GROUP BY z.title")
    List<Object[]> getTotalUserByTitle();
}
