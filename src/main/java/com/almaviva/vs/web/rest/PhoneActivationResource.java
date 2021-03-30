package com.almaviva.vs.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.almaviva.vs.domain.PhoneActivation;
import com.almaviva.vs.repository.PhoneActivationRepository;
import com.almaviva.vs.repository.search.PhoneActivationSearchRepository;
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
 * REST controller for managing {@link com.almaviva.vs.domain.PhoneActivation}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PhoneActivationResource {

    private final Logger log = LoggerFactory.getLogger(PhoneActivationResource.class);

    private static final String ENTITY_NAME = "phoneActivation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PhoneActivationRepository phoneActivationRepository;

    private final PhoneActivationSearchRepository phoneActivationSearchRepository;

    public PhoneActivationResource(
        PhoneActivationRepository phoneActivationRepository,
        PhoneActivationSearchRepository phoneActivationSearchRepository
    ) {
        this.phoneActivationRepository = phoneActivationRepository;
        this.phoneActivationSearchRepository = phoneActivationSearchRepository;
    }

    /**
     * {@code POST  /phone-activations} : Create a new phoneActivation.
     *
     * @param phoneActivation the phoneActivation to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new phoneActivation, or with status {@code 400 (Bad Request)} if the phoneActivation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/phone-activations")
    public ResponseEntity<PhoneActivation> createPhoneActivation(@RequestBody PhoneActivation phoneActivation) throws URISyntaxException {
        log.debug("REST request to save PhoneActivation : {}", phoneActivation);
        if (phoneActivation.getId() != null) {
            throw new BadRequestAlertException("A new phoneActivation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PhoneActivation result = phoneActivationRepository.save(phoneActivation);
        phoneActivationSearchRepository.save(result);
        return ResponseEntity
            .created(new URI("/api/phone-activations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /phone-activations/:id} : Updates an existing phoneActivation.
     *
     * @param id the id of the phoneActivation to save.
     * @param phoneActivation the phoneActivation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated phoneActivation,
     * or with status {@code 400 (Bad Request)} if the phoneActivation is not valid,
     * or with status {@code 500 (Internal Server Error)} if the phoneActivation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/phone-activations/{id}")
    public ResponseEntity<PhoneActivation> updatePhoneActivation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PhoneActivation phoneActivation
    ) throws URISyntaxException {
        log.debug("REST request to update PhoneActivation : {}, {}", id, phoneActivation);
        if (phoneActivation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, phoneActivation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!phoneActivationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PhoneActivation result = phoneActivationRepository.save(phoneActivation);
        phoneActivationSearchRepository.save(result);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, phoneActivation.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /phone-activations/:id} : Partial updates given fields of an existing phoneActivation, field will ignore if it is null
     *
     * @param id the id of the phoneActivation to save.
     * @param phoneActivation the phoneActivation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated phoneActivation,
     * or with status {@code 400 (Bad Request)} if the phoneActivation is not valid,
     * or with status {@code 404 (Not Found)} if the phoneActivation is not found,
     * or with status {@code 500 (Internal Server Error)} if the phoneActivation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/phone-activations/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<PhoneActivation> partialUpdatePhoneActivation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PhoneActivation phoneActivation
    ) throws URISyntaxException {
        log.debug("REST request to partial update PhoneActivation partially : {}, {}", id, phoneActivation);
        if (phoneActivation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, phoneActivation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!phoneActivationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PhoneActivation> result = phoneActivationRepository
            .findById(phoneActivation.getId())
            .map(
                existingPhoneActivation -> {
                    if (phoneActivation.getIsActivated() != null) {
                        existingPhoneActivation.setIsActivated(phoneActivation.getIsActivated());
                    }
                    if (phoneActivation.getActivationKey() != null) {
                        existingPhoneActivation.setActivationKey(phoneActivation.getActivationKey());
                    }
                    if (phoneActivation.getExpirationDate() != null) {
                        existingPhoneActivation.setExpirationDate(phoneActivation.getExpirationDate());
                    }
                    if (phoneActivation.getDeleted() != null) {
                        existingPhoneActivation.setDeleted(phoneActivation.getDeleted());
                    }

                    return existingPhoneActivation;
                }
            )
            .map(phoneActivationRepository::save)
            .map(
                savedPhoneActivation -> {
                    phoneActivationSearchRepository.save(savedPhoneActivation);

                    return savedPhoneActivation;
                }
            );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, phoneActivation.getId().toString())
        );
    }

    /**
     * {@code GET  /phone-activations} : get all the phoneActivations.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of phoneActivations in body.
     */
    @GetMapping("/phone-activations")
    public List<PhoneActivation> getAllPhoneActivations() {
        log.debug("REST request to get all PhoneActivations");
        return phoneActivationRepository.findAll();
    }

    /**
     * {@code GET  /phone-activations/:id} : get the "id" phoneActivation.
     *
     * @param id the id of the phoneActivation to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the phoneActivation, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/phone-activations/{id}")
    public ResponseEntity<PhoneActivation> getPhoneActivation(@PathVariable Long id) {
        log.debug("REST request to get PhoneActivation : {}", id);
        Optional<PhoneActivation> phoneActivation = phoneActivationRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(phoneActivation);
    }

    /**
     * {@code DELETE  /phone-activations/:id} : delete the "id" phoneActivation.
     *
     * @param id the id of the phoneActivation to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/phone-activations/{id}")
    public ResponseEntity<Void> deletePhoneActivation(@PathVariable Long id) {
        log.debug("REST request to delete PhoneActivation : {}", id);
        phoneActivationRepository.deleteById(id);
        phoneActivationSearchRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/phone-activations?query=:query} : search for the phoneActivation corresponding
     * to the query.
     *
     * @param query the query of the phoneActivation search.
     * @return the result of the search.
     */
    @GetMapping("/_search/phone-activations")
    public List<PhoneActivation> searchPhoneActivations(@RequestParam String query) {
        log.debug("REST request to search PhoneActivations for query {}", query);
        return StreamSupport
            .stream(phoneActivationSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
