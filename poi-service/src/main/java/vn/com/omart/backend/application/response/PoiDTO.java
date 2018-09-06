package vn.com.omart.backend.application.response;

import java.util.ArrayList;
import java.util.List;

import com.vdurmont.emoji.EmojiParser;

import vn.com.omart.backend.application.util.PoiState;
import vn.com.omart.backend.domain.model.Image;
import vn.com.omart.backend.domain.model.PoiPicture;

public class PoiDTO {
	public Long poiId;
	public List<Image> covers;
	public List<Image> avatars;
	public List<PoiPicture> poiPictures;
	private String name;
	private String address;
	private String description;
	private Double openHour;
	private Double closeHour;
	private String phone;
	private PoiState poiState;
	private int displayState;
	private String email;
	private String webAddress;
	public List<Image> getCovers() {
		if (this.covers == null) {
			this.covers = new ArrayList<Image>();
		}
		return covers;
	}

	public void setCovers(List<Image> covers) {
		this.covers = covers;
	}

	public List<Image> getAvatars() {
		if (this.avatars == null) {
			this.avatars = new ArrayList<Image>();
		}
		return avatars;
	}

	public void setAvatars(List<Image> avatars) {
		this.avatars = avatars;
	}

	public Long getPoiId() {
		return poiId;
	}

	public void setPoiId(Long poiId) {
		this.poiId = poiId;
	}

	public List<PoiPicture> getPoiPictures() {
		return poiPictures;
	}

	public void setPoiPictures(List<PoiPicture> poiPictures) {
		this.poiPictures = poiPictures;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDescription() {
		if (description != null) {
			return EmojiParser.parseToUnicode(description);
		}
		return null;
	}

	public void setDescription(String description) {
		if (description != null) {
			this.description = EmojiParser.parseToAliases(description);
		}
	}

	public Double getOpenHour() {
		return openHour;
	}

	public void setOpenHour(Double openHour) {
		this.openHour = openHour;
	}

	public Double getCloseHour() {
		return closeHour;
	}

	public void setCloseHour(Double closeHour) {
		this.closeHour = closeHour;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public PoiState getPoiState() {
		return poiState;
	}

	public void setPoiState(PoiState poiState) {
		this.poiState = poiState;
	}

	public int getDisplayState() {
		return displayState;
	}

	public void setDisplayState(int displayState) {
		this.displayState = displayState;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getWebAddress() {
		return webAddress;
	}

	public void setWebAddress(String webAddress) {
		this.webAddress = webAddress;
	}

}
