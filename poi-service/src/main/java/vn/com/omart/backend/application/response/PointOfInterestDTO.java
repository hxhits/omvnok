package vn.com.omart.backend.application.response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vdurmont.emoji.EmojiParser;

import lombok.Data;
import vn.com.omart.backend.application.util.CommonUtils;
import vn.com.omart.backend.application.util.PoiState;
import vn.com.omart.backend.domain.model.Image;
import vn.com.omart.backend.domain.model.PoiAction;
import vn.com.omart.backend.domain.model.PointOfInterest;
import vn.com.omart.backend.port.adapter.elasticsearch.POIAdmin;
import vn.com.omart.sharedkernel.application.model.dto.EntityMapper;

@Data
public class PointOfInterestDTO {

	private CategoryDTO category;

	private String ownerId;

	private Long id;

	private String name;

	private String description;

	private String career;

	private String address;

	private ProvinceDTO province;

	private DistrictDTO district;

	private WardDTO ward;

	private String snapshotMap;

	private List<String> phone;

	private Double latitude;

	private Double longitude;

	private Double distance;

	private Double openHour;

	private Double closeHour;

	private boolean isOpening;

	private PoiState poiState;

	private List<ImageDTO> avatarImage;

	private List<ImageDTO> coverImage;

	private List<ImageDTO> featuredImage;

	private List<ImageDTO> bannerImages;

	private boolean isBannerApproved;

	private String createdBy;

	private Date createdAt;

	private String updatedBy;

	private Date updatedAt;

	private List<PoiAction> poiActions;

	private List<PoiCommentDTO> poiComments;
	private List<PoiPictureDTO> poiPictures;

	private List<ItemDTO> items;

	private PoiStatsDTO stats;

	private JSONObject userAction;

	private List<PoiPictureActionDTO> pictureActions;

	private Date lastNotification;

	private boolean isDeleted;
	
	private boolean isApproved;

	private int displayState;

	private String email;

	private String webAddress;

	private int poiType;

	private String fax;

	private String tel;

	private String tax;

	private String facebook;

	private String twitter;
	
	private String reason;
	
	private boolean disableSale;
	
	private int ringIndex;
	private int deliveryRadius;
	private int currency;
	private int discount;
	/**
	 * Mapper to map Entity <=> DTO
	 */
	public static class BannerPOIMapper implements EntityMapper<PointOfInterestDTO, PointOfInterest> {

		@Override
		public PointOfInterestDTO map(PointOfInterest entity) {
			PointOfInterestDTO dto = new PointOfInterestDTO();

			dto.category = CategoryDTO.from(entity.category());
			dto.id = entity.id();
			dto.name = entity.name();

			dto.address = entity.address();

			dto.province = ProvinceDTO.from(entity.province());
			dto.district = DistrictDTO.from(entity.district());
			dto.ward = WardDTO.from(entity.ward());

			dto.bannerImages = ImageDTO.from(entity.bannerImages());
			dto.isBannerApproved = entity.isBannerApprored();

			dto.isDeleted = entity.getIsDeleted();

			return dto;
		}

		@Override
		public void map(PointOfInterest entity, PointOfInterestDTO dto) {
			throw new UnsupportedOperationException("Not Implement");
		}
	}

	public static PointOfInterestDTO from(Object[] modelObj) {
		PointOfInterest model = (PointOfInterest) modelObj[0];
		PointOfInterestDTO dto = from(model);
		Double distance = (Double) modelObj[1];
		if (null != distance) {
			dto.distance = distance;
		}
		return dto;
	}

	public static PointOfInterestDTO toDTO(Object[] modelObj) {
		PointOfInterest model = (PointOfInterest) modelObj[0];
		PointOfInterestDTO dto = from(model);
		Double distance = (Double) modelObj[1];
		if (null != distance) {
			dto.distance = Double.parseDouble(String.valueOf(CommonUtils.milestoMeters(distance)));
		}
		return dto;
	}

