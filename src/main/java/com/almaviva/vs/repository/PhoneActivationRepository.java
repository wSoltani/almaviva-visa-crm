package com.almaviva.vs.repository;

import com.almaviva.vs.domain.PhoneActivation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PhoneActivation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PhoneActivationRepository extends JpaRepository<PhoneActivation, Long> {}
