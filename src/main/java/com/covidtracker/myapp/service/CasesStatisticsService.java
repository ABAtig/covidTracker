package com.covidtracker.myapp.service;

import com.covidtracker.myapp.domain.CaseInfo;
import com.covidtracker.myapp.service.dto.CasesStatisticsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.covidtracker.myapp.domain.CasesStatistics}.
 */
public interface CasesStatisticsService {
    /**
     * Save a casesStatistics.
     *
     * @param casesStatisticsDTO the entity to save.
     * @return the persisted entity.
     */
    CasesStatisticsDTO save(CasesStatisticsDTO casesStatisticsDTO);

    /**
     * Partially updates a casesStatistics.
     *
     * @param casesStatisticsDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CasesStatisticsDTO> partialUpdate(CasesStatisticsDTO casesStatisticsDTO);

    /**
     *  updates a casesStatistics after saving new list cases.
     *
     */

    void UpdateCasesStatisticsByNewCasesList(List<CaseInfo> CaseInfos);

    /**
     * Get all the casesStatistics.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CasesStatisticsDTO> findAll(Pageable pageable);

    /**
     * Get the "id" casesStatistics.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CasesStatisticsDTO> findOne(Long id);

    /**
     * Get the "day" casesStatistics.
     *
     * @param day the day of the entity.
     * @return the entity.
     */
    List<Object> findByDayForeachCountry(LocalDate day);

    /**
     * Get the "month" casesStatistics.
     *
     * @param month the month of the entity.
     * @return the entity.
     */
    List<Object> findByMonthForeachCountry(Integer month);

    /**
     * Get the "year" casesStatistics.
     *
     * @param year the year of the entity.
     * @return the entity.
     */
    List<Object> findByYearForeachCountry(Integer year);


    /**
     * Delete the "id" casesStatistics.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
