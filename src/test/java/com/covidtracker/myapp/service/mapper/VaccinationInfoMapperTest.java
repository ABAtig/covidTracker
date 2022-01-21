package com.covidtracker.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VaccinationInfoMapperTest {

    private VaccinationInfoMapper vaccinationInfoMapper;

    @BeforeEach
    public void setUp() {
        vaccinationInfoMapper = new VaccinationInfoMapperImpl();
    }
}
