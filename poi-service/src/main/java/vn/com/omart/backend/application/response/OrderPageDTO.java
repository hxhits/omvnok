package vn.com.omart.backend.application.response;

import java.util.List;
import lombok.Data;

@Data
public class OrderPageDTO {
	private List<OrderDTO> orders;
	private Long size;
}
