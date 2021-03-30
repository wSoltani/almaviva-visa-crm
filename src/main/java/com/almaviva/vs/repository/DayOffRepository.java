package com.almaviva.vs.repository;

import com.almaviva.vs.domain.DayOff;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DayOff entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DayOffRepository extends JpaRepository<DayOff, Long> {}
