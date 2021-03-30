package com.almaviva.vs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.almaviva.vs.IntegrationTest;
import com.almaviva.vs.domain.AVService;
import com.almaviva.vs.repository.AVServiceRepository;
import com.almaviva.vs.repository.search.AVServiceSearchRepository;
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
 * Integration tests for the {@link AVServiceResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AVServiceResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final Boolean DEFAULT_IS_PRINCIPAL = false;
    private static final Boolean UPDATED_IS_PRINCIPAL = true;

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;

    private static final String ENTITY_API_URL = "/api/av-services";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/av-services";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AVServiceRepository aVServiceRepository;

    /**
     * This repository is mocked in the com.almaviva.vs.repository.search test package.
     *
     * @see com.almaviva.vs.repository.search.AVServiceSearchRepositoryMockConfiguration
     */
    @Autowired
    private AVServiceSearchRepository mockAVServiceSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAVServiceMockMvc;

    private AVService aVService;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AVService createEntity(EntityManager em) {
        AVService aVService = new AVService()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .price(DEFAULT_PRICE)
            .quantity(DEFAULT_QUANTITY)
            .isPrincipal(DEFAULT_IS_PRINCIPAL)
            .deleted(DEFAULT_DELETED);
        return aVService;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AVService createUpdatedEntity(EntityManager em) {
        AVService aVService = new AVService()
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .price(UPDATED_PRICE)
            .quantity(UPDATED_QUANTITY)
            .isPrincipal(UPDATED_IS_PRINCIPAL)
            .deleted(UPDATED_DELETED);
        return aVService;
    }

    @BeforeEach
    public void initTest() {
        aVService = createEntity(em);
    }

    @Test
    @Transactional
    void createAVService() throws Exception {
        int databaseSizeBeforeCreate = aVServiceRepository.findAll().size();
        // Create the AVService
        restAVServiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aVService)))
            .andExpect(status().isCreated());

        // Validate the AVService in the database
        List<AVService> aVServiceList = aVServiceRepository.findAll();
        assertThat(aVServiceList).hasSize(databaseSizeBeforeCreate + 1);
        AVService testAVService = aVServiceList.get(aVServiceList.size() - 1);
        assertThat(testAVService.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testAVService.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAVService.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testAVService.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testAVService.getIsPrincipal()).isEqualTo(DEFAULT_IS_PRINCIPAL);
        assertThat(testAVService.getDeleted()).isEqualTo(DEFAULT_DELETED);

        // Validate the AVService in Elasticsearch
        verify(mockAVServiceSearchRepository, times(1)).save(testAVService);
    }

    @Test
    @Transactional
    void createAVServiceWithExistingId() throws Exception {
        // Create the AVService with an existing ID
        aVService.setId(1L);

        int databaseSizeBeforeCreate = aVServiceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAVServiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aVService)))
            .andExpect(status().isBadRequest());

        // Validate the AVService in the database
        List<AVService> aVServiceList = aVServiceRepository.findAll();
        assertThat(aVServiceList).hasSize(databaseSizeBeforeCreate);

        // Validate the AVService in Elasticsearch
        verify(mockAVServiceSearchRepository, times(0)).save(aVService);
    }

    @Test
    @Transactional
    void getAllAVServices() throws Exception {
        // Initialize the database
        aVServiceRepository.saveAndFlush(aVService);

        // Get all the aVServiceList
        restAVServiceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aVService.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].isPrincipal").value(hasItem(DEFAULT_IS_PRINCIPAL.booleanValue())))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())));
    }

    @Test
    @Transactional
    void getAVService() throws Exception {
        // Initialize the database
        aVServiceRepository.saveAndFlush(aVService);

        // Get the aVService
        restAVServiceMockMvc
            .perform(get(ENTITY_API_URL_ID, aVService.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(aVService.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.isPrincipal").value(DEFAULT_IS_PRINCIPAL.booleanValue()))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingAVService() throws Exception {
        // Get the aVService
        restAVServiceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAVService() throws Exception {
        // Initialize the database
        aVServiceRepository.saveAndFlush(aVService);

        int databaseSizeBeforeUpdate = aVServiceRepository.findAll().size();

        // Update the aVService
        AVService updatedAVService = aVServiceRepository.findById(aVService.getId()).get();
        // Disconnect from session so that the updates on updatedAVService are not directly saved in db
        em.detach(updatedAVService);
        updatedAVService
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .price(UPDATED_PRICE)
            .quantity(UPDATED_QUANTITY)
            .isPrincipal(UPDATED_IS_PRINCIPAL)
            .deleted(UPDATED_DELETED);

        restAVServiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAVService.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAVService))
            )
            .andExpect(status().isOk());

        // Validate the AVService in the database
        List<AVService> aVServiceList = aVServiceRepository.findAll();
        assertThat(aVServiceList).hasSize(databaseSizeBeforeUpdate);
        AVService testAVService = aVServiceList.get(aVServiceList.size() - 1);
        assertThat(testAVService.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testAVService.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAVService.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testAVService.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testAVService.getIsPrincipal()).isEqualTo(UPDATED_IS_PRINCIPAL);
        assertThat(testAVService.getDeleted()).isEqualTo(UPDATED_DELETED);

        // Validate the AVService in Elasticsearch
        verify(mockAVServiceSearchRepository).save(testAVService);
    }

    @Test
    @Transactional
    void putNonExistingAVService() throws Exception {
        int databaseSizeBeforeUpdate = aVServiceRepository.findAll().size();
        aVService.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAVServiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, aVService.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(aVService))
            )
            .andExpect(status().isBadRequest());

        // Validate the AVService in the database
        List<AVService> aVServiceList = aVServiceRepository.findAll();
        assertThat(aVServiceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AVService in Elasticsearch
        verify(mockAVServiceSearchRepository, times(0)).save(aVService);
    }

    @Test
    @Transactional
    void putWithIdMismatchAVService() throws Exception {
        int databaseSizeBeforeUpdate = aVServiceRepository.findAll().size();
        aVService.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAVServiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(aVService))
            )
            .andExpect(status().isBadRequest());

        // Validate the AVService in the database
        List<AVService> aVServiceList = aVServiceRepository.findAll();
        assertThat(aVServiceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AVService in Elasticsearch
        verify(mockAVServiceSearchRepository, times(0)).save(aVService);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAVService() throws Exception {
        int databaseSizeBeforeUpdate = aVServiceRepository.findAll().size();
        aVService.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAVServiceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aVService)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AVService in the database
        List<AVService> aVServiceList = aVServiceRepository.findAll();
        assertThat(aVServiceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AVService in Elasticsearch
        verify(mockAVServiceSearchRepository, times(0)).save(aVService);
    }

    @Test
    @Transactional
    void partialUpdateAVServiceWithPatch() throws Exception {
        // Initialize the database
        aVServiceRepository.saveAndFlush(aVService);

        int databaseSizeBeforeUpdate = aVServiceRepository.findAll().size();

        // Update the aVService using partial update
        AVService partialUpdatedAVService = new AVService();
        partialUpdatedAVService.setId(aVService.getId());

        partialUpdatedAVService
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .quantity(UPDATED_QUANTITY)
            .isPrincipal(UPDATED_IS_PRINCIPAL);

        restAVServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAVService.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAVService))
            )
            .andExpect(status().isOk());

        // Validate the AVService in the database
        List<AVService> aVServiceList = aVServiceRepository.findAll();
        assertThat(aVServiceList).hasSize(databaseSizeBeforeUpdate);
        AVService testAVService = aVServiceList.get(aVServiceList.size() - 1);
        assertThat(testAVService.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testAVService.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAVService.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testAVService.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testAVService.getIsPrincipal()).isEqualTo(UPDATED_IS_PRINCIPAL);
        assertThat(testAVService.getDeleted()).isEqualTo(DEFAULT_DELETED);
    }

    @Test
    @Transactional
    void fullUpdateAVServiceWithPatch() throws Exception {
        // Initialize the database
        aVServiceRepository.saveAndFlush(aVService);

        int databaseSizeBeforeUpdate = aVServiceRepository.findAll().size();

        // Update the aVService using partial update
        AVService partialUpdatedAVService = new AVService();
        partialUpdatedAVService.setId(aVService.getId());

        partialUpdatedAVService
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .price(UPDATED_PRICE)
            .quantity(UPDATED_QUANTITY)
            .isPrincipal(UPDATED_IS_PRINCIPAL)
            .deleted(UPDATED_DELETED);

        restAVServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAVService.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAVService))
            )
            .andExpect(status().isOk());

        // Validate the AVService in the database
        List<AVService> aVServiceList = aVServiceRepository.findAll();
        assertThat(aVServiceList).hasSize(databaseSizeBeforeUpdate);
        AVService testAVService = aVServiceList.get(aVServiceList.size() - 1);
        assertThat(testAVService.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testAVService.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAVService.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testAVService.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testAVService.getIsPrincipal()).isEqualTo(UPDATED_IS_PRINCIPAL);
        assertThat(testAVService.getDeleted()).isEqualTo(UPDATED_DELETED);
    }

    @Test
    @Transactional
    void patchNonExistingAVService() throws Exception {
        int databaseSizeBeforeUpdate = aVServiceRepository.findAll().size();
        aVService.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAVServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, aVService.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(aVService))
            )
            .andExpect(status().isBadRequest());

        // Validate the AVService in the database
        List<AVService> aVServiceList = aVServiceRepository.findAll();
        assertThat(aVServiceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AVService in Elasticsearch
        verify(mockAVServiceSearchRepository, times(0)).save(aVService);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAVService() throws Exception {
        int databaseSizeBeforeUpdate = aVServiceRepository.findAll().size();
        aVService.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAVServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(aVService))
            )
            .andExpect(status().isBadRequest());

        // Validate the AVService in the database
        List<AVService> aVServiceList = aVServiceRepository.findAll();
        assertThat(aVServiceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AVService in Elasticsearch
        verify(mockAVServiceSearchRepository, times(0)).save(aVService);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAVService() throws Exception {
        int databaseSizeBeforeUpdate = aVServiceRepository.findAll().size();
        aVService.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAVServiceMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(aVService))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AVService in the database
        List<AVService> aVServiceList = aVServiceRepository.findAll();
        assertThat(aVServiceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AVService in Elasticsearch
        verify(mockAVServiceSearchRepository, times(0)).save(aVService);
    }

    @Test
    @Transactional
    void deleteAVService() throws Exception {
        // Initialize the database
        aVServiceRepository.saveAndFlush(aVService);

        int databaseSizeBeforeDelete = aVServiceRepository.findAll().size();

        // Delete the aVService
        restAVServiceMockMvc
            .perform(delete(ENTITY_API_URL_ID, aVService.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AVService> aVServiceList = aVServiceRepository.findAll();
        assertThat(aVServiceList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the AVService in Elasticsearch
        verify(mockAVServiceSearchRepository, times(1)).deleteById(aVService.getId());
    }

    @Test
    @Transactional
    void searchAVService() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        aVServiceRepository.saveAndFlush(aVService);
        when(mockAVServiceSearchRepository.search(queryStringQuery("id:" + aVService.getId())))
            .thenReturn(Collections.singletonList(aVService));

        // Search the aVService
        restAVServiceMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + aVService.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aVService.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].isPrincipal").value(hasItem(DEFAULT_IS_PRINCIPAL.booleanValue())))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())));
    }
}
