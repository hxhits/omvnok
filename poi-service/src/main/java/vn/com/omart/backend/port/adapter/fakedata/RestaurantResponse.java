package vn.com.omart.backend.port.adapter.fakedata;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RestaurantResponse {

    @JsonProperty("searchItems")
    private List<Restaurant> restaurants = new ArrayList<Restaurant>();

    @JsonProperty("totalResult")
    private int totalResult;

}
