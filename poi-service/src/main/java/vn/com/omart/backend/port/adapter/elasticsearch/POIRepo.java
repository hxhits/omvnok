package vn.com.omart.backend.port.adapter.elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface POIRepo extends ElasticsearchRepository<POI, Long> {
}
