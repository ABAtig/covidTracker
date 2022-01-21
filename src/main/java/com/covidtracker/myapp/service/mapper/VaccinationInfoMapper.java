package com.covidtracker.myapp.service.mapper;

import com.covidtracker.myapp.domain.VaccinationInfo;
import com.covidtracker.myapp.service.dto.VaccinationInfoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link VaccinationInfo} and its DTO {@link VaccinationInfoDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface VaccinationInfoMapper extends EntityMapper<VaccinationInfoDTO, VaccinationInfo> {}
