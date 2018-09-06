package vn.com.omart.messenger.domain.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class Image {

	private String url;

	public Image() {
	}

	public Image(String url) {
		this.url = url;
	}

	public String url() {
		return url;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
