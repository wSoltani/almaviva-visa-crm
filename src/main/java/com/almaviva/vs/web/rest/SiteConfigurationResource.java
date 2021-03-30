package com.almaviva.vs.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.almaviva.vs.domain.SiteConfiguration;
import com.almaviva.vs.repository.SiteConfigurationRepository;
import com.almaviva.vs.repository.search.SiteConfigurationSearchRepository;
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
 * REST controller for managing {@link com.almaviva.vs.domain.SiteConfiguration}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SiteConfigurationResource {

    private final Logger log = LoggerFactory.getLogger(SiteConfigurationResource.class);

    private static final String ENTITY_NAME = "siteConfiguration";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SiteConfigurationRepository siteConfigurationRepository;

    private final SiteConfigurationSearchRepository siteConfigurationSearchRepository;

    public SiteConfigurationResource(
        SiteConfigurationRepository siteConfigurationRepository,
        SiteConfigurationSearchRepository siteConfigurationSearchRepository
    ) {
        this.siteConfigurationRepository = siteConfigurationRepository;
        this.siteConfigurationSearchRepository = siteConfigurationSearchRepository;
    }

    /**
     * {@code POST  /site-configurations} : Create a new siteConfiguration.
     *
     * @param siteConfiguration the siteConfiguration to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new siteConfiguration, or with status {@code 400 (Bad Request)} if the siteConfiguration has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/site-configurations")
    public ResponseEntity<SiteConfiguration> createSiteConfiguration(@RequestBody SiteConfiguration siteConfiguration)
        throws URISyntaxException {
        log.debug("REST request to save SiteConfiguration : {}", siteConfiguration);
        if (siteConfiguration.getId() != null) {
            throw new BadRequestAlertException("A new siteConfiguration cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SiteConfiguration result = siteConfigurationRepository.save(siteConfiguration);
        siteConfigurationSearchRepository.save(result);
        return ResponseEntity
            .created(new URI("/api/site-configurations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /site-configurations/:id} : Updates an existing siteConfiguration.
     *
     * @param id the id of the siteConfiguration to save.
     * @param siteConfiguration the siteConfiguration to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated siteConfiguration,
     * or with status {@code 400 (Bad Request)} if the siteConfiguration is not valid,
     * or with status {@code 500 (Internal Server Error)} if the siteConfiguration couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/site-configurations/{id}")
    public ResponseEntity<SiteConfiguration> updateSiteConfiguration(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SiteConfiguration siteConfiguration
    ) throws URISyntaxException {
        log.debug("REST request to update SiteConfiguration : {}, {}", id, siteConfiguration);
        if (siteConfiguration.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, siteConfiguration.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!siteConfigurationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SiteConfiguration result = siteConfigurationRepository.save(siteConfiguration);
        siteConfigurationSearchRepository.save(result);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, siteConfiguration.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /site-configurations/:id} : Partial updates given fields of an existing siteConfiguration, field will ignore if it is null
     *
     * @param id the id of the siteConfiguration to save.
     * @param siteConfiguration the siteConfiguration to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated siteConfiguration,
     * or with status {@code 400 (Bad Request)} if the siteConfiguration is not valid,
     * or with status {@code 404 (Not Found)} if the siteConfiguration is not found,
     * or with status {@code 500 (Internal Server Error)} if the siteConfiguration couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/site-configurations/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<SiteConfiguration> partialUpdateSiteConfiguration(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SiteConfiguration siteConfiguration
    ) throws URISyntaxException {
        log.debug("REST request to partial update SiteConfiguration partially : {}, {}", id, siteConfiguration);
        if (siteConfiguration.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, siteConfiguration.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!siteConfigurationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SiteConfiguration> result = siteConfigurationRepository
            .findById(siteConfiguration.getId())
            .map(
                existingSiteConfiguration -> {
                    if (siteConfiguration.getStartDate() != null) {
                        existingSiteConfiguration.setStartDate(siteConfiguration.getStartDate());
                    }
                    if (siteConfiguration.getEndDate() != null) {
                        existingSiteConfiguration.setEndDate(siteConfiguration.getEndDate());
                    }
                    if (siteConfiguration.getOpeningTime() != null) {
                        existingSiteConfiguration.setOpeningTime(siteConfiguration.getOpeningTime());
                    }
                    if (siteConfiguration.getClosingTime() != null) {
                        existingSiteConfiguration.setClosingTime(siteConfiguration.getClosingTime());
                    }
                    if (siteConfiguration.getAppointmentTime() != null) {
                        existingSiteConfiguration.setAppointmentTime(siteConfiguration.getAppointmentTime());
                    }
                    if (siteConfiguration.getAppointmentQuota() != null) {
                        existingSiteConfiguration.setAppointmentQuota(siteConfiguration.getAppointmentQuota());
                    }
                    if (siteConfiguration.getAppointmentQuotaWeb() != null) {
                        existingSiteConfiguration.setAppointmentQuotaWeb(siteConfiguration.getAppointmentQuotaWeb());
                    }
                    if (siteConfiguration.getInformation() != null) {
                        existingSiteConfiguration.setInformation(siteConfiguration.getInformation());
                    }
                    if (siteConfiguration.getDailyMessage() != null) {
                        existingSiteConfiguration.setDailyMessage(siteConfiguration.getDailyMessage());
                    }
                    if (siteConfiguration.getPrestationMargin() != null) {
                        existingSiteConfiguration.setPrestationMargin(siteConfiguration.getPrestationMargin());
                    }
                    if (siteConfiguration.getSimultaneous() != null) {
                        existingSiteConfiguration.setSimultaneous(siteConfiguration.getSimultaneous());
                    }
                    if (siteConfiguration.getIsSpecific() != null) {
                        existingSiteConfiguration.setIsSpecific(siteConfiguration.getIsSpecific());
                    }
                    if (siteConfiguration.getDeleted() != null) {
                        existingSiteConfiguration.setDeleted(siteConfiguration.getDeleted());
                    }

                    return existingSiteConfiguration;
                }
            )
            .map(siteConfigurationRepository::save)
            .map(
                savedSiteConfiguration -> {
                    siteConfigurationSearchRepository.save(savedSiteConfiguration);

                    return savedSiteConfiguration;
                }
            );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, siteConfiguration.getId().toString())
        );
    }

    /**
     * {@code GET  /site-configurations} : get all the siteConfigurations.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of siteConfigurations in body.
     */
    @GetMapping("/site-configurations")
    public List<SiteConfiguration> getAllSiteConfigurations() {
        log.debug("REST request to get all SiteConfigurations");
        return siteConfigurationRepository.findAll();
    }

    /**
     * {@code GET  /site-configurations/:id} : get the "id" siteConfiguration.
     *
     * @param id the id of the siteConfiguration to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the siteConfiguration, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/site-configurations/{id}")
    public ResponseEntity<SiteConfiguration> getSiteConfiguration(@PathVariable Long id) {
        log.debug("REST request to get SiteConfiguration : {}", id);
        Optional<SiteConfiguration> siteConfiguration = siteConfigurationRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(siteConfiguration);
    }

    /**
     * {@code DELETE  /site-configurations/:id} : delete the "id" siteConfiguration.
     *
     * @param id the id of the siteConfiguration to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/site-configurations/{id}")
    public ResponseEntity<Void> deleteSiteConfiguration(@PathVariable Long id) {
        log.debug("REST request to delete SiteConfiguration : {}", id);
        siteConfigurationRepository.deleteById(id);
        siteConfigurationSearchRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/site-configurations?query=:query} : search for the siteConfiguration corresponding
     * to the query.
     *
     * @param query the query of the siteConfiguration search.
     * @return the result of the search.
     */
    @GetMapping("/_search/site-configurations")
    public List<SiteConfiguration> searchSiteConfigurations(@RequestParam String query) {
        log.debug("REST request to search SiteConfigurations for query {}", query);
        return StreamSupport
            .stream(siteConfigurationSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
