package vn.com.omart.driver.dto;

public class SettingDTO {
	
	private Long id;
	private int ringIndex;
	private boolean isDisable;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getRingIndex() {
		return ringIndex;
	}

	public void setRingIndex(int ringIndex) {
		this.ringIndex = ringIndex;
	}

	public boolean isDisable() {
		return isDisable;
	}

	public void setDisable(boolean isDisable) {
		this.isDisable = isDisable;
	}

}
