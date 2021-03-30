package com.almaviva.vs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.almaviva.vs.IntegrationTest;
import com.almaviva.vs.domain.PhoneActivation;
import com.almaviva.vs.repository.PhoneActivationRepository;
import com.almaviva.vs.repository.search.PhoneActivationSearchRepository;
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
 * Integration tests for the {@link PhoneActivationResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PhoneActivationResourceIT {

    private static final Boolean DEFAULT_IS_ACTIVATED = false;
    private static final Boolean UPDATED_IS_ACTIVATED = true;

    private static final String DEFAULT_ACTIVATION_KEY = "AAAAAAAAAA";
    private static final String UPDATED_ACTIVATION_KEY = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_EXPIRATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EXPIRATION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;

    private static final String ENTITY_API_URL = "/api/phone-activations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/phone-activations";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PhoneActivationRepository phoneActivationRepository;

    /**
     * This repository is mocked in the com.almaviva.vs.repository.search test package.
     *
     * @see com.almaviva.vs.repository.search.PhoneActivationSearchRepositoryMockConfiguration
     */
    @Autowired
    private PhoneActivationSearchRepository mockPhoneActivationSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPhoneActivationMockMvc;

    private PhoneActivation phoneActivation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PhoneActivation createEntity(EntityManager em) {
        PhoneActivation phoneActivation = new PhoneActivation()
            .isActivated(DEFAULT_IS_ACTIVATED)
            .activationKey(DEFAULT_ACTIVATION_KEY)
            .expirationDate(DEFAULT_EXPIRATION_DATE)
            .deleted(DEFAULT_DELETED);
        return phoneActivation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PhoneActivation createUpdatedEntity(EntityManager em) {
        PhoneActivation phoneActivation = new PhoneActivation()
            .isActivated(UPDATED_IS_ACTIVATED)
            .activationKey(UPDATED_ACTIVATION_KEY)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .deleted(UPDATED_DELETED);
        return phoneActivation;
    }

    @BeforeEach
    public void initTest() {
        phoneActivation = createEntity(em);
    }

    @Test
    @Transactional
    void createPhoneActivation() throws Exception {
        int databaseSizeBeforeCreate = phoneActivationRepository.findAll().size();
        // Create the PhoneActivation
        restPhoneActivationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(phoneActivation))
            )
            .andExpect(status().isCreated());

        // Validate the PhoneActivation in the database
        List<PhoneActivation> phoneActivationList = phoneActivationRepository.findAll();
        assertThat(phoneActivationList).hasSize(databaseSizeBeforeCreate + 1);
        PhoneActivation testPhoneActivation = phoneActivationList.get(phoneActivationList.size() - 1);
        assertThat(testPhoneActivation.getIsActivated()).isEqualTo(DEFAULT_IS_ACTIVATED);
        assertThat(testPhoneActivation.getActivationKey()).isEqualTo(DEFAULT_ACTIVATION_KEY);
        assertThat(testPhoneActivation.getExpirationDate()).isEqualTo(DEFAULT_EXPIRATION_DATE);
        assertThat(testPhoneActivation.getDeleted()).isEqualTo(DEFAULT_DELETED);

        // Validate the PhoneActivation in Elasticsearch
        verify(mockPhoneActivationSearchRepository, times(1)).save(testPhoneActivation);
    }

    @Test
    @Transactional
    void createPhoneActivationWithExistingId() throws Exception {
        // Create the PhoneActivation with an existing ID
        phoneActivation.setId(1L);

        int databaseSizeBeforeCreate = phoneActivationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPhoneActivationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(phoneActivation))
            )
            .andExpect(status().isBadRequest());

        // Validate the PhoneActivation in the database
        List<PhoneActivation> phoneActivationList = phoneActivationRepository.findAll();
        assertThat(phoneActivationList).hasSize(databaseSizeBeforeCreate);

        // Validate the PhoneActivation in Elasticsearch
        verify(mockPhoneActivationSearchRepository, times(0)).save(phoneActivation);
    }

    @Test
    @Transactional
    void getAllPhoneActivations() throws Exception {
        // Initialize the database
        phoneActivationRepository.saveAndFlush(phoneActivation);

        // Get all the phoneActivationList
        restPhoneActivationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(phoneActivation.getId().intValue())))
            .andExpect(jsonPath("$.[*].isActivated").value(hasItem(DEFAULT_IS_ACTIVATED.booleanValue())))
            .andExpect(jsonPath("$.[*].activationKey").value(hasItem(DEFAULT_ACTIVATION_KEY)))
            .andExpect(jsonPath("$.[*].expirationDate").value(hasItem(DEFAULT_EXPIRATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())));
    }

    @Test
    @Transactional
    void getPhoneActivation() throws Exception {
        // Initialize the database
        phoneActivationRepository.saveAndFlush(phoneActivation);

        // Get the phoneActivation
        restPhoneActivationMockMvc
            .perform(get(ENTITY_API_URL_ID, phoneActivation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(phoneActivation.getId().intValue()))
            .andExpect(jsonPath("$.isActivated").value(DEFAULT_IS_ACTIVATED.booleanValue()))
            .andExpect(jsonPath("$.activationKey").value(DEFAULT_ACTIVATION_KEY))
            .andExpect(jsonPath("$.expirationDate").value(DEFAULT_EXPIRATION_DATE.toString()))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingPhoneActivation() throws Exception {
        // Get the phoneActivation
        restPhoneActivationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPhoneActivation() throws Exception {
        // Initialize the database
        phoneActivationRepository.saveAndFlush(phoneActivation);

        int databaseSizeBeforeUpdate = phoneActivationRepository.findAll().size();

        // Update the phoneActivation
        PhoneActivation updatedPhoneActivation = phoneActivationRepository.findById(phoneActivation.getId()).get();
        // Disconnect from session so that the updates on updatedPhoneActivation are not directly saved in db
        em.detach(updatedPhoneActivation);
        updatedPhoneActivation
            .isActivated(UPDATED_IS_ACTIVATED)
            .activationKey(UPDATED_ACTIVATION_KEY)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .deleted(UPDATED_DELETED);

        restPhoneActivationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPhoneActivation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPhoneActivation))
            )
            .andExpect(status().isOk());

        // Validate the PhoneActivation in the database
        List<PhoneActivation> phoneActivationList = phoneActivationRepository.findAll();
        assertThat(phoneActivationList).hasSize(databaseSizeBeforeUpdate);
        PhoneActivation testPhoneActivation = phoneActivationList.get(phoneActivationList.size() - 1);
        assertThat(testPhoneActivation.getIsActivated()).isEqualTo(UPDATED_IS_ACTIVATED);
        assertThat(testPhoneActivation.getActivationKey()).isEqualTo(UPDATED_ACTIVATION_KEY);
        assertThat(testPhoneActivation.getExpirationDate()).isEqualTo(UPDATED_EXPIRATION_DATE);
        assertThat(testPhoneActivation.getDeleted()).isEqualTo(UPDATED_DELETED);

        // Validate the PhoneActivation in Elasticsearch
        verify(mockPhoneActivationSearchRepository).save(testPhoneActivation);
    }

    @Test
    @Transactional
    void putNonExistingPhoneActivation() throws Exception {
        int databaseSizeBeforeUpdate = phoneActivationRepository.findAll().size();
        phoneActivation.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPhoneActivationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, phoneActivation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(phoneActivation))
            )
            .andExpect(status().isBadRequest());

        // Validate the PhoneActivation in the database
        List<PhoneActivation> phoneActivationList = phoneActivationRepository.findAll();
        assertThat(phoneActivationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PhoneActivation in Elasticsearch
        verify(mockPhoneActivationSearchRepository, times(0)).save(phoneActivation);
    }

    @Test
    @Transactional
    void putWithIdMismatchPhoneActivation() throws Exception {
        int databaseSizeBeforeUpdate = phoneActivationRepository.findAll().size();
        phoneActivation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhoneActivationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(phoneActivation))
            )
            .andExpect(status().isBadRequest());

        // Validate the PhoneActivation in the database
        List<PhoneActivation> phoneActivationList = phoneActivationRepository.findAll();
        assertThat(phoneActivationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PhoneActivation in Elasticsearch
        verify(mockPhoneActivationSearchRepository, times(0)).save(phoneActivation);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPhoneActivation() throws Exception {
        int databaseSizeBeforeUpdate = phoneActivationRepository.findAll().size();
        phoneActivation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhoneActivationMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(phoneActivation))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PhoneActivation in the database
        List<PhoneActivation> phoneActivationList = phoneActivationRepository.findAll();
        assertThat(phoneActivationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PhoneActivation in Elasticsearch
        verify(mockPhoneActivationSearchRepository, times(0)).save(phoneActivation);
    }

    @Test
    @Transactional
    void partialUpdatePhoneActivationWithPatch() throws Exception {
        // Initialize the database
        phoneActivationRepository.saveAndFlush(phoneActivation);

        int databaseSizeBeforeUpdate = phoneActivationRepository.findAll().size();

        // Update the phoneActivation using partial update
        PhoneActivation partialUpdatedPhoneActivation = new PhoneActivation();
        partialUpdatedPhoneActivation.setId(phoneActivation.getId());

        partialUpdatedPhoneActivation.isActivated(UPDATED_IS_ACTIVATED).expirationDate(UPDATED_EXPIRATION_DATE);

        restPhoneActivationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPhoneActivation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPhoneActivation))
            )
            .andExpect(status().isOk());

        // Validate the PhoneActivation in the database
        List<PhoneActivation> phoneActivationList = phoneActivationRepository.findAll();
        assertThat(phoneActivationList).hasSize(databaseSizeBeforeUpdate);
        PhoneActivation testPhoneActivation = phoneActivationList.get(phoneActivationList.size() - 1);
        assertThat(testPhoneActivation.getIsActivated()).isEqualTo(UPDATED_IS_ACTIVATED);
        assertThat(testPhoneActivation.getActivationKey()).isEqualTo(DEFAULT_ACTIVATION_KEY);
        assertThat(testPhoneActivation.getExpirationDate()).isEqualTo(UPDATED_EXPIRATION_DATE);
        assertThat(testPhoneActivation.getDeleted()).isEqualTo(DEFAULT_DELETED);
    }

    @Test
    @Transactional
    void fullUpdatePhoneActivationWithPatch() throws Exception {
        // Initialize the database
        phoneActivationRepository.saveAndFlush(phoneActivation);

        int databaseSizeBeforeUpdate = phoneActivationRepository.findAll().size();

        // Update the phoneActivation using partial update
        PhoneActivation partialUpdatedPhoneActivation = new PhoneActivation();
        partialUpdatedPhoneActivation.setId(phoneActivation.getId());

        partialUpdatedPhoneActivation
            .isActivated(UPDATED_IS_ACTIVATED)
            .activationKey(UPDATED_ACTIVATION_KEY)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .deleted(UPDATED_DELETED);

        restPhoneActivationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPhoneActivation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPhoneActivation))
            )
            .andExpect(status().isOk());

        // Validate the PhoneActivation in the database
        List<PhoneActivation> phoneActivationList = phoneActivationRepository.findAll();
        assertThat(phoneActivationList).hasSize(databaseSizeBeforeUpdate);
        PhoneActivation testPhoneActivation = phoneActivationList.get(phoneActivationList.size() - 1);
        assertThat(testPhoneActivation.getIsActivated()).isEqualTo(UPDATED_IS_ACTIVATED);
        assertThat(testPhoneActivation.getActivationKey()).isEqualTo(UPDATED_ACTIVATION_KEY);
        assertThat(testPhoneActivation.getExpirationDate()).isEqualTo(UPDATED_EXPIRATION_DATE);
        assertThat(testPhoneActivation.getDeleted()).isEqualTo(UPDATED_DELETED);
    }

    @Test
    @Transactional
    void patchNonExistingPhoneActivation() throws Exception {
        int databaseSizeBeforeUpdate = phoneActivationRepository.findAll().size();
        phoneActivation.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPhoneActivationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, phoneActivation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(phoneActivation))
            )
            .andExpect(status().isBadRequest());

        // Validate the PhoneActivation in the database
        List<PhoneActivation> phoneActivationList = phoneActivationRepository.findAll();
        assertThat(phoneActivationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PhoneActivation in Elasticsearch
        verify(mockPhoneActivationSearchRepository, times(0)).save(phoneActivation);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPhoneActivation() throws Exception {
        int databaseSizeBeforeUpdate = phoneActivationRepository.findAll().size();
        phoneActivation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhoneActivationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(phoneActivation))
            )
            .andExpect(status().isBadRequest());

        // Validate the PhoneActivation in the database
        List<PhoneActivation> phoneActivationList = phoneActivationRepository.findAll();
        assertThat(phoneActivationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PhoneActivation in Elasticsearch
        verify(mockPhoneActivationSearchRepository, times(0)).save(phoneActivation);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPhoneActivation() throws Exception {
        int databaseSizeBeforeUpdate = phoneActivationRepository.findAll().size();
        phoneActivation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhoneActivationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(phoneActivation))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PhoneActivation in the database
        List<PhoneActivation> phoneActivationList = phoneActivationRepository.findAll();
        assertThat(phoneActivationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PhoneActivation in Elasticsearch
        verify(mockPhoneActivationSearchRepository, times(0)).save(phoneActivation);
    }

    @Test
    @Transactional
    void deletePhoneActivation() throws Exception {
        // Initialize the database
        phoneActivationRepository.saveAndFlush(phoneActivation);

        int databaseSizeBeforeDelete = phoneActivationRepository.findAll().size();

        // Delete the phoneActivation
        restPhoneActivationMockMvc
            .perform(delete(ENTITY_API_URL_ID, phoneActivation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PhoneActivation> phoneActivationList = phoneActivationRepository.findAll();
        assertThat(phoneActivationList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the PhoneActivation in Elasticsearch
        verify(mockPhoneActivationSearchRepository, times(1)).deleteById(phoneActivation.getId());
    }

    @Test
    @Transactional
    void searchPhoneActivation() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        phoneActivationRepository.saveAndFlush(phoneActivation);
        when(mockPhoneActivationSearchRepository.search(queryStringQuery("id:" + phoneActivation.getId())))
            .thenReturn(Collections.singletonList(phoneActivation));

        // Search the phoneActivation
        restPhoneActivationMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + phoneActivation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(phoneActivation.getId().intValue())))
            .andExpect(jsonPath("$.[*].isActivated").value(hasItem(DEFAULT_IS_ACTIVATED.booleanValue())))
            .andExpect(jsonPath("$.[*].activationKey").value(hasItem(DEFAULT_ACTIVATION_KEY)))
            .andExpect(jsonPath("$.[*].expirationDate").value(hasItem(DEFAULT_EXPIRATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())));
    }
}
