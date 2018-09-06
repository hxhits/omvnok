package vn.com.omart.backend.application.response;

import lombok.Data;

@Data
public class OrderReportDTO {
	private Long allOrderAll;
	private Long newOrderAll;
	private Long doneOrderAll;
	private Long allOrderMonth;
	private Long newOrderMonth;
	private Long doneOrderMonth;
	private Long allOrderDate;
	private Long newOrderDate;
	private Long doneOrderDate;
}
