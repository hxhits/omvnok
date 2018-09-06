package vn.com.omart.messenger.application.request;

import java.util.List;

public class DeviceGroupToken {
	
	private List<String> iosTokens;
	private List<String> androidTokens;
	private List<String> webTokens;
	private List<String> desktopTokens;
	
	public List<String> getIosTokens() {
		return iosTokens;
	}
	public void setIosTokens(List<String> iosTokens) {
		this.iosTokens = iosTokens;
	}
	public List<String> getAndroidTokens() {
		return androidTokens;
	}
	public void setAndroidTokens(List<String> androidTokens) {
		this.androidTokens = androidTokens;
	}
	public List<String> getWebTokens() {
		return webTokens;
	}
	public void setWebTokens(List<String> webTokens) {
		this.webTokens = webTokens;
	}
	public List<String> getDesktopTokens() {
		return desktopTokens;
	}
	public void setDesktopTokens(List<String> desktopTokens) {
		this.desktopTokens = desktopTokens;
	}
	
}
