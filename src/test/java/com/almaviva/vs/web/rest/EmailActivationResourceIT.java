package com.almaviva.vs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.almaviva.vs.IntegrationTest;
import com.almaviva.vs.domain.EmailActivation;
import com.almaviva.vs.repository.EmailActivationRepository;
import com.almaviva.vs.repository.search.EmailActivationSearchRepository;
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
 * Integration tests for the {@link EmailActivationResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class EmailActivationResourceIT {

    private static final Boolean DEFAULT_IS_ACTIVATED = false;
    private static final Boolean UPDATED_IS_ACTIVATED = true;

    private static final String DEFAULT_ACTIVATION_KEY = "AAAAAAAAAA";
    private static final String UPDATED_ACTIVATION_KEY = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_EXPIRATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EXPIRATION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;

    private static final String ENTITY_API_URL = "/api/email-activations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/email-activations";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EmailActivationRepository emailActivationRepository;

    /**
     * This repository is mocked in the com.almaviva.vs.repository.search test package.
     *
     * @see com.almaviva.vs.repository.search.EmailActivationSearchRepositoryMockConfiguration
     */
    @Autowired
    private EmailActivationSearchRepository mockEmailActivationSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmailActivationMockMvc;

    private EmailActivation emailActivation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmailActivation createEntity(EntityManager em) {
        EmailActivation emailActivation = new EmailActivation()
            .isActivated(DEFAULT_IS_ACTIVATED)
            .activationKey(DEFAULT_ACTIVATION_KEY)
            .expirationDate(DEFAULT_EXPIRATION_DATE)
            .deleted(DEFAULT_DELETED);
        return emailActivation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmailActivation createUpdatedEntity(EntityManager em) {
        EmailActivation emailActivation = new EmailActivation()
            .isActivated(UPDATED_IS_ACTIVATED)
            .activationKey(UPDATED_ACTIVATION_KEY)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .deleted(UPDATED_DELETED);
        return emailActivation;
    }

    @BeforeEach
    public void initTest() {
        emailActivation = createEntity(em);
    }

    @Test
    @Transactional
    void createEmailActivation() throws Exception {
        int databaseSizeBeforeCreate = emailActivationRepository.findAll().size();
        // Create the EmailActivation
        restEmailActivationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(emailActivation))
            )
            .andExpect(status().isCreated());

        // Validate the EmailActivation in the database
        List<EmailActivation> emailActivationList = emailActivationRepository.findAll();
        assertThat(emailActivationList).hasSize(databaseSizeBeforeCreate + 1);
        EmailActivation testEmailActivation = emailActivationList.get(emailActivationList.size() - 1);
        assertThat(testEmailActivation.getIsActivated()).isEqualTo(DEFAULT_IS_ACTIVATED);
        assertThat(testEmailActivation.getActivationKey()).isEqualTo(DEFAULT_ACTIVATION_KEY);
        assertThat(testEmailActivation.getExpirationDate()).isEqualTo(DEFAULT_EXPIRATION_DATE);
        assertThat(testEmailActivation.getDeleted()).isEqualTo(DEFAULT_DELETED);

        // Validate the EmailActivation in Elasticsearch
        verify(mockEmailActivationSearchRepository, times(1)).save(testEmailActivation);
    }

    @Test
    @Transactional
    void createEmailActivationWithExistingId() throws Exception {
        // Create the EmailActivation with an existing ID
        emailActivation.setId(1L);

        int databaseSizeBeforeCreate = emailActivationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmailActivationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(emailActivation))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmailActivation in the database
        List<EmailActivation> emailActivationList = emailActivationRepository.findAll();
        assertThat(emailActivationList).hasSize(databaseSizeBeforeCreate);

        // Validate the EmailActivation in Elasticsearch
        verify(mockEmailActivationSearchRepository, times(0)).save(emailActivation);
    }

    @Test
    @Transactional
    void getAllEmailActivations() throws Exception {
        // Initialize the database
        emailActivationRepository.saveAndFlush(emailActivation);

        // Get all the emailActivationList
        restEmailActivationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(emailActivation.getId().intValue())))
            .andExpect(jsonPath("$.[*].isActivated").value(hasItem(DEFAULT_IS_ACTIVATED.booleanValue())))
            .andExpect(jsonPath("$.[*].activationKey").value(hasItem(DEFAULT_ACTIVATION_KEY)))
            .andExpect(jsonPath("$.[*].expirationDate").value(hasItem(DEFAULT_EXPIRATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())));
    }

    @Test
    @Transactional
    void getEmailActivation() throws Exception {
        // Initialize the database
        emailActivationRepository.saveAndFlush(emailActivation);

        // Get the emailActivation
        restEmailActivationMockMvc
            .perform(get(ENTITY_API_URL_ID, emailActivation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(emailActivation.getId().intValue()))
            .andExpect(jsonPath("$.isActivated").value(DEFAULT_IS_ACTIVATED.booleanValue()))
            .andExpect(jsonPath("$.activationKey").value(DEFAULT_ACTIVATION_KEY))
            .andExpect(jsonPath("$.expirationDate").value(DEFAULT_EXPIRATION_DATE.toString()))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingEmailActivation() throws Exception {
        // Get the emailActivation
        restEmailActivationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEmailActivation() throws Exception {
        // Initialize the database
        emailActivationRepository.saveAndFlush(emailActivation);

        int databaseSizeBeforeUpdate = emailActivationRepository.findAll().size();

        // Update the emailActivation
        EmailActivation updatedEmailActivation = emailActivationRepository.findById(emailActivation.getId()).get();
        // Disconnect from session so that the updates on updatedEmailActivation are not directly saved in db
        em.detach(updatedEmailActivation);
        updatedEmailActivation
            .isActivated(UPDATED_IS_ACTIVATED)
            .activationKey(UPDATED_ACTIVATION_KEY)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .deleted(UPDATED_DELETED);

        restEmailActivationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEmailActivation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEmailActivation))
            )
            .andExpect(status().isOk());

        // Validate the EmailActivation in the database
        List<EmailActivation> emailActivationList = emailActivationRepository.findAll();
        assertThat(emailActivationList).hasSize(databaseSizeBeforeUpdate);
        EmailActivation testEmailActivation = emailActivationList.get(emailActivationList.size() - 1);
        assertThat(testEmailActivation.getIsActivated()).isEqualTo(UPDATED_IS_ACTIVATED);
        assertThat(testEmailActivation.getActivationKey()).isEqualTo(UPDATED_ACTIVATION_KEY);
        assertThat(testEmailActivation.getExpirationDate()).isEqualTo(UPDATED_EXPIRATION_DATE);
        assertThat(testEmailActivation.getDeleted()).isEqualTo(UPDATED_DELETED);

        // Validate the EmailActivation in Elasticsearch
        verify(mockEmailActivationSearchRepository).save(testEmailActivation);
    }

    @Test
    @Transactional
    void putNonExistingEmailActivation() throws Exception {
        int databaseSizeBeforeUpdate = emailActivationRepository.findAll().size();
        emailActivation.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmailActivationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, emailActivation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(emailActivation))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmailActivation in the database
        List<EmailActivation> emailActivationList = emailActivationRepository.findAll();
        assertThat(emailActivationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the EmailActivation in Elasticsearch
        verify(mockEmailActivationSearchRepository, times(0)).save(emailActivation);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmailActivation() throws Exception {
        int databaseSizeBeforeUpdate = emailActivationRepository.findAll().size();
        emailActivation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmailActivationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(emailActivation))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmailActivation in the database
        List<EmailActivation> emailActivationList = emailActivationRepository.findAll();
        assertThat(emailActivationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the EmailActivation in Elasticsearch
        verify(mockEmailActivationSearchRepository, times(0)).save(emailActivation);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmailActivation() throws Exception {
        int databaseSizeBeforeUpdate = emailActivationRepository.findAll().size();
        emailActivation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmailActivationMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(emailActivation))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmailActivation in the database
        List<EmailActivation> emailActivationList = emailActivationRepository.findAll();
        assertThat(emailActivationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the EmailActivation in Elasticsearch
        verify(mockEmailActivationSearchRepository, times(0)).save(emailActivation);
    }

    @Test
    @Transactional
    void partialUpdateEmailActivationWithPatch() throws Exception {
        // Initialize the database
        emailActivationRepository.saveAndFlush(emailActivation);

        int databaseSizeBeforeUpdate = emailActivationRepository.findAll().size();

        // Update the emailActivation using partial update
        EmailActivation partialUpdatedEmailActivation = new EmailActivation();
        partialUpdatedEmailActivation.setId(emailActivation.getId());

        partialUpdatedEmailActivation.expirationDate(UPDATED_EXPIRATION_DATE);

        restEmailActivationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmailActivation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmailActivation))
            )
            .andExpect(status().isOk());

        // Validate the EmailActivation in the database
        List<EmailActivation> emailActivationList = emailActivationRepository.findAll();
        assertThat(emailActivationList).hasSize(databaseSizeBeforeUpdate);
        EmailActivation testEmailActivation = emailActivationList.get(emailActivationList.size() - 1);
        assertThat(testEmailActivation.getIsActivated()).isEqualTo(DEFAULT_IS_ACTIVATED);
        assertThat(testEmailActivation.getActivationKey()).isEqualTo(DEFAULT_ACTIVATION_KEY);
        assertThat(testEmailActivation.getExpirationDate()).isEqualTo(UPDATED_EXPIRATION_DATE);
        assertThat(testEmailActivation.getDeleted()).isEqualTo(DEFAULT_DELETED);
    }

    @Test
    @Transactional
    void fullUpdateEmailActivationWithPatch() throws Exception {
        // Initialize the database
        emailActivationRepository.saveAndFlush(emailActivation);

        int databaseSizeBeforeUpdate = emailActivationRepository.findAll().size();

        // Update the emailActivation using partial update
        EmailActivation partialUpdatedEmailActivation = new EmailActivation();
        partialUpdatedEmailActivation.setId(emailActivation.getId());

        partialUpdatedEmailActivation
            .isActivated(UPDATED_IS_ACTIVATED)
            .activationKey(UPDATED_ACTIVATION_KEY)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .deleted(UPDATED_DELETED);

        restEmailActivationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmailActivation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmailActivation))
            )
            .andExpect(status().isOk());

        // Validate the EmailActivation in the database
        List<EmailActivation> emailActivationList = emailActivationRepository.findAll();
        assertThat(emailActivationList).hasSize(databaseSizeBeforeUpdate);
        EmailActivation testEmailActivation = emailActivationList.get(emailActivationList.size() - 1);
        assertThat(testEmailActivation.getIsActivated()).isEqualTo(UPDATED_IS_ACTIVATED);
        assertThat(testEmailActivation.getActivationKey()).isEqualTo(UPDATED_ACTIVATION_KEY);
        assertThat(testEmailActivation.getExpirationDate()).isEqualTo(UPDATED_EXPIRATION_DATE);
        assertThat(testEmailActivation.getDeleted()).isEqualTo(UPDATED_DELETED);
    }

    @Test
    @Transactional
    void patchNonExistingEmailActivation() throws Exception {
        int databaseSizeBeforeUpdate = emailActivationRepository.findAll().size();
        emailActivation.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmailActivationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, emailActivation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(emailActivation))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmailActivation in the database
        List<EmailActivation> emailActivationList = emailActivationRepository.findAll();
        assertThat(emailActivationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the EmailActivation in Elasticsearch
        verify(mockEmailActivationSearchRepository, times(0)).save(emailActivation);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmailActivation() throws Exception {
        int databaseSizeBeforeUpdate = emailActivationRepository.findAll().size();
        emailActivation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmailActivationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(emailActivation))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmailActivation in the database
        List<EmailActivation> emailActivationList = emailActivationRepository.findAll();
        assertThat(emailActivationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the EmailActivation in Elasticsearch
        verify(mockEmailActivationSearchRepository, times(0)).save(emailActivation);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmailActivation() throws Exception {
        int databaseSizeBeforeUpdate = emailActivationRepository.findAll().size();
        emailActivation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmailActivationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(emailActivation))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmailActivation in the database
        List<EmailActivation> emailActivationList = emailActivationRepository.findAll();
        assertThat(emailActivationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the EmailActivation in Elasticsearch
        verify(mockEmailActivationSearchRepository, times(0)).save(emailActivation);
    }

    @Test
    @Transactional
    void deleteEmailActivation() throws Exception {
        // Initialize the database
        emailActivationRepository.saveAndFlush(emailActivation);

        int databaseSizeBeforeDelete = emailActivationRepository.findAll().size();

        // Delete the emailActivation
        restEmailActivationMockMvc
            .perform(delete(ENTITY_API_URL_ID, emailActivation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EmailActivation> emailActivationList = emailActivationRepository.findAll();
        assertThat(emailActivationList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the EmailActivation in Elasticsearch
        verify(mockEmailActivationSearchRepository, times(1)).deleteById(emailActivation.getId());
    }

    @Test
    @Transactional
    void searchEmailActivation() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        emailActivationRepository.saveAndFlush(emailActivation);
        when(mockEmailActivationSearchRepository.search(queryStringQuery("id:" + emailActivation.getId())))
            .thenReturn(Collections.singletonList(emailActivation));

        // Search the emailActivation
        restEmailActivationMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + emailActivation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(emailActivation.getId().intValue())))
            .andExpect(jsonPath("$.[*].isActivated").value(hasItem(DEFAULT_IS_ACTIVATED.booleanValue())))
            .andExpect(jsonPath("$.[*].activationKey").value(hasItem(DEFAULT_ACTIVATION_KEY)))
            .andExpect(jsonPath("$.[*].expirationDate").value(hasItem(DEFAULT_EXPIRATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())));
    }
}
