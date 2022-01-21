package com.covidtracker.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.covidtracker.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VaccinationInfoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VaccinationInfoDTO.class);
        VaccinationInfoDTO vaccinationInfoDTO1 = new VaccinationInfoDTO();
        vaccinationInfoDTO1.setId(1L);
        VaccinationInfoDTO vaccinationInfoDTO2 = new VaccinationInfoDTO();
        assertThat(vaccinationInfoDTO1).isNotEqualTo(vaccinationInfoDTO2);
        vaccinationInfoDTO2.setId(vaccinationInfoDTO1.getId());
        assertThat(vaccinationInfoDTO1).isEqualTo(vaccinationInfoDTO2);
        vaccinationInfoDTO2.setId(2L);
        assertThat(vaccinationInfoDTO1).isNotEqualTo(vaccinationInfoDTO2);
        vaccinationInfoDTO1.setId(null);
        assertThat(vaccinationInfoDTO1).isNotEqualTo(vaccinationInfoDTO2);
    }
}
