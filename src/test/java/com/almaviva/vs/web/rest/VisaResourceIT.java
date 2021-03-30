package com.almaviva.vs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.almaviva.vs.IntegrationTest;
import com.almaviva.vs.domain.Visa;
import com.almaviva.vs.repository.VisaRepository;
import com.almaviva.vs.repository.search.VisaSearchRepository;
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
 * Integration tests for the {@link VisaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class VisaResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;

    private static final String ENTITY_API_URL = "/api/visas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/visas";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VisaRepository visaRepository;

    /**
     * This repository is mocked in the com.almaviva.vs.repository.search test package.
     *
     * @see com.almaviva.vs.repository.search.VisaSearchRepositoryMockConfiguration
     */
    @Autowired
    private VisaSearchRepository mockVisaSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVisaMockMvc;

    private Visa visa;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Visa createEntity(EntityManager em) {
        Visa visa = new Visa().title(DEFAULT_TITLE).price(DEFAULT_PRICE).description(DEFAULT_DESCRIPTION).deleted(DEFAULT_DELETED);
        return visa;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Visa createUpdatedEntity(EntityManager em) {
        Visa visa = new Visa().title(UPDATED_TITLE).price(UPDATED_PRICE).description(UPDATED_DESCRIPTION).deleted(UPDATED_DELETED);
        return visa;
    }

    @BeforeEach
    public void initTest() {
        visa = createEntity(em);
    }

    @Test
    @Transactional
    void createVisa() throws Exception {
        int databaseSizeBeforeCreate = visaRepository.findAll().size();
        // Create the Visa
        restVisaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(visa)))
            .andExpect(status().isCreated());

        // Validate the Visa in the database
        List<Visa> visaList = visaRepository.findAll();
        assertThat(visaList).hasSize(databaseSizeBeforeCreate + 1);
        Visa testVisa = visaList.get(visaList.size() - 1);
        assertThat(testVisa.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testVisa.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testVisa.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testVisa.getDeleted()).isEqualTo(DEFAULT_DELETED);

        // Validate the Visa in Elasticsearch
        verify(mockVisaSearchRepository, times(1)).save(testVisa);
    }

    @Test
    @Transactional
    void createVisaWithExistingId() throws Exception {
        // Create the Visa with an existing ID
        visa.setId(1L);

        int databaseSizeBeforeCreate = visaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVisaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(visa)))
            .andExpect(status().isBadRequest());

        // Validate the Visa in the database
        List<Visa> visaList = visaRepository.findAll();
        assertThat(visaList).hasSize(databaseSizeBeforeCreate);

        // Validate the Visa in Elasticsearch
        verify(mockVisaSearchRepository, times(0)).save(visa);
    }

    @Test
    @Transactional
    void getAllVisas() throws Exception {
        // Initialize the database
        visaRepository.saveAndFlush(visa);

        // Get all the visaList
        restVisaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(visa.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())));
    }

    @Test
    @Transactional
    void getVisa() throws Exception {
        // Initialize the database
        visaRepository.saveAndFlush(visa);

        // Get the visa
        restVisaMockMvc
            .perform(get(ENTITY_API_URL_ID, visa.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(visa.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingVisa() throws Exception {
        // Get the visa
        restVisaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewVisa() throws Exception {
        // Initialize the database
        visaRepository.saveAndFlush(visa);

        int databaseSizeBeforeUpdate = visaRepository.findAll().size();

        // Update the visa
        Visa updatedVisa = visaRepository.findById(visa.getId()).get();
        // Disconnect from session so that the updates on updatedVisa are not directly saved in db
        em.detach(updatedVisa);
        updatedVisa.title(UPDATED_TITLE).price(UPDATED_PRICE).description(UPDATED_DESCRIPTION).deleted(UPDATED_DELETED);

        restVisaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVisa.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedVisa))
            )
            .andExpect(status().isOk());

        // Validate the Visa in the database
        List<Visa> visaList = visaRepository.findAll();
        assertThat(visaList).hasSize(databaseSizeBeforeUpdate);
        Visa testVisa = visaList.get(visaList.size() - 1);
        assertThat(testVisa.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testVisa.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testVisa.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testVisa.getDeleted()).isEqualTo(UPDATED_DELETED);

        // Validate the Visa in Elasticsearch
        verify(mockVisaSearchRepository).save(testVisa);
    }

    @Test
    @Transactional
    void putNonExistingVisa() throws Exception {
        int databaseSizeBeforeUpdate = visaRepository.findAll().size();
        visa.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVisaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, visa.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(visa))
            )
            .andExpect(status().isBadRequest());

        // Validate the Visa in the database
        List<Visa> visaList = visaRepository.findAll();
        assertThat(visaList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Visa in Elasticsearch
        verify(mockVisaSearchRepository, times(0)).save(visa);
    }

    @Test
    @Transactional
    void putWithIdMismatchVisa() throws Exception {
        int databaseSizeBeforeUpdate = visaRepository.findAll().size();
        visa.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVisaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(visa))
            )
            .andExpect(status().isBadRequest());

        // Validate the Visa in the database
        List<Visa> visaList = visaRepository.findAll();
        assertThat(visaList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Visa in Elasticsearch
        verify(mockVisaSearchRepository, times(0)).save(visa);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVisa() throws Exception {
        int databaseSizeBeforeUpdate = visaRepository.findAll().size();
        visa.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVisaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(visa)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Visa in the database
        List<Visa> visaList = visaRepository.findAll();
        assertThat(visaList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Visa in Elasticsearch
        verify(mockVisaSearchRepository, times(0)).save(visa);
    }

    @Test
    @Transactional
    void partialUpdateVisaWithPatch() throws Exception {
        // Initialize the database
        visaRepository.saveAndFlush(visa);

        int databaseSizeBeforeUpdate = visaRepository.findAll().size();

        // Update the visa using partial update
        Visa partialUpdatedVisa = new Visa();
        partialUpdatedVisa.setId(visa.getId());

        partialUpdatedVisa.title(UPDATED_TITLE).price(UPDATED_PRICE).description(UPDATED_DESCRIPTION).deleted(UPDATED_DELETED);

        restVisaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVisa.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVisa))
            )
            .andExpect(status().isOk());

        // Validate the Visa in the database
        List<Visa> visaList = visaRepository.findAll();
        assertThat(visaList).hasSize(databaseSizeBeforeUpdate);
        Visa testVisa = visaList.get(visaList.size() - 1);
        assertThat(testVisa.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testVisa.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testVisa.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testVisa.getDeleted()).isEqualTo(UPDATED_DELETED);
    }

    @Test
    @Transactional
    void fullUpdateVisaWithPatch() throws Exception {
        // Initialize the database
        visaRepository.saveAndFlush(visa);

        int databaseSizeBeforeUpdate = visaRepository.findAll().size();

        // Update the visa using partial update
        Visa partialUpdatedVisa = new Visa();
        partialUpdatedVisa.setId(visa.getId());

        partialUpdatedVisa.title(UPDATED_TITLE).price(UPDATED_PRICE).description(UPDATED_DESCRIPTION).deleted(UPDATED_DELETED);

        restVisaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVisa.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVisa))
            )
            .andExpect(status().isOk());

        // Validate the Visa in the database
        List<Visa> visaList = visaRepository.findAll();
        assertThat(visaList).hasSize(databaseSizeBeforeUpdate);
        Visa testVisa = visaList.get(visaList.size() - 1);
        assertThat(testVisa.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testVisa.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testVisa.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testVisa.getDeleted()).isEqualTo(UPDATED_DELETED);
    }

    @Test
    @Transactional
    void patchNonExistingVisa() throws Exception {
        int databaseSizeBeforeUpdate = visaRepository.findAll().size();
        visa.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVisaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, visa.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(visa))
            )
            .andExpect(status().isBadRequest());

        // Validate the Visa in the database
        List<Visa> visaList = visaRepository.findAll();
        assertThat(visaList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Visa in Elasticsearch
        verify(mockVisaSearchRepository, times(0)).save(visa);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVisa() throws Exception {
        int databaseSizeBeforeUpdate = visaRepository.findAll().size();
        visa.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVisaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(visa))
            )
            .andExpect(status().isBadRequest());

        // Validate the Visa in the database
        List<Visa> visaList = visaRepository.findAll();
        assertThat(visaList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Visa in Elasticsearch
        verify(mockVisaSearchRepository, times(0)).save(visa);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVisa() throws Exception {
        int databaseSizeBeforeUpdate = visaRepository.findAll().size();
        visa.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVisaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(visa)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Visa in the database
        List<Visa> visaList = visaRepository.findAll();
        assertThat(visaList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Visa in Elasticsearch
        verify(mockVisaSearchRepository, times(0)).save(visa);
    }

    @Test
    @Transactional
    void deleteVisa() throws Exception {
        // Initialize the database
        visaRepository.saveAndFlush(visa);

        int databaseSizeBeforeDelete = visaRepository.findAll().size();

        // Delete the visa
        restVisaMockMvc
            .perform(delete(ENTITY_API_URL_ID, visa.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Visa> visaList = visaRepository.findAll();
        assertThat(visaList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Visa in Elasticsearch
        verify(mockVisaSearchRepository, times(1)).deleteById(visa.getId());
    }

    @Test
    @Transactional
    void searchVisa() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        visaRepository.saveAndFlush(visa);
        when(mockVisaSearchRepository.search(queryStringQuery("id:" + visa.getId()))).thenReturn(Collections.singletonList(visa));

        // Search the visa
        restVisaMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + visa.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(visa.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())));
    }
}
