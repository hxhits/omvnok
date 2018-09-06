package vn.com.omart.backend.port.adapter.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import vn.com.omart.backend.application.ItemService;
import vn.com.omart.backend.application.response.HttpStatusCodeDTO;
import vn.com.omart.backend.application.response.ItemDTO;
import vn.com.omart.backend.constants.OmartType.Commons;
import vn.com.omart.backend.domain.model.EmptyJsonResponse;
import vn.com.omart.sharedkernel.application.model.error.NotFoundException;

@RestController
@RequestMapping("/v1/pois")
public class ItemResource {

	@Autowired
	private ItemService itemService;

	/**
	 * Delete Item By ID.
	 * 
	 * @param itemId
	 * @return HttpStatus
	 */
	@RequestMapping(value = "/item/{item-id}", method = RequestMethod.DELETE)
	public ResponseEntity<EmptyJsonResponse> deleteItemById(
			@PathVariable(value = "item-id", required = true) Long itemId,
			@RequestHeader(value = "X-User-Id", required = true) String userId) {
		itemService.deleteItemById(itemId);
		return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.OK);
	}

	/**
	 * Update Item.
	 * 
	 * @param userId
	 * @param id
	 * @param recruitmentDTO
	 * @return
	 */
	@RequestMapping(value = "/item/{item-id}/stock", method = RequestMethod.PUT)
	public ResponseEntity<EmptyJsonResponse> updateV1(@RequestHeader(value = "X-User-Id") String userId,
			@PathVariable(value = "item-id", required = true) Long itemId,
			@RequestBody(required = true) ItemDTO itemDTO) {
		itemService.updateItemStock(userId, itemId, itemDTO);
		return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.OK);
	}

	/**
	 * Post item to omart products.
	 * 
	 * @param userId
	 * @param id
	 * @param action
	 * @return
	 */
	@GetMapping(value = "/item/{id}/post")
	public ResponseEntity<EmptyJsonResponse> postItem(
			@RequestHeader(value = "X-User-Id", required = true) String userId,
			@PathVariable(value = "id", required = true) Long id,
			@RequestParam(required = true, value = "action") Commons action) {
		itemService.postItem(id, action);
		return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.OK);
	}

	/**
	 * Get all item posts.
	 * 
	 * @param pageable
	 * @return List of ItemDTO
	 */
	@GetMapping(value = "/item/posts")
	public ResponseEntity<List<ItemDTO>> getItemPosts(@PageableDefault(page = 0, size = 20) Pageable pageable) {
		return new ResponseEntity<List<ItemDTO>>(itemService.getItemPosts(pageable), HttpStatus.OK);
	}

	/**
	 * Checking item is posted.
	 * 
	 * @param userId
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/item/{id}/isPosted")
	public ResponseEntity<HttpStatusCodeDTO> isItemPosted(
			@RequestHeader(value = "X-User-Id", required = true) String userId,
			@PathVariable(value = "id", required = true) Long id) {
		boolean isPosted = itemService.isItemPosted(id);
		HttpStatusCodeDTO dto = new HttpStatusCodeDTO();
		if (isPosted) {
			dto.setCode(HttpStatus.FOUND.value()); 
			return new ResponseEntity<HttpStatusCodeDTO>(dto,HttpStatus.OK);
		}
		dto.setCode(HttpStatus.NOT_FOUND.value());
		return new ResponseEntity<HttpStatusCodeDTO>(dto,HttpStatus.OK);
	}
}
