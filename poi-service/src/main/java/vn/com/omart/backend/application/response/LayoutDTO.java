package vn.com.omart.backend.application.response;

public class LayoutDTO {
  private LayoutPropertiesDTO banner;
  private LayoutPropertiesDTO info;
  private LayoutPropertiesDTO address;
  private LayoutPropertiesDTO desc;
  private LayoutPropertiesDTO item;
  private LayoutPropertiesDTO photo;
  private LayoutPropertiesDTO comment;

  public LayoutDTO() {

  }

  public LayoutDTO(LayoutPropertiesDTO banner, LayoutPropertiesDTO info, LayoutPropertiesDTO address, LayoutPropertiesDTO desc, LayoutPropertiesDTO item,
      LayoutPropertiesDTO photo, LayoutPropertiesDTO comment) {
    super();
    this.banner = banner;
    this.info = info;
    this.address = address;
    this.desc = desc;
    this.item = item;
    this.photo = photo;
    this.comment = comment;
  }

  public LayoutPropertiesDTO getBanner() {
    return banner;
  }

  public void setBanner(LayoutPropertiesDTO banner) {
    this.banner = banner;
  }

  public LayoutPropertiesDTO getInfo() {
    return info;
  }

  public void setInfo(LayoutPropertiesDTO info) {
    this.info = info;
  }

  public LayoutPropertiesDTO getAddress() {
    return address;
  }

  public void setAddress(LayoutPropertiesDTO address) {
    this.address = address;
  }

  public LayoutPropertiesDTO getDesc() {
    return desc;
  }

  public void setDesc(LayoutPropertiesDTO desc) {
    this.desc = desc;
  }

  public LayoutPropertiesDTO getItem() {
    return item;
  }

  public void setItem(LayoutPropertiesDTO item) {
    this.item = item;
  }

  public LayoutPropertiesDTO getPhoto() {
    return photo;
  }

  public void setPhoto(LayoutPropertiesDTO photo) {
    this.photo = photo;
  }

  public LayoutPropertiesDTO getComment() {
    return comment;
  }

  public void setComment(LayoutPropertiesDTO comment) {
    this.comment = comment;
  }

}
