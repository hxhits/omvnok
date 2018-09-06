package vn.com.omart.backend.application.response;

public class LayoutPropertiesDTO {
  private int index;
  private boolean isDisplay;

  public LayoutPropertiesDTO() {

  }

  public LayoutPropertiesDTO(int index, boolean isDisplay) {
    super();
    this.index = index;
    this.isDisplay = isDisplay;
  }

  public int getIndex() {
    return index;
  }

  public void setIndex(int index) {
    this.index = index;
  }

  public boolean isDisplay() {
    return isDisplay;
  }

  public void setDisplay(boolean isDisplay) {
    this.isDisplay = isDisplay;
  }
}
