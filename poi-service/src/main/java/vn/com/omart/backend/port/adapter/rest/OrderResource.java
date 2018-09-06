package vn.com.omart.backend.port.adapter.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import vn.com.omart.backend.application.OrderDetailService;
import vn.com.omart.backend.application.OrderService;
import vn.com.omart.backend.application.response.BookCarDTO;
import vn.com.omart.backend.application.response.BookOrderDTO;
import vn.com.omart.backend.application.response.OrderDTO;
import vn.com.omart.backend.application.response.OrderReportDTO;
import vn.com.omart.backend.application.response.UserDTO;
import vn.com.omart.backend.application.response.OrderPageDTO;
import vn.com.omart.backend.constants.OmartType.OrderSellerState;
import vn.com.omart.backend.domain.model.EmptyJsonResponse;

@RestController
@RequestMapping("/v1/order")
public class OrderResource {

	@Autowired
	private OrderService orderService;

	@Autowired
	private OrderDetailService orderDetailService;

	/**
	 * Create order.
	 * 
	 * @param userId
	 * @param location
	 * @param dto
	 * @return EmptyJsonResponse
	 */
	@PostMapping(value = "")
	public ResponseEntity<EmptyJsonResponse> save(@RequestHeader(value = "X-User-Id", required = true) String userId,
			@RequestHeader(value = "X-Location-Geo", required = false) String location,
			@Valid @RequestBody(required = true) OrderDTO dto) {
		orderService.save(userId, location, dto);
		return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.CREATED);
	}

	/**
	 * Get order by userId.
	 * 
	 * @param userId
	 * @param pageable
	 * @return List of OrderDTO
	 */
	@GetMapping(value = "")
	public ResponseEntity<List<OrderDTO>> getOrder(@RequestHeader(value = "X-User-Id", required = true) String userId,
			@PageableDefault(page = 0, size = Integer.MAX_VALUE, sort = "updatedAt", direction = Direction.DESC) Pageable pageable) {
		List<OrderDTO> dtos = orderService.getOrderByUserId(userId, pageable);
		return new ResponseEntity<List<OrderDTO>>(dtos, HttpStatus.OK);
	}

	/**
	 * Get order by userId and poiId.
	 * 
	 * @param userId
	 * @param pageable
	 * @return List of OrderDTO
	 */
	@GetMapping(value = "/user/{user-id}/poiId/{poi-id}")
	public ResponseEntity<List<OrderDTO>> getOrderByUserId(
			@PathVariable(value = "user-id", required = true) String userId,
			@PathVariable(value = "poi-id", required = true) Long poiId,
			@PageableDefault(page = 0, size = Integer.MAX_VALUE, sort = "updatedAt", direction = Direction.DESC) Pageable pageable) {
		List<OrderDTO> dtos = orderService.getOrderByUserIdAndPoiId(userId, poiId, pageable);
		return new ResponseEntity<List<OrderDTO>>(dtos, HttpStatus.OK);
	}

	/**
	 * Get order by poiId.
	 * 
	 * @param userId
	 * @param poiId
	 * @param pageable
	 * @return List of OrderDTO
	 */
	@GetMapping(value = "/poiId/{poiId}")
	public ResponseEntity<List<OrderDTO>> getOrderByPoiId(
			@RequestHeader(value = "X-User-Id", required = true) String userId,
			@PathVariable(value = "poiId", required = true) Long poiId,
			@PageableDefault(page = 0, size = Integer.MAX_VALUE, sort = "updatedAt", direction = Direction.DESC) Pageable pageable) {
		List<OrderDTO> dtos = orderService.getOrderByPoiId(poiId, pageable);
		return new ResponseEntity<List<OrderDTO>>(dtos, HttpStatus.OK);
	}

	/**
	 * Get order by poiId.
	 * 
	 * @param userId
	 * @param poiId
	 * @param pageable
	 * @return OrderPageDTO
	 */
	@GetMapping(value = "/poiId/{poiId}", headers = { "X-API-Version=1.1" })
	public ResponseEntity<OrderPageDTO> getOrdersByPoiId(
			@RequestHeader(value = "X-User-Id", required = true) String userId,
			@PathVariable(value = "poiId", required = true) Long poiId,
			@PageableDefault(page = 0, size = Integer.MAX_VALUE, sort = "updatedAt", direction = Direction.DESC) Pageable pageable) {
		OrderPageDTO dtos = orderService.getOrdersByPoiId(poiId, pageable);
		return new ResponseEntity<OrderPageDTO>(dtos, HttpStatus.OK);
	}

	/**
	 * Get order report by poiId.
	 * 
	 * @param userId
	 * @param poiId
	 * @return OrderReportDTO
	 */
	@GetMapping(value = "/report/{poiId}")
	public ResponseEntity<OrderReportDTO> getOrderReportByPoiId(
			@RequestHeader(value = "X-User-Id", required = true) String userId,
			@PathVariable(value = "poiId", required = true) Long poiId) {
		OrderReportDTO dtos = orderService.getOrderReportByPoiId(poiId);
		return new ResponseEntity<OrderReportDTO>(dtos, HttpStatus.OK);
	}

	/**
	 * Get Order By OrderId.
	 * 
	 * @param userId
	 * @param orderId
	 * @param pageable
	 * @return OrderDTO
	 */
	@GetMapping(value = "/{orderId}")
	public ResponseEntity<OrderDTO> getOrderByOrderId(
			@RequestHeader(value = "X-User-Id", required = true) String userId,
			@PathVariable(value = "orderId", required = true) Long orderId,
			@PageableDefault(page = 0, size = Integer.MAX_VALUE, sort = "updatedAt", direction = Direction.DESC) Pageable pageable) {
		OrderDTO dto = orderService.getOrderByOderId(orderId, pageable);
		return new ResponseEntity<OrderDTO>(dto, HttpStatus.OK);
	}

	/**
	 * Update Seller State.
	 * 
	 * @param userId
	 * @param orderId
	 * @param sellerState
	 * @return EmptyJsonResponse
	 */
	@PutMapping(value = "/{orderId}/seller")
	public ResponseEntity<EmptyJsonResponse> updateState(
			@RequestHeader(value = "X-User-Id", required = true) String userId,
			@PathVariable(value = "orderId", required = true) Long orderId,
			@RequestParam(value = "action", required = true) OrderSellerState sellerState) {
		orderService.updateState(orderId, sellerState);
		return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.OK);
	}

	/**
	 * Update Seller State V1.
	 * 
	 * @param userId
	 * @param orderId
	 * @param sellerState
	 * @return EmptyJsonResponse
	 */
	@PutMapping(value = "/{orderId}/seller", headers = { "X-API-Version=1.1" })
	public ResponseEntity<EmptyJsonResponse> updateStateV1(
			@RequestHeader(value = "X-User-Id", required = true) String userId,
			@PathVariable(value = "orderId", required = true) Long orderId,
			@RequestBody(required = true) OrderDTO dto) {
		orderService.updateStateV1(orderId, dto);
		return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.OK);
	}

	/**
	 * Delete.
	 * 
	 * @param userId
	 * @param orderId
	 * @return EmptyJsonResponse
	 */
	@PutMapping(value = "/{orderId}/delete")
	public ResponseEntity<EmptyJsonResponse> delete(@RequestHeader(value = "X-User-Id", required = true) String userId,
			@PathVariable(value = "orderId", required = true) Long orderId) {
		orderService.delete(orderId);
		return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.OK);
	}

	/**
	 * Get order by order state.
	 * 
	 * @param userId
	 * @param poiId
	 * @param sellerState
	 * @param pageable
	 * @return List of OrderDTO
	 */
	@GetMapping(value = "/poiId/{poiId}/seller/{state}")
	public ResponseEntity<List<OrderDTO>> getOrderByPoiIdAndIsDeletedAndSellerState(
			@RequestHeader(value = "X-User-Id", required = true) String userId,
			@PathVariable(value = "poiId", required = true) Long poiId,
			@PathVariable(value = "state", required = true) OrderSellerState sellerState,
			@PageableDefault(page = 0, size = Integer.MAX_VALUE) Pageable pageable) {
		List<OrderDTO> dtos = orderService.getOrderByPoiIdAndIsDeletedAndSellerState(poiId, sellerState, pageable);
		return new ResponseEntity<List<OrderDTO>>(dtos, HttpStatus.OK);
	}

	/**
	 * Get order by order state.
	 * 
	 * @param userId
	 * @param poiId
	 * @param sellerState
	 * @param pageable
	 * @return OrderPageDTO
	 */
	@PostMapping(value = "/poiId/seller/{state}", headers = { "X-API-Version=1.1" })
	public ResponseEntity<OrderPageDTO> getOrderByPoiIdAndIsDeletedAndSellerStateV1(
			@RequestHeader(value = "X-User-Id", required = true) String userId,
			@PathVariable(value = "state", required = true) OrderSellerState sellerState,
			@RequestBody(required = true) long[] ids,
			@PageableDefault(page = 0, size = Integer.MAX_VALUE) Pageable pageable) {
		OrderPageDTO dtos = orderService.getOrderByPoiIdAndIsDeletedAndSellerStateV1(ids, sellerState, pageable);
		return new ResponseEntity<OrderPageDTO>(dtos, HttpStatus.OK);
	}

	/**
	 * Get orders by userId.
	 * 
	 * @param userId
	 * @param poiId
	 * @param pageable
	 * @return List of OrderDTO
	 */
	@GetMapping(value = "/user")
	public ResponseEntity<List<OrderDTO>> getOrderByUserIdForWeb(
			@RequestHeader(value = "X-User-Id", required = true) String userId,
			@PageableDefault(page = 0, size = Integer.MAX_VALUE, sort = "updatedAt", direction = Direction.DESC) Pageable pageable) {
		List<OrderDTO> dtos = orderService.getOrderByUserIdForWeb(userId, pageable);
		return new ResponseEntity<List<OrderDTO>>(dtos, HttpStatus.OK);
	}

	/**
	 * Get order with users and total quantity.
	 * 
	 * @param poiId
	 * @param userId
	 * @param pageable
	 * @return List of OrderDTO
	 */
	@GetMapping(value = "/poiId/{poi-id}/users")
	public ResponseEntity<List<OrderDTO>> getOrderByPoiAndSumQuantity(
			@PathVariable(value = "poi-id", required = true) Long poiId,
			@RequestHeader(value = "X-User-Id", required = true) String userId,
			@PageableDefault(page = 0, size = Integer.MAX_VALUE) Pageable pageable) {
		List<OrderDTO> dtos = orderService.getOrderByPoiAndSumQuantity(userId, pageable, poiId);
		return new ResponseEntity<List<OrderDTO>>(dtos, HttpStatus.OK);
	}

	/**
	 * Delivery
	 * 
	 * @param userId
	 * @param dto
	 * @return EmptyJsonResponse
	 */
	@PostMapping(value = "/delivery")
	public ResponseEntity<EmptyJsonResponse> lookingForDeliverer(
			@RequestHeader(value = "X-User-Id", required = true) String userId,
			@RequestBody(required = true) BookOrderDTO dto) {
		orderService.lookingForDeliverer(userId, dto);
		return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.OK);
	}

	/**
	 * Accept Delivery.
	 * 
	 * @param orderId
	 * @param dto
	 * @return EmptyJsonResponse
	 */
	@PostMapping(value = "/{order-id}/delivery/accept")
	public ResponseEntity<EmptyJsonResponse> acceptDelivery(
			@PathVariable(value = "order-id", required = true) Long orderId,
			@RequestBody(required = true) BookCarDTO dto) {
		orderService.acceptDelivery(orderId, dto);
		return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.OK);
	}

}
