package com.almaviva.vs.repository;

import com.almaviva.vs.domain.SiteConfiguration;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SiteConfiguration entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SiteConfigurationRepository extends JpaRepository<SiteConfiguration, Long> {}
