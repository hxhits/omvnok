package vn.com.omart.backend.domain.model;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import vn.com.omart.backend.application.response.NewsDTO;

public interface NewsRepository extends JpaRepository<News, Long> {

	News findById(Long id);

	List<News> findAllByOrderByNewsTypeAscCreatedAtDesc();

	List<News> findByApprovalOrderByNewsTypeAscCreatedAtAsc(int approval, Pageable pageable);

	List<News> findByNewsTypeOrderByCreatedAtAsc(int newsType, Pageable pageable);

	List<News> findByApprovalAndNewsTypeOrderByCreatedAtAsc(int approval, int newsType, Pageable pageable);

	@Query(value = "SELECT n.id,n.title,n.description,n.thumbnail_url,n.banner_url,n.news_type,n.is_read,p.own_id,n.longitude,n.latitude,n.poi_id FROM omart_db.omart_news n LEFT JOIN  omart_db.omart_point_of_interest p ON n.poi_id = p.id WHERE n.approval = :approval "
			+ "ORDER BY n.news_type ASC, n.created_at DESC \n#pageable\n", nativeQuery = true)
	List<Object[]> getNewsByApproval(@Param("approval") int approval, Pageable pageable);

	/**
	 * Note Newtype (type) -1 to get all.
	 * @param type
	 * @param isIgnoreCatId
	 * @param categoryIds
	 * @param approval
	 * @param pageable
	 * @return
	 */
	@Query(value = "SELECT n.id,n.title,n.description,n.thumbnail_url,n.banner_url,n.news_type,n.is_read,p.own_id,n.longitude,n.latitude,n.poi_id,p.last_notification,p.discount FROM omart_db.omart_news n LEFT JOIN  omart_db.omart_point_of_interest p ON n.poi_id = p.id WHERE n.approval = :approval "
			+ "AND (:isIgnoreCatId = true OR p.cat_id IN (:categoryIds)) AND (n.news_type = :newsType OR :newsType = -1) ORDER BY n.news_type ASC, n.created_at DESC \n#pageable\n", nativeQuery = true)
	List<Object[]> getNewsByApproval_V11(@Param("newsType") int newsType ,@Param("isIgnoreCatId") boolean isIgnoreCatId , @Param("categoryIds") Long []categoryIds, @Param("approval") int approval,
			Pageable pageable);

	@Query("SELECT new vn.com.omart.backend.application.response.NewsDTO(n.id,n.title,n.desc,n.thumbnailUrl,n.bannerUrl,n.newsType,n.read) "
			+ "FROM News n WHERE n.approval = :approval AND n.newsType = :newsType ORDER BY n.createdAt DESC")
	List<NewsDTO> getByApprovalAndNewsTypeOrderByCreatedAtAsc(@Param("approval") int approval, @Param("newsType") int newsType);

	@Query("SELECT new vn.com.omart.backend.application.response.NewsDTO(n.poi.id,n.title,n.desc,n.thumbnailUrl,n.bannerUrl) FROM News n WHERE n.poi.id in (:id) AND n.approval = 1")
	List<NewsDTO> getNews(@Param("id") Long[] idList);
	
	List<News> findByPoiOrderByCreatedAtDesc(PointOfInterest poi,Pageable pageable);
}
