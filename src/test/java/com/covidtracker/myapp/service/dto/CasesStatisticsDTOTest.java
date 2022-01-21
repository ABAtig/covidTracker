package com.covidtracker.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.covidtracker.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CasesStatisticsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CasesStatisticsDTO.class);
        CasesStatisticsDTO casesStatisticsDTO1 = new CasesStatisticsDTO();
        casesStatisticsDTO1.setId(1L);
        CasesStatisticsDTO casesStatisticsDTO2 = new CasesStatisticsDTO();
        assertThat(casesStatisticsDTO1).isNotEqualTo(casesStatisticsDTO2);
        casesStatisticsDTO2.setId(casesStatisticsDTO1.getId());
        assertThat(casesStatisticsDTO1).isEqualTo(casesStatisticsDTO2);
        casesStatisticsDTO2.setId(2L);
        assertThat(casesStatisticsDTO1).isNotEqualTo(casesStatisticsDTO2);
        casesStatisticsDTO1.setId(null);
        assertThat(casesStatisticsDTO1).isNotEqualTo(casesStatisticsDTO2);
    }
}
