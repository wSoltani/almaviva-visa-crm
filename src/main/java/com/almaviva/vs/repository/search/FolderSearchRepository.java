package com.almaviva.vs.repository.search;

import com.almaviva.vs.domain.Folder;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Folder} entity.
 */
public interface FolderSearchRepository extends ElasticsearchRepository<Folder, Long> {}
