package com.covidtracker.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.covidtracker.myapp.IntegrationTest;
import com.covidtracker.myapp.domain.CasesStatistics;
import com.covidtracker.myapp.domain.enumeration.EnumCountry;
import com.covidtracker.myapp.repository.CasesStatisticsRepository;
import com.covidtracker.myapp.service.criteria.CasesStatisticsCriteria;
import com.covidtracker.myapp.service.dto.CasesStatisticsDTO;
import com.covidtracker.myapp.service.mapper.CasesStatisticsMapper;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CasesStatisticsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CasesStatisticsResourceIT {

    private static final UUID DEFAULT_UUID = UUID.randomUUID();
    private static final UUID UPDATED_UUID = UUID.randomUUID();

    private static final Double DEFAULT_TOTAL_CASES_NUMBER = 1D;
    private static final Double UPDATED_TOTAL_CASES_NUMBER = 2D;
    private static final Double SMALLER_TOTAL_CASES_NUMBER = 1D - 1D;

    private static final EnumCountry DEFAULT_COUNTRY = EnumCountry.TUNISIA;
    private static final EnumCountry UPDATED_COUNTRY = EnumCountry.ALGERIA;

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE = LocalDate.ofEpochDay(-1L);

    private static final Integer DEFAULT_YEAR = 1;
    private static final Integer UPDATED_YEAR = 2;
    private static final Integer SMALLER_YEAR = 1 - 1;

    private static final Integer DEFAULT_MONTH = 1;
    private static final Integer UPDATED_MONTH = 2;
    private static final Integer SMALLER_MONTH = 1 - 1;

    private static final String ENTITY_API_URL = "/api/cases-statistics";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CasesStatisticsRepository casesStatisticsRepository;

    @Autowired
    private CasesStatisticsMapper casesStatisticsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCasesStatisticsMockMvc;

    private CasesStatistics casesStatistics;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CasesStatistics createEntity(EntityManager em) {
        CasesStatistics casesStatistics = new CasesStatistics()
            .uuid(DEFAULT_UUID)
            .totalCasesNumber(DEFAULT_TOTAL_CASES_NUMBER)
            .country(DEFAULT_COUNTRY)
            .date(DEFAULT_DATE)
            .year(DEFAULT_YEAR)
            .month(DEFAULT_MONTH);
        return casesStatistics;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CasesStatistics createUpdatedEntity(EntityManager em) {
        CasesStatistics casesStatistics = new CasesStatistics()
            .uuid(UPDATED_UUID)
            .totalCasesNumber(UPDATED_TOTAL_CASES_NUMBER)
            .country(UPDATED_COUNTRY)
            .date(UPDATED_DATE)
            .year(UPDATED_YEAR)
            .month(UPDATED_MONTH);
        return casesStatistics;
    }

    @BeforeEach
    public void initTest() {
        casesStatistics = createEntity(em);
    }

    @Test
    @Transactional
    void createCasesStatistics() throws Exception {
        int databaseSizeBeforeCreate = casesStatisticsRepository.findAll().size();
        // Create the CasesStatistics
        CasesStatisticsDTO casesStatisticsDTO = casesStatisticsMapper.toDto(casesStatistics);
        restCasesStatisticsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(casesStatisticsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CasesStatistics in the database
        List<CasesStatistics> casesStatisticsList = casesStatisticsRepository.findAll();
        assertThat(casesStatisticsList).hasSize(databaseSizeBeforeCreate + 1);
        CasesStatistics testCasesStatistics = casesStatisticsList.get(casesStatisticsList.size() - 1);
        assertThat(testCasesStatistics.getUuid()).isEqualTo(DEFAULT_UUID);
        assertThat(testCasesStatistics.getTotalCasesNumber()).isEqualTo(DEFAULT_TOTAL_CASES_NUMBER);
        assertThat(testCasesStatistics.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testCasesStatistics.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testCasesStatistics.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testCasesStatistics.getMonth()).isEqualTo(DEFAULT_MONTH);
    }

    @Test
    @Transactional
    void createCasesStatisticsWithExistingId() throws Exception {
        // Create the CasesStatistics with an existing ID
        casesStatistics.setId(1L);
        CasesStatisticsDTO casesStatisticsDTO = casesStatisticsMapper.toDto(casesStatistics);

        int databaseSizeBeforeCreate = casesStatisticsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCasesStatisticsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(casesStatisticsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CasesStatistics in the database
        List<CasesStatistics> casesStatisticsList = casesStatisticsRepository.findAll();
        assertThat(casesStatisticsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUuidIsRequired() throws Exception {
        int databaseSizeBeforeTest = casesStatisticsRepository.findAll().size();
        // set the field null
        casesStatistics.setUuid(null);

        // Create the CasesStatistics, which fails.
        CasesStatisticsDTO casesStatisticsDTO = casesStatisticsMapper.toDto(casesStatistics);

        restCasesStatisticsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(casesStatisticsDTO))
            )
            .andExpect(status().isBadRequest());

        List<CasesStatistics> casesStatisticsList = casesStatisticsRepository.findAll();
        assertThat(casesStatisticsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCountryIsRequired() throws Exception {
        int databaseSizeBeforeTest = casesStatisticsRepository.findAll().size();
        // set the field null
        casesStatistics.setCountry(null);

        // Create the CasesStatistics, which fails.
        CasesStatisticsDTO casesStatisticsDTO = casesStatisticsMapper.toDto(casesStatistics);

        restCasesStatisticsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(casesStatisticsDTO))
            )
            .andExpect(status().isBadRequest());

        List<CasesStatistics> casesStatisticsList = casesStatisticsRepository.findAll();
        assertThat(casesStatisticsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = casesStatisticsRepository.findAll().size();
        // set the field null
        casesStatistics.setDate(null);

        // Create the CasesStatistics, which fails.
        CasesStatisticsDTO casesStatisticsDTO = casesStatisticsMapper.toDto(casesStatistics);

        restCasesStatisticsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(casesStatisticsDTO))
            )
            .andExpect(status().isBadRequest());

        List<CasesStatistics> casesStatisticsList = casesStatisticsRepository.findAll();
        assertThat(casesStatisticsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCasesStatistics() throws Exception {
        // Initialize the database
        casesStatisticsRepository.saveAndFlush(casesStatistics);

        // Get all the casesStatisticsList
        restCasesStatisticsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(casesStatistics.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].totalCasesNumber").value(hasItem(DEFAULT_TOTAL_CASES_NUMBER.doubleValue())))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
            .andExpect(jsonPath("$.[*].month").value(hasItem(DEFAULT_MONTH)));
    }

    @Test
    @Transactional
    void getCasesStatistics() throws Exception {
        // Initialize the database
        casesStatisticsRepository.saveAndFlush(casesStatistics);

        // Get the casesStatistics
        restCasesStatisticsMockMvc
            .perform(get(ENTITY_API_URL_ID, casesStatistics.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(casesStatistics.getId().intValue()))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID.toString()))
            .andExpect(jsonPath("$.totalCasesNumber").value(DEFAULT_TOTAL_CASES_NUMBER.doubleValue()))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY.toString()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR))
            .andExpect(jsonPath("$.month").value(DEFAULT_MONTH));
    }

    @Test
    @Transactional
    void getCasesStatisticsByIdFiltering() throws Exception {
        // Initialize the database
        casesStatisticsRepository.saveAndFlush(casesStatistics);

        Long id = casesStatistics.getId();

        defaultCasesStatisticsShouldBeFound("id.equals=" + id);
        defaultCasesStatisticsShouldNotBeFound("id.notEquals=" + id);

        defaultCasesStatisticsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCasesStatisticsShouldNotBeFound("id.greaterThan=" + id);

        defaultCasesStatisticsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCasesStatisticsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCasesStatisticsByUuidIsEqualToSomething() throws Exception {
        // Initialize the database
        casesStatisticsRepository.saveAndFlush(casesStatistics);

        // Get all the casesStatisticsList where uuid equals to DEFAULT_UUID
        defaultCasesStatisticsShouldBeFound("uuid.equals=" + DEFAULT_UUID);

        // Get all the casesStatisticsList where uuid equals to UPDATED_UUID
        defaultCasesStatisticsShouldNotBeFound("uuid.equals=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllCasesStatisticsByUuidIsNotEqualToSomething() throws Exception {
        // Initialize the database
        casesStatisticsRepository.saveAndFlush(casesStatistics);

        // Get all the casesStatisticsList where uuid not equals to DEFAULT_UUID
        defaultCasesStatisticsShouldNotBeFound("uuid.notEquals=" + DEFAULT_UUID);

        // Get all the casesStatisticsList where uuid not equals to UPDATED_UUID
        defaultCasesStatisticsShouldBeFound("uuid.notEquals=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllCasesStatisticsByUuidIsInShouldWork() throws Exception {
        // Initialize the database
        casesStatisticsRepository.saveAndFlush(casesStatistics);

        // Get all the casesStatisticsList where uuid in DEFAULT_UUID or UPDATED_UUID
        defaultCasesStatisticsShouldBeFound("uuid.in=" + DEFAULT_UUID + "," + UPDATED_UUID);

        // Get all the casesStatisticsList where uuid equals to UPDATED_UUID
        defaultCasesStatisticsShouldNotBeFound("uuid.in=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllCasesStatisticsByUuidIsNullOrNotNull() throws Exception {
        // Initialize the database
        casesStatisticsRepository.saveAndFlush(casesStatistics);

        // Get all the casesStatisticsList where uuid is not null
        defaultCasesStatisticsShouldBeFound("uuid.specified=true");

        // Get all the casesStatisticsList where uuid is null
        defaultCasesStatisticsShouldNotBeFound("uuid.specified=false");
    }

    @Test
    @Transactional
    void getAllCasesStatisticsByTotalCasesNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        casesStatisticsRepository.saveAndFlush(casesStatistics);

        // Get all the casesStatisticsList where totalCasesNumber equals to DEFAULT_TOTAL_CASES_NUMBER
        defaultCasesStatisticsShouldBeFound("totalCasesNumber.equals=" + DEFAULT_TOTAL_CASES_NUMBER);

        // Get all the casesStatisticsList where totalCasesNumber equals to UPDATED_TOTAL_CASES_NUMBER
        defaultCasesStatisticsShouldNotBeFound("totalCasesNumber.equals=" + UPDATED_TOTAL_CASES_NUMBER);
    }

    @Test
    @Transactional
    void getAllCasesStatisticsByTotalCasesNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        casesStatisticsRepository.saveAndFlush(casesStatistics);

        // Get all the casesStatisticsList where totalCasesNumber not equals to DEFAULT_TOTAL_CASES_NUMBER
        defaultCasesStatisticsShouldNotBeFound("totalCasesNumber.notEquals=" + DEFAULT_TOTAL_CASES_NUMBER);

        // Get all the casesStatisticsList where totalCasesNumber not equals to UPDATED_TOTAL_CASES_NUMBER
        defaultCasesStatisticsShouldBeFound("totalCasesNumber.notEquals=" + UPDATED_TOTAL_CASES_NUMBER);
    }

    @Test
    @Transactional
    void getAllCasesStatisticsByTotalCasesNumberIsInShouldWork() throws Exception {
        // Initialize the database
        casesStatisticsRepository.saveAndFlush(casesStatistics);

        // Get all the casesStatisticsList where totalCasesNumber in DEFAULT_TOTAL_CASES_NUMBER or UPDATED_TOTAL_CASES_NUMBER
        defaultCasesStatisticsShouldBeFound("totalCasesNumber.in=" + DEFAULT_TOTAL_CASES_NUMBER + "," + UPDATED_TOTAL_CASES_NUMBER);

        // Get all the casesStatisticsList where totalCasesNumber equals to UPDATED_TOTAL_CASES_NUMBER
        defaultCasesStatisticsShouldNotBeFound("totalCasesNumber.in=" + UPDATED_TOTAL_CASES_NUMBER);
    }

    @Test
    @Transactional
    void getAllCasesStatisticsByTotalCasesNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        casesStatisticsRepository.saveAndFlush(casesStatistics);

        // Get all the casesStatisticsList where totalCasesNumber is not null
        defaultCasesStatisticsShouldBeFound("totalCasesNumber.specified=true");

        // Get all the casesStatisticsList where totalCasesNumber is null
        defaultCasesStatisticsShouldNotBeFound("totalCasesNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllCasesStatisticsByTotalCasesNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        casesStatisticsRepository.saveAndFlush(casesStatistics);

        // Get all the casesStatisticsList where totalCasesNumber is greater than or equal to DEFAULT_TOTAL_CASES_NUMBER
        defaultCasesStatisticsShouldBeFound("totalCasesNumber.greaterThanOrEqual=" + DEFAULT_TOTAL_CASES_NUMBER);

        // Get all the casesStatisticsList where totalCasesNumber is greater than or equal to UPDATED_TOTAL_CASES_NUMBER
        defaultCasesStatisticsShouldNotBeFound("totalCasesNumber.greaterThanOrEqual=" + UPDATED_TOTAL_CASES_NUMBER);
    }

    @Test
    @Transactional
    void getAllCasesStatisticsByTotalCasesNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        casesStatisticsRepository.saveAndFlush(casesStatistics);

        // Get all the casesStatisticsList where totalCasesNumber is less than or equal to DEFAULT_TOTAL_CASES_NUMBER
        defaultCasesStatisticsShouldBeFound("totalCasesNumber.lessThanOrEqual=" + DEFAULT_TOTAL_CASES_NUMBER);

        // Get all the casesStatisticsList where totalCasesNumber is less than or equal to SMALLER_TOTAL_CASES_NUMBER
        defaultCasesStatisticsShouldNotBeFound("totalCasesNumber.lessThanOrEqual=" + SMALLER_TOTAL_CASES_NUMBER);
    }

    @Test
    @Transactional
    void getAllCasesStatisticsByTotalCasesNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        casesStatisticsRepository.saveAndFlush(casesStatistics);

        // Get all the casesStatisticsList where totalCasesNumber is less than DEFAULT_TOTAL_CASES_NUMBER
        defaultCasesStatisticsShouldNotBeFound("totalCasesNumber.lessThan=" + DEFAULT_TOTAL_CASES_NUMBER);

        // Get all the casesStatisticsList where totalCasesNumber is less than UPDATED_TOTAL_CASES_NUMBER
        defaultCasesStatisticsShouldBeFound("totalCasesNumber.lessThan=" + UPDATED_TOTAL_CASES_NUMBER);
    }

    @Test
    @Transactional
    void getAllCasesStatisticsByTotalCasesNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        casesStatisticsRepository.saveAndFlush(casesStatistics);

        // Get all the casesStatisticsList where totalCasesNumber is greater than DEFAULT_TOTAL_CASES_NUMBER
        defaultCasesStatisticsShouldNotBeFound("totalCasesNumber.greaterThan=" + DEFAULT_TOTAL_CASES_NUMBER);

        // Get all the casesStatisticsList where totalCasesNumber is greater than SMALLER_TOTAL_CASES_NUMBER
        defaultCasesStatisticsShouldBeFound("totalCasesNumber.greaterThan=" + SMALLER_TOTAL_CASES_NUMBER);
    }

    @Test
    @Transactional
    void getAllCasesStatisticsByCountryIsEqualToSomething() throws Exception {
        // Initialize the database
        casesStatisticsRepository.saveAndFlush(casesStatistics);

        // Get all the casesStatisticsList where country equals to DEFAULT_COUNTRY
        defaultCasesStatisticsShouldBeFound("country.equals=" + DEFAULT_COUNTRY);

        // Get all the casesStatisticsList where country equals to UPDATED_COUNTRY
        defaultCasesStatisticsShouldNotBeFound("country.equals=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllCasesStatisticsByCountryIsNotEqualToSomething() throws Exception {
        // Initialize the database
        casesStatisticsRepository.saveAndFlush(casesStatistics);

        // Get all the casesStatisticsList where country not equals to DEFAULT_COUNTRY
        defaultCasesStatisticsShouldNotBeFound("country.notEquals=" + DEFAULT_COUNTRY);

        // Get all the casesStatisticsList where country not equals to UPDATED_COUNTRY
        defaultCasesStatisticsShouldBeFound("country.notEquals=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllCasesStatisticsByCountryIsInShouldWork() throws Exception {
        // Initialize the database
        casesStatisticsRepository.saveAndFlush(casesStatistics);

        // Get all the casesStatisticsList where country in DEFAULT_COUNTRY or UPDATED_COUNTRY
        defaultCasesStatisticsShouldBeFound("country.in=" + DEFAULT_COUNTRY + "," + UPDATED_COUNTRY);

        // Get all the casesStatisticsList where country equals to UPDATED_COUNTRY
        defaultCasesStatisticsShouldNotBeFound("country.in=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllCasesStatisticsByCountryIsNullOrNotNull() throws Exception {
        // Initialize the database
        casesStatisticsRepository.saveAndFlush(casesStatistics);

        // Get all the casesStatisticsList where country is not null
        defaultCasesStatisticsShouldBeFound("country.specified=true");

        // Get all the casesStatisticsList where country is null
        defaultCasesStatisticsShouldNotBeFound("country.specified=false");
    }

    @Test
    @Transactional
    void getAllCasesStatisticsByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        casesStatisticsRepository.saveAndFlush(casesStatistics);

        // Get all the casesStatisticsList where date equals to DEFAULT_DATE
        defaultCasesStatisticsShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the casesStatisticsList where date equals to UPDATED_DATE
        defaultCasesStatisticsShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllCasesStatisticsByDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        casesStatisticsRepository.saveAndFlush(casesStatistics);

        // Get all the casesStatisticsList where date not equals to DEFAULT_DATE
        defaultCasesStatisticsShouldNotBeFound("date.notEquals=" + DEFAULT_DATE);

        // Get all the casesStatisticsList where date not equals to UPDATED_DATE
        defaultCasesStatisticsShouldBeFound("date.notEquals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllCasesStatisticsByDateIsInShouldWork() throws Exception {
        // Initialize the database
        casesStatisticsRepository.saveAndFlush(casesStatistics);

        // Get all the casesStatisticsList where date in DEFAULT_DATE or UPDATED_DATE
        defaultCasesStatisticsShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the casesStatisticsList where date equals to UPDATED_DATE
        defaultCasesStatisticsShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllCasesStatisticsByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        casesStatisticsRepository.saveAndFlush(casesStatistics);

        // Get all the casesStatisticsList where date is not null
        defaultCasesStatisticsShouldBeFound("date.specified=true");

        // Get all the casesStatisticsList where date is null
        defaultCasesStatisticsShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    void getAllCasesStatisticsByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        casesStatisticsRepository.saveAndFlush(casesStatistics);

        // Get all the casesStatisticsList where date is greater than or equal to DEFAULT_DATE
        defaultCasesStatisticsShouldBeFound("date.greaterThanOrEqual=" + DEFAULT_DATE);

        // Get all the casesStatisticsList where date is greater than or equal to UPDATED_DATE
        defaultCasesStatisticsShouldNotBeFound("date.greaterThanOrEqual=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllCasesStatisticsByDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        casesStatisticsRepository.saveAndFlush(casesStatistics);

        // Get all the casesStatisticsList where date is less than or equal to DEFAULT_DATE
        defaultCasesStatisticsShouldBeFound("date.lessThanOrEqual=" + DEFAULT_DATE);

        // Get all the casesStatisticsList where date is less than or equal to SMALLER_DATE
        defaultCasesStatisticsShouldNotBeFound("date.lessThanOrEqual=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllCasesStatisticsByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        casesStatisticsRepository.saveAndFlush(casesStatistics);

        // Get all the casesStatisticsList where date is less than DEFAULT_DATE
        defaultCasesStatisticsShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the casesStatisticsList where date is less than UPDATED_DATE
        defaultCasesStatisticsShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllCasesStatisticsByDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        casesStatisticsRepository.saveAndFlush(casesStatistics);

        // Get all the casesStatisticsList where date is greater than DEFAULT_DATE
        defaultCasesStatisticsShouldNotBeFound("date.greaterThan=" + DEFAULT_DATE);

        // Get all the casesStatisticsList where date is greater than SMALLER_DATE
        defaultCasesStatisticsShouldBeFound("date.greaterThan=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllCasesStatisticsByYearIsEqualToSomething() throws Exception {
        // Initialize the database
        casesStatisticsRepository.saveAndFlush(casesStatistics);

        // Get all the casesStatisticsList where year equals to DEFAULT_YEAR
        defaultCasesStatisticsShouldBeFound("year.equals=" + DEFAULT_YEAR);

        // Get all the casesStatisticsList where year equals to UPDATED_YEAR
        defaultCasesStatisticsShouldNotBeFound("year.equals=" + UPDATED_YEAR);
    }

    @Test
    @Transactional
    void getAllCasesStatisticsByYearIsNotEqualToSomething() throws Exception {
        // Initialize the database
        casesStatisticsRepository.saveAndFlush(casesStatistics);

        // Get all the casesStatisticsList where year not equals to DEFAULT_YEAR
        defaultCasesStatisticsShouldNotBeFound("year.notEquals=" + DEFAULT_YEAR);

        // Get all the casesStatisticsList where year not equals to UPDATED_YEAR
        defaultCasesStatisticsShouldBeFound("year.notEquals=" + UPDATED_YEAR);
    }

    @Test
    @Transactional
    void getAllCasesStatisticsByYearIsInShouldWork() throws Exception {
        // Initialize the database
        casesStatisticsRepository.saveAndFlush(casesStatistics);

        // Get all the casesStatisticsList where year in DEFAULT_YEAR or UPDATED_YEAR
        defaultCasesStatisticsShouldBeFound("year.in=" + DEFAULT_YEAR + "," + UPDATED_YEAR);

        // Get all the casesStatisticsList where year equals to UPDATED_YEAR
        defaultCasesStatisticsShouldNotBeFound("year.in=" + UPDATED_YEAR);
    }

    @Test
    @Transactional
    void getAllCasesStatisticsByYearIsNullOrNotNull() throws Exception {
        // Initialize the database
        casesStatisticsRepository.saveAndFlush(casesStatistics);

        // Get all the casesStatisticsList where year is not null
        defaultCasesStatisticsShouldBeFound("year.specified=true");

        // Get all the casesStatisticsList where year is null
        defaultCasesStatisticsShouldNotBeFound("year.specified=false");
    }

    @Test
    @Transactional
    void getAllCasesStatisticsByYearIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        casesStatisticsRepository.saveAndFlush(casesStatistics);

        // Get all the casesStatisticsList where year is greater than or equal to DEFAULT_YEAR
        defaultCasesStatisticsShouldBeFound("year.greaterThanOrEqual=" + DEFAULT_YEAR);

        // Get all the casesStatisticsList where year is greater than or equal to UPDATED_YEAR
        defaultCasesStatisticsShouldNotBeFound("year.greaterThanOrEqual=" + UPDATED_YEAR);
    }

    @Test
    @Transactional
    void getAllCasesStatisticsByYearIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        casesStatisticsRepository.saveAndFlush(casesStatistics);

        // Get all the casesStatisticsList where year is less than or equal to DEFAULT_YEAR
        defaultCasesStatisticsShouldBeFound("year.lessThanOrEqual=" + DEFAULT_YEAR);

        // Get all the casesStatisticsList where year is less than or equal to SMALLER_YEAR
        defaultCasesStatisticsShouldNotBeFound("year.lessThanOrEqual=" + SMALLER_YEAR);
    }

    @Test
    @Transactional
    void getAllCasesStatisticsByYearIsLessThanSomething() throws Exception {
        // Initialize the database
        casesStatisticsRepository.saveAndFlush(casesStatistics);

        // Get all the casesStatisticsList where year is less than DEFAULT_YEAR
        defaultCasesStatisticsShouldNotBeFound("year.lessThan=" + DEFAULT_YEAR);

        // Get all the casesStatisticsList where year is less than UPDATED_YEAR
        defaultCasesStatisticsShouldBeFound("year.lessThan=" + UPDATED_YEAR);
    }

    @Test
    @Transactional
    void getAllCasesStatisticsByYearIsGreaterThanSomething() throws Exception {
        // Initialize the database
        casesStatisticsRepository.saveAndFlush(casesStatistics);

        // Get all the casesStatisticsList where year is greater than DEFAULT_YEAR
        defaultCasesStatisticsShouldNotBeFound("year.greaterThan=" + DEFAULT_YEAR);

        // Get all the casesStatisticsList where year is greater than SMALLER_YEAR
        defaultCasesStatisticsShouldBeFound("year.greaterThan=" + SMALLER_YEAR);
    }

    @Test
    @Transactional
    void getAllCasesStatisticsByMonthIsEqualToSomething() throws Exception {
        // Initialize the database
        casesStatisticsRepository.saveAndFlush(casesStatistics);

        // Get all the casesStatisticsList where month equals to DEFAULT_MONTH
        defaultCasesStatisticsShouldBeFound("month.equals=" + DEFAULT_MONTH);

        // Get all the casesStatisticsList where month equals to UPDATED_MONTH
        defaultCasesStatisticsShouldNotBeFound("month.equals=" + UPDATED_MONTH);
    }

    @Test
    @Transactional
    void getAllCasesStatisticsByMonthIsNotEqualToSomething() throws Exception {
        // Initialize the database
        casesStatisticsRepository.saveAndFlush(casesStatistics);

        // Get all the casesStatisticsList where month not equals to DEFAULT_MONTH
        defaultCasesStatisticsShouldNotBeFound("month.notEquals=" + DEFAULT_MONTH);

        // Get all the casesStatisticsList where month not equals to UPDATED_MONTH
        defaultCasesStatisticsShouldBeFound("month.notEquals=" + UPDATED_MONTH);
    }

    @Test
    @Transactional
    void getAllCasesStatisticsByMonthIsInShouldWork() throws Exception {
        // Initialize the database
        casesStatisticsRepository.saveAndFlush(casesStatistics);

        // Get all the casesStatisticsList where month in DEFAULT_MONTH or UPDATED_MONTH
        defaultCasesStatisticsShouldBeFound("month.in=" + DEFAULT_MONTH + "," + UPDATED_MONTH);

        // Get all the casesStatisticsList where month equals to UPDATED_MONTH
        defaultCasesStatisticsShouldNotBeFound("month.in=" + UPDATED_MONTH);
    }

    @Test
    @Transactional
    void getAllCasesStatisticsByMonthIsNullOrNotNull() throws Exception {
        // Initialize the database
        casesStatisticsRepository.saveAndFlush(casesStatistics);

        // Get all the casesStatisticsList where month is not null
        defaultCasesStatisticsShouldBeFound("month.specified=true");

        // Get all the casesStatisticsList where month is null
        defaultCasesStatisticsShouldNotBeFound("month.specified=false");
    }

    @Test
    @Transactional
    void getAllCasesStatisticsByMonthIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        casesStatisticsRepository.saveAndFlush(casesStatistics);

        // Get all the casesStatisticsList where month is greater than or equal to DEFAULT_MONTH
        defaultCasesStatisticsShouldBeFound("month.greaterThanOrEqual=" + DEFAULT_MONTH);

        // Get all the casesStatisticsList where month is greater than or equal to UPDATED_MONTH
        defaultCasesStatisticsShouldNotBeFound("month.greaterThanOrEqual=" + UPDATED_MONTH);
    }

    @Test
    @Transactional
    void getAllCasesStatisticsByMonthIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        casesStatisticsRepository.saveAndFlush(casesStatistics);

        // Get all the casesStatisticsList where month is less than or equal to DEFAULT_MONTH
        defaultCasesStatisticsShouldBeFound("month.lessThanOrEqual=" + DEFAULT_MONTH);

        // Get all the casesStatisticsList where month is less than or equal to SMALLER_MONTH
        defaultCasesStatisticsShouldNotBeFound("month.lessThanOrEqual=" + SMALLER_MONTH);
    }

    @Test
    @Transactional
    void getAllCasesStatisticsByMonthIsLessThanSomething() throws Exception {
        // Initialize the database
        casesStatisticsRepository.saveAndFlush(casesStatistics);

        // Get all the casesStatisticsList where month is less than DEFAULT_MONTH
        defaultCasesStatisticsShouldNotBeFound("month.lessThan=" + DEFAULT_MONTH);

        // Get all the casesStatisticsList where month is less than UPDATED_MONTH
        defaultCasesStatisticsShouldBeFound("month.lessThan=" + UPDATED_MONTH);
    }

    @Test
    @Transactional
    void getAllCasesStatisticsByMonthIsGreaterThanSomething() throws Exception {
        // Initialize the database
        casesStatisticsRepository.saveAndFlush(casesStatistics);

        // Get all the casesStatisticsList where month is greater than DEFAULT_MONTH
        defaultCasesStatisticsShouldNotBeFound("month.greaterThan=" + DEFAULT_MONTH);

        // Get all the casesStatisticsList where month is greater than SMALLER_MONTH
        defaultCasesStatisticsShouldBeFound("month.greaterThan=" + SMALLER_MONTH);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCasesStatisticsShouldBeFound(String filter) throws Exception {
        restCasesStatisticsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(casesStatistics.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].totalCasesNumber").value(hasItem(DEFAULT_TOTAL_CASES_NUMBER.doubleValue())))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
            .andExpect(jsonPath("$.[*].month").value(hasItem(DEFAULT_MONTH)));

        // Check, that the count call also returns 1
        restCasesStatisticsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCasesStatisticsShouldNotBeFound(String filter) throws Exception {
        restCasesStatisticsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCasesStatisticsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCasesStatistics() throws Exception {
        // Get the casesStatistics
        restCasesStatisticsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCasesStatistics() throws Exception {
        // Initialize the database
        casesStatisticsRepository.saveAndFlush(casesStatistics);

        int databaseSizeBeforeUpdate = casesStatisticsRepository.findAll().size();

        // Update the casesStatistics
        CasesStatistics updatedCasesStatistics = casesStatisticsRepository.findById(casesStatistics.getId()).get();
        // Disconnect from session so that the updates on updatedCasesStatistics are not directly saved in db
        em.detach(updatedCasesStatistics);
        updatedCasesStatistics
            .uuid(UPDATED_UUID)
            .totalCasesNumber(UPDATED_TOTAL_CASES_NUMBER)
            .country(UPDATED_COUNTRY)
            .date(UPDATED_DATE)
            .year(UPDATED_YEAR)
            .month(UPDATED_MONTH);
        CasesStatisticsDTO casesStatisticsDTO = casesStatisticsMapper.toDto(updatedCasesStatistics);

        restCasesStatisticsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, casesStatisticsDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(casesStatisticsDTO))
            )
            .andExpect(status().isOk());

        // Validate the CasesStatistics in the database
        List<CasesStatistics> casesStatisticsList = casesStatisticsRepository.findAll();
        assertThat(casesStatisticsList).hasSize(databaseSizeBeforeUpdate);
        CasesStatistics testCasesStatistics = casesStatisticsList.get(casesStatisticsList.size() - 1);
        assertThat(testCasesStatistics.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testCasesStatistics.getTotalCasesNumber()).isEqualTo(UPDATED_TOTAL_CASES_NUMBER);
        assertThat(testCasesStatistics.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testCasesStatistics.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testCasesStatistics.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testCasesStatistics.getMonth()).isEqualTo(UPDATED_MONTH);
    }

    @Test
    @Transactional
    void putNonExistingCasesStatistics() throws Exception {
        int databaseSizeBeforeUpdate = casesStatisticsRepository.findAll().size();
        casesStatistics.setId(count.incrementAndGet());

        // Create the CasesStatistics
        CasesStatisticsDTO casesStatisticsDTO = casesStatisticsMapper.toDto(casesStatistics);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCasesStatisticsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, casesStatisticsDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(casesStatisticsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CasesStatistics in the database
        List<CasesStatistics> casesStatisticsList = casesStatisticsRepository.findAll();
        assertThat(casesStatisticsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCasesStatistics() throws Exception {
        int databaseSizeBeforeUpdate = casesStatisticsRepository.findAll().size();
        casesStatistics.setId(count.incrementAndGet());

        // Create the CasesStatistics
        CasesStatisticsDTO casesStatisticsDTO = casesStatisticsMapper.toDto(casesStatistics);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCasesStatisticsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(casesStatisticsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CasesStatistics in the database
        List<CasesStatistics> casesStatisticsList = casesStatisticsRepository.findAll();
        assertThat(casesStatisticsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCasesStatistics() throws Exception {
        int databaseSizeBeforeUpdate = casesStatisticsRepository.findAll().size();
        casesStatistics.setId(count.incrementAndGet());

        // Create the CasesStatistics
        CasesStatisticsDTO casesStatisticsDTO = casesStatisticsMapper.toDto(casesStatistics);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCasesStatisticsMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(casesStatisticsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CasesStatistics in the database
        List<CasesStatistics> casesStatisticsList = casesStatisticsRepository.findAll();
        assertThat(casesStatisticsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCasesStatisticsWithPatch() throws Exception {
        // Initialize the database
        casesStatisticsRepository.saveAndFlush(casesStatistics);

        int databaseSizeBeforeUpdate = casesStatisticsRepository.findAll().size();

        // Update the casesStatistics using partial update
        CasesStatistics partialUpdatedCasesStatistics = new CasesStatistics();
        partialUpdatedCasesStatistics.setId(casesStatistics.getId());

        partialUpdatedCasesStatistics.uuid(UPDATED_UUID).totalCasesNumber(UPDATED_TOTAL_CASES_NUMBER).country(UPDATED_COUNTRY);

        restCasesStatisticsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCasesStatistics.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCasesStatistics))
            )
            .andExpect(status().isOk());

        // Validate the CasesStatistics in the database
        List<CasesStatistics> casesStatisticsList = casesStatisticsRepository.findAll();
        assertThat(casesStatisticsList).hasSize(databaseSizeBeforeUpdate);
        CasesStatistics testCasesStatistics = casesStatisticsList.get(casesStatisticsList.size() - 1);
        assertThat(testCasesStatistics.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testCasesStatistics.getTotalCasesNumber()).isEqualTo(UPDATED_TOTAL_CASES_NUMBER);
        assertThat(testCasesStatistics.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testCasesStatistics.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testCasesStatistics.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testCasesStatistics.getMonth()).isEqualTo(DEFAULT_MONTH);
    }

    @Test
    @Transactional
    void fullUpdateCasesStatisticsWithPatch() throws Exception {
        // Initialize the database
        casesStatisticsRepository.saveAndFlush(casesStatistics);

        int databaseSizeBeforeUpdate = casesStatisticsRepository.findAll().size();

        // Update the casesStatistics using partial update
        CasesStatistics partialUpdatedCasesStatistics = new CasesStatistics();
        partialUpdatedCasesStatistics.setId(casesStatistics.getId());

        partialUpdatedCasesStatistics
            .uuid(UPDATED_UUID)
            .totalCasesNumber(UPDATED_TOTAL_CASES_NUMBER)
            .country(UPDATED_COUNTRY)
            .date(UPDATED_DATE)
            .year(UPDATED_YEAR)
            .month(UPDATED_MONTH);

        restCasesStatisticsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCasesStatistics.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCasesStatistics))
            )
            .andExpect(status().isOk());

        // Validate the CasesStatistics in the database
        List<CasesStatistics> casesStatisticsList = casesStatisticsRepository.findAll();
        assertThat(casesStatisticsList).hasSize(databaseSizeBeforeUpdate);
        CasesStatistics testCasesStatistics = casesStatisticsList.get(casesStatisticsList.size() - 1);
        assertThat(testCasesStatistics.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testCasesStatistics.getTotalCasesNumber()).isEqualTo(UPDATED_TOTAL_CASES_NUMBER);
        assertThat(testCasesStatistics.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testCasesStatistics.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testCasesStatistics.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testCasesStatistics.getMonth()).isEqualTo(UPDATED_MONTH);
    }

    @Test
    @Transactional
    void patchNonExistingCasesStatistics() throws Exception {
        int databaseSizeBeforeUpdate = casesStatisticsRepository.findAll().size();
        casesStatistics.setId(count.incrementAndGet());

        // Create the CasesStatistics
        CasesStatisticsDTO casesStatisticsDTO = casesStatisticsMapper.toDto(casesStatistics);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCasesStatisticsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, casesStatisticsDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(casesStatisticsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CasesStatistics in the database
        List<CasesStatistics> casesStatisticsList = casesStatisticsRepository.findAll();
        assertThat(casesStatisticsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCasesStatistics() throws Exception {
        int databaseSizeBeforeUpdate = casesStatisticsRepository.findAll().size();
        casesStatistics.setId(count.incrementAndGet());

        // Create the CasesStatistics
        CasesStatisticsDTO casesStatisticsDTO = casesStatisticsMapper.toDto(casesStatistics);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCasesStatisticsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(casesStatisticsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CasesStatistics in the database
        List<CasesStatistics> casesStatisticsList = casesStatisticsRepository.findAll();
        assertThat(casesStatisticsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCasesStatistics() throws Exception {
        int databaseSizeBeforeUpdate = casesStatisticsRepository.findAll().size();
        casesStatistics.setId(count.incrementAndGet());

        // Create the CasesStatistics
        CasesStatisticsDTO casesStatisticsDTO = casesStatisticsMapper.toDto(casesStatistics);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCasesStatisticsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(casesStatisticsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CasesStatistics in the database
        List<CasesStatistics> casesStatisticsList = casesStatisticsRepository.findAll();
        assertThat(casesStatisticsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCasesStatistics() throws Exception {
        // Initialize the database
        casesStatisticsRepository.saveAndFlush(casesStatistics);

        int databaseSizeBeforeDelete = casesStatisticsRepository.findAll().size();

        // Delete the casesStatistics
        restCasesStatisticsMockMvc
            .perform(delete(ENTITY_API_URL_ID, casesStatistics.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CasesStatistics> casesStatisticsList = casesStatisticsRepository.findAll();
        assertThat(casesStatisticsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
