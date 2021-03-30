package com.almaviva.vs.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.almaviva.vs.domain.Visa;
import com.almaviva.vs.repository.VisaRepository;
import com.almaviva.vs.repository.search.VisaSearchRepository;
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
 * REST controller for managing {@link com.almaviva.vs.domain.Visa}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class VisaResource {

    private final Logger log = LoggerFactory.getLogger(VisaResource.class);

    private static final String ENTITY_NAME = "visa";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VisaRepository visaRepository;

    private final VisaSearchRepository visaSearchRepository;

    public VisaResource(VisaRepository visaRepository, VisaSearchRepository visaSearchRepository) {
        this.visaRepository = visaRepository;
        this.visaSearchRepository = visaSearchRepository;
    }

    /**
     * {@code POST  /visas} : Create a new visa.
     *
     * @param visa the visa to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new visa, or with status {@code 400 (Bad Request)} if the visa has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/visas")
    public ResponseEntity<Visa> createVisa(@RequestBody Visa visa) throws URISyntaxException {
        log.debug("REST request to save Visa : {}", visa);
        if (visa.getId() != null) {
            throw new BadRequestAlertException("A new visa cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Visa result = visaRepository.save(visa);
        visaSearchRepository.save(result);
        return ResponseEntity
            .created(new URI("/api/visas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /visas/:id} : Updates an existing visa.
     *
     * @param id the id of the visa to save.
     * @param visa the visa to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated visa,
     * or with status {@code 400 (Bad Request)} if the visa is not valid,
     * or with status {@code 500 (Internal Server Error)} if the visa couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/visas/{id}")
    public ResponseEntity<Visa> updateVisa(@PathVariable(value = "id", required = false) final Long id, @RequestBody Visa visa)
        throws URISyntaxException {
        log.debug("REST request to update Visa : {}, {}", id, visa);
        if (visa.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, visa.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!visaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Visa result = visaRepository.save(visa);
        visaSearchRepository.save(result);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, visa.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /visas/:id} : Partial updates given fields of an existing visa, field will ignore if it is null
     *
     * @param id the id of the visa to save.
     * @param visa the visa to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated visa,
     * or with status {@code 400 (Bad Request)} if the visa is not valid,
     * or with status {@code 404 (Not Found)} if the visa is not found,
     * or with status {@code 500 (Internal Server Error)} if the visa couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/visas/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Visa> partialUpdateVisa(@PathVariable(value = "id", required = false) final Long id, @RequestBody Visa visa)
        throws URISyntaxException {
        log.debug("REST request to partial update Visa partially : {}, {}", id, visa);
        if (visa.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, visa.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!visaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Visa> result = visaRepository
            .findById(visa.getId())
            .map(
                existingVisa -> {
                    if (visa.getTitle() != null) {
                        existingVisa.setTitle(visa.getTitle());
                    }
                    if (visa.getPrice() != null) {
                        existingVisa.setPrice(visa.getPrice());
                    }
                    if (visa.getDescription() != null) {
                        existingVisa.setDescription(visa.getDescription());
                    }
                    if (visa.getDeleted() != null) {
                        existingVisa.setDeleted(visa.getDeleted());
                    }

                    return existingVisa;
                }
            )
            .map(visaRepository::save)
            .map(
                savedVisa -> {
                    visaSearchRepository.save(savedVisa);

                    return savedVisa;
                }
            );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, visa.getId().toString())
        );
    }

    /**
     * {@code GET  /visas} : get all the visas.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of visas in body.
     */
    @GetMapping("/visas")
    public List<Visa> getAllVisas() {
        log.debug("REST request to get all Visas");
        return visaRepository.findAll();
    }

    /**
     * {@code GET  /visas/:id} : get the "id" visa.
     *
     * @param id the id of the visa to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the visa, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/visas/{id}")
    public ResponseEntity<Visa> getVisa(@PathVariable Long id) {
        log.debug("REST request to get Visa : {}", id);
        Optional<Visa> visa = visaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(visa);
    }

    /**
     * {@code DELETE  /visas/:id} : delete the "id" visa.
     *
     * @param id the id of the visa to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/visas/{id}")
    public ResponseEntity<Void> deleteVisa(@PathVariable Long id) {
        log.debug("REST request to delete Visa : {}", id);
        visaRepository.deleteById(id);
        visaSearchRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/visas?query=:query} : search for the visa corresponding
     * to the query.
     *
     * @param query the query of the visa search.
     * @return the result of the search.
     */
    @GetMapping("/_search/visas")
    public List<Visa> searchVisas(@RequestParam String query) {
        log.debug("REST request to search Visas for query {}", query);
        return StreamSupport.stream(visaSearchRepository.search(queryStringQuery(query)).spliterator(), false).collect(Collectors.toList());
    }
}
