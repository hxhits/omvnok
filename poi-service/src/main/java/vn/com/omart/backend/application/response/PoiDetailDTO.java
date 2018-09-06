package vn.com.omart.backend.application.response;

import java.util.List;

/**
 * Custom Json Response.
 * 
 * @author Win10
 *
 */
public class PoiDetailDTO {
  private PointOfInterestDTO detail;
  private List<ItemDTO> items;

  public PoiDetailDTO() {}

  public PoiDetailDTO(PointOfInterestDTO detail, List<ItemDTO> items) {
    super();
    this.detail = detail;
    this.items = items;
  }

  public PointOfInterestDTO getDetail() {
    return detail;
  }

  public void setDetail(PointOfInterestDTO detail) {
    this.detail = detail;
  }

  public List<ItemDTO> getItems() {
    return items;
  }

  public void setItems(List<ItemDTO> items) {
    this.items = items;
  }
}
