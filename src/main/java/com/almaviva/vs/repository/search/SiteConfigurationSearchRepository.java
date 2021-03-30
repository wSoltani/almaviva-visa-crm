package com.almaviva.vs.repository.search;

import com.almaviva.vs.domain.SiteConfiguration;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link SiteConfiguration} entity.
 */
public interface SiteConfigurationSearchRepository extends ElasticsearchRepository<SiteConfiguration, Long> {}
