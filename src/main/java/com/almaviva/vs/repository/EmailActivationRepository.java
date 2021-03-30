package com.almaviva.vs.repository;

import com.almaviva.vs.domain.EmailActivation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the EmailActivation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmailActivationRepository extends JpaRepository<EmailActivation, Long> {}
