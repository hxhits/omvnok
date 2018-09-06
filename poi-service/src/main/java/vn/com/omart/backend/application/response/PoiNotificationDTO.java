package vn.com.omart.backend.application.response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.vdurmont.emoji.EmojiParser;

import vn.com.omart.backend.application.util.CommonUtils;
import vn.com.omart.backend.constants.OmartType;
import vn.com.omart.backend.domain.model.Image;
import vn.com.omart.backend.domain.model.PoiNotification;
import vn.com.omart.backend.domain.model.PointOfInterest;

public class PoiNotificationDTO {
	private Long id;
	private Long poiId;
	private Long catId;
	private String userId;
	private List<Image> images;
	private String description;
	private int notificationType;
	private Double latitude;
	private Double longitude;
	private Date createdAt;
	private Date updatedAt;
	private int distance;
	private PointOfInterestDTO poi;
	private RecruitmentDTO recruit;
	private String color = "";
	private String fontStyle = "";
	private int fontSize;
	private BookCarDTO bookcar;

	public static PoiNotificationDTO toDTO(Object[] objs) {
		try {
			PoiNotificationDTO dto = new PoiNotificationDTO();
			PointOfInterestDTO poi = new PointOfInterestDTO();
			dto.setPoi(poi);

			dto.setId(Long.parseLong(String.valueOf(objs[0])));
			if (objs[1] != null) {
				dto.setPoiId(Long.parseLong(String.valueOf(objs[1])));
				poi.setId(Long.parseLong(String.valueOf(objs[1]))); // POI
			}
			dto.setCatId(Long.parseLong(String.valueOf(objs[2])));
			dto.setUserId(String.valueOf(objs[3]));
			String description = EmojiParser.parseToUnicode(String.valueOf(objs[4]));
			dto.setDescription(description);

			ObjectMapper objectMapper = new ObjectMapper();
			List<Image> coverImages = objectMapper.readValue(String.valueOf(objs[5]),
					objectMapper.getTypeFactory().constructCollectionType(List.class, Image.class));

			dto.setImages(coverImages);
			dto.setNotificationType(Integer.parseInt(String.valueOf(objs[6])));
			dto.setCreatedAt((Date) objs[7]);
			if (objs[8] != null) {
				poi.setName(String.valueOf(objs[8])); // POI
			}

			if (objs[9] != null) {
				List<Image> avatarImages = objectMapper.readValue(String.valueOf(objs[9]),
						objectMapper.getTypeFactory().constructCollectionType(List.class, Image.class));
				poi.setAvatarImage(ImageDTO.from(avatarImages)); // POI
			}
			if (objs[10] != null) {
				poi.setAddress(String.valueOf(objs[10])); // POI
			}

			String val = String.valueOf(objs[11]);
			double distance = Double.valueOf(val);
			dto.setDistance(CommonUtils.milestoMeters(distance));

			if (objs[12] != null) {
				String[] phoneStr = String.valueOf(objs[12]).split(",");
				List<String> phoneList = Arrays.asList(phoneStr);
				poi.setPhone(phoneList); // POI
			}

			dto.setLatitude(Double.parseDouble(String.valueOf(objs[13])));
			dto.setLongitude(Double.parseDouble(String.valueOf(objs[14])));
			poi.setLatitude(dto.getLatitude()); // POI
			poi.setLongitude(dto.getLongitude()); // POI

			if (objs[15] != null) {
				Long recruitId = Long.parseLong(String.valueOf(objs[15]));
				RecruitmentDTO recruitDto = new RecruitmentDTO(recruitId);
				dto.setRecruit(recruitDto);
			}
			if (objs[16] != null) {
				dto.setColor(String.valueOf(objs[16]));
			}
			if (objs[17] != null) {
				dto.setFontStyle(String.valueOf(objs[17]));
			}
			dto.setFontSize(Integer.parseInt(String.valueOf(objs[18])));
			if (objs[19] != null) {
				dto.setUpdatedAt((Date) objs[19]);
			}
			if (objs[20] != null) {
				Long bookId = Long.parseLong(String.valueOf(objs[20]));
				BookCarDTO bookcarDTO = new BookCarDTO(bookId);
				dto.setBookcar(bookcarDTO);
			}
			// Add Poi OwnerId
			if (objs.length > 21 && objs[21] != null) {
				poi.setOwnerId(String.valueOf(objs[21])); // POI
			}
			return dto;
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
		return null;
	}

	public static PoiNotificationDTO toDTO(PoiNotification entity) {
		PoiNotificationDTO dto = new PoiNotificationDTO();
		dto.setCatId(entity.getCategory().id());
		dto.setCreatedAt(entity.getCreatedAt());
		dto.setUpdatedAt(entity.getUpdatedAt());
		dto.setDescription(entity.getDescription());
		dto.setId(entity.getId());
		dto.setImages(entity.getImages());
		dto.setLatitude(entity.getLatitude());
		dto.setLongitude(entity.getLongitude());
		dto.setPoiId(entity.getPoi().id());
		dto.setUserId(entity.getUserId());
		dto.setNotificationType(entity.getNotificationType());
		PointOfInterestDTO poi = new PointOfInterestDTO();
		poi.setAddress(entity.getAddress());
		poi.setAvatarImage(ImageDTO.from(entity.getAvatarImages()));
		poi.setName(entity.getName());
		if (entity.getPhone() != null) {
			List<String> phoneList = Arrays.asList(entity.getPhone().split(","));
			poi.setPhone(phoneList);
		}
		PointOfInterest poie = entity.getPoi();
		if (poie != null) {
			poi.setId(poie.id());
			poi.setLatitude(poie.latitude());
			poi.setLongitude(poie.longitude());
			poi.setOwnerId(poie.ownerId());
		}
		dto.setPoi(poi);
		if (entity.getRecruit() != null) {
			// RecruitmentDTO recruitmentDTO = new
			// RecruitmentDTO(entity.getRecruit().getId());
			RecruitmentDTO recruitmentDTO = RecruitmentDTO.toDTO(entity.getRecruit());
			dto.setRecruit(recruitmentDTO);
		}
		dto.setColor(entity.getColor());
		dto.setFontStyle(entity.getFontStyle());
		dto.setFontSize(entity.getFontSize());
		return dto;
	}

	public static PoiNotification toEntity(PoiNotificationDTO dto) {
		PoiNotification entity = new PoiNotification();
		entity.setCreatedAt(new Date());
		entity.setUpdatedAt(new Date());
		entity.setDescription(dto.getDescription());
		entity.setColor(dto.getColor());
		entity.setFontSize(dto.getFontSize());
		entity.setFontStyle(dto.getFontStyle());
		if (dto.getImages() != null) {
			entity.setImages(dto.getImages());
		} else {
			entity.setImages(new ArrayList<>());
		}
		entity.setNotificationType(OmartType.NotificationType.POI.getId());
		if (OmartType.NotificationType.getById(dto.getNotificationType()) == OmartType.NotificationType.RECRUIT) {
			entity.setNotificationType(OmartType.NotificationType.RECRUIT.getId());
		}
		return entity;
	}

	public static PoiNotification mergeToEntity(PoiNotification entity, PoiNotificationDTO dto) {
		entity.setDescription(dto.getDescription());
		if (dto.getImages() != null) {
			entity.setImages(dto.getImages());
		} else {
			entity.setImages(new ArrayList<>());
		}
		entity.setUpdatedAt(new Date());
		entity.setColor(dto.getColor());
		entity.setFontSize(dto.getFontSize());
		entity.setFontStyle(dto.getFontStyle());
		return entity;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPoiId() {
		return poiId;
	}

	public void setPoiId(Long poiId) {
		this.poiId = poiId;
	}

	public Long getCatId() {
		return catId;
	}

	public void setCatId(Long catId) {
		this.catId = catId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public List<Image> getImages() {
		return images;
	}

	public void setImages(List<Image> images) {
		this.images = images;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(int notificationType) {
		this.notificationType = notificationType;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public PointOfInterestDTO getPoi() {
		return poi;
	}

	public void setPoi(PointOfInterestDTO poi) {
		this.poi = poi;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public RecruitmentDTO getRecruit() {
		return recruit;
	}

	public void setRecruit(RecruitmentDTO recruit) {
		this.recruit = recruit;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getFontStyle() {
		return fontStyle;
	}

	public void setFontStyle(String fontStyle) {
		this.fontStyle = fontStyle;
	}

	public int getFontSize() {
		return fontSize;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public BookCarDTO getBookcar() {
		return bookcar;
	}

	public void setBookcar(BookCarDTO bookcar) {
		this.bookcar = bookcar;
	}

}
