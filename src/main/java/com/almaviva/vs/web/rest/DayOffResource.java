package com.almaviva.vs.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.almaviva.vs.domain.DayOff;
import com.almaviva.vs.repository.DayOffRepository;
import com.almaviva.vs.repository.search.DayOffSearchRepository;
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
 * REST controller for managing {@link com.almaviva.vs.domain.DayOff}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class DayOffResource {

    private final Logger log = LoggerFactory.getLogger(DayOffResource.class);

    private static final String ENTITY_NAME = "dayOff";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DayOffRepository dayOffRepository;

    private final DayOffSearchRepository dayOffSearchRepository;

    public DayOffResource(DayOffRepository dayOffRepository, DayOffSearchRepository dayOffSearchRepository) {
        this.dayOffRepository = dayOffRepository;
        this.dayOffSearchRepository = dayOffSearchRepository;
    }

    /**
     * {@code POST  /day-offs} : Create a new dayOff.
     *
     * @param dayOff the dayOff to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dayOff, or with status {@code 400 (Bad Request)} if the dayOff has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/day-offs")
    public ResponseEntity<DayOff> createDayOff(@RequestBody DayOff dayOff) throws URISyntaxException {
        log.debug("REST request to save DayOff : {}", dayOff);
        if (dayOff.getId() != null) {
            throw new BadRequestAlertException("A new dayOff cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DayOff result = dayOffRepository.save(dayOff);
        dayOffSearchRepository.save(result);
        return ResponseEntity
            .created(new URI("/api/day-offs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /day-offs/:id} : Updates an existing dayOff.
     *
     * @param id the id of the dayOff to save.
     * @param dayOff the dayOff to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dayOff,
     * or with status {@code 400 (Bad Request)} if the dayOff is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dayOff couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/day-offs/{id}")
    public ResponseEntity<DayOff> updateDayOff(@PathVariable(value = "id", required = false) final Long id, @RequestBody DayOff dayOff)
        throws URISyntaxException {
        log.debug("REST request to update DayOff : {}, {}", id, dayOff);
        if (dayOff.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dayOff.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dayOffRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DayOff result = dayOffRepository.save(dayOff);
        dayOffSearchRepository.save(result);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dayOff.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /day-offs/:id} : Partial updates given fields of an existing dayOff, field will ignore if it is null
     *
     * @param id the id of the dayOff to save.
     * @param dayOff the dayOff to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dayOff,
     * or with status {@code 400 (Bad Request)} if the dayOff is not valid,
     * or with status {@code 404 (Not Found)} if the dayOff is not found,
     * or with status {@code 500 (Internal Server Error)} if the dayOff couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/day-offs/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<DayOff> partialUpdateDayOff(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DayOff dayOff
    ) throws URISyntaxException {
        log.debug("REST request to partial update DayOff partially : {}, {}", id, dayOff);
        if (dayOff.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dayOff.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dayOffRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DayOff> result = dayOffRepository
            .findById(dayOff.getId())
            .map(
                existingDayOff -> {
                    if (dayOff.getTitle() != null) {
                        existingDayOff.setTitle(dayOff.getTitle());
                    }
                    if (dayOff.getDescription() != null) {
                        existingDayOff.setDescription(dayOff.getDescription());
                    }
                    if (dayOff.getDate() != null) {
                        existingDayOff.setDate(dayOff.getDate());
                    }
                    if (dayOff.getIsHoliday() != null) {
                        existingDayOff.setIsHoliday(dayOff.getIsHoliday());
                    }
                    if (dayOff.getDeleted() != null) {
                        existingDayOff.setDeleted(dayOff.getDeleted());
                    }

                    return existingDayOff;
                }
            )
            .map(dayOffRepository::save)
            .map(
                savedDayOff -> {
                    dayOffSearchRepository.save(savedDayOff);

                    return savedDayOff;
                }
            );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dayOff.getId().toString())
        );
    }

    /**
     * {@code GET  /day-offs} : get all the dayOffs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dayOffs in body.
     */
    @GetMapping("/day-offs")
    public List<DayOff> getAllDayOffs() {
        log.debug("REST request to get all DayOffs");
        return dayOffRepository.findAll();
    }

    /**
     * {@code GET  /day-offs/:id} : get the "id" dayOff.
     *
     * @param id the id of the dayOff to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dayOff, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/day-offs/{id}")
    public ResponseEntity<DayOff> getDayOff(@PathVariable Long id) {
        log.debug("REST request to get DayOff : {}", id);
        Optional<DayOff> dayOff = dayOffRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(dayOff);
    }

    /**
     * {@code DELETE  /day-offs/:id} : delete the "id" dayOff.
     *
     * @param id the id of the dayOff to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/day-offs/{id}")
    public ResponseEntity<Void> deleteDayOff(@PathVariable Long id) {
        log.debug("REST request to delete DayOff : {}", id);
        dayOffRepository.deleteById(id);
        dayOffSearchRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/day-offs?query=:query} : search for the dayOff corresponding
     * to the query.
     *
     * @param query the query of the dayOff search.
     * @return the result of the search.
     */
    @GetMapping("/_search/day-offs")
    public List<DayOff> searchDayOffs(@RequestParam String query) {
        log.debug("REST request to search DayOffs for query {}", query);
        return StreamSupport
            .stream(dayOffSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
