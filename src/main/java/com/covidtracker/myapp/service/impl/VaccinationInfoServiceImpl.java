package com.covidtracker.myapp.service.impl;

import com.covidtracker.myapp.domain.VaccinationInfo;
import com.covidtracker.myapp.repository.VaccinationInfoRepository;
import com.covidtracker.myapp.service.VaccinationInfoService;
import com.covidtracker.myapp.service.dto.VaccinationInfoDTO;
import com.covidtracker.myapp.service.mapper.VaccinationInfoMapper;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link VaccinationInfo}.
 */
@Service
@Transactional
public class VaccinationInfoServiceImpl implements VaccinationInfoService {

    private final Logger log = LoggerFactory.getLogger(VaccinationInfoServiceImpl.class);

    private final VaccinationInfoRepository vaccinationInfoRepository;

    private final VaccinationInfoMapper vaccinationInfoMapper;

    public VaccinationInfoServiceImpl(VaccinationInfoRepository vaccinationInfoRepository, VaccinationInfoMapper vaccinationInfoMapper) {
        this.vaccinationInfoRepository = vaccinationInfoRepository;
        this.vaccinationInfoMapper = vaccinationInfoMapper;
    }

    @Override
    public VaccinationInfoDTO save(VaccinationInfoDTO vaccinationInfoDTO) {
        log.debug("Request to save VaccinationInfo : {}", vaccinationInfoDTO);
        VaccinationInfo vaccinationInfo = vaccinationInfoMapper.toEntity(vaccinationInfoDTO);
        vaccinationInfo = vaccinationInfoRepository.save(vaccinationInfo);
        return vaccinationInfoMapper.toDto(vaccinationInfo);
    }

    @Override
    public List<VaccinationInfoDTO> saveAll(List<VaccinationInfoDTO> VaccinationInfosDTO) {

        log.debug("Request to save CaseInfoList : {}", VaccinationInfosDTO);
        List<VaccinationInfo> caseInfoList = vaccinationInfoMapper.toEntity(VaccinationInfosDTO);
        caseInfoList = vaccinationInfoRepository.saveAll(caseInfoList);
        return vaccinationInfoMapper.toDto(caseInfoList);
    }

    @Override
    public Optional<VaccinationInfoDTO> partialUpdate(VaccinationInfoDTO vaccinationInfoDTO) {
        log.debug("Request to partially update VaccinationInfo : {}", vaccinationInfoDTO);

        return vaccinationInfoRepository
            .findById(vaccinationInfoDTO.getId())
            .map(existingVaccinationInfo -> {
                vaccinationInfoMapper.partialUpdate(existingVaccinationInfo, vaccinationInfoDTO);

                return existingVaccinationInfo;
            })
            .map(vaccinationInfoRepository::save)
            .map(vaccinationInfoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VaccinationInfoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all VaccinationInfos");
        return vaccinationInfoRepository.findAll(pageable).map(vaccinationInfoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<VaccinationInfoDTO> findOne(Long id) {
        log.debug("Request to get VaccinationInfo : {}", id);
        return vaccinationInfoRepository.findById(id).map(vaccinationInfoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete VaccinationInfo : {}", id);
        vaccinationInfoRepository.deleteById(id);
    }
}
