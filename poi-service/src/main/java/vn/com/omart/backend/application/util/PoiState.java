package vn.com.omart.backend.application.util;

public enum PoiState {
	
	OPEN(0, "open"), CLOSE(1, "close"), PAUSE(2, "pause");

	private int id;
	private String lable;

	private PoiState(int id, String lable) {
		this.id = id;
		this.lable = lable;
	}

	public int getId() {
		return id;
	}

	public String getLable() {
		return lable;
	}

	public void setLable(String lable) {
		this.lable = lable;
	}

	public static boolean contains(int id) {
		for (PoiState poiState : PoiState.values()) {
			if (poiState.getId() == id) {
				return true;
			}
		}
		return false;
	}

	public static PoiState getById(int id) {
		for (PoiState poiState : PoiState.values()) {
			if (poiState.getId() == id) {
				return poiState;
			}
		}
		return null;
	}

	public static PoiState getByLable(String label) {
		for (PoiState poiState : PoiState.values()) {
			if (poiState.getLable().equalsIgnoreCase(label)) {
				return poiState;
			}
		}
		return null;
	}
}
