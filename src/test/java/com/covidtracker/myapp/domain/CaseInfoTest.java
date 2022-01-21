package com.covidtracker.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.covidtracker.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CaseInfoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CaseInfo.class);
        CaseInfo caseInfo1 = new CaseInfo();
        caseInfo1.setId(1L);
        CaseInfo caseInfo2 = new CaseInfo();
        caseInfo2.setId(caseInfo1.getId());
        assertThat(caseInfo1).isEqualTo(caseInfo2);
        caseInfo2.setId(2L);
        assertThat(caseInfo1).isNotEqualTo(caseInfo2);
        caseInfo1.setId(null);
        assertThat(caseInfo1).isNotEqualTo(caseInfo2);
    }
}
