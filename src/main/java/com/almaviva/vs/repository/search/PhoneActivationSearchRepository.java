package com.almaviva.vs.repository.search;

import com.almaviva.vs.domain.PhoneActivation;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link PhoneActivation} entity.
 */
public interface PhoneActivationSearchRepository extends ElasticsearchRepository<PhoneActivation, Long> {}
