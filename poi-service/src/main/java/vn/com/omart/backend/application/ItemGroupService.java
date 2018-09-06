package vn.com.omart.backend.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import vn.com.omart.backend.application.response.ItemDTO;
import vn.com.omart.backend.application.response.ItemGroupDTO;
import vn.com.omart.backend.domain.model.Item;
import vn.com.omart.backend.domain.model.ItemGroup;
import vn.com.omart.backend.domain.model.ItemGroupRepository;
import vn.com.omart.backend.domain.model.ItemRepository;

@Service
public class ItemGroupService {

	@Autowired
	private ItemGroupRepository itemGroupRepository;

	@Autowired
	private ItemRepository itemRepository;

	/**
	 * Get Items.
	 * 
	 * @param poiId
	 * @param groupId
	 * @param pageable
	 * @return List of ItemDTO
	 */
	public List<ItemDTO> getItems(Long poiId, Long groupId, Pageable pageable) {
		if (groupId > 0L) {
			// get by group id
			List<ItemGroup> itemGroups = itemGroupRepository.findByGroupIdAndPoiId(groupId, poiId, pageable);
			if (itemGroups != null) {
				return this.toItems(itemGroups);
			}
		} else if (groupId == 0L) {
			// get order
			List<ItemGroup> itemGroups = itemGroupRepository.findByGroupIsNullAndPoiId(poiId, pageable);
			if (itemGroups != null) {
				return this.toItems(itemGroups);
			}
		} else {
			// get all
			List<Item> entities = itemRepository.findByPoiId(poiId, pageable);
			if (!entities.isEmpty()) {
				List<ItemDTO> items = entities.stream().map(ItemDTO::from).collect(Collectors.toList());
				return items;
			}
		}
		return new ArrayList<>();
	}

	/**
	 * To item dto
	 * 
	 * @param itemGroups
	 * @return List of ItemDTO
	 */
	private List<ItemDTO> toItems(List<ItemGroup> itemGroups) {
		List<ItemDTO> items = itemGroups.stream().map(ItemGroupDTO::toImageDTO).collect(Collectors.toList());
		return items;
	}

	/**
	 * delete
	 * 
	 * @param groupId
	 */
	public void deleteGroup(Long groupId,Long poiId) {
		List<ItemGroup> itemGroups = itemGroupRepository.findByGroupIsNotNullAndPoiId(poiId);
		if (itemGroups != null) {
			List<ItemGroup> deleteList = new ArrayList<>();
			List<ItemGroup> resetList = new ArrayList<>();
			// filter by group id.
			List<ItemGroup> grs0 = itemGroups.stream().filter(item->item.getGroup().getId()==groupId).collect(Collectors.toList());
			// filter without group id.
			List<ItemGroup> grs1 = itemGroups.stream().filter(item->item.getGroup().getId()!=groupId).collect(Collectors.toList());
			for(ItemGroup itemGrp: grs0) {
				boolean isExisting = grs1.stream().anyMatch(item->item.getItem().id()==itemGrp.getItem().id());
				if(isExisting) {
					deleteList.add(itemGrp);
				}else {
					resetList.add(itemGrp);
				}
			}

			if(!resetList.isEmpty()) {
				this.resetGroup(resetList);
			}
			if(!deleteList.isEmpty()) {
				itemGroupRepository.delete(deleteList);
			}
		}
	}
	
	/**
	 * Reset group null value.
	 * @param reset
	 */
	public void resetGroup(List<ItemGroup> reset) {
		reset.forEach(item -> {
			item.setGroup(null);
		});
		itemGroupRepository.save(reset);
	}
	
//	public void deleteGroup(Long groupId) {
//		List<ItemGroup> itemGroups = itemGroupRepository.findByGroupId(groupId);
//		if (itemGroups != null) {
//			itemGroups.forEach(item -> {
//				item.setGroup(null);
//			});
//			itemGroupRepository.save(itemGroups);
//		}
//	}
}
