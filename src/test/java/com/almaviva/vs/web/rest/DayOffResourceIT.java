package com.almaviva.vs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.almaviva.vs.IntegrationTest;
import com.almaviva.vs.domain.DayOff;
import com.almaviva.vs.repository.DayOffRepository;
import com.almaviva.vs.repository.search.DayOffSearchRepository;
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
 * Integration tests for the {@link DayOffResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DayOffResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_IS_HOLIDAY = false;
    private static final Boolean UPDATED_IS_HOLIDAY = true;

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;

    private static final String ENTITY_API_URL = "/api/day-offs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/day-offs";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DayOffRepository dayOffRepository;

    /**
     * This repository is mocked in the com.almaviva.vs.repository.search test package.
     *
     * @see com.almaviva.vs.repository.search.DayOffSearchRepositoryMockConfiguration
     */
    @Autowired
    private DayOffSearchRepository mockDayOffSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDayOffMockMvc;

    private DayOff dayOff;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DayOff createEntity(EntityManager em) {
        DayOff dayOff = new DayOff()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .date(DEFAULT_DATE)
            .isHoliday(DEFAULT_IS_HOLIDAY)
            .deleted(DEFAULT_DELETED);
        return dayOff;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DayOff createUpdatedEntity(EntityManager em) {
        DayOff dayOff = new DayOff()
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .date(UPDATED_DATE)
            .isHoliday(UPDATED_IS_HOLIDAY)
            .deleted(UPDATED_DELETED);
        return dayOff;
    }

    @BeforeEach
    public void initTest() {
        dayOff = createEntity(em);
    }

    @Test
    @Transactional
    void createDayOff() throws Exception {
        int databaseSizeBeforeCreate = dayOffRepository.findAll().size();
        // Create the DayOff
        restDayOffMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dayOff)))
            .andExpect(status().isCreated());

        // Validate the DayOff in the database
        List<DayOff> dayOffList = dayOffRepository.findAll();
        assertThat(dayOffList).hasSize(databaseSizeBeforeCreate + 1);
        DayOff testDayOff = dayOffList.get(dayOffList.size() - 1);
        assertThat(testDayOff.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testDayOff.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testDayOff.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testDayOff.getIsHoliday()).isEqualTo(DEFAULT_IS_HOLIDAY);
        assertThat(testDayOff.getDeleted()).isEqualTo(DEFAULT_DELETED);

        // Validate the DayOff in Elasticsearch
        verify(mockDayOffSearchRepository, times(1)).save(testDayOff);
    }

    @Test
    @Transactional
    void createDayOffWithExistingId() throws Exception {
        // Create the DayOff with an existing ID
        dayOff.setId(1L);

        int databaseSizeBeforeCreate = dayOffRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDayOffMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dayOff)))
            .andExpect(status().isBadRequest());

        // Validate the DayOff in the database
        List<DayOff> dayOffList = dayOffRepository.findAll();
        assertThat(dayOffList).hasSize(databaseSizeBeforeCreate);

        // Validate the DayOff in Elasticsearch
        verify(mockDayOffSearchRepository, times(0)).save(dayOff);
    }

    @Test
    @Transactional
    void getAllDayOffs() throws Exception {
        // Initialize the database
        dayOffRepository.saveAndFlush(dayOff);

        // Get all the dayOffList
        restDayOffMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dayOff.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].isHoliday").value(hasItem(DEFAULT_IS_HOLIDAY.booleanValue())))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())));
    }

    @Test
    @Transactional
    void getDayOff() throws Exception {
        // Initialize the database
        dayOffRepository.saveAndFlush(dayOff);

        // Get the dayOff
        restDayOffMockMvc
            .perform(get(ENTITY_API_URL_ID, dayOff.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dayOff.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.isHoliday").value(DEFAULT_IS_HOLIDAY.booleanValue()))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingDayOff() throws Exception {
        // Get the dayOff
        restDayOffMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDayOff() throws Exception {
        // Initialize the database
        dayOffRepository.saveAndFlush(dayOff);

        int databaseSizeBeforeUpdate = dayOffRepository.findAll().size();

        // Update the dayOff
        DayOff updatedDayOff = dayOffRepository.findById(dayOff.getId()).get();
        // Disconnect from session so that the updates on updatedDayOff are not directly saved in db
        em.detach(updatedDayOff);
        updatedDayOff
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .date(UPDATED_DATE)
            .isHoliday(UPDATED_IS_HOLIDAY)
            .deleted(UPDATED_DELETED);

        restDayOffMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDayOff.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDayOff))
            )
            .andExpect(status().isOk());

        // Validate the DayOff in the database
        List<DayOff> dayOffList = dayOffRepository.findAll();
        assertThat(dayOffList).hasSize(databaseSizeBeforeUpdate);
        DayOff testDayOff = dayOffList.get(dayOffList.size() - 1);
        assertThat(testDayOff.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testDayOff.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDayOff.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testDayOff.getIsHoliday()).isEqualTo(UPDATED_IS_HOLIDAY);
        assertThat(testDayOff.getDeleted()).isEqualTo(UPDATED_DELETED);

        // Validate the DayOff in Elasticsearch
        verify(mockDayOffSearchRepository).save(testDayOff);
    }

    @Test
    @Transactional
    void putNonExistingDayOff() throws Exception {
        int databaseSizeBeforeUpdate = dayOffRepository.findAll().size();
        dayOff.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDayOffMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dayOff.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dayOff))
            )
            .andExpect(status().isBadRequest());

        // Validate the DayOff in the database
        List<DayOff> dayOffList = dayOffRepository.findAll();
        assertThat(dayOffList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DayOff in Elasticsearch
        verify(mockDayOffSearchRepository, times(0)).save(dayOff);
    }

    @Test
    @Transactional
    void putWithIdMismatchDayOff() throws Exception {
        int databaseSizeBeforeUpdate = dayOffRepository.findAll().size();
        dayOff.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDayOffMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dayOff))
            )
            .andExpect(status().isBadRequest());

        // Validate the DayOff in the database
        List<DayOff> dayOffList = dayOffRepository.findAll();
        assertThat(dayOffList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DayOff in Elasticsearch
        verify(mockDayOffSearchRepository, times(0)).save(dayOff);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDayOff() throws Exception {
        int databaseSizeBeforeUpdate = dayOffRepository.findAll().size();
        dayOff.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDayOffMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dayOff)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DayOff in the database
        List<DayOff> dayOffList = dayOffRepository.findAll();
        assertThat(dayOffList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DayOff in Elasticsearch
        verify(mockDayOffSearchRepository, times(0)).save(dayOff);
    }

    @Test
    @Transactional
    void partialUpdateDayOffWithPatch() throws Exception {
        // Initialize the database
        dayOffRepository.saveAndFlush(dayOff);

        int databaseSizeBeforeUpdate = dayOffRepository.findAll().size();

        // Update the dayOff using partial update
        DayOff partialUpdatedDayOff = new DayOff();
        partialUpdatedDayOff.setId(dayOff.getId());

        partialUpdatedDayOff.date(UPDATED_DATE).isHoliday(UPDATED_IS_HOLIDAY);

        restDayOffMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDayOff.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDayOff))
            )
            .andExpect(status().isOk());

        // Validate the DayOff in the database
        List<DayOff> dayOffList = dayOffRepository.findAll();
        assertThat(dayOffList).hasSize(databaseSizeBeforeUpdate);
        DayOff testDayOff = dayOffList.get(dayOffList.size() - 1);
        assertThat(testDayOff.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testDayOff.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testDayOff.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testDayOff.getIsHoliday()).isEqualTo(UPDATED_IS_HOLIDAY);
        assertThat(testDayOff.getDeleted()).isEqualTo(DEFAULT_DELETED);
    }

    @Test
    @Transactional
    void fullUpdateDayOffWithPatch() throws Exception {
        // Initialize the database
        dayOffRepository.saveAndFlush(dayOff);

        int databaseSizeBeforeUpdate = dayOffRepository.findAll().size();

        // Update the dayOff using partial update
        DayOff partialUpdatedDayOff = new DayOff();
        partialUpdatedDayOff.setId(dayOff.getId());

        partialUpdatedDayOff
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .date(UPDATED_DATE)
            .isHoliday(UPDATED_IS_HOLIDAY)
            .deleted(UPDATED_DELETED);

        restDayOffMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDayOff.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDayOff))
            )
            .andExpect(status().isOk());

        // Validate the DayOff in the database
        List<DayOff> dayOffList = dayOffRepository.findAll();
        assertThat(dayOffList).hasSize(databaseSizeBeforeUpdate);
        DayOff testDayOff = dayOffList.get(dayOffList.size() - 1);
        assertThat(testDayOff.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testDayOff.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDayOff.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testDayOff.getIsHoliday()).isEqualTo(UPDATED_IS_HOLIDAY);
        assertThat(testDayOff.getDeleted()).isEqualTo(UPDATED_DELETED);
    }

    @Test
    @Transactional
    void patchNonExistingDayOff() throws Exception {
        int databaseSizeBeforeUpdate = dayOffRepository.findAll().size();
        dayOff.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDayOffMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dayOff.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dayOff))
            )
            .andExpect(status().isBadRequest());

        // Validate the DayOff in the database
        List<DayOff> dayOffList = dayOffRepository.findAll();
        assertThat(dayOffList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DayOff in Elasticsearch
        verify(mockDayOffSearchRepository, times(0)).save(dayOff);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDayOff() throws Exception {
        int databaseSizeBeforeUpdate = dayOffRepository.findAll().size();
        dayOff.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDayOffMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dayOff))
            )
            .andExpect(status().isBadRequest());

        // Validate the DayOff in the database
        List<DayOff> dayOffList = dayOffRepository.findAll();
        assertThat(dayOffList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DayOff in Elasticsearch
        verify(mockDayOffSearchRepository, times(0)).save(dayOff);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDayOff() throws Exception {
        int databaseSizeBeforeUpdate = dayOffRepository.findAll().size();
        dayOff.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDayOffMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(dayOff)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DayOff in the database
        List<DayOff> dayOffList = dayOffRepository.findAll();
        assertThat(dayOffList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DayOff in Elasticsearch
        verify(mockDayOffSearchRepository, times(0)).save(dayOff);
    }

    @Test
    @Transactional
    void deleteDayOff() throws Exception {
        // Initialize the database
        dayOffRepository.saveAndFlush(dayOff);

        int databaseSizeBeforeDelete = dayOffRepository.findAll().size();

        // Delete the dayOff
        restDayOffMockMvc
            .perform(delete(ENTITY_API_URL_ID, dayOff.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DayOff> dayOffList = dayOffRepository.findAll();
        assertThat(dayOffList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the DayOff in Elasticsearch
        verify(mockDayOffSearchRepository, times(1)).deleteById(dayOff.getId());
    }

    @Test
    @Transactional
    void searchDayOff() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        dayOffRepository.saveAndFlush(dayOff);
        when(mockDayOffSearchRepository.search(queryStringQuery("id:" + dayOff.getId()))).thenReturn(Collections.singletonList(dayOff));

        // Search the dayOff
        restDayOffMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + dayOff.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dayOff.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].isHoliday").value(hasItem(DEFAULT_IS_HOLIDAY.booleanValue())))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())));
    }
}
