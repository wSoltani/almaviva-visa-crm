package com.almaviva.vs.repository.search;

import com.almaviva.vs.domain.EmailActivation;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link EmailActivation} entity.
 */
public interface EmailActivationSearchRepository extends ElasticsearchRepository<EmailActivation, Long> {}
