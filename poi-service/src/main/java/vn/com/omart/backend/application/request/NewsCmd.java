package vn.com.omart.backend.application.request;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class NewsCmd {

	@Data
	@ToString
	public static class CreateOrUpdate {

		private String title;

		private String desc;

		private String thumbnailUrl;

		private String bannerUrl;

		private int type;

		private boolean read;

		private String approval;
	}

	@Data
	@ToString
	public static class CreateNew {

		private String title;

		private String desc;

		private String thumbnailUrl;

		private String bannerUrl;

		private int type;

		private boolean read;

	}
}
