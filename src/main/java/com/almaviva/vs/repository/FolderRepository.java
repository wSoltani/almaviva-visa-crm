package com.almaviva.vs.repository;

import com.almaviva.vs.domain.Folder;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Folder entity.
 */
@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {
    @Query(
        value = "select distinct folder from Folder folder left join fetch folder.services",
        countQuery = "select count(distinct folder) from Folder folder"
    )
    Page<Folder> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct folder from Folder folder left join fetch folder.services")
    List<Folder> findAllWithEagerRelationships();

    @Query("select folder from Folder folder left join fetch folder.services where folder.id =:id")
    Optional<Folder> findOneWithEagerRelationships(@Param("id") Long id);
}
