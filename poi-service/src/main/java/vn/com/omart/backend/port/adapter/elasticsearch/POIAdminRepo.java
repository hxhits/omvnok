package vn.com.omart.backend.port.adapter.elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface POIAdminRepo extends ElasticsearchRepository<POIAdmin, Long> {
}