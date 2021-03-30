package com.almaviva.vs.repository.search;

import com.almaviva.vs.domain.VisaDocuments;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link VisaDocuments} entity.
 */
public interface VisaDocumentsSearchRepository extends ElasticsearchRepository<VisaDocuments, Long> {}
