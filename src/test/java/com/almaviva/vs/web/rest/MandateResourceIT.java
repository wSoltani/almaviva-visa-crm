package com.almaviva.vs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.almaviva.vs.IntegrationTest;
import com.almaviva.vs.domain.Mandate;
import com.almaviva.vs.repository.MandateRepository;
import com.almaviva.vs.repository.search.MandateSearchRepository;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link MandateResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MandateResourceIT {

    private static final Integer DEFAULT_CODE = 1;
    private static final Integer UPDATED_CODE = 2;

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_IS_AVS_PAIMENT = false;
    private static final Boolean UPDATED_IS_AVS_PAIMENT = true;

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;

    private static final String ENTITY_API_URL = "/api/mandates";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/mandates";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MandateRepository mandateRepository;

    /**
     * This repository is mocked in the com.almaviva.vs.repository.search test package.
     *
     * @see com.almaviva.vs.repository.search.MandateSearchRepositoryMockConfiguration
     */
    @Autowired
    private MandateSearchRepository mockMandateSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMandateMockMvc;

    private Mandate mandate;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mandate createEntity(EntityManager em) {
        Mandate mandate = new Mandate()
            .code(DEFAULT_CODE)
            .location(DEFAULT_LOCATION)
            .amount(DEFAULT_AMOUNT)
            .date(DEFAULT_DATE)
            .isAVSPaiment(DEFAULT_IS_AVS_PAIMENT)
            .deleted(DEFAULT_DELETED);
        return mandate;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mandate createUpdatedEntity(EntityManager em) {
        Mandate mandate = new Mandate()
            .code(UPDATED_CODE)
            .location(UPDATED_LOCATION)
            .amount(UPDATED_AMOUNT)
            .date(UPDATED_DATE)
            .isAVSPaiment(UPDATED_IS_AVS_PAIMENT)
            .deleted(UPDATED_DELETED);
        return mandate;
    }

    @BeforeEach
    public void initTest() {
        mandate = createEntity(em);
    }

    @Test
    @Transactional
    void createMandate() throws Exception {
        int databaseSizeBeforeCreate = mandateRepository.findAll().size();
        // Create the Mandate
        restMandateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mandate)))
            .andExpect(status().isCreated());

        // Validate the Mandate in the database
        List<Mandate> mandateList = mandateRepository.findAll();
        assertThat(mandateList).hasSize(databaseSizeBeforeCreate + 1);
        Mandate testMandate = mandateList.get(mandateList.size() - 1);
        assertThat(testMandate.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testMandate.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testMandate.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testMandate.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testMandate.getIsAVSPaiment()).isEqualTo(DEFAULT_IS_AVS_PAIMENT);
        assertThat(testMandate.getDeleted()).isEqualTo(DEFAULT_DELETED);

        // Validate the Mandate in Elasticsearch
        verify(mockMandateSearchRepository, times(1)).save(testMandate);
    }

    @Test
    @Transactional
    void createMandateWithExistingId() throws Exception {
        // Create the Mandate with an existing ID
        mandate.setId(1L);

        int databaseSizeBeforeCreate = mandateRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMandateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mandate)))
            .andExpect(status().isBadRequest());

        // Validate the Mandate in the database
        List<Mandate> mandateList = mandateRepository.findAll();
        assertThat(mandateList).hasSize(databaseSizeBeforeCreate);

        // Validate the Mandate in Elasticsearch
        verify(mockMandateSearchRepository, times(0)).save(mandate);
    }

    @Test
    @Transactional
    void getAllMandates() throws Exception {
        // Initialize the database
        mandateRepository.saveAndFlush(mandate);

        // Get all the mandateList
        restMandateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mandate.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].isAVSPaiment").value(hasItem(DEFAULT_IS_AVS_PAIMENT.booleanValue())))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())));
    }

    @Test
    @Transactional
    void getMandate() throws Exception {
        // Initialize the database
        mandateRepository.saveAndFlush(mandate);

        // Get the mandate
        restMandateMockMvc
            .perform(get(ENTITY_API_URL_ID, mandate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(mandate.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.isAVSPaiment").value(DEFAULT_IS_AVS_PAIMENT.booleanValue()))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingMandate() throws Exception {
        // Get the mandate
        restMandateMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMandate() throws Exception {
        // Initialize the database
        mandateRepository.saveAndFlush(mandate);

        int databaseSizeBeforeUpdate = mandateRepository.findAll().size();

        // Update the mandate
        Mandate updatedMandate = mandateRepository.findById(mandate.getId()).get();
        // Disconnect from session so that the updates on updatedMandate are not directly saved in db
        em.detach(updatedMandate);
        updatedMandate
            .code(UPDATED_CODE)
            .location(UPDATED_LOCATION)
            .amount(UPDATED_AMOUNT)
            .date(UPDATED_DATE)
            .isAVSPaiment(UPDATED_IS_AVS_PAIMENT)
            .deleted(UPDATED_DELETED);

        restMandateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMandate.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMandate))
            )
            .andExpect(status().isOk());

        // Validate the Mandate in the database
        List<Mandate> mandateList = mandateRepository.findAll();
        assertThat(mandateList).hasSize(databaseSizeBeforeUpdate);
        Mandate testMandate = mandateList.get(mandateList.size() - 1);
        assertThat(testMandate.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testMandate.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testMandate.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testMandate.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testMandate.getIsAVSPaiment()).isEqualTo(UPDATED_IS_AVS_PAIMENT);
        assertThat(testMandate.getDeleted()).isEqualTo(UPDATED_DELETED);

        // Validate the Mandate in Elasticsearch
        verify(mockMandateSearchRepository).save(testMandate);
    }

    @Test
    @Transactional
    void putNonExistingMandate() throws Exception {
        int databaseSizeBeforeUpdate = mandateRepository.findAll().size();
        mandate.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMandateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mandate.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mandate))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mandate in the database
        List<Mandate> mandateList = mandateRepository.findAll();
        assertThat(mandateList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Mandate in Elasticsearch
        verify(mockMandateSearchRepository, times(0)).save(mandate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMandate() throws Exception {
        int databaseSizeBeforeUpdate = mandateRepository.findAll().size();
        mandate.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMandateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mandate))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mandate in the database
        List<Mandate> mandateList = mandateRepository.findAll();
        assertThat(mandateList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Mandate in Elasticsearch
        verify(mockMandateSearchRepository, times(0)).save(mandate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMandate() throws Exception {
        int databaseSizeBeforeUpdate = mandateRepository.findAll().size();
        mandate.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMandateMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mandate)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Mandate in the database
        List<Mandate> mandateList = mandateRepository.findAll();
        assertThat(mandateList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Mandate in Elasticsearch
        verify(mockMandateSearchRepository, times(0)).save(mandate);
    }

    @Test
    @Transactional
    void partialUpdateMandateWithPatch() throws Exception {
        // Initialize the database
        mandateRepository.saveAndFlush(mandate);

        int databaseSizeBeforeUpdate = mandateRepository.findAll().size();

        // Update the mandate using partial update
        Mandate partialUpdatedMandate = new Mandate();
        partialUpdatedMandate.setId(mandate.getId());

        partialUpdatedMandate
            .code(UPDATED_CODE)
            .location(UPDATED_LOCATION)
            .amount(UPDATED_AMOUNT)
            .date(UPDATED_DATE)
            .isAVSPaiment(UPDATED_IS_AVS_PAIMENT);

        restMandateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMandate.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMandate))
            )
            .andExpect(status().isOk());

        // Validate the Mandate in the database
        List<Mandate> mandateList = mandateRepository.findAll();
        assertThat(mandateList).hasSize(databaseSizeBeforeUpdate);
        Mandate testMandate = mandateList.get(mandateList.size() - 1);
        assertThat(testMandate.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testMandate.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testMandate.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testMandate.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testMandate.getIsAVSPaiment()).isEqualTo(UPDATED_IS_AVS_PAIMENT);
        assertThat(testMandate.getDeleted()).isEqualTo(DEFAULT_DELETED);
    }

    @Test
    @Transactional
    void fullUpdateMandateWithPatch() throws Exception {
        // Initialize the database
        mandateRepository.saveAndFlush(mandate);

        int databaseSizeBeforeUpdate = mandateRepository.findAll().size();

        // Update the mandate using partial update
        Mandate partialUpdatedMandate = new Mandate();
        partialUpdatedMandate.setId(mandate.getId());

        partialUpdatedMandate
            .code(UPDATED_CODE)
            .location(UPDATED_LOCATION)
            .amount(UPDATED_AMOUNT)
            .date(UPDATED_DATE)
            .isAVSPaiment(UPDATED_IS_AVS_PAIMENT)
            .deleted(UPDATED_DELETED);

        restMandateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMandate.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMandate))
            )
            .andExpect(status().isOk());

        // Validate the Mandate in the database
        List<Mandate> mandateList = mandateRepository.findAll();
        assertThat(mandateList).hasSize(databaseSizeBeforeUpdate);
        Mandate testMandate = mandateList.get(mandateList.size() - 1);
        assertThat(testMandate.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testMandate.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testMandate.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testMandate.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testMandate.getIsAVSPaiment()).isEqualTo(UPDATED_IS_AVS_PAIMENT);
        assertThat(testMandate.getDeleted()).isEqualTo(UPDATED_DELETED);
    }

    @Test
    @Transactional
    void patchNonExistingMandate() throws Exception {
        int databaseSizeBeforeUpdate = mandateRepository.findAll().size();
        mandate.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMandateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, mandate.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mandate))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mandate in the database
        List<Mandate> mandateList = mandateRepository.findAll();
        assertThat(mandateList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Mandate in Elasticsearch
        verify(mockMandateSearchRepository, times(0)).save(mandate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMandate() throws Exception {
        int databaseSizeBeforeUpdate = mandateRepository.findAll().size();
        mandate.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMandateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mandate))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mandate in the database
        List<Mandate> mandateList = mandateRepository.findAll();
        assertThat(mandateList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Mandate in Elasticsearch
        verify(mockMandateSearchRepository, times(0)).save(mandate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMandate() throws Exception {
        int databaseSizeBeforeUpdate = mandateRepository.findAll().size();
        mandate.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMandateMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(mandate)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Mandate in the database
        List<Mandate> mandateList = mandateRepository.findAll();
        assertThat(mandateList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Mandate in Elasticsearch
        verify(mockMandateSearchRepository, times(0)).save(mandate);
    }

    @Test
    @Transactional
    void deleteMandate() throws Exception {
        // Initialize the database
        mandateRepository.saveAndFlush(mandate);

        int databaseSizeBeforeDelete = mandateRepository.findAll().size();

        // Delete the mandate
        restMandateMockMvc
            .perform(delete(ENTITY_API_URL_ID, mandate.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Mandate> mandateList = mandateRepository.findAll();
        assertThat(mandateList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Mandate in Elasticsearch
        verify(mockMandateSearchRepository, times(1)).deleteById(mandate.getId());
    }

    @Test
    @Transactional
    void searchMandate() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        mandateRepository.saveAndFlush(mandate);
        when(mockMandateSearchRepository.search(queryStringQuery("id:" + mandate.getId()))).thenReturn(Collections.singletonList(mandate));

        // Search the mandate
        restMandateMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + mandate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mandate.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].isAVSPaiment").value(hasItem(DEFAULT_IS_AVS_PAIMENT.booleanValue())))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())));
    }
}
