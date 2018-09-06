package vn.com.omart.backend.port.adapter.elasticsearch;


import java.util.Date;
import java.util.List;

import org.elasticsearch.common.geo.GeoPoint;
import org.hibernate.loader.collection.OneToManyJoinWalker;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import lombok.Data;
import vn.com.omart.backend.domain.model.Image;
import vn.com.omart.backend.domain.model.PointOfInterest;
import vn.com.omart.sharedkernel.StringUtils;

@Document(indexName = "omart_admin", type = "pois_admin")
@Data
public class POIAdmin {

	@Id
	private Long id;
	private String ownerId;
	private Long categoryId;
	private String categoryName;
	private String categoryNameNormalize;

	private String name;
	private String nameNormalize;

	private String categoryKeywords;
	private String categoryKeywordsNormalize;

	private String description;
	private String descriptionNormalize;
	private String address;
	private String addressNormalize;
	private String wardName;
	private String wardNameNormalize;
	private String districtName;
	private String districtNameNormalize;
	private String provinceName;
	private String provinceNameNormalize;
	private String phone; // list split by comma

	private Double latitude;
	private Double longitude;
	private GeoPoint location;

	private Double openHour;
	private Double closeHour;
	private boolean isOpening;
	private int poiState;
	private int displayState;
	private String avatarImage;
	private List<Image> coverImage;
	private String career;
	private boolean isDeleted;
	private boolean isApproved;
	private Date createdAt;
	private Date updatedAt;

	public static POIAdmin from(PointOfInterest model) {
		POIAdmin obj = new POIAdmin();

		obj.id = model.id();
		obj.categoryId = model.category().id();

		obj.categoryName = model.category().name();
		obj.categoryNameNormalize = StringUtils.removeAccent(obj.categoryName);

		obj.categoryKeywords = model.category().keywords();
		obj.categoryKeywordsNormalize = StringUtils.removeAccent(obj.categoryKeywords);

		obj.name = model.name();
		obj.nameNormalize = StringUtils.removeAccent(obj.name);

		obj.description = model.description();
		obj.descriptionNormalize = StringUtils.removeAccent(obj.description);

		obj.address = model.address();
		obj.addressNormalize = StringUtils.removeAccent(obj.address);

		obj.wardName = model.ward().name();
		obj.wardNameNormalize = StringUtils.removeAccent(obj.wardName);

		obj.districtName = model.district().name();
		obj.districtNameNormalize = StringUtils.removeAccent(obj.districtName);
		obj.provinceName = model.province().name();
		obj.provinceNameNormalize = StringUtils.removeAccent(obj.provinceName);
		obj.phone = model.phone();
		obj.latitude = model.latitude();
		obj.longitude = model.longitude();
		obj.location = new GeoPoint(model.latitude(), model.longitude());
		obj.openHour = model.openHour();
		obj.closeHour = model.closeHour();
		obj.isOpening = model.isOpening();
		obj.poiState = model.getPoiState();
		obj.displayState = model.getDisplayState();
		if (null != model.avatarImage() && !model.avatarImage().isEmpty()) {
			obj.avatarImage = model.avatarImage().get(0).url();
		}
		obj.coverImage = model.coverImage();
		obj.career = model.getCareer();
		obj.isApproved = model.isApproved();
		obj.isDeleted =  model.getIsDeleted();
		obj.ownerId = model.ownerId();
		obj.createdAt = model.createdAt();
		obj.updatedAt = model.updatedAt();
		return obj;
	}
}