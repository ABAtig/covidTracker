package com.covidtracker.myapp.web.rest;

import com.covidtracker.myapp.repository.VaccinationInfoRepository;
import com.covidtracker.myapp.service.VaccinationInfoQueryService;
import com.covidtracker.myapp.service.VaccinationInfoService;
import com.covidtracker.myapp.service.criteria.VaccinationInfoCriteria;
import com.covidtracker.myapp.service.dto.VaccinationInfoDTO;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;
import tech.jhipster.web.util.PaginationUtil;


/**
 * REST controller for managing {@link com.covidtracker.myapp.domain.VaccinationInfo}.
 */
@RestController
@RequestMapping("/api")
public class VaccinationInfoResource {

    private final Logger log = LoggerFactory.getLogger(VaccinationInfoResource.class);

    private static final String ENTITY_NAME = "vaccinationInfo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VaccinationInfoService vaccinationInfoService;

    private final VaccinationInfoRepository vaccinationInfoRepository;

    private final VaccinationInfoQueryService vaccinationInfoQueryService;

    public VaccinationInfoResource(
        VaccinationInfoService vaccinationInfoService,
        VaccinationInfoRepository vaccinationInfoRepository,
        VaccinationInfoQueryService vaccinationInfoQueryService
    ) {
        this.vaccinationInfoService = vaccinationInfoService;
        this.vaccinationInfoRepository = vaccinationInfoRepository;
        this.vaccinationInfoQueryService = vaccinationInfoQueryService;
    }

    /**
     * {@code POST  /vaccination-infos} : Create a new vaccinationInfo.
     *
     * @param vaccinationInfoDTO the vaccinationInfoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vaccinationInfoDTO, or with status {@code 400 (Bad Request)} if the vaccinationInfo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/vaccination-infos/1")
    public ResponseEntity<VaccinationInfoDTO> createVaccinationInfo(@Valid @RequestBody VaccinationInfoDTO vaccinationInfoDTO)
        throws URISyntaxException {
        log.debug("REST request to save VaccinationInfo : {}", vaccinationInfoDTO);
        if (vaccinationInfoDTO.getId() != null) {
            throw new BadRequestAlertException("A new vaccinationInfo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VaccinationInfoDTO result = vaccinationInfoService.save(vaccinationInfoDTO);
        return ResponseEntity
            .created(new URI("/api/vaccination-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code POST  /case-infos} : Create a new list of vaccinationInfo.
     *
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new list of caseInfoDTO, or with status {@code 400 (Bad Request)} if the caseInfo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/vaccination-infos")
    public ResponseEntity<List<VaccinationInfoDTO>> createCaseInfos(@Valid @RequestBody List<VaccinationInfoDTO> VaccinationInfosDTO) throws URISyntaxException {
        log.debug("REST request to save VaccinationInfoList : []", VaccinationInfosDTO);

        List<VaccinationInfoDTO> result = vaccinationInfoService.saveAll(VaccinationInfosDTO);
        return ResponseEntity.ok().body(result);
    }


    /**
     * {@code PUT  /vaccination-infos/:id} : Updates an existing vaccinationInfo.
     *
     * @param id the id of the vaccinationInfoDTO to save.
     * @param vaccinationInfoDTO the vaccinationInfoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vaccinationInfoDTO,
     * or with status {@code 400 (Bad Request)} if the vaccinationInfoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vaccinationInfoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/vaccination-infos/{id}")
    public ResponseEntity<VaccinationInfoDTO> updateVaccinationInfo(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody VaccinationInfoDTO vaccinationInfoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update VaccinationInfo : {}, {}", id, vaccinationInfoDTO);
        if (vaccinationInfoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vaccinationInfoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vaccinationInfoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        VaccinationInfoDTO result = vaccinationInfoService.save(vaccinationInfoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, vaccinationInfoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /vaccination-infos/:id} : Partial updates given fields of an existing vaccinationInfo, field will ignore if it is null
     *
     * @param id the id of the vaccinationInfoDTO to save.
     * @param vaccinationInfoDTO the vaccinationInfoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vaccinationInfoDTO,
     * or with status {@code 400 (Bad Request)} if the vaccinationInfoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the vaccinationInfoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the vaccinationInfoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/vaccination-infos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VaccinationInfoDTO> partialUpdateVaccinationInfo(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody VaccinationInfoDTO vaccinationInfoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update VaccinationInfo partially : {}, {}", id, vaccinationInfoDTO);
        if (vaccinationInfoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vaccinationInfoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vaccinationInfoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VaccinationInfoDTO> result = vaccinationInfoService.partialUpdate(vaccinationInfoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, vaccinationInfoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /vaccination-infos} : get all the vaccinationInfos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vaccinationInfos in body.
     */
    @GetMapping("/vaccination-infos")
    public ResponseEntity<List<VaccinationInfoDTO>> getAllVaccinationInfos(VaccinationInfoCriteria criteria, Pageable pageable) {
        log.debug("REST request to get VaccinationInfos by criteria: {}", criteria);
        Page<VaccinationInfoDTO> page = vaccinationInfoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /vaccination-infos/count} : count all the vaccinationInfos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/vaccination-infos/count")
    public ResponseEntity<Long> countVaccinationInfos(VaccinationInfoCriteria criteria) {
        log.debug("REST request to count VaccinationInfos by criteria: {}", criteria);
        return ResponseEntity.ok().body(vaccinationInfoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /vaccination-infos/:id} : get the "id" vaccinationInfo.
     *
     * @param id the id of the vaccinationInfoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vaccinationInfoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/vaccination-infos/{id}")
    public ResponseEntity<VaccinationInfoDTO> getVaccinationInfo(@PathVariable Long id) {
        log.debug("REST request to get VaccinationInfo : {}", id);
        Optional<VaccinationInfoDTO> vaccinationInfoDTO = vaccinationInfoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vaccinationInfoDTO);
    }

    /**
     * {@code DELETE  /vaccination-infos/:id} : delete the "id" vaccinationInfo.
     *
     * @param id the id of the vaccinationInfoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/vaccination-infos/{id}")
    public ResponseEntity<Void> deleteVaccinationInfo(@PathVariable Long id) {
        log.debug("REST request to delete VaccinationInfo : {}", id);
        vaccinationInfoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
