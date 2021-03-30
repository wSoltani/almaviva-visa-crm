package com.almaviva.vs.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.almaviva.vs.domain.EmailActivation;
import com.almaviva.vs.repository.EmailActivationRepository;
import com.almaviva.vs.repository.search.EmailActivationSearchRepository;
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
 * REST controller for managing {@link com.almaviva.vs.domain.EmailActivation}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class EmailActivationResource {

    private final Logger log = LoggerFactory.getLogger(EmailActivationResource.class);

    private static final String ENTITY_NAME = "emailActivation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmailActivationRepository emailActivationRepository;

    private final EmailActivationSearchRepository emailActivationSearchRepository;

    public EmailActivationResource(
        EmailActivationRepository emailActivationRepository,
        EmailActivationSearchRepository emailActivationSearchRepository
    ) {
        this.emailActivationRepository = emailActivationRepository;
        this.emailActivationSearchRepository = emailActivationSearchRepository;
    }

    /**
     * {@code POST  /email-activations} : Create a new emailActivation.
     *
     * @param emailActivation the emailActivation to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new emailActivation, or with status {@code 400 (Bad Request)} if the emailActivation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/email-activations")
    public ResponseEntity<EmailActivation> createEmailActivation(@RequestBody EmailActivation emailActivation) throws URISyntaxException {
        log.debug("REST request to save EmailActivation : {}", emailActivation);
        if (emailActivation.getId() != null) {
            throw new BadRequestAlertException("A new emailActivation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EmailActivation result = emailActivationRepository.save(emailActivation);
        emailActivationSearchRepository.save(result);
        return ResponseEntity
            .created(new URI("/api/email-activations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /email-activations/:id} : Updates an existing emailActivation.
     *
     * @param id the id of the emailActivation to save.
     * @param emailActivation the emailActivation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated emailActivation,
     * or with status {@code 400 (Bad Request)} if the emailActivation is not valid,
     * or with status {@code 500 (Internal Server Error)} if the emailActivation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/email-activations/{id}")
    public ResponseEntity<EmailActivation> updateEmailActivation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EmailActivation emailActivation
    ) throws URISyntaxException {
        log.debug("REST request to update EmailActivation : {}, {}", id, emailActivation);
        if (emailActivation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, emailActivation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!emailActivationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EmailActivation result = emailActivationRepository.save(emailActivation);
        emailActivationSearchRepository.save(result);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, emailActivation.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /email-activations/:id} : Partial updates given fields of an existing emailActivation, field will ignore if it is null
     *
     * @param id the id of the emailActivation to save.
     * @param emailActivation the emailActivation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated emailActivation,
     * or with status {@code 400 (Bad Request)} if the emailActivation is not valid,
     * or with status {@code 404 (Not Found)} if the emailActivation is not found,
     * or with status {@code 500 (Internal Server Error)} if the emailActivation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/email-activations/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<EmailActivation> partialUpdateEmailActivation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EmailActivation emailActivation
    ) throws URISyntaxException {
        log.debug("REST request to partial update EmailActivation partially : {}, {}", id, emailActivation);
        if (emailActivation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, emailActivation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!emailActivationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EmailActivation> result = emailActivationRepository
            .findById(emailActivation.getId())
            .map(
                existingEmailActivation -> {
                    if (emailActivation.getIsActivated() != null) {
                        existingEmailActivation.setIsActivated(emailActivation.getIsActivated());
                    }
                    if (emailActivation.getActivationKey() != null) {
                        existingEmailActivation.setActivationKey(emailActivation.getActivationKey());
                    }
                    if (emailActivation.getExpirationDate() != null) {
                        existingEmailActivation.setExpirationDate(emailActivation.getExpirationDate());
                    }
                    if (emailActivation.getDeleted() != null) {
                        existingEmailActivation.setDeleted(emailActivation.getDeleted());
                    }

                    return existingEmailActivation;
                }
            )
            .map(emailActivationRepository::save)
            .map(
                savedEmailActivation -> {
                    emailActivationSearchRepository.save(savedEmailActivation);

                    return savedEmailActivation;
                }
            );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, emailActivation.getId().toString())
        );
    }

    /**
     * {@code GET  /email-activations} : get all the emailActivations.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of emailActivations in body.
     */
    @GetMapping("/email-activations")
    public List<EmailActivation> getAllEmailActivations() {
        log.debug("REST request to get all EmailActivations");
        return emailActivationRepository.findAll();
    }

    /**
     * {@code GET  /email-activations/:id} : get the "id" emailActivation.
     *
     * @param id the id of the emailActivation to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the emailActivation, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/email-activations/{id}")
    public ResponseEntity<EmailActivation> getEmailActivation(@PathVariable Long id) {
        log.debug("REST request to get EmailActivation : {}", id);
        Optional<EmailActivation> emailActivation = emailActivationRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(emailActivation);
    }

    /**
     * {@code DELETE  /email-activations/:id} : delete the "id" emailActivation.
     *
     * @param id the id of the emailActivation to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/email-activations/{id}")
    public ResponseEntity<Void> deleteEmailActivation(@PathVariable Long id) {
        log.debug("REST request to delete EmailActivation : {}", id);
        emailActivationRepository.deleteById(id);
        emailActivationSearchRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/email-activations?query=:query} : search for the emailActivation corresponding
     * to the query.
     *
     * @param query the query of the emailActivation search.
     * @return the result of the search.
     */
    @GetMapping("/_search/email-activations")
    public List<EmailActivation> searchEmailActivations(@RequestParam String query) {
        log.debug("REST request to search EmailActivations for query {}", query);
        return StreamSupport
            .stream(emailActivationSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
