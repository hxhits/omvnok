package vn.com.omart.backend.application.response;

import lombok.Data;

@Data
public class PointOfInterestShortDTO {
	private Long id;
	private String name;
	private String description;
	private String address;
	private String avatarImage;
}
