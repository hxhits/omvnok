package vn.com.omart.messenger.common.constant;

public enum Device {
	
	undefine(0, "undefine") , android(1, "android"), ios(2, "ios"), web(3, "web"), desktop(4, "desktop");

	private int deviceId;
	private String lable;

	private Device(int deviceId, String lable) {
		this.deviceId = deviceId;
		this.lable = lable;
	}

	public int getId() {
		return deviceId;
	}

	public String getLable() {
		return lable;
	}

	public void setLable(String lable) {
		this.lable = lable;
	}

	public static boolean contains(int deviceType) {
		for (Device device : Device.values()) {
			if (device.getId() == deviceType) {
				return true;
			}
		}
		return false;
	}

	public static Device getById(int id) {
		for (Device device : Device.values()) {
			if (device.getId() == id) {
				return device;
			}
		}
		return null;
	}
}
