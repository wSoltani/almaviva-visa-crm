package com.almaviva.vs.repository.search;

import com.almaviva.vs.domain.Visa;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Visa} entity.
 */
public interface VisaSearchRepository extends ElasticsearchRepository<Visa, Long> {}
