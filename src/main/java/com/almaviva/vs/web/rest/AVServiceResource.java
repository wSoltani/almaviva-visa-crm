package com.almaviva.vs.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.almaviva.vs.domain.AVService;
import com.almaviva.vs.repository.AVServiceRepository;
import com.almaviva.vs.repository.search.AVServiceSearchRepository;
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
 * REST controller for managing {@link com.almaviva.vs.domain.AVService}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AVServiceResource {

    private final Logger log = LoggerFactory.getLogger(AVServiceResource.class);

    private static final String ENTITY_NAME = "aVService";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AVServiceRepository aVServiceRepository;

    private final AVServiceSearchRepository aVServiceSearchRepository;

    public AVServiceResource(AVServiceRepository aVServiceRepository, AVServiceSearchRepository aVServiceSearchRepository) {
        this.aVServiceRepository = aVServiceRepository;
        this.aVServiceSearchRepository = aVServiceSearchRepository;
    }

    /**
     * {@code POST  /av-services} : Create a new aVService.
     *
     * @param aVService the aVService to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aVService, or with status {@code 400 (Bad Request)} if the aVService has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/av-services")
    public ResponseEntity<AVService> createAVService(@RequestBody AVService aVService) throws URISyntaxException {
        log.debug("REST request to save AVService : {}", aVService);
        if (aVService.getId() != null) {
            throw new BadRequestAlertException("A new aVService cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AVService result = aVServiceRepository.save(aVService);
        aVServiceSearchRepository.save(result);
        return ResponseEntity
            .created(new URI("/api/av-services/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /av-services/:id} : Updates an existing aVService.
     *
     * @param id the id of the aVService to save.
     * @param aVService the aVService to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aVService,
     * or with status {@code 400 (Bad Request)} if the aVService is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aVService couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/av-services/{id}")
    public ResponseEntity<AVService> updateAVService(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AVService aVService
    ) throws URISyntaxException {
        log.debug("REST request to update AVService : {}, {}", id, aVService);
        if (aVService.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aVService.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aVServiceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AVService result = aVServiceRepository.save(aVService);
        aVServiceSearchRepository.save(result);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aVService.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /av-services/:id} : Partial updates given fields of an existing aVService, field will ignore if it is null
     *
     * @param id the id of the aVService to save.
     * @param aVService the aVService to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aVService,
     * or with status {@code 400 (Bad Request)} if the aVService is not valid,
     * or with status {@code 404 (Not Found)} if the aVService is not found,
     * or with status {@code 500 (Internal Server Error)} if the aVService couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/av-services/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<AVService> partialUpdateAVService(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AVService aVService
    ) throws URISyntaxException {
        log.debug("REST request to partial update AVService partially : {}, {}", id, aVService);
        if (aVService.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aVService.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aVServiceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AVService> result = aVServiceRepository
            .findById(aVService.getId())
            .map(
                existingAVService -> {
                    if (aVService.getTitle() != null) {
                        existingAVService.setTitle(aVService.getTitle());
                    }
                    if (aVService.getDescription() != null) {
                        existingAVService.setDescription(aVService.getDescription());
                    }
                    if (aVService.getPrice() != null) {
                        existingAVService.setPrice(aVService.getPrice());
                    }
                    if (aVService.getQuantity() != null) {
                        existingAVService.setQuantity(aVService.getQuantity());
                    }
                    if (aVService.getIsPrincipal() != null) {
                        existingAVService.setIsPrincipal(aVService.getIsPrincipal());
                    }
                    if (aVService.getDeleted() != null) {
                        existingAVService.setDeleted(aVService.getDeleted());
                    }

                    return existingAVService;
                }
            )
            .map(aVServiceRepository::save)
            .map(
                savedAVService -> {
                    aVServiceSearchRepository.save(savedAVService);

                    return savedAVService;
                }
            );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aVService.getId().toString())
        );
    }

    /**
     * {@code GET  /av-services} : get all the aVServices.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aVServices in body.
     */
    @GetMapping("/av-services")
    public List<AVService> getAllAVServices() {
        log.debug("REST request to get all AVServices");
        return aVServiceRepository.findAll();
    }

    /**
     * {@code GET  /av-services/:id} : get the "id" aVService.
     *
     * @param id the id of the aVService to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aVService, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/av-services/{id}")
    public ResponseEntity<AVService> getAVService(@PathVariable Long id) {
        log.debug("REST request to get AVService : {}", id);
        Optional<AVService> aVService = aVServiceRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(aVService);
    }

    /**
     * {@code DELETE  /av-services/:id} : delete the "id" aVService.
     *
     * @param id the id of the aVService to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/av-services/{id}")
    public ResponseEntity<Void> deleteAVService(@PathVariable Long id) {
        log.debug("REST request to delete AVService : {}", id);
        aVServiceRepository.deleteById(id);
        aVServiceSearchRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/av-services?query=:query} : search for the aVService corresponding
     * to the query.
     *
     * @param query the query of the aVService search.
     * @return the result of the search.
     */
    @GetMapping("/_search/av-services")
    public List<AVService> searchAVServices(@RequestParam String query) {
        log.debug("REST request to search AVServices for query {}", query);
        return StreamSupport
            .stream(aVServiceSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
