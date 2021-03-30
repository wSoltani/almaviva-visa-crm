package com.almaviva.vs.repository;

import com.almaviva.vs.domain.Visa;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Visa entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VisaRepository extends JpaRepository<Visa, Long> {}
