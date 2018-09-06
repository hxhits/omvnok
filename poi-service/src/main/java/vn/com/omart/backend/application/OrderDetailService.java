package vn.com.omart.backend.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import vn.com.omart.backend.application.response.OrderDetailDTO;
import vn.com.omart.backend.application.util.DateUtils;
import vn.com.omart.backend.domain.model.Order;
import vn.com.omart.backend.domain.model.OrderDetail;
import vn.com.omart.backend.domain.model.OrderDetailRepository;
import vn.com.omart.backend.domain.model.OrderRepository;

@Service
public class OrderDetailService {

	@Autowired
	private OrderDetailRepository orderDetailRepository;

	@Autowired
	private OrderRepository orderRepository;

	/**
	 * Save order detail.
	 * 
	 * @param entities
	 * @param order
	 */
	public void save(List<OrderDetail> entities, Order order) {
		entities.forEach(entity -> {
			entity.setOrder(order);
			entity.setCreatedAt(DateUtils.getCurrentDate());
			entity.setUpdatedAt(DateUtils.getCurrentDate());
		});
		orderDetailRepository.save(entities);
	}

	/**
	 * Get Order Details By Order Id
	 * 
	 * @param orderId
	 * @param pageable
	 * @return List OF OrderDetailDTO
	 */
	public List<OrderDetailDTO> getOrderDetailsByOrderId(Long orderId, Pageable pageable) {
		List<OrderDetailDTO> dtos = new ArrayList<>();
		Order order = orderRepository.findOne(orderId);
		if (order != null) {
			List<OrderDetail> entities = orderDetailRepository.findAllByOrderOrderByUpdatedAtDesc(order, pageable);
			if (entities != null) {
				dtos = entities.stream().map(OrderDetailDTO::toFullDTO).collect(Collectors.toList());
			}
		}
		return dtos;
	}
}
