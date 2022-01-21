package com.covidtracker.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.covidtracker.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CaseInfoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CaseInfoDTO.class);
        CaseInfoDTO caseInfoDTO1 = new CaseInfoDTO();
        caseInfoDTO1.setId(1L);
        CaseInfoDTO caseInfoDTO2 = new CaseInfoDTO();
        assertThat(caseInfoDTO1).isNotEqualTo(caseInfoDTO2);
        caseInfoDTO2.setId(caseInfoDTO1.getId());
        assertThat(caseInfoDTO1).isEqualTo(caseInfoDTO2);
        caseInfoDTO2.setId(2L);
        assertThat(caseInfoDTO1).isNotEqualTo(caseInfoDTO2);
        caseInfoDTO1.setId(null);
        assertThat(caseInfoDTO1).isNotEqualTo(caseInfoDTO2);
    }
}
