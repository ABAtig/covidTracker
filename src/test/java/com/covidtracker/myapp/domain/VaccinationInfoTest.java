package com.covidtracker.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.covidtracker.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VaccinationInfoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VaccinationInfo.class);
        VaccinationInfo vaccinationInfo1 = new VaccinationInfo();
        vaccinationInfo1.setId(1L);
        VaccinationInfo vaccinationInfo2 = new VaccinationInfo();
        vaccinationInfo2.setId(vaccinationInfo1.getId());
        assertThat(vaccinationInfo1).isEqualTo(vaccinationInfo2);
        vaccinationInfo2.setId(2L);
        assertThat(vaccinationInfo1).isNotEqualTo(vaccinationInfo2);
        vaccinationInfo1.setId(null);
        assertThat(vaccinationInfo1).isNotEqualTo(vaccinationInfo2);
    }
}
