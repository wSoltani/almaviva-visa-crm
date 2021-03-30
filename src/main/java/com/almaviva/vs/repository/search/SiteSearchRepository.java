package com.almaviva.vs.repository.search;

import com.almaviva.vs.domain.Site;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Site} entity.
 */
public interface SiteSearchRepository extends ElasticsearchRepository<Site, Long> {}
