package vn.com.omart.backend.application.request;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PointOfInterestCmd {

	@Data
	@ToString
	public static class CreateOrUpdate {

		@NotNull
		private Long categoryId;

		@NotNull
		private String ownerId;

		@NotBlank
		private String name;

		private String description;

		private String career;

		@NotBlank
		private String address;

		@NotNull
		private Integer wardId;

		@NotNull
		private Integer districtId;

		@NotNull
		private Integer provinceId;

		private String snapshotMap;

		private List<String> phone;

		@NotNull
		private Double latitude;
		@NotNull
		private Double longitude;

		@NotNull
		private Double openHour;
		@NotNull
		private Double closeHour;

		private boolean isOpening;

		private boolean noCheckProvince;

		private List<ImageCmd> avatarImage;
		private List<ImageCmd> coverImage;
		private List<ImageCmd> featuredImage;
		private String email = "";
		private String webAddress = "";
		private int poiType;
		private int deliveryRadius;
		private int currency;
	}

	@Data
	@ToString
	public static class ImageCmd {
		private String url;
	}
}
