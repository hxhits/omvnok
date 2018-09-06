package vn.com.omart.backend.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.com.omart.backend.application.response.ItemDTO;
import vn.com.omart.backend.application.util.DateUtils;
import vn.com.omart.backend.constants.OmartType.Commons;
import vn.com.omart.backend.domain.model.CategoryRepository;
import vn.com.omart.backend.domain.model.Item;
import vn.com.omart.backend.domain.model.ItemRepository;
import vn.com.omart.backend.domain.model.PointOfInterest;
import vn.com.omart.backend.domain.model.PointOfInterestRepository;
import vn.com.omart.sharedkernel.application.model.error.NotFoundException;

@Service
public class ItemService {

	private final Logger logger = LoggerFactory.getLogger(ItemService.class);

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private PointOfInterestRepository pointOfInterestRepository;

	/**
	 * Delete Item By Id.
	 *
	 * @param itemId
	 */
	@Transactional
	public void deleteItemById(Long itemId) {
		Item item = itemRepository.findOne(itemId);
		if (item == null) {
			throw new NotFoundException("Item not found");
		}
		itemRepository.delete(item);
	}

	/**
	 * Update Item By Id.
	 *
	 * @param itemId
	 */
	@Transactional
	public void updateItemStock(String userId, Long itemId, ItemDTO itemDTO) {
		Item item = itemRepository.findOne(itemId);
		if (item == null) {
			throw new NotFoundException("Item not found");
		}
		item.setOutOfStock(itemDTO.isOutOfStock());
		itemRepository.save(item);
	}

	/**
	 * Post item to omart products
	 * 
	 * @param id
	 */
	public void postItem(Long id, Commons action) {
		Item item = itemRepository.findOne(id);
		if(item!=null) {
			//reset item post
			List<Item> entities = itemRepository.findByPoiIdAndIsPosted(item.getPoi().id(),true);
			if(entities !=null && !entities.isEmpty()) {
				entities.forEach(entity->{
					entity.setPosted(false);
				});
				itemRepository.save(entities);
			}
			//update item post
			if (action == Commons.POST) {
				if (item.getPoi() != null) {
					item.setPosted(true);
					item.setCatId(item.getPoi().category().id());
					item.setPostedAt(DateUtils.getCurrentDate());
				} else {
					logger.error("\nPOST ITEM exception: Poi not found");
				}
			}
			itemRepository.save(item);
		}
	}

	/**
	 * Checking is posted.
	 * 
	 * @param id
	 * @return boolean
	 */
	public boolean isItemPosted(Long id) {
		Item item = itemRepository.findOne(id);
		if (item != null) {
			List<Item> entities = itemRepository.findByPoiIdAndIsPosted(item.getPoi().id(),true);
			if (entities!=null && !entities.isEmpty()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Get all item posts.
	 * 
	 * @param pageable
	 * @return List of ItemDTO
	 */
	public List<ItemDTO> getItemPosts(Pageable pageable) {
		List<Item> entities = itemRepository.getByIsPosted(true, pageable);
		if (entities != null) {
			List<ItemDTO> dtos = entities.stream().map(ItemDTO::toFullDTO).collect(Collectors.toList());
			return dtos;
		}
		return new ArrayList<>();
	}

}
