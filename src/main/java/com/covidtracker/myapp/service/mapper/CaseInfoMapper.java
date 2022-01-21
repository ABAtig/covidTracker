package com.covidtracker.myapp.service.mapper;

import com.covidtracker.myapp.domain.CaseInfo;
import com.covidtracker.myapp.service.dto.CaseInfoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CaseInfo} and its DTO {@link CaseInfoDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CaseInfoMapper extends EntityMapper<CaseInfoDTO, CaseInfo> {}