	public static List<PointOfInterestDTO> objectToDTO(List<Object[]> poiObj, List<Long> ids) {
		List<PointOfInterestDTO> poiDTOList = new ArrayList<>();
		ObjectMapper objectMapper = new ObjectMapper();
		for (Object[] obj : poiObj) {
			try {
				PointOfInterestDTO dto = new PointOfInterestDTO();
				Long id = Long.valueOf(String.valueOf(obj[0]));
				dto.setId(id);
				String name = String.valueOf(obj[1]);
				dto.setName(name);
				if (obj[2] != null) {
					String desc = EmojiParser.parseToUnicode(String.valueOf(obj[2]));
					dto.setDescription(desc);
				}
				if (obj[3] != null) {
					String x = String.valueOf(obj[3]);
					List<Image> avatarImages = objectMapper.readValue(x,
							objectMapper.getTypeFactory().constructCollectionType(List.class, Image.class));
					dto.setAvatarImage(ImageDTO.from(avatarImages));
				}
				if (obj[4] != null) {
					List<Image> coverImages = objectMapper.readValue(String.valueOf(obj[4]),
							objectMapper.getTypeFactory().constructCollectionType(List.class, Image.class));
					dto.setCoverImage(ImageDTO.from(coverImages));
				}
				String ownerId = String.valueOf(obj[5]);
				dto.setOwnerId(ownerId);
				Double openHour = Double.valueOf(String.valueOf(obj[6]));
				dto.setOpenHour(openHour);
				Double closeHour = Double.valueOf(String.valueOf(obj[7]));
				dto.setCloseHour(closeHour);
				Boolean isOpening = Boolean.valueOf(String.valueOf(obj[8]));
				dto.setOpening(isOpening);

				int poiStateId = Integer.valueOf(String.valueOf(obj[9]));
				dto.setPoiState(PoiState.getById(poiStateId));
				if (obj[10] != null) {
					dto.setCareer(String.valueOf(obj[10]));
				}
				Double distance = Double.valueOf(String.valueOf(obj[11]));
				dto.setDistance(Double.parseDouble(String.valueOf(CommonUtils.milestoMeters(distance))));

				if (obj[12] != null) {
					dto.setLastNotification((Date) obj[12]);
				}
				dto.setDisplayState(Integer.parseInt(String.valueOf(obj[13])));
				ids.add(id);
				poiDTOList.add(dto);
			} catch (JsonParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return poiDTOList;

	}

	public static List<PointOfInterestDTO> objectToDTOWithNoLocation(List<Object[]> poiObj, List<Long> ids) {
		List<PointOfInterestDTO> poiDTOList = new ArrayList<>();
		ObjectMapper objectMapper = new ObjectMapper();
		for (Object[] obj : poiObj) {
			try {
				PointOfInterestDTO dto = new PointOfInterestDTO();
				Long id = Long.valueOf(String.valueOf(obj[0]));
				dto.setId(id);
				String name = String.valueOf(obj[1]);
				dto.setName(name);
				if (obj[2] != null) {
					String desc = EmojiParser.parseToUnicode(String.valueOf(obj[2]));
					dto.setDescription(desc);
				}
				if (obj[3] != null) {
					String x = String.valueOf(obj[3]);
					List<Image> avatarImages = objectMapper.readValue(x,
							objectMapper.getTypeFactory().constructCollectionType(List.class, Image.class));
					dto.setAvatarImage(ImageDTO.from(avatarImages));
				}
				if (obj[4] != null) {
					List<Image> coverImages = objectMapper.readValue(String.valueOf(obj[4]),
							objectMapper.getTypeFactory().constructCollectionType(List.class, Image.class));
					dto.setCoverImage(ImageDTO.from(coverImages));
				}
				String ownerId = String.valueOf(obj[5]);
				dto.setOwnerId(ownerId);
				Double openHour = Double.valueOf(String.valueOf(obj[6]));
				dto.setOpenHour(openHour);
				Double closeHour = Double.valueOf(String.valueOf(obj[7]));
				dto.setCloseHour(closeHour);
				Boolean isOpening = Boolean.valueOf(String.valueOf(obj[8]));
				dto.setOpening(isOpening);

				int poiStateId = Integer.valueOf(String.valueOf(obj[9]));
				dto.setPoiState(PoiState.getById(poiStateId));
				if (obj[10] != null) {
					dto.setCareer(String.valueOf(obj[10]));
				}

				dto.setDistance(0d);

				if (obj[11] != null) {
					dto.setLastNotification((Date) obj[11]);
				}
				dto.setDisplayState(Integer.parseInt(String.valueOf(obj[12])));
				ids.add(id);
				poiDTOList.add(dto);
			} catch (JsonParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return poiDTOList;

	}

	public static List<PointOfInterestDTO> entityToDTO(List<PointOfInterest> entitys, List<Long> ids) {
		List<PointOfInterestDTO> poiDTOList = new ArrayList<>();
		for (PointOfInterest entity : entitys) {
			PointOfInterestDTO dto = new PointOfInterestDTO();
			Long id = entity.id();
			dto.setId(id);
			dto.setName(entity.name());
			String desc = entity.description() == null ? "" : EmojiParser.parseToUnicode(entity.description());
			dto.setDescription(desc);
			dto.setCareer(entity.getCareer());
			dto.setAvatarImage(ImageDTO.from(entity.avatarImage()));
			dto.setCoverImage(ImageDTO.from(entity.coverImage()));
			dto.setOwnerId(entity.ownerId());
			dto.setOpenHour(entity.openHour());
			dto.setCloseHour(entity.closeHour());
			dto.setOpening(entity.isOpening());
			dto.setPoiState(PoiState.getById(entity.getPoiState()));
			dto.setDeleted(entity.getIsDeleted());
			dto.setCreatedAt(entity.createdAt());
			dto.setDisplayState(entity.getDisplayState());
			if (entity.getLastNotification() != null) {
				dto.setLastNotification(entity.getLastNotification());
			}
			ids.add(id);
			poiDTOList.add(dto);
		}
		return poiDTOList;
	}

	public static PointOfInterest update(PointOfInterest entity, PointOfInterestDTO dto) {
		// 2018-06-09
		entity.setPoiType(dto.getPoiType());
		entity.setEmail(dto.getEmail());
		entity.setWebAddress(dto.getWebAddress());
		entity.setFax(dto.getFax());
		entity.setTel(dto.getTel());
		entity.setTax(dto.getTax());
		entity.setFacebook(dto.getFacebook());
		entity.setTwitter(dto.getTwitter());

		return entity;
	}

	public static PointOfInterestDTO toBannerApproved(PointOfInterest model) {
		PointOfInterestDTO dto = new PointOfInterestDTO();
			dto.setId(model.id());
			dto.setBannerApproved(model.isApproved());
			dto.setBannerImages(ImageDTO.from(model.getBannerImages()));
		return dto;
	}
	
	public static PointOfInterestDTO from(PointOfInterest model) {
		PointOfInterestDTO dto = new PointOfInterestDTO();

		dto.category = CategoryDTO.from(model.category());

		dto.ownerId = model.ownerId();

		dto.id = model.id();
		dto.name = model.name();

		dto.address = model.address();
		String desc = model.description() == null ? "" : EmojiParser.parseToUnicode(model.description());
		dto.description = desc;
		dto.career = model.getCareer();
		dto.province = ProvinceDTO.from(model.province());
		dto.district = DistrictDTO.from(model.district());
		dto.ward = WardDTO.from(model.ward());

		dto.snapshotMap = model.snapshotMap();

		if (StringUtils.isBlank(model.phone())) {
			dto.phone = new ArrayList<>();
		} else {
			dto.phone = Arrays.asList(model.phone().split(","));
		}

		dto.latitude = model.latitude();
		dto.longitude = model.longitude();

		dto.openHour = model.openHour();
		dto.closeHour = model.closeHour();
		dto.isOpening = model.isOpening();

		dto.poiState = PoiState.getById(model.getPoiState());

		dto.avatarImage = ImageDTO.from(model.avatarImage());
		dto.coverImage = ImageDTO.from(model.coverImage());
		dto.featuredImage = ImageDTO.from(model.featuredImage());

		dto.bannerImages = ImageDTO.from(model.bannerImages());
		dto.isBannerApproved = model.isBannerApprored();

		dto.createdBy = model.createdBy();
		dto.createdAt = model.createdAt();

		dto.updatedBy = model.updatedBy();
		dto.updatedAt = model.updatedAt();

		dto.isDeleted = model.getIsDeleted();
		dto.displayState = model.getDisplayState();
		dto.webAddress = model.getWebAddress();
		dto.email = model.getEmail();
		dto.isApproved = model.isApproved();
		// 2018-06-09
		dto.setPoiType(model.getPoiType());
		dto.setFax(model.getFax());
		dto.setTel(model.getTel());
		dto.setTax(model.getTax());
		dto.setFacebook(model.getFacebook());
		dto.setTwitter(model.getTwitter());
		dto.setReason(model.getReason());
		dto.setDisableSale(model.isDisableSaleFeature());
		dto.setRingIndex(model.getRingIndex());
		dto.setCurrency(model.getCurrency());
		dto.setDeliveryRadius(model.getDeliveryRadius());
		dto.setDiscount(model.getDiscount());
		return dto;
	}

	public static PointOfInterestDTO toBasicDTO(PointOfInterest poiEntity) {
		PointOfInterestDTO poiDTO = new PointOfInterestDTO();
		poiDTO.setName(poiEntity.getName());
		poiDTO.setAvatarImage(ImageDTO.from(poiEntity.getAvatarImage()));
		poiDTO.setCoverImage(ImageDTO.from(poiEntity.getCoverImage()));
		poiDTO.setProvince(ProvinceDTO.from(poiEntity.province()));
		poiDTO.setDistrict(DistrictDTO.from(poiEntity.district()));
		poiDTO.setWard(WardDTO.from(poiEntity.ward()));
		poiDTO.setAddress(poiEntity.getAddress());
		poiDTO.setId(poiEntity.id());
		poiDTO.setLatitude(poiEntity.latitude());
		poiDTO.setLongitude(poiEntity.longitude());
		poiDTO.setOwnerId(poiEntity.ownerId());
		poiDTO.setRingIndex(poiEntity.getRingIndex());
		poiDTO.setCurrency(poiEntity.getCurrency());
		poiDTO.setDiscount(poiEntity.getDiscount());
		poiDTO.setDisableSale(poiEntity.isDisableSaleFeature());
		CategoryDTO category = new CategoryDTO();
		category.setId(poiEntity.category().id());
		poiDTO.setCategory(category);
		poiDTO.setPoiState(PoiState.getById(poiEntity.getPoiState()));
		if (StringUtils.isBlank(poiEntity.phone())) {
			poiDTO.setPhone(new ArrayList<>());
		} else {
			poiDTO.setPhone(Arrays.asList(poiEntity.phone().split(",")));
		}
		return poiDTO;
	}
	
	public static String getStringValueOfObject(Object obj) {
		if(obj!=null) {
			return String.valueOf(obj);
		}
		return "";
	}

	public PointOfInterestDTO(String ownerId, Long id) {
		super();
		this.ownerId = ownerId;
		this.id = id;
	}

	public PointOfInterestDTO() {
		super();
	}
	
	public static class QueryMapper implements EntityMapper<PointOfInterestDTO, PointOfInterest>{

		@Override
		public PointOfInterestDTO map(PointOfInterest entity) {
			// TODO Auto-generated method stub
			return from(entity);
		}

		@Override
		public void map(PointOfInterest entity, PointOfInterestDTO dto) {
			// TODO Auto-generated method stub
			
		}
		
	}

}
