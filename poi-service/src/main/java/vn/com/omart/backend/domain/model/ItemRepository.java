package vn.com.omart.backend.domain.model;


import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

//  List<Item> findByPoiIdAndCreatedBy(Long poiId, String createdBy);

    Item findByIdAndPoiId(Long id, Long poiId);

    List<Item> findByPoiId(Long poiId);
    
    List<Item> findByPoiIdAndIsPosted(Long poiId,boolean isPosted);
    
    List<Item> findByPoiId(Long poiId,Pageable pageable);
    
    @Query("SELECT i FROM Item i WHERE i.isPosted = :isPosted Order By i.postedAt DESC")
    List<Item> getByIsPosted(@Param("isPosted") boolean isPosted,Pageable pageable);

}
