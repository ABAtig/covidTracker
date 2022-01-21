package com.covidtracker.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.covidtracker.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CasesStatisticsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CasesStatistics.class);
        CasesStatistics casesStatistics1 = new CasesStatistics();
        casesStatistics1.setId(1L);
        CasesStatistics casesStatistics2 = new CasesStatistics();
        casesStatistics2.setId(casesStatistics1.getId());
        assertThat(casesStatistics1).isEqualTo(casesStatistics2);
        casesStatistics2.setId(2L);
        assertThat(casesStatistics1).isNotEqualTo(casesStatistics2);
        casesStatistics1.setId(null);
        assertThat(casesStatistics1).isNotEqualTo(casesStatistics2);
    }
}
