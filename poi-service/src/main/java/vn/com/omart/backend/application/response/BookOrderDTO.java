package vn.com.omart.backend.application.response;

public class BookOrderDTO {
	
	private Long orderId;
	private String poiAddress;
	private int shipFee;
	
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public String getPoiAddress() {
		return poiAddress;
	}
	public void setPoiAddress(String poiAddress) {
		this.poiAddress = poiAddress;
	}
	public int getShipFee() {
		return shipFee;
	}
	public void setShipFee(int shipFee) {
		this.shipFee = shipFee;
	}
}
