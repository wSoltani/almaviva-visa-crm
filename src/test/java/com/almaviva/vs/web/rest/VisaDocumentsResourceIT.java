package com.almaviva.vs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.almaviva.vs.IntegrationTest;
import com.almaviva.vs.domain.VisaDocuments;
import com.almaviva.vs.repository.VisaDocumentsRepository;
import com.almaviva.vs.repository.search.VisaDocumentsSearchRepository;
import java.util.ArrayList;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link VisaDocumentsResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class VisaDocumentsResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;

    private static final String ENTITY_API_URL = "/api/visa-documents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/visa-documents";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VisaDocumentsRepository visaDocumentsRepository;

    @Mock
    private VisaDocumentsRepository visaDocumentsRepositoryMock;

    /**
     * This repository is mocked in the com.almaviva.vs.repository.search test package.
     *
     * @see com.almaviva.vs.repository.search.VisaDocumentsSearchRepositoryMockConfiguration
     */
    @Autowired
    private VisaDocumentsSearchRepository mockVisaDocumentsSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVisaDocumentsMockMvc;

    private VisaDocuments visaDocuments;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VisaDocuments createEntity(EntityManager em) {
        VisaDocuments visaDocuments = new VisaDocuments().title(DEFAULT_TITLE).description(DEFAULT_DESCRIPTION).deleted(DEFAULT_DELETED);
        return visaDocuments;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VisaDocuments createUpdatedEntity(EntityManager em) {
        VisaDocuments visaDocuments = new VisaDocuments().title(UPDATED_TITLE).description(UPDATED_DESCRIPTION).deleted(UPDATED_DELETED);
        return visaDocuments;
    }

    @BeforeEach
    public void initTest() {
        visaDocuments = createEntity(em);
    }

    @Test
    @Transactional
    void createVisaDocuments() throws Exception {
        int databaseSizeBeforeCreate = visaDocumentsRepository.findAll().size();
        // Create the VisaDocuments
        restVisaDocumentsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(visaDocuments)))
            .andExpect(status().isCreated());

        // Validate the VisaDocuments in the database
        List<VisaDocuments> visaDocumentsList = visaDocumentsRepository.findAll();
        assertThat(visaDocumentsList).hasSize(databaseSizeBeforeCreate + 1);
        VisaDocuments testVisaDocuments = visaDocumentsList.get(visaDocumentsList.size() - 1);
        assertThat(testVisaDocuments.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testVisaDocuments.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testVisaDocuments.getDeleted()).isEqualTo(DEFAULT_DELETED);

        // Validate the VisaDocuments in Elasticsearch
        verify(mockVisaDocumentsSearchRepository, times(1)).save(testVisaDocuments);
    }

    @Test
    @Transactional
    void createVisaDocumentsWithExistingId() throws Exception {
        // Create the VisaDocuments with an existing ID
        visaDocuments.setId(1L);

        int databaseSizeBeforeCreate = visaDocumentsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVisaDocumentsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(visaDocuments)))
            .andExpect(status().isBadRequest());

        // Validate the VisaDocuments in the database
        List<VisaDocuments> visaDocumentsList = visaDocumentsRepository.findAll();
        assertThat(visaDocumentsList).hasSize(databaseSizeBeforeCreate);

        // Validate the VisaDocuments in Elasticsearch
        verify(mockVisaDocumentsSearchRepository, times(0)).save(visaDocuments);
    }

    @Test
    @Transactional
    void getAllVisaDocuments() throws Exception {
        // Initialize the database
        visaDocumentsRepository.saveAndFlush(visaDocuments);

        // Get all the visaDocumentsList
        restVisaDocumentsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(visaDocuments.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllVisaDocumentsWithEagerRelationshipsIsEnabled() throws Exception {
        when(visaDocumentsRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restVisaDocumentsMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(visaDocumentsRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllVisaDocumentsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(visaDocumentsRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restVisaDocumentsMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(visaDocumentsRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getVisaDocuments() throws Exception {
        // Initialize the database
        visaDocumentsRepository.saveAndFlush(visaDocuments);

        // Get the visaDocuments
        restVisaDocumentsMockMvc
            .perform(get(ENTITY_API_URL_ID, visaDocuments.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(visaDocuments.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingVisaDocuments() throws Exception {
        // Get the visaDocuments
        restVisaDocumentsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewVisaDocuments() throws Exception {
        // Initialize the database
        visaDocumentsRepository.saveAndFlush(visaDocuments);

        int databaseSizeBeforeUpdate = visaDocumentsRepository.findAll().size();

        // Update the visaDocuments
        VisaDocuments updatedVisaDocuments = visaDocumentsRepository.findById(visaDocuments.getId()).get();
        // Disconnect from session so that the updates on updatedVisaDocuments are not directly saved in db
        em.detach(updatedVisaDocuments);
        updatedVisaDocuments.title(UPDATED_TITLE).description(UPDATED_DESCRIPTION).deleted(UPDATED_DELETED);

        restVisaDocumentsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVisaDocuments.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedVisaDocuments))
            )
            .andExpect(status().isOk());

        // Validate the VisaDocuments in the database
        List<VisaDocuments> visaDocumentsList = visaDocumentsRepository.findAll();
        assertThat(visaDocumentsList).hasSize(databaseSizeBeforeUpdate);
        VisaDocuments testVisaDocuments = visaDocumentsList.get(visaDocumentsList.size() - 1);
        assertThat(testVisaDocuments.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testVisaDocuments.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testVisaDocuments.getDeleted()).isEqualTo(UPDATED_DELETED);

        // Validate the VisaDocuments in Elasticsearch
        verify(mockVisaDocumentsSearchRepository).save(testVisaDocuments);
    }

    @Test
    @Transactional
    void putNonExistingVisaDocuments() throws Exception {
        int databaseSizeBeforeUpdate = visaDocumentsRepository.findAll().size();
        visaDocuments.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVisaDocumentsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, visaDocuments.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(visaDocuments))
            )
            .andExpect(status().isBadRequest());

        // Validate the VisaDocuments in the database
        List<VisaDocuments> visaDocumentsList = visaDocumentsRepository.findAll();
        assertThat(visaDocumentsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the VisaDocuments in Elasticsearch
        verify(mockVisaDocumentsSearchRepository, times(0)).save(visaDocuments);
    }

    @Test
    @Transactional
    void putWithIdMismatchVisaDocuments() throws Exception {
        int databaseSizeBeforeUpdate = visaDocumentsRepository.findAll().size();
        visaDocuments.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVisaDocumentsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(visaDocuments))
            )
            .andExpect(status().isBadRequest());

        // Validate the VisaDocuments in the database
        List<VisaDocuments> visaDocumentsList = visaDocumentsRepository.findAll();
        assertThat(visaDocumentsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the VisaDocuments in Elasticsearch
        verify(mockVisaDocumentsSearchRepository, times(0)).save(visaDocuments);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVisaDocuments() throws Exception {
        int databaseSizeBeforeUpdate = visaDocumentsRepository.findAll().size();
        visaDocuments.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVisaDocumentsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(visaDocuments)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the VisaDocuments in the database
        List<VisaDocuments> visaDocumentsList = visaDocumentsRepository.findAll();
        assertThat(visaDocumentsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the VisaDocuments in Elasticsearch
        verify(mockVisaDocumentsSearchRepository, times(0)).save(visaDocuments);
    }

    @Test
    @Transactional
    void partialUpdateVisaDocumentsWithPatch() throws Exception {
        // Initialize the database
        visaDocumentsRepository.saveAndFlush(visaDocuments);

        int databaseSizeBeforeUpdate = visaDocumentsRepository.findAll().size();

        // Update the visaDocuments using partial update
        VisaDocuments partialUpdatedVisaDocuments = new VisaDocuments();
        partialUpdatedVisaDocuments.setId(visaDocuments.getId());

        partialUpdatedVisaDocuments.title(UPDATED_TITLE).description(UPDATED_DESCRIPTION);

        restVisaDocumentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVisaDocuments.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVisaDocuments))
            )
            .andExpect(status().isOk());

        // Validate the VisaDocuments in the database
        List<VisaDocuments> visaDocumentsList = visaDocumentsRepository.findAll();
        assertThat(visaDocumentsList).hasSize(databaseSizeBeforeUpdate);
        VisaDocuments testVisaDocuments = visaDocumentsList.get(visaDocumentsList.size() - 1);
        assertThat(testVisaDocuments.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testVisaDocuments.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testVisaDocuments.getDeleted()).isEqualTo(DEFAULT_DELETED);
    }

    @Test
    @Transactional
    void fullUpdateVisaDocumentsWithPatch() throws Exception {
        // Initialize the database
        visaDocumentsRepository.saveAndFlush(visaDocuments);

        int databaseSizeBeforeUpdate = visaDocumentsRepository.findAll().size();

        // Update the visaDocuments using partial update
        VisaDocuments partialUpdatedVisaDocuments = new VisaDocuments();
        partialUpdatedVisaDocuments.setId(visaDocuments.getId());

        partialUpdatedVisaDocuments.title(UPDATED_TITLE).description(UPDATED_DESCRIPTION).deleted(UPDATED_DELETED);

        restVisaDocumentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVisaDocuments.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVisaDocuments))
            )
            .andExpect(status().isOk());

        // Validate the VisaDocuments in the database
        List<VisaDocuments> visaDocumentsList = visaDocumentsRepository.findAll();
        assertThat(visaDocumentsList).hasSize(databaseSizeBeforeUpdate);
        VisaDocuments testVisaDocuments = visaDocumentsList.get(visaDocumentsList.size() - 1);
        assertThat(testVisaDocuments.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testVisaDocuments.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testVisaDocuments.getDeleted()).isEqualTo(UPDATED_DELETED);
    }

    @Test
    @Transactional
    void patchNonExistingVisaDocuments() throws Exception {
        int databaseSizeBeforeUpdate = visaDocumentsRepository.findAll().size();
        visaDocuments.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVisaDocumentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, visaDocuments.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(visaDocuments))
            )
            .andExpect(status().isBadRequest());

        // Validate the VisaDocuments in the database
        List<VisaDocuments> visaDocumentsList = visaDocumentsRepository.findAll();
        assertThat(visaDocumentsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the VisaDocuments in Elasticsearch
        verify(mockVisaDocumentsSearchRepository, times(0)).save(visaDocuments);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVisaDocuments() throws Exception {
        int databaseSizeBeforeUpdate = visaDocumentsRepository.findAll().size();
        visaDocuments.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVisaDocumentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(visaDocuments))
            )
            .andExpect(status().isBadRequest());

        // Validate the VisaDocuments in the database
        List<VisaDocuments> visaDocumentsList = visaDocumentsRepository.findAll();
        assertThat(visaDocumentsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the VisaDocuments in Elasticsearch
        verify(mockVisaDocumentsSearchRepository, times(0)).save(visaDocuments);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVisaDocuments() throws Exception {
        int databaseSizeBeforeUpdate = visaDocumentsRepository.findAll().size();
        visaDocuments.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVisaDocumentsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(visaDocuments))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the VisaDocuments in the database
        List<VisaDocuments> visaDocumentsList = visaDocumentsRepository.findAll();
        assertThat(visaDocumentsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the VisaDocuments in Elasticsearch
        verify(mockVisaDocumentsSearchRepository, times(0)).save(visaDocuments);
    }

    @Test
    @Transactional
    void deleteVisaDocuments() throws Exception {
        // Initialize the database
        visaDocumentsRepository.saveAndFlush(visaDocuments);

        int databaseSizeBeforeDelete = visaDocumentsRepository.findAll().size();

        // Delete the visaDocuments
        restVisaDocumentsMockMvc
            .perform(delete(ENTITY_API_URL_ID, visaDocuments.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<VisaDocuments> visaDocumentsList = visaDocumentsRepository.findAll();
        assertThat(visaDocumentsList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the VisaDocuments in Elasticsearch
        verify(mockVisaDocumentsSearchRepository, times(1)).deleteById(visaDocuments.getId());
    }

    @Test
    @Transactional
    void searchVisaDocuments() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        visaDocumentsRepository.saveAndFlush(visaDocuments);
        when(mockVisaDocumentsSearchRepository.search(queryStringQuery("id:" + visaDocuments.getId())))
            .thenReturn(Collections.singletonList(visaDocuments));

        // Search the visaDocuments
        restVisaDocumentsMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + visaDocuments.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(visaDocuments.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())));
    }
}
