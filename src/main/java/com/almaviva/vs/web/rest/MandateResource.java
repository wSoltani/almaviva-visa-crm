package com.almaviva.vs.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.almaviva.vs.domain.Mandate;
import com.almaviva.vs.repository.MandateRepository;
import com.almaviva.vs.repository.search.MandateSearchRepository;
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
 * REST controller for managing {@link com.almaviva.vs.domain.Mandate}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class MandateResource {

    private final Logger log = LoggerFactory.getLogger(MandateResource.class);

    private static final String ENTITY_NAME = "mandate";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MandateRepository mandateRepository;

    private final MandateSearchRepository mandateSearchRepository;

    public MandateResource(MandateRepository mandateRepository, MandateSearchRepository mandateSearchRepository) {
        this.mandateRepository = mandateRepository;
        this.mandateSearchRepository = mandateSearchRepository;
    }

    /**
     * {@code POST  /mandates} : Create a new mandate.
     *
     * @param mandate the mandate to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mandate, or with status {@code 400 (Bad Request)} if the mandate has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/mandates")
    public ResponseEntity<Mandate> createMandate(@RequestBody Mandate mandate) throws URISyntaxException {
        log.debug("REST request to save Mandate : {}", mandate);
        if (mandate.getId() != null) {
            throw new BadRequestAlertException("A new mandate cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Mandate result = mandateRepository.save(mandate);
        mandateSearchRepository.save(result);
        return ResponseEntity
            .created(new URI("/api/mandates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /mandates/:id} : Updates an existing mandate.
     *
     * @param id the id of the mandate to save.
     * @param mandate the mandate to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mandate,
     * or with status {@code 400 (Bad Request)} if the mandate is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mandate couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/mandates/{id}")
    public ResponseEntity<Mandate> updateMandate(@PathVariable(value = "id", required = false) final Long id, @RequestBody Mandate mandate)
        throws URISyntaxException {
        log.debug("REST request to update Mandate : {}, {}", id, mandate);
        if (mandate.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mandate.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mandateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Mandate result = mandateRepository.save(mandate);
        mandateSearchRepository.save(result);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mandate.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /mandates/:id} : Partial updates given fields of an existing mandate, field will ignore if it is null
     *
     * @param id the id of the mandate to save.
     * @param mandate the mandate to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mandate,
     * or with status {@code 400 (Bad Request)} if the mandate is not valid,
     * or with status {@code 404 (Not Found)} if the mandate is not found,
     * or with status {@code 500 (Internal Server Error)} if the mandate couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/mandates/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Mandate> partialUpdateMandate(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Mandate mandate
    ) throws URISyntaxException {
        log.debug("REST request to partial update Mandate partially : {}, {}", id, mandate);
        if (mandate.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mandate.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mandateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Mandate> result = mandateRepository
            .findById(mandate.getId())
            .map(
                existingMandate -> {
                    if (mandate.getCode() != null) {
                        existingMandate.setCode(mandate.getCode());
                    }
                    if (mandate.getLocation() != null) {
                        existingMandate.setLocation(mandate.getLocation());
                    }
                    if (mandate.getAmount() != null) {
                        existingMandate.setAmount(mandate.getAmount());
                    }
                    if (mandate.getDate() != null) {
                        existingMandate.setDate(mandate.getDate());
                    }
                    if (mandate.getIsAVSPaiment() != null) {
                        existingMandate.setIsAVSPaiment(mandate.getIsAVSPaiment());
                    }
                    if (mandate.getDeleted() != null) {
                        existingMandate.setDeleted(mandate.getDeleted());
                    }

                    return existingMandate;
                }
            )
            .map(mandateRepository::save)
            .map(
                savedMandate -> {
                    mandateSearchRepository.save(savedMandate);

                    return savedMandate;
                }
            );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mandate.getId().toString())
        );
    }

    /**
     * {@code GET  /mandates} : get all the mandates.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of mandates in body.
     */
    @GetMapping("/mandates")
    public List<Mandate> getAllMandates() {
        log.debug("REST request to get all Mandates");
        return mandateRepository.findAll();
    }

    /**
     * {@code GET  /mandates/:id} : get the "id" mandate.
     *
     * @param id the id of the mandate to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mandate, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/mandates/{id}")
    public ResponseEntity<Mandate> getMandate(@PathVariable Long id) {
        log.debug("REST request to get Mandate : {}", id);
        Optional<Mandate> mandate = mandateRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(mandate);
    }

    /**
     * {@code DELETE  /mandates/:id} : delete the "id" mandate.
     *
     * @param id the id of the mandate to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/mandates/{id}")
    public ResponseEntity<Void> deleteMandate(@PathVariable Long id) {
        log.debug("REST request to delete Mandate : {}", id);
        mandateRepository.deleteById(id);
        mandateSearchRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/mandates?query=:query} : search for the mandate corresponding
     * to the query.
     *
     * @param query the query of the mandate search.
     * @return the result of the search.
     */
    @GetMapping("/_search/mandates")
    public List<Mandate> searchMandates(@RequestParam String query) {
        log.debug("REST request to search Mandates for query {}", query);
        return StreamSupport
            .stream(mandateSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
