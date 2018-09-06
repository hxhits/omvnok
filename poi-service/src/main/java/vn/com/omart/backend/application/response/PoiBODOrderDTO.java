package vn.com.omart.backend.application.response;

public class PoiBODOrderDTO {
	
	private String poiName;
	private Long poiId;
	private int orderAmountCurrDate;
	private int orderAmountCurrWeek;
	private int orderAmountCurrMonth;
	private int orderAmountCurrQuater;

	public PoiBODOrderDTO() {

	}

	public PoiBODOrderDTO(int orderAmountCurrDate, int orderAmountCurrWeek, int orderAmountCurrMonth, int orderAmountCurrQuater) {
		super();
		this.orderAmountCurrDate = orderAmountCurrDate;
		this.orderAmountCurrWeek = orderAmountCurrWeek;
		this.orderAmountCurrMonth = orderAmountCurrMonth;
		this.orderAmountCurrQuater = orderAmountCurrQuater;
	}

	public int getOrderAmountCurrDate() {
		return orderAmountCurrDate;
	}

	public void setOrderAmountCurrDate(int orderAmountCurrDate) {
		this.orderAmountCurrDate = orderAmountCurrDate;
	}

	public int getOrderAmountCurrWeek() {
		return orderAmountCurrWeek;
	}

	public void setOrderAmountCurrWeek(int orderAmountCurrWeek) {
		this.orderAmountCurrWeek = orderAmountCurrWeek;
	}

	public int getOrderAmountCurrMonth() {
		return orderAmountCurrMonth;
	}

	public void setOrderAmountCurrMonth(int orderAmountCurrMonth) {
		this.orderAmountCurrMonth = orderAmountCurrMonth;
	}

	public int getOrderAmountCurrQuater() {
		return orderAmountCurrQuater;
	}

	public void setOrderAmountCurrQuater(int orderAmountCurrQuater) {
		this.orderAmountCurrQuater = orderAmountCurrQuater;
	}

	public String getPoiName() {
		return poiName;
	}

	public void setPoiName(String poiName) {
		this.poiName = poiName;
	}

	public Long getPoiId() {
		return poiId;
	}

	public void setPoiId(Long poiId) {
		this.poiId = poiId;
	}
	
}
