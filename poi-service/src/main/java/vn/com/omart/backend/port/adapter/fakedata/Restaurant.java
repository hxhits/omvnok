package vn.com.omart.backend.port.adapter.fakedata;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Restaurant {

    //    private String TotalReview;
//    private String TotalView;
//    private String TotalFavourite;
//    private String TotalCheckins;
    @JsonProperty("Id")
    private long id;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Address")
    private String address;

    @JsonProperty("DistrictId")
    private int districtId;

    @JsonProperty("District")
    private String district;

    @JsonProperty("City")
    private String city;

    @JsonProperty("Phone")
    private String phone;

    @JsonProperty("PicturePath")
    private String picturePath;

    @JsonProperty("MobilePicturePath")
    private String mobilePicturePath;

    @JsonProperty("SpecialDesc")
    private String specialDesc;

//    private String AvgRating;
//    private String ReviewUrl;

    @JsonProperty("Latitude")
    private double latitude;

    @JsonProperty("Longitude")
    private double longitude;

//    private int MainCategoryId;
//    private int Status;
//    private String DetailUrl;
//    private int DocumentType;
//    private boolean ShowInSearchResult;
//    private boolean IsAd;
}
