package com.covidtracker.myapp.service.impl;

import com.covidtracker.myapp.domain.CaseInfo;
import com.covidtracker.myapp.domain.CasesStatistics;
import com.covidtracker.myapp.domain.enumeration.EnumCountry;
import com.covidtracker.myapp.repository.CasesStatisticsRepository;
import com.covidtracker.myapp.service.CasesStatisticsService;
import com.covidtracker.myapp.service.dto.CasesStatisticsDTO;
import com.covidtracker.myapp.service.mapper.CasesStatisticsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link CasesStatistics}.
 */
@Service
@Transactional
public class CasesStatisticsServiceImpl implements CasesStatisticsService {

    private final Logger log = LoggerFactory.getLogger(CasesStatisticsServiceImpl.class);

    private final CasesStatisticsRepository casesStatisticsRepository;

    private final CasesStatisticsMapper casesStatisticsMapper;

    public CasesStatisticsServiceImpl(CasesStatisticsRepository casesStatisticsRepository, CasesStatisticsMapper casesStatisticsMapper) {
        this.casesStatisticsRepository = casesStatisticsRepository;
        this.casesStatisticsMapper = casesStatisticsMapper;
    }

    @Override
    public CasesStatisticsDTO save(CasesStatisticsDTO casesStatisticsDTO) {
        log.debug("Request to save CasesStatistics : {}", casesStatisticsDTO);
        CasesStatistics casesStatistics = casesStatisticsMapper.toEntity(casesStatisticsDTO);
        casesStatistics = casesStatisticsRepository.save(casesStatistics);
        return casesStatisticsMapper.toDto(casesStatistics);
    }

    @Override
    public Optional<CasesStatisticsDTO> partialUpdate(CasesStatisticsDTO casesStatisticsDTO) {
        log.debug("Request to partially update CasesStatistics : {}", casesStatisticsDTO);

        return casesStatisticsRepository
            .findById(casesStatisticsDTO.getId())
            .map(existingCasesStatistics -> {
                casesStatisticsMapper.partialUpdate(existingCasesStatistics, casesStatisticsDTO);

                return existingCasesStatistics;
            })
            .map(casesStatisticsRepository::save)
            .map(casesStatisticsMapper::toDto);
    }

    @Override
    public void UpdateCasesStatisticsByNewCasesList(List<CaseInfo> caseInfos) {
        Map<Pair<EnumCountry, LocalDate>, List<CaseInfo>> DailyCountriesNewCasescounts =
            caseInfos.stream().collect(Collectors.groupingBy( (CaseInfo c) -> Pair.of(c.getCountry(), c.getTestDate().toLocalDate())));


//        Map<EnumCountry, Long> countriesNewCasescounts =
//            caseInfos.stream().collect(Collectors.groupingBy(CaseInfo::getCountry, Collectors.counting()));

        DailyCountriesNewCasescounts.forEach((pairCountryDate, newCases) -> {
                Optional<CasesStatistics> casesStatisticsByCountry = casesStatisticsRepository.findByCountryAndDate(pairCountryDate.getFirst(), pairCountryDate.getSecond());
                if (casesStatisticsByCountry.isPresent()) {
                    Double newTotalCasesNumber = Double.sum(casesStatisticsByCountry.get().getTotalCasesNumber(), newCases.size());
                    casesStatisticsByCountry.get().setTotalCasesNumber(newTotalCasesNumber);
                    casesStatisticsByCountry.get().setDate(pairCountryDate.getSecond());
                    casesStatisticsByCountry.get().setMonth(pairCountryDate.getSecond().getMonthValue());
                    casesStatisticsByCountry.get().setYear(pairCountryDate.getSecond().getYear());
                    casesStatisticsRepository.save(casesStatisticsByCountry.get());
                } else {
                    CasesStatistics newCasesStatistics = new CasesStatistics();
                    newCasesStatistics.setCountry(pairCountryDate.getFirst());
                    newCasesStatistics.setTotalCasesNumber(Long.valueOf(newCases.size()).doubleValue());
                    newCasesStatistics.setDate(pairCountryDate.getSecond());
                    newCasesStatistics.setMonth(pairCountryDate.getSecond().getMonthValue());
                    newCasesStatistics.setYear(pairCountryDate.getSecond().getYear());
                    casesStatisticsRepository.save(newCasesStatistics);
                }
            }
        );
        log.info("CasesStatistics is updated");
    }


    @Override
    @Transactional(readOnly = true)
    public Page<CasesStatisticsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CasesStatistics");
        return casesStatisticsRepository.findAll(pageable).map(casesStatisticsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CasesStatisticsDTO> findOne(Long id) {
        log.debug("Request to get CasesStatistics : {}", id);
        return casesStatisticsRepository.findById(id).map(casesStatisticsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object> findByDayForeachCountry(LocalDate day) {
        log.debug("Request to get all CasesStatistics report by day foreach country");
        return casesStatisticsRepository.findByDate(day);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object> findByMonthForeachCountry(Integer month) {
        log.debug("Request to get all CasesStatistics report by month foreach country");
        return casesStatisticsRepository.findByMonth(month);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object> findByYearForeachCountry(Integer year) {
        log.debug("Request to get all CasesStatistics report by year foreach country");
        return casesStatisticsRepository.findByYear(year);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CasesStatistics : {}", id);
        casesStatisticsRepository.deleteById(id);
    }
}
