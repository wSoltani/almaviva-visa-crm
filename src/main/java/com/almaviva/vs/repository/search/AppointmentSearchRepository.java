package com.almaviva.vs.repository.search;

import com.almaviva.vs.domain.Appointment;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Appointment} entity.
 */
public interface AppointmentSearchRepository extends ElasticsearchRepository<Appointment, Long> {}
