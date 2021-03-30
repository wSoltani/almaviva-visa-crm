package com.almaviva.vs.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.almaviva.vs.domain.VisaDocuments;
import com.almaviva.vs.repository.VisaDocumentsRepository;
import com.almaviva.vs.repository.search.VisaDocumentsSearchRepository;
import com.almaviva.vs.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.almaviva.vs.domain.VisaDocuments}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class VisaDocumentsResource {

    private final Logger log = LoggerFactory.getLogger(VisaDocumentsResource.class);

    private static final String ENTITY_NAME = "visaDocuments";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VisaDocumentsRepository visaDocumentsRepository;

    private final VisaDocumentsSearchRepository visaDocumentsSearchRepository;

    public VisaDocumentsResource(
        VisaDocumentsRepository visaDocumentsRepository,
        VisaDocumentsSearchRepository visaDocumentsSearchRepository
    ) {
        this.visaDocumentsRepository = visaDocumentsRepository;
        this.visaDocumentsSearchRepository = visaDocumentsSearchRepository;
    }

    /**
     * {@code POST  /visa-documents} : Create a new visaDocuments.
     *
     * @param visaDocuments the visaDocuments to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new visaDocuments, or with status {@code 400 (Bad Request)} if the visaDocuments has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/visa-documents")
    public ResponseEntity<VisaDocuments> createVisaDocuments(@RequestBody VisaDocuments visaDocuments) throws URISyntaxException {
        log.debug("REST request to save VisaDocuments : {}", visaDocuments);
        if (visaDocuments.getId() != null) {
            throw new BadRequestAlertException("A new visaDocuments cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VisaDocuments result = visaDocumentsRepository.save(visaDocuments);
        visaDocumentsSearchRepository.save(result);
        return ResponseEntity
            .created(new URI("/api/visa-documents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /visa-documents/:id} : Updates an existing visaDocuments.
     *
     * @param id the id of the visaDocuments to save.
     * @param visaDocuments the visaDocuments to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated visaDocuments,
     * or with status {@code 400 (Bad Request)} if the visaDocuments is not valid,
     * or with status {@code 500 (Internal Server Error)} if the visaDocuments couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/visa-documents/{id}")
    public ResponseEntity<VisaDocuments> updateVisaDocuments(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VisaDocuments visaDocuments
    ) throws URISyntaxException {
        log.debug("REST request to update VisaDocuments : {}, {}", id, visaDocuments);
        if (visaDocuments.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, visaDocuments.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!visaDocumentsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        VisaDocuments result = visaDocumentsRepository.save(visaDocuments);
        visaDocumentsSearchRepository.save(result);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, visaDocuments.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /visa-documents/:id} : Partial updates given fields of an existing visaDocuments, field will ignore if it is null
     *
     * @param id the id of the visaDocuments to save.
     * @param visaDocuments the visaDocuments to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated visaDocuments,
     * or with status {@code 400 (Bad Request)} if the visaDocuments is not valid,
     * or with status {@code 404 (Not Found)} if the visaDocuments is not found,
     * or with status {@code 500 (Internal Server Error)} if the visaDocuments couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/visa-documents/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<VisaDocuments> partialUpdateVisaDocuments(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VisaDocuments visaDocuments
    ) throws URISyntaxException {
        log.debug("REST request to partial update VisaDocuments partially : {}, {}", id, visaDocuments);
        if (visaDocuments.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, visaDocuments.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!visaDocumentsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VisaDocuments> result = visaDocumentsRepository
            .findById(visaDocuments.getId())
            .map(
                existingVisaDocuments -> {
                    if (visaDocuments.getTitle() != null) {
                        existingVisaDocuments.setTitle(visaDocuments.getTitle());
                    }
                    if (visaDocuments.getDescription() != null) {
                        existingVisaDocuments.setDescription(visaDocuments.getDescription());
                    }
                    if (visaDocuments.getDeleted() != null) {
                        existingVisaDocuments.setDeleted(visaDocuments.getDeleted());
                    }

                    return existingVisaDocuments;
                }
            )
            .map(visaDocumentsRepository::save)
            .map(
                savedVisaDocuments -> {
                    visaDocumentsSearchRepository.save(savedVisaDocuments);

                    return savedVisaDocuments;
                }
            );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, visaDocuments.getId().toString())
        );
    }

    /**
     * {@code GET  /visa-documents} : get all the visaDocuments.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of visaDocuments in body.
     */
    @GetMapping("/visa-documents")
    public List<VisaDocuments> getAllVisaDocuments(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all VisaDocuments");
        return visaDocumentsRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /visa-documents/:id} : get the "id" visaDocuments.
     *
     * @param id the id of the visaDocuments to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the visaDocuments, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/visa-documents/{id}")
    public ResponseEntity<VisaDocuments> getVisaDocuments(@PathVariable Long id) {
        log.debug("REST request to get VisaDocuments : {}", id);
        Optional<VisaDocuments> visaDocuments = visaDocumentsRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(visaDocuments);
    }

    /**
     * {@code DELETE  /visa-documents/:id} : delete the "id" visaDocuments.
     *
     * @param id the id of the visaDocuments to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/visa-documents/{id}")
    public ResponseEntity<Void> deleteVisaDocuments(@PathVariable Long id) {
        log.debug("REST request to delete VisaDocuments : {}", id);
        visaDocumentsRepository.deleteById(id);
        visaDocumentsSearchRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/visa-documents?query=:query} : search for the visaDocuments corresponding
     * to the query.
     *
     * @param query the query of the visaDocuments search.
     * @return the result of the search.
     */
    @GetMapping("/_search/visa-documents")
    public List<VisaDocuments> searchVisaDocuments(@RequestParam String query) {
        log.debug("REST request to search VisaDocuments for query {}", query);
        return StreamSupport
            .stream(visaDocumentsSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
