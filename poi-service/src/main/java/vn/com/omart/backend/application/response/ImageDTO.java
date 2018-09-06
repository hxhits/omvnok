package vn.com.omart.backend.application.response;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Data;
import vn.com.omart.backend.domain.model.Image;

@Data
public class ImageDTO {

	private String url;

	public ImageDTO() {
	}

	public ImageDTO(String url) {
		this.url = url;
	}

	public static ImageDTO from(Image image) {
		return new ImageDTO(image.url());
	}

	public static List<ImageDTO> from(List<Image> images) {
		if (images == null) {
			return Arrays.asList(new ImageDTO());
		}
		return images.stream().map(ImageDTO::from).collect(Collectors.toList());
	}
}