package com.almaviva.vs.repository;

import com.almaviva.vs.domain.Mandate;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Mandate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MandateRepository extends JpaRepository<Mandate, Long> {}
