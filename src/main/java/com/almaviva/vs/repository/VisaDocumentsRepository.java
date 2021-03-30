package com.almaviva.vs.repository;

import com.almaviva.vs.domain.VisaDocuments;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the VisaDocuments entity.
 */
@Repository
public interface VisaDocumentsRepository extends JpaRepository<VisaDocuments, Long> {
    @Query(
        value = "select distinct visaDocuments from VisaDocuments visaDocuments left join fetch visaDocuments.visas",
        countQuery = "select count(distinct visaDocuments) from VisaDocuments visaDocuments"
    )
    Page<VisaDocuments> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct visaDocuments from VisaDocuments visaDocuments left join fetch visaDocuments.visas")
    List<VisaDocuments> findAllWithEagerRelationships();

    @Query("select visaDocuments from VisaDocuments visaDocuments left join fetch visaDocuments.visas where visaDocuments.id =:id")
    Optional<VisaDocuments> findOneWithEagerRelationships(@Param("id") Long id);
}
