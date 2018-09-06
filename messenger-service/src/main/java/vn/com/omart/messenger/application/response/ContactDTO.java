package vn.com.omart.messenger.application.response;

import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import vn.com.omart.messenger.domain.model.Contact;

public class ContactDTO {

	private Long id;
	private String name;
	private String userId;
	private String phoneNumber;
	private String omartId;
	private String avatar;
	private int type;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOmartId() {
		return omartId;
	}

	public void setOmartId(String omartId) {
		this.omartId = omartId;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public static ContactDTO toDTO(Contact entity) {
		ContactDTO dto = new ContactDTO();
		dto.setId(entity.getId());
		dto.setUserId(entity.getUserId());
		dto.setName(entity.getName());
		dto.setOmartId(entity.getOmartId());
		dto.setPhoneNumber(entity.getPhoneNumber());
		dto.setType(entity.getType());
		return dto;
	}

	public static ContactDTO objectToDTO(Object[] obj) {
		ContactDTO dto = new ContactDTO();
		dto.setId(Long.valueOf(String.valueOf(obj[0])));
		dto.setUserId(String.valueOf(obj[1]));
		dto.setName(String.valueOf(obj[2]));
		dto.setOmartId(String.valueOf(obj[3]));
		if(obj[4]!=null) {
			dto.setPhoneNumber(String.valueOf(obj[4]));
		}
		dto.setType(Integer.parseInt(String.valueOf(obj[6])));
		String avatar = ((obj[8] == null) ? "" : String.valueOf(obj[8]));
		dto.setAvatar(avatar);
		return dto;
	}

	public static void sortByName(List<ContactDTO> contacts) {
		Collections.sort(contacts, new Comparator<ContactDTO>() {
			@Override
			public int compare(ContactDTO o1, ContactDTO o2) {
				Locale locale = new Locale("vi_VN");
				Collator collator = Collator.getInstance(locale);
				return collator.compare(o1.getName(), o2.getName());
			}
		});
	}

}
