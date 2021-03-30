package com.almaviva.vs.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.almaviva.vs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PhoneActivationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PhoneActivation.class);
        PhoneActivation phoneActivation1 = new PhoneActivation();
        phoneActivation1.setId(1L);
        PhoneActivation phoneActivation2 = new PhoneActivation();
        phoneActivation2.setId(phoneActivation1.getId());
        assertThat(phoneActivation1).isEqualTo(phoneActivation2);
        phoneActivation2.setId(2L);
        assertThat(phoneActivation1).isNotEqualTo(phoneActivation2);
        phoneActivation1.setId(null);
        assertThat(phoneActivation1).isNotEqualTo(phoneActivation2);
    }
}
