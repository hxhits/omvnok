package vn.com.omart.backend.application.response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import lombok.Data;
import vn.com.omart.backend.application.util.PoiState;
import vn.com.omart.backend.port.adapter.elasticsearch.POI;
import vn.com.omart.backend.port.adapter.elasticsearch.POIAdmin;
import vn.com.omart.sharedkernel.GeoCalculator;
import vn.com.omart.sharedkernel.application.model.dto.EntityMapper;

@Data
public class SearchPointOfInterestDTO {

	private Long id;
	private String ownerId;
	private Long categoryId;
	private String categoryName;

	private String name;
	private String description;
	private String address;
	private String wardName;
	private String districtName;
	private String provinceName;
	private List<String> phone; // list split by comma
	private Long distance;

	private Double openHour;
	private Double closeHour;
	private boolean isOpening;
	private PoiState poiState;
	private int displayState;

	private List<ImageDTO> avatarImage;
	private List<ImageDTO> coverImage;
	private String career;
	private boolean isDeleted;
	private boolean isApproved;
	private Date createdAt;
	private Date updatedAt;

	public static SearchPointOfInterestDTO from(POI doc, String locationGeo) {
		SearchPointOfInterestDTO dto = new SearchPointOfInterestDTO();

		dto.id = doc.getId();
		dto.categoryId = doc.getCategoryId();
		dto.categoryName = doc.getCategoryName();

		dto.name = doc.getName();
		dto.description = doc.getDescription();
		dto.address = doc.getAddress();
		dto.wardName = doc.getWardName();
		dto.districtName = doc.getDistrictName();
		dto.provinceName = doc.getProvinceName();

		if (StringUtils.isBlank(doc.getPhone())) {
			dto.phone = new ArrayList<>();
		} else {
			dto.phone = Arrays.asList(doc.getPhone().split(","));
		}

		dto.openHour = doc.getOpenHour();
		dto.closeHour = doc.getCloseHour();
		dto.isOpening = doc.isOpening();
		dto.poiState = PoiState.getById(doc.getPoiState());
		dto.avatarImage = Collections.singletonList(new ImageDTO(doc.getAvatarImage()));

		dto.coverImage = ImageDTO.from(doc.getCoverImage());
		dto.displayState = doc.getDisplayState();
		dto.career = doc.getCareer();
		if (!StringUtils.isBlank(locationGeo)) {
			Double lat = Double.valueOf(locationGeo.split(",")[0]);
			Double lon = Double.valueOf(locationGeo.split(",")[1]);

			// distance in m
			dto.distance = new Double(
					GeoCalculator.distance(lat, lon, doc.getLatitude(), doc.getLongitude(), "K") * 1000).longValue();
		}

		return dto;
	}

	public static SearchPointOfInterestDTO from(POIAdmin doc) {
		SearchPointOfInterestDTO dto = new SearchPointOfInterestDTO();

		dto.id = doc.getId();
		dto.categoryId = doc.getCategoryId();
		dto.categoryName = doc.getCategoryName();

		dto.name = doc.getName();
		dto.description = doc.getDescription();
		dto.address = doc.getAddress();
		dto.wardName = doc.getWardName();
		dto.districtName = doc.getDistrictName();
		dto.provinceName = doc.getProvinceName();

		if (StringUtils.isBlank(doc.getPhone())) {
			dto.phone = new ArrayList<>();
		} else {
			dto.phone = Arrays.asList(doc.getPhone().split(","));
		}

		dto.openHour = doc.getOpenHour();
		dto.closeHour = doc.getCloseHour();
		dto.isOpening = doc.isOpening();
		dto.poiState = PoiState.getById(doc.getPoiState());
		dto.avatarImage = Collections.singletonList(new ImageDTO(doc.getAvatarImage()));

		dto.coverImage = ImageDTO.from(doc.getCoverImage());
		dto.displayState = doc.getDisplayState();
		dto.career = doc.getCareer();
		dto.isDeleted = doc.isDeleted();
		dto.isApproved = doc.isApproved();
		dto.ownerId = doc.getOwnerId();
		dto.createdAt = doc.getCreatedAt();
		dto.updatedAt = doc.getUpdatedAt();
		return dto;
	}

	public static class QueryMapper implements EntityMapper<SearchPointOfInterestDTO, POIAdmin> {

		@Override
		public SearchPointOfInterestDTO map(POIAdmin entity) {
			// TODO Auto-generated method stub
			return from(entity);
		}

		@Override
		public void map(POIAdmin entity, SearchPointOfInterestDTO dto) {
			// TODO Auto-generated method stub

		}

	}

}
