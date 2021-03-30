package com.almaviva.vs.repository.search;

import com.almaviva.vs.domain.DayOff;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link DayOff} entity.
 */
public interface DayOffSearchRepository extends ElasticsearchRepository<DayOff, Long> {}
