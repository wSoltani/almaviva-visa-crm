package com.almaviva.vs.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.almaviva.vs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MandateTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Mandate.class);
        Mandate mandate1 = new Mandate();
        mandate1.setId(1L);
        Mandate mandate2 = new Mandate();
        mandate2.setId(mandate1.getId());
        assertThat(mandate1).isEqualTo(mandate2);
        mandate2.setId(2L);
        assertThat(mandate1).isNotEqualTo(mandate2);
        mandate1.setId(null);
        assertThat(mandate1).isNotEqualTo(mandate2);
    }
}
