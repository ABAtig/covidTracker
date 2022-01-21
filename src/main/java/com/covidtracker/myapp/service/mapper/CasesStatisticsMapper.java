package com.covidtracker.myapp.service.mapper;

import com.covidtracker.myapp.domain.CasesStatistics;
import com.covidtracker.myapp.service.dto.CasesStatisticsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CasesStatistics} and its DTO {@link CasesStatisticsDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CasesStatisticsMapper extends EntityMapper<CasesStatisticsDTO, CasesStatistics> {}
