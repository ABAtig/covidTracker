package com.covidtracker.myapp.web.rest;

import static com.covidtracker.myapp.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.covidtracker.myapp.IntegrationTest;
import com.covidtracker.myapp.domain.VaccinationInfo;
import com.covidtracker.myapp.domain.enumeration.EnumCountry;
import com.covidtracker.myapp.domain.enumeration.EnumVaccinationNumber;
import com.covidtracker.myapp.repository.VaccinationInfoRepository;
import com.covidtracker.myapp.service.criteria.VaccinationInfoCriteria;
import com.covidtracker.myapp.service.dto.VaccinationInfoDTO;
import com.covidtracker.myapp.service.mapper.VaccinationInfoMapper;
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
 * Integration tests for the {@link VaccinationInfoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VaccinationInfoResourceIT {

    private static final UUID DEFAULT_UUID = UUID.randomUUID();
    private static final UUID UPDATED_UUID = UUID.randomUUID();

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_BIRTHDAY = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BIRTHDAY = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_BIRTHDAY = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_IDENTITY_CARD_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_IDENTITY_CARD_NUMBER = "BBBBBBBBBB";

    private static final EnumVaccinationNumber DEFAULT_VACCINATION_NUMBER = EnumVaccinationNumber.FIRST;
    private static final EnumVaccinationNumber UPDATED_VACCINATION_NUMBER = EnumVaccinationNumber.SECOND;

    private static final ZonedDateTime DEFAULT_FIRST_VACCINATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_FIRST_VACCINATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_FIRST_VACCINATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_SECOND_VACCINATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_SECOND_VACCINATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_SECOND_VACCINATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final EnumCountry DEFAULT_COUNTRY = EnumCountry.TUNISIA;
    private static final EnumCountry UPDATED_COUNTRY = EnumCountry.ALGERIA;

    private static final String DEFAULT_ADRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADRESS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/vaccination-infos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VaccinationInfoRepository vaccinationInfoRepository;

    @Autowired
    private VaccinationInfoMapper vaccinationInfoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVaccinationInfoMockMvc;

    private VaccinationInfo vaccinationInfo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VaccinationInfo createEntity(EntityManager em) {
        VaccinationInfo vaccinationInfo = new VaccinationInfo()
            .uuid(DEFAULT_UUID)
            .name(DEFAULT_NAME)
            .birthday(DEFAULT_BIRTHDAY)
            .identityCardNumber(DEFAULT_IDENTITY_CARD_NUMBER)
            .vaccinationNumber(DEFAULT_VACCINATION_NUMBER)
            .firstVaccinationDate(DEFAULT_FIRST_VACCINATION_DATE)
            .secondVaccinationDate(DEFAULT_SECOND_VACCINATION_DATE)
            .country(DEFAULT_COUNTRY)
            .adress(DEFAULT_ADRESS);
        return vaccinationInfo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VaccinationInfo createUpdatedEntity(EntityManager em) {
        VaccinationInfo vaccinationInfo = new VaccinationInfo()
            .uuid(UPDATED_UUID)
            .name(UPDATED_NAME)
            .birthday(UPDATED_BIRTHDAY)
            .identityCardNumber(UPDATED_IDENTITY_CARD_NUMBER)
            .vaccinationNumber(UPDATED_VACCINATION_NUMBER)
            .firstVaccinationDate(UPDATED_FIRST_VACCINATION_DATE)
            .secondVaccinationDate(UPDATED_SECOND_VACCINATION_DATE)
            .country(UPDATED_COUNTRY)
            .adress(UPDATED_ADRESS);
        return vaccinationInfo;
    }

    @BeforeEach
    public void initTest() {
        vaccinationInfo = createEntity(em);
    }

    @Test
    @Transactional
    void createVaccinationInfo() throws Exception {
        int databaseSizeBeforeCreate = vaccinationInfoRepository.findAll().size();
        // Create the VaccinationInfo
        VaccinationInfoDTO vaccinationInfoDTO = vaccinationInfoMapper.toDto(vaccinationInfo);
        restVaccinationInfoMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vaccinationInfoDTO))
            )
            .andExpect(status().isCreated());

        // Validate the VaccinationInfo in the database
        List<VaccinationInfo> vaccinationInfoList = vaccinationInfoRepository.findAll();
        assertThat(vaccinationInfoList).hasSize(databaseSizeBeforeCreate + 1);
        VaccinationInfo testVaccinationInfo = vaccinationInfoList.get(vaccinationInfoList.size() - 1);
        assertThat(testVaccinationInfo.getUuid()).isEqualTo(DEFAULT_UUID);
        assertThat(testVaccinationInfo.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testVaccinationInfo.getBirthday()).isEqualTo(DEFAULT_BIRTHDAY);
        assertThat(testVaccinationInfo.getIdentityCardNumber()).isEqualTo(DEFAULT_IDENTITY_CARD_NUMBER);
        assertThat(testVaccinationInfo.getVaccinationNumber()).isEqualTo(DEFAULT_VACCINATION_NUMBER);
        assertThat(testVaccinationInfo.getFirstVaccinationDate()).isEqualTo(DEFAULT_FIRST_VACCINATION_DATE);
        assertThat(testVaccinationInfo.getSecondVaccinationDate()).isEqualTo(DEFAULT_SECOND_VACCINATION_DATE);
        assertThat(testVaccinationInfo.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testVaccinationInfo.getAdress()).isEqualTo(DEFAULT_ADRESS);
    }

    @Test
    @Transactional
    void createVaccinationInfoWithExistingId() throws Exception {
        // Create the VaccinationInfo with an existing ID
        vaccinationInfo.setId(1L);
        VaccinationInfoDTO vaccinationInfoDTO = vaccinationInfoMapper.toDto(vaccinationInfo);

        int databaseSizeBeforeCreate = vaccinationInfoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVaccinationInfoMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vaccinationInfoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VaccinationInfo in the database
        List<VaccinationInfo> vaccinationInfoList = vaccinationInfoRepository.findAll();
        assertThat(vaccinationInfoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUuidIsRequired() throws Exception {
        int databaseSizeBeforeTest = vaccinationInfoRepository.findAll().size();
        // set the field null
        vaccinationInfo.setUuid(null);

        // Create the VaccinationInfo, which fails.
        VaccinationInfoDTO vaccinationInfoDTO = vaccinationInfoMapper.toDto(vaccinationInfo);

        restVaccinationInfoMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vaccinationInfoDTO))
            )
            .andExpect(status().isBadRequest());

        List<VaccinationInfo> vaccinationInfoList = vaccinationInfoRepository.findAll();
        assertThat(vaccinationInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = vaccinationInfoRepository.findAll().size();
        // set the field null
        vaccinationInfo.setName(null);

        // Create the VaccinationInfo, which fails.
        VaccinationInfoDTO vaccinationInfoDTO = vaccinationInfoMapper.toDto(vaccinationInfo);

        restVaccinationInfoMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vaccinationInfoDTO))
            )
            .andExpect(status().isBadRequest());

        List<VaccinationInfo> vaccinationInfoList = vaccinationInfoRepository.findAll();
        assertThat(vaccinationInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVaccinationNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = vaccinationInfoRepository.findAll().size();
        // set the field null
        vaccinationInfo.setVaccinationNumber(null);

        // Create the VaccinationInfo, which fails.
        VaccinationInfoDTO vaccinationInfoDTO = vaccinationInfoMapper.toDto(vaccinationInfo);

        restVaccinationInfoMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vaccinationInfoDTO))
            )
            .andExpect(status().isBadRequest());

        List<VaccinationInfo> vaccinationInfoList = vaccinationInfoRepository.findAll();
        assertThat(vaccinationInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFirstVaccinationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = vaccinationInfoRepository.findAll().size();
        // set the field null
        vaccinationInfo.setFirstVaccinationDate(null);

        // Create the VaccinationInfo, which fails.
        VaccinationInfoDTO vaccinationInfoDTO = vaccinationInfoMapper.toDto(vaccinationInfo);

        restVaccinationInfoMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vaccinationInfoDTO))
            )
            .andExpect(status().isBadRequest());

        List<VaccinationInfo> vaccinationInfoList = vaccinationInfoRepository.findAll();
        assertThat(vaccinationInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCountryIsRequired() throws Exception {
        int databaseSizeBeforeTest = vaccinationInfoRepository.findAll().size();
        // set the field null
        vaccinationInfo.setCountry(null);

        // Create the VaccinationInfo, which fails.
        VaccinationInfoDTO vaccinationInfoDTO = vaccinationInfoMapper.toDto(vaccinationInfo);

        restVaccinationInfoMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vaccinationInfoDTO))
            )
            .andExpect(status().isBadRequest());

        List<VaccinationInfo> vaccinationInfoList = vaccinationInfoRepository.findAll();
        assertThat(vaccinationInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVaccinationInfos() throws Exception {
        // Initialize the database
        vaccinationInfoRepository.saveAndFlush(vaccinationInfo);

        // Get all the vaccinationInfoList
        restVaccinationInfoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vaccinationInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].birthday").value(hasItem(DEFAULT_BIRTHDAY.toString())))
            .andExpect(jsonPath("$.[*].identityCardNumber").value(hasItem(DEFAULT_IDENTITY_CARD_NUMBER)))
            .andExpect(jsonPath("$.[*].vaccinationNumber").value(hasItem(DEFAULT_VACCINATION_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].firstVaccinationDate").value(hasItem(sameInstant(DEFAULT_FIRST_VACCINATION_DATE))))
            .andExpect(jsonPath("$.[*].secondVaccinationDate").value(hasItem(sameInstant(DEFAULT_SECOND_VACCINATION_DATE))))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY.toString())))
            .andExpect(jsonPath("$.[*].adress").value(hasItem(DEFAULT_ADRESS)));
    }

    @Test
    @Transactional
    void getVaccinationInfo() throws Exception {
        // Initialize the database
        vaccinationInfoRepository.saveAndFlush(vaccinationInfo);

        // Get the vaccinationInfo
        restVaccinationInfoMockMvc
            .perform(get(ENTITY_API_URL_ID, vaccinationInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vaccinationInfo.getId().intValue()))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.birthday").value(DEFAULT_BIRTHDAY.toString()))
            .andExpect(jsonPath("$.identityCardNumber").value(DEFAULT_IDENTITY_CARD_NUMBER))
            .andExpect(jsonPath("$.vaccinationNumber").value(DEFAULT_VACCINATION_NUMBER.toString()))
            .andExpect(jsonPath("$.firstVaccinationDate").value(sameInstant(DEFAULT_FIRST_VACCINATION_DATE)))
            .andExpect(jsonPath("$.secondVaccinationDate").value(sameInstant(DEFAULT_SECOND_VACCINATION_DATE)))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY.toString()))
            .andExpect(jsonPath("$.adress").value(DEFAULT_ADRESS));
    }

    @Test
    @Transactional
    void getVaccinationInfosByIdFiltering() throws Exception {
        // Initialize the database
        vaccinationInfoRepository.saveAndFlush(vaccinationInfo);

        Long id = vaccinationInfo.getId();

        defaultVaccinationInfoShouldBeFound("id.equals=" + id);
        defaultVaccinationInfoShouldNotBeFound("id.notEquals=" + id);

        defaultVaccinationInfoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultVaccinationInfoShouldNotBeFound("id.greaterThan=" + id);

        defaultVaccinationInfoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultVaccinationInfoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllVaccinationInfosByUuidIsEqualToSomething() throws Exception {
        // Initialize the database
        vaccinationInfoRepository.saveAndFlush(vaccinationInfo);

        // Get all the vaccinationInfoList where uuid equals to DEFAULT_UUID
        defaultVaccinationInfoShouldBeFound("uuid.equals=" + DEFAULT_UUID);

        // Get all the vaccinationInfoList where uuid equals to UPDATED_UUID
        defaultVaccinationInfoShouldNotBeFound("uuid.equals=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllVaccinationInfosByUuidIsNotEqualToSomething() throws Exception {
        // Initialize the database
        vaccinationInfoRepository.saveAndFlush(vaccinationInfo);

        // Get all the vaccinationInfoList where uuid not equals to DEFAULT_UUID
        defaultVaccinationInfoShouldNotBeFound("uuid.notEquals=" + DEFAULT_UUID);

        // Get all the vaccinationInfoList where uuid not equals to UPDATED_UUID
        defaultVaccinationInfoShouldBeFound("uuid.notEquals=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllVaccinationInfosByUuidIsInShouldWork() throws Exception {
        // Initialize the database
        vaccinationInfoRepository.saveAndFlush(vaccinationInfo);

        // Get all the vaccinationInfoList where uuid in DEFAULT_UUID or UPDATED_UUID
        defaultVaccinationInfoShouldBeFound("uuid.in=" + DEFAULT_UUID + "," + UPDATED_UUID);

        // Get all the vaccinationInfoList where uuid equals to UPDATED_UUID
        defaultVaccinationInfoShouldNotBeFound("uuid.in=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllVaccinationInfosByUuidIsNullOrNotNull() throws Exception {
        // Initialize the database
        vaccinationInfoRepository.saveAndFlush(vaccinationInfo);

        // Get all the vaccinationInfoList where uuid is not null
        defaultVaccinationInfoShouldBeFound("uuid.specified=true");

        // Get all the vaccinationInfoList where uuid is null
        defaultVaccinationInfoShouldNotBeFound("uuid.specified=false");
    }

    @Test
    @Transactional
    void getAllVaccinationInfosByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        vaccinationInfoRepository.saveAndFlush(vaccinationInfo);

        // Get all the vaccinationInfoList where name equals to DEFAULT_NAME
        defaultVaccinationInfoShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the vaccinationInfoList where name equals to UPDATED_NAME
        defaultVaccinationInfoShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllVaccinationInfosByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        vaccinationInfoRepository.saveAndFlush(vaccinationInfo);

        // Get all the vaccinationInfoList where name not equals to DEFAULT_NAME
        defaultVaccinationInfoShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the vaccinationInfoList where name not equals to UPDATED_NAME
        defaultVaccinationInfoShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllVaccinationInfosByNameIsInShouldWork() throws Exception {
        // Initialize the database
        vaccinationInfoRepository.saveAndFlush(vaccinationInfo);

        // Get all the vaccinationInfoList where name in DEFAULT_NAME or UPDATED_NAME
        defaultVaccinationInfoShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the vaccinationInfoList where name equals to UPDATED_NAME
        defaultVaccinationInfoShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllVaccinationInfosByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        vaccinationInfoRepository.saveAndFlush(vaccinationInfo);

        // Get all the vaccinationInfoList where name is not null
        defaultVaccinationInfoShouldBeFound("name.specified=true");

        // Get all the vaccinationInfoList where name is null
        defaultVaccinationInfoShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllVaccinationInfosByNameContainsSomething() throws Exception {
        // Initialize the database
        vaccinationInfoRepository.saveAndFlush(vaccinationInfo);

        // Get all the vaccinationInfoList where name contains DEFAULT_NAME
        defaultVaccinationInfoShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the vaccinationInfoList where name contains UPDATED_NAME
        defaultVaccinationInfoShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllVaccinationInfosByNameNotContainsSomething() throws Exception {
        // Initialize the database
        vaccinationInfoRepository.saveAndFlush(vaccinationInfo);

        // Get all the vaccinationInfoList where name does not contain DEFAULT_NAME
        defaultVaccinationInfoShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the vaccinationInfoList where name does not contain UPDATED_NAME
        defaultVaccinationInfoShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllVaccinationInfosByBirthdayIsEqualToSomething() throws Exception {
        // Initialize the database
        vaccinationInfoRepository.saveAndFlush(vaccinationInfo);

        // Get all the vaccinationInfoList where birthday equals to DEFAULT_BIRTHDAY
        defaultVaccinationInfoShouldBeFound("birthday.equals=" + DEFAULT_BIRTHDAY);

        // Get all the vaccinationInfoList where birthday equals to UPDATED_BIRTHDAY
        defaultVaccinationInfoShouldNotBeFound("birthday.equals=" + UPDATED_BIRTHDAY);
    }

    @Test
    @Transactional
    void getAllVaccinationInfosByBirthdayIsNotEqualToSomething() throws Exception {
        // Initialize the database
        vaccinationInfoRepository.saveAndFlush(vaccinationInfo);

        // Get all the vaccinationInfoList where birthday not equals to DEFAULT_BIRTHDAY
        defaultVaccinationInfoShouldNotBeFound("birthday.notEquals=" + DEFAULT_BIRTHDAY);

        // Get all the vaccinationInfoList where birthday not equals to UPDATED_BIRTHDAY
        defaultVaccinationInfoShouldBeFound("birthday.notEquals=" + UPDATED_BIRTHDAY);
    }

    @Test
    @Transactional
    void getAllVaccinationInfosByBirthdayIsInShouldWork() throws Exception {
        // Initialize the database
        vaccinationInfoRepository.saveAndFlush(vaccinationInfo);

        // Get all the vaccinationInfoList where birthday in DEFAULT_BIRTHDAY or UPDATED_BIRTHDAY
        defaultVaccinationInfoShouldBeFound("birthday.in=" + DEFAULT_BIRTHDAY + "," + UPDATED_BIRTHDAY);

        // Get all the vaccinationInfoList where birthday equals to UPDATED_BIRTHDAY
        defaultVaccinationInfoShouldNotBeFound("birthday.in=" + UPDATED_BIRTHDAY);
    }

    @Test
    @Transactional
    void getAllVaccinationInfosByBirthdayIsNullOrNotNull() throws Exception {
        // Initialize the database
        vaccinationInfoRepository.saveAndFlush(vaccinationInfo);

        // Get all the vaccinationInfoList where birthday is not null
        defaultVaccinationInfoShouldBeFound("birthday.specified=true");

        // Get all the vaccinationInfoList where birthday is null
        defaultVaccinationInfoShouldNotBeFound("birthday.specified=false");
    }

    @Test
    @Transactional
    void getAllVaccinationInfosByBirthdayIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vaccinationInfoRepository.saveAndFlush(vaccinationInfo);

        // Get all the vaccinationInfoList where birthday is greater than or equal to DEFAULT_BIRTHDAY
        defaultVaccinationInfoShouldBeFound("birthday.greaterThanOrEqual=" + DEFAULT_BIRTHDAY);

        // Get all the vaccinationInfoList where birthday is greater than or equal to UPDATED_BIRTHDAY
        defaultVaccinationInfoShouldNotBeFound("birthday.greaterThanOrEqual=" + UPDATED_BIRTHDAY);
    }

    @Test
    @Transactional
    void getAllVaccinationInfosByBirthdayIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vaccinationInfoRepository.saveAndFlush(vaccinationInfo);

        // Get all the vaccinationInfoList where birthday is less than or equal to DEFAULT_BIRTHDAY
        defaultVaccinationInfoShouldBeFound("birthday.lessThanOrEqual=" + DEFAULT_BIRTHDAY);

        // Get all the vaccinationInfoList where birthday is less than or equal to SMALLER_BIRTHDAY
        defaultVaccinationInfoShouldNotBeFound("birthday.lessThanOrEqual=" + SMALLER_BIRTHDAY);
    }

    @Test
    @Transactional
    void getAllVaccinationInfosByBirthdayIsLessThanSomething() throws Exception {
        // Initialize the database
        vaccinationInfoRepository.saveAndFlush(vaccinationInfo);

        // Get all the vaccinationInfoList where birthday is less than DEFAULT_BIRTHDAY
        defaultVaccinationInfoShouldNotBeFound("birthday.lessThan=" + DEFAULT_BIRTHDAY);

        // Get all the vaccinationInfoList where birthday is less than UPDATED_BIRTHDAY
        defaultVaccinationInfoShouldBeFound("birthday.lessThan=" + UPDATED_BIRTHDAY);
    }

    @Test
    @Transactional
    void getAllVaccinationInfosByBirthdayIsGreaterThanSomething() throws Exception {
        // Initialize the database
        vaccinationInfoRepository.saveAndFlush(vaccinationInfo);

        // Get all the vaccinationInfoList where birthday is greater than DEFAULT_BIRTHDAY
        defaultVaccinationInfoShouldNotBeFound("birthday.greaterThan=" + DEFAULT_BIRTHDAY);

        // Get all the vaccinationInfoList where birthday is greater than SMALLER_BIRTHDAY
        defaultVaccinationInfoShouldBeFound("birthday.greaterThan=" + SMALLER_BIRTHDAY);
    }

    @Test
    @Transactional
    void getAllVaccinationInfosByIdentityCardNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        vaccinationInfoRepository.saveAndFlush(vaccinationInfo);

        // Get all the vaccinationInfoList where identityCardNumber equals to DEFAULT_IDENTITY_CARD_NUMBER
        defaultVaccinationInfoShouldBeFound("identityCardNumber.equals=" + DEFAULT_IDENTITY_CARD_NUMBER);

        // Get all the vaccinationInfoList where identityCardNumber equals to UPDATED_IDENTITY_CARD_NUMBER
        defaultVaccinationInfoShouldNotBeFound("identityCardNumber.equals=" + UPDATED_IDENTITY_CARD_NUMBER);
    }

    @Test
    @Transactional
    void getAllVaccinationInfosByIdentityCardNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        vaccinationInfoRepository.saveAndFlush(vaccinationInfo);

        // Get all the vaccinationInfoList where identityCardNumber not equals to DEFAULT_IDENTITY_CARD_NUMBER
        defaultVaccinationInfoShouldNotBeFound("identityCardNumber.notEquals=" + DEFAULT_IDENTITY_CARD_NUMBER);

        // Get all the vaccinationInfoList where identityCardNumber not equals to UPDATED_IDENTITY_CARD_NUMBER
        defaultVaccinationInfoShouldBeFound("identityCardNumber.notEquals=" + UPDATED_IDENTITY_CARD_NUMBER);
    }

    @Test
    @Transactional
    void getAllVaccinationInfosByIdentityCardNumberIsInShouldWork() throws Exception {
        // Initialize the database
        vaccinationInfoRepository.saveAndFlush(vaccinationInfo);

        // Get all the vaccinationInfoList where identityCardNumber in DEFAULT_IDENTITY_CARD_NUMBER or UPDATED_IDENTITY_CARD_NUMBER
        defaultVaccinationInfoShouldBeFound("identityCardNumber.in=" + DEFAULT_IDENTITY_CARD_NUMBER + "," + UPDATED_IDENTITY_CARD_NUMBER);

        // Get all the vaccinationInfoList where identityCardNumber equals to UPDATED_IDENTITY_CARD_NUMBER
        defaultVaccinationInfoShouldNotBeFound("identityCardNumber.in=" + UPDATED_IDENTITY_CARD_NUMBER);
    }

    @Test
    @Transactional
    void getAllVaccinationInfosByIdentityCardNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        vaccinationInfoRepository.saveAndFlush(vaccinationInfo);

        // Get all the vaccinationInfoList where identityCardNumber is not null
        defaultVaccinationInfoShouldBeFound("identityCardNumber.specified=true");

        // Get all the vaccinationInfoList where identityCardNumber is null
        defaultVaccinationInfoShouldNotBeFound("identityCardNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllVaccinationInfosByIdentityCardNumberContainsSomething() throws Exception {
        // Initialize the database
        vaccinationInfoRepository.saveAndFlush(vaccinationInfo);

        // Get all the vaccinationInfoList where identityCardNumber contains DEFAULT_IDENTITY_CARD_NUMBER
        defaultVaccinationInfoShouldBeFound("identityCardNumber.contains=" + DEFAULT_IDENTITY_CARD_NUMBER);

        // Get all the vaccinationInfoList where identityCardNumber contains UPDATED_IDENTITY_CARD_NUMBER
        defaultVaccinationInfoShouldNotBeFound("identityCardNumber.contains=" + UPDATED_IDENTITY_CARD_NUMBER);
    }

    @Test
    @Transactional
    void getAllVaccinationInfosByIdentityCardNumberNotContainsSomething() throws Exception {
        // Initialize the database
        vaccinationInfoRepository.saveAndFlush(vaccinationInfo);

        // Get all the vaccinationInfoList where identityCardNumber does not contain DEFAULT_IDENTITY_CARD_NUMBER
        defaultVaccinationInfoShouldNotBeFound("identityCardNumber.doesNotContain=" + DEFAULT_IDENTITY_CARD_NUMBER);

        // Get all the vaccinationInfoList where identityCardNumber does not contain UPDATED_IDENTITY_CARD_NUMBER
        defaultVaccinationInfoShouldBeFound("identityCardNumber.doesNotContain=" + UPDATED_IDENTITY_CARD_NUMBER);
    }

    @Test
    @Transactional
    void getAllVaccinationInfosByVaccinationNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        vaccinationInfoRepository.saveAndFlush(vaccinationInfo);

        // Get all the vaccinationInfoList where vaccinationNumber equals to DEFAULT_VACCINATION_NUMBER
        defaultVaccinationInfoShouldBeFound("vaccinationNumber.equals=" + DEFAULT_VACCINATION_NUMBER);

        // Get all the vaccinationInfoList where vaccinationNumber equals to UPDATED_VACCINATION_NUMBER
        defaultVaccinationInfoShouldNotBeFound("vaccinationNumber.equals=" + UPDATED_VACCINATION_NUMBER);
    }

    @Test
    @Transactional
    void getAllVaccinationInfosByVaccinationNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        vaccinationInfoRepository.saveAndFlush(vaccinationInfo);

        // Get all the vaccinationInfoList where vaccinationNumber not equals to DEFAULT_VACCINATION_NUMBER
        defaultVaccinationInfoShouldNotBeFound("vaccinationNumber.notEquals=" + DEFAULT_VACCINATION_NUMBER);

        // Get all the vaccinationInfoList where vaccinationNumber not equals to UPDATED_VACCINATION_NUMBER
        defaultVaccinationInfoShouldBeFound("vaccinationNumber.notEquals=" + UPDATED_VACCINATION_NUMBER);
    }

    @Test
    @Transactional
    void getAllVaccinationInfosByVaccinationNumberIsInShouldWork() throws Exception {
        // Initialize the database
        vaccinationInfoRepository.saveAndFlush(vaccinationInfo);

        // Get all the vaccinationInfoList where vaccinationNumber in DEFAULT_VACCINATION_NUMBER or UPDATED_VACCINATION_NUMBER
        defaultVaccinationInfoShouldBeFound("vaccinationNumber.in=" + DEFAULT_VACCINATION_NUMBER + "," + UPDATED_VACCINATION_NUMBER);

        // Get all the vaccinationInfoList where vaccinationNumber equals to UPDATED_VACCINATION_NUMBER
        defaultVaccinationInfoShouldNotBeFound("vaccinationNumber.in=" + UPDATED_VACCINATION_NUMBER);
    }

    @Test
    @Transactional
    void getAllVaccinationInfosByVaccinationNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        vaccinationInfoRepository.saveAndFlush(vaccinationInfo);

        // Get all the vaccinationInfoList where vaccinationNumber is not null
        defaultVaccinationInfoShouldBeFound("vaccinationNumber.specified=true");

        // Get all the vaccinationInfoList where vaccinationNumber is null
        defaultVaccinationInfoShouldNotBeFound("vaccinationNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllVaccinationInfosByFirstVaccinationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        vaccinationInfoRepository.saveAndFlush(vaccinationInfo);

        // Get all the vaccinationInfoList where firstVaccinationDate equals to DEFAULT_FIRST_VACCINATION_DATE
        defaultVaccinationInfoShouldBeFound("firstVaccinationDate.equals=" + DEFAULT_FIRST_VACCINATION_DATE);

        // Get all the vaccinationInfoList where firstVaccinationDate equals to UPDATED_FIRST_VACCINATION_DATE
        defaultVaccinationInfoShouldNotBeFound("firstVaccinationDate.equals=" + UPDATED_FIRST_VACCINATION_DATE);
    }

    @Test
    @Transactional
    void getAllVaccinationInfosByFirstVaccinationDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        vaccinationInfoRepository.saveAndFlush(vaccinationInfo);

        // Get all the vaccinationInfoList where firstVaccinationDate not equals to DEFAULT_FIRST_VACCINATION_DATE
        defaultVaccinationInfoShouldNotBeFound("firstVaccinationDate.notEquals=" + DEFAULT_FIRST_VACCINATION_DATE);

        // Get all the vaccinationInfoList where firstVaccinationDate not equals to UPDATED_FIRST_VACCINATION_DATE
        defaultVaccinationInfoShouldBeFound("firstVaccinationDate.notEquals=" + UPDATED_FIRST_VACCINATION_DATE);
    }

    @Test
    @Transactional
    void getAllVaccinationInfosByFirstVaccinationDateIsInShouldWork() throws Exception {
        // Initialize the database
        vaccinationInfoRepository.saveAndFlush(vaccinationInfo);

        // Get all the vaccinationInfoList where firstVaccinationDate in DEFAULT_FIRST_VACCINATION_DATE or UPDATED_FIRST_VACCINATION_DATE
        defaultVaccinationInfoShouldBeFound(
            "firstVaccinationDate.in=" + DEFAULT_FIRST_VACCINATION_DATE + "," + UPDATED_FIRST_VACCINATION_DATE
        );

        // Get all the vaccinationInfoList where firstVaccinationDate equals to UPDATED_FIRST_VACCINATION_DATE
        defaultVaccinationInfoShouldNotBeFound("firstVaccinationDate.in=" + UPDATED_FIRST_VACCINATION_DATE);
    }

    @Test
    @Transactional
    void getAllVaccinationInfosByFirstVaccinationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        vaccinationInfoRepository.saveAndFlush(vaccinationInfo);

        // Get all the vaccinationInfoList where firstVaccinationDate is not null
        defaultVaccinationInfoShouldBeFound("firstVaccinationDate.specified=true");

        // Get all the vaccinationInfoList where firstVaccinationDate is null
        defaultVaccinationInfoShouldNotBeFound("firstVaccinationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllVaccinationInfosByFirstVaccinationDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vaccinationInfoRepository.saveAndFlush(vaccinationInfo);

        // Get all the vaccinationInfoList where firstVaccinationDate is greater than or equal to DEFAULT_FIRST_VACCINATION_DATE
        defaultVaccinationInfoShouldBeFound("firstVaccinationDate.greaterThanOrEqual=" + DEFAULT_FIRST_VACCINATION_DATE);

        // Get all the vaccinationInfoList where firstVaccinationDate is greater than or equal to UPDATED_FIRST_VACCINATION_DATE
        defaultVaccinationInfoShouldNotBeFound("firstVaccinationDate.greaterThanOrEqual=" + UPDATED_FIRST_VACCINATION_DATE);
    }

    @Test
    @Transactional
    void getAllVaccinationInfosByFirstVaccinationDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vaccinationInfoRepository.saveAndFlush(vaccinationInfo);

        // Get all the vaccinationInfoList where firstVaccinationDate is less than or equal to DEFAULT_FIRST_VACCINATION_DATE
        defaultVaccinationInfoShouldBeFound("firstVaccinationDate.lessThanOrEqual=" + DEFAULT_FIRST_VACCINATION_DATE);

        // Get all the vaccinationInfoList where firstVaccinationDate is less than or equal to SMALLER_FIRST_VACCINATION_DATE
        defaultVaccinationInfoShouldNotBeFound("firstVaccinationDate.lessThanOrEqual=" + SMALLER_FIRST_VACCINATION_DATE);
    }

    @Test
    @Transactional
    void getAllVaccinationInfosByFirstVaccinationDateIsLessThanSomething() throws Exception {
        // Initialize the database
        vaccinationInfoRepository.saveAndFlush(vaccinationInfo);

        // Get all the vaccinationInfoList where firstVaccinationDate is less than DEFAULT_FIRST_VACCINATION_DATE
        defaultVaccinationInfoShouldNotBeFound("firstVaccinationDate.lessThan=" + DEFAULT_FIRST_VACCINATION_DATE);

        // Get all the vaccinationInfoList where firstVaccinationDate is less than UPDATED_FIRST_VACCINATION_DATE
        defaultVaccinationInfoShouldBeFound("firstVaccinationDate.lessThan=" + UPDATED_FIRST_VACCINATION_DATE);
    }

    @Test
    @Transactional
    void getAllVaccinationInfosByFirstVaccinationDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        vaccinationInfoRepository.saveAndFlush(vaccinationInfo);

        // Get all the vaccinationInfoList where firstVaccinationDate is greater than DEFAULT_FIRST_VACCINATION_DATE
        defaultVaccinationInfoShouldNotBeFound("firstVaccinationDate.greaterThan=" + DEFAULT_FIRST_VACCINATION_DATE);

        // Get all the vaccinationInfoList where firstVaccinationDate is greater than SMALLER_FIRST_VACCINATION_DATE
        defaultVaccinationInfoShouldBeFound("firstVaccinationDate.greaterThan=" + SMALLER_FIRST_VACCINATION_DATE);
    }

    @Test
    @Transactional
    void getAllVaccinationInfosBySecondVaccinationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        vaccinationInfoRepository.saveAndFlush(vaccinationInfo);

        // Get all the vaccinationInfoList where secondVaccinationDate equals to DEFAULT_SECOND_VACCINATION_DATE
        defaultVaccinationInfoShouldBeFound("secondVaccinationDate.equals=" + DEFAULT_SECOND_VACCINATION_DATE);

        // Get all the vaccinationInfoList where secondVaccinationDate equals to UPDATED_SECOND_VACCINATION_DATE
        defaultVaccinationInfoShouldNotBeFound("secondVaccinationDate.equals=" + UPDATED_SECOND_VACCINATION_DATE);
    }

    @Test
    @Transactional
    void getAllVaccinationInfosBySecondVaccinationDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        vaccinationInfoRepository.saveAndFlush(vaccinationInfo);

        // Get all the vaccinationInfoList where secondVaccinationDate not equals to DEFAULT_SECOND_VACCINATION_DATE
        defaultVaccinationInfoShouldNotBeFound("secondVaccinationDate.notEquals=" + DEFAULT_SECOND_VACCINATION_DATE);

        // Get all the vaccinationInfoList where secondVaccinationDate not equals to UPDATED_SECOND_VACCINATION_DATE
        defaultVaccinationInfoShouldBeFound("secondVaccinationDate.notEquals=" + UPDATED_SECOND_VACCINATION_DATE);
    }

    @Test
    @Transactional
    void getAllVaccinationInfosBySecondVaccinationDateIsInShouldWork() throws Exception {
        // Initialize the database
        vaccinationInfoRepository.saveAndFlush(vaccinationInfo);

        // Get all the vaccinationInfoList where secondVaccinationDate in DEFAULT_SECOND_VACCINATION_DATE or UPDATED_SECOND_VACCINATION_DATE
        defaultVaccinationInfoShouldBeFound(
            "secondVaccinationDate.in=" + DEFAULT_SECOND_VACCINATION_DATE + "," + UPDATED_SECOND_VACCINATION_DATE
        );

        // Get all the vaccinationInfoList where secondVaccinationDate equals to UPDATED_SECOND_VACCINATION_DATE
        defaultVaccinationInfoShouldNotBeFound("secondVaccinationDate.in=" + UPDATED_SECOND_VACCINATION_DATE);
    }

    @Test
    @Transactional
    void getAllVaccinationInfosBySecondVaccinationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        vaccinationInfoRepository.saveAndFlush(vaccinationInfo);

        // Get all the vaccinationInfoList where secondVaccinationDate is not null
        defaultVaccinationInfoShouldBeFound("secondVaccinationDate.specified=true");

        // Get all the vaccinationInfoList where secondVaccinationDate is null
        defaultVaccinationInfoShouldNotBeFound("secondVaccinationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllVaccinationInfosBySecondVaccinationDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vaccinationInfoRepository.saveAndFlush(vaccinationInfo);

        // Get all the vaccinationInfoList where secondVaccinationDate is greater than or equal to DEFAULT_SECOND_VACCINATION_DATE
        defaultVaccinationInfoShouldBeFound("secondVaccinationDate.greaterThanOrEqual=" + DEFAULT_SECOND_VACCINATION_DATE);

        // Get all the vaccinationInfoList where secondVaccinationDate is greater than or equal to UPDATED_SECOND_VACCINATION_DATE
        defaultVaccinationInfoShouldNotBeFound("secondVaccinationDate.greaterThanOrEqual=" + UPDATED_SECOND_VACCINATION_DATE);
    }

    @Test
    @Transactional
    void getAllVaccinationInfosBySecondVaccinationDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vaccinationInfoRepository.saveAndFlush(vaccinationInfo);

        // Get all the vaccinationInfoList where secondVaccinationDate is less than or equal to DEFAULT_SECOND_VACCINATION_DATE
        defaultVaccinationInfoShouldBeFound("secondVaccinationDate.lessThanOrEqual=" + DEFAULT_SECOND_VACCINATION_DATE);

        // Get all the vaccinationInfoList where secondVaccinationDate is less than or equal to SMALLER_SECOND_VACCINATION_DATE
        defaultVaccinationInfoShouldNotBeFound("secondVaccinationDate.lessThanOrEqual=" + SMALLER_SECOND_VACCINATION_DATE);
    }

    @Test
    @Transactional
    void getAllVaccinationInfosBySecondVaccinationDateIsLessThanSomething() throws Exception {
        // Initialize the database
        vaccinationInfoRepository.saveAndFlush(vaccinationInfo);

        // Get all the vaccinationInfoList where secondVaccinationDate is less than DEFAULT_SECOND_VACCINATION_DATE
        defaultVaccinationInfoShouldNotBeFound("secondVaccinationDate.lessThan=" + DEFAULT_SECOND_VACCINATION_DATE);

        // Get all the vaccinationInfoList where secondVaccinationDate is less than UPDATED_SECOND_VACCINATION_DATE
        defaultVaccinationInfoShouldBeFound("secondVaccinationDate.lessThan=" + UPDATED_SECOND_VACCINATION_DATE);
    }

    @Test
    @Transactional
    void getAllVaccinationInfosBySecondVaccinationDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        vaccinationInfoRepository.saveAndFlush(vaccinationInfo);

        // Get all the vaccinationInfoList where secondVaccinationDate is greater than DEFAULT_SECOND_VACCINATION_DATE
        defaultVaccinationInfoShouldNotBeFound("secondVaccinationDate.greaterThan=" + DEFAULT_SECOND_VACCINATION_DATE);

        // Get all the vaccinationInfoList where secondVaccinationDate is greater than SMALLER_SECOND_VACCINATION_DATE
        defaultVaccinationInfoShouldBeFound("secondVaccinationDate.greaterThan=" + SMALLER_SECOND_VACCINATION_DATE);
    }

    @Test
    @Transactional
    void getAllVaccinationInfosByCountryIsEqualToSomething() throws Exception {
        // Initialize the database
        vaccinationInfoRepository.saveAndFlush(vaccinationInfo);

        // Get all the vaccinationInfoList where country equals to DEFAULT_COUNTRY
        defaultVaccinationInfoShouldBeFound("country.equals=" + DEFAULT_COUNTRY);

        // Get all the vaccinationInfoList where country equals to UPDATED_COUNTRY
        defaultVaccinationInfoShouldNotBeFound("country.equals=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllVaccinationInfosByCountryIsNotEqualToSomething() throws Exception {
        // Initialize the database
        vaccinationInfoRepository.saveAndFlush(vaccinationInfo);

        // Get all the vaccinationInfoList where country not equals to DEFAULT_COUNTRY
        defaultVaccinationInfoShouldNotBeFound("country.notEquals=" + DEFAULT_COUNTRY);

        // Get all the vaccinationInfoList where country not equals to UPDATED_COUNTRY
        defaultVaccinationInfoShouldBeFound("country.notEquals=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllVaccinationInfosByCountryIsInShouldWork() throws Exception {
        // Initialize the database
        vaccinationInfoRepository.saveAndFlush(vaccinationInfo);

        // Get all the vaccinationInfoList where country in DEFAULT_COUNTRY or UPDATED_COUNTRY
        defaultVaccinationInfoShouldBeFound("country.in=" + DEFAULT_COUNTRY + "," + UPDATED_COUNTRY);

        // Get all the vaccinationInfoList where country equals to UPDATED_COUNTRY
        defaultVaccinationInfoShouldNotBeFound("country.in=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllVaccinationInfosByCountryIsNullOrNotNull() throws Exception {
        // Initialize the database
        vaccinationInfoRepository.saveAndFlush(vaccinationInfo);

        // Get all the vaccinationInfoList where country is not null
        defaultVaccinationInfoShouldBeFound("country.specified=true");

        // Get all the vaccinationInfoList where country is null
        defaultVaccinationInfoShouldNotBeFound("country.specified=false");
    }

    @Test
    @Transactional
    void getAllVaccinationInfosByAdressIsEqualToSomething() throws Exception {
        // Initialize the database
        vaccinationInfoRepository.saveAndFlush(vaccinationInfo);

        // Get all the vaccinationInfoList where adress equals to DEFAULT_ADRESS
        defaultVaccinationInfoShouldBeFound("adress.equals=" + DEFAULT_ADRESS);

        // Get all the vaccinationInfoList where adress equals to UPDATED_ADRESS
        defaultVaccinationInfoShouldNotBeFound("adress.equals=" + UPDATED_ADRESS);
    }

    @Test
    @Transactional
    void getAllVaccinationInfosByAdressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        vaccinationInfoRepository.saveAndFlush(vaccinationInfo);

        // Get all the vaccinationInfoList where adress not equals to DEFAULT_ADRESS
        defaultVaccinationInfoShouldNotBeFound("adress.notEquals=" + DEFAULT_ADRESS);

        // Get all the vaccinationInfoList where adress not equals to UPDATED_ADRESS
        defaultVaccinationInfoShouldBeFound("adress.notEquals=" + UPDATED_ADRESS);
    }

    @Test
    @Transactional
    void getAllVaccinationInfosByAdressIsInShouldWork() throws Exception {
        // Initialize the database
        vaccinationInfoRepository.saveAndFlush(vaccinationInfo);

        // Get all the vaccinationInfoList where adress in DEFAULT_ADRESS or UPDATED_ADRESS
        defaultVaccinationInfoShouldBeFound("adress.in=" + DEFAULT_ADRESS + "," + UPDATED_ADRESS);

        // Get all the vaccinationInfoList where adress equals to UPDATED_ADRESS
        defaultVaccinationInfoShouldNotBeFound("adress.in=" + UPDATED_ADRESS);
    }

    @Test
    @Transactional
    void getAllVaccinationInfosByAdressIsNullOrNotNull() throws Exception {
        // Initialize the database
        vaccinationInfoRepository.saveAndFlush(vaccinationInfo);

        // Get all the vaccinationInfoList where adress is not null
        defaultVaccinationInfoShouldBeFound("adress.specified=true");

        // Get all the vaccinationInfoList where adress is null
        defaultVaccinationInfoShouldNotBeFound("adress.specified=false");
    }

    @Test
    @Transactional
    void getAllVaccinationInfosByAdressContainsSomething() throws Exception {
        // Initialize the database
        vaccinationInfoRepository.saveAndFlush(vaccinationInfo);

        // Get all the vaccinationInfoList where adress contains DEFAULT_ADRESS
        defaultVaccinationInfoShouldBeFound("adress.contains=" + DEFAULT_ADRESS);

        // Get all the vaccinationInfoList where adress contains UPDATED_ADRESS
        defaultVaccinationInfoShouldNotBeFound("adress.contains=" + UPDATED_ADRESS);
    }

    @Test
    @Transactional
    void getAllVaccinationInfosByAdressNotContainsSomething() throws Exception {
        // Initialize the database
        vaccinationInfoRepository.saveAndFlush(vaccinationInfo);

        // Get all the vaccinationInfoList where adress does not contain DEFAULT_ADRESS
        defaultVaccinationInfoShouldNotBeFound("adress.doesNotContain=" + DEFAULT_ADRESS);

        // Get all the vaccinationInfoList where adress does not contain UPDATED_ADRESS
        defaultVaccinationInfoShouldBeFound("adress.doesNotContain=" + UPDATED_ADRESS);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultVaccinationInfoShouldBeFound(String filter) throws Exception {
        restVaccinationInfoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vaccinationInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].birthday").value(hasItem(DEFAULT_BIRTHDAY.toString())))
            .andExpect(jsonPath("$.[*].identityCardNumber").value(hasItem(DEFAULT_IDENTITY_CARD_NUMBER)))
            .andExpect(jsonPath("$.[*].vaccinationNumber").value(hasItem(DEFAULT_VACCINATION_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].firstVaccinationDate").value(hasItem(sameInstant(DEFAULT_FIRST_VACCINATION_DATE))))
            .andExpect(jsonPath("$.[*].secondVaccinationDate").value(hasItem(sameInstant(DEFAULT_SECOND_VACCINATION_DATE))))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY.toString())))
            .andExpect(jsonPath("$.[*].adress").value(hasItem(DEFAULT_ADRESS)));

        // Check, that the count call also returns 1
        restVaccinationInfoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultVaccinationInfoShouldNotBeFound(String filter) throws Exception {
        restVaccinationInfoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restVaccinationInfoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingVaccinationInfo() throws Exception {
        // Get the vaccinationInfo
        restVaccinationInfoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewVaccinationInfo() throws Exception {
        // Initialize the database
        vaccinationInfoRepository.saveAndFlush(vaccinationInfo);

        int databaseSizeBeforeUpdate = vaccinationInfoRepository.findAll().size();

        // Update the vaccinationInfo
        VaccinationInfo updatedVaccinationInfo = vaccinationInfoRepository.findById(vaccinationInfo.getId()).get();
        // Disconnect from session so that the updates on updatedVaccinationInfo are not directly saved in db
        em.detach(updatedVaccinationInfo);
        updatedVaccinationInfo
            .uuid(UPDATED_UUID)
            .name(UPDATED_NAME)
            .birthday(UPDATED_BIRTHDAY)
            .identityCardNumber(UPDATED_IDENTITY_CARD_NUMBER)
            .vaccinationNumber(UPDATED_VACCINATION_NUMBER)
            .firstVaccinationDate(UPDATED_FIRST_VACCINATION_DATE)
            .secondVaccinationDate(UPDATED_SECOND_VACCINATION_DATE)
            .country(UPDATED_COUNTRY)
            .adress(UPDATED_ADRESS);
        VaccinationInfoDTO vaccinationInfoDTO = vaccinationInfoMapper.toDto(updatedVaccinationInfo);

        restVaccinationInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vaccinationInfoDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vaccinationInfoDTO))
            )
            .andExpect(status().isOk());

        // Validate the VaccinationInfo in the database
        List<VaccinationInfo> vaccinationInfoList = vaccinationInfoRepository.findAll();
        assertThat(vaccinationInfoList).hasSize(databaseSizeBeforeUpdate);
        VaccinationInfo testVaccinationInfo = vaccinationInfoList.get(vaccinationInfoList.size() - 1);
        assertThat(testVaccinationInfo.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testVaccinationInfo.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testVaccinationInfo.getBirthday()).isEqualTo(UPDATED_BIRTHDAY);
        assertThat(testVaccinationInfo.getIdentityCardNumber()).isEqualTo(UPDATED_IDENTITY_CARD_NUMBER);
        assertThat(testVaccinationInfo.getVaccinationNumber()).isEqualTo(UPDATED_VACCINATION_NUMBER);
        assertThat(testVaccinationInfo.getFirstVaccinationDate()).isEqualTo(UPDATED_FIRST_VACCINATION_DATE);
        assertThat(testVaccinationInfo.getSecondVaccinationDate()).isEqualTo(UPDATED_SECOND_VACCINATION_DATE);
        assertThat(testVaccinationInfo.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testVaccinationInfo.getAdress()).isEqualTo(UPDATED_ADRESS);
    }

    @Test
    @Transactional
    void putNonExistingVaccinationInfo() throws Exception {
        int databaseSizeBeforeUpdate = vaccinationInfoRepository.findAll().size();
        vaccinationInfo.setId(count.incrementAndGet());

        // Create the VaccinationInfo
        VaccinationInfoDTO vaccinationInfoDTO = vaccinationInfoMapper.toDto(vaccinationInfo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVaccinationInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vaccinationInfoDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vaccinationInfoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VaccinationInfo in the database
        List<VaccinationInfo> vaccinationInfoList = vaccinationInfoRepository.findAll();
        assertThat(vaccinationInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVaccinationInfo() throws Exception {
        int databaseSizeBeforeUpdate = vaccinationInfoRepository.findAll().size();
        vaccinationInfo.setId(count.incrementAndGet());

        // Create the VaccinationInfo
        VaccinationInfoDTO vaccinationInfoDTO = vaccinationInfoMapper.toDto(vaccinationInfo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVaccinationInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vaccinationInfoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VaccinationInfo in the database
        List<VaccinationInfo> vaccinationInfoList = vaccinationInfoRepository.findAll();
        assertThat(vaccinationInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVaccinationInfo() throws Exception {
        int databaseSizeBeforeUpdate = vaccinationInfoRepository.findAll().size();
        vaccinationInfo.setId(count.incrementAndGet());

        // Create the VaccinationInfo
        VaccinationInfoDTO vaccinationInfoDTO = vaccinationInfoMapper.toDto(vaccinationInfo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVaccinationInfoMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vaccinationInfoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the VaccinationInfo in the database
        List<VaccinationInfo> vaccinationInfoList = vaccinationInfoRepository.findAll();
        assertThat(vaccinationInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVaccinationInfoWithPatch() throws Exception {
        // Initialize the database
        vaccinationInfoRepository.saveAndFlush(vaccinationInfo);

        int databaseSizeBeforeUpdate = vaccinationInfoRepository.findAll().size();

        // Update the vaccinationInfo using partial update
        VaccinationInfo partialUpdatedVaccinationInfo = new VaccinationInfo();
        partialUpdatedVaccinationInfo.setId(vaccinationInfo.getId());

        partialUpdatedVaccinationInfo
            .birthday(UPDATED_BIRTHDAY)
            .firstVaccinationDate(UPDATED_FIRST_VACCINATION_DATE)
            .adress(UPDATED_ADRESS);

        restVaccinationInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVaccinationInfo.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVaccinationInfo))
            )
            .andExpect(status().isOk());

        // Validate the VaccinationInfo in the database
        List<VaccinationInfo> vaccinationInfoList = vaccinationInfoRepository.findAll();
        assertThat(vaccinationInfoList).hasSize(databaseSizeBeforeUpdate);
        VaccinationInfo testVaccinationInfo = vaccinationInfoList.get(vaccinationInfoList.size() - 1);
        assertThat(testVaccinationInfo.getUuid()).isEqualTo(DEFAULT_UUID);
        assertThat(testVaccinationInfo.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testVaccinationInfo.getBirthday()).isEqualTo(UPDATED_BIRTHDAY);
        assertThat(testVaccinationInfo.getIdentityCardNumber()).isEqualTo(DEFAULT_IDENTITY_CARD_NUMBER);
        assertThat(testVaccinationInfo.getVaccinationNumber()).isEqualTo(DEFAULT_VACCINATION_NUMBER);
        assertThat(testVaccinationInfo.getFirstVaccinationDate()).isEqualTo(UPDATED_FIRST_VACCINATION_DATE);
        assertThat(testVaccinationInfo.getSecondVaccinationDate()).isEqualTo(DEFAULT_SECOND_VACCINATION_DATE);
        assertThat(testVaccinationInfo.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testVaccinationInfo.getAdress()).isEqualTo(UPDATED_ADRESS);
    }

    @Test
    @Transactional
    void fullUpdateVaccinationInfoWithPatch() throws Exception {
        // Initialize the database
        vaccinationInfoRepository.saveAndFlush(vaccinationInfo);

        int databaseSizeBeforeUpdate = vaccinationInfoRepository.findAll().size();

        // Update the vaccinationInfo using partial update
        VaccinationInfo partialUpdatedVaccinationInfo = new VaccinationInfo();
        partialUpdatedVaccinationInfo.setId(vaccinationInfo.getId());

        partialUpdatedVaccinationInfo
            .uuid(UPDATED_UUID)
            .name(UPDATED_NAME)
            .birthday(UPDATED_BIRTHDAY)
            .identityCardNumber(UPDATED_IDENTITY_CARD_NUMBER)
            .vaccinationNumber(UPDATED_VACCINATION_NUMBER)
            .firstVaccinationDate(UPDATED_FIRST_VACCINATION_DATE)
            .secondVaccinationDate(UPDATED_SECOND_VACCINATION_DATE)
            .country(UPDATED_COUNTRY)
            .adress(UPDATED_ADRESS);

        restVaccinationInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVaccinationInfo.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVaccinationInfo))
            )
            .andExpect(status().isOk());

        // Validate the VaccinationInfo in the database
        List<VaccinationInfo> vaccinationInfoList = vaccinationInfoRepository.findAll();
        assertThat(vaccinationInfoList).hasSize(databaseSizeBeforeUpdate);
        VaccinationInfo testVaccinationInfo = vaccinationInfoList.get(vaccinationInfoList.size() - 1);
        assertThat(testVaccinationInfo.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testVaccinationInfo.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testVaccinationInfo.getBirthday()).isEqualTo(UPDATED_BIRTHDAY);
        assertThat(testVaccinationInfo.getIdentityCardNumber()).isEqualTo(UPDATED_IDENTITY_CARD_NUMBER);
        assertThat(testVaccinationInfo.getVaccinationNumber()).isEqualTo(UPDATED_VACCINATION_NUMBER);
        assertThat(testVaccinationInfo.getFirstVaccinationDate()).isEqualTo(UPDATED_FIRST_VACCINATION_DATE);
        assertThat(testVaccinationInfo.getSecondVaccinationDate()).isEqualTo(UPDATED_SECOND_VACCINATION_DATE);
        assertThat(testVaccinationInfo.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testVaccinationInfo.getAdress()).isEqualTo(UPDATED_ADRESS);
    }

    @Test
    @Transactional
    void patchNonExistingVaccinationInfo() throws Exception {
        int databaseSizeBeforeUpdate = vaccinationInfoRepository.findAll().size();
        vaccinationInfo.setId(count.incrementAndGet());

        // Create the VaccinationInfo
        VaccinationInfoDTO vaccinationInfoDTO = vaccinationInfoMapper.toDto(vaccinationInfo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVaccinationInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vaccinationInfoDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vaccinationInfoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VaccinationInfo in the database
        List<VaccinationInfo> vaccinationInfoList = vaccinationInfoRepository.findAll();
        assertThat(vaccinationInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVaccinationInfo() throws Exception {
        int databaseSizeBeforeUpdate = vaccinationInfoRepository.findAll().size();
        vaccinationInfo.setId(count.incrementAndGet());

        // Create the VaccinationInfo
        VaccinationInfoDTO vaccinationInfoDTO = vaccinationInfoMapper.toDto(vaccinationInfo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVaccinationInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vaccinationInfoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VaccinationInfo in the database
        List<VaccinationInfo> vaccinationInfoList = vaccinationInfoRepository.findAll();
        assertThat(vaccinationInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVaccinationInfo() throws Exception {
        int databaseSizeBeforeUpdate = vaccinationInfoRepository.findAll().size();
        vaccinationInfo.setId(count.incrementAndGet());

        // Create the VaccinationInfo
        VaccinationInfoDTO vaccinationInfoDTO = vaccinationInfoMapper.toDto(vaccinationInfo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVaccinationInfoMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vaccinationInfoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the VaccinationInfo in the database
        List<VaccinationInfo> vaccinationInfoList = vaccinationInfoRepository.findAll();
        assertThat(vaccinationInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVaccinationInfo() throws Exception {
        // Initialize the database
        vaccinationInfoRepository.saveAndFlush(vaccinationInfo);

        int databaseSizeBeforeDelete = vaccinationInfoRepository.findAll().size();

        // Delete the vaccinationInfo
        restVaccinationInfoMockMvc
            .perform(delete(ENTITY_API_URL_ID, vaccinationInfo.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<VaccinationInfo> vaccinationInfoList = vaccinationInfoRepository.findAll();
        assertThat(vaccinationInfoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
