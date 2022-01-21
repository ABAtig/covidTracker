package com.covidtracker.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CaseInfoMapperTest {

    private CaseInfoMapper caseInfoMapper;

    @BeforeEach
    public void setUp() {
        caseInfoMapper = new CaseInfoMapperImpl();
    }
}
