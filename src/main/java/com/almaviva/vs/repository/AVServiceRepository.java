package com.almaviva.vs.repository;

import com.almaviva.vs.domain.AVService;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the AVService entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AVServiceRepository extends JpaRepository<AVService, Long> {}
