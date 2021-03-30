package com.almaviva.vs.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.almaviva.vs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VisaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Visa.class);
        Visa visa1 = new Visa();
        visa1.setId(1L);
        Visa visa2 = new Visa();
        visa2.setId(visa1.getId());
        assertThat(visa1).isEqualTo(visa2);
        visa2.setId(2L);
        assertThat(visa1).isNotEqualTo(visa2);
        visa1.setId(null);
        assertThat(visa1).isNotEqualTo(visa2);
    }
}
