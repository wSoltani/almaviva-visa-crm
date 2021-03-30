package com.almaviva.vs.repository.search;

import com.almaviva.vs.domain.Mandate;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Mandate} entity.
 */
public interface MandateSearchRepository extends ElasticsearchRepository<Mandate, Long> {}
