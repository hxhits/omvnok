package vn.com.omart.backend.domain.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class Video {

	private String url;
	private String thumbnail;
	private Long duration;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public Video(String url, String thumbnail, Long duration) {
		super();
		this.url = url;
		this.thumbnail = thumbnail;
		this.duration = duration;
	}

	public Video() {

	}
}
