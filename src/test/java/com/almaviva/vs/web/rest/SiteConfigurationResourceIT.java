package com.almaviva.vs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.almaviva.vs.IntegrationTest;
import com.almaviva.vs.domain.SiteConfiguration;
import com.almaviva.vs.repository.SiteConfigurationRepository;
import com.almaviva.vs.repository.search.SiteConfigurationSearchRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SiteConfigurationResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SiteConfigurationResourceIT {

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_OPENING_TIME = "AAAAAAAAAA";
    private static final String UPDATED_OPENING_TIME = "BBBBBBBBBB";

    private static final String DEFAULT_CLOSING_TIME = "AAAAAAAAAA";
    private static final String UPDATED_CLOSING_TIME = "BBBBBBBBBB";

    private static final Integer DEFAULT_APPOINTMENT_TIME = 1;
    private static final Integer UPDATED_APPOINTMENT_TIME = 2;

    private static final Integer DEFAULT_APPOINTMENT_QUOTA = 1;
    private static final Integer UPDATED_APPOINTMENT_QUOTA = 2;

    private static final Integer DEFAULT_APPOINTMENT_QUOTA_WEB = 1;
    private static final Integer UPDATED_APPOINTMENT_QUOTA_WEB = 2;

    private static final String DEFAULT_INFORMATION = "AAAAAAAAAA";
    private static final String UPDATED_INFORMATION = "BBBBBBBBBB";

    private static final String DEFAULT_DAILY_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_DAILY_MESSAGE = "BBBBBBBBBB";

    private static final Integer DEFAULT_PRESTATION_MARGIN = 1;
    private static final Integer UPDATED_PRESTATION_MARGIN = 2;

    private static final Integer DEFAULT_SIMULTANEOUS = 1;
    private static final Integer UPDATED_SIMULTANEOUS = 2;

    private static final Boolean DEFAULT_IS_SPECIFIC = false;
    private static final Boolean UPDATED_IS_SPECIFIC = true;

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;

    private static final String ENTITY_API_URL = "/api/site-configurations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/site-configurations";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SiteConfigurationRepository siteConfigurationRepository;

    /**
     * This repository is mocked in the com.almaviva.vs.repository.search test package.
     *
     * @see com.almaviva.vs.repository.search.SiteConfigurationSearchRepositoryMockConfiguration
     */
    @Autowired
    private SiteConfigurationSearchRepository mockSiteConfigurationSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSiteConfigurationMockMvc;

    private SiteConfiguration siteConfiguration;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SiteConfiguration createEntity(EntityManager em) {
        SiteConfiguration siteConfiguration = new SiteConfiguration()
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .openingTime(DEFAULT_OPENING_TIME)
            .closingTime(DEFAULT_CLOSING_TIME)
            .appointmentTime(DEFAULT_APPOINTMENT_TIME)
            .appointmentQuota(DEFAULT_APPOINTMENT_QUOTA)
            .appointmentQuotaWeb(DEFAULT_APPOINTMENT_QUOTA_WEB)
            .information(DEFAULT_INFORMATION)
            .dailyMessage(DEFAULT_DAILY_MESSAGE)
            .prestationMargin(DEFAULT_PRESTATION_MARGIN)
            .simultaneous(DEFAULT_SIMULTANEOUS)
            .isSpecific(DEFAULT_IS_SPECIFIC)
            .deleted(DEFAULT_DELETED);
        return siteConfiguration;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SiteConfiguration createUpdatedEntity(EntityManager em) {
        SiteConfiguration siteConfiguration = new SiteConfiguration()
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .openingTime(UPDATED_OPENING_TIME)
            .closingTime(UPDATED_CLOSING_TIME)
            .appointmentTime(UPDATED_APPOINTMENT_TIME)
            .appointmentQuota(UPDATED_APPOINTMENT_QUOTA)
            .appointmentQuotaWeb(UPDATED_APPOINTMENT_QUOTA_WEB)
            .information(UPDATED_INFORMATION)
            .dailyMessage(UPDATED_DAILY_MESSAGE)
            .prestationMargin(UPDATED_PRESTATION_MARGIN)
            .simultaneous(UPDATED_SIMULTANEOUS)
            .isSpecific(UPDATED_IS_SPECIFIC)
            .deleted(UPDATED_DELETED);
        return siteConfiguration;
    }

    @BeforeEach
    public void initTest() {
        siteConfiguration = createEntity(em);
    }

    @Test
    @Transactional
    void createSiteConfiguration() throws Exception {
        int databaseSizeBeforeCreate = siteConfigurationRepository.findAll().size();
        // Create the SiteConfiguration
        restSiteConfigurationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(siteConfiguration))
            )
            .andExpect(status().isCreated());

        // Validate the SiteConfiguration in the database
        List<SiteConfiguration> siteConfigurationList = siteConfigurationRepository.findAll();
        assertThat(siteConfigurationList).hasSize(databaseSizeBeforeCreate + 1);
        SiteConfiguration testSiteConfiguration = siteConfigurationList.get(siteConfigurationList.size() - 1);
        assertThat(testSiteConfiguration.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testSiteConfiguration.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testSiteConfiguration.getOpeningTime()).isEqualTo(DEFAULT_OPENING_TIME);
        assertThat(testSiteConfiguration.getClosingTime()).isEqualTo(DEFAULT_CLOSING_TIME);
        assertThat(testSiteConfiguration.getAppointmentTime()).isEqualTo(DEFAULT_APPOINTMENT_TIME);
        assertThat(testSiteConfiguration.getAppointmentQuota()).isEqualTo(DEFAULT_APPOINTMENT_QUOTA);
        assertThat(testSiteConfiguration.getAppointmentQuotaWeb()).isEqualTo(DEFAULT_APPOINTMENT_QUOTA_WEB);
        assertThat(testSiteConfiguration.getInformation()).isEqualTo(DEFAULT_INFORMATION);
        assertThat(testSiteConfiguration.getDailyMessage()).isEqualTo(DEFAULT_DAILY_MESSAGE);
        assertThat(testSiteConfiguration.getPrestationMargin()).isEqualTo(DEFAULT_PRESTATION_MARGIN);
        assertThat(testSiteConfiguration.getSimultaneous()).isEqualTo(DEFAULT_SIMULTANEOUS);
        assertThat(testSiteConfiguration.getIsSpecific()).isEqualTo(DEFAULT_IS_SPECIFIC);
        assertThat(testSiteConfiguration.getDeleted()).isEqualTo(DEFAULT_DELETED);

        // Validate the SiteConfiguration in Elasticsearch
        verify(mockSiteConfigurationSearchRepository, times(1)).save(testSiteConfiguration);
    }

    @Test
    @Transactional
    void createSiteConfigurationWithExistingId() throws Exception {
        // Create the SiteConfiguration with an existing ID
        siteConfiguration.setId(1L);

        int databaseSizeBeforeCreate = siteConfigurationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSiteConfigurationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(siteConfiguration))
            )
            .andExpect(status().isBadRequest());

        // Validate the SiteConfiguration in the database
        List<SiteConfiguration> siteConfigurationList = siteConfigurationRepository.findAll();
        assertThat(siteConfigurationList).hasSize(databaseSizeBeforeCreate);

        // Validate the SiteConfiguration in Elasticsearch
        verify(mockSiteConfigurationSearchRepository, times(0)).save(siteConfiguration);
    }

    @Test
    @Transactional
    void getAllSiteConfigurations() throws Exception {
        // Initialize the database
        siteConfigurationRepository.saveAndFlush(siteConfiguration);

        // Get all the siteConfigurationList
        restSiteConfigurationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(siteConfiguration.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].openingTime").value(hasItem(DEFAULT_OPENING_TIME)))
            .andExpect(jsonPath("$.[*].closingTime").value(hasItem(DEFAULT_CLOSING_TIME)))
            .andExpect(jsonPath("$.[*].appointmentTime").value(hasItem(DEFAULT_APPOINTMENT_TIME)))
            .andExpect(jsonPath("$.[*].appointmentQuota").value(hasItem(DEFAULT_APPOINTMENT_QUOTA)))
            .andExpect(jsonPath("$.[*].appointmentQuotaWeb").value(hasItem(DEFAULT_APPOINTMENT_QUOTA_WEB)))
            .andExpect(jsonPath("$.[*].information").value(hasItem(DEFAULT_INFORMATION)))
            .andExpect(jsonPath("$.[*].dailyMessage").value(hasItem(DEFAULT_DAILY_MESSAGE)))
            .andExpect(jsonPath("$.[*].prestationMargin").value(hasItem(DEFAULT_PRESTATION_MARGIN)))
            .andExpect(jsonPath("$.[*].simultaneous").value(hasItem(DEFAULT_SIMULTANEOUS)))
            .andExpect(jsonPath("$.[*].isSpecific").value(hasItem(DEFAULT_IS_SPECIFIC.booleanValue())))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())));
    }

    @Test
    @Transactional
    void getSiteConfiguration() throws Exception {
        // Initialize the database
        siteConfigurationRepository.saveAndFlush(siteConfiguration);

        // Get the siteConfiguration
        restSiteConfigurationMockMvc
            .perform(get(ENTITY_API_URL_ID, siteConfiguration.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(siteConfiguration.getId().intValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.openingTime").value(DEFAULT_OPENING_TIME))
            .andExpect(jsonPath("$.closingTime").value(DEFAULT_CLOSING_TIME))
            .andExpect(jsonPath("$.appointmentTime").value(DEFAULT_APPOINTMENT_TIME))
            .andExpect(jsonPath("$.appointmentQuota").value(DEFAULT_APPOINTMENT_QUOTA))
            .andExpect(jsonPath("$.appointmentQuotaWeb").value(DEFAULT_APPOINTMENT_QUOTA_WEB))
            .andExpect(jsonPath("$.information").value(DEFAULT_INFORMATION))
            .andExpect(jsonPath("$.dailyMessage").value(DEFAULT_DAILY_MESSAGE))
            .andExpect(jsonPath("$.prestationMargin").value(DEFAULT_PRESTATION_MARGIN))
            .andExpect(jsonPath("$.simultaneous").value(DEFAULT_SIMULTANEOUS))
            .andExpect(jsonPath("$.isSpecific").value(DEFAULT_IS_SPECIFIC.booleanValue()))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingSiteConfiguration() throws Exception {
        // Get the siteConfiguration
        restSiteConfigurationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSiteConfiguration() throws Exception {
        // Initialize the database
        siteConfigurationRepository.saveAndFlush(siteConfiguration);

        int databaseSizeBeforeUpdate = siteConfigurationRepository.findAll().size();

        // Update the siteConfiguration
        SiteConfiguration updatedSiteConfiguration = siteConfigurationRepository.findById(siteConfiguration.getId()).get();
        // Disconnect from session so that the updates on updatedSiteConfiguration are not directly saved in db
        em.detach(updatedSiteConfiguration);
        updatedSiteConfiguration
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .openingTime(UPDATED_OPENING_TIME)
            .closingTime(UPDATED_CLOSING_TIME)
            .appointmentTime(UPDATED_APPOINTMENT_TIME)
            .appointmentQuota(UPDATED_APPOINTMENT_QUOTA)
            .appointmentQuotaWeb(UPDATED_APPOINTMENT_QUOTA_WEB)
            .information(UPDATED_INFORMATION)
            .dailyMessage(UPDATED_DAILY_MESSAGE)
            .prestationMargin(UPDATED_PRESTATION_MARGIN)
            .simultaneous(UPDATED_SIMULTANEOUS)
            .isSpecific(UPDATED_IS_SPECIFIC)
            .deleted(UPDATED_DELETED);

        restSiteConfigurationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSiteConfiguration.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSiteConfiguration))
            )
            .andExpect(status().isOk());

        // Validate the SiteConfiguration in the database
        List<SiteConfiguration> siteConfigurationList = siteConfigurationRepository.findAll();
        assertThat(siteConfigurationList).hasSize(databaseSizeBeforeUpdate);
        SiteConfiguration testSiteConfiguration = siteConfigurationList.get(siteConfigurationList.size() - 1);
        assertThat(testSiteConfiguration.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testSiteConfiguration.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testSiteConfiguration.getOpeningTime()).isEqualTo(UPDATED_OPENING_TIME);
        assertThat(testSiteConfiguration.getClosingTime()).isEqualTo(UPDATED_CLOSING_TIME);
        assertThat(testSiteConfiguration.getAppointmentTime()).isEqualTo(UPDATED_APPOINTMENT_TIME);
        assertThat(testSiteConfiguration.getAppointmentQuota()).isEqualTo(UPDATED_APPOINTMENT_QUOTA);
        assertThat(testSiteConfiguration.getAppointmentQuotaWeb()).isEqualTo(UPDATED_APPOINTMENT_QUOTA_WEB);
        assertThat(testSiteConfiguration.getInformation()).isEqualTo(UPDATED_INFORMATION);
        assertThat(testSiteConfiguration.getDailyMessage()).isEqualTo(UPDATED_DAILY_MESSAGE);
        assertThat(testSiteConfiguration.getPrestationMargin()).isEqualTo(UPDATED_PRESTATION_MARGIN);
        assertThat(testSiteConfiguration.getSimultaneous()).isEqualTo(UPDATED_SIMULTANEOUS);
        assertThat(testSiteConfiguration.getIsSpecific()).isEqualTo(UPDATED_IS_SPECIFIC);
        assertThat(testSiteConfiguration.getDeleted()).isEqualTo(UPDATED_DELETED);

        // Validate the SiteConfiguration in Elasticsearch
        verify(mockSiteConfigurationSearchRepository).save(testSiteConfiguration);
    }

    @Test
    @Transactional
    void putNonExistingSiteConfiguration() throws Exception {
        int databaseSizeBeforeUpdate = siteConfigurationRepository.findAll().size();
        siteConfiguration.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSiteConfigurationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, siteConfiguration.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(siteConfiguration))
            )
            .andExpect(status().isBadRequest());

        // Validate the SiteConfiguration in the database
        List<SiteConfiguration> siteConfigurationList = siteConfigurationRepository.findAll();
        assertThat(siteConfigurationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SiteConfiguration in Elasticsearch
        verify(mockSiteConfigurationSearchRepository, times(0)).save(siteConfiguration);
    }

    @Test
    @Transactional
    void putWithIdMismatchSiteConfiguration() throws Exception {
        int databaseSizeBeforeUpdate = siteConfigurationRepository.findAll().size();
        siteConfiguration.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSiteConfigurationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(siteConfiguration))
            )
            .andExpect(status().isBadRequest());

        // Validate the SiteConfiguration in the database
        List<SiteConfiguration> siteConfigurationList = siteConfigurationRepository.findAll();
        assertThat(siteConfigurationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SiteConfiguration in Elasticsearch
        verify(mockSiteConfigurationSearchRepository, times(0)).save(siteConfiguration);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSiteConfiguration() throws Exception {
        int databaseSizeBeforeUpdate = siteConfigurationRepository.findAll().size();
        siteConfiguration.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSiteConfigurationMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(siteConfiguration))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SiteConfiguration in the database
        List<SiteConfiguration> siteConfigurationList = siteConfigurationRepository.findAll();
        assertThat(siteConfigurationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SiteConfiguration in Elasticsearch
        verify(mockSiteConfigurationSearchRepository, times(0)).save(siteConfiguration);
    }

    @Test
    @Transactional
    void partialUpdateSiteConfigurationWithPatch() throws Exception {
        // Initialize the database
        siteConfigurationRepository.saveAndFlush(siteConfiguration);

        int databaseSizeBeforeUpdate = siteConfigurationRepository.findAll().size();

        // Update the siteConfiguration using partial update
        SiteConfiguration partialUpdatedSiteConfiguration = new SiteConfiguration();
        partialUpdatedSiteConfiguration.setId(siteConfiguration.getId());

        partialUpdatedSiteConfiguration
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .closingTime(UPDATED_CLOSING_TIME)
            .appointmentQuota(UPDATED_APPOINTMENT_QUOTA)
            .appointmentQuotaWeb(UPDATED_APPOINTMENT_QUOTA_WEB)
            .simultaneous(UPDATED_SIMULTANEOUS)
            .isSpecific(UPDATED_IS_SPECIFIC);

        restSiteConfigurationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSiteConfiguration.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSiteConfiguration))
            )
            .andExpect(status().isOk());

        // Validate the SiteConfiguration in the database
        List<SiteConfiguration> siteConfigurationList = siteConfigurationRepository.findAll();
        assertThat(siteConfigurationList).hasSize(databaseSizeBeforeUpdate);
        SiteConfiguration testSiteConfiguration = siteConfigurationList.get(siteConfigurationList.size() - 1);
        assertThat(testSiteConfiguration.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testSiteConfiguration.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testSiteConfiguration.getOpeningTime()).isEqualTo(DEFAULT_OPENING_TIME);
        assertThat(testSiteConfiguration.getClosingTime()).isEqualTo(UPDATED_CLOSING_TIME);
        assertThat(testSiteConfiguration.getAppointmentTime()).isEqualTo(DEFAULT_APPOINTMENT_TIME);
        assertThat(testSiteConfiguration.getAppointmentQuota()).isEqualTo(UPDATED_APPOINTMENT_QUOTA);
        assertThat(testSiteConfiguration.getAppointmentQuotaWeb()).isEqualTo(UPDATED_APPOINTMENT_QUOTA_WEB);
        assertThat(testSiteConfiguration.getInformation()).isEqualTo(DEFAULT_INFORMATION);
        assertThat(testSiteConfiguration.getDailyMessage()).isEqualTo(DEFAULT_DAILY_MESSAGE);
        assertThat(testSiteConfiguration.getPrestationMargin()).isEqualTo(DEFAULT_PRESTATION_MARGIN);
        assertThat(testSiteConfiguration.getSimultaneous()).isEqualTo(UPDATED_SIMULTANEOUS);
        assertThat(testSiteConfiguration.getIsSpecific()).isEqualTo(UPDATED_IS_SPECIFIC);
        assertThat(testSiteConfiguration.getDeleted()).isEqualTo(DEFAULT_DELETED);
    }

    @Test
    @Transactional
    void fullUpdateSiteConfigurationWithPatch() throws Exception {
        // Initialize the database
        siteConfigurationRepository.saveAndFlush(siteConfiguration);

        int databaseSizeBeforeUpdate = siteConfigurationRepository.findAll().size();

        // Update the siteConfiguration using partial update
        SiteConfiguration partialUpdatedSiteConfiguration = new SiteConfiguration();
        partialUpdatedSiteConfiguration.setId(siteConfiguration.getId());

        partialUpdatedSiteConfiguration
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .openingTime(UPDATED_OPENING_TIME)
            .closingTime(UPDATED_CLOSING_TIME)
            .appointmentTime(UPDATED_APPOINTMENT_TIME)
            .appointmentQuota(UPDATED_APPOINTMENT_QUOTA)
            .appointmentQuotaWeb(UPDATED_APPOINTMENT_QUOTA_WEB)
            .information(UPDATED_INFORMATION)
            .dailyMessage(UPDATED_DAILY_MESSAGE)
            .prestationMargin(UPDATED_PRESTATION_MARGIN)
            .simultaneous(UPDATED_SIMULTANEOUS)
            .isSpecific(UPDATED_IS_SPECIFIC)
            .deleted(UPDATED_DELETED);

        restSiteConfigurationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSiteConfiguration.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSiteConfiguration))
            )
            .andExpect(status().isOk());

        // Validate the SiteConfiguration in the database
        List<SiteConfiguration> siteConfigurationList = siteConfigurationRepository.findAll();
        assertThat(siteConfigurationList).hasSize(databaseSizeBeforeUpdate);
        SiteConfiguration testSiteConfiguration = siteConfigurationList.get(siteConfigurationList.size() - 1);
        assertThat(testSiteConfiguration.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testSiteConfiguration.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testSiteConfiguration.getOpeningTime()).isEqualTo(UPDATED_OPENING_TIME);
        assertThat(testSiteConfiguration.getClosingTime()).isEqualTo(UPDATED_CLOSING_TIME);
        assertThat(testSiteConfiguration.getAppointmentTime()).isEqualTo(UPDATED_APPOINTMENT_TIME);
        assertThat(testSiteConfiguration.getAppointmentQuota()).isEqualTo(UPDATED_APPOINTMENT_QUOTA);
        assertThat(testSiteConfiguration.getAppointmentQuotaWeb()).isEqualTo(UPDATED_APPOINTMENT_QUOTA_WEB);
        assertThat(testSiteConfiguration.getInformation()).isEqualTo(UPDATED_INFORMATION);
        assertThat(testSiteConfiguration.getDailyMessage()).isEqualTo(UPDATED_DAILY_MESSAGE);
        assertThat(testSiteConfiguration.getPrestationMargin()).isEqualTo(UPDATED_PRESTATION_MARGIN);
        assertThat(testSiteConfiguration.getSimultaneous()).isEqualTo(UPDATED_SIMULTANEOUS);
        assertThat(testSiteConfiguration.getIsSpecific()).isEqualTo(UPDATED_IS_SPECIFIC);
        assertThat(testSiteConfiguration.getDeleted()).isEqualTo(UPDATED_DELETED);
    }

    @Test
    @Transactional
    void patchNonExistingSiteConfiguration() throws Exception {
        int databaseSizeBeforeUpdate = siteConfigurationRepository.findAll().size();
        siteConfiguration.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSiteConfigurationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, siteConfiguration.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(siteConfiguration))
            )
            .andExpect(status().isBadRequest());

        // Validate the SiteConfiguration in the database
        List<SiteConfiguration> siteConfigurationList = siteConfigurationRepository.findAll();
        assertThat(siteConfigurationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SiteConfiguration in Elasticsearch
        verify(mockSiteConfigurationSearchRepository, times(0)).save(siteConfiguration);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSiteConfiguration() throws Exception {
        int databaseSizeBeforeUpdate = siteConfigurationRepository.findAll().size();
        siteConfiguration.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSiteConfigurationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(siteConfiguration))
            )
            .andExpect(status().isBadRequest());

        // Validate the SiteConfiguration in the database
        List<SiteConfiguration> siteConfigurationList = siteConfigurationRepository.findAll();
        assertThat(siteConfigurationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SiteConfiguration in Elasticsearch
        verify(mockSiteConfigurationSearchRepository, times(0)).save(siteConfiguration);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSiteConfiguration() throws Exception {
        int databaseSizeBeforeUpdate = siteConfigurationRepository.findAll().size();
        siteConfiguration.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSiteConfigurationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(siteConfiguration))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SiteConfiguration in the database
        List<SiteConfiguration> siteConfigurationList = siteConfigurationRepository.findAll();
        assertThat(siteConfigurationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SiteConfiguration in Elasticsearch
        verify(mockSiteConfigurationSearchRepository, times(0)).save(siteConfiguration);
    }

    @Test
    @Transactional
    void deleteSiteConfiguration() throws Exception {
        // Initialize the database
        siteConfigurationRepository.saveAndFlush(siteConfiguration);

        int databaseSizeBeforeDelete = siteConfigurationRepository.findAll().size();

        // Delete the siteConfiguration
        restSiteConfigurationMockMvc
            .perform(delete(ENTITY_API_URL_ID, siteConfiguration.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SiteConfiguration> siteConfigurationList = siteConfigurationRepository.findAll();
        assertThat(siteConfigurationList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the SiteConfiguration in Elasticsearch
        verify(mockSiteConfigurationSearchRepository, times(1)).deleteById(siteConfiguration.getId());
    }

    @Test
    @Transactional
    void searchSiteConfiguration() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        siteConfigurationRepository.saveAndFlush(siteConfiguration);
        when(mockSiteConfigurationSearchRepository.search(queryStringQuery("id:" + siteConfiguration.getId())))
            .thenReturn(Collections.singletonList(siteConfiguration));

        // Search the siteConfiguration
        restSiteConfigurationMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + siteConfiguration.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(siteConfiguration.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].openingTime").value(hasItem(DEFAULT_OPENING_TIME)))
            .andExpect(jsonPath("$.[*].closingTime").value(hasItem(DEFAULT_CLOSING_TIME)))
            .andExpect(jsonPath("$.[*].appointmentTime").value(hasItem(DEFAULT_APPOINTMENT_TIME)))
            .andExpect(jsonPath("$.[*].appointmentQuota").value(hasItem(DEFAULT_APPOINTMENT_QUOTA)))
            .andExpect(jsonPath("$.[*].appointmentQuotaWeb").value(hasItem(DEFAULT_APPOINTMENT_QUOTA_WEB)))
            .andExpect(jsonPath("$.[*].information").value(hasItem(DEFAULT_INFORMATION)))
            .andExpect(jsonPath("$.[*].dailyMessage").value(hasItem(DEFAULT_DAILY_MESSAGE)))
            .andExpect(jsonPath("$.[*].prestationMargin").value(hasItem(DEFAULT_PRESTATION_MARGIN)))
            .andExpect(jsonPath("$.[*].simultaneous").value(hasItem(DEFAULT_SIMULTANEOUS)))
            .andExpect(jsonPath("$.[*].isSpecific").value(hasItem(DEFAULT_IS_SPECIFIC.booleanValue())))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())));
    }
}
