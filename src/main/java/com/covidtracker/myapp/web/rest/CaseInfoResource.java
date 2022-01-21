package com.covidtracker.myapp.web.rest;

import com.covidtracker.myapp.repository.CaseInfoRepository;
import com.covidtracker.myapp.service.CaseInfoQueryService;
import com.covidtracker.myapp.service.CaseInfoService;
import com.covidtracker.myapp.service.criteria.CaseInfoCriteria;
import com.covidtracker.myapp.service.dto.CaseInfoDTO;
import com.covidtracker.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
/**
 * REST controller for managing {@link com.covidtracker.myapp.domain.CaseInfo}.
 */
@RestController
@RequestMapping("/api")
public class CaseInfoResource {

    private final Logger log = LoggerFactory.getLogger(CaseInfoResource.class);

    private static final String ENTITY_NAME = "caseInfo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CaseInfoService caseInfoService;

    private final CaseInfoRepository caseInfoRepository;

    private final CaseInfoQueryService caseInfoQueryService;

    public CaseInfoResource(
        CaseInfoService caseInfoService,
        CaseInfoRepository caseInfoRepository,
        CaseInfoQueryService caseInfoQueryService
    ) {
        this.caseInfoService = caseInfoService;
        this.caseInfoRepository = caseInfoRepository;
        this.caseInfoQueryService = caseInfoQueryService;
    }

    /**
     * {@code POST  /case-infos} : Create a new caseInfo.
     *
     * @param caseInfoDTO the caseInfoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new caseInfoDTO, or with status {@code 400 (Bad Request)} if the caseInfo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/case-infos/1")
    public ResponseEntity<CaseInfoDTO> createCaseInfo(@Valid @RequestBody CaseInfoDTO caseInfoDTO) throws URISyntaxException {
        log.debug("REST request to save CaseInfo : {}", caseInfoDTO);
        if (caseInfoDTO.getId() != null) {
            throw new BadRequestAlertException("A new caseInfo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CaseInfoDTO result = caseInfoService.save(caseInfoDTO);
        return ResponseEntity
            .created(new URI("/api/case-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code POST  /case-infos} : Create a new list of caseInfo.
     *
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new list of caseInfoDTO, or with status {@code 400 (Bad Request)} if the caseInfo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/case-infos")
    public ResponseEntity<List<CaseInfoDTO>> createCaseInfos(@Valid @RequestBody List<CaseInfoDTO> caseInfosDTO) throws URISyntaxException {
        log.debug("REST request to save CaseInfo List : []", caseInfosDTO);

        List<CaseInfoDTO> result = caseInfoService.saveAll(caseInfosDTO);
        return ResponseEntity.ok().body(result);
    }


    /**
     * {@code PUT  /case-infos/:id} : Updates an existing caseInfo.
     *
     * @param id the id of the caseInfoDTO to save.
     * @param caseInfoDTO the caseInfoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated caseInfoDTO,
     * or with status {@code 400 (Bad Request)} if the caseInfoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the caseInfoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/case-infos/{id}")
    public ResponseEntity<CaseInfoDTO> updateCaseInfo(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CaseInfoDTO caseInfoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CaseInfo : {}, {}", id, caseInfoDTO);
        if (caseInfoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, caseInfoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!caseInfoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CaseInfoDTO result = caseInfoService.save(caseInfoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, caseInfoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /case-infos/:id} : Partial updates given fields of an existing caseInfo, field will ignore if it is null
     *
     * @param id the id of the caseInfoDTO to save.
     * @param caseInfoDTO the caseInfoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated caseInfoDTO,
     * or with status {@code 400 (Bad Request)} if the caseInfoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the caseInfoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the caseInfoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/case-infos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CaseInfoDTO> partialUpdateCaseInfo(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CaseInfoDTO caseInfoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CaseInfo partially : {}, {}", id, caseInfoDTO);
        if (caseInfoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, caseInfoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!caseInfoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CaseInfoDTO> result = caseInfoService.partialUpdate(caseInfoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, caseInfoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /case-infos} : get all the caseInfos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of caseInfos in body.
     */
    @GetMapping("/case-infos")
    public ResponseEntity<List<CaseInfoDTO>> getAllCaseInfos(CaseInfoCriteria criteria, Pageable pageable) {
        log.debug("REST request to get CaseInfos by criteria: {}", criteria);
        Page<CaseInfoDTO> page = caseInfoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /case-infos/count} : count all the caseInfos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/case-infos/count")
    public ResponseEntity<Long> countCaseInfos(CaseInfoCriteria criteria) {
        log.debug("REST request to count CaseInfos by criteria: {}", criteria);
        return ResponseEntity.ok().body(caseInfoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /case-infos/:id} : get the "id" caseInfo.
     *
     * @param id the id of the caseInfoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the caseInfoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/case-infos/{id}")
    public ResponseEntity<CaseInfoDTO> getCaseInfo(@PathVariable Long id) {
        log.debug("REST request to get CaseInfo : {}", id);
        Optional<CaseInfoDTO> caseInfoDTO = caseInfoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(caseInfoDTO);
    }

    /**
     * {@code DELETE  /case-infos/:id} : delete the "id" caseInfo.
     *
     * @param id the id of the caseInfoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/case-infos/{id}")
    public ResponseEntity<Void> deleteCaseInfo(@PathVariable Long id) {
        log.debug("REST request to delete CaseInfo : {}", id);
        caseInfoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
