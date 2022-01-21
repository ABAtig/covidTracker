package com.covidtracker.myapp.service;

import com.covidtracker.myapp.service.dto.CaseInfoDTO;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.covidtracker.myapp.domain.CaseInfo}.
 */
public interface CaseInfoService {
    /**
     * Save a caseInfo.
     *
     * @param caseInfoDTO the entity to save.
     * @return the persisted entity.
     */
    CaseInfoDTO save(CaseInfoDTO caseInfoDTO);
    /**
     * Save list caseInfo.
     *
     * @return the list of entities.
     */
    List<CaseInfoDTO> saveAll(List<CaseInfoDTO> caseInfosDTO);

    /**
     * Partially updates a caseInfo.
     *
     * @param caseInfoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CaseInfoDTO> partialUpdate(CaseInfoDTO caseInfoDTO);

    /**
     * Get all the caseInfos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CaseInfoDTO> findAll(Pageable pageable);

    /**
     * Get the "id" caseInfo.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CaseInfoDTO> findOne(Long id);

    /**
     * Delete the "id" caseInfo.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
