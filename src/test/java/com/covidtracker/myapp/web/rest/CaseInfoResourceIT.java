package com.covidtracker.myapp.web.rest;

import static com.covidtracker.myapp.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.covidtracker.myapp.IntegrationTest;
import com.covidtracker.myapp.domain.CaseInfo;
import com.covidtracker.myapp.domain.enumeration.EnumCountry;
import com.covidtracker.myapp.domain.enumeration.EnumTestResult;
import com.covidtracker.myapp.repository.CaseInfoRepository;
import com.covidtracker.myapp.service.criteria.CaseInfoCriteria;
import com.covidtracker.myapp.service.dto.CaseInfoDTO;
import com.covidtracker.myapp.service.mapper.CaseInfoMapper;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
 * Integration tests for the {@link CaseInfoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CaseInfoResourceIT {

    private static final UUID DEFAULT_UUID = UUID.randomUUID();
    private static final UUID UPDATED_UUID = UUID.randomUUID();

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_BIRTHDAY = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BIRTHDAY = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_BIRTHDAY = LocalDate.ofEpochDay(-1L);

    private static final EnumTestResult DEFAULT_TEST_RESULT = EnumTestResult.POSITIVE;
    private static final EnumTestResult UPDATED_TEST_RESULT = EnumTestResult.NEGATIVE;

    private static final ZonedDateTime DEFAULT_TEST_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TEST_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_TEST_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final EnumCountry DEFAULT_COUNTRY = EnumCountry.TUNISIA;
    private static final EnumCountry UPDATED_COUNTRY = EnumCountry.ALGERIA;

    private static final String DEFAULT_ADRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADRESS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/case-infos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CaseInfoRepository caseInfoRepository;

    @Autowired
    private CaseInfoMapper caseInfoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCaseInfoMockMvc;

    private CaseInfo caseInfo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CaseInfo createEntity(EntityManager em) {
        CaseInfo caseInfo = new CaseInfo()
            .uuid(DEFAULT_UUID)
            .name(DEFAULT_NAME)
            .birthday(DEFAULT_BIRTHDAY)
            .testResult(DEFAULT_TEST_RESULT)
            .testDate(DEFAULT_TEST_DATE)
            .country(DEFAULT_COUNTRY)
            .adress(DEFAULT_ADRESS);
        return caseInfo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CaseInfo createUpdatedEntity(EntityManager em) {
        CaseInfo caseInfo = new CaseInfo()
            .uuid(UPDATED_UUID)
            .name(UPDATED_NAME)
            .birthday(UPDATED_BIRTHDAY)
            .testResult(UPDATED_TEST_RESULT)
            .testDate(UPDATED_TEST_DATE)
            .country(UPDATED_COUNTRY)
            .adress(UPDATED_ADRESS);
        return caseInfo;
    }

    @BeforeEach
    public void initTest() {
        caseInfo = createEntity(em);
    }

    @Test
    @Transactional
    void createCaseInfo() throws Exception {
        int databaseSizeBeforeCreate = caseInfoRepository.findAll().size();
        // Create the CaseInfo
        CaseInfoDTO caseInfoDTO = caseInfoMapper.toDto(caseInfo);
        restCaseInfoMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(caseInfoDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CaseInfo in the database
        List<CaseInfo> caseInfoList = caseInfoRepository.findAll();
        assertThat(caseInfoList).hasSize(databaseSizeBeforeCreate + 1);
        CaseInfo testCaseInfo = caseInfoList.get(caseInfoList.size() - 1);
        assertThat(testCaseInfo.getUuid()).isEqualTo(DEFAULT_UUID);
        assertThat(testCaseInfo.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCaseInfo.getBirthday()).isEqualTo(DEFAULT_BIRTHDAY);
        assertThat(testCaseInfo.getTestResult()).isEqualTo(DEFAULT_TEST_RESULT);
        assertThat(testCaseInfo.getTestDate()).isEqualTo(DEFAULT_TEST_DATE);
        assertThat(testCaseInfo.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testCaseInfo.getAdress()).isEqualTo(DEFAULT_ADRESS);
    }

    @Test
    @Transactional
    void createCaseInfoWithExistingId() throws Exception {
        // Create the CaseInfo with an existing ID
        caseInfo.setId(1L);
        CaseInfoDTO caseInfoDTO = caseInfoMapper.toDto(caseInfo);

        int databaseSizeBeforeCreate = caseInfoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCaseInfoMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(caseInfoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CaseInfo in the database
        List<CaseInfo> caseInfoList = caseInfoRepository.findAll();
        assertThat(caseInfoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUuidIsRequired() throws Exception {
        int databaseSizeBeforeTest = caseInfoRepository.findAll().size();
        // set the field null
        caseInfo.setUuid(null);

        // Create the CaseInfo, which fails.
        CaseInfoDTO caseInfoDTO = caseInfoMapper.toDto(caseInfo);

        restCaseInfoMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(caseInfoDTO))
            )
            .andExpect(status().isBadRequest());

        List<CaseInfo> caseInfoList = caseInfoRepository.findAll();
        assertThat(caseInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = caseInfoRepository.findAll().size();
        // set the field null
        caseInfo.setName(null);

        // Create the CaseInfo, which fails.
        CaseInfoDTO caseInfoDTO = caseInfoMapper.toDto(caseInfo);

        restCaseInfoMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(caseInfoDTO))
            )
            .andExpect(status().isBadRequest());

        List<CaseInfo> caseInfoList = caseInfoRepository.findAll();
        assertThat(caseInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTestResultIsRequired() throws Exception {
        int databaseSizeBeforeTest = caseInfoRepository.findAll().size();
        // set the field null
        caseInfo.setTestResult(null);

        // Create the CaseInfo, which fails.
        CaseInfoDTO caseInfoDTO = caseInfoMapper.toDto(caseInfo);

        restCaseInfoMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(caseInfoDTO))
            )
            .andExpect(status().isBadRequest());

        List<CaseInfo> caseInfoList = caseInfoRepository.findAll();
        assertThat(caseInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCountryIsRequired() throws Exception {
        int databaseSizeBeforeTest = caseInfoRepository.findAll().size();
        // set the field null
        caseInfo.setCountry(null);

        // Create the CaseInfo, which fails.
        CaseInfoDTO caseInfoDTO = caseInfoMapper.toDto(caseInfo);

        restCaseInfoMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(caseInfoDTO))
            )
            .andExpect(status().isBadRequest());

        List<CaseInfo> caseInfoList = caseInfoRepository.findAll();
        assertThat(caseInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCaseInfos() throws Exception {
        // Initialize the database
        caseInfoRepository.saveAndFlush(caseInfo);

        // Get all the caseInfoList
        restCaseInfoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(caseInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].birthday").value(hasItem(DEFAULT_BIRTHDAY.toString())))
            .andExpect(jsonPath("$.[*].testResult").value(hasItem(DEFAULT_TEST_RESULT.toString())))
            .andExpect(jsonPath("$.[*].testDate").value(hasItem(sameInstant(DEFAULT_TEST_DATE))))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY.toString())))
            .andExpect(jsonPath("$.[*].adress").value(hasItem(DEFAULT_ADRESS)));
    }

    @Test
    @Transactional
    void getCaseInfo() throws Exception {
        // Initialize the database
        caseInfoRepository.saveAndFlush(caseInfo);

        // Get the caseInfo
        restCaseInfoMockMvc
            .perform(get(ENTITY_API_URL_ID, caseInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(caseInfo.getId().intValue()))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.birthday").value(DEFAULT_BIRTHDAY.toString()))
            .andExpect(jsonPath("$.testResult").value(DEFAULT_TEST_RESULT.toString()))
            .andExpect(jsonPath("$.testDate").value(sameInstant(DEFAULT_TEST_DATE)))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY.toString()))
            .andExpect(jsonPath("$.adress").value(DEFAULT_ADRESS));
    }

    @Test
    @Transactional
    void getCaseInfosByIdFiltering() throws Exception {
        // Initialize the database
        caseInfoRepository.saveAndFlush(caseInfo);

        Long id = caseInfo.getId();

        defaultCaseInfoShouldBeFound("id.equals=" + id);
        defaultCaseInfoShouldNotBeFound("id.notEquals=" + id);

        defaultCaseInfoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCaseInfoShouldNotBeFound("id.greaterThan=" + id);

        defaultCaseInfoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCaseInfoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCaseInfosByUuidIsEqualToSomething() throws Exception {
        // Initialize the database
        caseInfoRepository.saveAndFlush(caseInfo);

        // Get all the caseInfoList where uuid equals to DEFAULT_UUID
        defaultCaseInfoShouldBeFound("uuid.equals=" + DEFAULT_UUID);

        // Get all the caseInfoList where uuid equals to UPDATED_UUID
        defaultCaseInfoShouldNotBeFound("uuid.equals=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllCaseInfosByUuidIsNotEqualToSomething() throws Exception {
        // Initialize the database
        caseInfoRepository.saveAndFlush(caseInfo);

        // Get all the caseInfoList where uuid not equals to DEFAULT_UUID
        defaultCaseInfoShouldNotBeFound("uuid.notEquals=" + DEFAULT_UUID);

        // Get all the caseInfoList where uuid not equals to UPDATED_UUID
        defaultCaseInfoShouldBeFound("uuid.notEquals=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllCaseInfosByUuidIsInShouldWork() throws Exception {
        // Initialize the database
        caseInfoRepository.saveAndFlush(caseInfo);

        // Get all the caseInfoList where uuid in DEFAULT_UUID or UPDATED_UUID
        defaultCaseInfoShouldBeFound("uuid.in=" + DEFAULT_UUID + "," + UPDATED_UUID);

        // Get all the caseInfoList where uuid equals to UPDATED_UUID
        defaultCaseInfoShouldNotBeFound("uuid.in=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllCaseInfosByUuidIsNullOrNotNull() throws Exception {
        // Initialize the database
        caseInfoRepository.saveAndFlush(caseInfo);

        // Get all the caseInfoList where uuid is not null
        defaultCaseInfoShouldBeFound("uuid.specified=true");

        // Get all the caseInfoList where uuid is null
        defaultCaseInfoShouldNotBeFound("uuid.specified=false");
    }

    @Test
    @Transactional
    void getAllCaseInfosByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        caseInfoRepository.saveAndFlush(caseInfo);

        // Get all the caseInfoList where name equals to DEFAULT_NAME
        defaultCaseInfoShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the caseInfoList where name equals to UPDATED_NAME
        defaultCaseInfoShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCaseInfosByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        caseInfoRepository.saveAndFlush(caseInfo);

        // Get all the caseInfoList where name not equals to DEFAULT_NAME
        defaultCaseInfoShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the caseInfoList where name not equals to UPDATED_NAME
        defaultCaseInfoShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCaseInfosByNameIsInShouldWork() throws Exception {
        // Initialize the database
        caseInfoRepository.saveAndFlush(caseInfo);

        // Get all the caseInfoList where name in DEFAULT_NAME or UPDATED_NAME
        defaultCaseInfoShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the caseInfoList where name equals to UPDATED_NAME
        defaultCaseInfoShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCaseInfosByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        caseInfoRepository.saveAndFlush(caseInfo);

        // Get all the caseInfoList where name is not null
        defaultCaseInfoShouldBeFound("name.specified=true");

        // Get all the caseInfoList where name is null
        defaultCaseInfoShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllCaseInfosByNameContainsSomething() throws Exception {
        // Initialize the database
        caseInfoRepository.saveAndFlush(caseInfo);

        // Get all the caseInfoList where name contains DEFAULT_NAME
        defaultCaseInfoShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the caseInfoList where name contains UPDATED_NAME
        defaultCaseInfoShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCaseInfosByNameNotContainsSomething() throws Exception {
        // Initialize the database
        caseInfoRepository.saveAndFlush(caseInfo);

        // Get all the caseInfoList where name does not contain DEFAULT_NAME
        defaultCaseInfoShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the caseInfoList where name does not contain UPDATED_NAME
        defaultCaseInfoShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCaseInfosByBirthdayIsEqualToSomething() throws Exception {
        // Initialize the database
        caseInfoRepository.saveAndFlush(caseInfo);

        // Get all the caseInfoList where birthday equals to DEFAULT_BIRTHDAY
        defaultCaseInfoShouldBeFound("birthday.equals=" + DEFAULT_BIRTHDAY);

        // Get all the caseInfoList where birthday equals to UPDATED_BIRTHDAY
        defaultCaseInfoShouldNotBeFound("birthday.equals=" + UPDATED_BIRTHDAY);
    }

    @Test
    @Transactional
    void getAllCaseInfosByBirthdayIsNotEqualToSomething() throws Exception {
        // Initialize the database
        caseInfoRepository.saveAndFlush(caseInfo);

        // Get all the caseInfoList where birthday not equals to DEFAULT_BIRTHDAY
        defaultCaseInfoShouldNotBeFound("birthday.notEquals=" + DEFAULT_BIRTHDAY);

        // Get all the caseInfoList where birthday not equals to UPDATED_BIRTHDAY
        defaultCaseInfoShouldBeFound("birthday.notEquals=" + UPDATED_BIRTHDAY);
    }

    @Test
    @Transactional
    void getAllCaseInfosByBirthdayIsInShouldWork() throws Exception {
        // Initialize the database
        caseInfoRepository.saveAndFlush(caseInfo);

        // Get all the caseInfoList where birthday in DEFAULT_BIRTHDAY or UPDATED_BIRTHDAY
        defaultCaseInfoShouldBeFound("birthday.in=" + DEFAULT_BIRTHDAY + "," + UPDATED_BIRTHDAY);

        // Get all the caseInfoList where birthday equals to UPDATED_BIRTHDAY
        defaultCaseInfoShouldNotBeFound("birthday.in=" + UPDATED_BIRTHDAY);
    }

    @Test
    @Transactional
    void getAllCaseInfosByBirthdayIsNullOrNotNull() throws Exception {
        // Initialize the database
        caseInfoRepository.saveAndFlush(caseInfo);

        // Get all the caseInfoList where birthday is not null
        defaultCaseInfoShouldBeFound("birthday.specified=true");

        // Get all the caseInfoList where birthday is null
        defaultCaseInfoShouldNotBeFound("birthday.specified=false");
    }

    @Test
    @Transactional
    void getAllCaseInfosByBirthdayIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        caseInfoRepository.saveAndFlush(caseInfo);

        // Get all the caseInfoList where birthday is greater than or equal to DEFAULT_BIRTHDAY
        defaultCaseInfoShouldBeFound("birthday.greaterThanOrEqual=" + DEFAULT_BIRTHDAY);

        // Get all the caseInfoList where birthday is greater than or equal to UPDATED_BIRTHDAY
        defaultCaseInfoShouldNotBeFound("birthday.greaterThanOrEqual=" + UPDATED_BIRTHDAY);
    }

    @Test
    @Transactional
    void getAllCaseInfosByBirthdayIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        caseInfoRepository.saveAndFlush(caseInfo);

        // Get all the caseInfoList where birthday is less than or equal to DEFAULT_BIRTHDAY
        defaultCaseInfoShouldBeFound("birthday.lessThanOrEqual=" + DEFAULT_BIRTHDAY);

        // Get all the caseInfoList where birthday is less than or equal to SMALLER_BIRTHDAY
        defaultCaseInfoShouldNotBeFound("birthday.lessThanOrEqual=" + SMALLER_BIRTHDAY);
    }

    @Test
    @Transactional
    void getAllCaseInfosByBirthdayIsLessThanSomething() throws Exception {
        // Initialize the database
        caseInfoRepository.saveAndFlush(caseInfo);

        // Get all the caseInfoList where birthday is less than DEFAULT_BIRTHDAY
        defaultCaseInfoShouldNotBeFound("birthday.lessThan=" + DEFAULT_BIRTHDAY);

        // Get all the caseInfoList where birthday is less than UPDATED_BIRTHDAY
        defaultCaseInfoShouldBeFound("birthday.lessThan=" + UPDATED_BIRTHDAY);
    }

    @Test
    @Transactional
    void getAllCaseInfosByBirthdayIsGreaterThanSomething() throws Exception {
        // Initialize the database
        caseInfoRepository.saveAndFlush(caseInfo);

        // Get all the caseInfoList where birthday is greater than DEFAULT_BIRTHDAY
        defaultCaseInfoShouldNotBeFound("birthday.greaterThan=" + DEFAULT_BIRTHDAY);

        // Get all the caseInfoList where birthday is greater than SMALLER_BIRTHDAY
        defaultCaseInfoShouldBeFound("birthday.greaterThan=" + SMALLER_BIRTHDAY);
    }

    @Test
    @Transactional
    void getAllCaseInfosByTestResultIsEqualToSomething() throws Exception {
        // Initialize the database
        caseInfoRepository.saveAndFlush(caseInfo);

        // Get all the caseInfoList where testResult equals to DEFAULT_TEST_RESULT
        defaultCaseInfoShouldBeFound("testResult.equals=" + DEFAULT_TEST_RESULT);

        // Get all the caseInfoList where testResult equals to UPDATED_TEST_RESULT
        defaultCaseInfoShouldNotBeFound("testResult.equals=" + UPDATED_TEST_RESULT);
    }

    @Test
    @Transactional
    void getAllCaseInfosByTestResultIsNotEqualToSomething() throws Exception {
        // Initialize the database
        caseInfoRepository.saveAndFlush(caseInfo);

        // Get all the caseInfoList where testResult not equals to DEFAULT_TEST_RESULT
        defaultCaseInfoShouldNotBeFound("testResult.notEquals=" + DEFAULT_TEST_RESULT);

        // Get all the caseInfoList where testResult not equals to UPDATED_TEST_RESULT
        defaultCaseInfoShouldBeFound("testResult.notEquals=" + UPDATED_TEST_RESULT);
    }

    @Test
    @Transactional
    void getAllCaseInfosByTestResultIsInShouldWork() throws Exception {
        // Initialize the database
        caseInfoRepository.saveAndFlush(caseInfo);

        // Get all the caseInfoList where testResult in DEFAULT_TEST_RESULT or UPDATED_TEST_RESULT
        defaultCaseInfoShouldBeFound("testResult.in=" + DEFAULT_TEST_RESULT + "," + UPDATED_TEST_RESULT);

        // Get all the caseInfoList where testResult equals to UPDATED_TEST_RESULT
        defaultCaseInfoShouldNotBeFound("testResult.in=" + UPDATED_TEST_RESULT);
    }

    @Test
    @Transactional
    void getAllCaseInfosByTestResultIsNullOrNotNull() throws Exception {
        // Initialize the database
        caseInfoRepository.saveAndFlush(caseInfo);

        // Get all the caseInfoList where testResult is not null
        defaultCaseInfoShouldBeFound("testResult.specified=true");

        // Get all the caseInfoList where testResult is null
        defaultCaseInfoShouldNotBeFound("testResult.specified=false");
    }

    @Test
    @Transactional
    void getAllCaseInfosByTestDateIsEqualToSomething() throws Exception {
        // Initialize the database
        caseInfoRepository.saveAndFlush(caseInfo);

        // Get all the caseInfoList where testDate equals to DEFAULT_TEST_DATE
        defaultCaseInfoShouldBeFound("testDate.equals=" + DEFAULT_TEST_DATE);

        // Get all the caseInfoList where testDate equals to UPDATED_TEST_DATE
        defaultCaseInfoShouldNotBeFound("testDate.equals=" + UPDATED_TEST_DATE);
    }

    @Test
    @Transactional
    void getAllCaseInfosByTestDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        caseInfoRepository.saveAndFlush(caseInfo);

        // Get all the caseInfoList where testDate not equals to DEFAULT_TEST_DATE
        defaultCaseInfoShouldNotBeFound("testDate.notEquals=" + DEFAULT_TEST_DATE);

        // Get all the caseInfoList where testDate not equals to UPDATED_TEST_DATE
        defaultCaseInfoShouldBeFound("testDate.notEquals=" + UPDATED_TEST_DATE);
    }

    @Test
    @Transactional
    void getAllCaseInfosByTestDateIsInShouldWork() throws Exception {
        // Initialize the database
        caseInfoRepository.saveAndFlush(caseInfo);

        // Get all the caseInfoList where testDate in DEFAULT_TEST_DATE or UPDATED_TEST_DATE
        defaultCaseInfoShouldBeFound("testDate.in=" + DEFAULT_TEST_DATE + "," + UPDATED_TEST_DATE);

        // Get all the caseInfoList where testDate equals to UPDATED_TEST_DATE
        defaultCaseInfoShouldNotBeFound("testDate.in=" + UPDATED_TEST_DATE);
    }

    @Test
    @Transactional
    void getAllCaseInfosByTestDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        caseInfoRepository.saveAndFlush(caseInfo);

        // Get all the caseInfoList where testDate is not null
        defaultCaseInfoShouldBeFound("testDate.specified=true");

        // Get all the caseInfoList where testDate is null
        defaultCaseInfoShouldNotBeFound("testDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCaseInfosByTestDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        caseInfoRepository.saveAndFlush(caseInfo);

        // Get all the caseInfoList where testDate is greater than or equal to DEFAULT_TEST_DATE
        defaultCaseInfoShouldBeFound("testDate.greaterThanOrEqual=" + DEFAULT_TEST_DATE);

        // Get all the caseInfoList where testDate is greater than or equal to UPDATED_TEST_DATE
        defaultCaseInfoShouldNotBeFound("testDate.greaterThanOrEqual=" + UPDATED_TEST_DATE);
    }

    @Test
    @Transactional
    void getAllCaseInfosByTestDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        caseInfoRepository.saveAndFlush(caseInfo);

        // Get all the caseInfoList where testDate is less than or equal to DEFAULT_TEST_DATE
        defaultCaseInfoShouldBeFound("testDate.lessThanOrEqual=" + DEFAULT_TEST_DATE);

        // Get all the caseInfoList where testDate is less than or equal to SMALLER_TEST_DATE
        defaultCaseInfoShouldNotBeFound("testDate.lessThanOrEqual=" + SMALLER_TEST_DATE);
    }

    @Test
    @Transactional
    void getAllCaseInfosByTestDateIsLessThanSomething() throws Exception {
        // Initialize the database
        caseInfoRepository.saveAndFlush(caseInfo);

        // Get all the caseInfoList where testDate is less than DEFAULT_TEST_DATE
        defaultCaseInfoShouldNotBeFound("testDate.lessThan=" + DEFAULT_TEST_DATE);

        // Get all the caseInfoList where testDate is less than UPDATED_TEST_DATE
        defaultCaseInfoShouldBeFound("testDate.lessThan=" + UPDATED_TEST_DATE);
    }

    @Test
    @Transactional
    void getAllCaseInfosByTestDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        caseInfoRepository.saveAndFlush(caseInfo);

        // Get all the caseInfoList where testDate is greater than DEFAULT_TEST_DATE
        defaultCaseInfoShouldNotBeFound("testDate.greaterThan=" + DEFAULT_TEST_DATE);

        // Get all the caseInfoList where testDate is greater than SMALLER_TEST_DATE
        defaultCaseInfoShouldBeFound("testDate.greaterThan=" + SMALLER_TEST_DATE);
    }

    @Test
    @Transactional
    void getAllCaseInfosByCountryIsEqualToSomething() throws Exception {
        // Initialize the database
        caseInfoRepository.saveAndFlush(caseInfo);

        // Get all the caseInfoList where country equals to DEFAULT_COUNTRY
        defaultCaseInfoShouldBeFound("country.equals=" + DEFAULT_COUNTRY);

        // Get all the caseInfoList where country equals to UPDATED_COUNTRY
        defaultCaseInfoShouldNotBeFound("country.equals=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllCaseInfosByCountryIsNotEqualToSomething() throws Exception {
        // Initialize the database
        caseInfoRepository.saveAndFlush(caseInfo);

        // Get all the caseInfoList where country not equals to DEFAULT_COUNTRY
        defaultCaseInfoShouldNotBeFound("country.notEquals=" + DEFAULT_COUNTRY);

        // Get all the caseInfoList where country not equals to UPDATED_COUNTRY
        defaultCaseInfoShouldBeFound("country.notEquals=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllCaseInfosByCountryIsInShouldWork() throws Exception {
        // Initialize the database
        caseInfoRepository.saveAndFlush(caseInfo);

        // Get all the caseInfoList where country in DEFAULT_COUNTRY or UPDATED_COUNTRY
        defaultCaseInfoShouldBeFound("country.in=" + DEFAULT_COUNTRY + "," + UPDATED_COUNTRY);

        // Get all the caseInfoList where country equals to UPDATED_COUNTRY
        defaultCaseInfoShouldNotBeFound("country.in=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllCaseInfosByCountryIsNullOrNotNull() throws Exception {
        // Initialize the database
        caseInfoRepository.saveAndFlush(caseInfo);

        // Get all the caseInfoList where country is not null
        defaultCaseInfoShouldBeFound("country.specified=true");

        // Get all the caseInfoList where country is null
        defaultCaseInfoShouldNotBeFound("country.specified=false");
    }

    @Test
    @Transactional
    void getAllCaseInfosByAdressIsEqualToSomething() throws Exception {
        // Initialize the database
        caseInfoRepository.saveAndFlush(caseInfo);

        // Get all the caseInfoList where adress equals to DEFAULT_ADRESS
        defaultCaseInfoShouldBeFound("adress.equals=" + DEFAULT_ADRESS);

        // Get all the caseInfoList where adress equals to UPDATED_ADRESS
        defaultCaseInfoShouldNotBeFound("adress.equals=" + UPDATED_ADRESS);
    }

    @Test
    @Transactional
    void getAllCaseInfosByAdressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        caseInfoRepository.saveAndFlush(caseInfo);

        // Get all the caseInfoList where adress not equals to DEFAULT_ADRESS
        defaultCaseInfoShouldNotBeFound("adress.notEquals=" + DEFAULT_ADRESS);

        // Get all the caseInfoList where adress not equals to UPDATED_ADRESS
        defaultCaseInfoShouldBeFound("adress.notEquals=" + UPDATED_ADRESS);
    }

    @Test
    @Transactional
    void getAllCaseInfosByAdressIsInShouldWork() throws Exception {
        // Initialize the database
        caseInfoRepository.saveAndFlush(caseInfo);

        // Get all the caseInfoList where adress in DEFAULT_ADRESS or UPDATED_ADRESS
        defaultCaseInfoShouldBeFound("adress.in=" + DEFAULT_ADRESS + "," + UPDATED_ADRESS);

        // Get all the caseInfoList where adress equals to UPDATED_ADRESS
        defaultCaseInfoShouldNotBeFound("adress.in=" + UPDATED_ADRESS);
    }

    @Test
    @Transactional
    void getAllCaseInfosByAdressIsNullOrNotNull() throws Exception {
        // Initialize the database
        caseInfoRepository.saveAndFlush(caseInfo);

        // Get all the caseInfoList where adress is not null
        defaultCaseInfoShouldBeFound("adress.specified=true");

        // Get all the caseInfoList where adress is null
        defaultCaseInfoShouldNotBeFound("adress.specified=false");
    }

    @Test
    @Transactional
    void getAllCaseInfosByAdressContainsSomething() throws Exception {
        // Initialize the database
        caseInfoRepository.saveAndFlush(caseInfo);

        // Get all the caseInfoList where adress contains DEFAULT_ADRESS
        defaultCaseInfoShouldBeFound("adress.contains=" + DEFAULT_ADRESS);

        // Get all the caseInfoList where adress contains UPDATED_ADRESS
        defaultCaseInfoShouldNotBeFound("adress.contains=" + UPDATED_ADRESS);
    }

    @Test
    @Transactional
    void getAllCaseInfosByAdressNotContainsSomething() throws Exception {
        // Initialize the database
        caseInfoRepository.saveAndFlush(caseInfo);

        // Get all the caseInfoList where adress does not contain DEFAULT_ADRESS
        defaultCaseInfoShouldNotBeFound("adress.doesNotContain=" + DEFAULT_ADRESS);

        // Get all the caseInfoList where adress does not contain UPDATED_ADRESS
        defaultCaseInfoShouldBeFound("adress.doesNotContain=" + UPDATED_ADRESS);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCaseInfoShouldBeFound(String filter) throws Exception {
        restCaseInfoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(caseInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].birthday").value(hasItem(DEFAULT_BIRTHDAY.toString())))
            .andExpect(jsonPath("$.[*].testResult").value(hasItem(DEFAULT_TEST_RESULT.toString())))
            .andExpect(jsonPath("$.[*].testDate").value(hasItem(sameInstant(DEFAULT_TEST_DATE))))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY.toString())))
            .andExpect(jsonPath("$.[*].adress").value(hasItem(DEFAULT_ADRESS)));

        // Check, that the count call also returns 1
        restCaseInfoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCaseInfoShouldNotBeFound(String filter) throws Exception {
        restCaseInfoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCaseInfoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCaseInfo() throws Exception {
        // Get the caseInfo
        restCaseInfoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCaseInfo() throws Exception {
        // Initialize the database
        caseInfoRepository.saveAndFlush(caseInfo);

        int databaseSizeBeforeUpdate = caseInfoRepository.findAll().size();

        // Update the caseInfo
        CaseInfo updatedCaseInfo = caseInfoRepository.findById(caseInfo.getId()).get();
        // Disconnect from session so that the updates on updatedCaseInfo are not directly saved in db
        em.detach(updatedCaseInfo);
        updatedCaseInfo
            .uuid(UPDATED_UUID)
            .name(UPDATED_NAME)
            .birthday(UPDATED_BIRTHDAY)
            .testResult(UPDATED_TEST_RESULT)
            .testDate(UPDATED_TEST_DATE)
            .country(UPDATED_COUNTRY)
            .adress(UPDATED_ADRESS);
        CaseInfoDTO caseInfoDTO = caseInfoMapper.toDto(updatedCaseInfo);

        restCaseInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, caseInfoDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(caseInfoDTO))
            )
            .andExpect(status().isOk());

        // Validate the CaseInfo in the database
        List<CaseInfo> caseInfoList = caseInfoRepository.findAll();
        assertThat(caseInfoList).hasSize(databaseSizeBeforeUpdate);
        CaseInfo testCaseInfo = caseInfoList.get(caseInfoList.size() - 1);
        assertThat(testCaseInfo.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testCaseInfo.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCaseInfo.getBirthday()).isEqualTo(UPDATED_BIRTHDAY);
        assertThat(testCaseInfo.getTestResult()).isEqualTo(UPDATED_TEST_RESULT);
        assertThat(testCaseInfo.getTestDate()).isEqualTo(UPDATED_TEST_DATE);
        assertThat(testCaseInfo.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testCaseInfo.getAdress()).isEqualTo(UPDATED_ADRESS);
    }

    @Test
    @Transactional
    void putNonExistingCaseInfo() throws Exception {
        int databaseSizeBeforeUpdate = caseInfoRepository.findAll().size();
        caseInfo.setId(count.incrementAndGet());

        // Create the CaseInfo
        CaseInfoDTO caseInfoDTO = caseInfoMapper.toDto(caseInfo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCaseInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, caseInfoDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(caseInfoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CaseInfo in the database
        List<CaseInfo> caseInfoList = caseInfoRepository.findAll();
        assertThat(caseInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCaseInfo() throws Exception {
        int databaseSizeBeforeUpdate = caseInfoRepository.findAll().size();
        caseInfo.setId(count.incrementAndGet());

        // Create the CaseInfo
        CaseInfoDTO caseInfoDTO = caseInfoMapper.toDto(caseInfo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCaseInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(caseInfoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CaseInfo in the database
        List<CaseInfo> caseInfoList = caseInfoRepository.findAll();
        assertThat(caseInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCaseInfo() throws Exception {
        int databaseSizeBeforeUpdate = caseInfoRepository.findAll().size();
        caseInfo.setId(count.incrementAndGet());

        // Create the CaseInfo
        CaseInfoDTO caseInfoDTO = caseInfoMapper.toDto(caseInfo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCaseInfoMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(caseInfoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CaseInfo in the database
        List<CaseInfo> caseInfoList = caseInfoRepository.findAll();
        assertThat(caseInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCaseInfoWithPatch() throws Exception {
        // Initialize the database
        caseInfoRepository.saveAndFlush(caseInfo);

        int databaseSizeBeforeUpdate = caseInfoRepository.findAll().size();

        // Update the caseInfo using partial update
        CaseInfo partialUpdatedCaseInfo = new CaseInfo();
        partialUpdatedCaseInfo.setId(caseInfo.getId());

        partialUpdatedCaseInfo.name(UPDATED_NAME).country(UPDATED_COUNTRY);

        restCaseInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCaseInfo.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCaseInfo))
            )
            .andExpect(status().isOk());

        // Validate the CaseInfo in the database
        List<CaseInfo> caseInfoList = caseInfoRepository.findAll();
        assertThat(caseInfoList).hasSize(databaseSizeBeforeUpdate);
        CaseInfo testCaseInfo = caseInfoList.get(caseInfoList.size() - 1);
        assertThat(testCaseInfo.getUuid()).isEqualTo(DEFAULT_UUID);
        assertThat(testCaseInfo.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCaseInfo.getBirthday()).isEqualTo(DEFAULT_BIRTHDAY);
        assertThat(testCaseInfo.getTestResult()).isEqualTo(DEFAULT_TEST_RESULT);
        assertThat(testCaseInfo.getTestDate()).isEqualTo(DEFAULT_TEST_DATE);
        assertThat(testCaseInfo.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testCaseInfo.getAdress()).isEqualTo(DEFAULT_ADRESS);
    }

    @Test
    @Transactional
    void fullUpdateCaseInfoWithPatch() throws Exception {
        // Initialize the database
        caseInfoRepository.saveAndFlush(caseInfo);

        int databaseSizeBeforeUpdate = caseInfoRepository.findAll().size();

        // Update the caseInfo using partial update
        CaseInfo partialUpdatedCaseInfo = new CaseInfo();
        partialUpdatedCaseInfo.setId(caseInfo.getId());

        partialUpdatedCaseInfo
            .uuid(UPDATED_UUID)
            .name(UPDATED_NAME)
            .birthday(UPDATED_BIRTHDAY)
            .testResult(UPDATED_TEST_RESULT)
            .testDate(UPDATED_TEST_DATE)
            .country(UPDATED_COUNTRY)
            .adress(UPDATED_ADRESS);

        restCaseInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCaseInfo.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCaseInfo))
            )
            .andExpect(status().isOk());

        // Validate the CaseInfo in the database
        List<CaseInfo> caseInfoList = caseInfoRepository.findAll();
        assertThat(caseInfoList).hasSize(databaseSizeBeforeUpdate);
        CaseInfo testCaseInfo = caseInfoList.get(caseInfoList.size() - 1);
        assertThat(testCaseInfo.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testCaseInfo.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCaseInfo.getBirthday()).isEqualTo(UPDATED_BIRTHDAY);
        assertThat(testCaseInfo.getTestResult()).isEqualTo(UPDATED_TEST_RESULT);
        assertThat(testCaseInfo.getTestDate()).isEqualTo(UPDATED_TEST_DATE);
        assertThat(testCaseInfo.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testCaseInfo.getAdress()).isEqualTo(UPDATED_ADRESS);
    }

    @Test
    @Transactional
    void patchNonExistingCaseInfo() throws Exception {
        int databaseSizeBeforeUpdate = caseInfoRepository.findAll().size();
        caseInfo.setId(count.incrementAndGet());

        // Create the CaseInfo
        CaseInfoDTO caseInfoDTO = caseInfoMapper.toDto(caseInfo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCaseInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, caseInfoDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(caseInfoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CaseInfo in the database
        List<CaseInfo> caseInfoList = caseInfoRepository.findAll();
        assertThat(caseInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCaseInfo() throws Exception {
        int databaseSizeBeforeUpdate = caseInfoRepository.findAll().size();
        caseInfo.setId(count.incrementAndGet());

        // Create the CaseInfo
        CaseInfoDTO caseInfoDTO = caseInfoMapper.toDto(caseInfo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCaseInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(caseInfoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CaseInfo in the database
        List<CaseInfo> caseInfoList = caseInfoRepository.findAll();
        assertThat(caseInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCaseInfo() throws Exception {
        int databaseSizeBeforeUpdate = caseInfoRepository.findAll().size();
        caseInfo.setId(count.incrementAndGet());

        // Create the CaseInfo
        CaseInfoDTO caseInfoDTO = caseInfoMapper.toDto(caseInfo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCaseInfoMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(caseInfoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CaseInfo in the database
        List<CaseInfo> caseInfoList = caseInfoRepository.findAll();
        assertThat(caseInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCaseInfo() throws Exception {
        // Initialize the database
        caseInfoRepository.saveAndFlush(caseInfo);

        int databaseSizeBeforeDelete = caseInfoRepository.findAll().size();

        // Delete the caseInfo
        restCaseInfoMockMvc
            .perform(delete(ENTITY_API_URL_ID, caseInfo.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CaseInfo> caseInfoList = caseInfoRepository.findAll();
        assertThat(caseInfoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
