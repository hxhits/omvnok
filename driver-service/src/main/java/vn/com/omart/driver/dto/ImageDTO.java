package vn.com.omart.driver.dto;

public class ImageDTO {
	
	private String url;

	public ImageDTO() {
		super();
	}
	
	public ImageDTO(String url) {
		super();
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
