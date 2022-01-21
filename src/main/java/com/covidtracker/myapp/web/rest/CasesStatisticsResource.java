package com.covidtracker.myapp.web.rest;

import com.covidtracker.myapp.repository.CasesStatisticsRepository;
import com.covidtracker.myapp.service.CasesStatisticsQueryService;
import com.covidtracker.myapp.service.CasesStatisticsService;
import com.covidtracker.myapp.service.criteria.CasesStatisticsCriteria;
import com.covidtracker.myapp.service.dto.CasesStatisticsDTO;
import com.covidtracker.myapp.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


/**
 * REST controller for managing {@link com.covidtracker.myapp.domain.CasesStatistics}.
 */
@RestController
@RequestMapping("/api")
public class CasesStatisticsResource {

    private final Logger log = LoggerFactory.getLogger(CasesStatisticsResource.class);

    private static final String ENTITY_NAME = "casesStatistics";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CasesStatisticsService casesStatisticsService;

    private final CasesStatisticsRepository casesStatisticsRepository;

    private final CasesStatisticsQueryService casesStatisticsQueryService;

    public CasesStatisticsResource(
        CasesStatisticsService casesStatisticsService,
        CasesStatisticsRepository casesStatisticsRepository,
        CasesStatisticsQueryService casesStatisticsQueryService
    ) {
        this.casesStatisticsService = casesStatisticsService;
        this.casesStatisticsRepository = casesStatisticsRepository;
        this.casesStatisticsQueryService = casesStatisticsQueryService;
    }

    /**
     * {@code POST  /cases-statistics} : Create a new casesStatistics.
     *
     * @param casesStatisticsDTO the casesStatisticsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new casesStatisticsDTO, or with status {@code 400 (Bad Request)} if the casesStatistics has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cases-statistics")
    public ResponseEntity<CasesStatisticsDTO> createCasesStatistics(@Valid @RequestBody CasesStatisticsDTO casesStatisticsDTO)
        throws URISyntaxException {
        log.debug("REST request to save CasesStatistics : {}", casesStatisticsDTO);
        if (casesStatisticsDTO.getId() != null) {
            throw new BadRequestAlertException("A new casesStatistics cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CasesStatisticsDTO result = casesStatisticsService.save(casesStatisticsDTO);
        return ResponseEntity
            .created(new URI("/api/cases-statistics/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cases-statistics/:id} : Updates an existing casesStatistics.
     *
     * @param id the id of the casesStatisticsDTO to save.
     * @param casesStatisticsDTO the casesStatisticsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated casesStatisticsDTO,
     * or with status {@code 400 (Bad Request)} if the casesStatisticsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the casesStatisticsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cases-statistics/{id}")
    public ResponseEntity<CasesStatisticsDTO> updateCasesStatistics(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CasesStatisticsDTO casesStatisticsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CasesStatistics : {}, {}", id, casesStatisticsDTO);
        if (casesStatisticsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, casesStatisticsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!casesStatisticsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CasesStatisticsDTO result = casesStatisticsService.save(casesStatisticsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, casesStatisticsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /cases-statistics/:id} : Partial updates given fields of an existing casesStatistics, field will ignore if it is null
     *
     * @param id the id of the casesStatisticsDTO to save.
     * @param casesStatisticsDTO the casesStatisticsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated casesStatisticsDTO,
     * or with status {@code 400 (Bad Request)} if the casesStatisticsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the casesStatisticsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the casesStatisticsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cases-statistics/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CasesStatisticsDTO> partialUpdateCasesStatistics(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CasesStatisticsDTO casesStatisticsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CasesStatistics partially : {}, {}", id, casesStatisticsDTO);
        if (casesStatisticsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, casesStatisticsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!casesStatisticsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CasesStatisticsDTO> result = casesStatisticsService.partialUpdate(casesStatisticsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, casesStatisticsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /cases-statistics} : get all the casesStatistics.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of casesStatistics in body.
     */
    @GetMapping("/cases-statistics")
    public ResponseEntity<List<CasesStatisticsDTO>> getAllCasesStatistics(CasesStatisticsCriteria criteria, Pageable pageable) {
        log.debug("REST request to get CasesStatistics by criteria: {}", criteria);
        Page<CasesStatisticsDTO> page = casesStatisticsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cases-statistics/count} : count all the casesStatistics.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/cases-statistics/count")
    public ResponseEntity<Long> countCasesStatistics(CasesStatisticsCriteria criteria) {
        log.debug("REST request to count CasesStatistics by criteria: {}", criteria);
        return ResponseEntity.ok().body(casesStatisticsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cases-statistics/:id} : get the "id" casesStatistics.
     *
     * @param id the id of the casesStatisticsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the casesStatisticsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cases-statistics/{id}")
    public ResponseEntity<CasesStatisticsDTO> getCasesStatistics(@PathVariable Long id) {
        log.debug("REST request to get CasesStatistics : {}", id);
        Optional<CasesStatisticsDTO> casesStatisticsDTO = casesStatisticsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(casesStatisticsDTO);
    }

    /**
     * {@code GET  /cases-statistics/:day} : get the "day" casesStatistics.
     *
     * @param day the date of the casesStatisticsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the casesStatisticsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cases-statistics/report/day/{day}")
    public ResponseEntity< List<Object>> getCasesStatisticsByDayForeachCountry(@PathVariable LocalDate day) {
        log.debug("REST request to get CasesStatistics By Day foreach country : {}", day);
        List<Object> casesStatisticsDTO = casesStatisticsService.findByDayForeachCountry(day);
        return ResponseEntity.ok().body(casesStatisticsDTO);
    }

    /**
     * {@code GET  /cases-statistics/:month} : get the "month" casesStatistics.
     *
     * @param month the month of the casesStatisticsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the casesStatisticsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cases-statistics/report/month/{month}")
    public ResponseEntity<List<Object>> getCasesStatisticsByMounthForeachCountry(@PathVariable Integer month) {
        log.debug("REST request to get CasesStatistics by month and country : {}", month);
        List<Object> casesStatisticsDTO = casesStatisticsService.findByMonthForeachCountry(month);
        return ResponseEntity.ok().body(casesStatisticsDTO);
    }

    /**
     * {@code GET  /cases-statistics/:year} : get the "year" casesStatistics.
     *
     * @param year the year of the casesStatisticsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the casesStatisticsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cases-statistics/report/year/{year}")
    public ResponseEntity<List<Object>> getCasesStatisticsByYearForeachCountry(@PathVariable Integer year) {
        log.debug("REST request to get CasesStatistics by year and country : {}", year);
        List<Object> casesStatisticsDTO = casesStatisticsService.findByYearForeachCountry(year);
        return ResponseEntity.ok().body(casesStatisticsDTO);
    }

    /**
     * {@code DELETE  /cases-statistics/:id} : delete the "id" casesStatistics.
     *
     * @param id the id of the casesStatisticsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cases-statistics/{id}")
    public ResponseEntity<Void> deleteCasesStatistics(@PathVariable Long id) {
        log.debug("REST request to delete CasesStatistics : {}", id);
        casesStatisticsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
