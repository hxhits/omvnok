package vn.com.omart.backend.port.adapter.fakedata;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import vn.com.omart.backend.domain.model.Category;
import vn.com.omart.backend.domain.model.CategoryRepository;
import vn.com.omart.backend.domain.model.PointOfInterestRepository;
import vn.com.omart.backend.domain.model.PointOfInterest;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class Foody {

    private final CategoryRepository categoryRepository;
    private final PointOfInterestRepository poiRepository;

    private static String URL = "http://www.foody.vn/ho-chi-minh/dia-diem?ds=Restaurant&vt=row&st=1&dt=undefined&provinceId=217&categoryId=12&append=true&page=%d";


    public Foody(CategoryRepository categoryRepository, PointOfInterestRepository repository) {
        this.categoryRepository = categoryRepository;
        this.poiRepository = repository;
    }

    public void executeRequest() {

//        Category category1 = this.categoryRepository.findOne(1L);
        List<PointOfInterest> crawlers = this.poiRepository.findByCategoryId(1L);
//        this.poiRepository.deleteInBatch(crawlers);

        int page = 1;
        int maxPage = Integer.MAX_VALUE;
        RestTemplate restTemplate = new RestTemplate();
        do {
            RequestEntity<Void> xmlHttpRequest = RequestEntity.get(URI.create(String.format(URL, page)))
                .header("X-Requested-With", "XMLHttpRequest")
                .build();
            Category category = this.categoryRepository.findOne(1L);
            ResponseEntity<RestaurantResponse> response = restTemplate.exchange(xmlHttpRequest, RestaurantResponse.class);
            List<PointOfInterest> result = response.getBody().getRestaurants().stream()
                .map(restaurant -> PointOfInterest.builder()
                    .category(category)
                    .name(restaurant.getName())
                    .description(restaurant.getSpecialDesc())
                    .address(restaurant.getAddress())
//                    .ward("0")
//                    .district(restaurant.getDistrict())
//                    .province(restaurant.getCity())
                    .phone(null)
                    .latitude(restaurant.getLatitude())
                    .longitude(restaurant.getLongitude())
                    .openHour(9D)
                    .closeHour(21D)
                    .openingState(1)
                    .avatarImage(new ArrayList<>())
                    .coverImage(new ArrayList<>())
                    .featuredImage(new ArrayList<>())
                    .createdBy("system")
                    .createdAt(new Date())
                    .build())
                .map(this.poiRepository::save)
                .collect(Collectors.toList());

            if (page == 1) {
                if (response.getBody().getTotalResult() % response.getBody().getRestaurants().size() != 0) {
                    maxPage = response.getBody().getTotalResult() / response.getBody().getRestaurants().size() + 1;
                } else {
                    maxPage = response.getBody().getTotalResult() / response.getBody().getRestaurants().size();
                }

                log.info("Page 1 so We have to request for " + maxPage + " pages more!");
            }

            page++;
        } while (page <= maxPage);
    }
}
