package vn.com.omart.driver.service.implement;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import vn.com.omart.driver.dto.BookCarDTO;
import vn.com.omart.driver.service.RemotePoiService;

@Service
public class RemotePoiServiceImpl implements RemotePoiService {
	
	@Autowired
	private RestTemplate restTemplate;

	@Value("${poi.order.delivery.accept}")
	private String Accept_Delivery;
	
	@Override
	public void acceptDelivery(BookCarDTO dto) {
		// set param.
		if(dto.getOrderId()!=null) {
			Map<String, String> params = new HashMap<String, String>();
			params.put("order-id", dto.getOrderId().toString());
			// set booking info.
			ResponseEntity<Void> response = restTemplate.postForEntity(Accept_Delivery, dto, Void.class, params);
			HttpStatus status = response.getStatusCode();
			if (status == HttpStatus.OK) {
				System.out.println("====ACCEPT DELIVERY SUCCESS=====");
			}	
		}
					
	}
}
