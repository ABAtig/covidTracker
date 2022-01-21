package com.covidtracker.myapp.service;

import com.covidtracker.myapp.service.dto.VaccinationInfoDTO;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.covidtracker.myapp.domain.VaccinationInfo}.
 */
public interface VaccinationInfoService {
    /**
     * Save a vaccinationInfo.
     *
     * @param vaccinationInfoDTO the entity to save.
     * @return the persisted entity.
     */
    VaccinationInfoDTO save(VaccinationInfoDTO vaccinationInfoDTO);
    /**
        * Save list vaccinationInfo.
     *
         * @return the list of entities.
        */
    List<VaccinationInfoDTO> saveAll(List<VaccinationInfoDTO> VaccinationInfosDTO);

    /**
     * Partially updates a vaccinationInfo.
     *
     * @param vaccinationInfoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<VaccinationInfoDTO> partialUpdate(VaccinationInfoDTO vaccinationInfoDTO);

    /**
     * Get all the vaccinationInfos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<VaccinationInfoDTO> findAll(Pageable pageable);

    /**
     * Get the "id" vaccinationInfo.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<VaccinationInfoDTO> findOne(Long id);

    /**
     * Delete the "id" vaccinationInfo.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
