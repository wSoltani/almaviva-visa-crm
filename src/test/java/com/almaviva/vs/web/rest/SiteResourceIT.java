package com.almaviva.vs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.almaviva.vs.IntegrationTest;
import com.almaviva.vs.domain.Site;
import com.almaviva.vs.repository.SiteRepository;
import com.almaviva.vs.repository.search.SiteSearchRepository;
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
 * Integration tests for the {@link SiteResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SiteResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_IMG_URL = "AAAAAAAAAA";
    private static final String UPDATED_IMG_URL = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;

    private static final String ENTITY_API_URL = "/api/sites";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/sites";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SiteRepository siteRepository;

    /**
     * This repository is mocked in the com.almaviva.vs.repository.search test package.
     *
     * @see com.almaviva.vs.repository.search.SiteSearchRepositoryMockConfiguration
     */
    @Autowired
    private SiteSearchRepository mockSiteSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSiteMockMvc;

    private Site site;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Site createEntity(EntityManager em) {
        Site site = new Site().name(DEFAULT_NAME).imgUrl(DEFAULT_IMG_URL).address(DEFAULT_ADDRESS).deleted(DEFAULT_DELETED);
        return site;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Site createUpdatedEntity(EntityManager em) {
        Site site = new Site().name(UPDATED_NAME).imgUrl(UPDATED_IMG_URL).address(UPDATED_ADDRESS).deleted(UPDATED_DELETED);
        return site;
    }

    @BeforeEach
    public void initTest() {
        site = createEntity(em);
    }

    @Test
    @Transactional
    void createSite() throws Exception {
        int databaseSizeBeforeCreate = siteRepository.findAll().size();
        // Create the Site
        restSiteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(site)))
            .andExpect(status().isCreated());

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll();
        assertThat(siteList).hasSize(databaseSizeBeforeCreate + 1);
        Site testSite = siteList.get(siteList.size() - 1);
        assertThat(testSite.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSite.getImgUrl()).isEqualTo(DEFAULT_IMG_URL);
        assertThat(testSite.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testSite.getDeleted()).isEqualTo(DEFAULT_DELETED);

        // Validate the Site in Elasticsearch
        verify(mockSiteSearchRepository, times(1)).save(testSite);
    }

    @Test
    @Transactional
    void createSiteWithExistingId() throws Exception {
        // Create the Site with an existing ID
        site.setId(1L);

        int databaseSizeBeforeCreate = siteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSiteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(site)))
            .andExpect(status().isBadRequest());

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll();
        assertThat(siteList).hasSize(databaseSizeBeforeCreate);

        // Validate the Site in Elasticsearch
        verify(mockSiteSearchRepository, times(0)).save(site);
    }

    @Test
    @Transactional
    void getAllSites() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList
        restSiteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(site.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].imgUrl").value(hasItem(DEFAULT_IMG_URL)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())));
    }

    @Test
    @Transactional
    void getSite() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get the site
        restSiteMockMvc
            .perform(get(ENTITY_API_URL_ID, site.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(site.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.imgUrl").value(DEFAULT_IMG_URL))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingSite() throws Exception {
        // Get the site
        restSiteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSite() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        int databaseSizeBeforeUpdate = siteRepository.findAll().size();

        // Update the site
        Site updatedSite = siteRepository.findById(site.getId()).get();
        // Disconnect from session so that the updates on updatedSite are not directly saved in db
        em.detach(updatedSite);
        updatedSite.name(UPDATED_NAME).imgUrl(UPDATED_IMG_URL).address(UPDATED_ADDRESS).deleted(UPDATED_DELETED);

        restSiteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSite.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSite))
            )
            .andExpect(status().isOk());

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll();
        assertThat(siteList).hasSize(databaseSizeBeforeUpdate);
        Site testSite = siteList.get(siteList.size() - 1);
        assertThat(testSite.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSite.getImgUrl()).isEqualTo(UPDATED_IMG_URL);
        assertThat(testSite.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testSite.getDeleted()).isEqualTo(UPDATED_DELETED);

        // Validate the Site in Elasticsearch
        verify(mockSiteSearchRepository).save(testSite);
    }

    @Test
    @Transactional
    void putNonExistingSite() throws Exception {
        int databaseSizeBeforeUpdate = siteRepository.findAll().size();
        site.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSiteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, site.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(site))
            )
            .andExpect(status().isBadRequest());

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll();
        assertThat(siteList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Site in Elasticsearch
        verify(mockSiteSearchRepository, times(0)).save(site);
    }

    @Test
    @Transactional
    void putWithIdMismatchSite() throws Exception {
        int databaseSizeBeforeUpdate = siteRepository.findAll().size();
        site.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSiteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(site))
            )
            .andExpect(status().isBadRequest());

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll();
        assertThat(siteList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Site in Elasticsearch
        verify(mockSiteSearchRepository, times(0)).save(site);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSite() throws Exception {
        int databaseSizeBeforeUpdate = siteRepository.findAll().size();
        site.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSiteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(site)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll();
        assertThat(siteList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Site in Elasticsearch
        verify(mockSiteSearchRepository, times(0)).save(site);
    }

    @Test
    @Transactional
    void partialUpdateSiteWithPatch() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        int databaseSizeBeforeUpdate = siteRepository.findAll().size();

        // Update the site using partial update
        Site partialUpdatedSite = new Site();
        partialUpdatedSite.setId(site.getId());

        partialUpdatedSite.imgUrl(UPDATED_IMG_URL).address(UPDATED_ADDRESS).deleted(UPDATED_DELETED);

        restSiteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSite.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSite))
            )
            .andExpect(status().isOk());

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll();
        assertThat(siteList).hasSize(databaseSizeBeforeUpdate);
        Site testSite = siteList.get(siteList.size() - 1);
        assertThat(testSite.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSite.getImgUrl()).isEqualTo(UPDATED_IMG_URL);
        assertThat(testSite.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testSite.getDeleted()).isEqualTo(UPDATED_DELETED);
    }

    @Test
    @Transactional
    void fullUpdateSiteWithPatch() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        int databaseSizeBeforeUpdate = siteRepository.findAll().size();

        // Update the site using partial update
        Site partialUpdatedSite = new Site();
        partialUpdatedSite.setId(site.getId());

        partialUpdatedSite.name(UPDATED_NAME).imgUrl(UPDATED_IMG_URL).address(UPDATED_ADDRESS).deleted(UPDATED_DELETED);

        restSiteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSite.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSite))
            )
            .andExpect(status().isOk());

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll();
        assertThat(siteList).hasSize(databaseSizeBeforeUpdate);
        Site testSite = siteList.get(siteList.size() - 1);
        assertThat(testSite.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSite.getImgUrl()).isEqualTo(UPDATED_IMG_URL);
        assertThat(testSite.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testSite.getDeleted()).isEqualTo(UPDATED_DELETED);
    }

    @Test
    @Transactional
    void patchNonExistingSite() throws Exception {
        int databaseSizeBeforeUpdate = siteRepository.findAll().size();
        site.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSiteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, site.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(site))
            )
            .andExpect(status().isBadRequest());

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll();
        assertThat(siteList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Site in Elasticsearch
        verify(mockSiteSearchRepository, times(0)).save(site);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSite() throws Exception {
        int databaseSizeBeforeUpdate = siteRepository.findAll().size();
        site.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSiteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(site))
            )
            .andExpect(status().isBadRequest());

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll();
        assertThat(siteList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Site in Elasticsearch
        verify(mockSiteSearchRepository, times(0)).save(site);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSite() throws Exception {
        int databaseSizeBeforeUpdate = siteRepository.findAll().size();
        site.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSiteMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(site)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll();
        assertThat(siteList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Site in Elasticsearch
        verify(mockSiteSearchRepository, times(0)).save(site);
    }

    @Test
    @Transactional
    void deleteSite() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        int databaseSizeBeforeDelete = siteRepository.findAll().size();

        // Delete the site
        restSiteMockMvc
            .perform(delete(ENTITY_API_URL_ID, site.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Site> siteList = siteRepository.findAll();
        assertThat(siteList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Site in Elasticsearch
        verify(mockSiteSearchRepository, times(1)).deleteById(site.getId());
    }

    @Test
    @Transactional
    void searchSite() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        siteRepository.saveAndFlush(site);
        when(mockSiteSearchRepository.search(queryStringQuery("id:" + site.getId()))).thenReturn(Collections.singletonList(site));

        // Search the site
        restSiteMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + site.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(site.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].imgUrl").value(hasItem(DEFAULT_IMG_URL)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())));
    }
}
