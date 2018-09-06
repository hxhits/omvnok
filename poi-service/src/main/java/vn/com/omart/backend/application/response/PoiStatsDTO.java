package vn.com.omart.backend.application.response;

/**
 * Custom Poi Stats.
 * 
 * @author Win10
 *
 */
public class PoiStatsDTO {

  private int likes;
  private int dislikes;
  private int comments;
  private int views;
  private int photos;
  private int shares;
  private double rate;
  private double distance;

  public PoiStatsDTO() {}

  public PoiStatsDTO(int likes, int dislikes, int comments, int views, int photos, double rate, double distance) {
    super();
    this.likes = likes;
    this.dislikes = dislikes;
    this.comments = comments;
    this.views = views;
    this.photos = photos;
    this.rate = rate;
    this.distance = distance;
  }

  public int getLikes() {
    return likes;
  }

  public void setLikes(int likes) {
    this.likes = likes;
  }

  public int getDislikes() {
    return dislikes;
  }

  public void setDislikes(int dislikes) {
    this.dislikes = dislikes;
  }

  public int getComments() {
    return comments;
  }

  public void setComments(int comments) {
    this.comments = comments;
  }

  public int getViews() {
    return views;
  }

  public void setViews(int views) {
    this.views = views;
  }

  public int getPhotos() {
    return photos;
  }

  public void setPhotos(int photos) {
    this.photos = photos;
  }

  public double getRate() {
    return rate;
  }

  public void setRate(double rate) {
    this.rate = rate;
  }

  public double getDistance() {
    return distance;
  }

  public void setDistance(double distance) {
    this.distance = distance;
  }

  public int getShares() {
    return shares;
  }

  public void setShares(int shares) {
    this.shares = shares;
  }

}
