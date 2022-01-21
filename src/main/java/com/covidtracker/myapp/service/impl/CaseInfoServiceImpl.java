package com.covidtracker.myapp.service.impl;

import com.covidtracker.myapp.domain.CaseInfo;
import com.covidtracker.myapp.repository.CaseInfoRepository;
import com.covidtracker.myapp.service.CaseInfoService;
import com.covidtracker.myapp.service.CasesStatisticsService;
import com.covidtracker.myapp.service.dto.CaseInfoDTO;
import com.covidtracker.myapp.service.mapper.CaseInfoMapper;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CaseInfo}.
 */
@Service
@Transactional
public class CaseInfoServiceImpl implements CaseInfoService {

    private final Logger log = LoggerFactory.getLogger(CaseInfoServiceImpl.class);

    private final CaseInfoRepository caseInfoRepository;

    private final CaseInfoMapper caseInfoMapper;

    private final CasesStatisticsService casesStatisticsService;

    public CaseInfoServiceImpl(CaseInfoRepository caseInfoRepository, CaseInfoMapper caseInfoMapper, CasesStatisticsService casesStatisticsService) {
        this.caseInfoRepository = caseInfoRepository;
        this.caseInfoMapper = caseInfoMapper;
        this.casesStatisticsService = casesStatisticsService;
    }

    @Override
    public CaseInfoDTO save(CaseInfoDTO caseInfoDTO) {
        log.debug("Request to save CaseInfo : {}", caseInfoDTO);
        CaseInfo caseInfo = caseInfoMapper.toEntity(caseInfoDTO);
        caseInfo = caseInfoRepository.save(caseInfo);
        return caseInfoMapper.toDto(caseInfo);
    }

    @Override
    public List<CaseInfoDTO> saveAll(List<CaseInfoDTO> caseInfosDTO) {
        log.debug("Request to save CaseInfoList : {}", caseInfosDTO);
        List<CaseInfo> caseInfoList = caseInfoMapper.toEntity(caseInfosDTO);
        caseInfoList = caseInfoRepository.saveAll(caseInfoList);
        UpdateCasesStatisticsByNewCasesList(caseInfoList);
        return caseInfoMapper.toDto(caseInfoList);
    }

    public void UpdateCasesStatisticsByNewCasesList(List<CaseInfo> caseInfos){
        log.debug("Request to update casesStatistics");
        casesStatisticsService.UpdateCasesStatisticsByNewCasesList(caseInfos);
    }


    @Override
    public Optional<CaseInfoDTO> partialUpdate(CaseInfoDTO caseInfoDTO) {
        log.debug("Request to partially update CaseInfo : {}", caseInfoDTO);

        return caseInfoRepository
            .findById(caseInfoDTO.getId())
            .map(existingCaseInfo -> {
                caseInfoMapper.partialUpdate(existingCaseInfo, caseInfoDTO);

                return existingCaseInfo;
            })
            .map(caseInfoRepository::save)
            .map(caseInfoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CaseInfoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CaseInfos");
        return caseInfoRepository.findAll(pageable).map(caseInfoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CaseInfoDTO> findOne(Long id) {
        log.debug("Request to get CaseInfo : {}", id);
        return caseInfoRepository.findById(id).map(caseInfoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CaseInfo : {}", id);
        caseInfoRepository.deleteById(id);
    }
}
