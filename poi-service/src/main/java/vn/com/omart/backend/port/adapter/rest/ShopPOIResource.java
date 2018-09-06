package vn.com.omart.backend.port.adapter.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import lombok.extern.slf4j.Slf4j;
import vn.com.omart.backend.application.response.PointOfInterestDTO;
import vn.com.omart.backend.application.response.PointOfInterestShortDTO;
import vn.com.omart.backend.constants.ConstantUtils;
import vn.com.omart.backend.domain.model.EmptyJsonResponse;
import vn.com.omart.backend.domain.model.Image;
import vn.com.omart.backend.domain.model.PointOfInterest;
import vn.com.omart.backend.domain.model.PointOfInterestRepository;
import vn.com.omart.sharedkernel.application.model.error.CannotUpdateException;
import vn.com.omart.sharedkernel.application.model.error.InvalidInputException;
import vn.com.omart.sharedkernel.application.model.error.NotFoundException;

@RestController
@RequestMapping("/v1/apps")
@Slf4j
public class ShopPOIResource {

	@Autowired
	private PointOfInterestRepository pointOfInterestRepository;

	@GetMapping(value = { "/shop/{id}" })
	public PointOfInterestShortDTO get(@RequestHeader(value = "X-User-Id", required = false) String userId,
			@PathVariable(value = "id") Long id) {

		if (id == null) {
			throw new InvalidInputException("Please input shop id");
		}

		PointOfInterest entity = pointOfInterestRepository.findByIdAndIsDeleted(id, false);

		if (entity == null) {
			throw new NotFoundException("Shop not found or has just been deleted");
		}

		PointOfInterestShortDTO dto = new PointOfInterestShortDTO();

		dto.setId(entity.id());
		dto.setName(entity.name());
		dto.setAddress(entity.address());
		dto.setDescription(entity.description());

		List<Image> avatarImage = entity.avatarImage();
		String avatar = null;

		if (avatarImage != null && !avatarImage.isEmpty()) {
			avatar = avatarImage.get(0).url();
		}

		dto.setAvatarImage(avatar);

		return dto;
	}

	/**
	 * Delete Shop
	 * 
	 * @param userId
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/shop/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<EmptyJsonResponse> delete(@RequestHeader(value = "X-User-Id") String userId,
			@PathVariable(value = "id", required = true) Long id) {

		if (id == null) {
			throw new InvalidInputException("Please input shop id");
		}

		PointOfInterest entity = pointOfInterestRepository.findByIdAndIsDeleted(id, false);

		if (entity == null) {
			throw new NotFoundException("Shop not found or has just been deleted");
		}

		entity.setIsDeleted(true);

		entity = pointOfInterestRepository.save(entity);

		if (!entity.getIsDeleted()) {
			throw new CannotUpdateException("Cannot delete Shop");
		}

		return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.OK);
	}

	/**
	 * Delete/revert shop
	 * 
	 * @param userId
	 * @param action
	 * @param id
	 * @return EmptyJsonResponse
	 */
//	@RequestMapping(value = "/shop/{id}/delete", method = RequestMethod.PUT)
//	public ResponseEntity<EmptyJsonResponse> actionDelete(
//			@RequestHeader(value = "X-User-Id", required = true) String userId,
//			@RequestParam(value = "action", required = true) String action,
//			@PathVariable(value = "id", required = true) Long id) {
//		
//		PointOfInterest entity = pointOfInterestRepository.findOne(id);
//		if (entity == null) {
//			throw new NotFoundException("Shop not found or has just been deleted");
//		}
//		if (ConstantUtils.DELETE.equals(action)) {
//			entity.setIsDeleted(true);
//			entity = pointOfInterestRepository.save(entity);
//			if (!entity.getIsDeleted()) {
//				throw new CannotUpdateException("Cannot delete Shop");
//			}
//		} else if (ConstantUtils.REVERT.equals(action)) {
//			entity.setIsDeleted(false);
//			entity = pointOfInterestRepository.save(entity);
//			if (entity.getIsDeleted()) {
//				throw new CannotUpdateException("Cannot revert delete Shop");
//			}
//		} else {
//			throw new InvalidInputException("action param invalid " + action);
//		}
//		return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.OK);
//	}
	
	
	
	/**
	 * Delete/revert shop
	 * 
	 * @param userId
	 * @param action
	 * @param id
	 * @return EmptyJsonResponse
	 */
	@RequestMapping(value = "/shop/{id}/delete", method = RequestMethod.PUT)
	public ResponseEntity<EmptyJsonResponse> actionDelete(
			@RequestHeader(value = "X-User-Id", required = true) String userId,
			@RequestBody(required=true) PointOfInterestDTO dto,
			@PathVariable(value = "id", required = true) Long id) {
		
		PointOfInterest entity = pointOfInterestRepository.findOne(id);
		if (entity == null) {
			throw new NotFoundException("Shop not found or has just been deleted");
		}
		if (dto.isDeleted()) {
			entity.setIsDeleted(true);
		} else if (!dto.isDeleted()) {
			entity.setIsDeleted(false);
		} 
		if(dto.getReason()!=null) {
			entity.setReason(dto.getReason());
		}
		entity = pointOfInterestRepository.save(entity);
		return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.OK);
	}

}
