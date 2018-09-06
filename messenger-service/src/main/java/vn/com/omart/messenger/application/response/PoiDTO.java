package vn.com.omart.messenger.application.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import vn.com.omart.messenger.domain.model.Image;
import vn.com.omart.messenger.domain.model.PointOfInterest;

/**
 * Data Transfer Object Poi.
 * 
 * @author Win10
 *
 */
public class PoiDTO implements Serializable {
	private static final long serialVersionUID = 5193497973967169760L;
	private Long id;
	private String ownerId;
	private String name;
	private List<Image> avatarImage;
	private List<String> phone;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Image> getAvatarImage() {
		return avatarImage;
	}

	public void setAvatarImage(List<Image> avatarImage) {
		this.avatarImage = avatarImage;
	}

	public List<String> getPhone() {
		return phone;
	}

	public void setPhone(List<String> phone) {
		this.phone = phone;
	}

	public static PoiDTO toDTO(PointOfInterest poi) {
		PoiDTO poiDTO = new PoiDTO();
		poiDTO.setId(poi.id());
		poiDTO.setName(poi.name());
		poiDTO.setOwnerId(poi.ownerId());
		poiDTO.setAvatarImage(poi.getAvatarImage());
		// add phone list.
		String phones = poi.phone();
		if (!StringUtils.isBlank(phones)) {
			List<String> phoneLists = new ArrayList<>();
			String[] phoneArrays = phones.split(",");
			for (int i = 0; i < phoneArrays.length; i++) {
				phoneLists.add(phoneArrays[i]);
			}
			poiDTO.setPhone(phoneLists);
		}

		return poiDTO;
	}

}
