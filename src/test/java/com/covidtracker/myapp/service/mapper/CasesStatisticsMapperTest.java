package com.covidtracker.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CasesStatisticsMapperTest {

    private CasesStatisticsMapper casesStatisticsMapper;

    @BeforeEach
    public void setUp() {
        casesStatisticsMapper = new CasesStatisticsMapperImpl();
    }
}
