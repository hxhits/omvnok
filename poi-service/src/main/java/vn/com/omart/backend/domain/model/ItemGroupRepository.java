package vn.com.omart.backend.domain.model;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemGroupRepository extends JpaRepository<ItemGroup, Long> {

	public List<ItemGroup> findByGroupIdAndPoiId(Long groupId, Long poiId, Pageable pageable);

	public List<ItemGroup> findByGroupIsNullAndPoiId(Long poiId, Pageable pageable);

	public List<ItemGroup> findByGroupId(Long groupId, Pageable pageable);

	public List<ItemGroup> findByGroupId(Long groupId);

	public List<ItemGroup> findByItemAndPoiId(Item item, Long poiId);
	
	public List<ItemGroup> findByItem(Item item);
	
	public List<ItemGroup> findByGroupIsNotNullAndPoiId(Long poiId);
}
