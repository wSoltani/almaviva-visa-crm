package com.almaviva.vs.repository.search;

import com.almaviva.vs.domain.AVService;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link AVService} entity.
 */
public interface AVServiceSearchRepository extends ElasticsearchRepository<AVService, Long> {}
